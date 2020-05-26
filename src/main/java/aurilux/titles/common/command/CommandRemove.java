package aurilux.titles.common.command;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncRemovedTitle;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandRemove extends CommandAdd {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void doCommand(EntityPlayerMP player, String titleKey) {
        TitlesAPI.removeTitleFromPlayer(player, titleKey);
        TitlesAPI.internalHandler.syncRemovedTitle(titleKey, player);
        player.refreshDisplayName();
    }
}