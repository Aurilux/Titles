package aurilux.titles.common.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandTitles extends CommandTreeBase {
    public CommandTitles() {
        addSubcommand(new CommandAdd());
        addSubcommand(new CommandRemove());
        addSubcommand(new CommandRefresh());
    }

    @Override
    public String getName() {
        return "titles";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.titles.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}