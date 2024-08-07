package aurilux.titles.client.gui.button;

import aurilux.titles.api.Title;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TitleButton extends SimpleButtonOverride {
    private final Title title;

    public TitleButton(int x, int y, int width, int height, Title t, boolean isMasculine, OnPress action) {
        super(x, y, width, height, t.getTextComponent(isMasculine), action);
        title = t;
    }

    public Title getTitle() {
        return title;
    }

    @Override
    public void renderContents(GuiGraphics graphics) {
        if (StringUtil.isNullOrEmpty(title.getFlavorText())) {
            super.renderContents(graphics);
        }
        else {
            graphics.drawCenteredString(Minecraft.getInstance().font, this.getMessage().copy().withStyle(Style.EMPTY.withUnderlined(true)),
                    getX() + this.width / 2, getY() + (this.height - 8) / 2, getFGColor() | Mth.ceil(this.alpha * 255.0F) << 24);
        }
    }
}
