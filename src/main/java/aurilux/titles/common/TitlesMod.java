package aurilux.titles.common;

import aurilux.titles.client.Keybinds;
import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.command.argument.TitleArgument;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.core.TitlesCapability;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.data.ItemModelGenerator;
import aurilux.titles.common.data.LangGenerator;
import aurilux.titles.common.data.TitlesGenerator;
import aurilux.titles.common.handler.LootHandler;
import aurilux.titles.common.init.ModItems;
import aurilux.titles.common.network.TitlesNetwork;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(TitlesMod.MOD_ID)
public class TitlesMod {
    public static final String MOD_ID = "titles";
    public static final Logger LOG = LogManager.getLogger(MOD_ID.toUpperCase());
    public static final Rarity MYTHIC = Rarity.create("MYTHIC", TextFormatting.GOLD);
    public static final ItemGroup TAB = new ItemGroup(TitlesMod.MOD_ID) {
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
        modBus.addListener(this::gatherData);
        ModItems.register(modBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(TitleRegistry::register);
        forgeBus.addListener(this::registerCommands);
        // TODO The new JSON loot table system does not work with those generated like chests (simple_dungeon,
        //  stronghold_corridor, etc), so this is still necessary until they change it.
        if (TitlesConfig.COMMON.fragmentLoot.get()) {
            forgeBus.addListener(LootHandler::addLoot);
            forgeBus.addListener(LootHandler::onVillagerTrades);
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(TitlesCapability.class, new TitlesCapability.Storage(), TitlesCapability::new);
        ArgumentTypes.register("titles:title", TitleArgument.class, new ArgumentSerializer<>(TitleArgument::title));
        TitlesNetwork.init();
        TitleRegistry.get().loadContributors();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        Keybinds.init();
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            gen.addProvider(new TitlesGenerator(gen));
        }
        if (event.includeClient()) {
            gen.addProvider(new LangGenerator(gen));
            gen.addProvider(new ItemModelGenerator(gen, fileHelper));
        }
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandTitles.register(event.getDispatcher());
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(TitlesMod.MOD_ID, path);
    }
}