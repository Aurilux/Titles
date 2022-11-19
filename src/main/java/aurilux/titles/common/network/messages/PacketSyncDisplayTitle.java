package aurilux.titles.common.network.messages;

import aurilux.titles.client.ClientOnlyMethods;
import aurilux.titles.common.ServerOnlyMethods;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
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
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                PlayerEntity player = ServerOnlyMethods.getPlayerByUUID(msg.playerUUID);
                TitleManager.setDisplayTitle(player, msg.displayTitle);
                TitlesNetwork.toAll(new PacketSyncDisplayTitle(msg.playerUUID, msg.displayTitle));
            }
            else {
                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Handler.handleClient(msg));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static class Handler {
        public static DistExecutor.SafeRunnable handleClient(PacketSyncDisplayTitle msg) {
            // We get an "unsafe referent" error if we turn this into a lambda
            return new DistExecutor.SafeRunnable() {
                @Override
                public void run() {
                    PlayerEntity player = ClientOnlyMethods.getPlayerByUUID(msg.playerUUID);
                    TitleManager.setDisplayTitle(player, msg.displayTitle);
                }
            };
        }
    }
}