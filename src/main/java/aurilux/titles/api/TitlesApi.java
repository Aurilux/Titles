package aurilux.titles.api;

import aurilux.titles.common.core.EnumTitleRarity;
import aurilux.titles.common.core.TitleInfo;
import net.minecraft.entity.player.EntityPlayer;
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
    /** A list of all the titles all players have by default and don't need an achievement to unlock */
    public static final ArrayList<TitleInfo> commonTitles = new ArrayList<TitleInfo>();
    /** Stores all in-game available titles by achievement ID */
    public static final Map<String, TitleInfo> titlesByAchievement = new HashMap<String, TitleInfo>();

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
    public static void addTitle(String achievementId, String titleName, EnumTitleRarity titleRarity) {
        titlesByAchievement.put(achievementId, new TitleInfo(titleName, titleRarity));
    }

    /**
     * Adds a commonly available title - one that all players have access with no need to complete an achievement.
     * @param titleNames list of commonly available title names
     */
    public static void addDefaultTitles(String... titleNames) {
        for (String name : titleNames) {
            commonTitles.add(new TitleInfo(name, EnumTitleRarity.common));
        }
    }

    public static boolean titleExists(String achievementId) {
        return titlesByAchievement.containsKey(achievementId);
    }

    //// UTILS
    /**
     * Converts a TitleInfo string into a TitleInfo object
     * @param titleInfo the string to convert
     * @return the TitleInfo object
     */
    public static TitleInfo buildTitleInfo(String titleInfo) {
        String[] ti = titleInfo.split(",");
        return new TitleInfo(ti[0], EnumTitleRarity.valueOf(ti[1]));
    }

    /**
     * Converts a TitleInfo string into a TitleInfo ArrayList
     * @param titleInfo
     * @return the arraylist of all titles
     */
    public static ArrayList<TitleInfo> buildTitleInfoList(String titleInfo) {
        ArrayList<TitleInfo> titles = new ArrayList<TitleInfo>();
        String[] ti = titleInfo.split(":");
        for (String info : ti) {
            titles.add(buildTitleInfo(info));
        }
        return titles;
    }

    /**
     * Converts an ArrayList of TitleInfo into a string to be easily saved in the player's persistent nbt tag
     * @param titles the list of TitleInfo to convert into a string
     * @return the string of titles
     */
    public static String buildTitleString(ArrayList<TitleInfo> titles) {
        String titleString = "";
        for (TitleInfo info : titles) {
            titleString += info.toString() + ":";
        }
        //remove the trailing ':' from the end of the string if we have at least one TitleInfo converted
        return titleString.length() > 0 ? titleString.substring(0, titleString.length() - 1) : titleString;
    }

    public static NBTTagCompound getPersistantPlayerTag(EntityPlayer player) {
        return player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    }
    //// END UTILS
}