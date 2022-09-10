package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncDisplayTitle {
    private final UUID playerUUID;
    private final ResourceLocation displayTitle;

    public PacketSyncDisplayTitle(UUID uuid, ResourceLocation titleKey) {
        playerUUID = uuid;
        displayTitle = titleKey;
    }

    public static void encode(PacketSyncDisplayTitle msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.playerUUID.toString());
        buf.writeUtf(msg.displayTitle.toString());
    }

    public static PacketSyncDisplayTitle decode(FriendlyByteBuf buf) {
        UUID uuid = UUID.fromString(buf.readUtf());
        String titleKey = buf.readUtf();
        return new PacketSyncDisplayTitle(uuid, new ResourceLocation(titleKey));
    }

    public static void handle(PacketSyncDisplayTitle msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = TitlesMod.PROXY.getPlayerByUUID(msg.playerUUID);
            TitleManager.setDisplayTitle(player, msg.displayTitle);

            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                TitlesNetwork.toAll(new PacketSyncDisplayTitle(msg.playerUUID, msg.displayTitle));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}