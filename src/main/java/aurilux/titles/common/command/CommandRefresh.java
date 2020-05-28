package aurilux.titles.common.command;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.TitlesImpl;
import aurilux.titles.common.Titles;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncTitleData;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public class CommandRefresh extends CommandBase {
    @Override
    public String getName() {
        return "refresh";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/titles refresh";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            throw new WrongUsageException(getUsage(sender));
        }
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        TitlesImpl.ITitles cap = TitlesAPI.getTitlesCap(player);
        cap.getObtainedTitles().clear();
        cap.setDisplayTitle(TitleInfo.NULL_TITLE);
        player.refreshDisplayName();
        PlayerAdvancements playerAdvancements = player.getAdvancements();
        StreamSupport.stream(server.getAdvancementManager().getAdvancements().spliterator(), false)
                .forEach(advancement -> {
                    if (playerAdvancements.getProgress(advancement).isDone()) {
                        TitlesAPI.addTitleToPlayer(player, advancement.getId().toString());
                    }
                });
        sender.sendMessage(new TextComponentString("Refreshing obtained titles..."));
        PacketDispatcher.INSTANCE.sendTo(new PacketSyncTitleData(player), player);
    }
}