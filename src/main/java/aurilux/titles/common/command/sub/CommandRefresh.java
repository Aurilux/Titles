package aurilux.titles.common.command.sub;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncTitlesCapability;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class CommandRefresh {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("refresh").executes(CommandRefresh::run);
    }

    private static int run(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().asPlayer();
            TitleManager.doIfPresent(player, cap -> {
                TitleManager.setDisplayTitle(player, Title.NULL_TITLE.getID());
                TitlesMod.LOG.debug("Player's total obtained titles: {}", cap.getObtainedTitles().size());
                cap.getObtainedTitles().removeIf(t -> t.getType().equals(Title.AwardType.ADVANCEMENT));
                TitlesMod.LOG.debug("Player's total obtained titles after removing all advancement titles: {}", cap.getObtainedTitles().size());
                PlayerAdvancements playerAdvancements = player.getAdvancements();
                TitlesMod.LOG.debug("Titles capability found for player {}. Iterating through their advancements", player.getName().getString());
                Collection<Advancement> allAdvancements = context.getSource().getServer().getAdvancementManager().getAllAdvancements();
                allAdvancements = allAdvancements.stream()
                        .filter(advancement -> playerAdvancements.getProgress(advancement).isDone()
                                && !TitleManager.getTitle(advancement.getId()).isNull())
                        .collect(Collectors.toCollection(ArrayList::new));
                TitlesMod.LOG.debug("After filtering, how many advancement-earned titles are there?: {}", allAdvancements.size());
                allAdvancements.forEach(advancement -> {
                            TitlesMod.LOG.debug("Re-awarding title for advancement {}", advancement.getId());
                            cap.add(TitleManager.getTitle(advancement.getId()));
                        });
                context.getSource().sendFeedback(new StringTextComponent("Finished refreshing advancement titles!"), true);
                TitlesMod.LOG.debug("Player's total obtained titles after re-awarding advancement titles: {}", cap.getObtainedTitles().size());
                TitlesNetwork.toPlayer(new PacketSyncTitlesCapability(cap.serializeNBT()), player);
            });
        }
        catch (CommandSyntaxException ex) {
            TitlesMod.LOG.warn("Exception in titles command: refresh. {}", ex.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }
}