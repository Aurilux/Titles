package aurilux.titles.common.item;

import aurilux.titles.common.Titles;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemAux extends Item {
    public ItemAux(String name) {
        setCreativeTab(Titles.CREATIVE_TAB);
        setRegistryName(new ResourceLocation(Titles.MOD_ID, name));
        setTranslationKey(name);
    }
}