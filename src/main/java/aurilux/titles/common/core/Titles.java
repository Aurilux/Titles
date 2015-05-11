package aurilux.titles.common.core;

import aurilux.ardentcore.common.init.ModVersionChecker;
import aurilux.ardentcore.common.mod.AssetWrapper;
import aurilux.ardentcore.common.mod.NetworkWrapper;
import aurilux.titles.common.init.ModAchievements;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [12 Mar 2015]
 */
@Mod(modid = Titles.MOD_ID, name = "Titles", version = Titles.MOD_VERSION,
        dependencies = "required-after:ArdentCore@[1.0.0,)")
public class Titles {
    public static final String MOD_ID = "Titles";
    public static final String MOD_VERSION = "1.0.0";
    public static final String versionUrl = "https://raw.githubusercontent.com/Aurilux/Titles/master/version.xml";

    public static final AssetWrapper assets = new AssetWrapper(MOD_ID.toLowerCase());
    public static final Logger logger = LogManager.getLogger(MOD_ID.toUpperCase());
    public static final NetworkWrapper network = new NetworkWrapper(MOD_ID);

    @Mod.Instance(MOD_ID)
    public static Titles instance;

    @SidedProxy(
            clientSide = "aurilux.titles.client.core.ClientProxy",
            serverSide = "aurilux.titles.common.core.CommonProxy")
    public static CommonProxy proxy;

    /**
     * Run before anything else. Read your config, create blocks, items, etc
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModVersionChecker.registerModToUpdate("Titles", MOD_VERSION, EnumChatFormatting.DARK_GREEN, versionUrl);
        ModAchievements.init();
    }

    /**
     * Build whatever data structures you care about and register handlers, tile entities, renderers, and recipes.
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    /**
     * Handle interaction with other mods, complete your setup based on this
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}