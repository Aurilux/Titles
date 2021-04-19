package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncAllDisplayTitles {
    private final Map<UUID, String> playerDisplayTitles;

    public PacketSyncAllDisplayTitles(Map<UUID, String> playerDisplayTitles) {
        this.playerDisplayTitles = playerDisplayTitles;
    }

    public static void encode(PacketSyncAllDisplayTitles msg, PacketBuffer buf) {
        buf.writeInt(msg.playerDisplayTitles.entrySet().size());
        for (Map.Entry<UUID, String> entry : msg.playerDisplayTitles.entrySet()) {
            buf.writeString(entry.getKey().toString());
            buf.writeString(entry.getValue());
        }
    }

    public static PacketSyncAllDisplayTitles decode(PacketBuffer buf) {
        Map<UUID, String> map = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            map.put(UUID.fromString(buf.readString()),
                    buf.readString());
        }
        return new PacketSyncAllDisplayTitles(map);
    }

    public static void handle(PacketSyncAllDisplayTitles msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                World world = Minecraft.getInstance().world;
                if (world != null) {
                    for (Map.Entry<UUID, String> entry : msg.playerDisplayTitles.entrySet()) {
                        PlayerEntity otherPlayer = world.getPlayerByUuid(entry.getKey());
                        TitlesAPI.setDisplayTitle(otherPlayer, entry.getValue());
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}