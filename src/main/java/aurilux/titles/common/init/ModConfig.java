package aurilux.titles.common.init;

import aurilux.titles.common.Titles;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;

@Config(modid = Titles.MOD_ID, name = Titles.MOD_NAME + "/" + Titles.MOD_ID)
public class ModConfig {
    @Config.Comment("The color for common titles")
    public static ColorOnlyTextFormatting commonColor = ColorOnlyTextFormatting.WHITE;
    @Config.Comment("The color for uncommon titles")
    public static ColorOnlyTextFormatting uncommonColor = ColorOnlyTextFormatting.YELLOW;
    @Config.Comment("The color for rare titles")
    public static ColorOnlyTextFormatting rareColor = ColorOnlyTextFormatting.AQUA;
    @Config.Comment("The color for unique (a.k.a. contributor) titles")
    public static ColorOnlyTextFormatting uniqueColor = ColorOnlyTextFormatting.LIGHT_PURPLE;

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
}