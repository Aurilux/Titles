package aurilux.titles.common.network;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.network.messages.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Optional;

public class PacketHandler {
    private final static String protocol = "1";
    private static SimpleChannel CHANNEL;
    private static int index = 0;

    public static void init() {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(TitlesAPI.MOD_ID, "main"))
                .networkProtocolVersion(() -> protocol)
                .clientAcceptedVersions(protocol::equals)
                .serverAcceptedVersions(protocol::equals)
                .simpleChannel();

        // To client
        CHANNEL.registerMessage(index++, PacketSyncUnlockedTitle.class,
                PacketSyncUnlockedTitle::encode,
                PacketSyncUnlockedTitle::decode,
                PacketSyncUnlockedTitle::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(index++, PacketSyncAllDisplayTitles.class,
                PacketSyncAllDisplayTitles::encode,
                PacketSyncAllDisplayTitles::decode,
                PacketSyncAllDisplayTitles::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(index++, PacketSyncTitles.class,
                PacketSyncTitles::encode,
                PacketSyncTitles::decode,
                PacketSyncTitles::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        // To server

        // To both
        CHANNEL.registerMessage(index++, PacketSyncDisplayTitle.class,
                PacketSyncDisplayTitle::encode,
                PacketSyncDisplayTitle::decode,
                PacketSyncDisplayTitle::handle);
        CHANNEL.registerMessage(index++, PacketSyncGenderSetting.class,
                PacketSyncGenderSetting::encode,
                PacketSyncGenderSetting::decode,
                PacketSyncGenderSetting::handle);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void sendTo(Object msg, ServerPlayerEntity player) {
        CHANNEL.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToAll(Object msg) {
        sendToAllExcept(msg, null);
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
}