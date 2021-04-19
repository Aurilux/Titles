package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class TitleRegistry {
    public static final TitleRegistry INSTANCE = new TitleRegistry();

    private final String[] acceptedDataFiles = new String[]{"advancement_linked.json", "command_only.json", "loot_titles.json"};

    private final Map<String, Title> commandTitles = new HashMap<>();
    private final Map<String, Title> advancementTitles = new HashMap<>();
    private final Map<String, Title> lootTitles = new HashMap<>();

    // I need two maps because contributor titles can be referenced by either a username or by title key
    private final Map<String, Title> contributorTitlesByUsername = new HashMap<>();
    private final Map<String, Title> contributorTitlesByTitleKey = new HashMap<>();

    private TitleRegistry() {}

    public Title getTitle(String titleKey) {
        return advancementTitles.getOrDefault(titleKey,
                commandTitles.getOrDefault(titleKey,
                lootTitles.getOrDefault(titleKey,
                contributorTitlesByUsername.getOrDefault(titleKey,
                contributorTitlesByTitleKey.getOrDefault(titleKey,
                   Title.NULL_TITLE)))));
    }

    public Map<String, Title> getAdvancementTitles() {
        return new HashMap<>(advancementTitles);
    }

    public Map<String, Title> getLootTitles() {
        return new HashMap<>(lootTitles);
    }

    public Map<String, Title> getRegisteredTitles() {
        Map<String, Title> temp = new HashMap<>();
        temp.putAll(advancementTitles);
        temp.putAll(lootTitles);
        return temp;
    }

    public void init() {
        new ThreadContributorLoader();
        loadTitlesData();
    }

    public void registerTitle(Rarity rarity, String titleKey) {
        advancementTitles.put(titleKey, new Title(titleKey, rarity));
    }

    private void loadTitlesData() {
        Optional<? extends ModContainer> container = ModList.get().getModContainerById(TitlesAPI.MOD_ID);
            container.ifPresent(c -> {
            try {
                Class<?> ownerClass = c.getMod().getClass();
                List<Map<String, Title>> maps = Arrays.asList(advancementTitles, commandTitles, lootTitles);
                for (int i = 0; i < acceptedDataFiles.length; i++) {
                    String fileName = acceptedDataFiles[i];
                    Map<String, Title> correspondingMap = maps.get(i);
                    InputStream inputFile = ownerClass.getResourceAsStream("/data/titles/titles/" + fileName);
                    if (inputFile != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile));
                        JsonObject titleData = new JsonParser().parse(reader).getAsJsonObject();
                        if (titleData != null) {
                            for (Map.Entry<String, JsonElement> entry : titleData.entrySet()) {
                                Rarity titleRarity = Rarity.valueOf(entry.getKey().toUpperCase());
                                // I reserve the EPIC title rarity for my contributors. This prevents people from being sneaky.
                                if (!titleRarity.equals(Rarity.EPIC)) {
                                    // Advancement titles will always be handled first (i == 0).
                                    // With the advancements data, a key must be explicitly given, so it is handled differently
                                    if (i == 0) {
                                        for (Map.Entry<String, JsonElement> entry1 : entry.getValue().getAsJsonObject().entrySet()) {
                                            String key = entry1.getKey();
                                            correspondingMap.put(key, new Title(key, entry.getValue().getAsString(), titleRarity));
                                        }
                                    }
                                    else {
                                        String prefix = fileName.substring(0, fileName.indexOf("_")) + ".titles.";
                                        for (JsonElement element : entry.getValue().getAsJsonArray()) {
                                            String title = element.getAsString();
                                            String key = prefix + title.toLowerCase().replace("the ", "")
                                                    .replaceAll(" ", "_");
                                            correspondingMap.put(key, new Title(key, title, titleRarity));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {
                TitlesMod.LOG.error("Failed to load titles from data", ex);
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