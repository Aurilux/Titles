package aurilux.titles.common;

import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.core.TitlesCapability;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.data.ItemModelGenerator;
import aurilux.titles.common.data.LangGenerator;
import aurilux.titles.common.data.TitlesGenerator;
import aurilux.titles.common.handler.ConfigEventHandler;
import aurilux.titles.common.init.ModArgumentTypes;
import aurilux.titles.common.init.ModItems;
import aurilux.titles.common.network.TitlesNetwork;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

@Mod(TitlesMod.MOD_ID)
public class TitlesMod {
    public static final String MOD_ID = "titles";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID.toUpperCase());
    // public static final Rarity MYTHIC = Rarity.create("MYTHIC", ChatFormatting.GOLD);

    public TitlesMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TitlesConfig.COMMON_SPEC);
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::makeCreativeTab);
        modBus.addListener(this::gatherData);
        modBus.addListener(this::registerCapabilities);
        ModItems.register(modBus);
        ModArgumentTypes.register(modBus);

    private void commonSetup(FMLCommonSetupEvent event) {
        TitlesNetwork.init();
        TitleRegistry.get().loadContributors();

        var forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(TitleRegistry::register);
        forgeBus.addListener(this::registerCommands);

        event.enqueueWork(() -> {
            if (TitlesConfig.COMMON.fragmentLoot.get()) {
                forgeBus.addListener(ConfigEventHandler::addLoot);
                forgeBus.addListener(ConfigEventHandler::onVillagerTrades);
            }

            if (TitlesConfig.SERVER.showInTablist.get()) {
                forgeBus.addListener(ConfigEventHandler::onTabListNameFormat);
            }
        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        // Nothing yet
    }

    private void gatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var packoutput = gen.getPackOutput();
        var fileHelper = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new LangGenerator(packoutput));
        gen.addProvider(event.includeClient(), new ItemModelGenerator(packoutput, fileHelper));

        gen.addProvider(event.includeServer(), new TitlesGenerator(packoutput));
    }

    private void makeCreativeTab(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "main"), builder -> {
            builder.title(Component.literal("Titles"))
                    .icon(() -> new ItemStack(ModItems.TITLE_SCROLL_COMMON.get()));
        });
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandTitles.register(event.getDispatcher());
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(TitlesCapability.class);
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(TitlesMod.MOD_ID, path);
    }
}