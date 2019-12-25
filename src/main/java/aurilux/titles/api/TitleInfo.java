package aurilux.titles.api;

import aurilux.titles.common.Titles;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Comparator;

public class TitleInfo {
    public final static TitleInfo NULL_TITLE = new TitleInfo(Titles.MOD_ID, "null", TitleRarity.COMMON);
    private String modid;
    private String key;
    private TitleRarity titleRarity;

    public enum TitleRarity {
        COMMON, UNCOMMON, RARE, UNIQUE
    }

    public TitleInfo() {}

    public TitleInfo(String modid, String key, TitleRarity titleRarity) {
        this.modid = modid;
        this.key = key;
        this.titleRarity = titleRarity;
    }

    public String getKey() {
        return key;
    }

    public TitleRarity getTitleRarity() {
        return titleRarity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TitleInfo)) {
            return false;
        }
        else {
            TitleInfo temp = (TitleInfo) o;
            return this.key.equals(temp.key) && this.titleRarity.equals(temp.titleRarity);
        }
    }

    @Override
    public String toString() {
        return "(" + this.key + ", " + this.titleRarity.toString() + ")";
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("modid", modid);
        nbt.setString("key", key);
        nbt.setString("titleRarity", titleRarity.name().toUpperCase());
        return nbt;
    }

    public static TitleInfo readFromNBT(NBTTagCompound nbt) {
        return new TitleInfo(nbt.getString("modid"),
                nbt.getString("key"), TitleRarity.valueOf(nbt.getString("titleRarity")));
    }

    public static class RarityComparator implements Comparator<TitleInfo> {
        @Override
        public int compare(TitleInfo o1, TitleInfo o2) {
            if (o1.titleRarity.ordinal() > o2.titleRarity.ordinal()) {
                return 1;
            }
            else if (o1.titleRarity.ordinal() < o2.titleRarity.ordinal()) {
                return -1;
            }
            return 0;
        }
    }

    public static class ModComparator implements Comparator<TitleInfo> {
        @Override
        public int compare(TitleInfo o1, TitleInfo o2) {
            return o1.modid.compareTo(o2.modid);
        }
    }
}