package aurilux.titles.api;

import com.google.gson.JsonObject;
import net.minecraft.item.Rarity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Comparator;
import java.util.function.Consumer;

public class Title {
    public final static Title NULL_TITLE = new Title(new Builder(AwardType.ADVANCEMENT)
            .id("titles:null")
            .defaultDisplay("")
            .rarity(Rarity.COMMON));

    private final AwardType type;
    private final ResourceLocation id;
    private final String modid;
    private final String defaultDisplay;
    private final String variantDisplay;
    private final String flavorText;
    private final Rarity rarity;

    public Title(Builder builder) {
        type = builder.getType();
        id = builder.getID();
        modid = id.getNamespace();
        defaultDisplay = builder.getDefaultDisplay();
        variantDisplay = builder.getVariantDisplay();
        flavorText = builder.getFlavorText();
        rarity = builder.getRarity();
    }

    public AwardType getType() {
        return type;
    }
    public ResourceLocation getID() {
        return id;
    }
    public String getModid() {
        return modid;
    }
    public String getDefaultDisplay() {
        return defaultDisplay;
    }
    public String getVariantDisplay() {
        return variantDisplay;
    }
    public String getFlavorText() {
        return flavorText;
    }
    public Rarity getRarity() {
        return rarity;
    }
    public boolean isNull() {
        return this.equals(NULL_TITLE);
    }

    public IFormattableTextComponent getTextComponent(boolean isMasculine) {
        String translatable = getDefaultDisplay();
        if (!isMasculine && !StringUtils.isNullOrEmpty(getVariantDisplay())) {
            translatable = getVariantDisplay();
        }
        return new TranslationTextComponent(translatable).mergeStyle(getRarity().color);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", getID(), getRarity());
    }

    public JsonObject serializeJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("type", getType().toString().toLowerCase());
        json.addProperty("rarity", getRarity().toString().toLowerCase());
        JsonObject display = new JsonObject();
        display.addProperty("default", getDefaultDisplay());
        if (!StringUtils.isNullOrEmpty(getVariantDisplay())) {
            display.addProperty("variant", getVariantDisplay());
        }
        if (!StringUtils.isNullOrEmpty(getFlavorText())) {
            display.addProperty("flavor", getFlavorText());
        }
        json.add("display", display);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Title)) {
            return false;
        }
        else {
            Title temp = (Title) o;
            return getID().equals(temp.id) && getRarity().equals(temp.rarity);
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

    public enum AwardType {
        ADVANCEMENT,
        COMMAND,
        CONTRIBUTOR,
        LOOT
    }

    public static class Builder {
        private AwardType type;
        private ResourceLocation id;
        private String defaultDisplay;
        private String variantDisplay;
        private String flavorText;
        private Rarity rarity;

        private Builder(AwardType type) {
            this.type = type;
        }

        public static Builder create(AwardType type) {
            return new Builder(type);
        }

        public AwardType getType() {
            return type;
        }

        public ResourceLocation getID() {
            return id;
        }

        public Builder id(String s) {
            return id(new ResourceLocation(s));
        }

        public Builder id(ResourceLocation r) {
            id = r;
            return this;
        }

        public Rarity getRarity() {
            return rarity;
        }

        public Builder rarity(Rarity r) {
            rarity = r;
            return this;
        }

        public String getDefaultDisplay() {
            return defaultDisplay;
        }

        public Builder defaultDisplay(String s) {
            defaultDisplay = s;
            return this;
        }

        public String getVariantDisplay() {
            return variantDisplay;
        }

        public Builder variantDisplay(String s) {
            variantDisplay = s;
            return this;
        }

        public String getFlavorText() {
            return flavorText;
        }

        public Builder flavorText(String s) {
            flavorText = s;
            return this;
        }

        public static Title deserializeJSON(ResourceLocation res, JsonObject json) {
            AwardType testType = AwardType.valueOf(JSONUtils.getString(json, "type").toUpperCase());
            Builder builder = create(testType.equals(AwardType.CONTRIBUTOR) ? AwardType.COMMAND : testType)
                    .id(res);

            Rarity testRarity = Rarity.valueOf(JSONUtils.getString(json, "rarity").toUpperCase());
            builder.rarity(testRarity.equals(Rarity.EPIC) ? Rarity.COMMON : testRarity);

            JsonObject display = JSONUtils.getJsonObject(json, "display");
            builder.defaultDisplay(JSONUtils.getString(display, "default"));
            if (json.has("variant")) {
                builder.variantDisplay(JSONUtils.getString(display, "variant"));
            }
            if (json.has("flavor")) {
                builder.flavorText(JSONUtils.getString(display, "flavor"));
            }

            return new Title(builder);
        }

        public Title build(Consumer<Title> consumer) {
            Title title = new Title(this);
            consumer.accept(title);
            return title;
        }
    }
}