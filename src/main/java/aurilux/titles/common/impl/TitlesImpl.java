package aurilux.titles.common.impl;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.common.Titles;
import aurilux.titles.common.core.TitleRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class TitlesImpl implements ITitles {
    private final String SEL_TITLE = "selected_title";
    private final String OBT_TITLES = "obtained_titles";
    private final String FRAG_COUNT = "fragment_count";

    public static final ResourceLocation NAME = new ResourceLocation(Titles.MOD_ID, "titles");

    //A cache of the titles this player has obtained
    public Set<TitleInfo> unlockedTitles = new HashSet<>();
    public TitleInfo selectedTitle = TitleInfo.NULL_TITLE;
    public int fragmentCount = 0;

    public TitlesImpl() {}

    @Override
    public boolean add(TitleInfo title) {
        return !title.isNull() && unlockedTitles.add(title);
    }

    @Override
    public void remove(TitleInfo title) {
        unlockedTitles.remove(title);
    }

    @Override
    public boolean hasTitle(TitleInfo title) {
        return unlockedTitles.contains(title);
    }

    @Override
    public Set<TitleInfo> getUnlockedTitles() {
        return unlockedTitles;
    }

    @Override
    public void setSelectedTitle(TitleInfo newTitle) {
        selectedTitle = newTitle;
    }

    @Override
    public TitleInfo getSelectedTitle() {
        return selectedTitle;
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
    public CompoundNBT serializeNBT() {
        CompoundNBT data = new CompoundNBT();

        data.putInt(FRAG_COUNT, getFragmentCount());
        data.putString(SEL_TITLE, getSelectedTitle().getKey());

        ListNBT obtained = new ListNBT();
        for (TitleInfo title : unlockedTitles) {
            obtained.add(StringNBT.valueOf(title.getKey()));
        }
        data.put(OBT_TITLES, obtained);

        return data;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        fragmentCount = nbt.getInt(FRAG_COUNT);
        selectedTitle = TitleRegistry.INSTANCE.getTitle(nbt.getString(SEL_TITLE));

        ListNBT obtained = (ListNBT) nbt.get(OBT_TITLES);
        for (int i = 0; i < obtained.size(); i++) {
            TitleInfo title = TitleRegistry.INSTANCE.getTitle(obtained.getString(i));
            add(title);
        }
    }
}