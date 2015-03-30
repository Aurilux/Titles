package aurilux.titles.client.gui;

import aurilux.titles.api.TitlesApi;
import aurilux.titles.client.core.Keybindings;
import aurilux.titles.common.core.TitleInfo;
import aurilux.titles.common.core.Titles;
import aurilux.titles.common.packets.PacketTitleSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [19 Mar 2015]
 */
public class GuiTitleSelection extends GuiScreen {
    private final int BUTTON_SPACING = 10;
    private final int RANDOM_BUTTON = 12;
    private final int NONE_BUTTON = 13;
    private final int FIRST_PAGE = 14;
    private final int PREV_PAGE = 15;
    private final int CANCEL_BUTTON = 16;
    private final int CONFIRM_BUTTON = 17;
    private final int NEXT_PAGE = 18;
    private final int LAST_PAGE = 19;

    private final int xSize = 260;
    private final int ySize = 170;
    private final int maxPerPage = 12;
    private int guiLeft;
    private int guiTop;

    private EntityPlayer player;
    private TitleInfo temporaryTitle;
    private int page;
    private ArrayList<TitleInfo> playerTitles;
    public GuiTitleSelection(EntityPlayer player) {
        this.player = player;
        page = 0;
        playerTitles = TitlesApi.getPlayerTitles(player.getCommandSenderName());
        temporaryTitle = TitlesApi.getSelectedTitle(player.getCommandSenderName());
    }

    @Override
    public void initGui() {
        super.initGui();

        guiLeft = (this.width - xSize) / 2;
        guiTop = (this.height - ySize) / 2;

        updateButtonList();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        super.drawScreen(mouseX, mouseY, f);
        String displayString = player.getCommandSenderName();
        if(temporaryTitle != null) {
            displayString += ", " + temporaryTitle.getFormattedTitle();
        }
        this.drawCenteredString(this.fontRendererObj, displayString, this.width / 2, guiTop - 5, 0xFFFFFF);
    }

    public void exitScreen(boolean update) {
        if (update) Titles.network.sendToServer(new PacketTitleSelection(temporaryTitle));
        mc.displayGuiScreen(null);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case CANCEL_BUTTON : exitScreen(false); break;
            case CONFIRM_BUTTON : exitScreen(true); break;
            case FIRST_PAGE : page = 0; updateButtonList(); break;
            case PREV_PAGE : page--; updateButtonList(); break;
            case NEXT_PAGE : page++; updateButtonList(); break;
            case LAST_PAGE : page = (int) Math.ceil(playerTitles.size() / 12); updateButtonList(); break;
            case RANDOM_BUTTON : temporaryTitle = playerTitles.get(player.worldObj.rand.nextInt(playerTitles.size())); updateButtonList(); break;
            case NONE_BUTTON : temporaryTitle = null; updateButtonList(); break;
            default : temporaryTitle = playerTitles.get(button.id + (page * maxPerPage)); updateButtonList();
        }
    }


    @Override
    protected void keyTyped(char c, int ascii) {
        super.keyTyped(c, ascii);
        if (Keybindings.OPEN_TITLE_SELECTION.getKeyBinding().getKeyCode() == ascii
                && Minecraft.getMinecraft().currentScreen instanceof GuiTitleSelection) {
            exitScreen(false);
        }
    }

    public void updateButtonList() {
        buttonList.clear();

        int maxIndex = Math.min((page + 1) * maxPerPage, playerTitles.size());
        List<TitleInfo> titlesToDisplay = playerTitles.subList(page * maxPerPage, maxIndex);
        for (int i = 0; i < titlesToDisplay.size(); i++) {
            int col = i / 4;
            int row = i % 4;
            buttonList.add(new GuiButton(i, guiLeft + col * (120 + BUTTON_SPACING) - 60, guiTop + row * (20 + BUTTON_SPACING) + 20, 120, 20, titlesToDisplay.get(i).getFormattedTitle()));
        }

        buttonList.add(new GuiButton(RANDOM_BUTTON, guiLeft + 0 * (20 + BUTTON_SPACING), guiTop - BUTTON_SPACING, 60, 20, I18n.format("gui.titles.random")));
        buttonList.add(new GuiButton(NONE_BUTTON, guiLeft + 6 * (20 + BUTTON_SPACING) + 20, guiTop - BUTTON_SPACING, 60, 20, I18n.format("gui.titles.none")));

        buttonList.add(new GuiButton(FIRST_PAGE, guiLeft + 0 * (20 + BUTTON_SPACING), guiTop + 140, 20, 20, "<<"));
        buttonList.add(new GuiButton(PREV_PAGE, guiLeft + 1 * (20 + BUTTON_SPACING), guiTop + 140, 20, 20, "<"));
        if (page == 0) {
            ((GuiButton) buttonList.get(buttonList.size() - 2)).enabled = false;
            ((GuiButton) buttonList.get(buttonList.size() - 1)).enabled = false;
        }
        buttonList.add(new GuiButton(CANCEL_BUTTON, guiLeft + 2 * (20 + BUTTON_SPACING) + 5, guiTop + 140, 60, 20, I18n.format("gui.titles.cancel")));
        buttonList.add(new GuiButton(CONFIRM_BUTTON, guiLeft + 3 * (20 + BUTTON_SPACING) + 45, guiTop + 140, 60, 20, I18n.format("gui.titles.confirm")));
        buttonList.add(new GuiButton(NEXT_PAGE, guiLeft + 4 * (20 + BUTTON_SPACING) + 90, guiTop + 140, 20, 20, ">"));
        buttonList.add(new GuiButton(LAST_PAGE, guiLeft + 5 * (20 + BUTTON_SPACING) + 90, guiTop + 140, 20, 20, ">>"));
        if (maxIndex == playerTitles.size()) {
            ((GuiButton) buttonList.get(buttonList.size() - 2)).enabled = false;
            ((GuiButton) buttonList.get(buttonList.size() - 1)).enabled = false;
        }
    }
}