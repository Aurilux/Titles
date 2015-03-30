package aurilux.titles.common.core;

import net.minecraft.item.EnumRarity;
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
    public String titleId;
    public EnumRarity titleRarity;

    public TitleInfo(String name, EnumRarity rarity) {
        titleId = name;
        titleRarity = rarity;
    }

    public EnumChatFormatting getTitleColor() {
        if (titleRarity == null) {
            return EnumChatFormatting.BLUE;
        }
        return titleRarity.rarityColor;
    }

    public String getFormattedTitle() {
        return getTitleColor() + StatCollector.translateToLocal("title." + titleId);
    }

    public String toString() {
        return titleId + ", " + (titleRarity == null ? "null" : titleRarity.rarityName);
    }
}