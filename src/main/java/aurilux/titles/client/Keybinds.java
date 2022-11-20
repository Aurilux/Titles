package aurilux.titles.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;

public class Keybinds {
    public static final KeyMapping openTitleSelection = createKey("openTitleSelection", GLFW_KEY_T);

    private static KeyMapping createKey(String desc, int keycode) {
        return new KeyMapping("key.titles." + desc, keycode, "keybind.titles.category");
    }

    public static void init() {
        ClientRegistry.registerKeyBinding(openTitleSelection);
    }
}