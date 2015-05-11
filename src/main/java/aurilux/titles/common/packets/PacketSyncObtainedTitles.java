package aurilux.titles.common.packets;

import aurilux.ardentcore.common.network.AbstractPacket;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.core.Titles;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [30 Mar 2015]
 */
public class PacketSyncObtainedTitles extends AbstractPacket<PacketSyncObtainedTitles> {
    public String playerName;

    public PacketSyncObtainedTitles() {}

    public PacketSyncObtainedTitles(String name) {
        playerName = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String name = ByteBufUtils.readUTF8String(buf);
        Titles.proxy.titlesByPlayer.put(name, TitlesApi.buildTitleInfoList(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerName);
        ByteBufUtils.writeUTF8String(buf, TitlesApi.buildTitleString(Titles.proxy.titlesByPlayer.get(playerName)));
    }

    @Override
    public void handleClientSide(PacketSyncObtainedTitles message, EntityPlayer player) {
        //NOOP
    }

    @Override
    public void handleServerSide(PacketSyncObtainedTitles message, EntityPlayer player) {
        //NOOP
    }
}
