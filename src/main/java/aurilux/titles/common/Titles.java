package aurilux.titles.common;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.TitlesImpl;
import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.handler.InternalMethodHandler;
import aurilux.titles.common.handler.LootHandler;
import aurilux.titles.common.init.ModConfig;
import aurilux.titles.common.init.ModItems;
import aurilux.titles.common.network.GuiHandler;
import aurilux.titles.common.network.PacketDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Titles.MOD_ID,
        name = Titles.MOD_NAME,
        version = Titles.MOD_VERSION,
        acceptedMinecraftVersions = "[1.12,]",
        updateJSON = "https://raw.githubusercontent.com/Aurilux/Titles/blob/master/update.json")
public class Titles {
    public static final String MOD_ID = "titles";
    public static final String MOD_NAME = "Titles";
    public static final String MOD_VERSION = "3.1.1";
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
        ModConfig.init(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Titles.MOD_ID, new GuiHandler());
        proxy.init(event);
        PacketDispatcher.init();
        LootHandler.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        ModConfig.loadTitles();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandTitles());
    }

    public static void chatDebug(Object o) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(o.toString()));
    }

    public static void console(Object o, boolean warning) {
        LOGGER.log(warning ? Level.WARN : Level.INFO, o.toString());
    }

    public static void console(Object o) {
        console(o, false);
    }
}