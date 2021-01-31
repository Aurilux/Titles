package aurilux.titles.common.network.messages;


import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
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
        ctx.get().enqueueWork(() -> {
            ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
            if (clientPlayer != null) {
                TitlesAPI.getCapability(clientPlayer).ifPresent(c -> {
                    c.add(TitlesAPI.internal().getTitle(message.titleKey));
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}