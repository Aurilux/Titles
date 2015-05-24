package aurilux.titles.common.init;

import aurilux.ardentcore.common.stats.ProgressAchievement;
import aurilux.ardentcore.common.stats.SequentialAchievement;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.core.EnumTitleRarity;
import aurilux.titles.common.core.Titles;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [16 Mar 2015]
 */
public class ModAchievements {
    public static final ProgressAchievement zombieSlayer = new ProgressAchievement("achievement.titles.zombieSlayer", "titles.zombieSlayer", -3, -2, new ItemStack(Items.skull, 1, 2), null, 10);
    public static final ProgressAchievement zombieHunter = new ProgressAchievement("achievement.titles.zombieHunter", "titles.zombieHunter", -1, -2, new ItemStack(Items.skull, 1, 2), zombieSlayer, 25);
    public static final ProgressAchievement zombieBane = new ProgressAchievement("achievement.titles.zombieBane", "titles.zombieBane", 1, -2, new ItemStack(Items.skull, 1, 2), zombieHunter, 75);
    public static final ProgressAchievement zombieNemesis = new ProgressAchievement("achievement.titles.zombieNemesis", "titles.zombieNemesis", 3, -2, new ItemStack(Items.skull, 1, 2), zombieBane, 200);
    public static final ProgressAchievement zombieExecutioner = new ProgressAchievement("achievement.titles.zombieExecutioner", "titles.zombieExecutioner", 5, -2, new ItemStack(Items.skull, 1, 2), zombieNemesis, 500);
    public static final Achievement zombiesSlain = new SequentialAchievement("zombiesSlain", zombieSlayer, zombieHunter, zombieBane, zombieNemesis, zombieExecutioner);

    public static final ProgressAchievement skeletonSlayer = new ProgressAchievement("achievement.titles.skeletonSlayer", "titles.skeletonSlayer", -3, -1, new ItemStack(Items.skull, 1, 0), null, 10);
    public static final ProgressAchievement skeletonHunter = new ProgressAchievement("achievement.titles.skeletonHunter", "titles.skeletonHunter", -1, -1, new ItemStack(Items.skull, 1, 0), skeletonSlayer, 25);
    public static final ProgressAchievement skeletonBane = new ProgressAchievement("achievement.titles.skeletonBane", "titles.skeletonBane", 1, -1, new ItemStack(Items.skull, 1, 0), skeletonHunter, 75);
    public static final ProgressAchievement skeletonNemesis = new ProgressAchievement("achievement.titles.skeletonNemesis", "titles.skeletonNemesis", 3, -1, new ItemStack(Items.skull, 1, 0), skeletonBane, 200);
    public static final ProgressAchievement skeletonExecutioner = new ProgressAchievement("achievement.titles.skeletonExecutioner", "titles.skeletonExecutioner", 5, -1, new ItemStack(Items.skull, 1, 0), skeletonNemesis, 500);
    public static final Achievement skeletonsSlain = new SequentialAchievement("skeletonsSlain", skeletonSlayer, skeletonHunter, skeletonBane, skeletonNemesis, skeletonExecutioner);

    public static final ProgressAchievement creeperSlayer = new ProgressAchievement("achievement.titles.creeperSlayer", "titles.creeperSlayer", -3, 0, new ItemStack(Items.skull, 1, 4), null, 10);
    public static final ProgressAchievement creeperHunter = new ProgressAchievement("achievement.titles.creeperHunter", "titles.creeperHunter", -1, 0, new ItemStack(Items.skull, 1, 4), creeperSlayer, 25);
    public static final ProgressAchievement creeperBane = new ProgressAchievement("achievement.titles.creeperBane", "titles.creeperBane", 1, 0, new ItemStack(Items.skull, 1, 4), creeperHunter, 75);
    public static final ProgressAchievement creeperNemesis = new ProgressAchievement("achievement.titles.creeperNemesis", "titles.creeperNemesis", 3, 0, new ItemStack(Items.skull, 1, 4), creeperBane, 200);
    public static final ProgressAchievement creeperExecutioner = new ProgressAchievement("achievement.titles.creeperExecutioner", "titles.creeperExecutioner", 5, 0, new ItemStack(Items.skull, 1, 4), creeperNemesis, 500);
    public static final Achievement creepersSlain = new SequentialAchievement("creepersSlain", creeperSlayer, creeperHunter, creeperBane, creeperNemesis, creeperExecutioner);

