package aurilux.titles.client;

import aurilux.titles.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;

public class ClientProxy extends CommonProxy {
    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return mc().player;
    }

    @Override
    public PlayerEntity getPlayerByUUID(UUID uuid) {
        World world = mc().world;
        if (world != null) {
            return world.getPlayerByUuid(uuid);
        }
        return null;
    }

    private Minecraft mc() {
        return Minecraft.getInstance();
    }
}