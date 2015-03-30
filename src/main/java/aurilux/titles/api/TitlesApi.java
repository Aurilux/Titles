package aurilux.titles.api;

import aurilux.titles.common.core.TitleInfo;
import aurilux.titles.common.core.Titles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [19 Mar 2015]
 */
public class TitlesApi {
    /** A list of all the titles all players have by default and dont need an achievement to unlock */
    private static final ArrayList<TitleInfo> commonTitles = new ArrayList<TitleInfo>();
    /** Stores all in-game available titles by achievement ID */
    private static final Map<String, TitleInfo> titlesByAchievement = new HashMap<String, TitleInfo>();
    /** Stores the titles currently selected/displayed by each player */
    private static final Map<String, TitleInfo> selectedTitles = new HashMap<String, TitleInfo>();
    /** Stores all titles specific players have earned */
    private static Map<String, ArrayList<TitleInfo>> titlesByPlayer = new HashMap<String, ArrayList<TitleInfo>>();

    /**
     * Adds an achievement-unlockable title. Uncommon rarity titles should be those that are easily achievable, such as
     * the simple achievements the player get for simply playing the game ("Taking Inventory", "Getting Wood", etc).
     * Rare should be those that are more challenging, often marked as special ("Return to Sender", "On a Rail", "The
     * Beginning.", etc). Epic rarity titles should only be awarded for either achievements that are available very late
     * in the game or very challenging to accomplish ("The End.", "Beaconator", "Adventuring Time", etc).
     * @param achievementId the ID of the achievement that will unlock the title
     * @param titleName the name of the title
     * @param titleRarity the rarity of the title
     */
    public static void addTitle(String achievementId, String titleName, EnumRarity titleRarity) {
        titlesByAchievement.put(achievementId, new TitleInfo(titleName, titleRarity));
    }

    /**
     * Uses the achievementId as the title lang key
     * @param achievementId the ID of the achievement that will unlock the title
     * @param titleRarity the rarity of the title
     */
    public static void addTitle(String achievementId, EnumRarity titleRarity) {
        addTitle(achievementId, achievementId, titleRarity);
    }

    /**
     * Adds a commonly available title - one that all players have access with no need to complete an achievement.
     * @param titleNames list of commonly available title names
     */
    public static void addDefaultTitles(String... titleNames) {
        for (String name : titleNames) {
            commonTitles.add(new TitleInfo(name, EnumRarity.common));
        }
    }

    public static void createProfile(EntityPlayer player, ArrayList<TitleInfo> titles, boolean addDefaults) {
        if (addDefaults) titles.addAll(commonTitles);
        titlesByPlayer.put(player.getCommandSenderName(), titles);

        NBTTagCompound persistentTag = getPersistantPlayerTag(player);
        persistentTag.setString("Titles_unlocked", buildTitleString(titles));
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistentTag);
    }

    public static ArrayList<TitleInfo> getPlayerTitles(String playerName) {
        return titlesByPlayer.get(playerName);
    }

    public static boolean titleExists(String achievementId) {
        return titlesByAchievement.containsKey(achievementId);
    }

    public static void setSelectedTitle(EntityPlayer player, TitleInfo title) {
        selectedTitles.put(player.getCommandSenderName(), title);
        NBTTagCompound persistentTag = getPersistantPlayerTag(player);
        persistentTag.setString("Titles_selected", buildTitleString(title));
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistentTag);
    }

    public static TitleInfo getSelectedTitle(String playerName) {
        return selectedTitles.get(playerName);
    }

    public static void removeSelectedTitle(EntityPlayer player) {
        selectedTitles.remove(player.getCommandSenderName());
        NBTTagCompound persistentTag = getPersistantPlayerTag(player);
        persistentTag.setString("Titles_selected", "");
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistentTag);
    }

    /**
     * Unlocks a title for a player's use
     * @param player the player who just unlocked a title
     * @param achievementId the achievement id the title is linked to
     */
    public static void unlockTitle(EntityPlayer player, String achievementId) {
        ArrayList<TitleInfo> playerTitles = getPlayerTitles(player.getCommandSenderName());
        TitleInfo title = titlesByAchievement.get(achievementId);
        if (title != null) {
            playerTitles.add(title);

            NBTTagCompound persistentTag = getPersistantPlayerTag(player);
            persistentTag.setString("Titles_unlocked", buildTitleString(playerTitles));
            player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistentTag);

            //TODO add chat notification
        }
        else {
            Titles.logger.info(String.format("There is no title attached to achievement %s. Not title was unlocked", achievementId));
        }
    }

    //// UTILS
    /**
     * Converts a string (created by buildTitleString) into a TitleInfo object
     * @param titleInfo the string to convert
     * @return the TitleInfo object
     */
    public static TitleInfo convertToTitleInfo(String titleInfo) {
        String[] ti = titleInfo.split(",");
        return new TitleInfo(ti[0], ti[1].equals("null") ? null : EnumRarity.valueOf(ti[1].toLowerCase()));
    }

    /**
     * Converts an ArrayList of TitleInfo into a string to be easily saved in the player's persistent nbt tag
     * @param titles the list of TitleInfo to convert into a string
     * @return the string of titles
     */
    public static String buildTitleString(ArrayList<TitleInfo> titles) {
        String titleString = "";
        for (TitleInfo info : titles) {
            titleString += buildTitleString(info) + ":";
        }
        //remove the trailing ':' from the end of the string if we have at least one TitleInfo converted
        return titleString.length() > 0 ? titleString.substring(0, titleString.length() - 1) : titleString;
    }

    public static String buildTitleString(TitleInfo title) {
        return title.titleId + "," + (title.titleRarity == null ? "null" : title.titleRarity.rarityName);
    }

    public static NBTTagCompound getPersistantPlayerTag(EntityPlayer player) {
        return player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    }
    //// END UTILS
}