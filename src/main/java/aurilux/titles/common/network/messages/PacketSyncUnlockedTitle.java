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

    public static void handle(PacketSyncUnlockedTitle message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class or else it crashes trying to access ClientPlayerEntity from
            // Minecraft.getInstance().player on the server
            @Override
            public void run() {
                PlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    TitlesAPI.getCapability(player).ifPresent(c -> {
                        c.add(TitlesAPI.internal().getTitle(message.titleKey));
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}