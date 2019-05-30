package aurilux.titles.common.command;

import aurilux.titles.common.Titles;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandTitles extends CommandBase {
    @Override
    public String getName() {
        return "titles";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "titles <command> <arg>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender.getEntityWorld().isRemote) {
            return;
        }

        String commandName = args[0];
        switch (commandName.toLowerCase()) {
            case "refresh": break;
        }
    }
}
