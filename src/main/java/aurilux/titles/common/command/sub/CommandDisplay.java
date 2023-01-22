package aurilux.titles.common.command.sub;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.command.argument.TitleArgument;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncDisplayTitle;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public class CommandDisplay {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("display")
                .then(Commands.argument("title", TitleArgument.display())
                        .executes(ctx -> run(ctx, TitleArgument.getTitle(ctx, "title"))));

    }

    private static int run(CommandContext<CommandSourceStack> ctx, Title title) {
        try {
            ServerPlayer player = ctx.getSource().getPlayerOrException();
            TitleManager.doIfPresent(player, cap -> {
                TitleManager.setDisplayTitle(player, title.getID());
                TitlesNetwork.toAll(new PacketSyncDisplayTitle(player.getUUID(), title.getID()));

                MutableComponent feedback = Component.translatable("commands.display.success",
                        TitleManager.getFormattedTitle(title, player));
                ctx.getSource().sendSuccess(feedback, true);
            });
        }
        catch (CommandSyntaxException ex) {
            TitlesMod.LOG.warn("Exception in titles command: display. {}", ex.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }
}
