package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;

import java.util.Map;
import java.util.stream.Collectors;

public class TitleManager {
    public static void unlockTitle(ServerPlayerEntity player, ResourceLocation titleKey) {
        doIfPresent(player, cap -> {
            if (cap.add(getTitle(titleKey))) {
                TitlesNetwork.toPlayer(new PacketSyncUnlockedTitle(titleKey), player);
            }
        });
    }

    public static Map<ResourceLocation, Title> getTitlesOfType(Title.AwardType awardType) {
        return TitleRegistry.get().getTitles().entrySet().stream()
                .filter(entry -> entry.getValue().getType().equals(awardType))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    public static void setDisplayTitle(ServerPlayerEntity player, ResourceLocation id) {
        doIfPresent(player, cap -> {
            cap.setDisplayTitle(getTitle(id));
        });
    }

    public static Title getTitle(String id) {
        return getTitle(new ResourceLocation(id));
    }

    public static Title getTitle(ResourceLocation id) {
        return TitleRegistry.get().getTitles().getOrDefault(id, Title.NULL_TITLE);
    }

    public static void doIfPresent(PlayerEntity player, NonNullConsumer<TitlesCapability> toDo) {
        getCapability(player).ifPresent(toDo);
    }

    public static LazyOptional<TitlesCapability> getCapability(PlayerEntity player) {
        return player.getCapability(TitlesCapability.TITLES_CAPABILITY);
    }

    public static IFormattableTextComponent getFormattedTitle(Title title, boolean isMasculine) {
        return getFormattedTitle(title, null, isMasculine);
    }

    public static IFormattableTextComponent getFormattedTitle(Title title, PlayerEntity player) {
        return getFormattedTitle(title, player.getName(), getCapability(player).orElse(new TitlesCapability()).getGenderSetting());
    }

    public static IFormattableTextComponent getFormattedTitle(Title title, ITextComponent playerName, boolean isMasculine) {
        IFormattableTextComponent titleComponent = title.getTextComponent(isMasculine);
        if (playerName == null) {
            return titleComponent;
        }
        else {
            if (title.isNull()) {
                return playerName.deepCopy();
            }
            else {
                return playerName.deepCopy().appendString(", ").appendSibling(titleComponent);
            }
        }
    }
}
