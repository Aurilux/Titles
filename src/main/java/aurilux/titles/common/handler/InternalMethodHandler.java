package aurilux.titles.common.handler;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.internal.IInternalMethodHandler;
import aurilux.titles.common.Titles;
import aurilux.titles.common.init.ContributorLoader;
import aurilux.titles.common.init.ModConfig;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncRemovedTitle;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class InternalMethodHandler implements IInternalMethodHandler {
    @Override
    public TitleInfo getTitleFromKey(String key) {
        return ContributorLoader.getTitleFromKey(key);
    }

    @Override
    public void syncUnlockedTitle(String key, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            PacketDispatcher.INSTANCE.sendTo(new PacketSyncUnlockedTitle(key), (EntityPlayerMP) player);
        }
    }

    @Override
    public void syncRemovedTitle(String key, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            PacketDispatcher.INSTANCE.sendTo(new PacketSyncRemovedTitle(key), (EntityPlayerMP) player);
        }
    }

    @Override
    public void sendChatMessageToAllPlayers(String message, ITextComponent playerName, TitleInfo info) {
        Titles.proxy.sendChatMessageToAllPlayers(message, playerName, getFormattedTitle(info));
    }

    public String getFormattedTitle(TitleInfo titleInfo) {
        return getFormattedTitle(titleInfo, false);
    }

    public String getFormattedTitle(TitleInfo titleInfo, boolean addComma) {
        if (titleInfo.equals(TitleInfo.NULL_TITLE)) {
            return "";
        }

        TextFormatting titleColor;
        switch (titleInfo.getTitleRarity()) {
            case UNIQUE: titleColor = TextFormatting.LIGHT_PURPLE; break;
            case RARE: titleColor = TextFormatting.AQUA; break;
            case UNCOMMON: titleColor = TextFormatting.YELLOW; break;
            default: titleColor = TextFormatting.WHITE; break; //COMMON
        }

        return (addComma ? ", " : "") + titleColor + new TextComponentTranslation(titleInfo.getLangKey()).getFormattedText();
    }
}