package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketSyncTitleDataOnLogin implements IMessage {
    private final Map<UUID, TitleInfo> playerSelectedTitles = new HashMap<>();
    private NBTTagCompound comp;

    public PacketSyncTitleDataOnLogin() {}

    public PacketSyncTitleDataOnLogin(EntityPlayer player) {
        //the player's personal data
        this.comp = TitlesAPI.getTitlesCap(player).serializeNBT();

        //the selected titles of all other players
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        for (EntityPlayerMP temp : playerList.getPlayers()) {
            if (temp.getUniqueID() != player.getUniqueID()) {
                playerSelectedTitles.put(temp.getUniqueID(), TitlesAPI.getSelectedTitle(temp));
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

    public static class Handler implements IMessageHandler<PacketSyncTitleDataOnLogin, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncTitleDataOnLogin message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TitlesAPI.getTitlesCap(Minecraft.getMinecraft().player).deserializeNBT(message.comp);

                    World world = FMLClientHandler.instance().getWorldClient();
                    for (Map.Entry<UUID, TitleInfo> entry : message.playerSelectedTitles.entrySet()) {
                        EntityPlayer player = world.getPlayerEntityByUUID(entry.getKey());
                        TitlesAPI.getTitlesCap(player).setSelectedTitle(entry.getValue());
                    }
                }
            });
            return null;
        }
    }
}