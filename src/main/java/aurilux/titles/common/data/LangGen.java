package aurilux.titles.common.data;

import aurilux.titles.client.Keybinds;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.util.StringUtil;
import net.minecraftforge.common.data.LanguageProvider;

public class LangGen extends LanguageProvider {
    public LangGen(PackOutput packOutput) {
        super(packOutput, TitlesMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        var keyPath = "";
        // Chat
        add("chat.advancement.append", " and earned the title [%s]");
        add("chat.scroll.success", "The scroll reveals a the new title [%s]");
        add("chat.scroll.fail", "The scroll reveals no new titles");

        // Commands
        add("commands.titles.usage", "/titles <add|remove> <player> <titleKey>");
        add("commands.titles.addremove.fail", "This command failed for an unknown reason");
        add("commands.titles.add", "Added title [%s] to player %s.");
        add("commands.titles.remove", "Removed title [%s] from player %s.");
        add("commands.display.success", "Changed your display title to [%s]");
        add("commands.nickname.success", "Nickname changed to [%s]");
        add("commands.nickname.empty", "Nickname reset!");
        add("commands.nickname.error.pattern", "Nickname must be 3-16 characters long, and include only the characters [a-zA-Z0-9_]");

        // Keybindings
        add(Keybinds.openTitleSelection.getCategory(), TitlesMod.MOD_ID);
        add(Keybinds.openTitleSelection.getName(), "Open Title Selection Screen");

        // Gui
        keyPath = String.format("gui.%s.", TitlesMod.MOD_ID);
        add(keyPath + "cancel", "Cancel");
        add(keyPath + "confirm", "Confirm");
        add(keyPath + "random", "Random");
        add(keyPath + "none", "None");
        add(keyPath + "purchase", "Purchase");
        add(keyPath + "close", "Close");
        add(keyPath + "titleselection.empty", "Unlock advancements to earn titles");
        add(keyPath + "titleselection.empty.filter", "Your filter has returned no results");
        add(keyPath + "search_tooltip", "# to search rarity (#rare or #r)\n@ to search mod id (@titles)");

        // Items
        add("itemGroup.titles", TitlesMod.MOD_ID);
        add(ModItems.TITLE_FRAGMENT.get(), "Title Fragment");
        add(ModItems.TITLE_SCROLL_COMMON.get(), "Common Title Scroll");
        add(ModItems.TITLE_SCROLL_UNCOMMON.get(), "Uncommon Title Scroll");
        add(ModItems.TITLE_SCROLL_RARE.get(), "Rare Title Scroll");

        // Advancements
        keyPath = String.format("advancement.%s.", TitlesMod.MOD_ID);
        add(keyPath + "root.desc", "Flaunt your Feats");
        addAdvancement(keyPath + "spelunker", "How Low Can You Go?", "Go below y-level 20");
        addAdvancement(keyPath + "chicken_chaser", "Do you Chase Chickens?", "Kill a chicken");
        addAdvancement(keyPath + "captain", "You are the Captain Now", "Get in a boat");
        addAdvancement(keyPath + "melon_lord", "MWAHAHAHA", "Wear a carved pumpkin as a helmet");
        addAdvancement(keyPath + "pincushion", "Ouch... ouch...", "Have 7 arrows sticking out of you");
        addAdvancement(keyPath + "opulent", "The One Percent", "Build a beacon base entirely out of one type of expensive block");
        addAdvancement(keyPath + "frigid", "Ice Age", "Take freeze damage from being in Powder Snow");

        // Titles
        keyPath = String.format("title.%s.", TitlesMod.MOD_ID);
        add(keyPath + "spelunker", "Spelunker");
        addTitle(keyPath + "chicken_chaser", "the Chicken Chaser", "", "Good Cluck!");
        add(keyPath + "captain", "Captain");
        addTitle(keyPath + "opulent", "the Opulent", "", "How much can it cost? Like five dollars?");
        addTitle(keyPath + "melon_lord", "the Melon Lord", "", "I am not Toph! I am MELON LORD!");
        add(keyPath + "pincushion", "Pincushion");
        addTitle(keyPath + "frigid", "the Frigid", "", "Brrrrr!");

        minecraftTemplateTitles(keyPath);
        botaniaTemplateTitles(keyPath);
        IETemplateTitles(keyPath);
        TFTemplateTitles(keyPath);
    }

    private void TFTemplateTitles(String keyPath) {
        var twilightKeyPath = keyPath + "twilightforest.";
        add(twilightKeyPath + "quest_ram", "the Multicolored");
        add(twilightKeyPath + "troll", "Troll Slayer");
        add(twilightKeyPath + "giants", "Giant Slayer");

        add(twilightKeyPath + "kill_naga", "Naga Slayer");
        add(twilightKeyPath + "kill_lich", "Lich Slayer");
        addTitle(twilightKeyPath + "progress_glacier", "Queen Slayer", "", "Jaime Lannister would be proud.");

        add(twilightKeyPath + "full_mettle_alchemist", "the Resilient");
        add(twilightKeyPath + "mazebreaker", "Vault Breaker");
        add(twilightKeyPath + "fiery_set", "the Flame-forged");
    }

    private void IETemplateTitles(String keyPath) {
        var immersiveKeyPath = keyPath + "immersiveengineering.main.";
        add(immersiveKeyPath + "connect_wire", "the Live Wire");
        add(immersiveKeyPath + "craft_workbench", "Tinkerer");
        addTitle(immersiveKeyPath + "place_windmill", "the Tilted", "", "Perhaps to be too practical is madness.");
        add(immersiveKeyPath + "mb_fermenter", "the Bootlegger");

        add(immersiveKeyPath + "secret_luckofthedraw", "Gambler");
        add(immersiveKeyPath + "secret_birthdayparty", "Party Popper");
        addTitle(immersiveKeyPath + "secret_drillbreak", "the Heaven Piercer", "", "Let me see you grit those teeth!");
        add(immersiveKeyPath + "mb_excavator", "Quarrier");
    }

    private void botaniaTemplateTitles(String keyPath) {
        var botaniaKeyPath = keyPath + "botania.main.";
        add(botaniaKeyPath + "flower_pickup", "Flower Picker");
        addTitle(botaniaKeyPath + "cacophonium_craft", "the Noisy", "", "Let me play you the song of my people.");
        add(botaniaKeyPath + "rune_pickup", "the Runechild");
        add(botaniaKeyPath + "tiny_potato_pet", "Spud Bud");
        add(botaniaKeyPath + "pollidisiac_pickup", "Matchmaker");
        add(botaniaKeyPath + "manaweave_armor_craft", "Manaweaver");
        add(botaniaKeyPath + "spark_craft", "the Sparkling");
        add(botaniaKeyPath + "elf_portal_open", "the Midgardian Trader");
        add(botaniaKeyPath + "heisei_dream_pickup", "the Maddening");
        addTitle(botaniaKeyPath + "dandelifeon_pickup", "Wheel Spinner", "", "Collect beaucoup bucks!");
        add(botaniaKeyPath + "luminizer_ride", "Light Treader");

        botaniaKeyPath = keyPath + "botania.challenge.";
        add(botaniaKeyPath + "gaia_guardian_hardmode", "Gaia Slayer");
        add(botaniaKeyPath + "gaia_guardian_no_armor", "the Exhibitionist");
        addTitle(botaniaKeyPath + "alf_portal_bread", "the Midgardian Baker", "", "WHAT IS YOUR PROFESSION?!");
        add(botaniaKeyPath + "super_corporea_request", "Stockpiler");
        add(botaniaKeyPath + "rank_ss_pick", "World Breaker");
        add(botaniaKeyPath + "pinkinator", "the Rosy");
        add(botaniaKeyPath + "king_key", "the Babylonian");
    }

    private void minecraftTemplateTitles(String keyPath) {
        var minecraftKeyPath = keyPath + "minecraft.story.";
        add(minecraftKeyPath + "iron_tools", "Miner");
        addTitle(minecraftKeyPath + "obtain_armor", "Armorer", "", "This is the Way.");
        addTitle(minecraftKeyPath + "deflect_arrow", "Shield Bearer", "Shield Maiden", "As you wish, my Thane.");
        addTitle(minecraftKeyPath + "enchant_item", "the Enchanter", "the Enchantress", "");
        add(minecraftKeyPath + "cure_zombie_villager", "Purifier");
        addTitle(minecraftKeyPath + "enter_the_end", "the Puddle Jumper", "", "Flight, this is... Puddle Jumper.");

        minecraftKeyPath = keyPath + "minecraft.husbandry.";
        add(minecraftKeyPath + "breed_an_animal", "Rancher");
        add(minecraftKeyPath + "fishy_business", "Fisherman");
        add(minecraftKeyPath + "make_a_sign_glow", "the Glowing");
        add(minecraftKeyPath + "bred_all_animals", "Shepherd");
        add(minecraftKeyPath + "complete_catalogue", "the Ailurophile");
        add(minecraftKeyPath + "balanced_diet", "the Epicure");
        add(minecraftKeyPath + "obtain_netherite_hoe", "the Green Thumb");

        minecraftKeyPath = keyPath + "minecraft.adventure.";
        addTitle(minecraftKeyPath + "kill_a_mob", "Huntsman", "Huntress", "");
        add(minecraftKeyPath + "spyglass_at_parrot", "the Onlooker");
        add(minecraftKeyPath + "trade", "Haggler");
        add(minecraftKeyPath + "shoot_arrow", "Bowman");
        add(minecraftKeyPath + "fall_from_world_height", "the Base Jumper");
        add(minecraftKeyPath + "lightning_rod_with_villager_no_fire", "the Shocking");
        add(minecraftKeyPath + "kill_all_mobs", "Predator");
        add(minecraftKeyPath + "totem_of_undying", "the Deathless");
        add(minecraftKeyPath + "summon_iron_golem", "the Samaritan");
        add(minecraftKeyPath + "two_birds_one_arrow", "the Opportunist");
        add(minecraftKeyPath + "arbalistic", "the Patient");
        add(minecraftKeyPath + "adventuring_time", "the Globetrotter");
        add(minecraftKeyPath + "hero_of_the_village", "Defender");
        add(minecraftKeyPath + "spyglass_at_ghast", "the Watcher");
        add(minecraftKeyPath + "walk_on_powder_snow_with_leather_boots", "the Light-footed");
        add(minecraftKeyPath + "spyglass_at_dragon", "the Beholder");
        add(minecraftKeyPath + "sniper_duel", "Marksman");
        add(minecraftKeyPath + "bullseye", "the Unerring");

        minecraftKeyPath = keyPath + "minecraft.nether.";
        add(minecraftKeyPath + "return_to_sender", "the Mailman");
        add(minecraftKeyPath + "fast_travel", "the Expeditious");
        add(minecraftKeyPath + "obtain_crying_obsidian", "the Tear Jerker");
        add(minecraftKeyPath + "ride_strider", "the Strider Rider");
        add(minecraftKeyPath + "uneasy_alliance", "the Merciful");
        add(minecraftKeyPath + "loot_bastion", "Raider");
        add(minecraftKeyPath + "netherite_armor", "the Emblazoned");
        add(minecraftKeyPath + "charge_respawn_anchor", "the Prepared");
        add(minecraftKeyPath + "explore_nether", "Trailblazer");
        add(minecraftKeyPath + "brew_potion", "Brewer");
        add(minecraftKeyPath + "all_potions", "the Alchemist");
        add(minecraftKeyPath + "all_effects", "the Overloaded");
        add(minecraftKeyPath + "create_full_beacon", "the Architect");

        minecraftKeyPath = keyPath + "minecraft.end.";
        add(minecraftKeyPath + "kill_dragon", "Liberator");
        add(minecraftKeyPath + "dragon_egg", "Caretaker");
        add(minecraftKeyPath + "respawn_dragon", "Reviver");
        addTitle(minecraftKeyPath + "dragon_breath", "the Breathtaking", "", "No, you're breathtaking!");
        addTitle(minecraftKeyPath + "elytra", "the Lightyear", "", "...it's falling with STYLE!");
        add(minecraftKeyPath + "levitate", "the Elevated");
    }

    private void addAdvancement(String path, String name, String description) {
        add(path, name);
        add(path + ".desc", description);
    }

    private void addTitle(String path, String name, String variant, String flavor) {
        add(path, name);
        if (!StringUtil.isNullOrEmpty(variant)) {
            add(path + ".variant", variant);
        }
        if (!StringUtil.isNullOrEmpty(flavor)) {
            add(path + ".flavor", flavor);
        }
    }
}