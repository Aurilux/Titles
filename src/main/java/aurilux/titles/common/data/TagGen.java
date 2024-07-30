package aurilux.titles.common.data;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TagGen {
    public static class BlockTags extends BlockTagsProvider {
        public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                         @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, TitlesMod.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(ModTags.Blocks.OPULENT)
                    .add(Blocks.DIAMOND_BLOCK)
                    .add(Blocks.EMERALD_BLOCK)
                    .add(Blocks.NETHERITE_BLOCK);
        }
    }
    public static class ItemTags extends ItemTagsProvider {
        public ItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider,
                        CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
            super(packOutput, lookupProvider, blockTags, TitlesMod.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {

        }
    }
}