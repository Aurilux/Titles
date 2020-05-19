package aurilux.titles.api;

import aurilux.titles.common.Titles;

import java.util.Comparator;

public class TitleInfo {
    public final static TitleInfo NULL_TITLE = new TitleInfo("null", TitleRarity.COMMON);
    private String key;
    private String modid;
    private String langKey;
    private TitleRarity titleRarity;

    public enum TitleRarity {
        COMMON, UNCOMMON, RARE, UNIQUE
    }

    public TitleInfo() {}

    public TitleInfo(String key, TitleRarity titleRarity) {
        this.key = key;
        int colon = key.indexOf(":");
        if (colon == -1) {
            this.modid = Titles.MOD_ID;
            this.langKey = key;
        }
        else {
            this.modid = key.substring(0, colon);
            this.langKey = "title." + key.replaceAll("[\\s]", "_").replaceAll("[\\W]", ".");
        }
        this.titleRarity = titleRarity;
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
    public TitleRarity getTitleRarity() {
        return titleRarity;
    }
    public boolean isNull() {
        return this.equals(NULL_TITLE);
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