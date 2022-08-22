package aurilux.titles.common.item;

import aurilux.titles.api.Title;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.core.TitlesCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ItemTitleScroll extends Item {
    public ItemTitleScroll(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (world.isRemote) {
            return super.onItemRightClick(world, player, hand);
        }

        ItemStack itemStack = player.getHeldItem(hand);
        AtomicBoolean unlockedTitle = new AtomicBoolean(false);
        TitleManager.doIfPresent(player, cap -> {
            List<Title> possibleLoot = TitleManager.getTitlesOfType(Title.AwardType.LOOT).values().stream()
                    .filter(t -> t.getRarity().equals(this.getRarity(itemStack)) && !cap.getObtainedTitles().contains(t))
                    .collect(Collectors.toList());
            if (possibleLoot.size() > 0) {
                Title newTitle = possibleLoot.get(world.rand.nextInt(possibleLoot.size()));
                TitleManager.unlockTitle((ServerPlayerEntity) player, newTitle.getID());
                player.sendMessage(new TranslationTextComponent("chat.scroll.success",
                        TitleManager.getFormattedTitle(newTitle, cap.getGenderSetting())), player.getUniqueID());
                itemStack.setCount(0);
                unlockedTitle.set(true);
            }
            else {
                player.sendMessage(new TranslationTextComponent("chat.scroll.fail"), player.getUniqueID());
            }
        });

        if (unlockedTitle.get()) {
            return ActionResult.resultConsume(itemStack);
        }
        else {
            return ActionResult.resultFail(itemStack);
        }
    }
}