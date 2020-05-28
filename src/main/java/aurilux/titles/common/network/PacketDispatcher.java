package aurilux.titles.common.network;

import aurilux.titles.common.Titles;
import aurilux.titles.common.network.messages.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketDispatcher {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Titles.MOD_ID);

    public static void init() {
        int disc = 0;
        INSTANCE.registerMessage(PacketSyncDisplayTitle.HandlerServer.class, PacketSyncDisplayTitle.class, disc++, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncFragmentCount.HandlerServer.class, PacketSyncFragmentCount.class, disc++, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncUnlockedTitle.HandlerServer.class, PacketSyncUnlockedTitle.class, disc++, Side.SERVER);

        INSTANCE.registerMessage(PacketSyncDisplayTitle.HandlerClient.class, PacketSyncDisplayTitle.class, disc++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncFragmentCount.HandlerClient.class, PacketSyncFragmentCount.class, disc++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncUnlockedTitle.HandlerClient.class, PacketSyncUnlockedTitle.class, disc++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncRemovedTitle.HandlerClient.class, PacketSyncRemovedTitle.class, disc++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncAllDisplayTitles.HandlerClient.class, PacketSyncAllDisplayTitles.class, disc++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSyncTitleData.HandlerClient.class, PacketSyncTitleData.class, disc++, Side.CLIENT);
    }
}