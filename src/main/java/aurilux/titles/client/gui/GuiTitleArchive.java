package aurilux.titles.client.gui;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.common.item.ModItems;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiTitleArchive extends GuiTitleSelection {
    private int totalFragCost = 0;

    public GuiTitleArchive(PlayerEntity player) {
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
    public void render(int mouseX, int mouseY, float f) {
        super.render(mouseX, mouseY, f);
        this.itemRenderer.renderItemIntoGUI(new ItemStack(ModItems.archiveFragment), guiLeft + 16, guiTop + 31);
        String fragmentDisplay = "x " + TitlesAPI.getTitlesCap(player).getFragmentCount() + " [" + totalFragCost + "]";
        if (temporaryTitle != TitleInfo.NULL_TITLE) {
            fragmentDisplay += ChatFormatting.RED + " -" + getFragCost(temporaryTitle);
        }
        this.font.drawStringWithShadow(fragmentDisplay, guiLeft + 30, guiTop + 35, 0xFFFFFF);
    }

    @Override
    protected String getEmptyMessage() {
        return I18n.format("gui.titles.titlearchive.empty");
    }

    private void purchaseTitle() {
        ITitles titlesImpl = TitlesAPI.getTitlesCap(player);
        if (titlesImpl.getFragmentCount() < getFragCost(temporaryTitle)) {
            return;
        }
        titlesImpl.addFragments(-getFragCost(temporaryTitle));
        TitlesAPI.addTitleToPlayer(player, temporaryTitle.getKey());
        //TODO update when network is updated
        //PacketDispatcher.INSTANCE.sendToServer(new PacketSyncUnlockedTitle(temporaryTitle.getKey()));
        //PacketDispatcher.INSTANCE.sendToServer(new PacketSyncFragmentCount(titlesImpl.getFragmentCount()));
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
    protected void addChangeButtons() {
        buttons.add(new Button(leftOffset + 45, buttonSecondRowStart, 60, buttonHeight,
                I18n.format("gui.titles.close"), button -> exitScreen(false)));
        buttons.add(new Button(leftOffset + 135, buttonSecondRowStart, 60, buttonHeight,
                I18n.format("gui.titles.purchase"), button -> purchaseTitle()));
    }
}