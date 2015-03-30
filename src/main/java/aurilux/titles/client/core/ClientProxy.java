package aurilux.titles.client.core;

import aurilux.titles.client.handler.KeyInputHandler;
import aurilux.titles.common.core.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [12 Mar 2015]
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        //register client-side exclusive objects
        registerHandlers();
        //registerRenderers();
    }

    protected void registerHandlers() {
        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
    }
}