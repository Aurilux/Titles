package aurilux.titles.api.handler;

import aurilux.titles.api.Title;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;

public interface IInternalMethodHandler {
    void unlockTitle(ServerPlayerEntity player, ResourceLocation titleKey);
    Title getTitle(ResourceLocation titleKey);
}