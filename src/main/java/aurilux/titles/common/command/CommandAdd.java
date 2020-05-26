package aurilux.titles.common.command;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandAdd extends CommandBase {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/titles <add|remove> <player> <titleKey>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException(getUsage(sender));
        }

        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        String titleKey = args[1];
        doCommand(player, titleKey);
    }

    public void doCommand(EntityPlayerMP player, String titleKey) {
        TitlesAPI.addTitleToPlayer(player, titleKey, true);
        TitlesAPI.internalHandler.syncUnlockedTitle(titleKey, player);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, TitlesAPI.getRegisteredTitles().keySet());
        }
        else {
            return Collections.emptyList();
        }
    }
}