package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncGenderSetting {
    private final boolean gender;
    private final UUID playerUUID;

    public PacketSyncGenderSetting(UUID uuid, boolean setting) {
        playerUUID = uuid;
        gender = setting;
    }

    public static void encode(PacketSyncGenderSetting msg, PacketBuffer buf) {
        buf.writeString(msg.playerUUID.toString());
        buf.writeBoolean(msg.gender);
    }

    public static PacketSyncGenderSetting decode(PacketBuffer buf) {
        UUID uuid = UUID.fromString(buf.readString(32767));
        boolean setting = buf.readBoolean();
        return new PacketSyncGenderSetting(uuid, setting);
    }

    public static void handle(PacketSyncGenderSetting message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player;
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                player = Minecraft.getInstance().world.getPlayerByUuid(message.playerUUID);
            }
            else {
                player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(message.playerUUID);
                PacketHandler.sendToAll(new PacketSyncGenderSetting(message.playerUUID, message.gender));
            }

            if (player != null) {
                TitlesAPI.getCapability(player).ifPresent(cap -> cap.setGenderSetting(message.gender));
                player.refreshDisplayName();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
