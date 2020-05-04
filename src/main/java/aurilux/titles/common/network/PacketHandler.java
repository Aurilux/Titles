package aurilux.titles.common.network;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.Titles;
import aurilux.titles.common.network.messages.PacketSendDataOnLogin;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PacketHandler {
    private static String protocol = "1";
    private static SimpleChannel network;

    public static void init() {
        network = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Titles.MOD_ID, "main"))
                .networkProtocolVersion(() -> protocol)
                .clientAcceptedVersions(protocol::equals)
                .serverAcceptedVersions(protocol::equals)
                .simpleChannel();

        int id = 0;
        network.registerMessage(id++, PacketSendDataOnLogin.class, PacketSendDataOnLogin::encode,
                PacketSendDataOnLogin::decode, PacketSendDataOnLogin::handle);
        network.registerMessage(id++, PacketSyncSelectedTitle.class, PacketSyncSelectedTitle::encode,
                PacketSyncSelectedTitle::decode, PacketSyncSelectedTitle::handle);
    }

    public static void syncFragmentCount() {

    }

    public static void syncSelectedTitle(ServerPlayerEntity player) {
        network.sendTo(new PacketSyncSelectedTitle(player.getUniqueID(),
                TitlesAPI.getPlayerSelectedTitle(player).toString()), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
    }

    public static void syncSelectedTitleToAll(ServerPlayerEntity player) {
        sendToAll(new PacketSyncSelectedTitle(player.getUniqueID(), TitlesAPI.getPlayerSelectedTitle(player).toString()));
    }

    public static void syncUnlockedTitle() {

    }

    public static void sendDataOnLogin(ServerPlayerEntity player) {
        network.sendTo(new PacketSendDataOnLogin(player), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        sendToAll(new PacketSyncSelectedTitle(player.getUniqueID(), TitlesAPI.getPlayerSelectedTitle(player).getKey()));
    }

    private static void sendToAll(Object msg) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            network.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}