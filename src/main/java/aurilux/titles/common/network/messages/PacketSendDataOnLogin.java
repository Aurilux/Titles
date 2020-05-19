package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketSendDataOnLogin {
    private Map<UUID, String> playerSelectedTitles;
    private final CompoundNBT comp;

    public PacketSendDataOnLogin(CompoundNBT comp, Map<UUID, String> playerSelectedTitles) {
        this.comp = comp;
        this.playerSelectedTitles = playerSelectedTitles;
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
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(new Runnable() {
                @Override
                public void run() {
                    TitlesAPI.instance().getCapability(Minecraft.getInstance().player).ifPresent(cap -> cap.deserializeNBT(message.comp));

                    World world = Minecraft.getInstance().world;
                    for (Map.Entry<UUID, String> entry : message.playerSelectedTitles.entrySet()) {
                        PlayerEntity player = world.getPlayerByUuid(entry.getKey());
                        TitlesAPI.instance().setDisplayTitle(player, entry.getValue());
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}