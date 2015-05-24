package aurilux.titles.common.handler;

import aurilux.ardentcore.common.util.AchievementUtils;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.core.EnumTitleRarity;
import aurilux.titles.common.core.TitleInfo;
import aurilux.titles.common.core.Titles;
import aurilux.titles.common.init.ModAchievements;
import aurilux.titles.common.packets.PacketSyncObtainedTitles;
import aurilux.titles.common.packets.PacketSyncSelectedTitles;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [12 Mar 2015]
 */
public class TitleHandler {
    /** Hardcoded list of notable people who get unique titles */
    private final String[] uniqueList = {"Aurilux", "Azanor", "Dinnerbone", "Direwolf20", "Grumm", "Jadedcat",
            "Pahimar", "Soaryn", "VivaDaylight3"};

    @SubscribeEvent
    public void onAchievement(AchievementEvent event) {
        EntityPlayer player = event.entityPlayer;
        if (TitlesApi.titleExists(event.achievement.statId) && !AchievementUtils.isAchievementUnlocked(player, event.achievement)) {
            Titles.proxy.unlockTitle(player, event.achievement.statId);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        //Gets the player's persistent tag that saves title info then reads the unlocked and selected titles
        NBTTagCompound persistentTag = TitlesApi.getPersistantPlayerTag(player);
        String titleString = persistentTag.getString("Titles_unlocked");
        if (!StringUtils.isNullOrEmpty(titleString)) {
            Titles.logger.info("Title info exists for " + player.getCommandSenderName() + ". Retrieving list...");
            Titles.proxy.createProfile(player, TitlesApi.buildTitleInfoList(titleString));
        }
        else {
            Titles.logger.info("Title info does not exist for " + player.getCommandSenderName() + ". Creating new profile...");
            ArrayList<TitleInfo> titles = new ArrayList<TitleInfo>();
            if (Arrays.asList(uniqueList).contains(player.getCommandSenderName())) {
                titles.add(new TitleInfo(player.getCommandSenderName(), EnumTitleRarity.unique));
            }
            titles.addAll(TitlesApi.commonTitles);
            Titles.proxy.createProfile(player, titles);
        }
        String playerSelectedTitle = persistentTag.getString("Titles_selected");
        if (!StringUtils.isNullOrEmpty(playerSelectedTitle)) {
            Titles.proxy.setSelectedTitle(player, TitlesApi.buildTitleInfo(playerSelectedTitle));
        }
        else {
            Titles.proxy.setSelectedTitle(player, TitleInfo.NULL_TITLE);
        }
        player.refreshDisplayName();

        //Send the new player's obtained titles to it's client so they can be accessed in the title selection gui
        Titles.network.sendToPlayer(new PacketSyncObtainedTitles(player.getCommandSenderName()), player);
        //Send the new player's selected title (if any) to everyone else
        Titles.network.sendToAllExcept(new PacketSyncSelectedTitles(player.getCommandSenderName()), player);
        //Send the entire list of selected titles to the new player
        Titles.network.sendToPlayer(new PacketSyncSelectedTitles(), player);
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        EntityPlayer player = event.player;
        //TODO send packet to remove from the server and each client?
        Titles.proxy.selectedTitles.remove(player.getCommandSenderName());
        Titles.proxy.titlesByPlayer.remove(player.getCommandSenderName());
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.source.getSourceOfDamage() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.source.getSourceOfDamage();
            if (event.entity instanceof EntityZombie) {
                AchievementUtils.completeAchievement(player, ModAchievements.zombiesSlain);
            }
            if (event.entity instanceof EntitySkeleton) {
                AchievementUtils.completeAchievement(player, ModAchievements.skeletonsSlain);
            }
            if (event.entity instanceof EntityCreeper) {
                AchievementUtils.completeAchievement(player, ModAchievements.creepersSlain);
            }
            if (event.entity instanceof EntitySpider) {
                AchievementUtils.completeAchievement(player, ModAchievements.spidersSlain);
            }
            if (event.entity instanceof EntityBlaze) {
                AchievementUtils.completeAchievement(player, ModAchievements.blazesSlain);
            }
            if (event.entity instanceof EntityEnderman) {
                AchievementUtils.completeAchievement(player, ModAchievements.endermenSlain);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerNameFormat(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {
        EntityPlayer player = event.entityPlayer;
        TitleInfo currentTitle = Titles.proxy.getSelectedTitle(player.getCommandSenderName());
        if (currentTitle != null && !currentTitle.equals(TitleInfo.NULL_TITLE)) {
            event.displayname += ", " + currentTitle.getFormattedTitle();
        }
    }
}