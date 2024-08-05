package aurilux.titles.common.entity.merchant.villager;

import aurilux.titles.common.init.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public record TitleForEmeraldsAndFragmentsTrade(Rarity titleRarity, int maxUses, int xpValue) implements VillagerTrades.ItemListing {
    @Override
    public MerchantOffer getOffer(Entity trader, RandomSource rand) {
        ItemStack firstInput = new ItemStack(Items.EMERALD);
        ItemStack secondInput = new ItemStack(ModItems.TITLE_FRAGMENT.get());
        ItemStack output;
        switch (titleRarity) {
            case RARE:
                output = new ItemStack(ModItems.TITLE_SCROLL_RARE.get());
                firstInput.setCount(8);
                secondInput.setCount(4);
                break;
            case UNCOMMON:
                output = new ItemStack(ModItems.TITLE_SCROLL_UNCOMMON.get());
                firstInput.setCount(4);
                secondInput.setCount(2);
                break;
            default:
                output = new ItemStack(ModItems.TITLE_SCROLL_COMMON.get());
                firstInput.setCount(2);
                secondInput.setCount(1);
                break;
        }
        return new MerchantOffer(firstInput, secondInput, output, maxUses, xpValue, .2f);
    }
}