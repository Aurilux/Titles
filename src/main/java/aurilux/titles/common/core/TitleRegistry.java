package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TitleRegistry {
    public static final TitleRegistry INSTANCE = new TitleRegistry();

    private final Map<String, Title> objectiveTitles = new HashMap<>();
    private final Map<String, Title> lootTitles = new HashMap<>();

    // I need two maps because contributor titles can be referenced by either a username or by title key
    private final Map<String, Title> contributorTitlesByUsername = new HashMap<>();
    private final Map<String, Title> contributorTitlesByTitleKey = new HashMap<>();

    private TitleRegistry() {}

    public Title getTitle(String titleKey) {
        return objectiveTitles.getOrDefault(titleKey,
               lootTitles.getOrDefault(titleKey,
               contributorTitlesByUsername.getOrDefault(titleKey,
               contributorTitlesByTitleKey.getOrDefault(titleKey,
                       Title.NULL_TITLE))));
    }

    public Map<String, Title> getObjectiveTitles() {
        return new HashMap<>(objectiveTitles);
    }

    public Map<String, Title> getLootTitles() {
        return new HashMap<>(lootTitles);
    }

    public Map<String, Title> getRegisteredTitles() {
        Map<String, Title> temp = new HashMap<>();
        temp.putAll(objectiveTitles);
        temp.putAll(lootTitles);
        return temp;
    }

    public void init() {
        new ThreadContributorLoader();
        loadLootTitles();
    }

    public void registerTitle(Rarity rarity, String titleKey) {
        objectiveTitles.put(titleKey, new Title(titleKey, rarity));
    }

    private void loadLootTitles() {
        Optional<? extends ModContainer> container = ModList.get().getModContainerById(TitlesAPI.MOD_ID);
            container.ifPresent(c -> {
            try {
                Class<?> ownerClass = c.getMod().getClass();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ownerClass.getResourceAsStream("/data/titles/loot_titles.json")));
                JsonObject titleData = new JsonParser().parse(reader).getAsJsonObject();
                if (titleData != null) {
                    for (Map.Entry<String, JsonElement> entry : titleData.entrySet()) {
                        Rarity titleRarity = Rarity.valueOf(entry.getKey().toUpperCase());
                        //I reserve the EPIC title rarity for my contributors. This prevents people from being sneaky.
                        if (!titleRarity.equals(Rarity.EPIC)) {
                            JsonArray titles = entry.getValue().getAsJsonArray();
                            for (int i = 0; i < titles.size(); i++) {
                                String key = titles.get(i).getAsString();
                                lootTitles.put(key, new Title(key, titleRarity));
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {
                TitlesMod.LOG.error("Failed to load loot titles", ex);
            }
        });
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
                    contributorTitlesByUsername.put(key, new Title(value, Rarity.EPIC));
                    contributorTitlesByTitleKey.put(value, new Title(value, Rarity.EPIC));
                }
            }
            catch (IOException e) {
                TitlesMod.LOG.warn("Unable to load contributors list. Most likely you're offline or github is down.");
            }
        }
    }
}