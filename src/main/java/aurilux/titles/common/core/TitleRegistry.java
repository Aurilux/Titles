package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.network.messages.PacketSyncDatapack;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Rarity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TitleRegistry extends JsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, Title> titles = new HashMap<>();
    private final Map<ResourceLocation, Title> contributorTitles = new HashMap<>();

    private static final TitleRegistry INSTANCE = new TitleRegistry();

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
    protected void apply(Map<ResourceLocation, JsonElement> dataFromMods, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        profilerIn.startSection("titleLoader");

        Set<String> modsWithNativeTitles = determineModsWithNativeTitles();
        TitlesMod.LOG.debug("Mods with native titles: {}", Arrays.asList(modsWithNativeTitles.toArray()));
        dataFromMods.forEach((location, element) -> {
            try {
                JsonObject titlejson = JSONUtils.getJsonObject(element, "title");
                ResourceLocation processedLocation = processTemplateResource(location, modsWithNativeTitles);
                Title title = loadTitle(processedLocation, titlejson);
                titles.put(processedLocation, title);
            }
            catch (IllegalArgumentException | JsonParseException ex) {
                TitlesMod.LOG.error("Parsing error loading title {}: {}", location, ex.getMessage());
            }
        });
        TitlesMod.LOG.debug("Loaded {} titles", titles.size());
        titles.putAll(contributorTitles);

        profilerIn.endSection();
    }

    private Set<String> determineModsWithNativeTitles() {
        Set<String> set = new HashSet<>();
        for (ModInfo modInfo : ModList.get().getMods()) {
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

    private ResourceLocation processTemplateResource(ResourceLocation location, Set<String> modsWithTitles) {
        String path = location.getPath();
        boolean isTemplate = location.getNamespace().equals(TitlesMod.MOD_ID) && path.startsWith("_");
        if (!isTemplate) {
            return location;
        }

        int slashIndex = path.indexOf("/");
        String possibleModId = path.substring(1, slashIndex);
        if (!modsWithTitles.contains(possibleModId)) {
            String trimmedPath = path.substring(slashIndex + 1);
            return new ResourceLocation(possibleModId, trimmedPath);
        }
        return location;
    }

    public Title loadTitle(ResourceLocation res, JsonObject json) {
        json.addProperty("id", res.toString());
        Title.Builder builder = Title.Builder.deserialize(json);

        // EPIC rarity titles are reserved for contributors only. No cheating!
        Rarity rarityRef = builder.getRarity();
        if (rarityRef.equals(Rarity.EPIC)) {
            builder.rarity(Rarity.COMMON);
        }

        return builder.build();
    }

    public Map<ResourceLocation, Title> getTitles() {
        return new HashMap<>(titles);
    }

    @OnlyIn(Dist.CLIENT)
    public void processServerData(PacketSyncDatapack msg) {
        titles.clear();
        titles.putAll(msg.getAllLoadedTitles());
        TitlesMod.LOG.debug("Synced {} titles from server", titles.size());
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