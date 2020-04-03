package aurilux.titles.common.item;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.TitlesImpl;
import aurilux.titles.client.gui.GuiTitleArchive;
import aurilux.titles.common.Titles;
import aurilux.titles.common.init.ModItems;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncFragmentCount;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.annotation.Nonnull;

public class ItemTitleArchive extends Item {
    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() && !world.isRemote) {
            boolean fragCountChanged = false;
            //add fragments from adjacent hotbar slots
            TitlesImpl.DefaultImpl titlesImpl = TitlesAPI.getTitlesCap(player);
            int hotbarSlotIndex = player.inventory.currentItem;
            if (hotbarSlotIndex < 9) {
                int adjacentSlotIndex = hotbarSlotIndex + 1;
                ItemStack adjacentStack = player.inventory.getStackInSlot(adjacentSlotIndex);
                if (adjacentStack.getItem() instanceof ItemArchiveFragment) {
                    titlesImpl.addFragments(adjacentStack.getCount());
                    player.inventory.setInventorySlotContents(adjacentSlotIndex, ItemStack.EMPTY);
                    fragCountChanged = true;
                }
                else if (adjacentStack == ItemStack.EMPTY) {
                    int currentFragCount = titlesImpl.getFragmentCount();
                    if (currentFragCount > 0) {
                        ItemStack newStack = new ItemStack(ModItems.archiveFragment);
                        if (currentFragCount >= 64) {
                            newStack.setCount(64);
                            titlesImpl.addFragments(-64);
                        }
                        else {
                            newStack.setCount(currentFragCount);
                            titlesImpl.addFragments(-currentFragCount);
                        }
                        player.inventory.setInventorySlotContents(adjacentSlotIndex, newStack);
                        fragCountChanged = true;
                    }
                }
                if (fragCountChanged) {
                    PacketDispatcher.INSTANCE.sendTo(new PacketSyncFragmentCount(titlesImpl.getFragmentCount()), (EntityPlayerMP) player);
                }
            }
        }
        else if (!player.isSneaking()) {
            FMLClientHandler.instance().displayGuiScreen(player, new GuiTitleArchive(player));
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}