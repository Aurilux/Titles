package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.capability.TitlesImpl;
import aurilux.titles.common.network.PacketDispatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketSyncSelectedTitle implements IMessage {
    private UUID playerUUID;
    private TitleInfo selectedTitle;

    public PacketSyncSelectedTitle() {}

    public PacketSyncSelectedTitle(UUID playerUUID, TitleInfo titleInfo) {
        this.playerUUID = playerUUID;
        selectedTitle = titleInfo;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        selectedTitle = TitleInfo.readFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerUUID.toString());
        ByteBufUtils.writeTag(buf, selectedTitle.writeToNBT());
    }

    public static class HandlerClient implements IMessageHandler<PacketSyncSelectedTitle, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncSelectedTitle message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = FMLClientHandler.instance().getWorldClient().getPlayerEntityByUUID(message.playerUUID);
                    if (player != null) {
                        TitlesImpl.getCapability(player).setSelectedTitle(message.selectedTitle);
                        player.refreshDisplayName();
                    }
                }
            });
            return null;
        }
    }

    public static class HandlerServer implements IMessageHandler<PacketSyncSelectedTitle, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncSelectedTitle message, MessageContext ctx) {
            ctx.getServerHandler().player.server.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(message.playerUUID);
                    TitlesImpl.getCapability(player).setSelectedTitle(message.selectedTitle);
                    player.refreshDisplayName();
                    PacketDispatcher.INSTANCE.sendToAll(new PacketSyncSelectedTitle(message.playerUUID, message.selectedTitle));
                }
            });
            return null;
        }
    }
}