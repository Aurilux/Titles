package aurilux.titles.client.gui;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.capability.ITitles;
import aurilux.titles.common.core.TitleRegistry;
import aurilux.titles.common.item.ModItems;
import aurilux.titles.common.network.PacketHandler;
import aurilux.titles.common.network.messages.PacketSyncFragmentCount;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
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

    public GuiTitleArchive(PlayerEntity player, ITitles cap) {
        super(player, cap);
        temporaryTitle = TitleInfo.NULL_TITLE;
        calculateTotalFragCost();
    }

    @Override
    public List<TitleInfo> getTitlesList() {
        List<TitleInfo> temp = new ArrayList<>(TitleRegistry.INSTANCE.getArchiveTitles().values());
        temp.removeIf(t -> (playerCapability.getUnlockedTitles().contains(t)));
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
        String fragmentDisplay = "x " + playerCapability.getFragmentCount() + " [" + totalFragCost + "]";
        if (!temporaryTitle.isNull()) {
            fragmentDisplay += ChatFormatting.RED + " -" + getFragCost(temporaryTitle);
        }
        this.font.drawStringWithShadow(fragmentDisplay, guiLeft + 30, guiTop + 35, 0xFFFFFF);
    }

    @Override
    protected String getEmptyMessage() {
        return I18n.format("gui.titles.titlearchive.empty");
    }

    private void purchaseTitle() {
        if (playerCapability.getFragmentCount() < getFragCost(temporaryTitle)) {
            return;
        }
        playerCapability.addFragments(-getFragCost(temporaryTitle));
        playerCapability.add(temporaryTitle);
        PacketHandler.sendToServer(new PacketSyncUnlockedTitle(temporaryTitle.getKey()));
        PacketHandler.sendToServer(new PacketSyncFragmentCount(playerCapability.getFragmentCount()));
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