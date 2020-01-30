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
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Config(modid = Titles.MOD_ID, name = Titles.MOD_NAME + "/" + Titles.MOD_ID + "_config")
public class ModConfig {
    @Config.Comment("The color for common titles")
    public static ColorOnlyTextFormatting commonColor = ColorOnlyTextFormatting.WHITE;
    @Config.Comment("The color for uncommon titles")
    public static ColorOnlyTextFormatting uncommonColor = ColorOnlyTextFormatting.YELLOW;
    @Config.Comment("The color for rare titles")
    public static ColorOnlyTextFormatting rareColor = ColorOnlyTextFormatting.AQUA;
    @Config.Comment("The color for unique (a.k.a. contributor) titles")
    public static ColorOnlyTextFormatting uniqueColor = ColorOnlyTextFormatting.LIGHT_PURPLE;

    public enum ColorOnlyTextFormatting {
        //Lighter Colors
        AQUA(TextFormatting.AQUA),
        BLUE(TextFormatting.BLUE),
        GOLD(TextFormatting.GOLD),
        GRAY(TextFormatting.GRAY),
        GREEN(TextFormatting.GREEN),
        LIGHT_PURPLE(TextFormatting.LIGHT_PURPLE),
        RED(TextFormatting.RED),
        YELLOW(TextFormatting.YELLOW),
        WHITE(TextFormatting.WHITE);

        public TextFormatting textFormatting;
        ColorOnlyTextFormatting(TextFormatting textFormatting) {
            this.textFormatting = textFormatting;
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
                Titles.console("Unable to load titles. Either the file doesn't exist or the format is wrong.");
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