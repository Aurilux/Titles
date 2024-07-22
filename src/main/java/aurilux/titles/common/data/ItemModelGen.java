package aurilux.titles.common.data;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGen extends ItemModelProvider {
    public ItemModelGen(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, TitlesMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generated(ModItems.TITLE_FRAGMENT.getId().getPath(), modLoc("item/title_fragment"));
        generated(ModItems.TITLE_SCROLL_COMMON.getId().getPath(), modLoc("item/title_scroll_common"));
        generated(ModItems.TITLE_SCROLL_UNCOMMON.getId().getPath(), modLoc("item/title_scroll_uncommon"));
        generated(ModItems.TITLE_SCROLL_RARE.getId().getPath(), modLoc("item/title_scroll_rare"));
    }

    private void generated(String name, ResourceLocation texture) {
        singleTexture(name, mcLoc("item/generated"), "layer0", texture);
    }
}