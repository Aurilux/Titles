package aurilux.titles.api.capability;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.common.Titles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TitlesImpl {
    @CapabilityInject(ITitles.class)
    public static final Capability<ITitles> TITLES_CAPABILITY = null;

    public static final ResourceLocation NAME = new ResourceLocation(Titles.MOD_ID, "titles");

    public static void register() {
        CapabilityManager.INSTANCE.register(ITitles.class, new Storage(), DefaultImpl::new);
    }

    public static DefaultImpl getCapability(EntityPlayer player) {
        return (DefaultImpl) player.getCapability(TITLES_CAPABILITY, null);
    }

    public interface ITitles extends INBTSerializable<NBTTagCompound> {
        void add(TitleInfo info);

        void remove(TitleInfo info);

        void setObtainedTitles(Set<TitleInfo> newTitles);

        Set<TitleInfo> getObtainedTitles();

        void setSelectedTitle(@Nonnull TitleInfo newTitle);

        TitleInfo getSelectedTitle();
    }

    private static class Storage implements Capability.IStorage<ITitles> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ITitles> capability, ITitles instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<ITitles> capability, ITitles instance, EnumFacing side, NBTBase nbt) {
            //do nothing
        }
    }

    public static class DefaultImpl implements ITitles {
        private Set<TitleInfo> obtainedTitles = new HashSet<>();
        private TitleInfo selectedTitle = TitleInfo.NULL_TITLE;

        public DefaultImpl() {}

        @Override
        public void add(TitleInfo info) {
            obtainedTitles.add(info);
        }

        @Override
        public void remove(TitleInfo info) {
            obtainedTitles.remove(info);
        }

        @Override
        public void setObtainedTitles(Set<TitleInfo> newTitles) {
            obtainedTitles = newTitles;
        }

        @Override
        public Set<TitleInfo> getObtainedTitles() {
            return obtainedTitles;
        }

        @Override
        public void setSelectedTitle(@Nonnull TitleInfo newTitle) {
            selectedTitle = newTitle;
        }

        @Override
        public TitleInfo getSelectedTitle() {
            return selectedTitle;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("selectedTitle", this.getSelectedTitle().writeToNBT());

            NBTTagList obtained = new NBTTagList();
            for (TitleInfo title : obtainedTitles) {
                obtained.appendTag(title.writeToNBT());
            }

            nbt.setTag("obtainedTitles", obtained);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            NBTTagCompound n1 = (NBTTagCompound) nbt.getTag("selectedTitle");
            this.setSelectedTitle(TitleInfo.readFromNBT(n1));

            NBTTagList obtained = nbt.getTagList("obtainedTitles", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < obtained.tagCount(); i++) {
                obtainedTitles.add(TitleInfo.readFromNBT(obtained.getCompoundTagAt(i)));
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private ITitles instance = new DefaultImpl();

        public Provider() {}

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == TITLES_CAPABILITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return hasCapability(capability, facing) ? TITLES_CAPABILITY.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            //return TITLES_CAPABILITY.getStorage().writeNBT(TITLES_CAPABILITY, this.instance, null);
            return this.instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            //TITLES_CAPABILITY.getStorage().readNBT(TITLES_CAPABILITY, this.instance, null, nbt);
            this.instance.deserializeNBT(nbt);
        }
    }
}