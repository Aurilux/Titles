package aurilux.titles.common.network.messages;

import aurilux.titles.client.ClientOnlyMethods;
import aurilux.titles.common.ServerOnlyMethods;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncGenderSetting {
    private final boolean gender;
    private final UUID playerUUID;

    public PacketSyncGenderSetting(UUID uuid, boolean setting) {
        playerUUID = uuid;
        gender = setting;
    }

    public static void encode(PacketSyncGenderSetting msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.playerUUID.toString());
        buf.writeBoolean(msg.gender);
    }

    public static PacketSyncGenderSetting decode(FriendlyByteBuf buf) {
        UUID uuid = UUID.fromString(buf.readUtf(32767));
        boolean setting = buf.readBoolean();
        return new PacketSyncGenderSetting(uuid, setting);
    }

    public static void handle(PacketSyncGenderSetting msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                Player player = ServerOnlyMethods.getPlayerByUUID(msg.playerUUID);
                TitleManager.doIfPresent(player, cap -> cap.setGenderSetting(msg.gender));
                player.refreshDisplayName();
                TitlesNetwork.toAll(new PacketSyncGenderSetting(msg.playerUUID, msg.gender));
            }
            else {
                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> PacketSyncGenderSetting.Handler.handleClient(msg));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static class Handler {
        public static DistExecutor.SafeRunnable handleClient(PacketSyncGenderSetting msg) {
            // We get an "unsafe referent" error if we turn this into a lambda
            return new DistExecutor.SafeRunnable() {
                @Override
                public void run() {
                    Player player = ClientOnlyMethods.getPlayerByUUID(msg.playerUUID);
                    TitleManager.doIfPresent(player, cap -> cap.setGenderSetting(msg.gender));
                    player.refreshDisplayName();
                }
            };
        }
    }
}
