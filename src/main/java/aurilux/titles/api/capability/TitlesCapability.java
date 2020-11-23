package aurilux.titles.api.capability;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class ITitles implements INBTSerializable<CompoundNBT> {
    private final String DISPLAY_TITLE = "display_title";
    private final String OBT_TITLES = "obtained_titles";

    public static final ResourceLocation NAME = new ResourceLocation(TitlesMod.ID, "titles");

    //A cache of the titles this player has obtained
    public Set<Title> unlockedTitles = new HashSet<>();
    public Title displayTitle = Title.NULL_TITLE;

    public ITitles() {}

    public boolean add(Title title) {
        return !title.isNull() && unlockedTitles.add(title);
    }

    public void remove(Title title) {
        unlockedTitles.remove(title);
    }

    public boolean hasTitle(Title title) {
        return unlockedTitles.contains(title);
    }

    public Set<Title> getUnlockedTitles() {
        return unlockedTitles;
    }

    public void setDisplayTitle(Title newTitle) {
        displayTitle = newTitle;
    }

    public Title getDisplayTitle() {
        return displayTitle;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT data = new CompoundNBT();

        data.putString(DISPLAY_TITLE, getDisplayTitle().getKey());

        ListNBT obtained = new ListNBT();
        for (Title title : unlockedTitles) {
            obtained.add(StringNBT.valueOf(title.getKey()));
        }
        data.put(OBT_TITLES, obtained);

        return data;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        displayTitle = TitlesAPI.instance().getTitle(nbt.getString(DISPLAY_TITLE));

        ListNBT obtained = (ListNBT) nbt.get(OBT_TITLES);
        for (int i = 0; i < obtained.size(); i++) {
            Title title = TitlesAPI.instance().getTitle(obtained.getString(i));
            add(title);
        }
    }
}