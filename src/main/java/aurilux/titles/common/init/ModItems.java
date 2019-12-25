package aurilux.titles.common.init;

import aurilux.titles.common.Titles;
import aurilux.titles.common.item.ItemArchiveFragment;
import aurilux.titles.common.item.ItemTitleArchive;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID)
public class ModItems {
    public static final ItemTitleArchive titleArchive = new ItemTitleArchive();
    public static final ItemArchiveFragment archiveFragment = new ItemArchiveFragment();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();

        reg.registerAll(titleArchive, archiveFragment);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        registerRender(titleArchive);
        registerRender(archiveFragment);
    }

    @SideOnly(Side.CLIENT)
    private static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
