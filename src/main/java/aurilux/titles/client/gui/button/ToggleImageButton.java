package aurilux.titles.client.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ToggleImageButton extends ImageButton {
    private boolean toggle;

    public ToggleImageButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yTexDiff, ResourceLocation texture, int texWidth, int texHeight, Button.OnPress action, boolean startValue) {
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
    public void renderWidget(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, this.resourceLocation);
        int startX = xTexStart;
        if (toggle) {
            // Just for simplicity's sake, using the yDiffText for the x-axis
            startX += yDiffTex;
        }
        RenderSystem.enableDepthTest();

        int i = this.getTextureY();
        blit(matrixStack, getX(), getY(), startX, this.yTexStart + (i * 20), this.width, this.height, this.textureWidth, this.textureHeight);
    }

    //Copied from vanilla because for some reason it's private access in AbstractButton
    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return i;
    }
}