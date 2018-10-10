package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.TitleManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public class PacketSyncSelectedTitle extends AbstractPacket<PacketSyncSelectedTitle> {
    private UUID playerUUID;
    private TitleInfo selectedTitle;

    public PacketSyncSelectedTitle() {}

    public PacketSyncSelectedTitle(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.selectedTitle = TitleManager.getSelectedTitle(playerUUID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        selectedTitle = TitleInfo.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, selectedTitle.toString());
    }

    @Override
    public void handleClientSide(PacketSyncSelectedTitle message, EntityPlayer player) {
        TitleManager.setSelectedTitle(message.playerUUID, selectedTitle);
    }

    @Override
    public void handleServerSide(PacketSyncSelectedTitle message, EntityPlayer player) {
        //NOOP
    }
}
