package aurilux.titles.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public enum ModKeybindings {
    OPEN_TITLE_SELECTION("keybind.titles.openTitleSelection", Keyboard.KEY_T);

    private final KeyBinding keyBinding;

    ModKeybindings(String keyName, int defaultKey) {
        keyBinding = new KeyBinding(keyName, defaultKey, "keybind.titles.category");
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }

    public boolean isPressed() {
        return keyBinding.isPressed();
    }

    public static void registerBindings() {
        for (ModKeybindings binding : ModKeybindings.values()) {
            ClientRegistry.registerKeyBinding(binding.getKeyBinding());
        }
    }
}