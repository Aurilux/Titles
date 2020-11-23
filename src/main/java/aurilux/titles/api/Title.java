package aurilux.titles.api;

import aurilux.titles.common.TitlesMod;
import net.minecraft.item.Rarity;

import java.util.Comparator;

public class Title {
    public final static Title NULL_TITLE = new Title("null", Rarity.COMMON);
    private String key;
    private String modid;
    private String langKey;
    private Rarity rarity;

    public Title() {}

    public Title(String key, Rarity rarity) {
        this.key = key;
        int colonIndex = key.indexOf(":");
        // This is used for NULL_TITLE and for contributor titles as neither needs unique translation
        if (colonIndex == -1) {
            modid = TitlesMod.ID;
            langKey = key;
        }
        else {
            this.modid = key.substring(0, colonIndex);
            this.langKey = "title." + key.replaceAll("[\\s]", "_").replaceAll("[\\W]", ".");
        }
        this.rarity = rarity;
    }

    public String getKey() {
        return key;
    }
    public String getModid() {
        return modid;
    }
    public String getLangKey() {
        return langKey;
    }
    public Rarity getRarity() {
        return rarity;
    }
    public boolean isNull() {
        return this.equals(NULL_TITLE);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Title)) {
            return false;
        }
        else {
            Title temp = (Title) o;
            return this.key.equals(temp.key) && this.rarity.equals(temp.rarity);
        }
    }

    @Override
    public String toString() {
        return "(" + this.modid + ":" + this.key + ", " + this.rarity.toString() + ")";
    }

    public static class RarityComparator implements Comparator<Title> {
        @Override
        public int compare(Title o1, Title o2) {
            if (o1.rarity.ordinal() > o2.rarity.ordinal()) {
                return 1;
            }
            else if (o1.rarity.ordinal() < o2.rarity.ordinal()) {
                return -1;
            }
            return 0;
        }
    }

    public static class ModComparator implements Comparator<Title> {
        @Override
        public int compare(Title o1, Title o2) {
            return o1.modid.compareTo(o2.modid);
        }
    }
}