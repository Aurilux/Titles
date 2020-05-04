package aurilux.titles.api;

import aurilux.titles.api.capability.ITitles;
import aurilux.titles.api.internal.DummyMethodHandler;
import aurilux.titles.api.internal.IInternalMethodHandler;
import aurilux.titles.common.core.TitlesConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TitlesAPI {
    @CapabilityInject(ITitles.class)
    public static Capability<ITitles> TITLES_CAPABILITY;

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

    public static void addTitleToPlayer(PlayerEntity player, String key) {
        addTitleToPlayer(player, key, false);
    }

    public static void addTitleToPlayer(PlayerEntity player, String key, boolean announce) {
        TitleInfo titleInfo = titlesMap.get(key);
        if (titleInfo == null) {
            titleInfo = archiveTitles.get(key);
        }

        if (titleInfo == null || hasTitle(player, titleInfo)) {
            return;
        }

        getTitlesCap(player).add(titleInfo);
        //TODO refactor this
        //internalHandler.syncUnlockedTitle(key, player);

        if (announce) {
            //TODO uncomment this later
            //internalHandler.sendChatMessageToAllPlayers("chat.title.add", player.getDisplayName(), titleInfo);
        }
    }

    public static void removeTitleFromPlayer(PlayerEntity player, String key) {
        TitleInfo titleInfo = titlesMap.get(key);
        if (titleInfo != null) {
            getTitlesCap(player).remove(titleInfo);
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
            return TitleInfo.NULL_TITLE;//TODO internalHandler.getTitleFromKey(key);
        }
    }

    public static String getFormattedTitle(TitleInfo titleInfo) {
        return getFormattedTitle(titleInfo, false);
    }

    public static String getFormattedTitle(TitleInfo titleInfo, boolean addComma) {
        if (titleInfo.equals(TitleInfo.NULL_TITLE)) {
            return "";
        }

        TextFormatting titleColor;
        switch (titleInfo.getTitleRarity()) {
            case UNIQUE: titleColor = TitlesConfig.CLIENT.uniqueColor.get().textFormatting; break;
            case RARE: titleColor = TitlesConfig.CLIENT.rareColor.get().textFormatting; break;
            case UNCOMMON: titleColor = TitlesConfig.CLIENT.uncommonColor.get().textFormatting; break;
            default: titleColor = TitlesConfig.CLIENT.commonColor.get().textFormatting; break; //COMMON
        }

        return (addComma ? ", " : "") + titleColor + new TranslationTextComponent(titleInfo.getLangKey()).getFormattedText();
    }

    public static TitleInfo getPlayerSelectedTitle(PlayerEntity player) {
        return getTitlesCap(player).getSelectedTitle();
    }

    public static void setPlayerSelectedTitle(PlayerEntity player, TitleInfo titleInfo) {
        getTitlesCap(player).setSelectedTitle(titleInfo);
    }

    public static boolean hasTitle(PlayerEntity player, TitleInfo titleInfo) {
        return getTitlesCap(player).getObtainedTitles().contains(titleInfo);
    }

    public static ITitles getTitlesCap(PlayerEntity player) {
        return player.getCapability(TITLES_CAPABILITY).orElseThrow(NullPointerException::new);
    }

    public static Map<String, TitleInfo> getRegisteredTitles() {
        return Collections.unmodifiableMap(titlesMap);
    }
}