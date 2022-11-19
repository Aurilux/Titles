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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Consumer;
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
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                PlayerEntity player = ServerOnlyMethods.getPlayerByUUID(msg.playerUUID);
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
                    PlayerEntity player = ClientOnlyMethods.getPlayerByUUID(msg.playerUUID);
                    TitleManager.doIfPresent(player, cap -> cap.setGenderSetting(msg.gender));
                    player.refreshDisplayName();
                }
            };
        }
    }
}
