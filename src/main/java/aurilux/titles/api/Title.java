package aurilux.titles.api;

import net.minecraft.client.resources.ClientLanguageMap;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
        // This is used for NULL_TITLE, loot, and contributor titles as neither needs unique translations
        if (colonIndex == -1) {
            modid = TitlesAPI.MOD_ID;
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

    public ITextComponent getComponent(boolean isMasculine) {
        String modifiedLangKey = getLangKey();
        if (!isMasculine && ClientLanguageMap.getInstance().func_230506_b_(getLangKey() + ".f")) {
            modifiedLangKey += ".f";
        }
        return new TranslationTextComponent(modifiedLangKey).mergeStyle(getRarity().color);
    }

    @Override
    public String toString() {
        return String.format("(%s:%s, %s)", getModid(), getKey(), getRarity());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Title)) {
            return false;
        }
        else {
            Title temp = (Title) o;
            return getKey().equals(temp.key) && getRarity().equals(temp.rarity);
        }
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
}