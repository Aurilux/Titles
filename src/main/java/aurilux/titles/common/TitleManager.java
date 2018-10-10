package aurilux.titles.common;

import aurilux.titles.common.init.ContributorLoader;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public final class TitleManager {
    private static final String TITLES_NBT_KEY = "display_title";
    /** A temporary holding place for advancement ID's to be turned into titles **/
    private static final Set<String> advancementsWithTitles = new HashSet<>();
    /** Stores all in-game available titles by achievement ID */
    private static final Map<Advancement, TitleInfo> titlesByAdvancement = new HashMap<>();
    /** Stores the titles that have been unlocked by each player */
    private static final Map<UUID, Set<TitleInfo>> obtainedTitlesMap = new HashMap<>();
    /** Stores the titles currently selected/displayed by each player */
    private static final Map<UUID, TitleInfo> selectedTitleMap = new HashMap<>();

    static void registerTitle(String s) {
        advancementsWithTitles.add(s);
    }

    static void generateTitles() {
        for (Advancement advancement : getAllAdvancements()) {
            for (String id : advancementsWithTitles) {
                if (advancement.getId().toString().equals(id)) {
                    EnumRarity titleRarity;
                    FrameType frameType = advancement.getDisplay().getFrame();
                    if (frameType.equals(FrameType.CHALLENGE)) {
                        titleRarity = EnumRarity.RARE;
                    }
                    else if (frameType.equals(FrameType.GOAL)) {
                        titleRarity = EnumRarity.UNCOMMON;
                    }
                    else {
                        titleRarity = EnumRarity.COMMON;
                    }
                    String name = "title." + id.replaceAll("([/:])", ".");
                    titlesByAdvancement.put(advancement, new TitleInfo(name, titleRarity));
                }
            }
        }
    }

    public static void loadProfile(EntityPlayerMP player) {
        Set<TitleInfo> obtainedTitles = new HashSet<>();

        //Load all titles for each advancement that the player has completed
        PlayerAdvancements playerAdvancements = player.getAdvancements();
        for (Map.Entry<Advancement, TitleInfo> entry : titlesByAdvancement.entrySet()) {
            if (playerAdvancements.getProgress(entry.getKey()).isDone()) {
                obtainedTitles.add(entry.getValue());
            }
        }

        //Load the player's selected title, if it has one
        NBTTagCompound persistentPlayerTag = getPersistentPlayerTag(player);
        if (persistentPlayerTag.hasKey(TITLES_NBT_KEY)) {
            TitleInfo selectedTitle = TitleInfo.fromString(getTitleDataTag(player));
            if ((ContributorLoader.contributorTitleExists(player.getName()) && ContributorLoader.getContributorTitle(player.getName()).equals(selectedTitle))
                    || titlesByAdvancement.containsValue(selectedTitle)) {
                selectedTitleMap.put(player.getUniqueID(), selectedTitle);
            }
        }

        Titles.console("Loaded " + obtainedTitles.size() + " titles for " + player.getName());
        obtainedTitlesMap.put(player.getUniqueID(), obtainedTitles);
    }

    public static void unloadProfile(EntityPlayer player) {
        UUID playerUUID = player.getUniqueID();
        setTitleDataTag(player, getSelectedTitle(playerUUID));
        obtainedTitlesMap.remove(playerUUID);
        selectedTitleMap.remove(playerUUID);
    }

    public static TitleInfo getSelectedTitle(UUID playerUUID) {
        TitleInfo temp = selectedTitleMap.get(playerUUID);
        if (temp == null) {
            temp = TitleInfo.NULL_TITLE;
            selectedTitleMap.put(playerUUID, temp);
        }
        return temp;
    }

    public static void setSelectedTitle(UUID playerUUID, TitleInfo titleInfo) {
        selectedTitleMap.put(playerUUID, titleInfo);
    }

    public static Set<TitleInfo> getObtainedTitles(UUID playerUUID) {
        Set<TitleInfo> temp = obtainedTitlesMap.get(playerUUID);
        if (temp == null) {
            temp = new HashSet<>();
            obtainedTitlesMap.put(playerUUID, temp);
        }
        return temp;
        //return Collections.unmodifiableList(obtainedTitlesMap.get(playerUUID));
    }

    public static void setObtainedTitles(UUID playerUUID, Set<TitleInfo> obtainedTitles) {
        Set<TitleInfo> titles = getObtainedTitles(playerUUID);
        titles.clear();
        titles.addAll(obtainedTitles);
    }

    public static Map<UUID, TitleInfo> getAllSelectedTitles() {
        return selectedTitleMap;
    }

    public static void setAllSelectedTitles(Map<UUID, TitleInfo> allSelectedTitles) {
        selectedTitleMap.clear();
        selectedTitleMap.putAll(allSelectedTitles);
    }

    public static void addTitle(UUID playerUUID, TitleInfo titleInfo) {
        getObtainedTitles(playerUUID).add(titleInfo);
    }

    public static void unlockTitle(EntityPlayer player, Advancement advancement) {
        TitleInfo titleInfo = titlesByAdvancement.get(advancement);
        addTitle(player.getUniqueID(), titleInfo);

        PacketDispatcher.INSTANCE.sendToAll(new PacketSyncUnlockedTitle(player.getUniqueID(), titleInfo));

        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
                .sendMessage(new TextComponentTranslation("chat.type.title",
                        new Object[]{player.getDisplayName(), titleInfo.getFormattedTitle()}));
    }

    /// HELPERS
    public static boolean titleExists(Advancement advancement) {
        return titlesByAdvancement.containsKey(advancement);
    }

    private static Iterable<Advancement> getAllAdvancements() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getAdvancementManager().getAdvancements();
    }

    private static NBTTagCompound getPersistentPlayerTag(EntityPlayer player) {
        return player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
    }

    private static String getTitleDataTag(EntityPlayer player) {
        return getPersistentPlayerTag(player).getString(TITLES_NBT_KEY);
    }

    private static void setTitleDataTag(EntityPlayer player, TitleInfo titleInfo) {
        NBTTagCompound titleDataTag = getPersistentPlayerTag(player);
        titleDataTag.setString(TITLES_NBT_KEY, titleInfo.toString());
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, titleDataTag);
    }
    /// END HELPERS
}