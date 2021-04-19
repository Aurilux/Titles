package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncTitles {
    private final CompoundNBT comp;

    public PacketSyncTitles(CompoundNBT comp) {
        this.comp = comp;
    }

    public static void encode(PacketSyncTitles msg, PacketBuffer buf) {
        buf.writeCompoundTag(msg.comp);
    }

    public static PacketSyncTitles decode(PacketBuffer buf) {
        return new PacketSyncTitles(buf.readCompoundTag());
    }

    public static void handle(PacketSyncTitles msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                PlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    TitlesAPI.getCapability(player).ifPresent(cap -> cap.deserializeNBT(msg.comp));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}