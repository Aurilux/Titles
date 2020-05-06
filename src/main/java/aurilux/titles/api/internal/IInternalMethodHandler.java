package aurilux.titles.api.internal;

import aurilux.titles.api.TitleInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

public interface IInternalMethodHandler {
    TitleInfo getTitleFromKey(String key);

    void syncUnlockedTitle(String key, EntityPlayer player);

    void sendChatMessageToAllPlayers(String message, ITextComponent playerName, TitleInfo titleInfo);

    String getFormattedTitle(TitleInfo titleInfo);

    String getFormattedTitle(TitleInfo titleInfo, boolean addComma);
}
