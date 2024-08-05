package aurilux.titles.common.init;

import aurilux.titles.common.TitlesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TitlesMod.MOD_ID);
    public static RegistryObject<CreativeModeTab> TAB_TITLES = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.literal("Titles"))
            .icon(() -> ModItems.TITLE_SCROLL_COMMON.get().asItem().getDefaultInstance())
            .displayItems((featureFlags, output) -> {
                ModItems.getAllItems().forEach(e -> {
                    Item item = e.get();
                    output.accept(item);
                });
            })
            .build());

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
