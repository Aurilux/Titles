package aurilux.titles.common.packets;

import aurilux.ardentcore.common.network.AbstractPacket;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.core.TitleInfo;
import aurilux.titles.common.core.Titles;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [05 Apr 2015]
 */
public class PacketSyncSelectedTitles extends AbstractPacket<PacketSyncSelectedTitles> {
    public String playerName;

    public PacketSyncSelectedTitles() {}

    public PacketSyncSelectedTitles(String name) {
        playerName = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int numPlayers = buf.readInt();
        for (int i = 0; i < numPlayers; i++) {
            Titles.proxy.selectedTitles.put(ByteBufUtils.readUTF8String(buf), TitlesApi.buildTitleInfo(ByteBufUtils.readUTF8String(buf)));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        //Sending the recently logged in player's selected title to everyone else
        if (!StringUtils.isNullOrEmpty(playerName)) {
            buf.writeInt(1);
            ByteBufUtils.writeUTF8String(buf, playerName);
            Titles.logger.info(Titles.proxy.selectedTitles);
            ByteBufUtils.writeUTF8String(buf, Titles.proxy.selectedTitles.get(playerName).toString());
        }
        //Sending all the titles to the recently logged in player
        else {
            Set<Map.Entry<String, TitleInfo>> selectedTitles = Titles.proxy.selectedTitles.entrySet();
            buf.writeInt(selectedTitles.size());
            for (Map.Entry<String, TitleInfo> selected : selectedTitles) {
                ByteBufUtils.writeUTF8String(buf, selected.getKey());
                ByteBufUtils.writeUTF8String(buf, selected.getValue().toString());
            }
        }
    }

    @Override
    public void handleClientSide(PacketSyncSelectedTitles message, EntityPlayer player) {
        //Refresh each other player's display name on this player's client to update the changes
        for (Object otherPlayer : player.worldObj.playerEntities) {
            if (otherPlayer instanceof EntityOtherPlayerMP) {
                ((EntityPlayer) otherPlayer).refreshDisplayName();
            }
        }
    }

    @Override
    public void handleServerSide(PacketSyncSelectedTitles message, EntityPlayer player) {
        //NOOP
    }
}
