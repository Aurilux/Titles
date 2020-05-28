package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.Titles;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Arrays;

public class PacketSyncTitleData implements IMessage {
    private NBTTagCompound comp;

    public PacketSyncTitleData() {
    }

    public PacketSyncTitleData(EntityPlayer player) {
        comp = TitlesAPI.getTitlesCap(player).serializeNBT();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, comp);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        comp = ByteBufUtils.readTag(buf);
    }

    public static class HandlerClient implements IMessageHandler<PacketSyncTitleData, IMessage> {
        @Override
        public IMessage onMessage(PacketSyncTitleData message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    TitlesAPI.getTitlesCap(Minecraft.getMinecraft().player).deserializeNBT(message.comp);
                    player.refreshDisplayName();
                }
            });
            return null;
        }
    }
}