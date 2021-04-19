package aurilux.titles.common.command;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.command.argument.TitleArgument;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncTitles;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.command.EnumArgument;

public class CommandTitles {
    public enum CommandType {
        remove,
        add
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(TitlesAPI.MOD_ID)
                .requires(s -> s.hasPermissionLevel(2))
                .then(Commands.argument("command", EnumArgument.enumArgument(CommandType.class))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("title", TitleArgument.title())
                                .executes(ctx -> run(ctx,
                                        EntityArgument.getPlayer(ctx, "player"),
                                        TitleArgument.getTitle(ctx, "title"))))))
        );
    }

    private static int run(CommandContext<CommandSource> context, ServerPlayerEntity player, Title title) {
        final CommandType commandType = context.getArgument("command", CommandType.class);
        final TextComponent[] response = {new StringTextComponent("Error completing command")};
        TitlesAPI.getCapability(player).ifPresent(t -> {
            if (commandType.equals(CommandType.add)) {
                t.add(title);
                response[0] = new TranslationTextComponent("commands.titles.add", title.getLangKey(), player.getName());
            }
            else if (commandType.equals(CommandType.remove)) {
                t.remove(title);
                response[0] = new TranslationTextComponent("commands.titles.remove", title.getLangKey(), player.getName());
            }
            PacketHandler.sendTo(new PacketSyncTitles(t.serializeNBT()), player);
        });
        context.getSource().sendFeedback(response[0], false);
        return Command.SINGLE_SUCCESS;
    }
}