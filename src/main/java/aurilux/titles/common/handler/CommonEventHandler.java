package aurilux.titles.common.handler;

import aurilux.ardentcore.common.helper.CapabilityHelper;
import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.Titles;
import aurilux.titles.common.impl.TitlesAPIImpl;
import aurilux.titles.common.impl.TitlesImpl;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSendDataOnLogin;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            CapabilityHelper.attach(event, TitlesImpl.NAME, TitlesAPIImpl.TITLES_CAPABILITY, new TitlesImpl());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        TitlesAPI.instance().getCapability(event.getOriginal()).ifPresent(oldCap -> {
            CompoundNBT bags = oldCap.serializeNBT();
            TitlesAPI.instance().getCapability(event.getPlayer()).ifPresent(newCap -> newCap.deserializeNBT(bags));
        });
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity playerLoggingIn = (ServerPlayerEntity) event.getPlayer();

        TitlesAPI.instance().getCapability(playerLoggingIn).ifPresent(cap -> {
            Map<UUID, String> allSelectedTitles = new HashMap<>();
            for (ServerPlayerEntity serverPlayer : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                if (serverPlayer.getUniqueID() != playerLoggingIn.getUniqueID()) {
                    allSelectedTitles.put(serverPlayer.getUniqueID(), cap.getSelectedTitle().getKey());
                }
            }
            //Send the just logged in player's title data that is loaded on the server to them
            PacketHandler.sendTo(new PacketSendDataOnLogin(cap.serializeNBT(), allSelectedTitles), playerLoggingIn);
            //Then send his display title to everyone else
            PacketHandler.sendToAll(new PacketSyncSelectedTitle(playerLoggingIn.getUniqueID(), cap.getSelectedTitle().getKey()));
        });
    }

    @SubscribeEvent
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        TitlesAPI.instance().getCapability(event.getPlayer()).ifPresent(cap -> {
            TitleInfo currentTitle = cap.getSelectedTitle();
            event.setDisplayname(event.getDisplayname() + TitlesAPI.instance().getFormattedTitle(currentTitle, true));
        });
    }
}