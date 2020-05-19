package aurilux.titles.client.gui;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.client.gui.button.GuiButtonTitle;
import aurilux.titles.client.handler.ClientEventHandler;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiTitleSelection extends Screen {
    private final ResourceLocation bgTexture = new ResourceLocation("titles", "textures/gui/titleselection.png");

    private final int NUM_COLS = 2;
    private final int NUM_ROWS = 6;
    protected final int MAX_PER_PAGE = NUM_COLS * NUM_ROWS;
    protected int page;
    protected int maxPages;

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

    protected PlayerEntity player;
    protected ITitles playerCapability;
    protected TitleInfo temporaryTitle;
    protected List<TitleInfo> titlesList;

    public GuiTitleSelection(PlayerEntity player, ITitles cap) {
        super(new StringTextComponent("Title Selection"));
        this.player = player;
        playerCapability = cap;
        temporaryTitle = playerCapability.getSelectedTitle();
        titlesList = getTitlesList();
    }

    public List<TitleInfo> getTitlesList() {
        List<TitleInfo> temp = new ArrayList<>(playerCapability.getUnlockedTitles());
        String playerName = player.getName().getString();
        TitleInfo possibleContributor = TitleRegistry.INSTANCE.getTitle(playerName);
        if (!possibleContributor.isNull()) {
            temp.add(possibleContributor);
        }
        Collections.sort(temp, new TitleInfo.RarityComparator());
        return temp;
    }

    @Override
    public void init() {
        super.init();

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
    public void render(int mouseX, int mouseY, float f) {
        //renders the background tint
        this.renderBackground();
        RenderSystem.pushMatrix();
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(bgTexture);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
        RenderSystem.popMatrix();
        super.render(mouseX, mouseY, f);

        //Draw the player's name with their selected title
        String titledPlayerName = player.getName().getString();
        titledPlayerName += TitlesAPI.instance().getFormattedTitle(temporaryTitle, true);
        this.drawCenteredString(this.font, titledPlayerName, this.width / 2, guiTop + 11, 0xFFFFFF);

        //Draw the page counter
        this.drawCenteredString(this.font, (page + 1) + "/" + (maxPages + 1), this.width / 2, guiTop + 183, 0xFFFFFF);

        if (titlesList.size() == 0) {
            this.drawCenteredString(this.font, getEmptyMessage(), this.width / 2, guiTop + 105, 0xFFFFFF);
        }
    }

    protected String getEmptyMessage() {
        return I18n.format("gui.titles.titleselection.empty");
    }

    protected void exitScreen(boolean update) {
        if (update) {
            PacketHandler.sendToServer(new PacketSyncSelectedTitle(player.getUniqueID(), temporaryTitle.getKey()));
        }
        getMinecraft().displayGuiScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ClientEventHandler.openTitleSelection.getKeyBinding().matchesKey(keyCode, scanCode)
                || getMinecraft().gameSettings.keyBindInventory.matchesKey(keyCode, scanCode)) {
            exitScreen(false);
            return true;
        }
        else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    protected void updateButtonList() {
        buttons.clear();

        //Add all of the displayed title buttons
        maxIndex = Math.min((page + 1) * MAX_PER_PAGE, titlesList.size());
        List<TitleInfo> titlesToDisplay = titlesList.subList(page * MAX_PER_PAGE, maxIndex);
        for (int i = 0; i < titlesToDisplay.size(); i++) {
            int col = i % NUM_COLS;
            int row = i / NUM_COLS;
            int x = leftOffset + (titleButtonWidth * col);
            int y = buttonTitleRowStart + (row * buttonHeight);
            this.addButton(new GuiButtonTitle(x, y, titleButtonWidth, buttonHeight, this::titleButtonPressed, titlesToDisplay.get(i)));
        }

        addChangeButtons();

        //Add the navigation buttons
        this.addButton(new Button(leftOffset, buttonSecondRowStart, 20, buttonHeight, "<<", button -> setPage(0)));
        this.addButton(new Button(leftOffset + 22, buttonSecondRowStart, 20, buttonHeight, "<", button -> setPage(page--)));
        if (page == 0) {
            buttons.get(buttons.size() - 2).active = false;
            buttons.get(buttons.size() - 1).active = false;
        }
        this.addButton(new Button(leftOffset + 198, buttonSecondRowStart, 20, buttonHeight, ">", button -> setPage(page++)));
        this.addButton(new Button(leftOffset + 220, buttonSecondRowStart, 20, buttonHeight, ">>", button -> setPage(MAX_PER_PAGE)));
        if (maxIndex == titlesList.size()) {
            buttons.get(buttons.size() - 2).active = false;
            buttons.get(buttons.size() - 1).active = false;
        }
    }

    protected void addChangeButtons() {
        this.addButton(new Button(leftOffset, buttonFirstRowStart, 60, buttonHeight,
                I18n.format("gui.titles.random"), button -> chooseRandomTitle()));
        this.addButton(new Button(leftOffset + 180, buttonFirstRowStart, 60, buttonHeight,
                I18n.format("gui.titles.none"), button -> temporaryTitle = TitleInfo.NULL_TITLE));
        this.addButton(new Button(leftOffset + 45, buttonSecondRowStart, 60, buttonHeight,
                I18n.format("gui.titles.cancel"), button -> exitScreen(false)));
        this.addButton(new Button(leftOffset + 135, buttonSecondRowStart, 60, buttonHeight,
                I18n.format("gui.titles.confirm"), button -> exitScreen(true)));
    }

    private void titleButtonPressed(Button button) {
        temporaryTitle = ((GuiButtonTitle) button).getTitle();
    }

    private void setPage(int pageNum) {
        page = pageNum;
    }

    private void chooseRandomTitle() {
        if (titlesList.size() <= 0) {
            temporaryTitle = TitleInfo.NULL_TITLE;
        }
        else {
            temporaryTitle = titlesList.get(player.world.rand.nextInt(titlesList.size()));
        }
    }
}