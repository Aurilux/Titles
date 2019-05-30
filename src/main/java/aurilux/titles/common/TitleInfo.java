package aurilux.titles.common;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;

public class TitleInfo implements Comparable<TitleInfo> {
    public final static TitleInfo NULL_TITLE = new TitleInfo("null", TitleRarity.COMMON);
    private String titleId;
    private TitleRarity titleRarity;

    public enum TitleRarity {
        COMMON, UNCOMMON, RARE, UNIQUE
    }

    public TitleInfo() {}

    public TitleInfo(String name, TitleRarity type) {
        titleId = name;
        titleRarity = type;
    }

    public String getFormattedTitle() {
        return getFormattedTitle(false);
    }

    public String getFormattedTitle(boolean addComma) {
        if (this.equals(NULL_TITLE)) {
            return "";
        }

        TextFormatting titleColor;
        switch (titleRarity) {
            case UNIQUE: titleColor = ModConfig.uniqueColor.textFormatting; break;
            case RARE: titleColor = ModConfig.rareColor.textFormatting; break;
            case UNCOMMON: titleColor = ModConfig.uncommonColor.textFormatting; break;
            default: titleColor = ModConfig.commonColor.textFormatting; break; //COMMON
        }

        return (addComma ? ", " : "") + titleColor + new TextComponentTranslation(titleId).getFormattedText();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TitleInfo)) {
            return false;
        }
        else {
            TitleInfo temp = (TitleInfo) o;
            return this.titleId.equals(temp.titleId) && this.titleRarity.equals(temp.titleRarity);
        }
    }

    @Override
    public int compareTo(TitleInfo o) {
        if (o.titleRarity.ordinal() > this.titleRarity.ordinal()) {
            return -1;
        }
        else if (o.titleRarity.ordinal() < this.titleRarity.ordinal()) {
            return 1;
        }
        return 0;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("titleId", titleId);
        nbt.setString("titleRarity", titleRarity.name().toUpperCase());
        return nbt;
    }

    public static TitleInfo readFromNBT(NBTTagCompound nbt) {
        return new TitleInfo(nbt.getString("titleId"), TitleRarity.valueOf(nbt.getString("titleRarity")));
    }
}