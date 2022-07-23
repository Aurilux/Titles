package aurilux.titles.client.gui.button;

import aurilux.titles.api.Title;
import aurilux.titles.common.core.TitleManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TitleButton extends SimpleButtonOverride {
    private final Title title;

    public TitleButton(int x, int y, int width, int height, Title t, boolean isMasculine, IPressable action, ITooltip tooltip) {
        super(x, y, width, height, TitleManager.getFormattedTitle(t, isMasculine), action, tooltip);
        title = t;
    }

    public Title getTitle() {
        return title;
    }

    @Override
    public void renderContents(MatrixStack matrixStack) {
        if (StringUtils.isNullOrEmpty(title.getFlavorText())) {
            super.renderContents(matrixStack);
        }
        else {
            drawCenteredString(matrixStack, Minecraft.getInstance().fontRenderer, this.getMessage().deepCopy().mergeStyle(Style.EMPTY.setUnderlined(true)),
                    this.x + this.width / 2, this.y + (this.height - 8) / 2, getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
        }
    }
}
