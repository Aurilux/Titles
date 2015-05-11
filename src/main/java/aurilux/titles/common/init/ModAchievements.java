package aurilux.titles.common.init;

import aurilux.ardentcore.common.stats.SequentialAchievement;
import aurilux.ardentcore.common.util.AchievementUtils;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.core.EnumTitleRarity;
import aurilux.titles.common.core.Titles;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [16 Mar 2015]
 */
public class ModAchievements {
    public static Achievement zombieSlayer = AchievementUtils.createAchievement("zombieSlayer", -3, -2, new ItemStack(Items.skull, 1, 2), 10);
    public static Achievement zombieHunter = AchievementUtils.createAchievement("zombieHunter", -1, -2, new ItemStack(Items.skull, 1, 2), zombieSlayer, 25);
    public static Achievement zombieBane = AchievementUtils.createAchievement("zombieBane", 1, -2, new ItemStack(Items.skull, 1, 2), zombieHunter, 75);
    public static Achievement zombieNemesis = AchievementUtils.createAchievement("zombieNemesis", 3, -2, new ItemStack(Items.skull, 1, 2), zombieBane, 200);
    public static Achievement zombieExecutioner = AchievementUtils.createAchievement("zombieExecutioner", 5, -2, new ItemStack(Items.skull, 1, 2), zombieNemesis, 500);
    public static Achievement zombiesSlain = new SequentialAchievement("zombiesSlain", zombieSlayer, zombieHunter, zombieBane, zombieNemesis, zombieExecutioner);

    public static Achievement skeletonSlayer = AchievementUtils.createAchievement("skeletonSlayer", -3, -1, new ItemStack(Items.skull, 1, 0), 10);
    public static Achievement skeletonHunter = AchievementUtils.createAchievement("skeletonHunter", -1, -1, new ItemStack(Items.skull, 1, 0), skeletonSlayer, 25);
    public static Achievement skeletonBane = AchievementUtils.createAchievement("skeletonBane", 1, -1, new ItemStack(Items.skull, 1, 0), skeletonHunter, 75);
    public static Achievement skeletonNemesis = AchievementUtils.createAchievement("skeletonNemesis", 3, -1, new ItemStack(Items.skull, 1, 0), skeletonBane, 200);
    public static Achievement skeletonExecutioner = AchievementUtils.createAchievement("skeletonExecutioner", 5, -1, new ItemStack(Items.skull, 1, 0), skeletonNemesis, 500);
    public static Achievement skeletonsSlain = new SequentialAchievement("skeletonsSlain", skeletonSlayer, skeletonHunter, skeletonBane, skeletonNemesis, skeletonExecutioner);

    public static Achievement creeperSlayer = AchievementUtils.createAchievement("creeperSlayer", -3, 0, new ItemStack(Items.skull, 1, 4), 10);
    public static Achievement creeperHunter = AchievementUtils.createAchievement("creeperHunter", -1, 0, new ItemStack(Items.skull, 1, 4), creeperSlayer, 25);
    public static Achievement creeperBane = AchievementUtils.createAchievement("creeperBane", 1, 0, new ItemStack(Items.skull, 1, 4), creeperHunter, 75);
    public static Achievement creeperNemesis = AchievementUtils.createAchievement("creeperNemesis", 3, 0, new ItemStack(Items.skull, 1, 4), creeperBane, 200);
    public static Achievement creeperExecutioner = AchievementUtils.createAchievement("creeperExecutioner", 5, 0, new ItemStack(Items.skull, 1, 4), creeperNemesis, 500);
    public static Achievement creepersSlain = new SequentialAchievement("creepersSlain", creeperSlayer, creeperHunter, creeperBane, creeperNemesis, creeperExecutioner);

    public static Achievement spiderSlayer = AchievementUtils.createAchievement("spiderSlayer", -3, 1, new ItemStack(Blocks.web), 10);
    public static Achievement spiderHunter = AchievementUtils.createAchievement("spiderHunter", -1, 1, new ItemStack(Blocks.web), spiderSlayer, 25);
    public static Achievement spiderBane = AchievementUtils.createAchievement("spiderBane", 1, 1, new ItemStack(Blocks.web), spiderHunter, 75);
    public static Achievement spiderNemesis = AchievementUtils.createAchievement("spiderNemesis", 3, 1, new ItemStack(Blocks.web), spiderBane, 200);
    public static Achievement spiderExecutioner = AchievementUtils.createAchievement("spiderExecutioner", 5, 1, new ItemStack(Blocks.web), spiderNemesis, 500);
    public static Achievement spidersSlain = new SequentialAchievement("spidersSlain", spiderSlayer, spiderHunter, spiderBane, spiderNemesis, spiderExecutioner);

