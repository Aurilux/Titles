package aurilux.titles.common.item;

import aurilux.titles.common.Titles;
import net.minecraft.item.Item;

public class ItemArchiveFragment extends Item {
    public ItemArchiveFragment() {
        super(new Item.Properties()
            .maxStackSize(1)
            .group(Titles.itemGroup)
        );
    }
}