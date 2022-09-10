package aurilux.titles.common.network.messages;

import aurilux.titles.api.Title;
import aurilux.titles.common.core.TitleRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PacketSyncDatapack {
    private final Map<ResourceLocation, Title> allLoadedTitles;
    private final int count;

    public PacketSyncDatapack() {
        this(TitleRegistry.get().getTitles());
    }

    public PacketSyncDatapack(Map<ResourceLocation, Title> allLoadedTitles) {
        this.allLoadedTitles = allLoadedTitles;
        count = this.allLoadedTitles.size();
    }

    public static void encode(PacketSyncDatapack msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.count);
        for (Map.Entry<ResourceLocation, Title> entry : msg.allLoadedTitles.entrySet()) {
            String test = entry.getKey().toString();
            buf.writeUtf(test);
            String test2 = entry.getValue().serialize().toString();
            buf.writeUtf(test2);
        }
    }

    public static PacketSyncDatapack decode(FriendlyByteBuf buf) {
        Map<ResourceLocation, Title> temp = new HashMap<>();
        int testInt = buf.readInt();
        for (int i = 0; i < testInt; i++) {
            String test = buf.readUtf();
            ResourceLocation res = new ResourceLocation(test);
            String test2 = buf.readUtf();
            temp.put(res, Title.deserialize(GsonHelper.parse(test2)));
        }
        return new PacketSyncDatapack(temp);
    }

    public static void handle(PacketSyncDatapack msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                TitleRegistry.get().processServerData(msg.allLoadedTitles);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
