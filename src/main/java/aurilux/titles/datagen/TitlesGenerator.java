package aurilux.titles.datagen;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitleProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Rarity;

import java.util.function.Consumer;

public class TitlesGenerator extends TitleProvider {
    public TitlesGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTitles(Consumer<Title> consumer) {
        registerAdvancementTitles(consumer);
        registerLootTitles(consumer);
    }

    private void registerAdvancementTitles(Consumer<Title> consumer) {
        Title.AwardType awardType = Title.AwardType.ADVANCEMENT;

        // Titles
        Title.Builder.create(awardType)
                .id("titles:captain")
                .defaultDisplay("title.titles.captain")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:chicken_chaser")
                .defaultDisplay("title.titles.chicken_chaser")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:melon_lord")
                .defaultDisplay("title.titles.melon_lord")
                .flavorText("title.titles.melon_lord.flavor")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:opulent")
                .defaultDisplay("title.titles.opulent")
                .flavorText("title.titles.opulent.flavor")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:pincushion")
                .defaultDisplay("title.titles.pincushion")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:spelunker")
                .defaultDisplay("title.titles.spelunker")
                .rarity(Rarity.COMMON)
                .build(consumer);

        // Minecraft
        Title.Builder.create(awardType)
                .id("titles:minecraft/story/iron_tools")
                .defaultDisplay("title.minecraft.story.iron_tools")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/story/obtain_armor")
                .defaultDisplay("title.minecraft.story.obtain_armor")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/story/deflect_arrow")
                .defaultDisplay("title.minecraft.story.deflect_arrow")
                .variantDisplay("title.minecraft.story.deflect_arrow.v")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/story/enchant_item")
                .defaultDisplay("title.minecraft.story.enchant_item")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/story/enter_the_end")
                .defaultDisplay("title.minecraft.story.enter_the_end")
                .flavorText("title.minecraft.story.enter_the_end.flavor")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/story/cure_zombie_villager")
                .defaultDisplay("title.minecraft.story.cure_zombie_villager")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);


        Title.Builder.create(awardType)
                .id("titles:minecraft/husbandry/breed_an_animal")
                .defaultDisplay("title.minecraft.husbandry.breed_an_animal")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/husbandry/fishy_business")
                .defaultDisplay("title.minecraft.husbandry.fishy_business")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/husbandry/bred_all_animals")
                .defaultDisplay("title.minecraft.husbandry.bred_all_animals")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/husbandry/complete_catalogue")
                .defaultDisplay("title.minecraft.husbandry.complete_catalogue")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/husbandry/balanced_diet")
                .defaultDisplay("title.minecraft.husbandry.balanced_diet")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/husbandry/obtain_netherite_hoe")
                .defaultDisplay("title.minecraft.husbandry.obtain_netherite_hoe")
                .rarity(Rarity.RARE)
                .build(consumer);

        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/kill_a_mob")
                .defaultDisplay("title.minecraft.adventure.kill_a_mob")
                .variantDisplay("title.minecraft.adventure.kill_a_mob.v")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/trade")
                .defaultDisplay("title.minecraft.adventure.trade")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/shoot_arrow")
                .defaultDisplay("title.minecraft.adventure.shoot_arrow")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/totem_of_undying")
                .defaultDisplay("title.minecraft.adventure.totem_of_undying")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/summon_iron_golem")
                .defaultDisplay("title.minecraft.adventure.summon_iron_golem")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/hero_of_the_village")
                .defaultDisplay("title.minecraft.adventure.hero_of_the_village")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/kill_all_mobs")
                .defaultDisplay("title.minecraft.adventure.kill_all_mobs")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/two_birds_one_arrow")
                .defaultDisplay("title.minecraft.adventure.two_birds_one_arrow")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/arbalistic")
                .defaultDisplay("title.minecraft.adventure.arbalistic")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/adventuring_time")
                .defaultDisplay("title.minecraft.adventure.adventuring_time")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/sniper_duel")
                .defaultDisplay("title.minecraft.adventure.sniper_duel")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/adventure/bullseye")
                .defaultDisplay("title.minecraft.adventure.bullseye")
                .rarity(Rarity.RARE)
                .build(consumer);

        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/obtain_crying_obsidian")
                .defaultDisplay("title.minecraft.nether.obtain_crying_obsidian")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/ride_strider")
                .defaultDisplay("title.minecraft.nether.ride_strider")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/loot_bastion")
                .defaultDisplay("title.minecraft.nether.loot_bastion")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/charge_respawn_anchor")
                .defaultDisplay("title.minecraft.nether.charge_respawn_anchor")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/brew_potion")
                .defaultDisplay("title.minecraft.nether.brew_potion")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/create_full_beacon")
                .defaultDisplay("title.minecraft.nether.create_full_beacon")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/return_to_sender")
                .defaultDisplay("title.minecraft.nether.return_to_sender")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/fast_travel")
                .defaultDisplay("title.minecraft.nether.fast_travel")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/uneasy_alliance")
                .defaultDisplay("title.minecraft.nether.uneasy_alliance")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/netherite_armor")
                .defaultDisplay("title.minecraft.nether.netherite_armor")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/explore_nether")
                .defaultDisplay("title.minecraft.nether.explore_nether")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/all_potions")
                .defaultDisplay("title.minecraft.nether.all_potions")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/nether/all_effects")
                .defaultDisplay("title.minecraft.nether.all_effects")
                .rarity(Rarity.RARE)
                .build(consumer);

        Title.Builder.create(awardType)
                .id("titles:minecraft/end/kill_dragon")
                .defaultDisplay("title.minecraft.end.kill_dragon")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/end/dragon_egg")
                .defaultDisplay("title.minecraft.end.dragon_egg")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/end/respawn_dragon")
                .defaultDisplay("title.minecraft.end.respawn_dragon")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/end/dragon_breath")
                .defaultDisplay("title.minecraft.end.dragon_breath")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/end/elytra")
                .defaultDisplay("title.minecraft.end.elytra")
                .flavorText("title.minecraft.end.elytra.flavor")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:minecraft/end/levitate")
                .defaultDisplay("title.minecraft.end.levitate")
                .rarity(Rarity.RARE)
                .build(consumer);
    }

    private void registerLootTitles(Consumer<Title> consumer) {
        Title.AwardType awardType = Title.AwardType.LOOT;
        Title.Builder.create(awardType)
                .id("titles:brute")
                .defaultDisplay("the Brute")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:apprentice")
                .defaultDisplay("the Apprentice")
                .rarity(Rarity.COMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:page")
                .defaultDisplay("the Page")
                .rarity(Rarity.COMMON)
                .build(consumer);

        Title.Builder.create(awardType)
                .id("titles:predator")
                .defaultDisplay("the Predator")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:journeyman")
                .defaultDisplay("the Journeyman")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:squire")
                .defaultDisplay("the Squire")
                .rarity(Rarity.UNCOMMON)
                .build(consumer);

        Title.Builder.create(awardType)
                .id("titles:reaper")
                .defaultDisplay("the Reaper")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:master")
                .defaultDisplay("the Master")
                .rarity(Rarity.RARE)
                .build(consumer);
        Title.Builder.create(awardType)
                .id("titles:knight")
                .defaultDisplay("the Knight")
                .rarity(Rarity.RARE)
                .build(consumer);

    }
}