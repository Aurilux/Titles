package aurilux.titles.common.packets;

import aurilux.ardentcore.common.network.AbstractPacket;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.core.TitleInfo;
import aurilux.titles.common.core.Titles;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [25 Mar 2015]
 */
public class PacketTitleSelection extends AbstractPacket<PacketTitleSelection> {
    public TitleInfo newTitle;

    public PacketTitleSelection() {}

    public PacketTitleSelection(TitleInfo titleInfo) {
        newTitle = titleInfo;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        newTitle = TitlesApi.buildTitleInfo(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, newTitle.toString());
    }

    @Override
    public void handleClientSide(PacketTitleSelection message, EntityPlayer player) {
        //NOOP
    }

    @Override
    public void handleServerSide(PacketTitleSelection message, EntityPlayer player) {
        Titles.proxy.setSelectedTitle(player, message.newTitle);
        Titles.network.sendToAllExcept(new PacketSyncSelectedTitles(), (EntityPlayerMP) player);
    }
}