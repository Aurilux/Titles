package aurilux.titles.common.command;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.common.command.argument.TitleArgument;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.server.command.EnumArgument;

public class CommandTitles {
    public enum CommandType {
        remove,
        add
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("titles-commands")
                .requires(s -> s.hasPermissionLevel(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("command", EnumArgument.enumArgument(CommandType.class))
                                .then(Commands.argument("title", new TitleArgument()))
                                .executes(ctx -> run(ctx,
                                        EntityArgument.getPlayer(ctx, "player"),
                                        TitleArgument.getTitle(ctx, "title")))))
        );
    }

    public static int run(CommandContext<CommandSource> context, ServerPlayerEntity player, TitleInfo titleInfo) throws CommandSyntaxException {
        CommandType commandType = context.getArgument("command", CommandType.class);
        //TODO complete after testing
        if (commandType.equals(CommandType.add)) {
            context.getSource().sendFeedback(new StringTextComponent("adding title..."), false);
        }
        else if (commandType.equals(CommandType.remove)) {
            context.getSource().sendFeedback(new StringTextComponent("removing title..."), false);
        }
        return 0;
    }
}