package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncDisplayTitle {
    private final UUID playerUUID;
    private final ResourceLocation selectedTitle;

    public PacketSyncDisplayTitle(UUID uuid, ResourceLocation titleKey) {
        playerUUID = uuid;
        selectedTitle = titleKey;
    }

    public static void encode(PacketSyncDisplayTitle msg, PacketBuffer buf) {
        buf.writeString(msg.playerUUID.toString());
        buf.writeString(msg.selectedTitle.toString());
    }

    public static PacketSyncDisplayTitle decode(PacketBuffer buf) {
        UUID uuid = UUID.fromString(buf.readString());
        String titleKey = buf.readString();
        return new PacketSyncDisplayTitle(uuid, new ResourceLocation(titleKey));
    }

    public static void handle(PacketSyncDisplayTitle msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                boolean clientSide = ctx.get().getDirection().getReceptionSide().isClient();
                PlayerEntity player = clientSide ? Minecraft.getInstance().world.getPlayerByUuid(msg.playerUUID) :
                        ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(msg.playerUUID);

                if (player != null) {
                    TitleManager.doIfPresent(player, cap ->
                            cap.setDisplayTitle(TitleManager.getTitle(msg.selectedTitle)));
                    player.refreshDisplayName();
                    if (clientSide) {
                        TitlesMod.LOG.debug("Display title {} for player {} synced to client {}",
                                msg.selectedTitle,
                                player.getName().getString(),
                                Minecraft.getInstance().player.getName().getString());
                    }
                    else {
                        TitlesNetwork.toAll(new PacketSyncDisplayTitle(msg.playerUUID, msg.selectedTitle));
                        TitlesMod.LOG.debug("Display title for player {} being sent to all clients", player.getName().getString());
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}