package aurilux.titles.common.handler;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.TitlesCapability;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.entity.merchant.villager.TitleForEmeraldsAndFragmentsTrade;
import aurilux.titles.common.impl.TitlesAPIImpl;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSendDataOnLogin;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import aurilux.titles.common.util.CapabilityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TitlesMod.ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            CapabilityHelper.attach(event, TitlesCapability.NAME, TitlesAPIImpl.TITLES_CAPABILITY, new TitlesCapability());
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
                    allSelectedTitles.put(serverPlayer.getUniqueID(), cap.getDisplayTitle().getKey());
                }
            }

            //Send the just-logged-in player's title data that is loaded on the server to them
            PacketHandler.sendTo(new PacketSendDataOnLogin(cap.serializeNBT(), allSelectedTitles), playerLoggingIn);
            //Then send their display title to everyone else
            PacketHandler.sendToAll(new PacketSyncSelectedTitle(playerLoggingIn.getUniqueID(), cap.getDisplayTitle().getKey()));
        });
    }

    @SubscribeEvent
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        TitlesAPI.instance().getCapability(event.getPlayer()).ifPresent(cap -> {
            Title currentTitle = cap.getDisplayTitle();
            event.setDisplayname(ITextComponent.getTextComponentOrEmpty(event.getDisplayname().getString() + TitlesAPI.instance().getFormattedTitle(currentTitle, true)));
        });
    }

    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType().equals(VillagerProfession.LIBRARIAN)) {
            event.getTrades().get(1).add(new TitleForEmeraldsAndFragmentsTrade(Rarity.COMMON, 5, 5));
            event.getTrades().get(3).add(new TitleForEmeraldsAndFragmentsTrade(Rarity.UNCOMMON, 3, 10));
            event.getTrades().get(5).add(new TitleForEmeraldsAndFragmentsTrade(Rarity.RARE, 1, 30));
        }
    }
}