package aurilux.titles.common.network;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.network.messages.PacketSendDataOnLogin;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Optional;

public class PacketHandler {
    private static String protocol = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TitlesMod.ID, "main"),
            () -> protocol,
            protocol::equals,
            protocol::equals
    );

    public static void init() {
        int id = 0;
        CHANNEL.registerMessage(id++, PacketSendDataOnLogin.class, PacketSendDataOnLogin::encode,
                PacketSendDataOnLogin::decode, PacketSendDataOnLogin::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, PacketSyncUnlockedTitle.class, PacketSyncUnlockedTitle::encode,
                PacketSyncUnlockedTitle::decode, PacketSyncUnlockedTitle::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(id++, PacketSyncSelectedTitle.class, PacketSyncSelectedTitle::encode,
                PacketSyncSelectedTitle::decode, PacketSyncSelectedTitle::handle);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void sendToAllExcept(Object msg, ServerPlayerEntity ignoredPlayer) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (ignoredPlayer == null || player != ignoredPlayer) {
                CHANNEL.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
            else {
                TitlesMod.LOG.debug("Ignored Player: " + ignoredPlayer.getDisplayName().getString());
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