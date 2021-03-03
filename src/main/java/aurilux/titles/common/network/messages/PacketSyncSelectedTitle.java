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

public class PacketSyncSelectedTitle {
    private final UUID playerUUID;
    private final String selectedTitle;

    public PacketSyncSelectedTitle(UUID uuid, String titleKey) {
        playerUUID = uuid;
        selectedTitle = titleKey;
    }

    public static void encode(PacketSyncSelectedTitle msg, PacketBuffer buf) {
        buf.writeString(msg.playerUUID.toString());
        buf.writeString(msg.selectedTitle);
    }

    public static PacketSyncSelectedTitle decode(PacketBuffer buf) {
        UUID uuid = UUID.fromString(buf.readString(32767));
        String titleKey = buf.readString(32767);
        return new PacketSyncSelectedTitle(uuid, titleKey);
    }

    public static void handle(PacketSyncSelectedTitle message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player;
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                player = Minecraft.getInstance().world.getPlayerByUuid(message.playerUUID);
            }
            else {
                player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(message.playerUUID);
                PacketHandler.sendToAll(new PacketSyncSelectedTitle(message.playerUUID, message.selectedTitle));
            }

            if (player != null) {
                TitlesAPI.setDisplayTitle(player, message.selectedTitle);
                player.refreshDisplayName();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}