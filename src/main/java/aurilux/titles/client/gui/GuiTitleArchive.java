package aurilux.titles.client.gui;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.TitlesImpl;
import aurilux.titles.common.init.ModItems;
import aurilux.titles.common.network.PacketDispatcher;
import aurilux.titles.common.network.messages.PacketSyncFragmentCount;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiTitleArchive extends GuiTitleSelection {
    private final int PURCHASE_BUTTON = maxPerPage + 3;
    private final int CLOSE_BUTTON = maxPerPage + 4;

    private int totalFragCost = 0;

    public GuiTitleArchive(EntityPlayer player) {
        super(player);
        temporaryTitle = TitleInfo.NULL_TITLE;
        calculateTotalFragCost();
    }

    @Override
    public List<TitleInfo> getTitlesList() {
        List<TitleInfo> temp = new ArrayList<>(TitlesAPI.getArchiveTitles().values());
        temp.removeIf(t -> (TitlesAPI.getTitlesCap(player).getObtainedTitles().contains(t)));
        Collections.sort(temp, new TitleInfo.RarityComparator());
        return temp;
    }

    private void calculateTotalFragCost() {
        totalFragCost = 0;
        for (TitleInfo info : titlesList) {
            totalFragCost = totalFragCost + getFragCost(info);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        super.drawScreen(mouseX, mouseY, f);
        this.itemRender.renderItemIntoGUI(new ItemStack(ModItems.archiveFragment), guiLeft + 16, guiTop + 31);
        String fragmentDisplay = "x " + TitlesAPI.getTitlesCap(player).getFragmentCount() + " [" + totalFragCost + "]";
        if (temporaryTitle != TitleInfo.NULL_TITLE) {
            fragmentDisplay += ChatFormatting.RED + " -" + getFragCost(temporaryTitle);
        }
        this.fontRenderer.drawStringWithShadow(fragmentDisplay, guiLeft + 30, guiTop + 35, 0xFFFFFF);
    }

    @Override
    protected String getEmptyMessage() {
        return "gui.titles.titlearchive.empty";
    }

    private void purchaseTitle() {
        TitlesImpl.DefaultImpl titlesImpl = TitlesAPI.getTitlesCap(player);
        if (titlesImpl.getFragmentCount() < getFragCost(temporaryTitle)) {
            return;
        }
        titlesImpl.addFragments(-getFragCost(temporaryTitle));
        TitlesAPI.addTitleToPlayer(player, temporaryTitle.getKey());
        PacketDispatcher.INSTANCE.sendToServer(new PacketSyncUnlockedTitle(temporaryTitle.getKey()));
        PacketDispatcher.INSTANCE.sendToServer(new PacketSyncFragmentCount(titlesImpl.getFragmentCount()));
        titlesList = getTitlesList();
        calculateTotalFragCost();
        temporaryTitle = TitleInfo.NULL_TITLE;
    }

    private int getFragCost(TitleInfo titleInfo) {
        switch (titleInfo.getTitleRarity().ordinal()) {
            case 0: return 1;
            case 1: return 2;
            case 2: return 4;
        }
        return 0;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case PURCHASE_BUTTON: purchaseTitle(); break;
            case CLOSE_BUTTON: exitScreen(false); break;
            case FIRST_PAGE : page = 0; break;
            case PREV_PAGE : page--; break;
            case NEXT_PAGE : page++; break;
            case LAST_PAGE : page = maxPages; break;
            default : temporaryTitle = titlesList.get(button.id + (page * maxPerPage));
        }
        updateButtonList();
    }

    @Override
    protected void addChangeButtons() {
        buttonList.add(new GuiButton(CLOSE_BUTTON, leftOffset + 45, buttonSecondRowStart, 60, buttonHeight, I18n.format("gui.titles.close")));
        buttonList.add(new GuiButton(PURCHASE_BUTTON, leftOffset + 135, buttonSecondRowStart, 60, buttonHeight, I18n.format("gui.titles.purchase")));
    }
}