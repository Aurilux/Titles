package aurilux.titles.common.network.messages;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncDataOnLogin {
    private final CompoundNBT comp;
    private final Map<UUID, String> playerDisplayTitles;

    public PacketSyncDataOnLogin(CompoundNBT comp, Map<UUID, String> playerDisplayTitles) {
        this.comp = comp;
        this.playerDisplayTitles = playerDisplayTitles;
    }

    public static void encode(PacketSyncDataOnLogin msg, PacketBuffer buf) {
        buf.writeCompoundTag(msg.comp);

        buf.writeInt(msg.playerDisplayTitles.entrySet().size());
        for (Map.Entry<UUID, String> entry : msg.playerDisplayTitles.entrySet()) {
            buf.writeString(entry.getKey().toString());
            buf.writeString(entry.getValue());
        }
    }

    public static PacketSyncDataOnLogin decode(PacketBuffer buf) {
        CompoundNBT nbt = buf.readCompoundTag();

        Map<UUID, String> map = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            map.put(UUID.fromString(buf.readString()),
                    buf.readString());
        }

        return new PacketSyncDataOnLogin(nbt, map);
    }

    public static void handle(PacketSyncDataOnLogin message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class or else it crashes trying to access ClientPlayerEntity from
            // Minecraft.getInstance().player on the server
            @Override
            public void run() {
                PlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    TitlesAPI.getCapability(player).ifPresent(cap -> cap.deserializeNBT(message.comp));

                    World world = Minecraft.getInstance().world;
                    if (world != null) {
                        for (Map.Entry<UUID, String> entry : message.playerDisplayTitles.entrySet()) {
                            PlayerEntity otherPlayer = world.getPlayerByUuid(entry.getKey());
                            TitlesAPI.setDisplayTitle(otherPlayer, entry.getValue());
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}