package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.TitleManager;
import aurilux.titles.common.network.PacketDispatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public class PacketTitleSelection extends AbstractPacket<PacketTitleSelection> {
    private UUID playerUUID;
    private TitleInfo selectedTitle;

    public PacketTitleSelection() {}

    public PacketTitleSelection(UUID playerUUID, TitleInfo titleInfo) {
        this.playerUUID = playerUUID;
        selectedTitle = titleInfo;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        selectedTitle = TitleInfo.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerUUID.toString());
        ByteBufUtils.writeUTF8String(buf, selectedTitle.toString());
    }

    @Override
    public void handleClientSide(PacketTitleSelection message, EntityPlayer receiver) {
        TitleManager.setSelectedTitle(message.playerUUID, message.selectedTitle);
        EntityPlayer player = FMLClientHandler.instance().getWorldClient().getPlayerEntityByUUID(message.playerUUID);
        if (player != null) {
            player.refreshDisplayName();
        }
    }

    @Override
    public void handleServerSide(PacketTitleSelection message, EntityPlayer receiver) {
        TitleManager.setSelectedTitle(message.playerUUID, message.selectedTitle);
        EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(message.playerUUID);
        player.refreshDisplayName();
        PacketDispatcher.INSTANCE.sendToAll(new PacketTitleSelection(message.playerUUID, message.selectedTitle));
    }
}