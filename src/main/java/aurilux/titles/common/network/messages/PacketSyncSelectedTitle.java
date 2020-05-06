package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
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
    private String selectedTitle;

    public PacketSyncSelectedTitle() {}

    public PacketSyncSelectedTitle(UUID playerUUID, String titleKey) {
        this.playerUUID = playerUUID;
        selectedTitle = titleKey;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        selectedTitle = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerUUID.toString());
        ByteBufUtils.writeUTF8String(buf, selectedTitle);
    }

    public static class HandlerClient implements IMessageHandler<PacketSyncSelectedTitle, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncSelectedTitle message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = FMLClientHandler.instance().getWorldClient().getPlayerEntityByUUID(message.playerUUID);
                    if (player != null) {
                        TitlesAPI.setPlayerSelectedTitle(player, TitlesAPI.getTitleFromKey(message.selectedTitle));
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
                    TitlesAPI.setPlayerSelectedTitle(player, TitlesAPI.getTitleFromKey(message.selectedTitle));
                    player.refreshDisplayName();
                    PacketDispatcher.INSTANCE.sendToAll(new PacketSyncSelectedTitle(message.playerUUID, message.selectedTitle));
                }
            });
            return null;
        }
    }
}