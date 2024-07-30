package aurilux.titles.api;

import aurilux.titles.common.TitlesMod;
import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Rarity;

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
    private final boolean isPrefix;
    private final ResourceLocation id;
    private final String modid;
    private final Rarity rarity;
    private final String defaultDisplay;
    private final MutableComponent defaultComponent;
    private final String variantDisplay;
    private final MutableComponent variantComponent;
    private final String flavorText;

    // This is private to follow the Builder Pattern. Since the Builder makes the object, it doesn't need to be public
    private Title(Builder builder) {
        type = builder.getType();
        isPrefix = builder.isPrefix();
        id = builder.getID();
        modid = builder.getModId();
        rarity = builder.getRarity();
        defaultDisplay = builder.getDefaultDisplay();
        defaultComponent = createComponent(defaultDisplay);
        variantDisplay = builder.getVariantDisplay();
        variantComponent = !StringUtil.isNullOrEmpty(variantDisplay) ? createComponent(variantDisplay) : null;
        flavorText = builder.getFlavorText();
    }

    private MutableComponent createComponent(String lang) {
        return Component.translatable(lang)
                .withStyle(getRarity().equals(Rarity.COMMON) ? ChatFormatting.GRAY : getRarity().color);
    }

    public AwardType getType() {
        return type;
    }
    public boolean isPrefix() {
        return isPrefix;
    }
    public ResourceLocation getID() {
        return id;
    }
    public String getModid() {
        return modid;
    }
    public Rarity getRarity() {
        return rarity;
    }
    public String getFlavorText() {
        return flavorText;
    }
    public boolean isNull() {
        return this.equals(NULL_TITLE);
    }
    public String getDefaultDisplay() {
        return defaultDisplay;
    }
    public String getVariantDisplay() {
        return variantDisplay;
    }

    public MutableComponent getTextComponent(boolean isMasculine) {
        if (!isMasculine && variantComponent != null) {
            return variantComponent;
        }
        else {
            return defaultComponent;
        }
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", getID(), getRarity());
    }

    public JsonObject serialize() {
        var json = new JsonObject();
        json.addProperty("type", getType().toString().toLowerCase());
        json.addProperty("isPrefix", isPrefix());
        json.addProperty("id", getID().toString());
        json.addProperty("rarity", getRarity().toString().toLowerCase());
        json.addProperty("defaultDisplay", getDefaultDisplay());
        if (!StringUtil.isNullOrEmpty(getVariantDisplay())) {
            json.addProperty("variantDisplay", getVariantDisplay());
        }
        if (!StringUtil.isNullOrEmpty(getFlavorText())) {
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
            var temp = (Title) o;
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
        private boolean isPrefix = false;
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
            var id = new ResourceLocation(GsonHelper.getAsString(json, "id"));
            Title.Builder builder = Builder.create(id.getNamespace())
                    .type(AwardType.valueOf(GsonHelper.getAsString(json, "type").toUpperCase()))
                    .id(id)
                    .rarity(Rarity.valueOf(GsonHelper.getAsString(json, "rarity").toUpperCase()));

            if (json.has("isPrefix") && GsonHelper.getAsBoolean(json, "isPrefix")) {
                builder.setPrefix();
            }

            builder.defaultDisplay(GsonHelper.getAsString(json, "defaultDisplay"));
            if (json.has("variantDisplay")) {
                builder.variantDisplay(GsonHelper.getAsString(json, "variantDisplay"));
            }
            if (json.has("flavorText")) {
                builder.flavorText(GsonHelper.getAsString(json, "flavorText"));
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

        public Builder rarity(Rarity r) {
            rarity = r;
            return this;
        }

        public Rarity getRarity() {
            return rarity;
        }

        public Builder type(AwardType t) {
            type = t;
            return this;
        }

        public AwardType getType() {
            return type;
        }

        public boolean isPrefix() {
            return isPrefix;
        }

        public Builder setPrefix() {
            isPrefix = true;
            return this;
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
            isPrefix = false;
            defaultDisplay = "Not Set";
            variantDisplay = null;
            flavorText = null;
        }

        // Used in data generation
        public Title save(Consumer<Title> consumer) {
            var title = build();
            consumer.accept(title);
            return title;
        }

        public Title build() {
            var title = new Title(this);
            reset();
            return title;
        }
    }
}