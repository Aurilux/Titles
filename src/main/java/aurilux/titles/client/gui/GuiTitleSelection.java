package aurilux.titles.client.gui;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.client.ModKeybindings;
import aurilux.titles.common.init.ContributorLoader;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
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
    private final ResourceLocation bgTexture = new ResourceLocation("titles", "textures/gui/titleselection.png");

    private final int numCols = 2;
    private final int numRows = 6;
    protected final int maxPerPage = numCols * numRows;
    protected int page;
    protected int maxPages;

    private final int RANDOM_BUTTON = maxPerPage + 1;
    private final int NONE_BUTTON = maxPerPage + 2;
    private final int CANCEL_BUTTON = maxPerPage + 3;
    private final int CONFIRM_BUTTON = maxPerPage + 4;
    protected final int FIRST_PAGE = maxPerPage + 5;
    protected final int PREV_PAGE = maxPerPage + 6;
    protected final int NEXT_PAGE = maxPerPage + 7;
    protected final int LAST_PAGE = maxPerPage + 8;

    //The size of the GUI image in pixels
    private final int xSize = 256;
    private final int ySize = 222;
    protected int guiLeft;
    protected int guiTop;

    protected final int buttonHeight = 20;
    private final int titleButtonWidth = 120;
    private int maxIndex;

    protected int leftOffset;
    protected int buttonFirstRowStart;
    protected int buttonTitleRowStart;
    protected int buttonSecondRowStart;

    protected EntityPlayer player;
    protected TitleInfo temporaryTitle;
    protected List<TitleInfo> titlesList;

    public GuiTitleSelection(EntityPlayer player) {
        this.player = player;
        temporaryTitle = TitlesAPI.getPlayerSelectedTitle(player);
        titlesList = getTitlesList();
    }

    public List<TitleInfo> getTitlesList() {
        String playerName = player.getName();
        List<TitleInfo> temp = new ArrayList<>(TitlesAPI.getTitlesCap(player).getObtainedTitles());
        if (ContributorLoader.contributorTitleExists(playerName)) {
            temp.add(ContributorLoader.getContributorTitle(playerName));
        }
        Collections.sort(temp, new TitleInfo.RarityComparator());
        return temp;
    }

    @Override
    public void initGui() {
        super.initGui();

        page = 0;
        maxPages = titlesList.size() / 12;
        guiLeft = (this.width - xSize) / 2;
        guiTop = (this.height - ySize) / 2;
        leftOffset = guiLeft + 8;
        buttonFirstRowStart = guiTop + 28;
        buttonTitleRowStart = guiTop + 53;
        buttonSecondRowStart = guiTop + 176;

        updateButtonList();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        //renders the background tint
        this.drawDefaultBackground();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bgTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        super.drawScreen(mouseX, mouseY, f);

        //Draw the player's name with their selected title
        String titledPlayerName = player.getName();
        titledPlayerName += TitlesAPI.internalHandler.getFormattedTitle(temporaryTitle, true);
        this.drawCenteredString(this.fontRenderer, titledPlayerName, this.width / 2, guiTop + 11, 0xFFFFFF);

        //Draw the page counter
        this.drawCenteredString(this.fontRenderer, (page + 1) + "/" + (maxPages + 1), this.width / 2, guiTop + 183, 0xFFFFFF);

        if (titlesList.size() == 0) {
            this.drawCenteredString(this.fontRenderer, I18n.format(getEmptyMessage()), this.width / 2, guiTop + 105, 0xFFFFFF);
        }
    }

    protected String getEmptyMessage() {
        return "gui.titles.titleselection.empty";
    }

    protected void exitScreen(boolean update) {
        if (update) {
            PacketDispatcher.INSTANCE.sendToServer(new PacketSyncSelectedTitle(player.getUniqueID(), temporaryTitle.getKey()));
        }
        mc.displayGuiScreen(null);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case CANCEL_BUTTON : exitScreen(false); break;
            case CONFIRM_BUTTON : exitScreen(true); break;
            case RANDOM_BUTTON :  chooseRandomTitle(); break;
            case NONE_BUTTON : temporaryTitle = TitleInfo.NULL_TITLE; break;
            case FIRST_PAGE : page = 0; break;
            case PREV_PAGE : page--; break;
            case NEXT_PAGE : page++; break;
            case LAST_PAGE : page = maxPages; break;
            default : temporaryTitle = titlesList.get(button.id + (page * maxPerPage));
        }
        updateButtonList();
    }

    private void chooseRandomTitle() {
        if (titlesList.size() <= 0) {
            temporaryTitle = TitleInfo.NULL_TITLE;
        }
        else {
            temporaryTitle = titlesList.get(player.world.rand.nextInt(titlesList.size()));
        }
    }

    @Override
    protected void keyTyped(char c, int ascii) throws IOException {
        super.keyTyped(c, ascii);
        if (ModKeybindings.OPEN_TITLE_SELECTION.getKeyBinding().getKeyCode() == ascii
                || mc.gameSettings.keyBindInventory.getKeyCode() == ascii) {
            exitScreen(false);
        }
    }

    protected void updateButtonList() {
        buttonList.clear();

        //Add all of the displayed title buttons
        maxIndex = Math.min((page + 1) * maxPerPage, titlesList.size());
        List<TitleInfo> titlesToDisplay = titlesList.subList(page * maxPerPage, maxIndex);
        for (int i = 0; i < titlesToDisplay.size(); i++) {
            int col = i % numCols;
            int row = i / numCols;
            buttonList.add(new GuiButton(i, leftOffset + (titleButtonWidth * col), buttonTitleRowStart + (row * buttonHeight), titleButtonWidth, buttonHeight, TitlesAPI.internalHandler.getFormattedTitle(titlesToDisplay.get(i))));
        }

        addChangeButtons();

        //Add the navigation buttons
        buttonList.add(new GuiButton(FIRST_PAGE, leftOffset, buttonSecondRowStart, 20, buttonHeight, "<<"));
        buttonList.add(new GuiButton(PREV_PAGE, leftOffset + 22, buttonSecondRowStart, 20, buttonHeight, "<"));
        if (page == 0) {
            buttonList.get(buttonList.size() - 2).enabled = false;
            buttonList.get(buttonList.size() - 1).enabled = false;
        }
        buttonList.add(new GuiButton(NEXT_PAGE, leftOffset + 198, buttonSecondRowStart, 20, buttonHeight, ">"));
        buttonList.add(new GuiButton(LAST_PAGE, leftOffset + 220, buttonSecondRowStart, 20, buttonHeight, ">>"));
        if (maxIndex == titlesList.size()) {
            buttonList.get(buttonList.size() - 2).enabled = false;
            buttonList.get(buttonList.size() - 1).enabled = false;
        }
    }

    protected void addChangeButtons() {
        buttonList.add(new GuiButton(RANDOM_BUTTON, leftOffset, buttonFirstRowStart, 60, buttonHeight, I18n.format("gui.titles.random")));
        buttonList.add(new GuiButton(NONE_BUTTON, leftOffset + 180, buttonFirstRowStart, 60, buttonHeight, I18n.format("gui.titles.none")));
        buttonList.add(new GuiButton(CANCEL_BUTTON, leftOffset + 45, buttonSecondRowStart, 60, buttonHeight, I18n.format("gui.titles.cancel")));
        buttonList.add(new GuiButton(CONFIRM_BUTTON, leftOffset + 135, buttonSecondRowStart, 60, buttonHeight, I18n.format("gui.titles.confirm")));
    }
}