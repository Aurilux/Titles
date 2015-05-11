package aurilux.titles.common.core;

import aurilux.ardentcore.common.util.LangUtils;
import aurilux.titles.api.TitlesApi;
import aurilux.titles.common.handler.TitleHandler;
import aurilux.titles.common.packets.PacketSyncObtainedTitles;
import aurilux.titles.common.packets.PacketSyncSelectedTitles;
import aurilux.titles.common.packets.PacketTitleSelection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [12 Mar 2015]
 */
public class CommonProxy implements IGuiHandler {
    /** Stores the titles currently selected/displayed by each player */
    public final Map<String, TitleInfo> selectedTitles = new HashMap<String, TitleInfo>();
    /** Stores all titles specific players have earned */
    public final Map<String, ArrayList<TitleInfo>> titlesByPlayer = new HashMap<String, ArrayList<TitleInfo>>();

    //Gui Id's
    public static int TITLE_SEL_ID = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public void init() {
        registerHandlers();
        registerPackets();
        //registerCommands();
    }

    protected void registerHandlers() {
        TitleHandler handler = new TitleHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
    }

    protected void registerPackets() {
        Titles.network.registerServerMessage(PacketTitleSelection.class, PacketTitleSelection.class);

        Titles.network.registerClientMessage(PacketSyncSelectedTitles.class, PacketSyncSelectedTitles.class);
        Titles.network.registerClientMessage(PacketSyncObtainedTitles.class, PacketSyncObtainedTitles.class);
    }

    public void createProfile(EntityPlayer player, ArrayList<TitleInfo> titles) {
        Titles.proxy.titlesByPlayer.put(player.getCommandSenderName(), titles);
        savePlayerTitles(player);
    }

    public void savePlayerTitles(EntityPlayer player) {
        ArrayList<TitleInfo> titles = getPlayerTitles(player.getCommandSenderName());
        NBTTagCompound persistentTag = TitlesApi.getPersistantPlayerTag(player);
        persistentTag.setString("Titles_unlocked", TitlesApi.buildTitleString(titles));
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistentTag);
    }

    public void saveSelectedTitle(EntityPlayer player) {
        TitleInfo title = getSelectedTitle(player.getCommandSenderName());
        NBTTagCompound persistentTag = TitlesApi.getPersistantPlayerTag(player);
        persistentTag.setString("Titles_selected", title.toString());
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistentTag);
    }

    public void saveAllTitles(EntityPlayer player) {
        savePlayerTitles(player);
        saveSelectedTitle(player);
    }

    public void setSelectedTitle(EntityPlayer player, TitleInfo title) {
        selectedTitles.put(player.getCommandSenderName(), title);
        saveSelectedTitle(player);
        player.refreshDisplayName();
    }

    public TitleInfo getSelectedTitle(String playerName) {
        return selectedTitles.get(playerName);
    }

    public ArrayList<TitleInfo> getPlayerTitles(String playerName) {
        return titlesByPlayer.get(playerName);
    }

    /**
     * Unlocks a title for a player's use
     * @param player the player who just unlocked a title
     * @param achievementId the achievement id the title is linked to
     */
    public void unlockTitle(EntityPlayer player, String achievementId) {
        ArrayList<TitleInfo> playerTitles = Titles.proxy.getPlayerTitles(player.getCommandSenderName());
        TitleInfo title = TitlesApi.titlesByAchievement.get(achievementId);
        if (title != null) {
            playerTitles.add(title);

            saveAllTitles(player);

            //Send a chat message to all player's notifying them that this player has just earned a title
            FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendChatMsg
                    (new ChatComponentText(LangUtils.translateFormatted("misc.titles.unlocked",
                            player.getCommandSenderName(), title.getTitleColor(), LangUtils.translate("title." + title.titleId))));
        }
        else {
            Titles.logger.info(LangUtils.translateFormatted("misc.titles.unlockError", achievementId));
        }
    }
}