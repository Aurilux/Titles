package aurilux.titles.common;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.common.capability.TitlesImpl;
import aurilux.titles.common.command.CommandTitles;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.handler.InternalMethodHandler;
import aurilux.titles.common.handler.LootHandler;
import aurilux.titles.common.item.ModItems;
import aurilux.titles.common.network.PacketHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod(Titles.MOD_ID)
public class Titles {
    public static final String MOD_ID = "titles";
    public static final String MOD_NAME = "Titles";

    public static Titles instance;

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID.toUpperCase());

    public static ItemGroup itemGroup = new ItemGroup(MOD_NAME) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.titleArchive);
        }
    };

    public Titles() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        //this.registerModConfigs(TitlesConfig.CLIENT_SPEC, null);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TitlesConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TitlesConfig.COMMON_SPEC);

        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        TitlesAPI.internalHandler = new InternalMethodHandler();
        CapabilityManager.INSTANCE.register(ITitles.class,
                new Capability.IStorage<ITitles>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<ITitles> capability, ITitles instance, Direction side) {
                        return instance.serializeNBT();
                    }

                    @Override
                    public void readNBT(Capability<ITitles> capability, ITitles instance, Direction side, INBT nbt) {
                        instance.deserializeNBT((CompoundNBT) nbt);
                    }
                }, TitlesImpl::new);
        //CapabilityHelper.registerDeferredCapability(ITitles.class, TitlesImpl::new);
        MinecraftForge.EVENT_BUS.register(new LootHandler());
        TitleRegistry.INSTANCE.init();
        PacketHandler.init();
    }

    private void clientSetup(FMLClientSetupEvent event) {
    }

    //TODO do I even need this? Will be determined after the API rework
    private void serverAboutToStart(FMLServerAboutToStartEvent event) {
        String className = TitlesAPI.internalHandler.getClass().getName();
        String expected = "aurilux.titles.common.handler.InternalMethodHandler";
        if(!className.equals(expected)) {
            throw new IllegalAccessError("The Titles API internal method handler has been overridden. "
                    + "(Expected classname: " + expected + ", Actual classname: " + className + ")");
        }
    }

    private void serverStarting(FMLServerStartingEvent event) {
        CommandTitles.register(event.getCommandDispatcher());
    }
}