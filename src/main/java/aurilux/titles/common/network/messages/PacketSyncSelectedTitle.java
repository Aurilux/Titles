package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncSelectedTitle {
    private final UUID playerUUID;
    private final String selectedTitle;

    public PacketSyncSelectedTitle(UUID uuid, String titleKey) {
        playerUUID = uuid;
        selectedTitle = titleKey;
    }

    public static void encode(PacketSyncSelectedTitle msg, PacketBuffer buf) {
        buf.writeString(msg.playerUUID.toString());
        buf.writeString(msg.selectedTitle);
    }

    public static PacketSyncSelectedTitle decode(PacketBuffer buf) {
        UUID uuid = UUID.fromString(buf.readString());
        String titleKey = buf.readString();
        return new PacketSyncSelectedTitle(uuid, titleKey);
    }

    public static void handle(PacketSyncSelectedTitle message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            @Override
            public void run() {
                if (ctx.get().getDirection().getReceptionSide().isClient()) {
                    PlayerEntity player = Minecraft.getInstance().player;
                    TitlesAPI.instance().setDisplayTitle(player, message.selectedTitle);
                }
                else {
                    ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(message.playerUUID);
                    TitlesAPI.instance().setDisplayTitle(player, message.selectedTitle);
                    PacketHandler.sendToAllExcept(new PacketSyncSelectedTitle(message.playerUUID, message.selectedTitle), player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
/*
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
 */