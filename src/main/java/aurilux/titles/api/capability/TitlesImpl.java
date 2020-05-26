package aurilux.titles.api.capability;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.Titles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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

        Set<TitleInfo> getObtainedTitles();

        void setDisplayTitle(TitleInfo newTitle);

        TitleInfo getDisplayTitle();

        void addFragments(int num);

        int getFragmentCount();

        void setFragmentCount(int count);
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
        private final String SEL_TITLE = "selected_title";
        private final String OBT_TITLES = "obtained_titles";
        private final String FRAG_COUNT = "fragment_count";

        private Set<TitleInfo> obtainedTitles = new HashSet<>();
        private TitleInfo displayTitle = TitleInfo.NULL_TITLE;

        private int fragmentCount = 0;

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
        public Set<TitleInfo> getObtainedTitles() {
            return obtainedTitles;
        }

        @Override
        public void setDisplayTitle(TitleInfo newTitle) {
            displayTitle = newTitle;
        }

        @Override
        public TitleInfo getDisplayTitle() {
            return displayTitle;
        }

        @Override
        public void addFragments(int num) {
            fragmentCount += num;
        }

        @Override
        public int getFragmentCount() {
            return fragmentCount;
        }

        @Override
        public void setFragmentCount(int count) {
            fragmentCount = count;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger(FRAG_COUNT, this.getFragmentCount());

            nbt.setString(SEL_TITLE, this.getDisplayTitle().getKey());

            NBTTagList obtained = new NBTTagList();
            for (TitleInfo title : obtainedTitles) {
                obtained.appendTag(new NBTTagString(title.getKey()));
            }
            nbt.setTag(OBT_TITLES, obtained);

            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            fragmentCount = nbt.getInteger(FRAG_COUNT);

            displayTitle = TitlesAPI.getTitleFromKey(nbt.getString(SEL_TITLE));

            obtainedTitles.clear();
            NBTTagList obtained = nbt.getTagList(OBT_TITLES, Constants.NBT.TAG_STRING);
            for (int i = 0; i < obtained.tagCount(); i++) {
                TitleInfo titleInfo = TitlesAPI.getTitleFromKey(obtained.getStringTagAt(i));
                if (!titleInfo.equals(TitleInfo.NULL_TITLE)) {
                    obtainedTitles.add(titleInfo);
                }
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
            return this.instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            this.instance.deserializeNBT(nbt);
        }
    }
}