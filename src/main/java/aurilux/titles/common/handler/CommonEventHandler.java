package aurilux.titles.common.handler;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.core.TitlesCapability;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncAllDisplayTitles;
import aurilux.titles.common.network.messages.PacketSyncDatapack;
import aurilux.titles.common.network.messages.PacketSyncDisplayTitle;
import aurilux.titles.common.network.messages.PacketSyncTitlesCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TitlesMod.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(TitlesCapability.NAME, new TitlesCapability.Provider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        original.reviveCaps();

        TitleManager.doIfPresent(original, oldCap ->
                TitleManager.doIfPresent(event.getPlayer(), newCap ->
                    newCap.deserializeNBT(oldCap.serializeNBT())));

        original.invalidateCaps();
    }

    /*
    Despite initially seeming redundant with PlayerEvent.Clone, packets cannot be sent during that event so must be
    accompanied by PlayerEvent.PlayerRespawnEvent to properly sync the info
     */
    @SubscribeEvent
    public static void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        TitleManager.doIfPresent(player, cap ->
                TitlesNetwork.toPlayer(new PacketSyncTitlesCapability(cap.serializeNBT()), (ServerPlayer) player));
    }

    @SubscribeEvent
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getPlayer();
        TitleManager.doIfPresent(player, cap ->
            TitlesNetwork.toPlayer(new PacketSyncTitlesCapability(cap.serializeNBT()), (ServerPlayer) player));
    }

    @SubscribeEvent
    public static void onDataPackSync(OnDatapackSyncEvent event) {
        // When event is triggered for a player joining the server
        if (event.getPlayer() != null) {
            TitlesNetwork.toPlayer(new PacketSyncDatapack(), event.getPlayer());
        }
        // When event is triggered for all players, such as doing the reload command
        else {
            for (ServerPlayer player : event.getPlayerList().getPlayers()) {
                TitlesNetwork.toPlayer(new PacketSyncDatapack(), player);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer playerLoggingIn = (ServerPlayer) event.getPlayer();
        TitleManager.doIfPresent(playerLoggingIn, cap -> {
            // Send the just-logged-in player's title data that is loaded on the server to them.
            TitlesNetwork.toPlayer(new PacketSyncTitlesCapability(cap.serializeNBT()), playerLoggingIn);
            // Also send them the display titles of everyone else currently logged in.
            TitlesNetwork.toPlayer(new PacketSyncAllDisplayTitles(getAllDisplayTitles(playerLoggingIn)), playerLoggingIn);
            // Then send their display title to everyone else.
            TitlesNetwork.toAll(new PacketSyncDisplayTitle(playerLoggingIn.getUUID(), cap.getDisplayTitle().getID()));
        });
    }

    private static Map<UUID, ResourceLocation> getAllDisplayTitles(Player player) {
        Map<UUID, ResourceLocation> allSelectedTitles = new HashMap<>();
        for (ServerPlayer serverPlayer : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (serverPlayer.getUUID() != player.getUUID()) {
                TitleManager.doIfPresent(serverPlayer, cap -> {
                    allSelectedTitles.put(serverPlayer.getUUID(), cap.getDisplayTitle().getID());
                });
            }
        }
        return allSelectedTitles;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        Player player = event.getPlayer();
        TitleManager.doIfPresent(player, cap -> {
            event.setDisplayname(TitleManager.getFormattedDisplayName(cap.getDisplayTitle(), player, cap));
        });
    }
}