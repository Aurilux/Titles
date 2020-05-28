package aurilux.titles.common.init;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.Titles;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Config(modid = Titles.MOD_ID, name = Titles.MOD_NAME + "/" + Titles.MOD_ID + "_config")
@Mod.EventBusSubscriber(modid = Titles.MOD_ID)
public class ModConfig {
    @Config.Comment("Set to false if you do not want archive fragments to be added to loot")
    public static boolean addFragmentsToLoot = true;

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Titles.MOD_ID)) {
            ConfigManager.sync(Titles.MOD_ID, Config.Type.INSTANCE);
        }
    }

    private static final List<String> defaultTitles = Arrays.asList("archive.json", "titles.json", "minecraft.json");
    private static File configDir;

    public static void init(FMLPreInitializationEvent event) {
        configDir = new File(event.getModConfigurationDirectory(), "titles");
        if (!configDir.exists()) {
            configDir.mkdir();
        }

        //Check the config directory for the default files. If the file doesn't exist, copy over default file
        for (String fileName : defaultTitles) {
            File defaultTitleFile = new File(configDir, fileName);
            if (!defaultTitleFile.exists()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = ModConfig.class.getResourceAsStream("/assets/titles/default_data/" + fileName);
                    outputStream = new FileOutputStream(defaultTitleFile);
                    ByteStreams.copy(inputStream, outputStream);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                finally {
                    IOUtils.closeQuietly(inputStream);
                    IOUtils.closeQuietly(outputStream);
                }
            }
        }
    }

    public static void loadTitles() {
        //Load all the title data from files in the config directory
        for (File titleFile : configDir.listFiles()) {
            String fileName = titleFile.getName();
            if (!fileName.endsWith(".json")) {
                continue;
            }

            JsonObject jsonObject = null;
            try {
                jsonObject = new JsonParser().parse(new FileReader(titleFile)).getAsJsonObject();
            }
            catch (IOException | IllegalArgumentException e) {
                Titles.LOGGER.warn("Unable to load titles. Either the file doesn't exist or the format is wrong.");
            }
            finally {
                if (jsonObject != null) {
                    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        TitleInfo.TitleRarity titleRarity = TitleInfo.TitleRarity.valueOf(entry.getKey().toUpperCase());
                        if (!titleRarity.equals(TitleInfo.TitleRarity.UNIQUE)) {
                            JsonArray titles = entry.getValue().getAsJsonArray();
                            for (int i = 0; i < titles.size(); i++) {
                                String key = titles.get(i).getAsString();
                                String modid = fileName.substring(0, fileName.indexOf("."));
                                if (modid.equals("archive")) {
                                    TitlesAPI.addArchiveTitle(key, titleRarity);
                                }
                                else {
                                    TitlesAPI.registerTitle(modid + ":" + key, titleRarity);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}