package aurilux.titles.common.network.messages;

import aurilux.titles.common.core.TitleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncAllDisplayTitles {
    private final Map<UUID, ResourceLocation> playerDisplayTitles;

    public PacketSyncAllDisplayTitles(Map<UUID, ResourceLocation> playerDisplayTitles) {
        this.playerDisplayTitles = playerDisplayTitles;
    }

    public static void encode(PacketSyncAllDisplayTitles msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.playerDisplayTitles.entrySet().size());
        for (Map.Entry<UUID, ResourceLocation> entry : msg.playerDisplayTitles.entrySet()) {
            buf.writeUtf(entry.getKey().toString());
            buf.writeUtf(entry.getValue().toString());
        }
    }

    public static PacketSyncAllDisplayTitles decode(FriendlyByteBuf buf) {
        Map<UUID, ResourceLocation> map = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            map.put(UUID.fromString(buf.readUtf()),
                    new ResourceLocation(buf.readUtf()));
        }
        return new PacketSyncAllDisplayTitles(map);
    }

    public static void handle(PacketSyncAllDisplayTitles msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                Level world = Minecraft.getInstance().level;
                if (world != null) {
                    for (Map.Entry<UUID, ResourceLocation> entry : msg.playerDisplayTitles.entrySet()) {
                        Player otherPlayer = world.getPlayerByUUID(entry.getKey());
                        TitleManager.setDisplayTitle(otherPlayer, entry.getValue());
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}