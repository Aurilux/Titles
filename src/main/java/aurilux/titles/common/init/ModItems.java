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

import java.util.Collection;

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

    private static Item.Properties unstackable() {
        return new Item.Properties().stacksTo(1);
    }

    private static Item.Properties quarterStack() {
        return new Item.Properties().stacksTo(16);
    }

    public static Collection<RegistryObject<Item>> getAllItems() {
        return ITEMS.getEntries();
    }
}