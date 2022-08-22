package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PacketSyncDisplayTitle {
    private final UUID playerUUID;
    private final ResourceLocation displayTitle;

    public PacketSyncDisplayTitle(UUID uuid, ResourceLocation titleKey) {
        playerUUID = uuid;
        displayTitle = titleKey;
    }

    public static void encode(PacketSyncDisplayTitle msg, PacketBuffer buf) {
        buf.writeString(msg.playerUUID.toString());
        buf.writeString(msg.displayTitle.toString());
    }

    public static PacketSyncDisplayTitle decode(PacketBuffer buf) {
        UUID uuid = UUID.fromString(buf.readString());
        String titleKey = buf.readString();
        return new PacketSyncDisplayTitle(uuid, new ResourceLocation(titleKey));
    }

    public static void handle(PacketSyncDisplayTitle msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = TitlesMod.PROXY.getPlayerByUUID(msg.playerUUID);
            TitleManager.setDisplayTitle(player, msg.displayTitle);

            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                TitlesNetwork.toAll(new PacketSyncDisplayTitle(msg.playerUUID, msg.displayTitle));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}