package aurilux.titles.client;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;

public class Keybinds {
    public static final KeyBinding openTitleSelection = new KeyBinding("key.titles.openTitleSelection", GLFW_KEY_T, TitlesAPI.MOD_ID);

    public static void init() {
        ClientRegistry.registerKeyBinding(openTitleSelection);
    }
}