package aurilux.titles.common.init;

import aurilux.titles.common.TitlesMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> OPULENT = tag("opulent");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(TitlesMod.prefix(name));
        }
    }

    public static class Items {
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(TitlesMod.prefix(name));
        }
    }
}
