package aurilux.titles.common.handler;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.entity.merchant.villager.TitleForEmeraldsAndFragmentsTrade;
import aurilux.titles.common.impl.TitlesCapImpl;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncAllDisplayTitles;
import aurilux.titles.common.network.messages.PacketSyncDisplayTitle;
import aurilux.titles.common.network.messages.PacketSyncTitles;
import aurilux.titles.common.util.CapabilityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TitlesAPI.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            CapabilityHelper.attach(event, TitlesCapImpl.NAME, TitlesAPI.TITLES_CAPABILITY, new TitlesCapImpl());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        TitlesAPI.getCapability(event.getOriginal()).ifPresent(oldCap ->
            TitlesAPI.getCapability(event.getPlayer()).ifPresent(newCap -> newCap.deserializeNBT(oldCap.serializeNBT())));
    }

    @SubscribeEvent
    public static void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEntity player = event.getPlayer();
        TitlesAPI.getCapability(player).ifPresent(c ->
                PacketHandler.sendTo(new PacketSyncTitles(c.serializeNBT()), (ServerPlayerEntity) player));
    }

    @SubscribeEvent
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlayerEntity player = event.getPlayer();
        TitlesAPI.getCapability(player).ifPresent(c ->
            PacketHandler.sendTo(new PacketSyncTitles(c.serializeNBT()), (ServerPlayerEntity) player));
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity playerLoggingIn = (ServerPlayerEntity) event.getPlayer();
        TitlesAPI.getCapability(playerLoggingIn).ifPresent(loggingInCap -> {
            // Send the just-logged-in player's title data that is loaded on the server to them.
            PacketHandler.sendTo(new PacketSyncTitles(loggingInCap.serializeNBT()), playerLoggingIn);
            // Also send them the display titles of everyone else currently logged in.
            PacketHandler.sendTo(new PacketSyncAllDisplayTitles(getAllDisplayTitles(playerLoggingIn)), playerLoggingIn);
            // Then send their display title to everyone else.
            PacketHandler.sendToAll(new PacketSyncDisplayTitle(playerLoggingIn.getUniqueID(), loggingInCap.getDisplayTitle().getID()));
        });
    }

    private static Map<UUID, ResourceLocation> getAllDisplayTitles(PlayerEntity player) {
        Map<UUID, ResourceLocation> allSelectedTitles = new HashMap<>();
        for (ServerPlayerEntity serverPlayer : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (serverPlayer.getUniqueID() != player.getUniqueID()) {
                TitlesAPI.getCapability(serverPlayer).ifPresent(cap -> {
                    allSelectedTitles.put(serverPlayer.getUniqueID(), cap.getDisplayTitle().getID());
                });
            }
        }
        return allSelectedTitles;
    }

    @SubscribeEvent
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        PlayerEntity player = event.getPlayer();
        TitlesAPI.getCapability(player).ifPresent(cap ->
            event.setDisplayname(TitlesAPI.getFormattedTitle(cap.getDisplayTitle(), player)));
    }

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType().equals(VillagerProfession.LIBRARIAN)) {
            event.getTrades().get(1).add(new TitleForEmeraldsAndFragmentsTrade(Rarity.COMMON, 3, 5));
            event.getTrades().get(3).add(new TitleForEmeraldsAndFragmentsTrade(Rarity.UNCOMMON, 2, 10));
            event.getTrades().get(5).add(new TitleForEmeraldsAndFragmentsTrade(Rarity.RARE, 1, 30));
        }
    }
}