package aurilux.titles.api;

import aurilux.titles.common.TitlesMod;
import com.google.gson.JsonObject;
import net.minecraft.item.Rarity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Comparator;
import java.util.function.Consumer;

public class Title {
    public final static Title NULL_TITLE = new Title(Builder.create(TitlesMod.MOD_ID).id(TitlesMod.prefix("null")));

    public enum AwardType {
        ADVANCEMENT,
        COMMAND,
        CONTRIBUTOR,
        LOOT
    }

    private final AwardType type;
    private final ResourceLocation id;
    private final String modid;
    private final String defaultDisplay;
    private final String variantDisplay;
    private final String flavorText;
    private final Rarity rarity;

    // This is private to follow the Builder Pattern. Since the Builder makes the object, it doesn't need to be public
    private Title(Builder builder) {
        type = builder.getType();
        id = builder.getID();
        modid = builder.getModId();
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

    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("type", getType().toString().toLowerCase());
        json.addProperty("id", getID().toString());
        json.addProperty("rarity", getRarity().toString().toLowerCase());
        json.addProperty("defaultDisplay", getDefaultDisplay());
        if (!StringUtils.isNullOrEmpty(getVariantDisplay())) {
            json.addProperty("variantDisplay", getVariantDisplay());
        }
        if (!StringUtils.isNullOrEmpty(getFlavorText())) {
            json.addProperty("flavorText", getFlavorText());
        }
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

    public static class Builder {
        private AwardType type = AwardType.ADVANCEMENT;
        private Rarity rarity = Rarity.COMMON;
        private ResourceLocation id;
        private String modId;
        private String defaultDisplay = "Not Set";
        private String variantDisplay;
        private String flavorText;

        private Builder() {}

        public static Builder create(String modId) {
            return new Builder().modId(modId);
        }

        public static Builder deserialize(JsonObject json) {
            ResourceLocation id = new ResourceLocation(JSONUtils.getString(json, "id"));
            Title.Builder builder = Builder.create(id.getNamespace())
                    .type(AwardType.valueOf(JSONUtils.getString(json, "type").toUpperCase()))
                    .id(id)
                    .rarity(Rarity.valueOf(JSONUtils.getString(json, "rarity").toUpperCase()));

            builder.defaultDisplay(JSONUtils.getString(json, "defaultDisplay"));
            if (json.has("variantDisplay")) {
                builder.variantDisplay(JSONUtils.getString(json, "variantDisplay"));
            }
            if (json.has("flavorText")) {
                builder.flavorText(JSONUtils.getString(json, "flavorText"));
            }

            return builder;
        }

        public Builder modId(String m) {
            modId = m;
            return this;
        }

        public String getModId() {
            return modId;
        }

        public Builder type(AwardType t) {
            type = t;
            return this;
        }

        public AwardType getType() {
            return type;
        }

        public Builder rarity(Rarity r) {
            rarity = r;
            return this;
        }

        public Rarity getRarity() {
            return rarity;
        }

        public Builder id(String s) {
            return id(TitlesMod.prefix(s));
        }

        public Builder id(ResourceLocation r) {
            id = r;
            return this;
        }

        public ResourceLocation getID() {
            return id;
        }

        public Builder defaultDisplay(String d) {
            defaultDisplay = d;
            return this;
        }

        public String getDefaultDisplay() {
            return defaultDisplay;
        }

        public Builder variantDisplay(String v) {
            variantDisplay = v;
            return this;
        }

        public String getVariantDisplay() {
            return variantDisplay;
        }

        public Builder flavorText(String f) {
            flavorText = f;
            return this;
        }

        public String getFlavorText() {
            return flavorText;
        }

        private void reset() {
            defaultDisplay = "Not Set";
            variantDisplay = null;
            flavorText = null;
        }

        // Used in data generation
        public Title save(Consumer<Title> consumer) {
            Title title = build();
            consumer.accept(title);
            return title;
        }

        public Title build() {
            Title title = new Title(this);
            reset();
            return title;
        }
    }
}