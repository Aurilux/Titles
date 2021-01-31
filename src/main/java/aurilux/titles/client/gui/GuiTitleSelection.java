package aurilux.titles.client.gui;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.client.Keybinds;
import aurilux.titles.client.gui.button.SimpleButtonOverride;
import aurilux.titles.client.gui.button.TitleButton;
import aurilux.titles.client.gui.button.ToggleImageButton;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncGenderSetting;
import aurilux.titles.common.network.messages.PacketSyncSelectedTitle;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class GuiTitleSelection extends Screen {
    private final ResourceLocation bgTexture = new ResourceLocation(TitlesAPI.MOD_ID, "textures/gui/title_selection.png");

    private final int NUM_COLS = 2;
    private final int NUM_ROWS = 6;
    protected final int MAX_PER_PAGE = NUM_COLS * NUM_ROWS;
    protected int page;
    protected int maxPages;

    //The size of the GUI image in pixels
    private final int xSize = 308;
    private final int ySize = 218;
    protected int guiLeft;
    protected int guiTop;

    protected final int buttonHeight = 20;

    protected int leftOffset;
    protected int buttonFirstRowStart;
    protected int buttonTitleRowStart;
    protected int buttonSecondRowStart;

    private final List<Button> backButtons = new ArrayList<>();
    private final List<Button> forwardButtons = new ArrayList<>();
    private final List<Button> titleButtons = new ArrayList<>();

    protected PlayerEntity player;
    protected Title temporaryTitle;
    protected List<Title> titlesListCache;
    protected List<Title> titlesListFiltered;
    private boolean gender;

    private TextFieldWidget search;

    private final ITextComponent maleComp = new StringTextComponent("M").mergeStyle(TextFormatting.BLUE);
    private final ITextComponent femaleComp = new StringTextComponent("F").mergeStyle(TextFormatting.RED);

    public GuiTitleSelection(PlayerEntity player, ITitles cap) {
        super(new StringTextComponent("Title Selection"));
        this.player = player;
        gender = cap.getGenderSetting();
        temporaryTitle = cap.getDisplayTitle();

        titlesListCache = new ArrayList<>(cap.getObtainedTitles());
        String playerName = player.getName().getString();
        Title possibleContributor = TitlesAPI.internal().getTitle(playerName);
        if (!possibleContributor.isNull()) {
            titlesListCache.add(possibleContributor);
        }
        titlesListCache.sort(new Title.RarityComparator());
        titlesListFiltered = new ArrayList<>(titlesListCache);
    }

    @Override
    public void init() {
        super.init();

        page = 1;
        updateMaxPages();
        guiLeft = (this.width - xSize) / 2;
        guiTop = ((this.height - ySize) / 2) - 10;
        leftOffset = guiLeft + 34;
        buttonFirstRowStart = guiTop + 37;
        buttonTitleRowStart = buttonFirstRowStart + 23;
        buttonSecondRowStart = buttonTitleRowStart + 123;

        search = new TextFieldWidget(this.font, leftOffset + 65, buttonFirstRowStart + 1, 110, 18, new StringTextComponent("search"));
        addButton(search);

        // Action buttons
        addButton(new SimpleButtonOverride(leftOffset, buttonFirstRowStart, 60, buttonHeight,
                new TranslationTextComponent("gui.titles.random"), button -> chooseRandomTitle()));
        addButton(new SimpleButtonOverride(leftOffset + 180, buttonFirstRowStart, 60, buttonHeight,
                new TranslationTextComponent("gui.titles.none"), button -> temporaryTitle = Title.NULL_TITLE));
        addButton(new SimpleButtonOverride(leftOffset + 45, buttonSecondRowStart, 60, buttonHeight,
                new TranslationTextComponent("gui.titles.cancel"), button -> exitScreen(false)));
        addButton(new SimpleButtonOverride(leftOffset + 135, buttonSecondRowStart, 60, buttonHeight,
                new TranslationTextComponent("gui.titles.confirm"), button -> exitScreen(true)));

        // Page buttons
        backButtons.add(addButton(new SimpleButtonOverride(leftOffset, buttonSecondRowStart, 20, buttonHeight,
                ITextComponent.getTextComponentOrEmpty("<<"), button -> setPage(1))));
        backButtons.add(addButton(new SimpleButtonOverride(leftOffset + 22, buttonSecondRowStart, 20, buttonHeight,
                ITextComponent.getTextComponentOrEmpty("<"), button -> setPage(page - 1))));
        forwardButtons.add(addButton(new SimpleButtonOverride(leftOffset + 198, buttonSecondRowStart, 20, buttonHeight,
                ITextComponent.getTextComponentOrEmpty(">"), button -> setPage(page + 1))));
        forwardButtons.add(addButton(new SimpleButtonOverride(leftOffset + 220, buttonSecondRowStart, 20, buttonHeight,
                ITextComponent.getTextComponentOrEmpty(">>"), button -> setPage(maxPages))));

        // Gender button
        addButton(new ToggleImageButton(guiLeft + 3, buttonFirstRowStart, 20, buttonHeight, 200, 220, 20, bgTexture, 512, 512, button -> genderToggle((ToggleImageButton) button), gender));

        updateButtons();
    }

    private void updateMaxPages() {
        maxPages = Math.max(1, (int) Math.ceil(titlesListFiltered.size() / (double) MAX_PER_PAGE));
    }

    private void genderToggle(ToggleImageButton button) {
        button.toggle();
        gender = button.getValue();
        updateButtons();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Renders the background tint
        renderBackground(matrixStack);
        RenderSystem.color4f(1, 1, 1, 1);
        getMinecraft().getTextureManager().bindTexture(bgTexture);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
        // Renders all the buttons (and the search bar since I added it with 'addButton')
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        // Draw the player's name with their selected title
        ITextComponent titledPlayerName = TitlesAPI.getFormattedTitle(temporaryTitle, player.getName(), gender);
        drawCenteredString(matrixStack, this.font, titledPlayerName, this.width / 2, guiTop + 17, 0xFFFFFF);

        // Draw the page counter
        drawCenteredString(matrixStack, this.font, String.format("%s/%s", page, maxPages), this.width / 2, guiTop + 189, 0xFFFFFF);

        if (titlesListFiltered.size() == 0) {
            String emptyText = "gui.titles.titleselection.empty";
            if (titlesListCache.size() > 0) {
                emptyText += ".filter";
            }
            drawCenteredString(matrixStack, this.font, I18n.format(emptyText), this.width / 2, guiTop + 109, 0xFFFFFF);
        }
    }

    protected void exitScreen(boolean update) {
        if (update) {
            PacketHandler.sendToServer(new PacketSyncSelectedTitle(player.getUniqueID(), temporaryTitle.getKey()));
        }
        PacketHandler.sendToServer(new PacketSyncGenderSetting(player.getUniqueID(), gender));
        closeScreen();
    }

    @Override
    public void tick() {
        search.tick();
        super.tick();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (search.isFocused()) {
            String s = search.getText();
            if (super.keyPressed(keyCode, scanCode, modifiers)) {
                if (!Objects.equals(s, search.getText())) {
                    parseSearch(search.getText());
                }
            }
            return true;
        }
        else if (Keybinds.openTitleSelection.getKeyBinding().matchesKey(keyCode, scanCode)
                || getMinecraft().gameSettings.keyBindInventory.matchesKey(keyCode, scanCode)) {
            exitScreen(false);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (search.isFocused()) {
            String s = search.getText();
            if (search.charTyped(codePoint, modifiers)) {
                if (!Objects.equals(s, search.getText())) {
                    parseSearch(search.getText());
                }
            }
            return true;
        }
        else {
            return super.charTyped(codePoint, modifiers);
        }
    }

    private void parseSearch(String searchText) {
        List<String> parts = new ArrayList<>(Arrays.asList(searchText.toLowerCase().split("\\s")));
        String modFilter = "";
        String rarityFilter = "";
        for (Iterator<String> iter = parts.iterator(); iter.hasNext(); ) {
            String part = iter.next();
            if (part.startsWith("@") && modFilter.equals("")) {
                modFilter = part.substring(1);
                iter.remove();
            }
            if (part.startsWith("#") && rarityFilter.equals("")) {
                rarityFilter = part.substring(1);
                iter.remove();
            }
        }

        String finalModFilter = modFilter;
        String finalRarityFilter = rarityFilter;
        // TODO implement a binary search tree to make this better/faster(/stronger)
        titlesListFiltered = titlesListCache.stream()
                .filter(t -> t.getModid().startsWith(finalModFilter))
                .filter(t -> {
                    String rarityName = t.getRarity().name();
                    for (String letter : finalRarityFilter.split("")) {
                        if (rarityName.startsWith(letter.toUpperCase())) {
                            return true;
                        }
                    }
                    return false;
                })
                .filter(t -> {
                    if (parts.size() < 1) {
                        return true;
                    }
                    String titleString = TitlesAPI.getFormattedTitle(t, gender).getString().toLowerCase();
                    for (String part : parts) {
                        if (titleString.contains(part)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());

        updateMaxPages();
        if (page > maxPages) {
            page = 1;
        }
        updateButtons();
    }

    protected void updateButtons() {
        removeButtons(titleButtons);
        titleButtons.clear();
        int maxIndex = Math.min(page * MAX_PER_PAGE, titlesListFiltered.size());

        // Make some page buttons inactive
        if (page == 1) {
            backButtons.forEach(b -> b.active = false);
        }
        else {
            backButtons.forEach(b -> b.active = true);
        }
        if (maxIndex == titlesListFiltered.size()) {
            forwardButtons.forEach(b -> b.active = false);
        }
        else {
            forwardButtons.forEach(b -> b.active = true);
        }

        // Add all of the displayed title buttons
        List<Title> titlesToDisplay = titlesListFiltered.subList((page - 1) * MAX_PER_PAGE, maxIndex);
        int titleButtonWidth = 120;
        for (int i = 0; i < titlesToDisplay.size(); i++) {
            int col = i % NUM_COLS;
            int row = i / NUM_COLS;
            int x = leftOffset + (titleButtonWidth * col);
            int y = buttonTitleRowStart + (row * buttonHeight);
            Button button = addButton(new TitleButton(x, y, titleButtonWidth, buttonHeight, b -> temporaryTitle = ((TitleButton) b).getTitle(), titlesToDisplay.get(i), gender));
            titleButtons.add(button);
        }
        buttons.addAll(titleButtons);
    }

    private void removeButtons(List<Button> list) {
        buttons.removeAll(list);
        children.removeAll(list);
    }

    private void setPage(int pageNum) {
        page = pageNum;
        updateButtons();
    }

    private void chooseRandomTitle() {
        if (titlesListFiltered.size() <= 0) {
            temporaryTitle = Title.NULL_TITLE;
        }
        else {
            temporaryTitle = titlesListFiltered.get(player.world.rand.nextInt(titlesListFiltered.size()));
        }
    }
}