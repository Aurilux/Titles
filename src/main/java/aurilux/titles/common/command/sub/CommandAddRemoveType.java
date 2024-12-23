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

    private enum CommandScope {
        all,
        only,
        except,
    }

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.argument("command", EnumArgument.enumArgument(CommandType.class))
                .requires(s -> s.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("scope", EnumArgument.enumArgument(CommandScope.class)))
                        .executes(ctx -> run(ctx,
                                ctx.getArgument("command", CommandType.class),
                                EntityArgument.getPlayer(ctx, "player"),
                                ctx.getArgument("scope", CommandScope.class)))
                                .then(Commands.argument("awards", EnumArgument.enumArgument(Title.AwardType.class))
                                        .executes(ctx -> run(ctx,
                                                ctx.getArgument("command", CommandType.class),
                                                EntityArgument.getPlayer(ctx, "player"),
                                                ctx.getArgument("scope", CommandScope.class),
                                                ctx.getArgument("awards", Title.AwardType.class)))));
    }

    private static int run(CommandContext<CommandSourceStack> ctx, CommandType command, ServerPlayer player, CommandScope scope, Title.AwardType... awards) {
        if (Arrays.asList(awards).contains(Title.AwardType.STARTING) || Arrays.asList(awards).contains(Title.AwardType.CONTRIBUTOR)) {
            ctx.getSource().sendFailure(Component.translatable("commands.titles.addremovetype.invalid_awards", scope.name(), player.getName()));
            return Command.SINGLE_SUCCESS;
        }

        // This is an array as a workaround of variables needing to be final when accessed inside a lambda.
        final MutableComponent[] response = {Component.translatable("commands.titles.addremovetype.fail")};
        TitleManager.doIfPresent(player, cap -> {
            if (command == CommandType.addtype) {
                List<Title> titlesToAdd = new ArrayList<>();
                if (scope == CommandScope.all) {
                    titlesToAdd.addAll(TitleManager.getAllObtainableTitles().values());
                }
                else if(scope == CommandScope.only) {
                    titlesToAdd.addAll(TitleManager.getTitlesOfType(awards).values());
                }
                else { //scope == CommandScope.except
                    titlesToAdd.addAll(TitleManager.getAllTitlesExcept(awards).values());
                }
                titlesToAdd.forEach(title -> {
                    TitlesMod.LOG.debug("Adding title {}", title);
                    cap.add(TitleManager.getTitle(title.getID()));
                });
                response[0] = Component.translatable("commands.titles.addtype", scope.name(), player.getName());
            }
            else { //command == CommandType.removetype
                List<Title> titlesToRemove = new ArrayList<>();
                if (scope == CommandScope.all) {
                    titlesToRemove.addAll(TitleManager.getAllObtainableTitles().values());
                }
                else if(scope == CommandScope.only) {
                    titlesToRemove.addAll(TitleManager.getTitlesOfType(awards).values());
                }
                else { //scope == CommandScope.except
                    titlesToRemove.addAll(TitleManager.getAllTitlesExcept(awards).values());
                }
                titlesToRemove.forEach(title -> {
                    TitlesMod.LOG.debug("Removing title {}", title);
                    cap.remove(TitleManager.getTitle(title.getID()));
                });
                response[0] = Component.translatable("commands.titles.removetype", scope.name(), player.getName());
            }
            TitlesNetwork.toPlayer(new PacketSyncTitlesCapability(cap.serializeNBT()), player);
        });
        ctx.getSource().sendSuccess(() -> response[0], true);
        return Command.SINGLE_SUCCESS;
    }
}
