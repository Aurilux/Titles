package aurilux.titles.common.util;

import aurilux.titles.common.TitlesMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class CapabilityHelper {
    private static class DummyStorage<T extends INBTSerializable<CompoundNBT>> implements Capability.IStorage<T> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction side) { return null; }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {}
    }

    public static <T extends INBTSerializable<CompoundNBT>> void registerDummyCapability(Class<T> type, Callable<? extends T> factory) {
        CapabilityManager.INSTANCE.register(type, new DummyStorage<>(), factory);
    }

    private static class DeferredStorage<T extends INBTSerializable<CompoundNBT>> implements Capability.IStorage<T> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
            TitlesMod.LOG.debug("Deferred - writing...");
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            TitlesMod.LOG.debug("Deferred - reading 1...");
            if (nbt instanceof CompoundNBT) {
                TitlesMod.LOG.debug("Deferred - reading 2...");
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }
    }

    public static <T extends INBTSerializable<CompoundNBT>> void registerDeferredCapability(Class<T> type, Callable<? extends T> factory) {
        CapabilityManager.INSTANCE.register(type, new DeferredStorage<>(), factory);
    }

    public static <C> void attach(AttachCapabilitiesEvent<?> event, ResourceLocation key, Capability<C> cap, C capInstance) {
        CapabilityHelper.SimpleCapabilityProvider<C> provider = new CapabilityHelper.SimpleCapabilityProvider<>(cap, capInstance);
        event.addCapability(key, provider);
        event.addListener(provider.capOptional::invalidate);
    }

    private static class SimpleCapabilityProvider<C> implements ICapabilityProvider {
        private final C capInstance;
        private final Capability<C> capability;
        private final LazyOptional<C> capOptional;

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
}
