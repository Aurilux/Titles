package aurilux.titles.client.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
        int startX = xTexStart;
        if (toggle) {
            // Just for simplicity's sake, using the yDiffText for the x-axis
            startX += yDiffText;
        }
        int i = this.getYImage(this.isHovered());

        RenderSystem.enableDepthTest();
        blit(matrixStack, this.x, this.y, startX, this.yTexStart + (i * 20), this.width, this.height, this.textureWidth, this.textureHeight);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }
}