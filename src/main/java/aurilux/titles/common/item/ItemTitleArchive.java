package aurilux.titles.common.item;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.common.Titles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemTitleArchive extends ItemAux {
    private static List<TitleInfo> archiveTitles = new ArrayList<>();

    public ItemTitleArchive() {
        super("titleArchive");
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    public static void addArchiveTitle(String key, TitleInfo.TitleRarity titleRarity) {
        archiveTitles.add(new TitleInfo(Titles.MOD_ID, key, titleRarity));
    }

    public static List<TitleInfo> getArchiveTitles() {
        return archiveTitles;
    }
}