    public static final ProgressAchievement spiderSlayer = new ProgressAchievement("achievement.titles.spiderSlayer", "titles.spiderSlayer", -3, 1, new ItemStack(Blocks.web), null, 10);
    public static final ProgressAchievement spiderHunter = new ProgressAchievement("achievement.titles.spiderHunter", "titles.spiderHunter", -1, 1, new ItemStack(Blocks.web), spiderSlayer, 25);
    public static final ProgressAchievement spiderBane = new ProgressAchievement("achievement.titles.spiderBane", "titles.spiderBane", 1, 1, new ItemStack(Blocks.web), spiderHunter, 75);
    public static final ProgressAchievement spiderNemesis = new ProgressAchievement("achievement.titles.spiderNemesis", "titles.spiderNemesis", 3, 1, new ItemStack(Blocks.web), spiderBane, 200);
    public static final ProgressAchievement spiderExecutioner = new ProgressAchievement("achievement.titles.spiderExecutioner", "titles.spiderExecutioner", 5, 1, new ItemStack(Blocks.web), spiderNemesis, 500);
    public static final Achievement spidersSlain = new SequentialAchievement("spidersSlain", spiderSlayer, spiderHunter, spiderBane, spiderNemesis, spiderExecutioner);

    public static final ProgressAchievement blazeSlayer = new ProgressAchievement("achievement.titles.blazeSlayer", "titles.blazeSlayer", -3, 2, new ItemStack(Items.blaze_powder), null, 10);
    public static final ProgressAchievement blazeHunter = new ProgressAchievement("achievement.titles.blazeHunter", "titles.blazeHunter", -1, 2, new ItemStack(Items.blaze_powder), blazeSlayer, 25);
    public static final ProgressAchievement blazeBane = new ProgressAchievement("achievement.titles.blazeBane", "titles.blazeBane", 1, 2, new ItemStack(Items.blaze_powder), blazeHunter, 75);
    public static final ProgressAchievement blazeNemesis = new ProgressAchievement("achievement.titles.blazeNemesis", "titles.blazeNemesis", 3, 2, new ItemStack(Items.blaze_powder), blazeBane, 200);
    public static final ProgressAchievement blazeExecutioner = new ProgressAchievement("achievement.titles.blazeExecutioner", "titles.blazeExecutioner", 5, 2, new ItemStack(Items.blaze_powder), blazeNemesis, 500);
    public static final Achievement blazesSlain = new SequentialAchievement("blazesSlain", blazeSlayer, blazeHunter, blazeBane, blazeNemesis, blazeExecutioner);

    public static final ProgressAchievement endermanSlayer = new ProgressAchievement("achievement.titles.endermanSlayer", "titles.endermanSlayer", -3, 3, new ItemStack(Items.ender_pearl), null, 10);
    public static final ProgressAchievement endermanHunter = new ProgressAchievement("achievement.titles.endermanHunter", "titles.endermanHunter", -1, 3, new ItemStack(Items.ender_pearl), endermanSlayer, 25);
    public static final ProgressAchievement endermanBane = new ProgressAchievement("achievement.titles.endermanBane", "titles.endermanBane", 1, 3, new ItemStack(Items.ender_pearl), endermanHunter, 75);
    public static final ProgressAchievement endermanNemesis = new ProgressAchievement("achievement.titles.endermanNemesis", "titles.endermanNemesis", 3, 3, new ItemStack(Items.ender_pearl), endermanBane, 200);
    public static final ProgressAchievement endermanExecutioner = new ProgressAchievement("achievement.titles.endermanExecutioner", "titles.endermanExecutioner", 5, 3, new ItemStack(Items.ender_pearl), endermanNemesis, 500);
    public static final Achievement endermenSlain = new SequentialAchievement("endermansSlain", endermanSlayer, endermanHunter, endermanBane, endermanNemesis, endermanExecutioner);

