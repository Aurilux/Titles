package aurilux.titles.common.impl;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.TitlesCapability;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import aurilux.titles.common.util.SideUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

public class TitlesAPIImpl implements TitlesAPI {
    @CapabilityInject(TitlesCapability.class)
    public static Capability<TitlesCapability> TITLES_CAPABILITY = null;

    public void unlockTitle(ServerPlayerEntity player, String titleKey) {
        Title title = TitleRegistry.INSTANCE.getTitle(titleKey);
        this.getCapability(player).ifPresent(c -> {
            if (c.add(title)) {
                PacketHandler.sendTo(new PacketSyncUnlockedTitle(titleKey), player);
                TitlesMod.LOG.debug("Unlocked title - " + c.serializeNBT());
            }
        });
    }

    public void setDisplayTitle(PlayerEntity player, String titleKey) {
        this.getCapability(player).ifPresent(c -> {
            c.setDisplayTitle(TitleRegistry.INSTANCE.getTitle(titleKey));
        });
    }

    public boolean titleExists(String titleId) {
        return TitleRegistry.INSTANCE.getTitlesMap().containsKey(titleId);
    }

    public LazyOptional<TitlesCapability> getCapability(PlayerEntity player) {
        return player.getCapability(TitlesAPIImpl.TITLES_CAPABILITY);
    }

    public String getFormattedTitle(Title title, boolean addComma) {
        if (title.isNull()) {
            return "";
        }

        TextFormatting titleColor = title.getRarity().color;
        return (addComma ? ", " : "") + titleColor + SideUtil.translate(title.getLangKey());
    }
}