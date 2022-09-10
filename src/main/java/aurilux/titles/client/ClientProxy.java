package aurilux.titles.client;

import aurilux.titles.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ClientProxy extends CommonProxy {
    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public Player getClientPlayer() {
        return mc().player;
    }

    @Override
    public Player getPlayerByUUID(UUID uuid) {
        Level world = mc().level;
        if (world != null) {
            return world.getPlayerByUUID(uuid);
        }
        return null;
    }

    private Minecraft mc() {
        return Minecraft.getInstance();
    }
}