package aurilux.titles.common.core;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [12 Mar 2015]
 */
public class CommonProxy implements IGuiHandler {
    //Gui Id's
    public static int TITLE_SEL_ID = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public void init() {
        //register server-side exclusive objects
        //registerHandlers();
        //registerCommands();
    }
}