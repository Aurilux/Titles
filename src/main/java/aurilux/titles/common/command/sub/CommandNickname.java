package aurilux.titles.common.command.sub;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.command.argument.TitleArgument;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.core.TitlesConfig;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncDisplayTitle;
import aurilux.titles.common.network.messages.PacketSyncNickname;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.util.internal.StringUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.loading.StringUtils;

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
            TranslatableComponent feedback = new TranslatableComponent("commands.nickname.success", nickname);
            if (nickname.isEmpty()) {
                feedback = new TranslatableComponent("commands.nickname.empty", nickname);
            }
            else if (!Pattern.matches("^[a-zA-Z0-9_]{3,16}$", nickname)) {
                ctx.getSource().sendSuccess(new TranslatableComponent("commands.nickname.error.pattern", nickname).withStyle(ChatFormatting.RED), true);
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