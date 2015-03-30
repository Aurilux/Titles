package aurilux.titles.common.packets;

import aurilux.ardentcore.common.network.AbstractPacket;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.core.TitleInfo;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;

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

    public PacketTitleSelection(){}

    public PacketTitleSelection(TitleInfo titleInfo) {
        newTitle = titleInfo;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String titleId = ByteBufUtils.readUTF8String(buf);
        if (titleId.equals("null")) newTitle = null;
        else {
            String rarity = ByteBufUtils.readUTF8String(buf);
            newTitle = new TitleInfo(titleId, rarity.equals("null") ? null : EnumRarity.valueOf(rarity.toLowerCase()));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (newTitle != null) {
            ByteBufUtils.writeUTF8String(buf, newTitle.titleId);
            ByteBufUtils.writeUTF8String(buf, newTitle.titleRarity == null ? "null" : newTitle.titleRarity.rarityName);
        }
        else {
            ByteBufUtils.writeUTF8String(buf, "null");
            ByteBufUtils.writeUTF8String(buf, "null");
        }
    }

    @Override
    public void handleClientSide(PacketTitleSelection message, EntityPlayer player) {
        //NOOP
    }

    @Override
    public void handleServerSide(PacketTitleSelection message, EntityPlayer player) {
        if (message.newTitle == null) TitlesApi.removeSelectedTitle(player);
        else TitlesApi.setSelectedTitle(player, message.newTitle);
    }
}