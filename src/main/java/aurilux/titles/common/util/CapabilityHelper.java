package aurilux.titles.common.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class CapabilityHelper {
    public static <T> void registerDummyCapability(Class<T> type, Callable<? extends T> factory) {
        CapabilityManager.INSTANCE.register(type, new DummyStorage<>(), factory);
    }

    public static <T extends INBTSerializable<CompoundNBT>> void registerDeferredCapability(Class<T> type, Callable<? extends T> factory) {
        CapabilityManager.INSTANCE.register(type, new DeferredStorage<>(), factory);
    }

    public static <T extends INBTSerializable<CompoundNBT>> void attach(AttachCapabilitiesEvent<?> event, ResourceLocation key, Capability<T> cap, T capInstance) {
        SerializableProvider<T> provider = new SerializableProvider<>(cap, capInstance);
        event.addCapability(key, provider);
        event.addListener(provider.capOptional::invalidate);
    }

    public static <T> void attach(AttachCapabilitiesEvent<?> event, ResourceLocation key, Capability<T> cap, T capInstance) {
        BasicProvider<T> provider = new BasicProvider<>(cap, capInstance);
        event.addCapability(key, provider);
        event.addListener(provider.capOptional::invalidate);
    }

    /*
    This is capability storage used when the capability data is saved and loaded automatically by SerializedProvider.
    The capability must implement INBTSerializable.
     */
    private static class DummyStorage<T> implements Capability.IStorage<T> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction side) { return null; }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {}
    }

    /*
    This capability storage is used when the capability data is saved and loaded manually, using BasicProvider.
     */
    private static class DeferredStorage<T extends INBTSerializable<CompoundNBT>> implements Capability.IStorage<T> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }
    }

    private static class BasicProvider<T> implements ICapabilityProvider {
        protected final T capInstance;
        protected final Capability<T> capability;
        protected final LazyOptional<T> capOptional;

        public BasicProvider(Capability<T> capability, T capInstance) {
            this.capability = capability;
            this.capInstance = capInstance;
            this.capOptional = LazyOptional.of(() -> this.capInstance);
        }

        @Nonnull
        @Override
        public <C> LazyOptional<C> getCapability(@Nonnull Capability<C> cap, @Nullable Direction side) {
            return capability.orEmpty(cap, capOptional);
        }
    }

    private static class SerializableProvider<T extends INBTSerializable<CompoundNBT>> extends BasicProvider<T> implements ICapabilitySerializable<CompoundNBT> {
        public SerializableProvider(Capability<T> capability, T capInstance) {
            super(capability, capInstance);
        }

        @Override
        public CompoundNBT serializeNBT() {
            return capInstance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            capInstance.deserializeNBT(nbt);
        }
    }
}
