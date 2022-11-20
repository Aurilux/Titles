package aurilux.titles.common.data;

import aurilux.titles.client.Keybinds;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.StringUtil;
import net.minecraftforge.common.data.LanguageProvider;

public class LangGenerator extends LanguageProvider {
    public LangGenerator(DataGenerator gen) {
        super(gen, TitlesMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        String keyPath = "";
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
        add(keyPath + "spelunker", "the Spelunker");
        addTitle(keyPath + "chicken_chaser", "the Chicken Chaser", "", "Good Cluck!");
        add(keyPath + "captain", "the Captain");
        addTitle(keyPath + "opulent", "the Opulent", "", "How much can it cost? Like five dollars?");
        addTitle(keyPath + "melon_lord", "the Melon Lord", "", "I am not Toph! I am MELON LORD!");
        add(keyPath + "pincushion", "the Pincushion");
        addTitle(keyPath + "frigid", "the Frigid", "", "Brrrrr!");

        minecraftTemplateTitles(keyPath);
        botaniaTemplateTitles(keyPath);
        IETemplateTitles(keyPath);
        TFTemplateTitles(keyPath);
    }

    private void TFTemplateTitles(String keyPath) {
        String twilightKeyPath = keyPath + "twilightforest.";
        add(twilightKeyPath + "quest_ram", "the Multicolored");
        add(twilightKeyPath + "progress_troll", "the Troll Slayer");

        add(twilightKeyPath + "progress_naga", "the Naga Slayer");
        add(twilightKeyPath + "progress_lich", "the Lich Slayer");
        add(twilightKeyPath + "progress_yeti", "the Yeti Slayer");
        addTitle(twilightKeyPath + "progress_glacier", "the Queen Slayer", "", "Jaime would be proud.");
        add(twilightKeyPath + "progress_knight", "the Knight Slayer");
        add(twilightKeyPath + "progress_ur_ghast", "the Ur-Ghast Slayer");

        add(twilightKeyPath + "mazebreaker", "the Vault Breaker");
        add(twilightKeyPath + "fiery_set", "the Flame-forged");
    }

    private void IETemplateTitles(String keyPath) {
        String immersiveKeyPath = keyPath + "immersiveengineering.main.";
        add(immersiveKeyPath + "connect_wire", "the Live Wire");
        add(immersiveKeyPath + "place_windmill", "the Tilted");
        add(immersiveKeyPath + "place_floodlight", "the Illuminated");

        add(immersiveKeyPath + "secret_luckofthedraw", "the Gambler");
        add(immersiveKeyPath + "secret_birthdayparty", "the Party Popper");
        addTitle(immersiveKeyPath + "secret_drillbreak", "the Heaven-piercer", "", "Let me see you grit those teeth!");
        add(immersiveKeyPath + "mb_excavator", "the Quarrier");
    }

    private void botaniaTemplateTitles(String keyPath) {
        String botaniaKeyPath = keyPath + "botania.main.";
        add(botaniaKeyPath + "flower_pickup", "the Flower Picker");
        addTitle(botaniaKeyPath + "cacophonium_craft", "the Noisy", "", "Let me play you the song of my people.");
        add(botaniaKeyPath + "rune_pickup", "the Runechild");
        add(botaniaKeyPath + "tiny_potato_pet", "the Spud Bud");
        add(botaniaKeyPath + "pollidisiac_pickup", "the Matchmaker");
        add(botaniaKeyPath + "manaweave_armor_craft", "the Manaweaver");
        add(botaniaKeyPath + "spark_craft", "the Sparkling");
        add(botaniaKeyPath + "alf_portal_open", "the Midgardian Trader");
        add(botaniaKeyPath + "heisei_dream_pickup", "the Maddening");
        addTitle(botaniaKeyPath + "dandelifeon_pickup", "the Wheel Spinner", "", "Collect beaucoup bucks!");
        add(botaniaKeyPath + "luminizer_ride", "the Light Treader");

        botaniaKeyPath = keyPath + "botania.challenge.";
        add(botaniaKeyPath + "gaia_guardian_hardmode", "the Gaia Slayer");
        add(botaniaKeyPath + "gaia_guardian_no_armor", "the Exhibitionist");
        addTitle(botaniaKeyPath + "alf_portal_bread", "the Midgardian Baker", "", "WHAT IS YOUR PROFESSION?!");
        add(botaniaKeyPath + "super_corporea_request", "the Stockpiler");
        add(botaniaKeyPath + "pinkinator", "the Rosy");
        add(botaniaKeyPath + "king_key", "the Babylonian");
    }

    private void minecraftTemplateTitles(String keyPath) {
        String minecraftKeyPath = keyPath + "minecraft.story.";
        add(minecraftKeyPath + "iron_tools", "the Miner");
        add(minecraftKeyPath + "obtain_armor", "the Armorer");
        addTitle(minecraftKeyPath + "deflect_arrow", "the Shield Bearer", "the Shield Maiden", "As you wish, my Thane.");
        addTitle(minecraftKeyPath + "enchant_item", "the Enchanter", "the Enchantress", "");
        add(minecraftKeyPath + "cure_zombie_villager", "the Purifier");
        addTitle(minecraftKeyPath + "enter_the_end", "the Puddle Jumper", "", "Flight, this is... Puddle Jumper.");

        minecraftKeyPath = keyPath + "minecraft.husbandry.";
        add(minecraftKeyPath + "breed_an_animal", "the Rancher");
        add(minecraftKeyPath + "fishy_business", "the Fisherman");
        add(minecraftKeyPath + "bred_all_animals", "the Shepherd");
        add(minecraftKeyPath + "complete_catalogue", "the Ailurophile");
        add(minecraftKeyPath + "balanced_diet", "the Epicure");
        add(minecraftKeyPath + "obtain_netherite_hoe", "the Green Thumb");

        minecraftKeyPath = keyPath + "minecraft.adventure.";
        addTitle(minecraftKeyPath + "kill_a_mob", "the Huntsman", "the Huntress", "");
        add(minecraftKeyPath + "trade", "the Haggler");
        add(minecraftKeyPath + "shoot_arrow", "the Bowman");
        add(minecraftKeyPath + "fall_from_world_height", "the Base Jumper");
        add(minecraftKeyPath + "kill_all_mobs", "the Predator");
        add(minecraftKeyPath + "totem_of_undying", "the Deathless");
        add(minecraftKeyPath + "summon_iron_golem", "the Samaritan");
        add(minecraftKeyPath + "two_birds_one_arrow", "the Opportunist");
        add(minecraftKeyPath + "arbalistic", "the Patient");
        add(minecraftKeyPath + "adventuring_time", "the Globetrotter");
        add(minecraftKeyPath + "hero_of_the_village", "the Defender");
        add(minecraftKeyPath + "sniper_duel", "the Marksman");
        add(minecraftKeyPath + "bullseye", "the Unerring");

        minecraftKeyPath = keyPath + "minecraft.nether.";
        add(minecraftKeyPath + "return_to_sender", "the Mailman");
        add(minecraftKeyPath + "fast_travel", "the Expeditious");
        add(minecraftKeyPath + "obtain_crying_obsidian", "the Tear Jerker");
        add(minecraftKeyPath + "ride_strider", "the Strider Rider");
        add(minecraftKeyPath + "uneasy_alliance", "the Merciful");
        add(minecraftKeyPath + "loot_bastion", "the Raider");
        add(minecraftKeyPath + "netherite_armor", "the Emblazoned");
        add(minecraftKeyPath + "charge_respawn_anchor", "the Prepared");
        add(minecraftKeyPath + "explore_nether", "the Trailblazer");
        add(minecraftKeyPath + "brew_potion", "the Brewer");
        add(minecraftKeyPath + "all_potions", "the Alchemist");
        add(minecraftKeyPath + "all_effects", "the Overloaded");
        add(minecraftKeyPath + "create_full_beacon", "the Architect");

        minecraftKeyPath = keyPath + "minecraft.end.";
        add(minecraftKeyPath + "kill_dragon", "the Liberator");
        add(minecraftKeyPath + "dragon_egg", "the Caretaker");
        add(minecraftKeyPath + "respawn_dragon", "the Reviver");
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