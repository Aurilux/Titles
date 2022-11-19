package aurilux.titles.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class ServerOnlyMethods {
    public static PlayerEntity getPlayerByUUID(UUID uuid) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid);
    }
}
