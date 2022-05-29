package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Rarity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class TitleManager extends JsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private Map<Title.AwardType, Map<ResourceLocation, Title>> titles = ImmutableMap.of();
    private final Map<ResourceLocation, Title> contributorTitles = new HashMap<>();

    public static final TitleManager INSTANCE = new TitleManager();

    private TitleManager() {
        super(GSON, "titles");
    }

    public void init() {
        new ThreadContributorLoader();
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(INSTANCE);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> dataFromMods, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        profilerIn.startSection("titleLoader");

        TitlesMod.LOG.info("Object in: " + Arrays.toString(dataFromMods.entrySet().toArray()));

        Map<ResourceLocation, JsonObject> filteredAndMapped = dataFromMods.entrySet().stream()
                .filter(e -> !e.getKey().getPath().startsWith("_") && e.getValue().isJsonObject())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getAsJsonObject()));

        TitlesMod.LOG.info("Filtered and mapped: " + Arrays.toString(filteredAndMapped.entrySet().toArray()));

        Map<Title.AwardType, ImmutableMap.Builder<ResourceLocation, Title>> processed = Maps.newHashMap();
        Set<String> modsWithTitles = new HashSet<>();
        Path titlesPath = getTitleDataPath(TitlesAPI.MOD_ID);
        String[] templateFolders = titlesPath.toFile().list((current, name) -> new File(current, name).isDirectory());

        TitlesMod.LOG.debug("Title template directories are: {}", Arrays.toString(templateFolders));
        filteredAndMapped.forEach((res, json) -> {
            // Grab the title templates for other mods, primarily minecraft. If a mod has the titles data folder, the
            // template folder is ignored.
            ResourceLocation actualLoc = res;
            if (res.getNamespace().equals("titles") && res.getPath().contains("/")) {
                List<String> parts = Arrays.asList(res.getPath().split("/"));
                String possibleModId = parts.get(0);
                if (templateFolders != null && Arrays.asList(templateFolders).contains(possibleModId)) {
                    TitlesMod.LOG.debug("Template folder exists for: {}", possibleModId);
                    if (modsWithTitles.contains(possibleModId)) {
                        TitlesMod.LOG.debug("Mod '{}' has it's own titles. Ignoring template folder", possibleModId);
                        return;
                    }
                    else {
                        Path modTitlesPath = getTitleDataPath(possibleModId);
                        if (modTitlesPath != null && Files.exists(modTitlesPath)) {
                            TitlesMod.LOG.debug("Mod '{}' has it's own titles. Ignoring template folder", possibleModId);
                            modsWithTitles.add(possibleModId);
                            return;
                        }
                        else {
                            actualLoc = new ResourceLocation(parts.get(0), String.join("/", parts.subList(1, parts.size())));
                            TitlesMod.LOG.debug("Template exists and is not overwritten by actual mod: {}, {}", possibleModId, actualLoc);
                        }
                    }
                }
            }

            try {
                Title title = deserializeTitleJSON(actualLoc, json);
                processed.computeIfAbsent(title.getType(), t -> ImmutableMap.builder()).put(actualLoc, title);
            }
            catch (IllegalArgumentException | JsonParseException ex) {
                TitlesMod.LOG.error("Parsing error loading title json {}", actualLoc, ex);
            }
        });

        processed.computeIfAbsent(Title.AwardType.CONTRIBUTOR,  t -> ImmutableMap.builder()).putAll(contributorTitles);
        resolveLoadedData(processed);

        profilerIn.endSection();
    }

    private Path getTitleDataPath(String modId) {
        ModFileInfo modFileInfo = ModList.get().getModFileById(modId);
        if (modFileInfo != null) {
            return modFileInfo.getFile().getFilePath().resolve(String.format("data/%s/titles", modId));
        }
        return null;
    }

    public Title deserializeTitleJSON(ResourceLocation res, JsonObject json) {
        Title.Builder builder = Title.Builder.create(Title.AwardType.valueOf(JSONUtils.getString(json, "type").toUpperCase()))
                .id(res);

        Rarity testRarity = Rarity.valueOf(JSONUtils.getString(json, "rarity").toUpperCase());
        builder.rarity(testRarity.equals(Rarity.EPIC) ? Rarity.COMMON : testRarity);

        JsonObject display = JSONUtils.getJsonObject(json, "display");
        builder.defaultDisplay(JSONUtils.getString(display, "default"));
        if (json.has("variant")) {
            builder.variantDisplay(JSONUtils.getString(display, "variant"));
        }
        if (json.has("flavor")) {
            builder.flavorText(JSONUtils.getString(display, "flavor"));
        }

        return new Title(builder);
    }

    public Title getTitle(ResourceLocation titleKey) {
        return getRegisteredTitles().getOrDefault(titleKey, Title.NULL_TITLE);
    }

    public Map<ResourceLocation, Title> getAdvancementTitles() {
        return titles.get(Title.AwardType.ADVANCEMENT);
    }

    public Map<ResourceLocation, Title> getCommandTitles() {
        return titles.get(Title.AwardType.COMMAND);
    }

    public Map<ResourceLocation, Title> getContributorTitles() {
        return titles.get(Title.AwardType.CONTRIBUTOR);
    }

    public Map<ResourceLocation, Title> getLootTitles() {
        return titles.get(Title.AwardType.LOOT);
    }

    public Map<ResourceLocation, Title> getRegisteredTitles() {
        return titles.values().stream().flatMap(e -> e.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @OnlyIn(Dist.CLIENT)
    public void processServerData(Map<ResourceLocation, Title> serverData) {
        Map<Title.AwardType, ImmutableMap.Builder<ResourceLocation, Title>> temp = new HashMap<>();
        for (Map.Entry<ResourceLocation, Title> entry : serverData.entrySet()) {
            temp.computeIfAbsent(entry.getValue().getType(), t -> ImmutableMap.builder()).put(entry.getKey(), entry.getValue());
        }
        this.titles = temp.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey,
                (titleEntry) -> titleEntry.getValue().build()));
    }

    private void resolveLoadedData(Map<Title.AwardType, ImmutableMap.Builder<ResourceLocation, Title>> toResolve) {
        this.titles = toResolve.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey,
                (titleEntry) -> titleEntry.getValue().build()));
        TitlesMod.LOG.info("Loaded {} titles", (int) titles.values().stream().mapToLong(m -> m.values().size()).sum());
    }

    private class ThreadContributorLoader extends Thread {
        private ThreadContributorLoader() {
            setName("Titles Contributor Loader");
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Aurilux/Titles/master/contributors.properties");
                Properties props = new Properties();
                InputStreamReader reader = new InputStreamReader(url.openStream());
                props.load(reader);

                for(String key : props.stringPropertyNames()) {
                    String value = props.getProperty(key);
                    ResourceLocation res = new ResourceLocation("titles:" + key.toLowerCase());
                    Title title = new Title(
                            Title.Builder.create(Title.AwardType.CONTRIBUTOR)
                                    .id(res)
                                    .rarity(Rarity.EPIC)
                                    .defaultDisplay(value));
                    contributorTitles.put(res, title);
                }
            }
            catch (IOException e) {
                TitlesMod.LOG.warn("Unable to load contributors list. Most likely you're offline or github is down.");
            }
        }
    }
}