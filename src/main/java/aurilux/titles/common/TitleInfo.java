package aurilux.titles.common;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.text.TextComponentTranslation;

public class TitleInfo implements Comparable {
    public final static TitleInfo NULL_TITLE = new TitleInfo("null", EnumRarity.COMMON);
    private String titleId;
    private EnumRarity titleRarity;

    public TitleInfo() {}

    public TitleInfo(String name, EnumRarity rarity) {
        titleId = name;
        titleRarity = rarity;
    }

    public String getFormattedTitle() {
        return titleRarity.color + new TextComponentTranslation(titleId).getFormattedText();
    }

    @Override
    public String toString() {
        return titleId + "," + titleRarity.rarityName.toUpperCase();
    }

    public static TitleInfo fromString(String titleInfo) {
        String[] ti = titleInfo.split(",");
        return new TitleInfo(ti[0], EnumRarity.valueOf(ti[1]));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TitleInfo && ((TitleInfo) o).titleId.equals(this.titleId)
                && ((TitleInfo) o).titleRarity.equals(this.titleRarity);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof TitleInfo) {
            TitleInfo info = (TitleInfo) o;
            if (info.titleRarity.ordinal() > this.titleRarity.ordinal()) {
                return -1;
            }
            else if (info.titleRarity.ordinal() < this.titleRarity.ordinal()) {
                return 1;
            }
        }
        return 0;
    }
}