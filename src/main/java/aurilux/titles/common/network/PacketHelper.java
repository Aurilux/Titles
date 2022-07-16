package aurilux.titles.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class PacketHelper {
    public static PlayerEntity getPlayerByUUID(boolean clientSide, UUID uuid) {
        return clientSide ? Minecraft.getInstance().world.getPlayerByUuid(uuid) :
                ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid);
    }
}