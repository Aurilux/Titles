package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
        UUID uuid = UUID.fromString(buf.readString());
        String titleKey = buf.readString();
        return new PacketSyncSelectedTitle(uuid, titleKey);
    }

    public static void handle(PacketSyncSelectedTitle message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            @Override
            public void run() {
                if (ctx.get().getDirection().getReceptionSide().isClient()) {
                    PlayerEntity player = Minecraft.getInstance().player;
                    TitlesAPI.instance().setDisplayTitle(player, message.selectedTitle);
                }
                else {
                    ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(message.playerUUID);
                    TitlesAPI.instance().setDisplayTitle(player, message.selectedTitle);
                    PacketHandler.sendToAll(new PacketSyncSelectedTitle(message.playerUUID, message.selectedTitle));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}