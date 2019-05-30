package aurilux.titles.common.handler;

import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.TitleManager;
import aurilux.titles.common.Titles;
import aurilux.titles.common.capability.TitlesImpl;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import aurilux.titles.common.network.messages.PacketSyncTitleDataOnLogin;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(TitlesImpl.NAME, new TitlesImpl.Provider());
        }
    }

    @SubscribeEvent
    //I have to have the full package name for PlayerEvent.Clone here because otherwise it conflicts with
    //net.minecraftforge.fml.common.gameevent.PlayerEvent.
    public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        //Get data from the old player...
        NBTTagCompound data = TitlesImpl.getCapability(event.getOriginal()).serializeNBT();
        //..and give it to the new clone player
        TitlesImpl.getCapability(event.getEntityPlayer()).deserializeNBT(data);
    }

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

        PacketDispatcher.INSTANCE.sendTo(new PacketSyncTitleDataOnLogin(player), player);
        PacketDispatcher.INSTANCE.sendToAll(new PacketSyncSelectedTitle(player.getUniqueID(), TitleManager.getSelectedTitle(player)));
    }

    @SubscribeEvent
    //I have to have the full package name for PlayerEvent.NameFormat here because otherwise it conflicts with
    //net.minecraftforge.fml.common.gameevent.PlayerEvent.
    public static void onPlayerNameFormat(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {
        TitleInfo currentTitle = TitleManager.getSelectedTitle(event.getEntityPlayer());
        event.setDisplayname(event.getDisplayname() + currentTitle.getFormattedTitle(true));
    }

    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Titles.MOD_ID)) {
            ConfigManager.sync(Titles.MOD_ID, Config.Type.INSTANCE);
        }
    }
}