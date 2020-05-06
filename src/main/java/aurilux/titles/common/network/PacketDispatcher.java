package aurilux.titles.common.network;

import aurilux.titles.common.Titles;
import aurilux.titles.common.network.messages.PacketSyncFragmentCount;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import aurilux.titles.common.network.messages.PacketSyncTitleDataOnLogin;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketDispatcher {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Titles.MOD_ID);

    public static void init() {
        int disc = 0;
        INSTANCE.registerMessage(PacketSyncSelectedTitle.HandlerServer.class, PacketSyncSelectedTitle.class, disc++, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncFragmentCount.HandlerServer.class, PacketSyncFragmentCount.class, disc++, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncUnlockedTitle.HandlerServer.class, PacketSyncUnlockedTitle.class, disc++, Side.SERVER);

        INSTANCE.registerMessage(PacketSyncSelectedTitle.HandlerClient.class, PacketSyncSelectedTitle.class, disc++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncFragmentCount.HandlerClient.class, PacketSyncFragmentCount.class, disc++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncUnlockedTitle.HandlerClient.class, PacketSyncUnlockedTitle.class, disc++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncTitleDataOnLogin.Handler.class, PacketSyncTitleDataOnLogin.class, disc++, Side.CLIENT);
    }
}