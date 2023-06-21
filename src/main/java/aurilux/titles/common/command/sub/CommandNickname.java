package aurilux.titles.common.command.sub;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncNickname;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.regex.Pattern;

public class CommandNickname {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("nickname")
                .requires(s -> TitlesConfig.COMMON.nickname.get())
                .executes(ctx -> run(ctx, ""))
                .then(Commands.argument("nickname", StringArgumentType.word())
                        .executes(ctx -> run(ctx, StringArgumentType.getString(ctx, "nickname"))));
    }

    private static int run(CommandContext<CommandSourceStack> ctx, String nickname) {
        try {
            ServerPlayer player = ctx.getSource().getPlayerOrException();
            MutableComponent feedback = Component.translatable("commands.nickname.success", nickname);
            if (nickname.isEmpty()) {
                feedback = Component.translatable("commands.nickname.empty", nickname);
            }
            else if (!Pattern.matches("^[a-zA-Z0-9_]{3,16}$", nickname)) {
                ctx.getSource().sendSuccess(Component.translatable("commands.nickname.error.pattern", nickname).withStyle(ChatFormatting.RED), true);
                return 0;
            }

            TitleManager.doIfPresent(player, cap -> {
                TitleManager.setNickname(player, nickname);
                TitlesNetwork.toAll(new PacketSyncNickname(player.getUUID(), nickname));
            });

            ctx.getSource().sendSuccess(feedback, true);
        }
        catch (CommandSyntaxException ex) {
            TitlesMod.LOG.warn("Exception in titles command: nickname. {}", ex.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }
}