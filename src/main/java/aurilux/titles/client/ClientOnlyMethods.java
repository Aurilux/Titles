package aurilux.titles.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ClientOnlyMethods {
    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static Player getPlayerByUUID(UUID uuid) {
        Level world = Minecraft.getInstance().level;
        if (world != null) {
            return world.getPlayerByUUID(uuid);
        }
        return null;
    }
}
