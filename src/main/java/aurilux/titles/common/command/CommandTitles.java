package aurilux.titles.common.command;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.command.sub.*;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandTitles {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(TitlesMod.MOD_ID)
                        .then(CommandAddRemove.register())
                        .then(CommandRefresh.register())
                        .then(CommandDisplay.register())
                        .then(CommandNickname.register())
                        .then(CommandAddRemoveType.register())
        );
    }
}