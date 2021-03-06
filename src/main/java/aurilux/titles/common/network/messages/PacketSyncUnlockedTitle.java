package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncUnlockedTitle {
    private final String titleKey;

    public PacketSyncUnlockedTitle(String titleKey) {
        this.titleKey = titleKey;
    }

    public static void encode(PacketSyncUnlockedTitle msg, PacketBuffer buf) {
        buf.writeString(msg.titleKey);
    }

    public static PacketSyncUnlockedTitle decode(PacketBuffer buf) {
        return new PacketSyncUnlockedTitle(buf.readString());
    }

    public static void handle(PacketSyncUnlockedTitle msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                PlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    TitlesAPI.getCapability(player).ifPresent(c ->
                            c.add(TitlesAPI.internal().getTitle(msg.titleKey)));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}