package aurilux.titles.client;

import aurilux.titles.common.TitlesMod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;

public class Keybinds {
    public static final KeyBinding openTitleSelection = new KeyBinding("key.titles.openTitleSelection", GLFW_KEY_T, TitlesMod.ID);

    public static void init() {
        ClientRegistry.registerKeyBinding(openTitleSelection);
    }
}