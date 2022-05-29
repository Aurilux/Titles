package aurilux.titles.common.handler;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.impl.TitlesCapImpl;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncAllDisplayTitles;
import aurilux.titles.common.network.messages.PacketSyncDisplayTitle;
import aurilux.titles.common.network.messages.PacketSyncLoadedTitles;
import aurilux.titles.common.network.messages.PacketSyncTitlesCapability;
import aurilux.titles.common.util.CapabilityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
                PacketHandler.toPlayer(new PacketSyncTitlesCapability(c.serializeNBT()), (ServerPlayerEntity) player));
    }

    @SubscribeEvent
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlayerEntity player = event.getPlayer();
        TitlesAPI.getCapability(player).ifPresent(c ->
            PacketHandler.toPlayer(new PacketSyncTitlesCapability(c.serializeNBT()), (ServerPlayerEntity) player));
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity playerLoggingIn = (ServerPlayerEntity) event.getPlayer();
        TitlesAPI.getCapability(playerLoggingIn).ifPresent(loggingInCap -> {
            // Send the just-logged-in player's title data that is loaded on the server to them.
            PacketHandler.toPlayer(new PacketSyncTitlesCapability(loggingInCap.serializeNBT()), playerLoggingIn);
            // Also send them the display titles of everyone else currently logged in.
            PacketHandler.toPlayer(new PacketSyncAllDisplayTitles(getAllDisplayTitles(playerLoggingIn)), playerLoggingIn);
            // Then send their display title to everyone else.
            PacketHandler.toAll(new PacketSyncDisplayTitle(playerLoggingIn.getUniqueID(), loggingInCap.getDisplayTitle().getID()));
        });

        // As of 1.16.5 there seems to be a bug where datapacks aren't being synced to clients who join the server.
        // Players who would otherwise have unlocked titles, found they weren't listed in their selection screen.
        // The solution was for those clients to unlock the same title in single-player, then return to multiplayer.
        // Sending a packet of all the titles loaded on the server fixes this issue, though hopefully this gets remedied
        // in an update to Minecraft or Forge.
        if (!playerLoggingIn.getServer().isServerOwner(playerLoggingIn.getGameProfile())) {
            PacketHandler.toPlayer(new PacketSyncLoadedTitles(), playerLoggingIn);
            TitlesMod.LOG.debug("Synced loaded title data from server to {}", playerLoggingIn.getName().getString());
        }
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
        TitlesAPI.getCapability(player).ifPresent(cap -> {
            event.setDisplayname(TitlesAPI.getFormattedTitle(cap.getDisplayTitle(), player));
        });
    }
}