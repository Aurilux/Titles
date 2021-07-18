package aurilux.titles.common.impl;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.handler.IInternalMethodHandler;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class InternalHandlerImpl implements IInternalMethodHandler {
    // TODO Is there a way to optimize by searching the titles by the AwardType?
    @Override
    public void unlockTitle(ServerPlayerEntity player, ResourceLocation titleKey) {
        Title title = TitlesAPI.internal().getTitle(titleKey);
        TitlesAPI.getCapability(player).ifPresent(c -> {
            if (c.add(title)) {
                PacketHandler.sendTo(new PacketSyncUnlockedTitle(titleKey), player);
            }
        });
    }

    @Override
    public Title getTitle(ResourceLocation titleKey) {
        return TitleManager.INSTANCE.getTitle(titleKey);
    }
}