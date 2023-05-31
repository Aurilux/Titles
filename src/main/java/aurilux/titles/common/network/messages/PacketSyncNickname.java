package aurilux.titles.common.network.messages;

import aurilux.titles.client.ClientOnlyMethods;
import aurilux.titles.common.ServerOnlyMethods;
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

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncNickname {
    private final String nickname;
    private final UUID playerUUID;

    public PacketSyncNickname(UUID uuid, String nick) {
        playerUUID = uuid;
        nickname = nick;
    }

    public static void encode(PacketSyncNickname msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.playerUUID.toString());
        buf.writeUtf(msg.nickname);
    }

    public static PacketSyncNickname decode(FriendlyByteBuf buf) {
        UUID uuid = UUID.fromString(buf.readUtf(32767));
        String nick = buf.readUtf();
        return new PacketSyncNickname(uuid, nick);
    }

    public static void handle(PacketSyncNickname msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                Player player = ServerOnlyMethods.getPlayerByUUID(msg.playerUUID);
                TitleManager.setNickname(player, msg.nickname);
                TitlesNetwork.toAll(new PacketSyncNickname(msg.playerUUID, msg.nickname));
            }
            else {
                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> PacketSyncNickname.Handler.handleClient(msg));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static class Handler {
        public static DistExecutor.SafeRunnable handleClient(PacketSyncNickname msg) {
            // We get an "unsafe referent" error if we turn this into a lambda
            return new DistExecutor.SafeRunnable() {
                @Override
                public void run() {
                    Player player = ClientOnlyMethods.getPlayerByUUID(msg.playerUUID);
                    TitleManager.setNickname(player, msg.nickname);
                }
            };
        }
    }
}