package aurilux.titles.common.capability;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.common.Titles;
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

    public Set<TitleInfo> obtainedTitles = new HashSet<>();
    public TitleInfo selectedTitle = TitleInfo.NULL_TITLE;
    public int fragmentCount = 0;

    public TitlesImpl() {}

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
        for (TitleInfo title : getObtainedTitles()) {
            obtained.add(StringNBT.valueOf(title.getKey()));
        }
        data.put(OBT_TITLES, obtained);

        return data;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setFragmentCount(nbt.getInt(FRAG_COUNT));
        setSelectedTitle(TitlesAPI.getTitleFromKey(nbt.getString(SEL_TITLE)));

        ListNBT obtained = (ListNBT) nbt.get(OBT_TITLES);
        for (int i = 0; i < obtained.size(); i++) {
            TitleInfo titleInfo = TitlesAPI.getTitleFromKey(obtained.getString(i));
            if (!titleInfo.equals(TitleInfo.NULL_TITLE)) {
                add(titleInfo);
            }
        }
    }
}