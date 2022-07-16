package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TitlesCapability {
    @CapabilityInject(TitlesCapability.class)
    public static final Capability<TitlesCapability> TITLES_CAPABILITY = null;
    public static final ResourceLocation NAME = TitlesMod.prefix("titles");

    private final String GENDER_SETTING = "gender_setting";
    private final String DISPLAY_TITLE = "display_title";
    private final String OBTAINED_TITLES = "obtained_titles";
    private final Set<Title> obtainedTitles = new HashSet<>();

    private Title displayTitle = Title.NULL_TITLE;
    private boolean genderSetting = true;

    public boolean add(Title title) {
        return !title.isNull() && obtainedTitles.add(title);
    }

    public void remove(Title title) {
        obtainedTitles.remove(title);
    }

    public boolean hasTitle(Title title) {
        return obtainedTitles.contains(title);
    }

    public Set<Title> getObtainedTitles() {
        return new HashSet<>(obtainedTitles);
    }

    public void setDisplayTitle(Title newTitle) {
        displayTitle = newTitle;
    }

    public Title getDisplayTitle() {
        return displayTitle;
    }

    public boolean getGenderSetting() {
        return genderSetting;
    }

    public void setGenderSetting(boolean newSetting) {
        genderSetting = newSetting;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT data = new CompoundNBT();
        data.putBoolean(GENDER_SETTING, genderSetting);
        data.putString(DISPLAY_TITLE, getDisplayTitle().getID().toString());
        ListNBT obtained = new ListNBT();
        for (Title title : obtainedTitles) {
            obtained.add(StringNBT.valueOf(title.getID().toString()));
        }
        data.put(OBTAINED_TITLES, obtained);
        return data;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        obtainedTitles.clear();
        genderSetting = nbt.getBoolean(GENDER_SETTING);
        displayTitle = TitleManager.getTitle(nbt.getString(DISPLAY_TITLE));
        ListNBT obtained = (ListNBT) nbt.get(OBTAINED_TITLES);
        for (int i = 0; i < obtained.size(); i++) {
            Title title = TitleManager.getTitle(obtained.getString(i));
            add(title);
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        protected final TitlesCapability capInstance;
        protected final LazyOptional<TitlesCapability> capOptional;

        public Provider() {
            this.capInstance = new TitlesCapability();
            this.capOptional = LazyOptional.of(() -> this.capInstance);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == TITLES_CAPABILITY) {
                return capOptional.cast();
            }
            return LazyOptional.empty();
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

    // Just a dummy storage since data is saved and loaded from the provider
    public static class Storage implements Capability.IStorage<TitlesCapability> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<TitlesCapability> capability, TitlesCapability instance, Direction side) { return null; }

        @Override
        public void readNBT(Capability<TitlesCapability> capability, TitlesCapability instance, Direction side, INBT nbt) {}
    }
}