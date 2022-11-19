package aurilux.titles.client;

import aurilux.titles.common.TitlesMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;

public class ClientOnlyMethods {
    public static PlayerEntity getPlayerByUUID(UUID uuid) {
        World world = Minecraft.getInstance().world;
        if (world != null) {
            return world.getPlayerByUuid(uuid);
        }
        return null;
    }
}