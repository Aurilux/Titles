package aurilux.titles.common.network.messages;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketSyncGenderSetting {
    private final boolean gender;
    private final UUID playerUUID;

    public PacketSyncGenderSetting(UUID uuid, boolean setting) {
        playerUUID = uuid;
        gender = setting;
    }

    public static void encode(PacketSyncGenderSetting msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.playerUUID.toString());
        buf.writeBoolean(msg.gender);
    }

    public static PacketSyncGenderSetting decode(FriendlyByteBuf buf) {
        UUID uuid = UUID.fromString(buf.readUtf(32767));
        boolean setting = buf.readBoolean();
        return new PacketSyncGenderSetting(uuid, setting);
    }

    public static void handle(PacketSyncGenderSetting msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = TitlesMod.PROXY.getPlayerByUUID(msg.playerUUID);
            TitleManager.doIfPresent(player, cap -> cap.setGenderSetting(msg.gender));
            player.refreshDisplayName();

            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                TitlesNetwork.toAll(new PacketSyncGenderSetting(msg.playerUUID, msg.gender));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
