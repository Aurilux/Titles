package aurilux.titles.client.handler;

import aurilux.titles.client.core.Keybindings;
import aurilux.titles.client.gui.GuiTitleSelection;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [23 Mar 2015]
 */
public class KeyInputHandler {
    public KeyInputHandler() {
        for (Keybindings kb : Keybindings.values()) {
            ClientRegistry.registerKeyBinding(kb.getKeyBinding());
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keybindings.OPEN_TITLE_SELECTION.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            FMLClientHandler.instance().displayGuiScreen(player, new GuiTitleSelection(player));
        }
    }
}