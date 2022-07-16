package aurilux.titles.common.init;

import aurilux.titles.common.TitlesMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {
    public static class Blocks {
        public static final ITag.INamedTag<Block> OPULENT = tag("opulent");

        private static ITag.INamedTag<Block> tag(String name) {
            return BlockTags.makeWrapperTag(TitlesMod.prefix(name).toString());
        }

        private static ITag.INamedTag<Block> tagForge(String name) {
            return BlockTags.makeWrapperTag(new ResourceLocation("forge", name).toString());
        }
    }

    public static class Items {
        private static ITag.INamedTag<Item> tag(String name) {
            return ItemTags.makeWrapperTag(TitlesMod.prefix(name).toString());
        }

        private static ITag.INamedTag<Item> tagForge(String name) {
            return ItemTags.makeWrapperTag(new ResourceLocation("forge", name).toString());
        }
    }
}
