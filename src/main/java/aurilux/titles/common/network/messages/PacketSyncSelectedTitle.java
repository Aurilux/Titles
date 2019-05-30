package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.capability.TitlesImpl;
import aurilux.titles.common.network.PacketDispatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public class PacketSyncSelectedTitle extends AbstractPacket<PacketSyncSelectedTitle> {
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

    @Override
    public void handleClientSide(PacketSyncSelectedTitle message, EntityPlayer receiver) {
        EntityPlayer player = FMLClientHandler.instance().getWorldClient().getPlayerEntityByUUID(message.playerUUID);
        if (player != null) {
            TitlesImpl.getCapability(player).setSelectedTitle(message.selectedTitle);
            player.refreshDisplayName();
        }
    }

    @Override
    public void handleServerSide(PacketSyncSelectedTitle message, EntityPlayer receiver) {
        EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(message.playerUUID);
        TitlesImpl.getCapability(player).setSelectedTitle(message.selectedTitle);
        player.refreshDisplayName();
        PacketDispatcher.INSTANCE.sendToAll(new PacketSyncSelectedTitle(message.playerUUID, message.selectedTitle));
    }
}