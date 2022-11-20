package aurilux.titles.common.network.messages;

import aurilux.titles.client.ClientOnlyMethods;
import aurilux.titles.common.ServerOnlyMethods;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncDisplayTitle {
    private final UUID playerUUID;
    private final ResourceLocation displayTitle;

    public PacketSyncDisplayTitle(UUID uuid, ResourceLocation titleKey) {
        playerUUID = uuid;
        displayTitle = titleKey;
    }

    public static void encode(PacketSyncDisplayTitle msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.playerUUID.toString());
        buf.writeUtf(msg.displayTitle.toString());
    }

    public static PacketSyncDisplayTitle decode(FriendlyByteBuf buf) {
        UUID uuid = UUID.fromString(buf.readUtf());
        String titleKey = buf.readUtf();
        return new PacketSyncDisplayTitle(uuid, new ResourceLocation(titleKey));
    }

    public static void handle(PacketSyncDisplayTitle msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                Player player = ServerOnlyMethods.getPlayerByUUID(msg.playerUUID);
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
                    Player player = ClientOnlyMethods.getPlayerByUUID(msg.playerUUID);
                    TitleManager.setDisplayTitle(player, msg.displayTitle);
                }
            };
        }
    }
}