    public static void init() {
        AchievementPage page = new AchievementPage(Titles.MOD_ID,
                zombieSlayer, zombieHunter, zombieBane, zombieNemesis, zombieExecutioner,
                skeletonSlayer, skeletonHunter, skeletonBane, skeletonNemesis, skeletonExecutioner,
                creeperSlayer, creeperHunter, creeperBane, creeperNemesis, creeperExecutioner,
                spiderSlayer, spiderHunter, spiderBane, spiderNemesis, spiderExecutioner,
                blazeSlayer, blazeHunter, blazeBane, blazeNemesis, blazeExecutioner,
                endermanSlayer, endermanHunter, endermanBane, endermanNemesis, endermanExecutioner);
        AchievementPage.registerAchievementPage(page);

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

        TitlesApi.addTitle(zombieSlayer.statId, "zombieSlayer", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(zombieHunter.statId, "zombieHunter", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(zombieBane.statId, "zombieBane", EnumTitleRarity.rare);
        TitlesApi.addTitle(zombieNemesis.statId, "zombieNemesis", EnumTitleRarity.rare);
        TitlesApi.addTitle(zombieExecutioner.statId, "zombieExecutioner", EnumTitleRarity.epic);

        TitlesApi.addTitle(skeletonSlayer.statId, "skeletonSlayer", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(skeletonHunter.statId, "skeletonHunter", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(skeletonBane.statId, "skeletonBane", EnumTitleRarity.rare);
        TitlesApi.addTitle(skeletonNemesis.statId, "skeletonNemesis", EnumTitleRarity.rare);
        TitlesApi.addTitle(skeletonExecutioner.statId, "skeletonExecutioner", EnumTitleRarity.epic);

        TitlesApi.addTitle(creeperSlayer.statId, "creeperSlayer", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(creeperHunter.statId, "creeperHunter", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(creeperBane.statId, "creeperBane", EnumTitleRarity.rare);
        TitlesApi.addTitle(creeperNemesis.statId, "creeperNemesis", EnumTitleRarity.rare);
        TitlesApi.addTitle(creeperExecutioner.statId, "creeperExecutioner", EnumTitleRarity.epic);

        TitlesApi.addTitle(spiderSlayer.statId, "spiderSlayer", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(spiderHunter.statId, "spiderHunter", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(spiderBane.statId, "spiderBane", EnumTitleRarity.rare);
        TitlesApi.addTitle(spiderNemesis.statId, "spiderNemesis", EnumTitleRarity.rare);
        TitlesApi.addTitle(spiderExecutioner.statId, "spiderExecutioner", EnumTitleRarity.epic);

        TitlesApi.addTitle(blazeSlayer.statId, "blazeSlayer", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(blazeHunter.statId, "blazeHunter", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(blazeBane.statId, "blazeBane", EnumTitleRarity.rare);
        TitlesApi.addTitle(blazeNemesis.statId, "blazeNemesis", EnumTitleRarity.rare);
        TitlesApi.addTitle(blazeExecutioner.statId, "blazeExecutioner", EnumTitleRarity.epic);

        TitlesApi.addTitle(endermanSlayer.statId, "endermanSlayer", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(endermanHunter.statId, "endermanHunter", EnumTitleRarity.uncommon);
        TitlesApi.addTitle(endermanBane.statId, "endermanBane", EnumTitleRarity.rare);
        TitlesApi.addTitle(endermanNemesis.statId, "endermanNemesis", EnumTitleRarity.rare);
        TitlesApi.addTitle(endermanExecutioner.statId, "endermanExecutioner", EnumTitleRarity.epic);
    }
}