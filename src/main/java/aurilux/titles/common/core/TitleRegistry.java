package aurilux.titles.common.core;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.common.Titles;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TitleRegistry {
    public static final TitleRegistry INSTANCE = new TitleRegistry();

    private final Map<String, TitleInfo> titlesMap = new HashMap<>();
    private final Map<String, TitleInfo> archiveTitles = new HashMap<>();
    private final Map<String, TitleInfo> contributorTitles = new HashMap<>();

    private TitleRegistry() {}

    public TitleInfo getTitle(String titleKey) {
        return titlesMap.getOrDefault(titleKey,
                archiveTitles.getOrDefault(titleKey,
                        contributorTitles.getOrDefault(titleKey,
                                TitleInfo.NULL_TITLE)));
    }

    public Map<String, TitleInfo> getTitlesMap() {
        return titlesMap;
    }

    public Map<String, TitleInfo> getArchiveTitles() {
        return archiveTitles;
    }

    public void init() {
        new ThreadContributorLoader();

        List<ModInfo> mods = ModList.get().getMods();
        Map<ModInfo, String> foundTitles = new HashMap<>();

        //From each mod, find it's title data
        mods.forEach(mod -> {
            String modId = mod.getModId();
            //Minecraft and Forge will not have title data, so we ignore them
            if (modId.equals("minecraft") || modId.equals("forge")) {
                return;
            }

            String filePath = String.format("data/%s/%s", modId, Titles.MOD_ID);
            Path modSource = mod.getOwningFile().getFile().getFilePath().resolve(filePath);
            if (Files.exists(modSource)) {
                try {
                    Iterator<Path> itr = Files.walk(modSource).iterator();
                    while (itr.hasNext()) {
                        Path p = itr.next();
                        //Path objects inherently use the "\". We replace it with "/" so it's easier for us to work with
                        String pathString = p.toString().replaceAll("\\\\", "/");
                        String fileName = pathString.substring(pathString.lastIndexOf(Titles.MOD_ID + "/"));
                        if (pathString.endsWith(".json") && (modId.equals(Titles.MOD_ID) || modId.equals(fileName))) {
                            String assetPath = pathString.substring(pathString.indexOf("/data"));
                            foundTitles.put(mod, assetPath);
                        }
                    }
                }
                catch (IOException ex) {
                    Titles.LOGGER.warn("Unable to find title data for mod {}", modId);
                }
            }
        });

        //Load all the titles from the files we found
        foundTitles.forEach((mod, file) -> {
            Optional<? extends ModContainer> container = ModList.get().getModContainerById(mod.getModId());
            container.ifPresent(c -> {
                Class<?> ownerClass = c.getMod().getClass();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ownerClass.getResourceAsStream(file)));
                    JsonObject titleData = new JsonParser().parse(reader).getAsJsonObject();
                    if (titleData != null) {
                        for (Map.Entry<String, JsonElement> entry : titleData.entrySet()) {
                            TitleInfo.TitleRarity titleRarity = TitleInfo.TitleRarity.valueOf(entry.getKey().toUpperCase());
                            //I reserve the UNIQUE title rarity for my contributors. This prevents people from being sneaky.
                            if (!titleRarity.equals(TitleInfo.TitleRarity.UNIQUE)) {
                                JsonArray titles = entry.getValue().getAsJsonArray();
                                for (int i = 0; i < titles.size(); i++) {
                                    String key = titles.get(i).getAsString();
                                    TitleInfo newTitle = new TitleInfo(key, titleRarity);
                                    if (file.equals("archive")) {
                                        archiveTitles.put(key, newTitle);
                                    }
                                    else {
                                        titlesMap.put(key, newTitle);
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    Titles.LOGGER.error("Failed to load titles defined by mod {}, skipping",
                            c.getModInfo().getModId(), ex);
                }
            });
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
                Titles.LOGGER.warn("Unable to load contributors list. Most likely you're offline or github is down.");
            }
        }
    }

    private void load(Properties props) {
        for(String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            contributorTitles.put(key, new TitleInfo(value, TitleInfo.TitleRarity.UNIQUE));
        }
    }
}