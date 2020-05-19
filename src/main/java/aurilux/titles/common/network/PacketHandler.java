package aurilux.titles.common.network;

import aurilux.ardentcore.common.util.Debug;
import aurilux.titles.common.Titles;
import aurilux.titles.common.network.messages.PacketSendDataOnLogin;
import aurilux.titles.common.network.messages.PacketSyncFragmentCount;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PacketHandler {
    private static String protocol = "1";
    private static SimpleChannel CHANNEL;

    public static void init() {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Titles.MOD_ID, "main"))
                .networkProtocolVersion(() -> protocol)
                .clientAcceptedVersions(protocol::equals)
                .serverAcceptedVersions(protocol::equals)
                .simpleChannel();

        int id = 0;
        CHANNEL.registerMessage(id++, PacketSendDataOnLogin.class, PacketSendDataOnLogin::encode,
                PacketSendDataOnLogin::decode, PacketSendDataOnLogin::handle);
        CHANNEL.registerMessage(id++, PacketSyncSelectedTitle.class, PacketSyncSelectedTitle::encode,
                PacketSyncSelectedTitle::decode, PacketSyncSelectedTitle::handle);
        CHANNEL.registerMessage(id++, PacketSyncFragmentCount.class, PacketSyncFragmentCount::encode,
                PacketSyncFragmentCount::decode, PacketSyncFragmentCount::handle);
        CHANNEL.registerMessage(id++, PacketSyncUnlockedTitle.class, PacketSyncUnlockedTitle::encode,
                PacketSyncUnlockedTitle::decode, PacketSyncUnlockedTitle::handle);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void sendToAllExcept(Object msg, ServerPlayerEntity ignoredPlayer) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (ignoredPlayer != null && player != ignoredPlayer) {
                CHANNEL.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
            else {
                if (ignoredPlayer != null) {
                    Debug.LOGGER.debug("Ignored Player: " + ignoredPlayer.getDisplayName().getString());
                }
            }
        }
    }

    public static void sendTo(Object msg, ServerPlayerEntity player) {
        CHANNEL.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToAll(Object msg) {
        sendToAllExcept(msg, null);
    }
}