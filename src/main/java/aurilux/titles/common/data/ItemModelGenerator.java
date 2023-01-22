package aurilux.titles.common.data;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TitlesMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.TITLE_FRAGMENT.get());
        simpleItem(ModItems.TITLE_SCROLL_COMMON.get());
        simpleItem(ModItems.TITLE_SCROLL_UNCOMMON.get());
        simpleItem(ModItems.TITLE_SCROLL_RARE.get());
    }

    private String name(Item item) {
        return item.getDescriptionId();
    }

    private ResourceLocation itemPath(String name) {
        return modLoc("item/" + name);
    }

    private void generated(String name) {
        generated(name, itemPath(name));
    }

    private void generated(String name, ResourceLocation texture) {
        singleTexture(name, new ResourceLocation("item/generated"), "layer0", texture);
    }

    private void simpleItem(Item item) {
        generated(name(item));
    }
}