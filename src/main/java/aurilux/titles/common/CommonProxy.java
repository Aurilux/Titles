package aurilux.titles.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class CommonProxy {
    public boolean isClient() {
        return false;
    }

    public PlayerEntity getClientPlayer() {
        return null;
    }

    public PlayerEntity getPlayerByUUID(UUID uuid) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid);
    }
}
