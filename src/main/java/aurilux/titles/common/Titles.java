package aurilux.titles.common;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.TitlesImpl;
import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.handler.InternalMethodHandler;
import aurilux.titles.common.handler.LootHandler;
import aurilux.titles.common.init.ModItems;
import aurilux.titles.common.item.ItemTitleArchive;
import aurilux.titles.common.network.PacketDispatcher;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;

@Mod(modid = Titles.MOD_ID,
        name = Titles.MOD_NAME,
        version = Titles.MOD_VERSION,
        acceptedMinecraftVersions = "[1.12,]",
        updateJSON = "https://raw.githubusercontent.com/Aurilux/Titles/blob/master/update.json")
public class Titles {
    public static final String MOD_ID = "titles";
    public static final String MOD_NAME = "Titles";
    public static final String MOD_VERSION = "1.0.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID.toUpperCase());
    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.titleArchive);
        }
    };

    public static boolean DEV_ENV;

    @Mod.Instance(MOD_ID)
    public static Titles instance;

    @SidedProxy(
            clientSide = "aurilux.titles.client.ClientProxy",
            serverSide = "aurilux.titles.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        DEV_ENV = ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"));
        proxy.preInit(event);

        TitlesAPI.internalHandler = new InternalMethodHandler();
        TitlesImpl.register();
        loadArchiveTitles(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        PacketDispatcher.init();
        LootHandler.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandTitles());
    }

    public void loadArchiveTitles(FMLPreInitializationEvent event) {
        File configFolder = new File(event.getModConfigurationDirectory(), "titles");
        if (!configFolder.exists()) {
            configFolder.mkdir();
        }

        File archiveTitles = new File(configFolder, "archive_titles.json");
        if (!archiveTitles.exists()) {
            InputStream input = null;
            OutputStream output = null;

            try {
                input = getClass().getResourceAsStream("/assets/titles/archive_titles.json");
                output = new FileOutputStream(archiveTitles);
                ByteStreams.copy(input, output);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                IOUtils.closeQuietly(output);
                IOUtils.closeQuietly(input);
            }
        }

        JsonParser parser = new JsonParser();
        try {
            JsonObject jsonObject = parser.parse(new FileReader(archiveTitles)).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                TitleInfo.TitleRarity titleRarity = TitleInfo.TitleRarity.valueOf(entry.getKey());
                if (!titleRarity.equals(TitleInfo.TitleRarity.UNIQUE)) {
                    JsonArray titles = entry.getValue().getAsJsonArray();
                    for (int i = 0; i < titles.size(); i++) {
                        String key = titles.get(i).getAsString();
                        ItemTitleArchive.addArchiveTitle(key, titleRarity);
                    }
                }
            }
        }
        catch (IOException | IllegalArgumentException e) {
            Titles.console("Unable to load archive titles. Either the file doesn't exist or the format is wrong.");
        }
    }

    public static void console(Object o, boolean warning) {
        LOGGER.log(warning ? Level.WARN : Level.INFO, o.toString());
    }

    public static void console(Object o) {
        console(o, false);
    }
}