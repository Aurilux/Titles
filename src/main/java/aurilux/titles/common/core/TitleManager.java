package aurilux.titles.common.core;

import aurilux.titles.api.Title;
import aurilux.titles.common.network.TitlesNetwork;
import aurilux.titles.common.network.messages.PacketSyncUnlockedTitle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;

import java.util.Map;
import java.util.stream.Collectors;

public class TitleManager {
    public static void unlockTitle(ServerPlayer player, ResourceLocation titleKey) {
        doIfPresent(player, cap -> {
            if (cap.add(getTitle(titleKey))) {
                TitlesNetwork.toPlayer(new PacketSyncUnlockedTitle(titleKey), player);
            }
        });
    }

    public static Map<ResourceLocation, Title> getAllTitles() {
        return flatten(TitleRegistry.get().getTitles());
    }

    public static Map<ResourceLocation, Title> getAllObtainableTitles() {
        return getAllTitlesExcept(Title.AwardType.STARTING, Title.AwardType.CONTRIBUTOR);
    }

    public static Map<ResourceLocation, Title> getAllTitlesExcept(Title.AwardType... awardType) {
        Map<Title.AwardType, Map<ResourceLocation, Title>> temp = TitleRegistry.get().getTitles();
        for (Title.AwardType t : awardType) {
            temp.remove(t);
        }
        return flatten(temp);
    }

    public static Map<ResourceLocation, Title> getTitlesOfType(Title.AwardType... awardType) {
        Map<Title.AwardType, Map<ResourceLocation, Title>> temp = TitleRegistry.get().getTitles();
        Map<Title.AwardType, Map<ResourceLocation, Title>> holder = TitleRegistry.get().getTitles();
        for (Title.AwardType t : awardType) {
            holder.put(t, temp.get(t));
        }
        return flatten(holder);
    }

    private static Map<ResourceLocation, Title> flatten(Map<Title.AwardType, Map<ResourceLocation, Title>> mapToFlatten) {
        return mapToFlatten.values().stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static void setDisplayTitle(Player player, ResourceLocation id) {
        doIfPresent(player, cap -> {
            cap.setDisplayTitle(getTitle(id));
            player.refreshDisplayName();

            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).refreshTabListName();
            }
        });
    }

    public static void setNickname(Player player, String nickname) {
        doIfPresent(player, cap -> {
            cap.setNickname(nickname);
            player.refreshDisplayName();
        });
    }

    public static Title getTitle(String id) {
        return getTitle(new ResourceLocation(id));
    }

    public static Title getTitle(ResourceLocation id) {
        return TitleManager.getAllTitles().getOrDefault(id, Title.NULL_TITLE);
    }

    public static void doIfPresent(Player player, NonNullConsumer<TitlesCapability> toDo) {
        getCapability(player).ifPresent(toDo);
    }

    public static LazyOptional<TitlesCapability> getCapability(Player player) {
        return player.getCapability(TitlesCapability.TITLES_CAPABILITY);
    }

    public static MutableComponent getFormattedDisplayName(Title title, Player player, TitlesCapability cap) {
        return getFormattedDisplayName(title, player, cap, cap.getGenderSetting());
    }

    // Just used in the TitleSelectionScreen to get the display name for when the gender setting is changed
    public static MutableComponent getFormattedDisplayName(Title title, Player player, TitlesCapability cap, boolean genderSetting) {
        MutableComponent titleComponent = title.getTextComponent(genderSetting);
        Component playerName = cap.getNickname().isEmpty() ? player.getName() : Component.literal(cap.getNickname());
        if (title.isNull()) {
            return playerName.copy();
        }
        else if (title.isPrefix()) {
            return Component.literal("").append(titleComponent).append(" ").append(playerName.copy());
        }
        else {
            return playerName.copy().append(", ").append(titleComponent);
        }
    }
}
