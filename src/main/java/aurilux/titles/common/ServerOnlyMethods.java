package aurilux.titles.common;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class ServerOnlyMethods {
    public static Player getPlayerByUUID(UUID uuid) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
    }
}