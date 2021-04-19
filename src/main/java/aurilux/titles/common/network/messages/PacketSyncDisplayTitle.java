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

public class PacketSyncDisplayTitle {
    private final UUID playerUUID;
    private final String selectedTitle;

    public PacketSyncDisplayTitle(UUID uuid, String titleKey) {
        playerUUID = uuid;
        selectedTitle = titleKey;
    }

    public static void encode(PacketSyncDisplayTitle msg, PacketBuffer buf) {
        buf.writeString(msg.playerUUID.toString());
        buf.writeString(msg.selectedTitle);
    }

    public static PacketSyncDisplayTitle decode(PacketBuffer buf) {
        UUID uuid = UUID.fromString(buf.readString(32767));
        String titleKey = buf.readString(32767);
        return new PacketSyncDisplayTitle(uuid, titleKey);
    }

    public static void handle(PacketSyncDisplayTitle msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                PlayerEntity player;
                if (ctx.get().getDirection().getReceptionSide().isClient()) {
                    player = Minecraft.getInstance().world.getPlayerByUuid(msg.playerUUID);
                } else {
                    player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(msg.playerUUID);
                    PacketHandler.sendToAll(new PacketSyncDisplayTitle(msg.playerUUID, msg.selectedTitle));
                }

                if (player != null) {
                    TitlesAPI.setDisplayTitle(player, msg.selectedTitle);
                    player.refreshDisplayName();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}