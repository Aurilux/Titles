package aurilux.titles.common;

import aurilux.titles.api.capability.TitlesCapability;
import aurilux.titles.client.Keybinds;
import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.handler.LootHandler;
import aurilux.titles.common.item.ModItems;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.util.CapabilityHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TitlesMod.ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TitlesMod {
    public static final String ID = "titles";
    public static final Logger LOG = LogManager.getLogger(ID.toUpperCase());

    public static ItemGroup itemGroup = new ItemGroup(ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.TITLE_FRAGMENT.get());
        }
    };

    public TitlesMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TitlesConfig.COMMON_SPEC);

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::registerCommands);
        if (TitlesConfig.COMMON.fragmentLoot.get()) {
            forgeBus.addListener(LootHandler::addLoot);
        }
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        // TODO was here last. Seems the capability doesn't save on world close. Possibly some problem with the IStorage?
        // The LOG calls I put in the read and write methods don't ever show up.
        CapabilityHelper.registerDeferredCapability(TitlesCapability.class, TitlesCapability::new);
        TitleRegistry.INSTANCE.init();
        PacketHandler.init();
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        Keybinds.init();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandTitles.register(event.getDispatcher());
    }
}