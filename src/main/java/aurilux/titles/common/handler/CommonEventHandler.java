package aurilux.titles.common.handler;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.Titles;
import aurilux.titles.common.capability.TitlesImpl;
import aurilux.titles.common.network.PacketHandler;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            attach(event, TitlesImpl.NAME, TitlesAPI.TITLES_CAPABILITY, new TitlesImpl());
        }
    }

    public static <C> void attach(AttachCapabilitiesEvent<?> event, ResourceLocation key, Capability<C> cap, C capInstance) {
        SimpleCapabilityProvider<C> provider = new SimpleCapabilityProvider<>(cap, capInstance);
        event.addCapability(key, provider);
        event.addListener(provider.capOptional::invalidate);
    }

    private static class SimpleCapabilityProvider<C> implements ICapabilityProvider {
        private final C capInstance;
        private final LazyOptional<C> capOptional;

        private final Capability<C> capability;

        public SimpleCapabilityProvider(Capability<C> capability, C capInstance) {
            this.capability = capability;
            this.capInstance = capInstance;
            this.capOptional = LazyOptional.of(() -> this.capInstance);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return capability.orEmpty(cap, capOptional);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            //Get data from the old player...
            CompoundNBT data = TitlesAPI.getTitlesCap(event.getOriginal()).serializeNBT();
            //..and give it to the new clone player
            TitlesAPI.getTitlesCap(event.getPlayer()).deserializeNBT(data);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        AdvancementManager manager = player.getServerWorld().getServer().getAdvancementManager();
        Titles.LOGGER.info("How many advancements do we have?:" + manager.getAllAdvancements().size());

        PacketHandler.sendDataOnLogin(player);
        //TODO update when network is finished
        //PacketDispatcher.INSTANCE.sendToAll(new PacketSyncSelectedTitle(player.getUniqueID(), TitlesAPI.getPlayerSelectedTitle(player).getKey()));
    }

    @SubscribeEvent
    public static void onPlayerNameFormat(PlayerEvent.NameFormat event) {
        TitleInfo currentTitle = TitlesAPI.getPlayerSelectedTitle(event.getPlayer());
        event.setDisplayname(event.getDisplayname() + TitlesAPI.getFormattedTitle(currentTitle, true));
    }
}