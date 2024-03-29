package aurilux.titles.common.init;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.item.ItemTitleFragment;
import aurilux.titles.common.item.ItemTitleScroll;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TitlesMod.MOD_ID);

    public static final RegistryObject<ItemTitleFragment> TITLE_FRAGMENT = ITEMS.register("title_fragment", () ->
            new ItemTitleFragment(quarterStack()));
    public static final RegistryObject<ItemTitleScroll> TITLE_SCROLL_COMMON = ITEMS.register("title_scroll_common",() ->
            new ItemTitleScroll(unstackable().rarity(Rarity.COMMON)));
    public static final RegistryObject<ItemTitleScroll> TITLE_SCROLL_UNCOMMON = ITEMS.register("title_scroll_uncommon", () ->
            new ItemTitleScroll(unstackable().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ItemTitleScroll> TITLE_SCROLL_RARE = ITEMS.register("title_scroll_rare", () ->
            new ItemTitleScroll(unstackable().rarity(Rarity.RARE)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static Item.Properties baseProp() {
        return new Item.Properties().tab(TitlesMod.TAB);
    }

    private static Item.Properties unstackable() {
        return baseProp().stacksTo(1);
    }

    private static Item.Properties quarterStack() {
        return baseProp().stacksTo(16);
    }
}