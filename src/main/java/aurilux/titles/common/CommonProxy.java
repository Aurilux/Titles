package aurilux.titles.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init(FMLInitializationEvent event) {
        Titles.console("Adding default titles...");
        //story
        TitleManager.registerTitle("minecraft:story/root");
        TitleManager.registerTitle("minecraft:story/mine_stone");
        TitleManager.registerTitle("minecraft:story/obtain_armor");
        TitleManager.registerTitle("minecraft:story/iron_tools");
        TitleManager.registerTitle("minecraft:story/deflect_arrow");
        TitleManager.registerTitle("minecraft:story/enter_the_nether");
        TitleManager.registerTitle("minecraft:story/enchant_item");
        TitleManager.registerTitle("minecraft:story/cure_zombie_villager");
        TitleManager.registerTitle("minecraft:story/enter_the_end");

        //husbandry
        TitleManager.registerTitle("minecraft:husbandry/root");
        TitleManager.registerTitle("minecraft:husbandry/tame_an_animal");
        TitleManager.registerTitle("minecraft:husbandry/bred_all_animals");
        TitleManager.registerTitle("minecraft:husbandry/balanced_diet");
        TitleManager.registerTitle("minecraft:husbandry/break_diamond_hoe");

        //adventure
        TitleManager.registerTitle("minecraft:adventure/root");
        TitleManager.registerTitle("minecraft:adventure/kill_a_mob");
        TitleManager.registerTitle("minecraft:adventure/trade");
        TitleManager.registerTitle("minecraft:adventure/shoot_arrow");
        TitleManager.registerTitle("minecraft:adventure/kill_all_mobs");
        TitleManager.registerTitle("minecraft:adventure/totem_of_undying");
        TitleManager.registerTitle("minecraft:adventure/summon_iron_golem");
        TitleManager.registerTitle("minecraft:adventure/adventuring_time");
        TitleManager.registerTitle("minecraft:adventure/sniper_duel");

        //nether
        TitleManager.registerTitle("minecraft:nether/fast_travel");
        TitleManager.registerTitle("minecraft:nether/return_to_sender");
        TitleManager.registerTitle("minecraft:nether/uneasy_alliance");
        TitleManager.registerTitle("minecraft:nether/brew_potion");
        TitleManager.registerTitle("minecraft:nether/all_potions");
        TitleManager.registerTitle("minecraft:nether/all_effects");
        TitleManager.registerTitle("minecraft:nether/create_full_beacon");

        //end
        TitleManager.registerTitle("minecraft:end/kill_dragon");
        TitleManager.registerTitle("minecraft:end/dragon_egg");
        TitleManager.registerTitle("minecraft:end/enter_end_gateway");
        TitleManager.registerTitle("minecraft:end/respawn_dragon");
        TitleManager.registerTitle("minecraft:end/dragon_breath");
        TitleManager.registerTitle("minecraft:end/elytra");
        TitleManager.registerTitle("minecraft:end/levitate");
    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}