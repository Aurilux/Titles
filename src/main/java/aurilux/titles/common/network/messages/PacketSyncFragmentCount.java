package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncFragmentCount implements IMessage {
    private int fragmentCount;

    public PacketSyncFragmentCount() {}

    public PacketSyncFragmentCount(int count) {
        fragmentCount = count;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        fragmentCount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(fragmentCount);
    }

    public static class HandlerClient implements IMessageHandler<PacketSyncFragmentCount, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncFragmentCount message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TitlesAPI.getTitlesCap(Minecraft.getMinecraft().player).setFragmentCount(message.fragmentCount);
                }
            });
            return null;
        }
    }

    public static class HandlerServer implements IMessageHandler<PacketSyncFragmentCount, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncFragmentCount message, MessageContext ctx) {
            ctx.getServerHandler().player.server.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    TitlesAPI.getTitlesCap(player).setFragmentCount(message.fragmentCount);
                }
            });
            return null;
        }
    }
}
