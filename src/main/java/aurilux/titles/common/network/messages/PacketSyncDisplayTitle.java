package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.network.PacketHandler;
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
    private final String selectedTitle;

    public PacketSyncDisplayTitle(UUID uuid, ResourceLocation titleKey) {
        playerUUID = uuid;
        selectedTitle = titleKey.toString();
    }

    public static void encode(PacketSyncDisplayTitle msg, PacketBuffer buf) {
        buf.writeString(msg.playerUUID.toString());
        buf.writeString(msg.selectedTitle);
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
                PlayerEntity player;
                if (ctx.get().getDirection().getReceptionSide().isClient()) {
                    player = Minecraft.getInstance().world.getPlayerByUuid(msg.playerUUID);
                }
                else {
                    player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(msg.playerUUID);
                    PacketHandler.toAll(new PacketSyncDisplayTitle(msg.playerUUID, new ResourceLocation(msg.selectedTitle)));
                }

                if (player != null) {
                    TitlesAPI.setDisplayTitle(player, new ResourceLocation(msg.selectedTitle));
                    player.refreshDisplayName();
                    if (ctx.get().getDirection().getReceptionSide().isClient()) {
                        TitlesMod.LOG.debug("Display title {} for player {} synced to client {}",
                                msg.selectedTitle,
                                player.getName().getString(),
                                Minecraft.getInstance().player.getName().getString());
                    }
                    else {
                        TitlesMod.LOG.debug("Display title for player {} being sent to all clients", player.getName().getString());
                    }
                    TitlesAPI.getCapability(player).ifPresent(c -> {
                        TitlesMod.LOG.debug("Capability exists for {}, and their current display titles is {}",
                                player.getName().getString(),
                                TitlesAPI.getFormattedTitle(c.getDisplayTitle(), c.getGenderSetting()).getString());
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}