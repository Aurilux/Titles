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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class TitleManager extends JsonReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
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
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        profilerIn.startSection("titleLoader");
        Map<ResourceLocation, JsonObject> filteredAndMapped = objectIn.entrySet().stream()
                .filter(e -> !e.getKey().getPath().startsWith("_") && e.getValue().isJsonObject())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getAsJsonObject()));

        Map<Title.AwardType, ImmutableMap.Builder<ResourceLocation, Title>> processed = Maps.newHashMap();
        Set<String> modsWithTitles = new HashSet<>();
        Path titlesPath = getTitleDataPath(TitlesAPI.MOD_ID);
        String[] templateFolders = titlesPath.toFile().list((current, name) -> new File(current, name).isDirectory());

        LOGGER.debug("Title sub directories are: {}, {}", titlesPath, Arrays.toString(templateFolders));
        filteredAndMapped.forEach((res, json) -> {
            LOGGER.debug("ResourceLocation is: {}", res);
            // Grab the title templates for other mods, primarily minecraft. If a mod has the titles data folder, my
            // template is ignored.
            ResourceLocation actualLoc = res;
            if (res.getNamespace().equals("titles") && res.getPath().contains("/")) {
                List<String> parts = Arrays.asList(res.getPath().split("/"));
                String possibleModId = parts.get(0);
                if (templateFolders != null && Arrays.asList(templateFolders).contains(possibleModId)) {
                    LOGGER.debug("Template folder exists for: {}", res);
                    if (modsWithTitles.contains(possibleModId)) {
                        return;
                    }
                    else {
                        Path modTitlesPath = getTitleDataPath(possibleModId);
                        if (modTitlesPath != null && Files.exists(modTitlesPath)) {
                            modsWithTitles.add(possibleModId);
                            return;
                        }
                        else {
                            actualLoc = new ResourceLocation(parts.get(0), String.join("/", parts.subList(1, parts.size())));
                            LOGGER.debug("Template exists and is not overwritten by actual mod: {}, {}", possibleModId, actualLoc);
                        }
                    }
                }
            }

            try {
                Title title = Title.Builder.deserializeJSON(actualLoc, json);
                processed.computeIfAbsent(title.getType(), t -> ImmutableMap.builder()).put(actualLoc, title);
            }
            catch (IllegalArgumentException | JsonParseException ex) {
                LOGGER.error("Parsing error loading title json {}", actualLoc, ex);
            }
        });

        processed.computeIfAbsent(Title.AwardType.CONTRIBUTOR,  t -> ImmutableMap.builder()).putAll(contributorTitles);
        this.titles = processed.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey,
                (titleEntry) -> titleEntry.getValue().build()));
        LOGGER.info("Loaded {} titles", processed.size());
        profilerIn.endSection();
    }

    private Path getTitleDataPath(String modId) {
        ModFileInfo modFileInfo = ModList.get().getModFileById(modId);
        if (modFileInfo != null) {
            return modFileInfo.getFile().getFilePath().resolve(String.format("data/%s/titles", modId));
        }
        return null;
    }

    public Title getTitle(ResourceLocation titleKey) {
        return getRegisteredTitles().getOrDefault(titleKey, Title.NULL_TITLE);
    }

    public Map<ResourceLocation, Title> getAdvancementTitles() {
        return titles.get(Title.AwardType.ADVANCEMENT);
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
                    TitlesMod.LOG.debug("Loaded contributor title {}", title.toString());
                    contributorTitles.put(res, title);
                }
            }
            catch (IOException e) {
                TitlesMod.LOG.warn("Unable to load contributors list. Most likely you're offline or github is down.");
            }
        }
    }
}