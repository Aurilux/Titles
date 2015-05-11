package aurilux.titles.common.core;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [13 Mar 2015]
 */
public class TitleInfo {
    public static final TitleInfo NULL_TITLE = new TitleInfo("null", EnumTitleRarity.unique);
    public String titleId;
    public EnumTitleRarity titleRarity;

    public TitleInfo() {}

    public TitleInfo(String name, EnumTitleRarity rarity) {
        titleId = name;
        titleRarity = rarity;
    }

    public EnumChatFormatting getTitleColor() {
        return titleRarity.rarityColor;
    }

    public String getFormattedTitle() {
        return getTitleColor() + StatCollector.translateToLocal("title." + titleId);
    }

    @Override
    public String toString() {
        return titleId + "," + titleRarity.rarityName;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TitleInfo && ((TitleInfo) o).titleId.equals(this.titleId) && ((TitleInfo) o).titleRarity.equals(this.titleRarity);
    }
}