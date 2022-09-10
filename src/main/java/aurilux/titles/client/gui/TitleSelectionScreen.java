package aurilux.titles.client.gui;

import aurilux.titles.api.Title;
import aurilux.titles.client.Keybinds;
import aurilux.titles.client.gui.button.SimpleButtonOverride;
import aurilux.titles.client.gui.button.TitleButton;
import aurilux.titles.client.gui.button.ToggleImageButton;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.core.TitlesCapability;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncDisplayTitle;
import aurilux.titles.common.network.messages.PacketSyncGenderSetting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class TitleSelectionScreen extends Screen {
    private final ResourceLocation bgTexture = TitlesMod.prefix("textures/gui/title_selection.png");
    private final Button.OnTooltip titleWithFlavorText = (button, matrixStack, mouseX, mouseY) -> {
        String titleButtonFlavorText = ((TitleButton) button).getTitle().getFlavorText();
        if (button.active && !StringUtil.isNullOrEmpty(titleButtonFlavorText)) {
            this.renderTooltip(matrixStack,this.minecraft.font.split(new TranslatableComponent(titleButtonFlavorText), Math.max(this.width / 2 - 43, 170)), mouseX, mouseY);
        }
    };

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

    protected Player player;
    protected Title temporaryTitle;
    protected List<Title> titlesListCache;
    protected List<Title> titlesListFiltered;
    private boolean gender;

    private EditBox search;

    public TitleSelectionScreen(Player player, TitlesCapability cap) {
        super(new TextComponent("Title Selection"));
        this.player = player;
        gender = cap.getGenderSetting();
        temporaryTitle = cap.getDisplayTitle();

        titlesListCache = new ArrayList<>(cap.getObtainedTitles());
        String playerName = player.getName().getString();
        ResourceLocation contributorTitle = TitlesMod.prefix(playerName.toLowerCase(Locale.ROOT));
        Title possibleContributor = TitleManager.getTitlesOfType(Title.AwardType.CONTRIBUTOR)
                .getOrDefault(contributorTitle, Title.NULL_TITLE);
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

        search = new EditBox(this.font, leftOffset + 65, buttonFirstRowStart + 1, 110, 18, new TextComponent("search"));
        addButton(search);

        // Action buttons
        addButton(new SimpleButtonOverride(leftOffset, buttonFirstRowStart, 60, buttonHeight,
                new TranslatableComponent("gui.titles.random"), button -> chooseRandomTitle()));
        addButton(new SimpleButtonOverride(leftOffset + 180, buttonFirstRowStart, 60, buttonHeight,
                new TranslatableComponent("gui.titles.none"), button -> temporaryTitle = Title.NULL_TITLE));
        addButton(new SimpleButtonOverride(leftOffset + 45, buttonSecondRowStart, 60, buttonHeight,
                new TranslatableComponent("gui.titles.cancel"), button -> exitScreen(false)));
        addButton(new SimpleButtonOverride(leftOffset + 135, buttonSecondRowStart, 60, buttonHeight,
                new TranslatableComponent("gui.titles.confirm"), button -> exitScreen(true)));

        // Page buttons
        backButtons.add(addButton(new SimpleButtonOverride(leftOffset, buttonSecondRowStart, 20, buttonHeight,
                Component.nullToEmpty("<<"), button -> setPage(1))));
        backButtons.add(addButton(new SimpleButtonOverride(leftOffset + 22, buttonSecondRowStart, 20, buttonHeight,
                Component.nullToEmpty("<"), button -> setPage(page - 1))));
        forwardButtons.add(addButton(new SimpleButtonOverride(leftOffset + 198, buttonSecondRowStart, 20, buttonHeight,
                Component.nullToEmpty(">"), button -> setPage(page + 1))));
        forwardButtons.add(addButton(new SimpleButtonOverride(leftOffset + 220, buttonSecondRowStart, 20, buttonHeight,
                Component.nullToEmpty(">>"), button -> setPage(maxPages))));

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
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Renders the background tint
        renderBackground(matrixStack);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, bgTexture);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize, 512, 512);
        // Renders all the buttons (and the search bar since I added it with 'addButton')
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        // Draw the player's name with their selected title
        Component titledPlayerName = TitleManager.getFormattedTitle(temporaryTitle, player.getName(), gender);
        drawCenteredString(matrixStack, this.font, titledPlayerName, this.width / 2, guiTop + 17, 0xFFFFFF);

        // Draw the page counter
        drawCenteredString(matrixStack, this.font, String.format("%s/%s", page, maxPages), this.width / 2, guiTop + 189, 0xFFFFFF);

        if (titlesListFiltered.size() == 0) {
            String emptyText = "gui.titles.titleselection.empty";
            if (titlesListCache.size() > 0) {
                emptyText += ".filter";
            }
            drawCenteredString(matrixStack, this.font, I18n.get(emptyText), this.width / 2, guiTop + 109, 0xFFFFFF);
        }
    }

    protected void exitScreen(boolean update) {
        if (update) {
            TitlesNetwork.toServer(new PacketSyncDisplayTitle(player.getUUID(), temporaryTitle.getID()));
        }
        TitlesNetwork.toServer(new PacketSyncGenderSetting(player.getUUID(), gender));
        onClose();
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
            String s = search.getValue();
            if (super.keyPressed(keyCode, scanCode, modifiers)) {
                if (!Objects.equals(s, search.getValue())) {
                    parseSearch(search.getValue());
                }
            }
            return true;
        }
        else if (Keybinds.openTitleSelection.matches(keyCode, scanCode)
                || getMinecraft().options.keyInventory.matches(keyCode, scanCode)) {
            exitScreen(false);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (search.isFocused()) {
            String s = search.getValue();
            if (search.charTyped(codePoint, modifiers)) {
                if (!Objects.equals(s, search.getValue())) {
                    parseSearch(search.getValue());
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
        // TODO implement a binary search tree to make this better/faster(/stronger)?
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
                    String titleString = TitleManager.getFormattedTitle(t, gender).getString().toLowerCase();
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
            Button button = addButton(new TitleButton(x, y, titleButtonWidth, buttonHeight, titlesToDisplay.get(i),
                    gender, b -> temporaryTitle = ((TitleButton) b).getTitle(), titleWithFlavorText));
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
            temporaryTitle = titlesListFiltered.get(player.level.random.nextInt(titlesListFiltered.size()));
        }
    }
}