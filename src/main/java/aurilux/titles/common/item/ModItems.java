package aurilux.titles.common.item;

import aurilux.titles.common.Titles;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Titles.MOD_ID)
public class ModItems {
    @ObjectHolder(Names.ARCHIVE)
    public static Item titleArchive;
    @ObjectHolder(Names.FRAGMENT)
    public static Item archiveFragment;

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
        reg.register(thing.setRegistryName(new ResourceLocation(Titles.MOD_ID, name)));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();

        register(reg, new ItemTitleArchive(), Names.ARCHIVE);
        register(reg, new ItemArchiveFragment(), Names.FRAGMENT);
    }

    public final class Names {
        public static final String ARCHIVE = "titles_archive";
        public static final String FRAGMENT = "archive_fragment";
    }
}
