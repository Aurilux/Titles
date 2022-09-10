package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TitleRegistry extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private Map<ResourceLocation, Title> titles = Collections.emptyMap();
    private final Map<ResourceLocation, Title> contributorTitles = new HashMap<>();

    private static final TitleRegistry INSTANCE = new TitleRegistry();;

    private TitleRegistry() {
        super(GSON, TitlesMod.MOD_ID);
    }

    public static TitleRegistry get() {
        return INSTANCE;
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(INSTANCE);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> dataFromMods, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        profilerIn.push("titleLoader");

        Set<String> modsWithNativeTitles = determineModsWithNativeTitles();
        TitlesMod.LOG.debug("Mods with native titles: {}", Arrays.asList(modsWithNativeTitles.toArray()));
        titles = dataFromMods.entrySet().stream()
                .filter(entry -> entry.getValue().isJsonObject())
                .map(entry -> processTemplateResources(entry, modsWithNativeTitles))
                .filter(Objects::nonNull)
                .map(entry -> deserializeTitleJSON(entry.getKey(), entry.getValue().getAsJsonObject(), true))
                .collect(Collectors.toMap(
                        Title::getID,
                        Function.identity()
                ));
        TitlesMod.LOG.debug("Loaded {} titles", titles.size());
        titles.putAll(contributorTitles);

        profilerIn.pop();
    }

    private Set<String> determineModsWithNativeTitles() {
        Set<String> set = new HashSet<>();
        for (IModInfo modInfo : ModList.get().getMods()) {
            String modId = modInfo.getModId();
            if (modId.equals("minecraft") || modId.equals("forge") || modId.equals(TitlesMod.MOD_ID)) {
                continue;
            }
            Path modTitlesPath = modInfo.getOwningFile().getFile()
                    .findResource(String.format("data/%s/%s", modId, TitlesMod.MOD_ID));
            if (Files.exists(modTitlesPath)) {
                set.add(modId);
            }
        }
        return set;
    }

    private Map.Entry<ResourceLocation, JsonElement> processTemplateResources(
            Map.Entry<ResourceLocation, JsonElement> entry, Set<String> modsWithTitles) {
        ResourceLocation oldResource = entry.getKey();
        String path = oldResource.getPath();

        boolean isTemplate = oldResource.getNamespace().equals(TitlesMod.MOD_ID) && path.startsWith("_");
        if (!isTemplate) return entry;

        int slashIndex = path.indexOf("/");
        String possibleModId = path.substring(1, slashIndex);
        if (!modsWithTitles.contains(possibleModId)) {
            String trimmedPath = path.substring(slashIndex + 1);
            ResourceLocation newResource = new ResourceLocation(possibleModId, trimmedPath);
            return new AbstractMap.SimpleEntry<>(newResource, entry.getValue());
        }
        return null;
    }

    public Title deserializeTitleJSON(ResourceLocation res, JsonObject json, boolean initialLoad) {
        Title.Builder builder = Title.Builder.create(res.getNamespace())
                .type(Title.AwardType.valueOf(GsonHelper.getAsString(json, "type").toUpperCase()))
                .id(res);

        Rarity testRarity = Rarity.valueOf(GsonHelper.getAsString(json, "rarity").toUpperCase());
        // EPIC rarity titles are reserved for contributors only
        boolean isTryingToCheat = initialLoad && testRarity.equals(Rarity.EPIC);
        builder.rarity(isTryingToCheat ? Rarity.COMMON : testRarity);

        builder.defaultDisplay(GsonHelper.getAsString(json, "defaultDisplay"));
        if (json.has("variantDisplay")) {
            builder.variantDisplay(GsonHelper.getAsString(json, "variantDisplay"));
        }
        if (json.has("flavorText")) {
            builder.flavorText(GsonHelper.getAsString(json, "flavorText"));
        }

        return new Title(builder);
    }

    public Map<ResourceLocation, Title> getTitles() {
        return new HashMap<>(titles);
    }

    @OnlyIn(Dist.CLIENT)
    public void processServerData(Map<ResourceLocation, Title> serverData) {
        titles = serverData;
    }

    public void loadContributors() {
        Thread thread = new Thread(this::fetchContributors);
        thread.setName("Titles Contributor Title Thread");
        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(TitlesMod.LOG));
        thread.start();
    }

    private void fetchContributors() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/Aurilux/Titles/master/contributors.properties");
            Properties props = new Properties();
            InputStreamReader reader = new InputStreamReader(url.openStream());
            props.load(reader);
            createContributorTitles(props);
        }
        catch (IOException e) {
            TitlesMod.LOG.info("Unable to load contributors list. Most likely you're offline or github is down.");
        }
    }

    private void createContributorTitles(Properties props) {
        Title.Builder contributorBuilder = Title.Builder.create(TitlesMod.MOD_ID)
                .type(Title.AwardType.CONTRIBUTOR)
                .rarity(Rarity.EPIC);
        for(String contributorName : props.stringPropertyNames()) {
            String contributorTitle = props.getProperty(contributorName);
            contributorBuilder.id(TitlesMod.prefix(contributorName.toLowerCase(Locale.ROOT)))
                    .defaultDisplay(contributorTitle);
            Title title = contributorBuilder.build();
            contributorTitles.put(title.getID(), title);
        }
        TitlesMod.LOG.info("Loaded {} contributor titles", contributorTitles.size());
    }
}