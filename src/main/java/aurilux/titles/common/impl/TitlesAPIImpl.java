package aurilux.titles.common.impl;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

public class TitlesAPIImpl implements TitlesAPI {
    @CapabilityInject(ITitles.class)
    public static Capability<ITitles> TITLES_CAPABILITY = null;

    public void unlockTitle(ServerPlayerEntity player, String titleKey) {
        TitleInfo title = TitleRegistry.INSTANCE.getTitle(titleKey);
        this.getCapability(player).ifPresent(c -> {
            if (c.add(title)) {
                PacketHandler.sendTo(new PacketSyncUnlockedTitle(titleKey), player);
            }
        });
    }

    public void setDisplayTitle(PlayerEntity player, String titleKey) {
        this.getCapability(player).ifPresent(c -> {
            c.setSelectedTitle(TitleRegistry.INSTANCE.getTitle(titleKey));
        });
    }

    public LazyOptional<ITitles> getCapability(PlayerEntity player) {
        return player.getCapability(TitlesAPIImpl.TITLES_CAPABILITY);
    }

    public String getFormattedTitle(TitleInfo titleInfo, boolean addComma) {
        if (titleInfo.equals(TitleInfo.NULL_TITLE)) {
            return "";
        }

        TextFormatting titleColor;
        switch (titleInfo.getTitleRarity()) {
            case UNIQUE: titleColor = TitlesConfig.CLIENT.uniqueColor.get().textFormatting; break;
            case RARE: titleColor = TitlesConfig.CLIENT.rareColor.get().textFormatting; break;
            case UNCOMMON: titleColor = TitlesConfig.CLIENT.uncommonColor.get().textFormatting; break;
            default: titleColor = TitlesConfig.CLIENT.commonColor.get().textFormatting; break; //COMMON
        }

        return (addComma ? ", " : "") + titleColor + new TranslationTextComponent(titleInfo.getLangKey()).getFormattedText();
    }
}