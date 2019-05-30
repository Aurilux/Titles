package aurilux.titles.common;

import aurilux.titles.common.capability.TitlesImpl;
import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncTitleDataOnLogin;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = Titles.MOD_ID,
        name = "Titles",
        version = Titles.MOD_VERSION,
        acceptedMinecraftVersions = "[1.12.2, 1.13)",
        updateJSON = "https://raw.githubusercontent.com/Aurilux/Titles/blob/master/update.json")
public class Titles {
    public static final String MOD_ID = "titles";
    public static final String MOD_VERSION = "1.0.0";
    public static final Logger logger = LogManager.getLogger(MOD_ID.toUpperCase());
    public static Configuration config;

    @Mod.Instance(MOD_ID)
    public static Titles instance;

    @SidedProxy(
            clientSide = "aurilux.titles.client.ClientProxy",
            serverSide = "aurilux.titles.common.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Run before anything else.
     * Read your config.
     * Register blocks, items, tile entities, and entities.
     * Assign ore dictionary names.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        TitlesImpl.register();
        proxy.preInit(event);
    }

    /**
     * Register world generators, recipes, event handlers, network (network).
     * Send IMC messages
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

        //NETWORK/PACKETS
        PacketDispatcher.registerDualMessage(PacketSyncSelectedTitle.class);
        PacketDispatcher.registerClientMessage(PacketSyncTitleDataOnLogin.class);
        PacketDispatcher.registerClientMessage(PacketSyncUnlockedTitle.class);
    }

    /**
     * Handle mod compatibility or anything which depends on other mods’ init phases being finished.
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    /**
     * Register commands
     */
    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        //Advancements are only loaded at the start, or reload, of the server. So, I have to generate the titles here
        //or else there will be no advancements to generate from.
        TitleManager.generateTitles();

        event.registerServerCommand(new CommandTitles());
    }

    /**
     * Process IMC messages
     */
    @Mod.EventHandler
    public void handleIMC(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equals("register-titles") && message.isStringMessage()) {
                for (String s : message.getStringValue().split(";")) {
                    TitleManager.registerTitle(s);
                }
            }
        }
    }

    public static void console(Object o, boolean warning) {
        logger.log(warning ? Level.WARN : Level.INFO, o.toString());
    }

    public static void console(Object o) {
        console(o, false);
    }
}