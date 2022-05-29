package aurilux.titles.common.network.messages;

import aurilux.titles.api.Title;
import aurilux.titles.common.core.TitleManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
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
            String test = entry.getKey().toString();
            buf.writeString(test);
            String test2 = entry.getValue().serialize().toString();
            buf.writeString(test2);
        }
    }

    public static PacketSyncLoadedTitles decode(PacketBuffer buf) {
        Map<ResourceLocation, Title> temp = new HashMap<>();
        int testInt = buf.readInt();
        for (int i = 0; i < testInt; i++) {
            String test = buf.readString();
            ResourceLocation res = new ResourceLocation(test);
            String test2 = buf.readString();
            temp.put(res, Title.deserialize(JSONUtils.fromJson(test2)));
        }
        return new PacketSyncLoadedTitles(temp);
    }

    public static void handle(PacketSyncLoadedTitles msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                TitleManager.INSTANCE.processServerData(msg.allLoadedTitles);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}