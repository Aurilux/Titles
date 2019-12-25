package aurilux.titles.api;

import aurilux.titles.api.capability.TitlesImpl;
import aurilux.titles.api.internal.DummyMethodHandler;
import aurilux.titles.api.internal.IInternalMethodHandler;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TitlesAPI {
    private static final Map<String, TitleInfo> titlesMap = new HashMap<>();

    public static IInternalMethodHandler internalHandler = new DummyMethodHandler();

    static {
        //Titles
        registerTitle("titles:spelunker");
        registerTitle("titles:chicken_chaser");
        registerTitle("titles:captain");
        registerTitle("titles:opulent", TitleInfo.TitleRarity.RARE);
        registerTitle("titles:melon_lord");

        //story
        registerTitle("minecraft:story/root");
        registerTitle("minecraft:story/mine_stone");
        registerTitle("minecraft:story/iron_tools");
        registerTitle("minecraft:story/obtain_armor");
        registerTitle("minecraft:story/deflect_arrow");
        registerTitle("minecraft:story/enter_the_nether");
        registerTitle("minecraft:story/enchant_item");
        registerTitle("minecraft:story/cure_zombie_villager", TitleInfo.TitleRarity.UNCOMMON);
        registerTitle("minecraft:story/enter_the_end");

        //husbandry
        registerTitle("minecraft:husbandry/root");
        registerTitle("minecraft:husbandry/breed_an_animal");
        registerTitle("minecraft:husbandry/tame_an_animal");
        registerTitle("minecraft:husbandry/bred_all_animals", TitleInfo.TitleRarity.RARE);
        registerTitle("minecraft:husbandry/balanced_diet", TitleInfo.TitleRarity.RARE);
        registerTitle("minecraft:husbandry/break_diamond_hoe", TitleInfo.TitleRarity.RARE);

        //adventure
        registerTitle("minecraft:adventure/root");
        registerTitle("minecraft:adventure/kill_a_mob");
        registerTitle("minecraft:adventure/trade");
        registerTitle("minecraft:adventure/shoot_arrow");
        registerTitle("minecraft:adventure/kill_all_mobs", TitleInfo.TitleRarity.RARE);
        registerTitle("minecraft:adventure/totem_of_undying", TitleInfo.TitleRarity.UNCOMMON);
        registerTitle("minecraft:adventure/summon_iron_golem");
        registerTitle("minecraft:adventure/adventuring_time", TitleInfo.TitleRarity.RARE);
        registerTitle("minecraft:adventure/sniper_duel", TitleInfo.TitleRarity.RARE);

        //nether
        registerTitle("minecraft:nether/fast_travel");
        registerTitle("minecraft:nether/return_to_sender", TitleInfo.TitleRarity.RARE);
        registerTitle("minecraft:nether/uneasy_alliance", TitleInfo.TitleRarity.RARE);
        registerTitle("minecraft:nether/brew_potion");
        registerTitle("minecraft:nether/all_potions", TitleInfo.TitleRarity.RARE);
        registerTitle("minecraft:nether/all_effects", TitleInfo.TitleRarity.RARE);
        registerTitle("minecraft:nether/create_full_beacon", TitleInfo.TitleRarity.UNCOMMON);

        //end
        registerTitle("minecraft:end/kill dragon");
        registerTitle("minecraft:end/dragon_egg", TitleInfo.TitleRarity.UNCOMMON);
        registerTitle("minecraft:end/enter_end_gateway");
        registerTitle("minecraft:end/respawn_dragon", TitleInfo.TitleRarity.UNCOMMON);
        registerTitle("minecraft:end/dragon_breath", TitleInfo.TitleRarity.UNCOMMON);
        registerTitle("minecraft:end/elytra", TitleInfo.TitleRarity.UNCOMMON);
        registerTitle("minecraft:end/levitate", TitleInfo.TitleRarity.RARE);
    }

    public static void registerTitle(String key) {
        registerTitle(key, TitleInfo.TitleRarity.COMMON);
    }

    public static void registerTitle(String key, TitleInfo.TitleRarity titleRarity) {
        if (titleRarity == TitleInfo.TitleRarity.UNIQUE) {
            return;
        }

        String modid = key.substring(0, key.indexOf(":"));
        //make the lang key, replacing all whitespace characters with "_", and non-word characters with "."
        String titleKey = key;
        titleKey = "title." + titleKey.replaceAll("[\\s]", "_").replaceAll("[\\W]", ".");

        TitleInfo titleInfo = new TitleInfo(modid, titleKey, titleRarity);
        titlesMap.put(key, titleInfo);
    }

    public static void addTitle(EntityPlayer player, String key) {
        addTitle(player, key, false);
    }

    public static void addTitle(EntityPlayer player, String key, boolean announce) {
        TitleInfo titleInfo = titlesMap.get(key);
        if (titleInfo == null || hasTitle(player, titleInfo)) {
            return;
        }

        getTitlesCap(player).add(titleInfo);
        internalHandler.syncUnlockedTitle(key, player);

        if (announce) {
            internalHandler.sendChatMessageToAllPlayers("chat.title.add", player.getDisplayName(), titleInfo);
        }
    }

    public static void removeTitle(EntityPlayer player, String key) {
        TitleInfo titleInfo = titlesMap.get(key);
        if (titleInfo != null) {
            getTitlesCap(player).remove(titleInfo);
        }
    }

    /// HELPERS
    public static TitleInfo getSelectedTitle(EntityPlayer player) {
        return getTitlesCap(player).getSelectedTitle();
    }

    public static boolean hasTitle(EntityPlayer player, TitleInfo titleInfo) {
        return getTitlesCap(player).getObtainedTitles().contains(titleInfo);
    }

    public static TitlesImpl.DefaultImpl getTitlesCap(EntityPlayer player) {
        return TitlesImpl.getCapability(player);
    }

    public static Map<String, TitleInfo> getTitlesMap() {
        return Collections.unmodifiableMap(titlesMap);
    }
    /// END HELPERS
}