package aurilux.titles.client.handler;

import aurilux.titles.client.gui.GuiTitleSelection;
import aurilux.titles.common.Titles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    public static final KeyBinding openTitleSelection = new KeyBinding(Titles.MOD_ID + ".key.openTitleSelection", GLFW_KEY_T, "key.categories." + Titles.MOD_ID);

    private void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(openTitleSelection);
    }

    @SubscribeEvent
    public static void onClientTickEvent(final TickEvent.ClientTickEvent event) {
        if (openTitleSelection.isPressed()) {
            PlayerEntity player = Minecraft.getInstance().player;
            Minecraft.getInstance().displayGuiScreen(new GuiTitleSelection(player));
        }
    }
}
