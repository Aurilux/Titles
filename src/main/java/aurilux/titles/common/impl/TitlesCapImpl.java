package aurilux.titles.common.impl;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class TitlesCapImpl implements ITitles {
    public static final ResourceLocation NAME = new ResourceLocation(TitlesAPI.MOD_ID, "titles");

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
        return obtainedTitles;
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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT data = new CompoundNBT();
        data.putBoolean(GENDER_SETTING, genderSetting);
        data.putString(DISPLAY_TITLE, getDisplayTitle().getKey());
        ListNBT obtained = new ListNBT();
        for (Title title : obtainedTitles) {
            obtained.add(StringNBT.valueOf(title.getKey()));
        }
        data.put(OBTAINED_TITLES, obtained);
        return data;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        genderSetting = nbt.getBoolean(GENDER_SETTING);
        displayTitle = TitlesAPI.internal().getTitle(nbt.getString(DISPLAY_TITLE));
        ListNBT obtained = (ListNBT) nbt.get(OBTAINED_TITLES);
        for (int i = 0; i < obtained.size(); i++) {
            Title title = TitlesAPI.internal().getTitle(obtained.getString(i));
            add(title);
        }
    }
}