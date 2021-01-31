package aurilux.titles.api.capability;

import aurilux.titles.api.Title;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;

public interface ITitles extends INBTSerializable<CompoundNBT> {
    boolean add(Title title);

    void remove(Title title);

    boolean hasTitle(Title title);

    Set<Title> getObtainedTitles();

    void setDisplayTitle(Title newTitle);

    Title getDisplayTitle();

    // True = male, false = female
    // This is just a setting for gender-specific titles (Lord vs Lady), or gendered languages such as Spanish
    boolean getGenderSetting();

    void setGenderSetting(boolean newSetting);
}