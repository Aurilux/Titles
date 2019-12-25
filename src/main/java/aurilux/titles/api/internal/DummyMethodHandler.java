package aurilux.titles.api.internal;

import aurilux.titles.api.TitleInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

public class DummyMethodHandler implements IInternalMethodHandler {
    @Override
    public void syncUnlockedTitle(String key, EntityPlayer player) {}

    @Override
    public void sendChatMessageToAllPlayers(String message, ITextComponent playerName, TitleInfo titleInfo) {}

    @Override
    public String getFormattedTitle(TitleInfo titleInfo) {
        return null;
    }

    @Override
    public String getFormattedTitle(TitleInfo titleInfo, boolean addComma) {
        return null;
    }
}
