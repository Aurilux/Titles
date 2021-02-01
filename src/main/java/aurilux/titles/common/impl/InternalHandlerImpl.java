package aurilux.titles.common.impl;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.handler.IInternalMethodHandler;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;

public class InternalHandlerImpl implements IInternalMethodHandler {
    @Override
    public void unlockTitle(ServerPlayerEntity player, String titleKey) {
        Title title = TitlesAPI.internal().getTitle(titleKey);
        TitlesAPI.getCapability(player).ifPresent(c -> {
            if (c.add(title)) {
                PacketHandler.sendTo(new PacketSyncUnlockedTitle(titleKey), player);
            }
        });
    }

    @Override
    public Title getTitle(String titleKey) {
        return TitleRegistry.INSTANCE.getTitle(titleKey);
    }

    @Override
    public void registerTitle(Rarity rarity, String titleKey) {
        TitleRegistry.INSTANCE.registerTitle(rarity, titleKey);
    }
}