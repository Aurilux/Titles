package aurilux.titles.common.command;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.command.sub.CommandAddRemove;
import aurilux.titles.common.command.sub.CommandDisplay;
import aurilux.titles.common.command.sub.CommandRefresh;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CommandTitles {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(TitlesMod.MOD_ID)
                .then(CommandAddRemove.register())
                .then(CommandRefresh.register())
                .then(CommandDisplay.register())
        );
    }
}