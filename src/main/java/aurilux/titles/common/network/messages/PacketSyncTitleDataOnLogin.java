package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.TitleManager;
import aurilux.titles.common.Titles;
import aurilux.titles.common.capability.TitlesImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.lang.reflect.Type;
import java.util.*;

public class PacketSyncTitleDataOnLogin extends AbstractPacket<PacketSyncTitleDataOnLogin> {
    private final Map<UUID, TitleInfo> playerSelectedTitles = new HashMap<>();
    private NBTTagCompound comp;

    public PacketSyncTitleDataOnLogin() {}

    public PacketSyncTitleDataOnLogin(EntityPlayer player) {
        //the player's personal data
        this.comp = TitlesImpl.getCapability(player).serializeNBT();

        //the selected titles of all other players
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        for (EntityPlayerMP temp : playerList.getPlayers()) {
            if (temp.getUniqueID() != player.getUniqueID()) {
                playerSelectedTitles.put(temp.getUniqueID(), TitlesImpl.getCapability(temp).getSelectedTitle());
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, comp);

        buf.writeInt(playerSelectedTitles.entrySet().size());
        for (Map.Entry<UUID, TitleInfo> entry : playerSelectedTitles.entrySet()) {
            ByteBufUtils.writeUTF8String(buf, entry.getKey().toString());
            ByteBufUtils.writeTag(buf, entry.getValue().writeToNBT());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        comp = ByteBufUtils.readTag(buf);

        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            playerSelectedTitles.put(
                    UUID.fromString(ByteBufUtils.readUTF8String(buf)),
                    TitleInfo.readFromNBT(ByteBufUtils.readTag(buf)));
        }
    }

    @Override
    public void handleClientSide(PacketSyncTitleDataOnLogin message, EntityPlayer receiver) {
        TitlesImpl.getCapability(receiver).deserializeNBT(message.comp);

        World world = FMLClientHandler.instance().getWorldClient();
        for (Map.Entry<UUID, TitleInfo> entry : message.playerSelectedTitles.entrySet()) {
            EntityPlayer player = world.getPlayerEntityByUUID(entry.getKey());
            TitlesImpl.getCapability(player).setSelectedTitle(entry.getValue());
        }
    }

    @Override
    public void handleServerSide(PacketSyncTitleDataOnLogin message, EntityPlayer receiver) {
        //NOOP
    }
}