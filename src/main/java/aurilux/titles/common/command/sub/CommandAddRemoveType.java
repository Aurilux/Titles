package aurilux.titles.common.command.sub;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
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

import java.util.*;

public class CommandAddRemoveType {
    private enum CommandType {
        addtype,
        removetype
    }

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.argument("commandtype", EnumArgument.enumArgument(CommandType.class))
                .requires(s -> s.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("awards", EnumArgument.enumArgument(Title.AwardType.class))
                        .suggests((ctx, builder) -> {
                            for (Title.AwardType type : Title.AwardType.values()) {
                                if (type != Title.AwardType.STARTING && type != Title.AwardType.CONTRIBUTOR) {
                                    builder.suggest(type.name());
                                }
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx -> run(ctx,
                                ctx.getArgument("commandtype", CommandType.class),
                                EntityArgument.getPlayer(ctx, "player"),
                                ctx.getArgument("awards", Title.AwardType.class)))));
    }

    private static int run(CommandContext<CommandSourceStack> ctx, CommandType command, ServerPlayer player, Title.AwardType award) {
        if (award.equals(Title.AwardType.STARTING) || award.equals(Title.AwardType.CONTRIBUTOR)) {
            ctx.getSource().sendFailure(Component.translatable("commands.titles.addremovetype.invalid_awards", player.getName()));
            return Command.SINGLE_SUCCESS;
        }

        // This is an array as a workaround of variables needing to be final when accessed inside a lambda.
        final MutableComponent[] response = {Component.translatable("commands.titles.addremovetype.fail")};
        TitleManager.doIfPresent(player, cap -> {
            TitleManager.getTitlesOfType(award).values().forEach(title -> {
                if (command == CommandType.addtype) {
                    TitlesMod.LOG.debug("Adding title type {}", award);
                    cap.add(TitleManager.getTitle(title.getID()));
                    response[0] = Component.translatable("commands.titles.addtype", award.toString(), player.getName());
                }
                else if (command == CommandType.removetype) {
                    TitlesMod.LOG.debug("Removing title type {}", award);
                    cap.remove(TitleManager.getTitle(title.getID()));
                    response[0] = Component.translatable("commands.titles.removetype", award.toString(), player.getName());
                }
            });
            TitlesNetwork.toPlayer(new PacketSyncTitlesCapability(cap.serializeNBT()), player);
        });
        ctx.getSource().sendSuccess(() -> response[0], true);
        return Command.SINGLE_SUCCESS;
    }
}
