package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncRemovedTitle implements IMessage {
    private String titleKey;

    public PacketSyncRemovedTitle() {}

    public PacketSyncRemovedTitle(String titleKey) {
        this.titleKey = titleKey;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        titleKey = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, titleKey);
    }

    public static class HandlerClient implements IMessageHandler<PacketSyncRemovedTitle, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncRemovedTitle message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    TitlesAPI.removeTitleFromPlayer(player, message.titleKey);
                    player.refreshDisplayName();
                }
            });
            return null;
        }
    }
}