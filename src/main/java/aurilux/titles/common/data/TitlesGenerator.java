package aurilux.titles.common.data;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitleProvider;
import aurilux.titles.common.TitlesMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Rarity;

import java.util.function.Consumer;

public class TitlesGenerator extends TitleProvider {
    public TitlesGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTitles(Consumer<Title> consumer) {
        registerLootTitles(consumer);
        registerAdvancementTitles(consumer);
        registerMinecraftTemplate(consumer);
    }

    private void registerLootTitles(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID)
                .type(Title.AwardType.LOOT)
                .withBuildValidator(consumer);
        builder.id("brute").defaultDisplay("the Brute").build();
        builder.id("apprentice").defaultDisplay("the Apprentice").build();
        builder.id("page").defaultDisplay("the Page").build();

        builder.rarity(Rarity.UNCOMMON);
        builder.id("slayer").defaultDisplay("the Slayer").build();
        builder.id("journeyman").defaultDisplay("the Journeyman").build();
        builder.id("squire").defaultDisplay("the Squire").build();

        builder.rarity(Rarity.RARE);
        builder.id("reaper").defaultDisplay("the Reaper").flavorText("Don't fear it").build();
        builder.id("master").defaultDisplay("the Master").build();
        builder.id("knight").defaultDisplay("the Knight").flavorText("...in shining armor").build();
    }

    private void registerAdvancementTitles(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID).withBuildValidator(consumer);
        builder.genWithName("captain").build();
        builder.genWithName("chicken_chaser").build();
        builder.genWithName("melon_lord", false, true).build();
        builder.genWithName("spelunker").build();

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName("pincushion", false, true).build();

        builder.rarity(Rarity.RARE);
        builder.genWithName("opulent", false, true).build();
    }

    private void registerMinecraftTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID).withBuildValidator(consumer);
        builder.rarity(Rarity.COMMON);
        String subfolder = "_minecraft/story/";
        builder.genWithName(subfolder + "iron_tools").build();
        builder.genWithName(subfolder + "obtain_armor").build();
        builder.genWithName(subfolder + "deflect_arrow", true, false).build();
        builder.genWithName(subfolder + "enchant_item").build();
        builder.genWithName(subfolder + "enter_the_end", false, true).build();

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName(subfolder + "cure_zombie_villager").build();

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/husbandry/";
        builder.genWithName(subfolder + "breed_an_animal").build();
        builder.genWithName(subfolder + "fishy_business").build();

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "bred_all_animals").build();
        builder.genWithName(subfolder + "complete_catalogue").build();
        builder.genWithName(subfolder + "balanced_diet").build();
        builder.genWithName(subfolder + "obtain_netherite_hoe").build();

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/adventure/";
        builder.genWithName(subfolder + "kill_a_mob", true, false).build();
        builder.genWithName(subfolder + "trade").build();
        builder.genWithName(subfolder + "shoot_arrow").build();

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName(subfolder + "totem_of_undying").build();
        builder.genWithName(subfolder + "summon_iron_golem").build();

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "hero_of_the_village").build();
        builder.genWithName(subfolder + "kill_all_mobs").build();
        builder.genWithName(subfolder + "two_birds_one_arrow").build();
        builder.genWithName(subfolder + "arbalistic").build();
        builder.genWithName(subfolder + "adventuring_time").build();
        builder.genWithName(subfolder + "sniper_duel").build();
        builder.genWithName(subfolder + "bullseye").build();

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/nether/";
        builder.genWithName(subfolder + "obtain_crying_obsidian").build();
        builder.genWithName(subfolder + "ride_strider").build();
        builder.genWithName(subfolder + "loot_bastion").build();
        builder.genWithName(subfolder + "charge_respawn_anchor").build();
        builder.genWithName(subfolder + "brew_potion").build();

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName(subfolder + "create_full_beacon").build();

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "return_to_sender").build();
        builder.genWithName(subfolder + "fast_travel").build();
        builder.genWithName(subfolder + "uneasy_alliance").build();
        builder.genWithName(subfolder + "netherite_armor").build();
        builder.genWithName(subfolder + "explore_nether").build();
        builder.genWithName(subfolder + "all_potions").build();
        builder.genWithName(subfolder + "all_effects").build();

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/end/";
        builder.genWithName(subfolder + "kill_dragon").build();

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName(subfolder + "dragon_egg").build();
        builder.genWithName(subfolder + "respawn_dragon").build();
        builder.genWithName(subfolder + "dragon_breath").build();
        builder.genWithName(subfolder + "elytra", false, true).build();

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "levitate").build();
    }
}