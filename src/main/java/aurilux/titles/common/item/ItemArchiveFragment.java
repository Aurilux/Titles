package aurilux.titles.common.item;

import aurilux.titles.common.Titles;
import net.minecraft.item.Item;

public class ItemArchiveFragment extends Item {
    public ItemArchiveFragment() {
        this.setCreativeTab(Titles.CREATIVE_TAB);
        this.setMaxStackSize(64);
        this.setRegistryName(Titles.MOD_ID, "archiveFragment");
        this.setTranslationKey("archiveFragment");
    }
}