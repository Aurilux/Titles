package aurilux.titles.common.core;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class TitlesConfig {
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public enum ColorOnlyTextFormatting {
        //Lighter Colors
        AQUA(TextFormatting.AQUA),
        BLUE(TextFormatting.BLUE),
        GOLD(TextFormatting.GOLD),
        GRAY(TextFormatting.GRAY),
        GREEN(TextFormatting.GREEN),
        LIGHT_PURPLE(TextFormatting.LIGHT_PURPLE),
        RED(TextFormatting.RED),
        YELLOW(TextFormatting.YELLOW),
        WHITE(TextFormatting.WHITE);

        public TextFormatting textFormatting;
        ColorOnlyTextFormatting(TextFormatting textFormatting) {
            this.textFormatting = textFormatting;
        }
    }

    public static class Client {
        public final ForgeConfigSpec.EnumValue<ColorOnlyTextFormatting> commonColor;
        public final ForgeConfigSpec.EnumValue<ColorOnlyTextFormatting> uncommonColor;
        public final ForgeConfigSpec.EnumValue<ColorOnlyTextFormatting> rareColor;
        public final ForgeConfigSpec.EnumValue<ColorOnlyTextFormatting> uniqueColor;

        public Client(ForgeConfigSpec.Builder builder) {
            commonColor = builder
                    .comment("The color for common titles")
                    .defineEnum("common.color", ColorOnlyTextFormatting.WHITE);
            uncommonColor = builder
                    .comment("The color for uncommon titles")
                    .defineEnum("uncommon.color", ColorOnlyTextFormatting.YELLOW);
            rareColor = builder
                    .comment("The color for rare titles")
                    .defineEnum("rare.color", ColorOnlyTextFormatting.AQUA);
            uniqueColor = builder
                    .comment("The color for unique (a.k.a. contributor) titles")
                    .defineEnum("unique.color", ColorOnlyTextFormatting.LIGHT_PURPLE);
        }
    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public Common(ForgeConfigSpec.Builder builder) {
            //NOOP
        }
    }
}