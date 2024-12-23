package aurilux.titles.common;

import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.core.TitlesCapability;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.data.*;
import aurilux.titles.common.handler.ConfigEventHandler;
import aurilux.titles.common.init.ModArgumentTypes;
import aurilux.titles.common.init.ModCreativeTabs;
import aurilux.titles.common.init.ModItems;
import aurilux.titles.common.network.TitlesNetwork;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TitlesMod.MOD_ID)
public class TitlesMod {
    public static final String MOD_ID = "titles";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID.toUpperCase());
    // public static final Rarity MYTHIC = Rarity.create("MYTHIC", ChatFormatting.GOLD);

    public TitlesMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TitlesConfig.COMMON_SPEC);
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        //modBus.addListener(this::clientSetup);
        modBus.addListener(this::gatherData);
        modBus.addListener(this::registerCapabilities);

        ModItems.register(modBus);
        ModArgumentTypes.register(modBus);
        ModCreativeTabs.register(modBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        TitlesNetwork.init();
        TitleRegistry.get().loadContributors();

        var forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(TitleRegistry::register);
        forgeBus.addListener(this::registerCommands);
        forgeBus.addListener(ConfigEventHandler::addLoot);
        forgeBus.addListener(ConfigEventHandler::onVillagerTrades);
        forgeBus.addListener(ConfigEventHandler::onTabListNameFormat);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        // Nothing yet
    }

    private void gatherData(@NotNull GatherDataEvent event) {
        var gen = event.getGenerator();
        var packoutput = gen.getPackOutput();
        var lookup = event.getLookupProvider();
        var fileHelper = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new LangGen(packoutput));
        gen.addProvider(event.includeClient(), new ItemModelGen(packoutput, fileHelper));

        gen.addProvider(event.includeServer(), new TitlesGen(packoutput));
        gen.addProvider(event.includeServer(), new AdvancementGen(packoutput, lookup, fileHelper));
        gen.addProvider(event.includeServer(), new RecipeGen(packoutput));
        gen.addProvider(event.includeServer(), new TagGen.BlockTags(packoutput, lookup, fileHelper));
    }

    private void registerCommands(@NotNull RegisterCommandsEvent event) {
        CommandTitles.register(event.getDispatcher());
    }

    private void registerCapabilities(@NotNull RegisterCapabilitiesEvent event) {
        event.register(TitlesCapability.class);
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation prefix(String path) {
        return new ResourceLocation(TitlesMod.MOD_ID, path);
    }
}