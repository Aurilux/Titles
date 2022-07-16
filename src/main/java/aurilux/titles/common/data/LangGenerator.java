package aurilux.titles.common.data;

import aurilux.titles.client.Keybinds;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.StringUtils;
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
        add("commands.titles.add", "Added title [%s] to player %s.");
        add("commands.titles.remove", "Removed title [%s] from player %s.");
        add("commands.display.error", "You have not earned the title [%s]");
        add("commands.display.success", "Changed your display title to [%s]");

        // Keybindings
        add(Keybinds.openTitleSelection.getKeyCategory(), TitlesMod.MOD_ID);
        add(Keybinds.openTitleSelection.getKeyDescription(), "Open Title Selection Screen");

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

        // Titles
        keyPath = String.format("title.%s.", TitlesMod.MOD_ID);
        add(keyPath + "spelunker", "the Spelunker");
        addTitle(keyPath + "chicken_chaser", "the Chicken Chaser", "", "Good Cluck!");
        add(keyPath + "captain", "the Captain");
        addTitle(keyPath + "opulent", "the Opulent", "", "How much can it cost? Like five dollars?");
        addTitle(keyPath + "melon_lord", "the Melon Lord", "", "I am not Toph! I am MELON LORD!");
        add(keyPath + "pincushion", "the Pincushion");

        keyPath = "title.minecraft.story.";
        add(keyPath + "iron_tools", "the Miner");
        add(keyPath + "obtain_armor", "the Armorer");
        addTitle(keyPath + "deflect_arrow", "the Shield Bearer", "the Shield Maiden", "As you wish, my Thane");
        addTitle(keyPath + "enchant_item", "the Enchanter", "the Enchantress", "");
        add(keyPath + "cure_zombie_villager", "the Purifier");
        addTitle(keyPath + "enter_the_end", "the Puddle Jumper", "", "Flight, this is... Puddle Jumper.");

        keyPath = "title.minecraft.husbandry.";
        add(keyPath + "breed_an_animal", "the Rancher");
        add(keyPath + "fishy_business", "the Fisherman");
        add(keyPath + "bred_all_animals", "the Shepherd");
        add(keyPath + "complete_catalogue", "the Ailurophile");
        add(keyPath + "balanced_diet", "the Epicure");
        add(keyPath + "obtain_netherite_hoe", "the Green Thumb");

        keyPath = "title.minecraft.adventure.";
        addTitle(keyPath + "kill_a_mob", "the Huntsman", "the Huntress", "");
        add(keyPath + "trade", "the Haggler");
        add(keyPath + "shoot_arrow", "the Bowman");
        add(keyPath + "kill_all_mobs", "the Predator");
        add(keyPath + "totem_of_undying", "the Deathless");
        add(keyPath + "summon_iron_golem", "the Samaritan");
        add(keyPath + "two_birds_one_arrow", "the Opportunist");
        add(keyPath + "arbalistic", "the Patient");
        add(keyPath + "adventuring_time", "the Globetrotter");
        add(keyPath + "hero_of_the_village", "the Defender");
        add(keyPath + "sniper_duel", "the Marksman");
        add(keyPath + "bullseye", "the Unerring");

        keyPath = "title.minecraft.nether.";
        add(keyPath + "return_to_sender", "the Mailman");
        add(keyPath + "fast_travel", "the Expeditious");
        add(keyPath + "obtain_crying_obsidian", "the Tear Jerker");
        add(keyPath + "ride_strider", "the Strider Rider");
        add(keyPath + "uneasy_alliance", "the Merciful");
        add(keyPath + "loot_bastion", "the Raider");
        add(keyPath + "netherite_armor", "the Emblazoned");
        add(keyPath + "charge_respawn_anchor", "the Prepared");
        add(keyPath + "explore_nether", "the Trailblazer");
        add(keyPath + "brew_potion", "the Brewer");
        add(keyPath + "all_potions", "the Alchemist");
        add(keyPath + "all_effects", "the Overloaded");
        add(keyPath + "create_full_beacon", "the Architect");

        keyPath = "title.minecraft.end.";
        add(keyPath + "kill_dragon", "the Liberator");
        add(keyPath + "dragon_egg", "the Caretaker");
        add(keyPath + "respawn_dragon", "the Reviver");
        addTitle(keyPath + "dragon_breath", "the Breathtaking", "", "No, you're breathtaking!");
        addTitle(keyPath + "elytra", "the Lightyear", "", "...it's falling with STYLE!");
        add(keyPath + "levitate", "the Elevated");
    }

    private void addAdvancement(String path, String name, String description) {
        add(path, name);
        add(path + ".desc", description);
    }

    private void addTitle(String path, String name, String variant, String flavor) {
        add(path, name);
        if (!StringUtils.isNullOrEmpty(variant)) {
            add(path + ".variant", variant);
        }
        if (!StringUtils.isNullOrEmpty(flavor)) {
            add(path + ".flavor", flavor);
        }
    }
}