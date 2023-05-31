package aurilux.titles.common.item;

import aurilux.titles.api.Title;
import aurilux.titles.common.core.TitleManager;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ItemTitleScroll extends Item {
    public ItemTitleScroll(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide) {
            return super.use(world, player, hand);
        }

        ItemStack itemStack = player.getItemInHand(hand);
        AtomicBoolean unlockedTitle = new AtomicBoolean(false);
        TitleManager.doIfPresent(player, cap -> {
            List<Title> possibleLoot = TitleManager.getTitlesOfType(Title.AwardType.LOOT).values().stream()
                    .filter(t -> t.getRarity().equals(this.getRarity(itemStack)) && !cap.getObtainedTitles().contains(t)).toList();
            if (possibleLoot.size() > 0) {
                Title newTitle = possibleLoot.get(world.random.nextInt(possibleLoot.size()));
                TitleManager.unlockTitle((ServerPlayer) player, newTitle.getID());
                player.sendMessage(new TranslatableComponent("chat.scroll.success",
                        newTitle.getTextComponent(cap.getGenderSetting())), player.getUUID());
                itemStack.setCount(0);
                unlockedTitle.set(true);
            }
            else {
                player.sendMessage(new TranslatableComponent("chat.scroll.fail"), player.getUUID());
            }
        });

        if (unlockedTitle.get()) {
            return InteractionResultHolder.consume(itemStack);
        }
        else {
            return InteractionResultHolder.fail(itemStack);
        }
    }
}