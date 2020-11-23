package aurilux.titles.client.gui.button;

import aurilux.titles.api.Title;
import aurilux.titles.api.TitlesAPI;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class GuiButtonTitle extends Button {
    private Title title;

    public GuiButtonTitle(int x, int y, int width, int height, IPressable action, Title t) {
        super(x, y, width, height, ITextComponent.getTextComponentOrEmpty(TitlesAPI.instance().getFormattedTitle(t, false)), action);
        title = t;
    }

    public Title getTitle() {
        return title;
    }
}
