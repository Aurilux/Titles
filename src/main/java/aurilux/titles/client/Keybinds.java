package aurilux.titles.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;

public class Keybinds {
    public static final KeyBinding openTitleSelection = createKey("openTitleSelection", GLFW_KEY_T);

    private static KeyBinding createKey(String desc, int keycode) {
        return new KeyBinding("key.titles." + desc, keycode, "keybind.titles.category");
    }

    public static void init() {
        ClientRegistry.registerKeyBinding(openTitleSelection);
    }
}