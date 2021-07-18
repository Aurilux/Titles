package aurilux.titles.common.item;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.impl.TitlesCapImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Optional;

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
        ITitles titles = TitlesAPI.getCapability(player).orElse(new TitlesCapImpl());
        ArrayList<Title> possibleLoot = new ArrayList<>(TitleManager.INSTANCE.getLootTitles().values());
        possibleLoot.removeAll(titles.getObtainedTitles());
        Optional<Title> optional = possibleLoot.stream()
                .filter(t -> t.getRarity().equals(this.getRarity(itemStack)))
                .findAny();
        if (optional.isPresent()) {
            TitlesAPI.internal().unlockTitle((ServerPlayerEntity) player, optional.get().getID());
            player.sendMessage(new TranslationTextComponent("chat.scroll.success", TitlesAPI.getFormattedTitle(optional.get(), titles.getGenderSetting())), player.getUniqueID());
            itemStack.setCount(0);
            return ActionResult.resultConsume(itemStack);
        }
        else {
            player.sendMessage(new TranslationTextComponent("chat.scroll.fail"), player.getUniqueID());
            return ActionResult.resultFail(itemStack);
        }
    }
}