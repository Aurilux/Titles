package aurilux.titles.common.handler;

import aurilux.ardentcore.common.util.AchievementUtil;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.core.TitleInfo;
import aurilux.titles.common.core.Titles;
import aurilux.titles.common.init.ModAchievements;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
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
        if (TitlesApi.titleExists(event.achievement.statId) && !AchievementUtil.isAchievementUnlocked(player, event.achievement)) {
            TitlesApi.unlockTitle(player, event.achievement.statId);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        NBTTagCompound persistentTag = TitlesApi.getPersistantPlayerTag(player);
        String titleString = persistentTag.getString("Titles_unlocked");
        if (StringUtils.isNullOrEmpty(titleString)) {
            Titles.logger.info("Unlocked titles tag does not exist. Adding default titles.");
            ArrayList<TitleInfo> titles = new ArrayList<TitleInfo>();
            if (Arrays.asList(uniqueList).contains(player.getCommandSenderName())) {
                titles.add(new TitleInfo(player.getCommandSenderName(), null));
            }
            TitlesApi.createProfile(player, titles, true);
        }
        else {
            Titles.logger.info("Unlocked titles tag exists. Retrieving list.");
            ArrayList<TitleInfo> titles = new ArrayList<TitleInfo>();
            String[] splitTitles = titleString.split(":");
            for (String t : splitTitles) {
                titles.add(TitlesApi.convertToTitleInfo(t));
            }
            TitlesApi.createProfile(player, titles, false);
        }

        String playerSelectedTitle = persistentTag.getString("Titles_selected");
        if (!StringUtils.isNullOrEmpty(playerSelectedTitle)) {
            TitlesApi.setSelectedTitle(player, TitlesApi.convertToTitleInfo(playerSelectedTitle));
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Titles.logger.info("Do I get called at all?");
        EntityPlayer player = event.player;
        TitlesApi.removeSelectedTitle(player);
    }

    @SubscribeEvent
    public void onPlayerNameFormat(net.minecraftforge.event.entity.player.PlayerEvent.NameFormat event) {
        EntityPlayer player = event.entityPlayer;
        TitleInfo currentTitle = TitlesApi.getSelectedTitle(player.getCommandSenderName());
        if (currentTitle != null) {
            event.displayname += currentTitle.getFormattedTitle();
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.source.getSourceOfDamage() != null && event.source.getSourceOfDamage() instanceof EntityPlayerMP) {
            if (event.entity instanceof EntityZombie) {
                AchievementUtil.completeAchievement((EntityPlayerMP) event.source.getSourceOfDamage(), ModAchievements.zombiesSlain);
            }
            if (event.entity instanceof EntitySkeleton) {
                AchievementUtil.completeAchievement((EntityPlayerMP) event.source.getSourceOfDamage(), ModAchievements.skeletonsSlain);
            }
            if (event.entity instanceof EntityCreeper) {
                AchievementUtil.completeAchievement((EntityPlayerMP) event.source.getSourceOfDamage(), ModAchievements.creepersSlain);
            }
            if (event.entity instanceof EntitySpider) {
                AchievementUtil.completeAchievement((EntityPlayerMP) event.source.getSourceOfDamage(), ModAchievements.spidersSlain);
            }
            if (event.entity instanceof EntityBlaze) {
                AchievementUtil.completeAchievement((EntityPlayerMP) event.source.getSourceOfDamage(), ModAchievements.blazesSlain);
            }
            if (event.entity instanceof EntityEnderman) {
                AchievementUtil.completeAchievement((EntityPlayerMP) event.source.getSourceOfDamage(), ModAchievements.endermenSlain);
            }
        }
    }
}