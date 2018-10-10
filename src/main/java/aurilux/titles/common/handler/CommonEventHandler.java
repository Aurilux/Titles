package aurilux.titles.common.handler;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.TitleManager;
import aurilux.titles.common.Titles;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import aurilux.titles.common.network.messages.PacketSyncTitleDataOnLogin;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        Advancement advancement = event.getAdvancement();
        if (TitleManager.titleExists(advancement)) {
            TitleManager.unlockTitle(player, advancement);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        TitleManager.loadProfile(player);

        PacketDispatcher.INSTANCE.sendTo(new PacketSyncTitleDataOnLogin(player), player);
        PacketDispatcher.INSTANCE.sendToAll(new PacketSyncSelectedTitle(player.getUniqueID()));
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        TitleManager.unloadProfile(event.player);
    }

    @SubscribeEvent
    //I have to have the full package name for PlayerEvent.NameFormat here because otherwise it conflicts with
    //net.minecraftforge.fml.common.gameevent.PlayerEvent.
    public static void onPlayerNameFormat(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {
        TitleInfo currentTitle = TitleManager.getSelectedTitle(event.getEntityPlayer().getUniqueID());
        if (currentTitle != null && !currentTitle.equals(TitleInfo.NULL_TITLE)) {
            event.setDisplayname(event.getDisplayname() + ", " + currentTitle.getFormattedTitle());
        }
    }
}