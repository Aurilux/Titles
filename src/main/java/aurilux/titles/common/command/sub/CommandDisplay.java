package aurilux.titles.common.command.sub;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.command.argument.TitleArgument;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncDisplayTitle;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandDisplay {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("display")
                .then(Commands.argument("title", TitleArgument.title())
                        .executes(ctx -> run(ctx, TitleArgument.getTitle(ctx, "title"))));

    }

    private static int run(CommandContext<CommandSource> context, Title title) {
        try {
            ServerPlayerEntity player = context.getSource().asPlayer();
            TitlesAPI.getCapability(player).ifPresent(c -> {
                TranslationTextComponent feedback = new TranslationTextComponent("commands.display.success",
                        TitlesAPI.getFormattedTitle(title, player));
                if (c.hasTitle(title)) {
                    TitlesAPI.setDisplayTitle(player, title.getID());
                    PacketHandler.toAll(new PacketSyncDisplayTitle(player.getUniqueID(), title.getID()));
                }
                else {
                    feedback = new TranslationTextComponent("commands.display.error",
                            TitlesAPI.getFormattedTitle(title, c.getGenderSetting()));

                }
                context.getSource().sendFeedback(feedback, true);
            });
        }
        catch (CommandSyntaxException ex) {
            TitlesMod.LOG.warn("Exception in titles command: display. {}", ex.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }
}
