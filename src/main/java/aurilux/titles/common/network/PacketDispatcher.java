package aurilux.titles.common.network;

import aurilux.titles.common.Titles;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketDispatcher {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Titles.MOD_ID);
    private static int discriminator = 0;

    private static void registerMessage(Class packet, Side side) {
        INSTANCE.registerMessage(packet, packet, discriminator++, side);
    }

    public static void registerClientMessage(Class packet) {
        registerMessage(packet, Side.CLIENT);
    }

    public static void registerServerMessage(Class packet) {
        registerMessage(packet, Side.SERVER);
    }

    public static void registerDualMessage(Class packet) {
        registerClientMessage(packet);
        registerServerMessage(packet);
    }
}