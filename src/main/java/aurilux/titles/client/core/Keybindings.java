package aurilux.titles.client.core;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [24 Mar 2015]
 */
public enum Keybindings {
    OPEN_TITLE_SELECTION("keybind.titles.openTitleSelection", Keyboard.KEY_T);

    private final KeyBinding keyBinding;

    Keybindings(String keyName, int defaultKey) {
        keyBinding = new KeyBinding(keyName, defaultKey, "keybind.titles.category");
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }

    public boolean isPressed() {
        return keyBinding.isPressed();
    }
}