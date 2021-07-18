package aurilux.titles.common.command.sub;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncTitles;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class CommandRefresh {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("refresh").executes(CommandRefresh::run);
    }

    private static int run(CommandContext<CommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().asPlayer();
            TitlesAPI.getCapability(player).ifPresent(c -> {
                PlayerAdvancements playerAdvancements = player.getAdvancements();
                context.getSource().getServer().getAdvancementManager().getAllAdvancements()
                        .forEach(advancement -> {
                            if (playerAdvancements.getProgress(advancement).isDone()) {
                                TitlesAPI.internal().unlockTitle(player, advancement.getId());
                            }
                        });
                context.getSource().sendFeedback(new StringTextComponent("Refreshed advancement titles!"), true);
                PacketHandler.sendTo(new PacketSyncTitles(c.serializeNBT()), player);
            });
        }
        catch (CommandSyntaxException ex) {
            TitlesMod.LOG.warn("Exception in titles command: refresh. {}", ex.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }
}