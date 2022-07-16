package aurilux.titles.common.network.messages;

import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
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

    public static void handle(PacketSyncGenderSetting msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                PlayerEntity player;
                if (ctx.get().getDirection().getReceptionSide().isClient()) {
                    player = Minecraft.getInstance().world.getPlayerByUuid(msg.playerUUID);
                }
                else {
                    player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(msg.playerUUID);
                    TitlesNetwork.toAll(new PacketSyncGenderSetting(msg.playerUUID, msg.gender));
                }

                if (player != null) {
                    TitleManager.doIfPresent(player, cap -> cap.setGenderSetting(msg.gender));
                    player.refreshDisplayName();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
