package aurilux.titles.api;

import aurilux.titles.api.capability.TitlesImpl;
import aurilux.titles.api.internal.DummyMethodHandler;
import aurilux.titles.api.internal.IInternalMethodHandler;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncDisplayTitle;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TitlesAPI {
    private static final Map<String, TitleInfo> titlesMap = new HashMap<>();
    private static final Map<String, TitleInfo> archiveTitles = new HashMap<>();

    public static IInternalMethodHandler internalHandler = new DummyMethodHandler();

    public static void registerTitle(String key) {
        registerTitle(key, TitleInfo.TitleRarity.COMMON);
    }

    public static void registerTitle(String key, TitleInfo.TitleRarity titleRarity) {
        if (!titleRarity.equals(TitleInfo.TitleRarity.UNIQUE)) {
            titlesMap.put(key, new TitleInfo(key, titleRarity));
        }
    }

    public static void addArchiveTitle(String key, TitleInfo.TitleRarity titleRarity) {
        archiveTitles.put(key, new TitleInfo(key, titleRarity));
    }

    public static Map<String, TitleInfo> getArchiveTitles() {
        return archiveTitles;
    }

    public static void addTitleToPlayer(EntityPlayer player, String key) {
        addTitleToPlayer(player, key, false);
    }

    public static void addTitleToPlayer(EntityPlayer player, String key, boolean announce) {
        TitleInfo titleInfo = titlesMap.get(key);
        if (titleInfo == null) {
            titleInfo = archiveTitles.get(key);
        }

        if (titleInfo == null || hasTitle(player, titleInfo)) {
            return;
        }

        getTitlesCap(player).add(titleInfo);

        if (announce) {
            internalHandler.sendChatMessageToAllPlayers("chat.title.add", player.getDisplayName(), titleInfo);
        }
    }

    public static void removeTitleFromPlayer(EntityPlayer player, String key) {
        TitleInfo titleInfo = titlesMap.get(key);
        if (titleInfo != null && hasTitle(player, titleInfo)) {
            TitlesImpl.ITitles cap = getTitlesCap(player);
            cap.remove(titleInfo);
            if (cap.getDisplayTitle().equals(titleInfo)) {
                cap.setDisplayTitle(TitleInfo.NULL_TITLE);
            }
        }
    }

    public static TitleInfo getTitleFromKey(String key) {
        if (key.equals(TitleInfo.NULL_TITLE.getKey())){
            return TitleInfo.NULL_TITLE;
        }
        else if (getRegisteredTitles().containsKey(key)) {
            return getRegisteredTitles().get(key);
        }
        else if (getArchiveTitles().containsKey(key)) {
            return getArchiveTitles().get(key);
        }
        else {
            return internalHandler.getTitleFromKey(key);
        }
    }

    public static TitleInfo getPlayerDisplayTitle(EntityPlayer player) {
        return getTitlesCap(player).getDisplayTitle();
    }

    public static void setPlayerDisplayTitle(EntityPlayer player, TitleInfo titleInfo) {
        getTitlesCap(player).setDisplayTitle(titleInfo);
    }

    public static boolean hasTitle(EntityPlayer player, TitleInfo titleInfo) {
        return getTitlesCap(player).getObtainedTitles().contains(titleInfo);
    }

    public static TitlesImpl.DefaultImpl getTitlesCap(EntityPlayer player) {
        return TitlesImpl.getCapability(player);
    }

    public static Map<String, TitleInfo> getRegisteredTitles() {
        return Collections.unmodifiableMap(titlesMap);
    }
}