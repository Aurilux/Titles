package aurilux.titles.api.handler;

import aurilux.titles.api.Title;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;

public interface IInternalMethodHandler {
    void unlockTitle(ServerPlayerEntity player, String titleKey);
    Title getTitle(String titleKey);
    void registerTitle(Rarity rarity, String titleKey);
}