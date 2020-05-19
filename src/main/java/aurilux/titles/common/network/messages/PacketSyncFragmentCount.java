package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncFragmentCount {
    private int fragmentCount;

    public PacketSyncFragmentCount(int count) {
        fragmentCount = count;
    }

    public static void encode(PacketSyncFragmentCount msg, PacketBuffer buf) {
        buf.writeInt(msg.fragmentCount);
    }

    public static PacketSyncFragmentCount decode(PacketBuffer buf) {
        return new PacketSyncFragmentCount(buf.readInt());
    }

    public static void handle(PacketSyncFragmentCount message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            @Override
            public void run() {
                if (ctx.get().getDirection().getReceptionSide().isClient()) {
                    PlayerEntity player = Minecraft.getInstance().player;
                    TitlesAPI.instance().getCapability(player).ifPresent(c -> c.setFragmentCount(message.fragmentCount));
                }
                else {
                    ServerPlayerEntity player = ctx.get().getSender();
                    TitlesAPI.instance().getCapability(player).ifPresent(c -> c.setFragmentCount(message.fragmentCount));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}