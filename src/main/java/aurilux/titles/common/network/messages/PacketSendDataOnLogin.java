package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketSendDataOnLogin {
    private Map<UUID, String> playerSelectedTitles = new HashMap<>();
    private final CompoundNBT comp;

    public PacketSendDataOnLogin(CompoundNBT comp, Map<UUID, String> playerSelectedTitles) {
        this.comp = comp;
        this.playerSelectedTitles = playerSelectedTitles;
    }

    public PacketSendDataOnLogin(PlayerEntity player) {
        this.comp = TitlesAPI.getTitlesCap(player).serializeNBT();

        //the selected titles of all other players
        for (ServerPlayerEntity temp : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (temp.getUniqueID() != player.getUniqueID()) {
                playerSelectedTitles.put(temp.getUniqueID(), TitlesAPI.getPlayerSelectedTitle(temp).getKey());
            }
        }
    }

    public static void encode(PacketSendDataOnLogin msg, PacketBuffer buf) {
        buf.writeCompoundTag(msg.comp);

        buf.writeInt(msg.playerSelectedTitles.entrySet().size());
        for (Map.Entry<UUID, String> entry : msg.playerSelectedTitles.entrySet()) {
            buf.writeString(entry.getKey().toString());
            buf.writeString(entry.getValue());
        }
    }

    public static PacketSendDataOnLogin decode(PacketBuffer buf) {
        CompoundNBT nbt = buf.readCompoundTag();

        Map<UUID, String> map = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            map.put(
                    UUID.fromString(buf.readString()),
                    buf.readString());
        }

        return new PacketSendDataOnLogin(nbt, map);
    }

    public static void handle(PacketSendDataOnLogin message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            @Override
            public void run() {
                TitlesAPI.getTitlesCap(Minecraft.getInstance().player).deserializeNBT(message.comp);

                World world = Minecraft.getInstance().world;
                for (Map.Entry<UUID, String> entry : message.playerSelectedTitles.entrySet()) {
                    PlayerEntity player = world.getPlayerByUuid(entry.getKey());
                    TitlesAPI.setPlayerSelectedTitle(player, TitlesAPI.getTitleFromKey(entry.getValue()));
                }
            }
        });
    }
}
/*
public class PacketSendDataOnLogin implements IMessage {
    private final Map<UUID, String> playerSelectedTitles = new HashMap<>();
    private NBTTagCompound comp;

    public PacketSendDataOnLogin() {}

    public PacketSendDataOnLogin(EntityPlayer player) {
        //the player's personal data
        this.comp = TitlesAPI.getTitlesCap(player).serializeNBT();

        //the selected titles of all other players
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();
        for (EntityPlayerMP temp : playerList.getPlayers()) {
            if (temp.getUniqueID() != player.getUniqueID()) {
                playerSelectedTitles.put(temp.getUniqueID(), TitlesAPI.getPlayerSelectedTitle(temp).getKey());
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, comp);

        buf.writeInt(playerSelectedTitles.entrySet().size());
        for (Map.Entry<UUID, String> entry : playerSelectedTitles.entrySet()) {
            ByteBufUtils.writeUTF8String(buf, entry.getKey().toString());
            ByteBufUtils.writeUTF8String(buf, entry.getValue());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        comp = ByteBufUtils.readTag(buf);

        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            playerSelectedTitles.put(
                    UUID.fromString(ByteBufUtils.readUTF8String(buf)),
                    ByteBufUtils.readUTF8String(buf));
        }
    }

    public static class Handler implements IMessageHandler<PacketSendDataOnLogin, IMessage> {
        @Override
        public IMessage onMessage(PacketSendDataOnLogin message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TitlesAPI.getTitlesCap(Minecraft.getMinecraft().player).deserializeNBT(message.comp);

                    World world = FMLClientHandler.instance().getWorldClient();
                    for (Map.Entry<UUID, String> entry : message.playerSelectedTitles.entrySet()) {
                        EntityPlayer player = world.getPlayerEntityByUUID(entry.getKey());
                        TitlesAPI.setPlayerSelectedTitle(player, TitlesAPI.getTitleFromKey(entry.getValue()));
                    }
                }
            });
            return null;
        }
    }
}
*/