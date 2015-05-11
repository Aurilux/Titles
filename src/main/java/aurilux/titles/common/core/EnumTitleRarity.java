package aurilux.titles.common.core;

import net.minecraft.util.EnumChatFormatting;

/**
 * This class was created by <Aurilux>. It's distributed as part of the Titles Mod.
 * <p/>
 * Titles is Open Source and distributed under the GNU Lesser General Public License v3.0
 * (https://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * File Created @ [06 May 2015]
 */
public enum EnumTitleRarity {
    unique(EnumChatFormatting.BLUE, "unique"),
    common(EnumChatFormatting.WHITE, "common"),
    uncommon(EnumChatFormatting.YELLOW, "uncommon"),
    rare(EnumChatFormatting.AQUA, "rare"),
    epic(EnumChatFormatting.LIGHT_PURPLE, "epic");

    public final EnumChatFormatting rarityColor;
    public final String rarityName;

    EnumTitleRarity(EnumChatFormatting rc, String rn) {
        rarityColor = rc;
        rarityName = rn;
    }
}