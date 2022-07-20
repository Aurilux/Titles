package aurilux.titles.common.network;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.network.messages.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.UUID;

public class TitlesNetwork {
    private final static String protocol = "1";
    private static SimpleChannel CHANNEL;
    private static int index = 0;

    public static void init() {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(TitlesMod.prefix("main"))
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
        CHANNEL.registerMessage(index++, PacketSyncTitlesCapability.class,
                PacketSyncTitlesCapability::encode,
                PacketSyncTitlesCapability::decode,
                PacketSyncTitlesCapability::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(index++, PacketSyncDatapack.class,
                PacketSyncDatapack::encode,
                PacketSyncDatapack::decode,
                PacketSyncDatapack::handle,
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

    public static void toServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void toPlayer(Object msg, ServerPlayerEntity player) {
        CHANNEL.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void toAll(Object msg) {
        toAllExcept(msg, null);
    }

    public static void toAllExcept(Object msg, ServerPlayerEntity ignoredPlayer) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (ignoredPlayer == null || player != ignoredPlayer) {
                toPlayer(msg, player);
            }
            else {
                TitlesMod.LOG.info("Ignored specified player: {}", ignoredPlayer.getDisplayName().getString());
            }
        }
    }

    public static PlayerEntity getPlayerByUUID(boolean clientSide, UUID uuid) {
        if (clientSide) {
            World world = Minecraft.getInstance().world;
            if (world != null) {
                return world.getPlayerByUuid(uuid);
            }
        }
        else {
            return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid);
        }
        return null;
    }
}