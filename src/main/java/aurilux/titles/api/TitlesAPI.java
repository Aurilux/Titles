package aurilux.titles.api;

import net.minecraft.item.EnumRarity;
import net.minecraft.stats.Achievement;

import java.util.HashMap;
import java.util.Map;

public final class TitlesAPI {
    /** Stores all in-game available titles by achievement ID */
    public static final Map<String, TitleInfo> titlesByAchievement = new HashMap<>();

    /**
     * Adds an achievement-unlockable title. common rarity titles should be those that are easily achievable, such as
     * the simple achievements the player gets for simply playing the game ("Taking Inventory", "Getting Wood", etc).
     * Uncommon should be those that are more challenging, often marked as special ("Return to Sender", "On a Rail",
     * "The Beginning.", etc). Rare rarity titles should only be awarded for either achievements that are available
     * very late in the game or very challenging to accomplish ("The End.", "Beaconator", "Adventuring Time", etc).
     * Epic rarity titles are reserved by me for renowned members of the Minecraft community (such as Direwolf20 and
     * Notch), Patreon patrons, and people who've had significant influence on my mods.
     *
     * Achievement ID's should be in the format of "achievement.achievementName" just like Vanilla Minecraft does, i.e.
     * "achievement.openInventory". This method will replace the leading "achievement." with "title." to make the title
     * localization key, i.e. "achievement.openInventory" will become "title.openInventory".
     *
     * Example usage:
     * TitlesAPI.addTitle(AchievementList.buildWorkBench, EnumRarity.common);
     *
     * @param achievement the achievement that will unlock the title
     * @param titleRarity the rarity of the title
     */
    public static void addTitle(Achievement achievement, EnumRarity titleRarity) {
        String achievementId = achievement.statId;
        //Epic rarity titles can only be added by me and no one can override another's title
        if (titleRarity.rarityName.equals(EnumRarity.EPIC.rarityName) || titlesByAchievement.containsKey(achievementId)) {
            return;
        }
        String titleName = achievementId.replace("achievement.", "title.");
        titlesByAchievement.put(achievementId, new TitleInfo(titleName, titleRarity));
    }
}
