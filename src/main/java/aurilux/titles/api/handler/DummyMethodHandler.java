package aurilux.titles.api.handler;

import aurilux.titles.api.Title;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;

public class DummyMethodHandler implements IInternalMethodHandler {
    @Override
    public void unlockTitle(ServerPlayerEntity player, String titleKey) {}

    @Override
    public Title getTitle(String titleKey) {
        return Title.NULL_TITLE;
    }

    @Override
    public void registerTitle(Rarity rarity, String titleKey) {}
}