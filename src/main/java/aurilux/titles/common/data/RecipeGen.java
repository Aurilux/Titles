package aurilux.titles.common.data;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider {
    public RecipeGen(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PAPER)
                .requires(ModItems.TITLE_FRAGMENT.get(), 3)
                .unlockedBy("has_item", has(ModItems.TITLE_FRAGMENT.get()))
                .save(consumer, TitlesMod.prefix("fragment_to_paper"));
    }
}