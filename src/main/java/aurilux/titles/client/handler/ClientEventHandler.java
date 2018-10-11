package aurilux.titles.client.handler;

import aurilux.titles.client.ModKeybindings;
import aurilux.titles.client.gui.GuiTitleSelection;
import aurilux.titles.common.Titles;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Titles.MOD_ID)
public class ClientEventHandler {
    //KEYBINDINGS
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ModKeybindings.OPEN_TITLE_SELECTION.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            FMLClientHandler.instance().displayGuiScreen(player, new GuiTitleSelection(player));
        }
    }
}
