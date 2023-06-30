package aurilux.titles.common.data;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitleProvider;
import aurilux.titles.common.TitlesMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
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
                .type(Title.AwardType.LOOT);

        builder.id("brute").defaultDisplay("the Brute").save(consumer);
        builder.id("apprentice").defaultDisplay("Apprentice").setPrefix().save(consumer);
        builder.id("page").defaultDisplay("Page").setPrefix().save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        builder.id("slayer").defaultDisplay("the Slayer").save(consumer);
        builder.id("journeyman").defaultDisplay("Journeyman").setPrefix().save(consumer);
        builder.id("squire").defaultDisplay("Squire").setPrefix().save(consumer);

        builder.rarity(Rarity.RARE);
        builder.id("reaper").defaultDisplay("the Reaper").flavorText("Don't fear it").save(consumer);
        builder.id("master").defaultDisplay("Master").setPrefix().save(consumer);
        builder.id("knight").defaultDisplay("Knight").setPrefix().flavorText("...in shining armor").save(consumer);
    }

    private void registerAdvancementTitles(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID);

        genWithName(builder, "captain").setPrefix().save(consumer);
        genWithName(builder, "chicken_chaser", false, true).save(consumer);
        genWithName(builder, "melon_lord", false, true).save(consumer);
        genWithName(builder, "spelunker").setPrefix().save(consumer);
        genWithName(builder, "frigid", false, true).save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, "pincushion").setPrefix().save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, "opulent", false, true).save(consumer);
    }

    private void registerMinecraftTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID)
                .rarity(Rarity.COMMON);

        String subfolder = "_minecraft/story/";
        genWithName(builder, subfolder + "iron_tools").setPrefix().save(consumer);
        genWithName(builder, subfolder + "obtain_armor").setPrefix().save(consumer);
        genWithName(builder, subfolder + "deflect_arrow", true, true).setPrefix().save(consumer);
        genWithName(builder, subfolder + "enchant_item").save(consumer);
        genWithName(builder, subfolder + "enter_the_end", false, true).save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "cure_zombie_villager").setPrefix().save(consumer);

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/husbandry/";
        genWithName(builder, subfolder + "breed_an_animal").setPrefix().save(consumer);
        genWithName(builder, subfolder + "fishy_business").setPrefix().save(consumer);
        genWithName(builder, subfolder + "make_a_sign_glow").setPrefix().save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "bred_all_animals").setPrefix().save(consumer);
        genWithName(builder, subfolder + "complete_catalogue").save(consumer);
        genWithName(builder, subfolder + "balanced_diet").save(consumer);
        genWithName(builder, subfolder + "obtain_netherite_hoe").save(consumer);

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/adventure/";
        genWithName(builder, subfolder + "kill_a_mob", true, false).setPrefix().save(consumer);
        genWithName(builder, subfolder + "spyglass_at_parrot").save(consumer);
        genWithName(builder, subfolder + "trade").setPrefix().save(consumer);
        genWithName(builder, subfolder + "shoot_arrow").setPrefix().save(consumer);
        genWithName(builder, subfolder + "lightning_rod_with_villager_no_fire").save(consumer);
        genWithName(builder, subfolder + "fall_from_world_height").save(consumer);
        genWithName(builder, subfolder + "walk_on_powder_snow_with_leather_boots").save(consumer);
        genWithName(builder, subfolder + "spyglass_at_dragon").save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "totem_of_undying").save(consumer);
        genWithName(builder, subfolder + "summon_iron_golem").save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "hero_of_the_village").setPrefix().save(consumer);
        genWithName(builder, subfolder + "kill_all_mobs").setPrefix().save(consumer);
        genWithName(builder, subfolder + "two_birds_one_arrow").save(consumer);
        genWithName(builder, subfolder + "arbalistic").save(consumer);
        genWithName(builder, subfolder + "adventuring_time").save(consumer);
        genWithName(builder, subfolder + "sniper_duel").setPrefix().save(consumer);
        genWithName(builder, subfolder + "bullseye").save(consumer);

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/nether/";
        genWithName(builder, subfolder + "obtain_crying_obsidian").save(consumer);
        genWithName(builder, subfolder + "ride_strider").save(consumer);
        genWithName(builder, subfolder + "loot_bastion").setPrefix().save(consumer);
        genWithName(builder, subfolder + "charge_respawn_anchor").save(consumer);
        genWithName(builder, subfolder + "brew_potion").setPrefix().save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "create_full_beacon").save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "return_to_sender").save(consumer);
        genWithName(builder, subfolder + "fast_travel").save(consumer);
        genWithName(builder, subfolder + "uneasy_alliance").save(consumer);
        genWithName(builder, subfolder + "netherite_armor").save(consumer);
        genWithName(builder, subfolder + "explore_nether").setPrefix().save(consumer);
        genWithName(builder, subfolder + "all_potions").save(consumer);
        genWithName(builder, subfolder + "all_effects").save(consumer);

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/end/";
        genWithName(builder, subfolder + "kill_dragon").setPrefix().save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "dragon_egg").setPrefix().save(consumer);
        genWithName(builder, subfolder + "respawn_dragon").setPrefix().save(consumer);
        genWithName(builder, subfolder + "dragon_breath", false, true).save(consumer);
        genWithName(builder, subfolder + "elytra", false, true).save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "levitate").save(consumer);
    }

    private void registerBotaniaTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID)
                .rarity(Rarity.COMMON);

        String subfolder = "_botania/main/";
        genWithName(builder, subfolder + "flower_pickup").setPrefix().save(consumer);
        genWithName(builder, subfolder + "cacophonium_craft", false, true).save(consumer);
        genWithName(builder, subfolder + "rune_pickup").save(consumer);
        genWithName(builder, subfolder + "tiny_potato_pet").setPrefix().save(consumer);
        genWithName(builder, subfolder + "pollidisiac_pickup").setPrefix().save(consumer);
        genWithName(builder, subfolder + "manaweave_armor_craft").setPrefix().save(consumer);
        genWithName(builder, subfolder + "spark_craft").save(consumer);
        genWithName(builder, subfolder + "alf_portal_open").save(consumer);
        genWithName(builder, subfolder + "heisei_dream_pickup").save(consumer);
        genWithName(builder, subfolder + "dandelifeon_pickup", false, true).setPrefix().save(consumer);
        genWithName(builder, subfolder + "luminizer_ride").setPrefix().save(consumer);

        builder.rarity(Rarity.RARE);
        subfolder = "_botania/challenge/";
        genWithName(builder, subfolder + "gaia_guardian_hardmode").setPrefix().save(consumer);
        genWithName(builder, subfolder + "gaia_guardian_no_armor").save(consumer);
        genWithName(builder, subfolder + "alf_portal_bread", false, true).save(consumer);
        genWithName(builder, subfolder + "super_corporea_request").setPrefix().save(consumer);
        genWithName(builder, subfolder + "rank_ss_pick").setPrefix().save(consumer);
        genWithName(builder, subfolder + "pinkinator").save(consumer);
        genWithName(builder, subfolder + "king_key").save(consumer);
    }

    private void registerIETemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID)
                .rarity(Rarity.COMMON);

        String subfolder = "_immersiveengineering/main/";
        genWithName(builder, subfolder + "connect_wire").save(consumer);
        genWithName(builder, subfolder + "craft_workbench").setPrefix().save(consumer);
        genWithName(builder, subfolder + "place_windmill").save(consumer);
        genWithName(builder, subfolder + "mb_fermenter").save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "secret_luckofthedraw").setPrefix().save(consumer);
        genWithName(builder, subfolder + "secret_birthdayparty").setPrefix().save(consumer);
        genWithName(builder, subfolder + "secret_drillbreak", false, true).save(consumer);
        genWithName(builder, subfolder + "mb_excavator").setPrefix().save(consumer);
    }

    private void registerTFTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID)
                .rarity(Rarity.COMMON);

        String subfolder = "_twilightforest/";
        genWithName(builder, subfolder + "quest_ram").save(consumer);
        genWithName(builder, subfolder + "troll").setPrefix().save(consumer);
        genWithName(builder, subfolder + "giants").setPrefix().save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "kill_naga").setPrefix().save(consumer);
        genWithName(builder, subfolder + "kill_lich").setPrefix().save(consumer);
        genWithName(builder, subfolder + "progress_glacier").setPrefix().save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "full_mettle_alchemist").save(consumer);
        genWithName(builder, subfolder + "mazebreaker").setPrefix().save(consumer);
        genWithName(builder, subfolder + "fiery_set").save(consumer);
    }

    private Title.Builder genWithName(Title.Builder builder, String n) {
        return genWithName(builder, n, false, false);
    }

    private Title.Builder genWithName(Title.Builder builder, String name, boolean variant, boolean flavor) {
        String defaultDisplay = String.format("title.%s.%s", builder.getModId(), convertToLang(name));
        builder.id(new ResourceLocation(builder.getModId(), name))
                .defaultDisplay(defaultDisplay);

        if (variant) {
            builder.variantDisplay(defaultDisplay + ".variant");
        }

        if (flavor) {
            builder.flavorText(defaultDisplay + ".flavor");
        }

        return builder;
    }

    private String convertToLang(String name) {
        String conversion = name;
        if (conversion.startsWith("_")) {
            conversion = conversion.substring(1);
        }
        return conversion.replaceAll("[/:]", ".");
    }
}