package aurilux.titles.common.network.messages;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.core.TitleManager;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncLoadedTitles {
    private final Map<ResourceLocation, Title> allLoadedTitles;
    private final int count;

    public PacketSyncLoadedTitles() {
        this(TitleManager.INSTANCE.getRegisteredTitles());
    }

    public PacketSyncLoadedTitles(Map<ResourceLocation, Title> allLoadedTitles) {
        this.allLoadedTitles = allLoadedTitles;
        count = this.allLoadedTitles.size();
    }

    public static void encode(PacketSyncLoadedTitles msg, PacketBuffer buf) {
        buf.writeInt(msg.count);
        for (Map.Entry<ResourceLocation, Title> entry : msg.allLoadedTitles.entrySet()) {
            buf.writeString(entry.getKey().toString());
            buf.writeString(entry.getValue().serializeJSON().getAsString());
        }
    }

    public static PacketSyncLoadedTitles decode(PacketBuffer buf) {
        Map<ResourceLocation, Title> temp = new HashMap<>();
        for (int i = 0; i < buf.readInt(); i++) {
            ResourceLocation res = new ResourceLocation(buf.readString());
            temp.put(res, Title.Builder.deserializeJSON(res, JSONUtils.fromJson(buf.readString())));
        }
        return new PacketSyncLoadedTitles(temp);
    }

    public static void handle(PacketSyncLoadedTitles msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                Map<Title.AwardType, Map<ResourceLocation, Title>> temp = new HashMap<>();
                for (Map.Entry<ResourceLocation, Title> entry : msg.allLoadedTitles.entrySet()) {
                    Title titleEntry = entry.getValue();
                    temp.put(titleEntry.getType(), ImmutableMap.of(entry.getKey(), titleEntry));
                }
                TitleManager.INSTANCE.processServerData(temp);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
