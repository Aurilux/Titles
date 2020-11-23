package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TitleRegistry {
    public static final TitleRegistry INSTANCE = new TitleRegistry();

    private final Map<String, Title> titlesMap = new HashMap<>();
    private final Map<String, Title> lootTitles = new HashMap<>();

    // I need two maps because contributor titles can be referenced by either a username or by title key
    private final Map<String, Title> contributorTitlesByUsername = new HashMap<>();
    private final Map<String, Title> contributorTitlesByTitleKey = new HashMap<>();

    private TitleRegistry() {}

    public Title getTitle(String titleKey) {
        return titlesMap.getOrDefault(titleKey,
               lootTitles.getOrDefault(titleKey,
               contributorTitlesByUsername.getOrDefault(titleKey,
               contributorTitlesByTitleKey.getOrDefault(titleKey,
                       Title.NULL_TITLE))));
    }

    public Map<String, Title> getTitlesMap() {
        return titlesMap;
    }

    public Map<String, Title> getLootTitles() {
        return lootTitles;
    }

    public void init() {
        new ThreadContributorLoader();

        //From each mod, find it's title data
        ModList.get().getMods().forEach(mod -> {
            String modId = mod.getModId();
            //Minecraft, Forge, and MCP will not have title data, so we ignore them
            if (modId.equals("minecraft") || modId.equals("forge") || modId.equals("mcp")) {
                return;
            }

            String filePath = String.format("data/%s/%s", modId, TitlesMod.ID);
            Path modSource = mod.getOwningFile().getFile().getFilePath().resolve(filePath);
            if (Files.exists(modSource)) {
                try {
                    Iterator<Path> itr = Files.walk(modSource).iterator();
                    while (itr.hasNext()) {
                        Path p = itr.next();
                        //Path objects inherently use the "\". We replace it with "/" so it's easier for us to work with
                        String pathString = p.toString().replaceAll("\\\\", "/");
                        String fileName = pathString.substring(pathString.lastIndexOf(TitlesMod.ID + "/"));
                        // We add all the titles files from Titles because it includes minecraft and loot titles.
                        // Other mods should only include titles for their own mods
                        if (pathString.endsWith(".json") && (modId.equals(TitlesMod.ID) || modId.equals(fileName))) {
                            String assetPath = pathString.substring(pathString.indexOf("/data"));
                            readTitlesFromFile(mod, assetPath);
                        }
                    }
                }
                catch (IOException ex) {
                    TitlesMod.LOG.warn("Unable to find title data for mod {}", modId);
                }
            }
        });
    }

    private void readTitlesFromFile(ModInfo modInfo, String filePath) {
        Optional<? extends ModContainer> container = ModList.get().getModContainerById(modInfo.getModId());
            container.ifPresent(c -> {
            Class<?> ownerClass = c.getMod().getClass();
            String fileName = Paths.get(filePath).getFileName().toString().split("[.]")[0];
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(ownerClass.getResourceAsStream(filePath)));
                JsonObject titleData = new JsonParser().parse(reader).getAsJsonObject();
                if (titleData != null) {
                    for (Map.Entry<String, JsonElement> entry : titleData.entrySet()) {
                        Rarity titleRarity = Rarity.valueOf(entry.getKey().toUpperCase());
                        //I reserve the EPIC title rarity for my contributors. This prevents people from being sneaky.
                        if (!titleRarity.equals(Rarity.EPIC)) {
                            JsonArray titles = entry.getValue().getAsJsonArray();
                            for (int i = 0; i < titles.size(); i++) {
                                String key = ":" + titles.get(i).getAsString();
                                if (fileName.equals("loot")) {
                                    key = TitlesMod.ID + key;
                                    lootTitles.put(key, new Title(key, titleRarity));
                                }
                                else {
                                    // At this point the file name should be the same as the mod id
                                    key = fileName + key;
                                    titlesMap.put(key, new Title(key, titleRarity));
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex) {
                TitlesMod.LOG.error("Failed to load titles defined by mod {}, skipping", c.getModId(), ex);
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
                load(props);
            }
            catch (IOException e) {
                TitlesMod.LOG.warn("Unable to load contributors list. Most likely you're offline or github is down.");
            }
        }
    }

    private void load(Properties props) {
        for(String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            contributorTitlesByUsername.put(key, new Title(value, Rarity.EPIC));
            contributorTitlesByTitleKey.put(value, new Title(value, Rarity.EPIC));
        }
    }
}