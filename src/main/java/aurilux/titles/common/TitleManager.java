package aurilux.titles.common;

import aurilux.titles.common.capability.TitlesImpl;
import aurilux.titles.common.init.ContributorLoader;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public final class TitleManager {
    /** A temporary holding place for advancement ID's to be turned into titles **/
    private static final Set<String> advancementsWithTitles = new HashSet<>();
    /** Stores all in-game available titles by achievement ID */
    private static final Map<Advancement, TitleInfo> titlesByAdvancement = new HashMap<>();

    static void registerTitle(String s) {
        advancementsWithTitles.add(s);
    }

    static void generateTitles() {
        for (Advancement advancement : getAllAdvancements()) {
            for (String id : advancementsWithTitles) {
                if (advancement.getId().toString().equals(id)) {
                    TitleInfo.TitleRarity titleRarity;
                    FrameType frameType = advancement.getDisplay().getFrame();
                    if (frameType.equals(FrameType.CHALLENGE)) {
                        titleRarity = TitleInfo.TitleRarity.RARE;
                    }
                    else if (frameType.equals(FrameType.GOAL)) {
                        titleRarity = TitleInfo.TitleRarity.UNCOMMON;
                    }
                    else {
                        titleRarity = TitleInfo.TitleRarity.COMMON;
                    }
                    String name = "title." + id.replaceAll("([/:])", ".");
                    titlesByAdvancement.put(advancement, new TitleInfo(name, titleRarity));
                }
            }
        }
    }

    public static TitleInfo getSelectedTitle(EntityPlayer player) {
        return TitlesImpl.getCapability(player).getSelectedTitle();
    }

    public static void addTitle(EntityPlayer player, TitleInfo titleInfo) {
        TitlesImpl.getCapability(player).add(titleInfo);
    }

    public static void unlockTitle(EntityPlayer player, Advancement advancement) {
        TitleInfo titleInfo = titlesByAdvancement.get(advancement);

        if (!hasTitle(player, titleInfo)) {
            addTitle(player, titleInfo);
            PacketDispatcher.INSTANCE.sendTo(new PacketSyncUnlockedTitle(titleInfo), (EntityPlayerMP) player);
            FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
                    .sendMessage(new TextComponentTranslation("chat.type.title",
                            new Object[]{player.getDisplayName(), titleInfo.getFormattedTitle()}));
        }
    }

    /// HELPERS
    public static boolean titleExists(Advancement advancement) {
        return titlesByAdvancement.containsKey(advancement);
    }

    public static boolean hasTitle(EntityPlayer player, TitleInfo info) {
        return TitlesImpl.getCapability(player).getObtainedTitles().contains(info);
    }

    private static Iterable<Advancement> getAllAdvancements() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getAdvancementManager().getAdvancements();
    }
    /// END HELPERS
}