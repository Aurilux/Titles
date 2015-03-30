package aurilux.titles.common.init;

import aurilux.ardentcore.common.stats.SequentialAchievement;
import aurilux.ardentcore.common.util.AchievementUtil;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.core.Titles;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
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
    public static Achievement zombieSlayer = AchievementUtil.createAchievement("zombieSlayer", -3, -2, new ItemStack(Items.skull, 1, 2), 10);
    public static Achievement zombieHunter = AchievementUtil.createAchievement("zombieHunter", -1, -2, new ItemStack(Items.skull, 1, 2), zombieSlayer, 25);
    public static Achievement zombieBane = AchievementUtil.createAchievement("zombieBane", 1, -2, new ItemStack(Items.skull, 1, 2), zombieHunter, 75);
    public static Achievement zombieNemesis = AchievementUtil.createAchievement("zombieNemesis", 3, -2, new ItemStack(Items.skull, 1, 2), zombieBane, 200);
    public static Achievement zombieExecutioner = AchievementUtil.createAchievement("zombieExecutioner", 5, -2, new ItemStack(Items.skull, 1, 2), zombieNemesis, 500);
    public static Achievement zombiesSlain = new SequentialAchievement("zombiesSlain", zombieSlayer, zombieHunter, zombieBane, zombieNemesis, zombieExecutioner);

    public static Achievement skeletonSlayer = AchievementUtil.createAchievement("skeletonSlayer", -3, -1, new ItemStack(Items.skull, 1, 0), 10);
    public static Achievement skeletonHunter = AchievementUtil.createAchievement("skeletonHunter", -1, -1, new ItemStack(Items.skull, 1, 0), skeletonSlayer, 25);
    public static Achievement skeletonBane = AchievementUtil.createAchievement("skeletonBane", 1, -1, new ItemStack(Items.skull, 1, 0), skeletonHunter, 75);
    public static Achievement skeletonNemesis = AchievementUtil.createAchievement("skeletonNemesis", 3, -1, new ItemStack(Items.skull, 1, 0), skeletonBane, 200);
    public static Achievement skeletonExecutioner = AchievementUtil.createAchievement("skeletonExecutioner", 5, -1, new ItemStack(Items.skull, 1, 0), skeletonNemesis, 500);
    public static Achievement skeletonsSlain = new SequentialAchievement("skeletonsSlain", skeletonSlayer, skeletonHunter, skeletonBane, skeletonNemesis, skeletonExecutioner);

    public static Achievement creeperSlayer = AchievementUtil.createAchievement("creeperSlayer", -3, 0, new ItemStack(Items.skull, 1, 4), 10);
    public static Achievement creeperHunter = AchievementUtil.createAchievement("creeperHunter", -1, 0, new ItemStack(Items.skull, 1, 4), creeperSlayer, 25);
    public static Achievement creeperBane = AchievementUtil.createAchievement("creeperBane", 1, 0, new ItemStack(Items.skull, 1, 4), creeperHunter, 75);
    public static Achievement creeperNemesis = AchievementUtil.createAchievement("creeperNemesis", 3, 0, new ItemStack(Items.skull, 1, 4), creeperBane, 200);
    public static Achievement creeperExecutioner = AchievementUtil.createAchievement("creeperExecutioner", 5, 0, new ItemStack(Items.skull, 1, 4), creeperNemesis, 500);
    public static Achievement creepersSlain = new SequentialAchievement("creepersSlain", creeperSlayer, creeperHunter, creeperBane, creeperNemesis, creeperExecutioner);

    public static Achievement spiderSlayer = AchievementUtil.createAchievement("spiderSlayer", -3, 1, new ItemStack(Blocks.web), 10);
    public static Achievement spiderHunter = AchievementUtil.createAchievement("spiderHunter", -1, 1, new ItemStack(Blocks.web), spiderSlayer, 25);
    public static Achievement spiderBane = AchievementUtil.createAchievement("spiderBane", 1, 1, new ItemStack(Blocks.web), spiderHunter, 75);
    public static Achievement spiderNemesis = AchievementUtil.createAchievement("spiderNemesis", 3, 1, new ItemStack(Blocks.web), spiderBane, 200);
    public static Achievement spiderExecutioner = AchievementUtil.createAchievement("spiderExecutioner", 5, 1, new ItemStack(Blocks.web), spiderNemesis, 500);
    public static Achievement spidersSlain = new SequentialAchievement("spidersSlain", spiderSlayer, spiderHunter, spiderBane, spiderNemesis, spiderExecutioner);

    public static Achievement blazeSlayer = AchievementUtil.createAchievement("blazeSlayer", -3, 2, new ItemStack(Items.blaze_powder), 10);
    public static Achievement blazeHunter = AchievementUtil.createAchievement("blazeHunter", -1, 2, new ItemStack(Items.blaze_powder), blazeSlayer, 25);
    public static Achievement blazeBane = AchievementUtil.createAchievement("blazeBane", 1, 2, new ItemStack(Items.blaze_powder), blazeHunter, 75);
    public static Achievement blazeNemesis = AchievementUtil.createAchievement("blazeNemesis", 3, 2, new ItemStack(Items.blaze_powder), blazeBane, 200);
    public static Achievement blazeExecutioner = AchievementUtil.createAchievement("blazeExecutioner", 5, 2, new ItemStack(Items.blaze_powder), blazeNemesis, 500);
    public static Achievement blazesSlain = new SequentialAchievement("blazesSlain", blazeSlayer, blazeHunter, blazeBane, blazeNemesis, blazeExecutioner);

    public static Achievement endermanSlayer = AchievementUtil.createAchievement("endermanSlayer", -3, 3, new ItemStack(Items.ender_pearl), 10);
    public static Achievement endermanHunter = AchievementUtil.createAchievement("endermanHunter", -1, 3, new ItemStack(Items.ender_pearl), endermanSlayer, 25);
    public static Achievement endermanBane = AchievementUtil.createAchievement("endermanBane", 1, 3, new ItemStack(Items.ender_pearl), endermanHunter, 75);
    public static Achievement endermanNemesis = AchievementUtil.createAchievement("endermanNemesis", 3, 3, new ItemStack(Items.ender_pearl), endermanBane, 200);
    public static Achievement endermanExecutioner = AchievementUtil.createAchievement("endermanExecutioner", 5, 3, new ItemStack(Items.ender_pearl), endermanNemesis, 500);
    public static Achievement endermenSlain = new SequentialAchievement("endermansSlain", endermanSlayer, endermanHunter, endermanBane, endermanNemesis, endermanExecutioner);

    public static void init() {
        TitlesApi.addDefaultTitles("kind", "brave", "bold", "explorer", "sly", "chickenHerder", "noob", "newb",
                "dogLover", "catLover", "hardy", "beautiful", "amateur");

        TitlesApi.addTitle(AchievementList.enchantments.statId, "enchanter", EnumRarity.uncommon);
        TitlesApi.addTitle(AchievementList.snipeSkeleton.statId, "marksman", EnumRarity.uncommon);
        //Diamonds to you
        TitlesApi.addTitle(AchievementList.field_150966_x.statId, "generous", EnumRarity.rare);
        //Explore all biomes
        TitlesApi.addTitle(AchievementList.field_150961_L.statId, "cartographer", EnumRarity.epic);
        //The Beginning.
        TitlesApi.addTitle(AchievementList.field_150964_J.statId, "witherslayer", EnumRarity.epic);
        TitlesApi.addTitle(AchievementList.theEnd2.statId, "dragonslayer", EnumRarity.epic);

        TitlesApi.addTitle(zombieSlayer.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(zombieHunter.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(zombieBane.statId, EnumRarity.rare);
        TitlesApi.addTitle(zombieNemesis.statId, EnumRarity.rare);
        TitlesApi.addTitle(zombieExecutioner.statId, EnumRarity.epic);

        TitlesApi.addTitle(skeletonSlayer.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(skeletonHunter.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(skeletonBane.statId, EnumRarity.rare);
        TitlesApi.addTitle(skeletonNemesis.statId, EnumRarity.rare);
        TitlesApi.addTitle(skeletonExecutioner.statId, EnumRarity.epic);

        TitlesApi.addTitle(creeperSlayer.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(creeperHunter.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(creeperBane.statId, EnumRarity.rare);
        TitlesApi.addTitle(creeperNemesis.statId, EnumRarity.rare);
        TitlesApi.addTitle(creeperExecutioner.statId, EnumRarity.epic);

        TitlesApi.addTitle(spiderSlayer.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(spiderHunter.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(spiderBane.statId, EnumRarity.rare);
        TitlesApi.addTitle(spiderNemesis.statId, EnumRarity.rare);
        TitlesApi.addTitle(spiderExecutioner.statId, EnumRarity.epic);

        TitlesApi.addTitle(blazeSlayer.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(blazeHunter.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(blazeBane.statId, EnumRarity.rare);
        TitlesApi.addTitle(blazeNemesis.statId, EnumRarity.rare);
        TitlesApi.addTitle(blazeExecutioner.statId, EnumRarity.epic);

        TitlesApi.addTitle(endermanSlayer.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(endermanHunter.statId, EnumRarity.uncommon);
        TitlesApi.addTitle(endermanBane.statId, EnumRarity.rare);
        TitlesApi.addTitle(endermanNemesis.statId, EnumRarity.rare);
        TitlesApi.addTitle(endermanExecutioner.statId, EnumRarity.epic);

        AchievementUtil.createAchievementPage(Titles.MOD_ID,
                zombieSlayer, zombieHunter, zombieBane, zombieNemesis, zombieExecutioner,
                skeletonSlayer, skeletonHunter, skeletonBane, skeletonNemesis, skeletonExecutioner,
                creeperSlayer, creeperHunter, creeperBane, creeperNemesis, creeperExecutioner,
                spiderSlayer, spiderHunter, spiderBane, spiderNemesis, spiderExecutioner,
                blazeSlayer, blazeHunter, blazeBane, blazeNemesis, blazeExecutioner,
                endermanSlayer, endermanHunter, endermanBane, endermanNemesis, endermanExecutioner);
    }
}