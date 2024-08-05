package aurilux.titles.common.command.sub;

import aurilux.titles.api.Title;
import aurilux.titles.common.command.argument.TitleArgument;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncTitlesCapability;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class CommandAddRemove {
    public enum CommandType {
        remove,
        add
    }

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.argument("command", EnumArgument.enumArgument(CommandType.class))
            .requires(s -> s.hasPermission(2))
                    .then(Commands.argument("player", EntityArgument.player())
                            .then(Commands.argument("title", TitleArgument.any())
                                    .executes(ctx -> run(ctx,
                                            EntityArgument.getPlayer(ctx, "player"),
                                            TitleArgument.getTitle(ctx, "title")))));
    }

    private static int run(CommandContext<CommandSourceStack> context, ServerPlayer player, Title title) {
        final CommandType commandType = context.getArgument("command", CommandType.class);
        // This is an array as a workaround of variables needing to be final when accessed inside a lambda.
        final MutableComponent[] response = {Component.translatable("commands.titles.addremove.fail")};
        TitleManager.doIfPresent(player, cap -> {
            Component formattedTitle = title.getTextComponent(cap.getGenderSetting());
            if (title.getType().equals(Title.AwardType.CONTRIBUTOR)) {
                return;
            }

            if (commandType.equals(CommandType.add)) {
                cap.add(title);
                response[0] = Component.translatable("commands.titles.add", formattedTitle, player.getName());
            }
            else if (commandType.equals(CommandType.remove)) {
                cap.remove(title);
                response[0] = Component.translatable("commands.titles.remove", formattedTitle, player.getName());
            }
            TitlesNetwork.toPlayer(new PacketSyncTitlesCapability(cap.serializeNBT()), player);
        });
        context.getSource().sendSuccess(() -> response[0], false);
        return Command.SINGLE_SUCCESS;
    }
}
