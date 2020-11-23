package aurilux.titles.common.core;

import aurilux.titles.common.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.Rarity;

import javax.annotation.Nullable;
import java.util.Random;

public class TitleForEmeraldsAndFragmentsTrade implements VillagerTrades.ITrade {
    private final Rarity titleRarity;
    private final int maxUses;
    private final int xpValue;

    public TitleForEmeraldsAndFragmentsTrade(Rarity titleRarity, int maxUses, int xpValue) {
        this.titleRarity = titleRarity;
        this.maxUses = maxUses;
        this.xpValue = xpValue;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity trader, Random rand) {
        ItemStack firstInput = new ItemStack(Items.EMERALD);
        ItemStack secondInput = new ItemStack(ModItems.TITLE_FRAGMENT.get());
        ItemStack output;
        switch (titleRarity) {
            case RARE: output = new ItemStack(ModItems.TITLE_SCROLL_RARE.get());
                firstInput.setCount(8);
                secondInput.setCount(4);
            break;
            case UNCOMMON: output = new ItemStack(ModItems.TITLE_SCROLL_UNCOMMON.get());
                firstInput.setCount(4);
                secondInput.setCount(2);
            break;
            default: output = new ItemStack(ModItems.TITLE_SCROLL_COMMON.get());
                firstInput.setCount(2);
                secondInput.setCount(1);
            break;
        }
        return new MerchantOffer(firstInput, secondInput, output, maxUses, xpValue, .2f);
    }
}