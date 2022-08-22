package aurilux.titles.client.gui.button;

import aurilux.titles.common.TitlesMod;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SimpleButtonOverride extends Button {
    protected final ResourceLocation guiLoc = TitlesMod.prefix("textures/gui/title_selection.png");
    protected final int buttonStartY = 220;

    public SimpleButtonOverride(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction) {
        super(x, y, width, height, title, pressedAction);
    }

    public SimpleButtonOverride(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, ITooltip onTooltip) {
        super(x, y, width, height, title, pressedAction, onTooltip);
    }

    @Override
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(guiLoc);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        blit(matrixStack, this.x, this.y, 0, buttonStartY + (i * 20), this.width / 2, this.height, 512, 512);
        blit(matrixStack, this.x + this.width / 2, this.y, 200 - this.width / 2, buttonStartY + (i * 20), this.width / 2, this.height, 512, 512);
        this.renderBg(matrixStack, minecraft, mouseX, mouseY);
        renderContents(matrixStack);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

    public void renderContents(MatrixStack matrixStack) {
        drawCenteredString(matrixStack, Minecraft.getInstance().fontRenderer, this.getMessage(),
                this.x + this.width / 2, this.y + (this.height - 8) / 2, getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}