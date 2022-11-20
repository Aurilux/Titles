package aurilux.titles.common.network.messages;

import aurilux.titles.common.core.TitleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncTitlesCapability {
    private final CompoundTag comp;

    public PacketSyncTitlesCapability(CompoundTag comp) {
        this.comp = comp;
    }

    public static void encode(PacketSyncTitlesCapability msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.comp);
    }

    public static PacketSyncTitlesCapability decode(FriendlyByteBuf buf) {
        return new PacketSyncTitlesCapability(buf.readNbt());
    }

    public static void handle(PacketSyncTitlesCapability msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    TitleManager.doIfPresent(player, cap -> cap.deserializeNBT(msg.comp));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}