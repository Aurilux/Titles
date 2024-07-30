package aurilux.titles.common.data;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.init.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementGen extends ForgeAdvancementProvider {
    public AdvancementGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(AdvancementGen::titlesAdvancements));
    }

    private static DisplayInfo simpleDisplay(ItemLike icon, String name, FrameType frameType) {
        String domain = "advancement.titles.";
        return new DisplayInfo(new ItemStack(icon.asItem()),
                Component.translatable(domain + name),
                Component.translatable(domain + name + ".desc"),
                null, frameType, true, true, false);
    }

    private static void titlesAdvancements(HolderLookup.Provider lookup, Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
        Advancement root = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.TITLE_SCROLL_COMMON.get()),
                        Component.translatable("itemGroup.titles"),
                        Component.translatable("advancement.titles.root.desc"),
                        new ResourceLocation("minecraft:textures/gui/advancements/backgrounds/stone.png"), FrameType.TASK, false, false, false))
                .addCriterion("crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(Items.CRAFTING_TABLE).build()
                ))
                .save(consumer, TitlesMod.prefix("root").toString());

        Advancement.Builder.advancement()
                .display(simpleDisplay(Items.OAK_BOAT, "captain", FrameType.TASK))
                .parent(root)
                .addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
                .save(consumer, TitlesMod.prefix("captain").toString());

        Advancement.Builder.advancement()
                .display(simpleDisplay(Items.EGG, "chicken_chaser", FrameType.TASK))
                .parent(root)
                .addCriterion("chicken_kill", KilledTrigger.TriggerInstance
                        .playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.CHICKEN)))
                .save(consumer, TitlesMod.prefix("chicken_chaser").toString());

        Advancement.Builder.advancement()
                .display(simpleDisplay(Items.POWDER_SNOW_BUCKET, "frigid", FrameType.TASK))
                .parent(root)
                .addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
                .save(consumer, TitlesMod.prefix("frigid").toString());

        Advancement.Builder.advancement()
                .display(simpleDisplay(Items.CARVED_PUMPKIN, "melon_lord", FrameType.TASK))
                .parent(root)
                .addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
                .save(consumer, TitlesMod.prefix("melon_lord").toString());

        Advancement.Builder.advancement()
                .display(simpleDisplay(Items.DIAMOND_BLOCK, "opulent", FrameType.CHALLENGE))
                .parent(root)
                .addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
                .save(consumer, TitlesMod.prefix("opulent").toString());

        Advancement.Builder.advancement()
                .display(simpleDisplay(Items.ARROW, "pincushion", FrameType.GOAL))
                .parent(root)
                .addCriterion("code_triggered", new ImpossibleTrigger.TriggerInstance())
                .save(consumer, TitlesMod.prefix("pincushion").toString());

        Advancement.Builder.advancement()
                .display(simpleDisplay(Items.DEEPSLATE, "spelunker", FrameType.GOAL))
                .parent(root)
                .addCriterion("spelunking", PlayerTrigger.TriggerInstance.located(
                        LocationPredicate.Builder.location().setDimension(Level.OVERWORLD).setY(MinMaxBounds.Doubles.atMost(0)).build()))
                .save(consumer, TitlesMod.prefix("spelunker").toString());
    }
}