package aurilux.titles.common.data;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitleProvider;
import aurilux.titles.common.TitlesMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Rarity;

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
        registerBotaniaTemplate(consumer);
        registerIETemplate(consumer);
        registerTFTemplate(consumer);
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
        builder.genWithName("captain");
        builder.genWithName("chicken_chaser", false, true);
        builder.genWithName("melon_lord", false, true);
        builder.genWithName("spelunker");

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName("pincushion");

        builder.rarity(Rarity.RARE);
        builder.genWithName("opulent", false, true);
    }

    private void registerMinecraftTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID).withBuildValidator(consumer);
        builder.rarity(Rarity.COMMON);
        String subfolder = "_minecraft/story/";
        builder.genWithName(subfolder + "iron_tools");
        builder.genWithName(subfolder + "obtain_armor");
        builder.genWithName(subfolder + "deflect_arrow", true, true);
        builder.genWithName(subfolder + "enchant_item");
        builder.genWithName(subfolder + "enter_the_end", false, true);

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName(subfolder + "cure_zombie_villager");

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/husbandry/";
        builder.genWithName(subfolder + "breed_an_animal");
        builder.genWithName(subfolder + "fishy_business");

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "bred_all_animals");
        builder.genWithName(subfolder + "complete_catalogue");
        builder.genWithName(subfolder + "balanced_diet");
        builder.genWithName(subfolder + "obtain_netherite_hoe");

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/adventure/";
        builder.genWithName(subfolder + "kill_a_mob", true, false);
        builder.genWithName(subfolder + "trade");
        builder.genWithName(subfolder + "shoot_arrow");

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName(subfolder + "totem_of_undying");
        builder.genWithName(subfolder + "summon_iron_golem");

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "hero_of_the_village");
        builder.genWithName(subfolder + "kill_all_mobs");
        builder.genWithName(subfolder + "two_birds_one_arrow");
        builder.genWithName(subfolder + "arbalistic");
        builder.genWithName(subfolder + "adventuring_time");
        builder.genWithName(subfolder + "sniper_duel");
        builder.genWithName(subfolder + "bullseye");

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/nether/";
        builder.genWithName(subfolder + "obtain_crying_obsidian");
        builder.genWithName(subfolder + "ride_strider");
        builder.genWithName(subfolder + "loot_bastion");
        builder.genWithName(subfolder + "charge_respawn_anchor");
        builder.genWithName(subfolder + "brew_potion");

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName(subfolder + "create_full_beacon");

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "return_to_sender");
        builder.genWithName(subfolder + "fast_travel");
        builder.genWithName(subfolder + "uneasy_alliance");
        builder.genWithName(subfolder + "netherite_armor");
        builder.genWithName(subfolder + "explore_nether");
        builder.genWithName(subfolder + "all_potions");
        builder.genWithName(subfolder + "all_effects");

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/end/";
        builder.genWithName(subfolder + "kill_dragon");

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName(subfolder + "dragon_egg");
        builder.genWithName(subfolder + "respawn_dragon");
        builder.genWithName(subfolder + "dragon_breath", false, true);
        builder.genWithName(subfolder + "elytra", false, true);

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "levitate");
    }

    private void registerBotaniaTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID).withBuildValidator(consumer);
        builder.rarity(Rarity.COMMON);
        String subfolder = "_botania/main/";
        builder.genWithName(subfolder + "flower_pickup");
        builder.genWithName(subfolder + "cacophonium_craft", false, true);
        builder.genWithName(subfolder + "rune_pickup");
        builder.genWithName(subfolder + "tiny_potato_pet");
        builder.genWithName(subfolder + "pollidisiac_pickup");
        builder.genWithName(subfolder + "manaweave_armor_craft");
        builder.genWithName(subfolder + "spark_craft");
        builder.genWithName(subfolder + "alf_portal_open");
        builder.genWithName(subfolder + "heisei_dream_pickup");
        builder.genWithName(subfolder + "dandelifeon_pickup", false, true);
        builder.genWithName(subfolder + "luminizer_ride");

        builder.rarity(Rarity.RARE);
        subfolder = "_botania/challenge/";
        builder.genWithName(subfolder + "gaia_guardian_hardmode");
        builder.genWithName(subfolder + "gaia_guardian_no_armor");
        builder.genWithName(subfolder + "alf_portal_bread", false, true);
        builder.genWithName(subfolder + "super_corporea_request");
        builder.genWithName(subfolder + "pinkinator");
        builder.genWithName(subfolder + "king_key");
    }

    private void registerIETemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID).withBuildValidator(consumer);
        builder.rarity(Rarity.COMMON);
        String subfolder = "_immersiveengineering/main/";
        builder.genWithName(subfolder + "connect_wire");
        builder.genWithName(subfolder + "place_windmill");
        builder.genWithName(subfolder + "place_floodlight");

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "secret_luckofthedraw");
        builder.genWithName(subfolder + "secret_birthdayparty");
        builder.genWithName(subfolder + "secret_drillbreak", false, true);
        builder.genWithName(subfolder + "mb_excavator");
    }

    private void registerTFTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID).withBuildValidator(consumer);
        builder.rarity(Rarity.COMMON);
        String subfolder = "_twilightforest/";
        builder.genWithName(subfolder + "quest_ram");
        builder.genWithName(subfolder + "progress_troll");

        builder.rarity(Rarity.UNCOMMON);
        builder.genWithName(subfolder + "progress_naga");
        builder.genWithName(subfolder + "progress_lich");
        builder.genWithName(subfolder + "progress_yeti");
        builder.genWithName(subfolder + "progress_glacier");
        builder.genWithName(subfolder + "progress_knight");
        builder.genWithName(subfolder + "progress_ur_ghast");

        builder.rarity(Rarity.RARE);
        builder.genWithName(subfolder + "mazebreaker");
        builder.genWithName(subfolder + "fiery_set");
    }
}