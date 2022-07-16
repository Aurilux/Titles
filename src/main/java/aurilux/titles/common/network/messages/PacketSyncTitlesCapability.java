package aurilux.titles.common.network.messages;

import aurilux.titles.common.core.TitleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncTitlesCapability {
    private final CompoundNBT comp;

    public PacketSyncTitlesCapability(CompoundNBT comp) {
        this.comp = comp;
    }

    public static void encode(PacketSyncTitlesCapability msg, PacketBuffer buf) {
        buf.writeCompoundTag(msg.comp);
    }

    public static PacketSyncTitlesCapability decode(PacketBuffer buf) {
        return new PacketSyncTitlesCapability(buf.readCompoundTag());
    }

    public static void handle(PacketSyncTitlesCapability msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                PlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    TitleManager.doIfPresent(player, cap -> cap.deserializeNBT(msg.comp));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}