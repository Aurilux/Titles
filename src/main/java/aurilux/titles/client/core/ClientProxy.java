package aurilux.titles.client.core;

import aurilux.titles.client.handler.KeyInputHandler;
import aurilux.titles.common.core.CommonProxy;
import aurilux.titles.common.core.Titles;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [12 Mar 2015]
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        super.init();
        //registerRenderers();
    }

    @Override
    protected void registerHandlers() {
        super.registerHandlers();
        NetworkRegistry.INSTANCE.registerGuiHandler(Titles.instance, Titles.proxy);
        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
    }
}