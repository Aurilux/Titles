package aurilux.titles.api.handler;

import aurilux.titles.api.Title;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

public class DummyMethodHandler implements IInternalMethodHandler {
    @Override
    public void unlockTitle(ServerPlayerEntity player, ResourceLocation titleKey) {}

    @Override
    public Title getTitle(ResourceLocation titleKey) {
        return Title.NULL_TITLE;
    }
}