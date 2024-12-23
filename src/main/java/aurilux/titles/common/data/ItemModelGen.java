package aurilux.titles.common.data;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGen extends ItemModelProvider {
    public ItemModelGen(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, TitlesMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        generated(ModItems.TITLE_FRAGMENT.getId().getPath());
        generated(ModItems.TITLE_SCROLL_COMMON.getId().getPath());
        generated(ModItems.TITLE_SCROLL_UNCOMMON.getId().getPath());
        generated(ModItems.TITLE_SCROLL_RARE.getId().getPath());
    }

    private void generated(String name) {
        singleTexture(name, mcLoc("item/generated"), "layer0", modLoc("item/" + name));
    }
}