    public static Achievement blazeSlayer = AchievementUtils.createAchievement("blazeSlayer", -3, 2, new ItemStack(Items.blaze_powder), 10);
    public static Achievement blazeHunter = AchievementUtils.createAchievement("blazeHunter", -1, 2, new ItemStack(Items.blaze_powder), blazeSlayer, 25);
    public static Achievement blazeBane = AchievementUtils.createAchievement("blazeBane", 1, 2, new ItemStack(Items.blaze_powder), blazeHunter, 75);
    public static Achievement blazeNemesis = AchievementUtils.createAchievement("blazeNemesis", 3, 2, new ItemStack(Items.blaze_powder), blazeBane, 200);
    public static Achievement blazeExecutioner = AchievementUtils.createAchievement("blazeExecutioner", 5, 2, new ItemStack(Items.blaze_powder), blazeNemesis, 500);
    public static Achievement blazesSlain = new SequentialAchievement("blazesSlain", blazeSlayer, blazeHunter, blazeBane, blazeNemesis, blazeExecutioner);

    public static Achievement endermanSlayer = AchievementUtils.createAchievement("endermanSlayer", -3, 3, new ItemStack(Items.ender_pearl), 10);
    public static Achievement endermanHunter = AchievementUtils.createAchievement("endermanHunter", -1, 3, new ItemStack(Items.ender_pearl), endermanSlayer, 25);
    public static Achievement endermanBane = AchievementUtils.createAchievement("endermanBane", 1, 3, new ItemStack(Items.ender_pearl), endermanHunter, 75);
    public static Achievement endermanNemesis = AchievementUtils.createAchievement("endermanNemesis", 3, 3, new ItemStack(Items.ender_pearl), endermanBane, 200);
    public static Achievement endermanExecutioner = AchievementUtils.createAchievement("endermanExecutioner", 5, 3, new ItemStack(Items.ender_pearl), endermanNemesis, 500);
    public static Achievement endermenSlain = new SequentialAchievement("endermansSlain", endermanSlayer, endermanHunter, endermanBane, endermanNemesis, endermanExecutioner);

    public static void init() {
        TitlesApi.addDefaultTitles("kind", "brave", "bold", "explorer", "sly", "chickenHerder", "noob", "newb",
                "dogLover", "catLover", "hardy", "beautiful", "amateur");

        TitlesApi.addTitle(AchievementList.enchantments.statId, "enchanter", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(AchievementList.snipeSkeleton.statId, "marksman", EnumTitleRarity.uncommon);
        //Diamonds to you
        TitlesApi.addTitle(AchievementList.field_150966_x.statId, "generous", EnumTitleRarity.rare);
        //Explore all biomes
        TitlesApi.addTitle(AchievementList.field_150961_L.statId, "cartographer", EnumTitleRarity.epic);
        //The Beginning.
        TitlesApi.addTitle(AchievementList.field_150964_J.statId, "witherslayer", EnumTitleRarity.epic);
        TitlesApi.addTitle(AchievementList.theEnd2.statId, "dragonslayer", EnumTitleRarity.epic);

        TitlesApi.addTitle(zombieSlayer.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(zombieHunter.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(zombieBane.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(zombieNemesis.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(zombieExecutioner.statId, EnumTitleRarity.epic);

        TitlesApi.addTitle(skeletonSlayer.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(skeletonHunter.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(skeletonBane.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(skeletonNemesis.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(skeletonExecutioner.statId, EnumTitleRarity.epic);

        TitlesApi.addTitle(creeperSlayer.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(creeperHunter.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(creeperBane.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(creeperNemesis.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(creeperExecutioner.statId, EnumTitleRarity.epic);

        TitlesApi.addTitle(spiderSlayer.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(spiderHunter.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(spiderBane.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(spiderNemesis.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(spiderExecutioner.statId, EnumTitleRarity.epic);

        TitlesApi.addTitle(blazeSlayer.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(blazeHunter.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(blazeBane.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(blazeNemesis.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(blazeExecutioner.statId, EnumTitleRarity.epic);

        TitlesApi.addTitle(endermanSlayer.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(endermanHunter.statId, EnumTitleRarity.uncommon);
        TitlesApi.addTitle(endermanBane.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(endermanNemesis.statId, EnumTitleRarity.rare);
        TitlesApi.addTitle(endermanExecutioner.statId, EnumTitleRarity.epic);

        AchievementUtils.createAchievementPage(Titles.MOD_ID,
                zombieSlayer, zombieHunter, zombieBane, zombieNemesis, zombieExecutioner,
                skeletonSlayer, skeletonHunter, skeletonBane, skeletonNemesis, skeletonExecutioner,
                creeperSlayer, creeperHunter, creeperBane, creeperNemesis, creeperExecutioner,
                spiderSlayer, spiderHunter, spiderBane, spiderNemesis, spiderExecutioner,
                blazeSlayer, blazeHunter, blazeBane, blazeNemesis, blazeExecutioner,
                endermanSlayer, endermanHunter, endermanBane, endermanNemesis, endermanExecutioner);
    }
}