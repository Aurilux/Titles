package aurilux.titles.common.command.sub;

import aurilux.titles.api.Title;
import aurilux.titles.common.command.argument.TitleArgument;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncTitlesCapability;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.server.command.EnumArgument;

public class CommandAddRemove {
    public enum CommandType {
        remove,
        add
    }

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.argument("command", EnumArgument.enumArgument(CommandType.class))
            .requires(s -> s.hasPermissionLevel(2))
                    .then(Commands.argument("player", EntityArgument.player())
                            .then(Commands.argument("title", TitleArgument.title())
                                    .executes(ctx -> run(ctx,
                                            EntityArgument.getPlayer(ctx, "player"),
                                            TitleArgument.getTitle(ctx, "title")))));
    }

    private static int run(CommandContext<CommandSource> context, ServerPlayerEntity player, Title title) {
        final CommandType commandType = context.getArgument("command", CommandType.class);
        // This is an array as a workaround of variables needing to be final when accessed inside a lambda.
        final TextComponent[] response = {new StringTextComponent("Error completing command")};
        TitleManager.doIfPresent(player, cap -> {
            ITextComponent formattedTitle = TitleManager.getFormattedTitle(title, cap.getGenderSetting());
            if (commandType.equals(CommandType.add)) {
                cap.add(title);
                response[0] = new TranslationTextComponent("commands.titles.add", formattedTitle, player.getName());
            }
            else if (commandType.equals(CommandType.remove)) {
                cap.remove(title);
                response[0] = new TranslationTextComponent("commands.titles.remove", formattedTitle, player.getName());
            }
            TitlesNetwork.toPlayer(new PacketSyncTitlesCapability(cap.serializeNBT()), player);
        });
        context.getSource().sendFeedback(response[0], false);
        return Command.SINGLE_SUCCESS;
    }
}
