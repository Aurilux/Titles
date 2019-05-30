package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.TitleManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public class PacketSyncUnlockedTitle extends AbstractPacket<PacketSyncUnlockedTitle> {
    private TitleInfo titleInfo;

    public PacketSyncUnlockedTitle() {}

    public PacketSyncUnlockedTitle(TitleInfo titleInfo) {
        this.titleInfo = titleInfo;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        titleInfo = TitleInfo.readFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, titleInfo.writeToNBT());
    }

    @Override
    public void handleClientSide(PacketSyncUnlockedTitle message, EntityPlayer receiver) {
        TitleManager.addTitle(receiver, message.titleInfo);
    }

    @Override
    public void handleServerSide(PacketSyncUnlockedTitle message, EntityPlayer receiver) {
        //NOOP
    }
}