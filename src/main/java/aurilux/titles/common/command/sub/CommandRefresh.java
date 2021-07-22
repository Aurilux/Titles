package aurilux.titles.common.command.sub;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncTitles;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class CommandRefresh {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("refresh").executes(CommandRefresh::run);
    }

    private static int run(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().asPlayer();
            TitlesAPI.getCapability(player).ifPresent(c -> {
                c.getObtainedTitles().removeIf(t -> t.getType().equals(Title.AwardType.ADVANCEMENT));
                PlayerAdvancements playerAdvancements = player.getAdvancements();
                TitlesMod.LOG.debug("Titles capability found for player {}. Iterating through their advancements", player.getName().getString());
                context.getSource().getServer().getAdvancementManager().getAllAdvancements().stream()
                        .filter(advancement -> playerAdvancements.getProgress(advancement).isDone())
                        .peek(a -> TitlesMod.LOG.debug("Refreshing title for advancement {}", a.getId()))
                        .forEach(advancement -> c.add(TitlesAPI.getTitle(advancement.getId())));
                context.getSource().sendFeedback(new StringTextComponent("Finished refreshing advancement titles!"), true);
                PacketHandler.sendTo(new PacketSyncTitles(c.serializeNBT()), player);
            });
        }
        catch (CommandSyntaxException ex) {
            TitlesMod.LOG.warn("Exception in titles command: refresh. {}", ex.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }
}