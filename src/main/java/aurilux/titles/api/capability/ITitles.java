package aurilux.titles.api.capability;

import aurilux.titles.api.TitleInfo;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;

public interface ITitles extends INBTSerializable<CompoundNBT> {
    void add(TitleInfo info);

    void remove(TitleInfo info);

    Set<TitleInfo> getObtainedTitles();

    void setSelectedTitle(TitleInfo newTitle);

    TitleInfo getSelectedTitle();

    void addFragments(int num);

    int getFragmentCount();

    void setFragmentCount(int count);
}