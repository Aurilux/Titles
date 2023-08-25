package aurilux.titles.client.gui.button;

import aurilux.titles.common.TitlesMod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SimpleButtonOverride extends Button {
    protected final ResourceLocation guiLoc = TitlesMod.prefix("textures/gui/title_selection.png");
    protected final int buttonStartY = 220;

    public SimpleButtonOverride(int x, int y, int width, int height, Component title, OnPress pressedAction) {
        super(x, y, width, height, title, pressedAction, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        //Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, guiLoc);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getTextureY();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        blit(matrixStack, getX(), getY(), 0, buttonStartY + (i * 20), this.width / 2, this.height, 512, 512);
        blit(matrixStack, getX() + this.width / 2, getY(), 200 - this.width / 2, buttonStartY + (i * 20), this.width / 2, this.height, 512, 512);
        //this.renderBg(matrixStack, minecraft, mouseX, mouseY);
        renderContents(matrixStack);
    }

    public void renderContents(PoseStack matrixStack) {
        drawCenteredString(matrixStack, Minecraft.getInstance().font, this.getMessage(),
                getX() + this.width / 2, getY() + (this.height - 8) / 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    //Copied from vanilla because for some reason it's private access in AbstractButton
    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return 46 + i * 20;
    }
}