package aurilux.titles.common;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.client.Keybinds;
import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.command.argument.TitleArgument;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.handler.CommonEventHandler;
import aurilux.titles.common.handler.LootHandler;
import aurilux.titles.common.impl.TitlesCapImpl;
import aurilux.titles.common.init.ModItems;
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
        forgeBus.addListener(TitleManager::register);
        // TODO The new JSON loot table system does not work with those generated like chests (simple_dungeon,
        //  stronghold_corridor, etc), so this is still necessary until they change it.
        if (TitlesConfig.COMMON.fragmentLoot.get()) {
            forgeBus.addListener(LootHandler::addLoot);
            forgeBus.addListener(LootHandler::onVillagerTrades);
        }
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CapabilityHelper.registerDummyCapability(ITitles.class, TitlesCapImpl::new);
        ArgumentTypes.register("titles:title", TitleArgument.class, new ArgumentSerializer<>(TitleArgument::title));
        PacketHandler.init();
        TitleManager.INSTANCE.init();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        Keybinds.init();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandTitles.register(event.getDispatcher());
    }
}