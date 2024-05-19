package aurilux.titles.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final KeyMapping openTitleSelection = createKey("openTitleSelection", GLFW.GLFW_KEY_T);

    private static KeyMapping createKey(String desc, int keycode) {
        return new KeyMapping("key.titles." + desc, keycode, "keybind.titles.category");
    }
}