package aurilux.titles.client.gui;

import aurilux.titles.client.ModKeybindings;
import aurilux.titles.common.TitleInfo;
import aurilux.titles.common.TitleManager;
import aurilux.titles.common.init.ContributorLoader;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketTitleSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiTitleSelection extends GuiScreen {
    private final int numCols = 2;
    private final int numRows = 6;
    private final int maxPerPage = numCols * numRows;

    private final int RANDOM_BUTTON = maxPerPage + 1;
    private final int NONE_BUTTON = maxPerPage + 2;
    private final int FIRST_PAGE = maxPerPage + 3;
    private final int PREV_PAGE = maxPerPage + 4;
    private final int CANCEL_BUTTON = maxPerPage + 5;
    private final int CONFIRM_BUTTON = maxPerPage + 6;
    private final int NEXT_PAGE = maxPerPage + 7;
    private final int LAST_PAGE = maxPerPage + 8;

    private final int xSize = 256;
    private final int ySize = 222;
    private int guiLeft;
    private int guiTop;

    private EntityPlayer player;
    private TitleInfo temporaryTitle;
    private int page;
    private List<TitleInfo> playerTitles;

    private final ResourceLocation bgTexture = new ResourceLocation("titles", "textures/gui/titleselection.png");

    public GuiTitleSelection(EntityPlayer player) {
        this.player = player;
        page = 0;

        String playerName = player.getName();
        playerTitles = new ArrayList<>(TitleManager.getObtainedTitles(player.getUniqueID()));
        if (ContributorLoader.contributorTitleExists(playerName)) {
            playerTitles.add(ContributorLoader.getContributorTitle(playerName));
        }
        temporaryTitle = TitleManager.getSelectedTitle(player.getUniqueID());
        Collections.sort(playerTitles);
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
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bgTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        super.drawScreen(mouseX, mouseY, f);

        String displayString = player.getName();
        if (!temporaryTitle.equals(TitleInfo.NULL_TITLE)) {
            displayString += ", " + temporaryTitle.getFormattedTitle();
        }
        this.drawCenteredString(this.fontRenderer, displayString, this.width / 2, guiTop + 11, 0xFFFFFF);
    }

    private void exitScreen(boolean update) {
        if (update) {
            PacketDispatcher.INSTANCE.sendToServer(new PacketTitleSelection(player.getUniqueID(), temporaryTitle));
        }
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
            case RANDOM_BUTTON :  chooseRandomTitle(); updateButtonList(); break;
            case NONE_BUTTON : temporaryTitle = TitleInfo.NULL_TITLE; updateButtonList(); break;
            default : temporaryTitle = playerTitles.get(button.id + (page * maxPerPage)); updateButtonList();
        }
    }

    private void chooseRandomTitle() {
        if (playerTitles.size() <= 0) {
            temporaryTitle = TitleInfo.NULL_TITLE;
        }
        else {
            temporaryTitle = playerTitles.get(player.world.rand.nextInt(playerTitles.size()));
        }
    }


    @Override
    protected void keyTyped(char c, int ascii) throws IOException {
        super.keyTyped(c, ascii);
        if (ModKeybindings.OPEN_TITLE_SELECTION.getKeyBinding().getKeyCode() == ascii
                && Minecraft.getMinecraft().currentScreen instanceof GuiTitleSelection) {
            exitScreen(false);
        }
    }

    public void updateButtonList() {
        buttonList.clear();

        int buttonHeight = 20;
        int titleButtonWidth = 120;
        int leftOffset = guiLeft + 8;

        int buttonFirstRowStart = guiTop + 28;
        int buttonTitleRowStart = guiTop + 53;
        int buttonSecondRowStart = guiTop + 176;

        int maxIndex = Math.min((page + 1) * maxPerPage, playerTitles.size());
        List<TitleInfo> titlesToDisplay = playerTitles.subList(page * maxPerPage, maxIndex);
        for (int i = 0; i < titlesToDisplay.size(); i++) {
            int col = i % numCols;
            int row = i / numCols;
            buttonList.add(new GuiButton(i, leftOffset + (titleButtonWidth * col), buttonTitleRowStart + (row * buttonHeight), titleButtonWidth, buttonHeight, titlesToDisplay.get(i).getFormattedTitle()));
        }

        buttonList.add(new GuiButton(RANDOM_BUTTON, leftOffset, buttonFirstRowStart, 60, buttonHeight, I18n.format("gui.titles.random")));
        buttonList.add(new GuiButton(NONE_BUTTON, leftOffset + 180, buttonFirstRowStart, 60, buttonHeight, I18n.format("gui.titles.none")));

        buttonList.add(new GuiButton(FIRST_PAGE, leftOffset, buttonSecondRowStart, 20, buttonHeight, "<<"));
        buttonList.add(new GuiButton(PREV_PAGE, leftOffset + 22, buttonSecondRowStart, 20, buttonHeight, "<"));
        if (page == 0) {
            buttonList.get(buttonList.size() - 2).enabled = false;
            buttonList.get(buttonList.size() - 1).enabled = false;
        }
        buttonList.add(new GuiButton(CANCEL_BUTTON, leftOffset + 55, buttonSecondRowStart, 60, buttonHeight, I18n.format("gui.titles.cancel")));
        buttonList.add(new GuiButton(CONFIRM_BUTTON, leftOffset + 125, buttonSecondRowStart, 60, buttonHeight, I18n.format("gui.titles.confirm")));

        buttonList.add(new GuiButton(NEXT_PAGE, leftOffset + 198, buttonSecondRowStart, 20, buttonHeight, ">"));
        buttonList.add(new GuiButton(LAST_PAGE, leftOffset + 220, buttonSecondRowStart, 20, buttonHeight, ">>"));
        if (maxIndex == playerTitles.size()) {
            buttonList.get(buttonList.size() - 2).enabled = false;
            buttonList.get(buttonList.size() - 1).enabled = false;
        }
    }
}