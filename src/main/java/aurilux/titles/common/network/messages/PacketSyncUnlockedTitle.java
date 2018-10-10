package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.TitleManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public class PacketSyncUnlockedTitle extends AbstractPacket<PacketSyncUnlockedTitle> {
    private UUID playerUUID;
    private TitleInfo titleInfo;

    public PacketSyncUnlockedTitle() {}

    public PacketSyncUnlockedTitle(UUID playerUUID, TitleInfo titleInfo) {
        this.playerUUID = playerUUID;
        this.titleInfo = titleInfo;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        titleInfo = TitleInfo.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerUUID.toString());
        ByteBufUtils.writeUTF8String(buf, titleInfo.toString());
    }

    @Override
    public void handleClientSide(PacketSyncUnlockedTitle message, EntityPlayer receiver) {
        TitleManager.addTitle(message.playerUUID, message.titleInfo);
    }

    @Override
    public void handleServerSide(PacketSyncUnlockedTitle message, EntityPlayer receiver) {
        //NOOP
    }
}