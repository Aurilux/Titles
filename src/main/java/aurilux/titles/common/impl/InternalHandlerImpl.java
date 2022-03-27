package aurilux.titles.common.impl;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.handler.IInternalMethodHandler;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class InternalHandlerImpl implements IInternalMethodHandler {
    // TODO Is there a way to optimize by searching the titles by the AwardType?
    @Override
    public void unlockTitle(ServerPlayerEntity player, ResourceLocation titleKey) {
        //TODO Did I seriously miss adding the title on the server side?!
        // Might need to verify the new title is added on this
        TitlesAPI.getCapability(player).ifPresent(c -> {
            TitlesMod.LOG.info("Does the player currently have this title ({})? {}.", getTitle(titleKey), c.getObtainedTitles().contains(getTitle(titleKey)));
            if (c.add(getTitle(titleKey))) {
                TitlesMod.LOG.info("How about now ({})? {}.", getTitle(titleKey), c.getObtainedTitles().contains(getTitle(titleKey)));
                PacketHandler.toPlayer(new PacketSyncUnlockedTitle(titleKey), player);
            }
        });
    }

    @Override
    public Title getTitle(ResourceLocation titleKey) {
        return TitleManager.INSTANCE.getTitle(titleKey);
    }
}