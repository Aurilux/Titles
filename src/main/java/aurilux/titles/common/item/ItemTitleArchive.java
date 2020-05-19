package aurilux.titles.common.item;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.client.gui.GuiTitleArchive;
import aurilux.titles.common.Titles;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncFragmentCount;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemTitleArchive extends Item {
    public ItemTitleArchive() {
        super(new Item.Properties()
            .maxStackSize(1)
            .group(Titles.itemGroup));
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
        ItemStack stack = player.getHeldItem(handIn);
        TitlesAPI.instance().getCapability(player).ifPresent(cap -> {
            if (player.isCrouching() && !world.isRemote) {
                boolean fragCountChanged = false;
                //add fragments from adjacent hotbar slots
                int hotbarSlotIndex = player.inventory.currentItem;
                if (hotbarSlotIndex < 9) {
                    int adjacentSlotIndex = hotbarSlotIndex + 1;
                    ItemStack adjacentStack = player.inventory.getStackInSlot(adjacentSlotIndex);
                    if (adjacentStack.getItem() instanceof ItemArchiveFragment) {
                        cap.addFragments(adjacentStack.getCount());
                        player.inventory.setInventorySlotContents(adjacentSlotIndex, ItemStack.EMPTY);
                        fragCountChanged = true;
                    }
                    else if (adjacentStack == ItemStack.EMPTY) {
                        int currentFragCount = cap.getFragmentCount();
                        if (currentFragCount > 0) {
                            ItemStack newStack = new ItemStack(ModItems.archiveFragment);
                            if (currentFragCount >= 64) {
                                newStack.setCount(64);
                                cap.addFragments(-64);
                            }
                            else {
                                newStack.setCount(currentFragCount);
                                cap.addFragments(-currentFragCount);
                            }
                            player.inventory.setInventorySlotContents(adjacentSlotIndex, newStack);
                            fragCountChanged = true;
                        }
                    }
                    if (fragCountChanged) {
                        PacketHandler.sendTo(new PacketSyncFragmentCount(cap.getFragmentCount()), (ServerPlayerEntity) player);
                    }
                }
            }
            else if (!player.isCrouching()) {
                Minecraft.getInstance().displayGuiScreen(new GuiTitleArchive(player, cap));
            }
        });
        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
}