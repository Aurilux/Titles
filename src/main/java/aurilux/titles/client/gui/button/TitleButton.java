package aurilux.titles.client.gui.button;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.gui.widget.button.Button;

public class TitleButton extends Button {
    private final Title title;

    public TitleButton(int x, int y, int width, int height, IPressable action, Title t, boolean isMasculine) {
        super(x, y, width, height, TitlesAPI.getFormattedTitle(t, isMasculine), action);
        title = t;
    }

    public Title getTitle() {
        return title;
    }
}
