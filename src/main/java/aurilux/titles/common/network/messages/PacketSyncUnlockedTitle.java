package aurilux.titles.common.network.messages;


import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncUnlockedTitle {
    private String titleKey;

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
            @Override
            public void run() {
                ClientPlayerEntity player = Minecraft.getInstance().player;
                TitlesAPI.instance().getCapability(player).ifPresent(c -> {
                        c.add(TitleRegistry.INSTANCE.getTitle(message.titleKey));
                        TitlesMod.LOG.debug("Synced unlocked title - CLIENT - " + c.serializeNBT());
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}