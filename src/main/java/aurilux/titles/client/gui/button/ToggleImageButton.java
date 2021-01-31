package aurilux.titles.client.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

public class ToggleImageButton extends ImageButton {
    private boolean toggle;

    public ToggleImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yTexDiff, ResourceLocation texture, int texWidth, int texHeight, Button.IPressable action, boolean startValue) {
        super(x, y, width, height, xTexStart, yTexStart, yTexDiff, texture, texWidth, texHeight, action);
        toggle = startValue;
    }

    public void toggle() {
         toggle = !toggle;
    }

    public boolean getValue() {
        return toggle;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft.getInstance().getTextureManager().bindTexture(this.resourceLocation);
        int startY = yTexStart;
        if (toggle) {
            startY += yDiffText;
        }

        RenderSystem.enableDepthTest();
        blit(matrixStack, this.x, this.y, this.xTexStart, startY, this.width, this.height, this.textureWidth, this.textureHeight);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }
}