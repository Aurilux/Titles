package aurilux.titles.common.command;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.command.argument.TitleArgument;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.command.EnumArgument;

public class CommandTitles {
    public enum CommandType {
        remove,
        add
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(TitlesMod.ID)
                .requires(s -> s.hasPermissionLevel(2))
                .then(Commands.argument("command", EnumArgument.enumArgument(CommandType.class))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("title", TitleArgument.title())
                                .executes(ctx -> run(ctx,
                                        EntityArgument.getPlayer(ctx, "player"),
                                        TitleArgument.getTitle(ctx, "title"))))))
        );
    }

    public static int run(CommandContext<CommandSource> context, ServerPlayerEntity player, Title title) throws CommandSyntaxException {
        CommandType commandType = context.getArgument("command", CommandType.class);
        if (commandType.equals(CommandType.add)) {
            TitlesAPI.instance().getCapability(player).ifPresent(t -> t.add(title));
            TranslationTextComponent tc = new TranslationTextComponent("Added title " + title.getLangKey() +
                    " to player " + player + ".");
            context.getSource().sendFeedback(tc, false);
        }
        else if (commandType.equals(CommandType.remove)) {
            TitlesAPI.instance().getCapability(player).ifPresent(t -> t.remove(title));
            TranslationTextComponent tc = new TranslationTextComponent("Removed title " + title.getLangKey() +
                    " from player " + player + ".");
            context.getSource().sendFeedback(tc, false);
        }
        return Command.SINGLE_SUCCESS;
    }
}