package aurilux.titles.common.network.messages;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface ISimplePacket {
    void encode(PacketBuffer buffer);

    void handle(NetworkEvent.Context context);

    static <T extends ISimplePacket> void handle(final T message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        // Have to use anon class or else we'll get classloading problems
        context.enqueueWork(new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                message.handle(context);
            }
        });
        context.setPacketHandled(true);
    }
}