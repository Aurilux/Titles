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
        builder.id("apprentice").defaultDisplay("the Apprentice").save(consumer);
        builder.id("page").defaultDisplay("the Page").save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        builder.id("slayer").defaultDisplay("the Slayer").save(consumer);
        builder.id("journeyman").defaultDisplay("the Journeyman").save(consumer);
        builder.id("squire").defaultDisplay("the Squire").save(consumer);

        builder.rarity(Rarity.RARE);
        builder.id("reaper").defaultDisplay("the Reaper").flavorText("Don't fear it").save(consumer);
        builder.id("master").defaultDisplay("the Master").save(consumer);
        builder.id("knight").defaultDisplay("the Knight").flavorText("...in shining armor").save(consumer);
    }

    private void registerAdvancementTitles(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID);

        genWithName(builder, "captain").save(consumer);
        genWithName(builder, "chicken_chaser", false, true).save(consumer);
        genWithName(builder, "melon_lord", false, true).save(consumer);
        genWithName(builder, "spelunker").save(consumer);
        genWithName(builder, "frigid", false, true).save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, "pincushion").save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, "opulent", false, true).save(consumer);
    }

    private void registerMinecraftTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID)
                .rarity(Rarity.COMMON);

        String subfolder = "_minecraft/story/";
        genWithName(builder, subfolder + "iron_tools").save(consumer);
        genWithName(builder, subfolder + "obtain_armor").save(consumer);
        genWithName(builder, subfolder + "deflect_arrow", true, true).save(consumer);
        genWithName(builder, subfolder + "enchant_item").save(consumer);
        genWithName(builder, subfolder + "enter_the_end", false, true).save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "cure_zombie_villager").save(consumer);

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/husbandry/";
        genWithName(builder, subfolder + "breed_an_animal").save(consumer);
        genWithName(builder, subfolder + "fishy_business").save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "bred_all_animals").save(consumer);
        genWithName(builder, subfolder + "complete_catalogue").save(consumer);
        genWithName(builder, subfolder + "balanced_diet").save(consumer);
        genWithName(builder, subfolder + "obtain_netherite_hoe").save(consumer);

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/adventure/";
        genWithName(builder, subfolder + "kill_a_mob", true, false).save(consumer);
        genWithName(builder, subfolder + "trade").save(consumer);
        genWithName(builder, subfolder + "shoot_arrow").save(consumer);
        genWithName(builder, subfolder + "fall_from_world_height").save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "totem_of_undying").save(consumer);
        genWithName(builder, subfolder + "summon_iron_golem").save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "hero_of_the_village").save(consumer);
        genWithName(builder, subfolder + "kill_all_mobs").save(consumer);
        genWithName(builder, subfolder + "two_birds_one_arrow").save(consumer);
        genWithName(builder, subfolder + "arbalistic").save(consumer);
        genWithName(builder, subfolder + "adventuring_time").save(consumer);
        genWithName(builder, subfolder + "sniper_duel").save(consumer);
        genWithName(builder, subfolder + "bullseye").save(consumer);

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/nether/";
        genWithName(builder, subfolder + "obtain_crying_obsidian").save(consumer);
        genWithName(builder, subfolder + "ride_strider").save(consumer);
        genWithName(builder, subfolder + "loot_bastion").save(consumer);
        genWithName(builder, subfolder + "charge_respawn_anchor").save(consumer);
        genWithName(builder, subfolder + "brew_potion").save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "create_full_beacon").save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "return_to_sender").save(consumer);
        genWithName(builder, subfolder + "fast_travel").save(consumer);
        genWithName(builder, subfolder + "uneasy_alliance").save(consumer);
        genWithName(builder, subfolder + "netherite_armor").save(consumer);
        genWithName(builder, subfolder + "explore_nether").save(consumer);
        genWithName(builder, subfolder + "all_potions").save(consumer);
        genWithName(builder, subfolder + "all_effects").save(consumer);

        builder.rarity(Rarity.COMMON);
        subfolder = "_minecraft/end/";
        genWithName(builder, subfolder + "kill_dragon").save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "dragon_egg").save(consumer);
        genWithName(builder, subfolder + "respawn_dragon").save(consumer);
        genWithName(builder, subfolder + "dragon_breath", false, true).save(consumer);
        genWithName(builder, subfolder + "elytra", false, true).save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "levitate").save(consumer);
    }

    private void registerBotaniaTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID)
                .rarity(Rarity.COMMON);

        String subfolder = "_botania/main/";
        genWithName(builder, subfolder + "flower_pickup").save(consumer);
        genWithName(builder, subfolder + "cacophonium_craft", false, true).save(consumer);
        genWithName(builder, subfolder + "rune_pickup").save(consumer);
        genWithName(builder, subfolder + "tiny_potato_pet").save(consumer);
        genWithName(builder, subfolder + "pollidisiac_pickup").save(consumer);
        genWithName(builder, subfolder + "manaweave_armor_craft").save(consumer);
        genWithName(builder, subfolder + "spark_craft").save(consumer);
        genWithName(builder, subfolder + "alf_portal_open").save(consumer);
        genWithName(builder, subfolder + "heisei_dream_pickup").save(consumer);
        genWithName(builder, subfolder + "dandelifeon_pickup", false, true).save(consumer);
        genWithName(builder, subfolder + "luminizer_ride").save(consumer);

        builder.rarity(Rarity.RARE);
        subfolder = "_botania/challenge/";
        genWithName(builder, subfolder + "gaia_guardian_hardmode").save(consumer);
        genWithName(builder, subfolder + "gaia_guardian_no_armor").save(consumer);
        genWithName(builder, subfolder + "alf_portal_bread", false, true).save(consumer);
        genWithName(builder, subfolder + "super_corporea_request").save(consumer);
        genWithName(builder, subfolder + "pinkinator").save(consumer);
        genWithName(builder, subfolder + "king_key").save(consumer);
    }

    private void registerIETemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID)
                .rarity(Rarity.COMMON);

        String subfolder = "_immersiveengineering/main/";
        genWithName(builder, subfolder + "connect_wire").save(consumer);
        genWithName(builder, subfolder + "place_windmill").save(consumer);
        genWithName(builder, subfolder + "place_floodlight").save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "secret_luckofthedraw").save(consumer);
        genWithName(builder, subfolder + "secret_birthdayparty").save(consumer);
        genWithName(builder, subfolder + "secret_drillbreak", false, true).save(consumer);
        genWithName(builder, subfolder + "mb_excavator").save(consumer);
    }

    private void registerTFTemplate(Consumer<Title> consumer) {
        Title.Builder builder = Title.Builder.create(TitlesMod.MOD_ID)
                .rarity(Rarity.COMMON);

        String subfolder = "_twilightforest/";
        genWithName(builder, subfolder + "quest_ram").save(consumer);
        genWithName(builder, subfolder + "progress_troll").save(consumer);

        builder.rarity(Rarity.UNCOMMON);
        genWithName(builder, subfolder + "progress_naga").save(consumer);
        genWithName(builder, subfolder + "progress_lich").save(consumer);
        genWithName(builder, subfolder + "progress_yeti").save(consumer);
        genWithName(builder, subfolder + "progress_glacier").save(consumer);
        genWithName(builder, subfolder + "progress_knight").save(consumer);
        genWithName(builder, subfolder + "progress_ur_ghast").save(consumer);

        builder.rarity(Rarity.RARE);
        genWithName(builder, subfolder + "mazebreaker").save(consumer);
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