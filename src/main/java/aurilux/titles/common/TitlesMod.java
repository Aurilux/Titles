package aurilux.titles.common;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.client.Keybinds;
import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.command.argument.TitleArgument;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.handler.LootHandler;
import aurilux.titles.common.impl.TitlesCapImpl;
import aurilux.titles.common.item.ModItems;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.util.CapabilityHelper;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(TitlesAPI.MOD_ID)
public class TitlesMod {
    public static final Logger LOG = LogManager.getLogger(TitlesAPI.MOD_ID.toUpperCase());

    public static ItemGroup itemGroup = new ItemGroup(TitlesAPI.MOD_ID) {
        @Nonnull
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.TITLE_SCROLL_COMMON.get());
        }
    };

    public TitlesMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TitlesConfig.COMMON_SPEC);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
        ModItems.register(modBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::registerCommands);
        // TODO The new JSON loot table system does not work with those generated like chests (simple_dungeon,
        //  stronghold_corridor, etc), so this is still necessary until they change it.
        if (TitlesConfig.COMMON.fragmentLoot.get()) {
            forgeBus.addListener(LootHandler::addLoot);
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CapabilityHelper.registerDummyCapability(ITitles.class, TitlesCapImpl::new);
        ArgumentTypes.register("titles:title", TitleArgument.class, new ArgumentSerializer<>(TitleArgument::title));
        TitleRegistry.INSTANCE.init();
        PacketHandler.init();

        TitlesAPI.registerCommonTitles(TitlesAPI.MOD_ID,
                "spelunker",
                "chicken_chaser",
                "captain",
                "melon_lord");
        TitlesAPI.registerCommonTitles("minecraft",
                "story/iron_tools",
                "story/obtain_armor",
                "story/deflect_arrow",
                "story/enchant_item",
                "story/enter_the_end",

                "husbandry/breed_an_animal",
                "husbandry/fishy_business",

                "adventure/kill_a_mob",
                "adventure/trade",
                "adventure/shoot_arrow",

                "nether/obtain_crying_obsidian",
                "nether/loot_bastion",
                "nether/charge_respawn_anchor",
                "nether/brew_potion",

                "end/kill_dragon");

        TitlesAPI.registerUncommonTitles(TitlesAPI.MOD_ID,
                "pincushion");
        TitlesAPI.registerUncommonTitles("minecraft",
                "story/cure_zombie_villager",

                "adventure/totem_of_undying",
                "adventure/summon_iron_golem",

                "nether/create_full_beacon",

                "end/dragon_egg",
                "end/respawn_dragon",
                "end/dragon_breath",
                "end/elytra");

        TitlesAPI.registerRareTitles(TitlesAPI.MOD_ID,
                "opulent");
        TitlesAPI.registerRareTitles("minecraft",
                "husbandry/bred_all_animals",
                "husbandry/complete_catalogue",
                "husbandry/balanced_diet",
                "husbandry/obtain_netherite_hoe",

                "adventure/hero_of_the_village",
                "adventure/kill_all_mobs",
                "adventure/two_birds_one_arrow",
                "adventure/kill_all_mobs",
                "adventure/arbalistic",
                "adventure/adventuring_time",
                "adventure/sniper_duel",
                "adventure/bullseye",

                "nether/return_to_sender",
                "nether/fast_travel",
                "nether/uneasy_alliance",
                "nether/netherite_armor",
                "nether/explore_nether",
                "nether/all_potions",
                "nether/all_effects",

                "end/levitate");
    }

    public void clientSetup(FMLClientSetupEvent event) {
        Keybinds.init();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandTitles.register(event.getDispatcher());
    }
}