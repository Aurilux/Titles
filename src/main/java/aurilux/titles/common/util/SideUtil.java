package aurilux.titles.common.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;

public class SideUtil {
    public static Dist getSide() {
        return FMLLoader.getDist();
    }

    public static boolean isClient() {
        return getSide().isClient();
    }

    public static boolean isServer() {
        return getSide().isDedicatedServer();
    }

    public static String translate(String key, Object... args) {
        if (isClient()) {
            return I18n.format(key, args);
        }
        else {
            return new TranslationTextComponent(key, args).getUnformattedComponentText();
        }
    }
}
