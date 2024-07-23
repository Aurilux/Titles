package aurilux.titles.client;

import aurilux.titles.common.TitlesMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = TitlesMod.MOD_ID, value = Dist.CLIENT, bus = Bus.MOD)
public class Keybinds {
    public static final KeyMapping openTitleSelection = createKey("openTitleSelection", GLFW.GLFW_KEY_T);

    private static KeyMapping createKey(String desc, int keycode) {
        return new KeyMapping("key.titles." + desc, keycode, "Titles");
    }

    @SubscribeEvent
    public static void onRegisterKeybindings(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.openTitleSelection);
    }
}