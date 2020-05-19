package aurilux.titles.client.gui.button;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.gui.widget.button.Button;

public class GuiButtonTitle extends Button {
    private TitleInfo titleInfo;

    public GuiButtonTitle(int x, int y, int width, int height, IPressable action, TitleInfo t) {
        super(x, y, width, height, TitlesAPI.instance().getFormattedTitle(t, false), action);
        titleInfo = t;
    }

    public TitleInfo getTitle() {
        return titleInfo;
    }
}
