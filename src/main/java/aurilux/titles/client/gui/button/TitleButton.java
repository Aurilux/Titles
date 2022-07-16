package aurilux.titles.client.gui.button;

import aurilux.titles.api.Title;
import aurilux.titles.common.core.TitleManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TitleButton extends SimpleButtonOverride {
    private final Title title;

    public TitleButton(int x, int y, int width, int height, IPressable action, Title t, boolean isMasculine) {
        super(x, y, width, height, TitleManager.getFormattedTitle(t, isMasculine), action);
        title = t;
    }

    public Title getTitle() {
        return title;
    }
}
