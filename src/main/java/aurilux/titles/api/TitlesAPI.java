package aurilux.titles.api;

import aurilux.titles.api.capability.ITitles;
import aurilux.titles.api.handler.DummyMethodHandler;
import aurilux.titles.api.handler.IInternalMethodHandler;
import aurilux.titles.common.impl.TitlesCapImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;

public class TitlesAPI {
    public static final String MOD_ID = "titles";

    public static final Rarity MYTHIC = Rarity.create("MYTHIC", TextFormatting.GOLD);

    @CapabilityInject(ITitles.class)
    public static Capability<ITitles> TITLES_CAPABILITY = null;

    // This allows access to parts of the mod that shouldn't be exposed in an API to prevent abuse. By redirecting certain
    // calls through an internal method handler, it limits what can be exploited by bad actors. Otherwise, there would be
    // many core things, such as network packets and the titles database, that could be manipulated in ways beyond the
    // design and intent of the mod, or ways for them to cheat rewards such as contributor titles.
    private static final LazyValue<IInternalMethodHandler> internalHandler = new LazyValue<>(() -> {
        try {
            return (IInternalMethodHandler) Class.forName("aurilux.titles.common.impl.InternalHandlerImpl").newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find InternalHandlerImpl, using a dummy");
            return new DummyMethodHandler();
        }
    });

    private static IInternalMethodHandler internal() {
        return internalHandler.getValue();
    }

    public static void unlockTitle(ServerPlayerEntity player, ResourceLocation titleKey) {
        internal().unlockTitle(player, titleKey);
    }

    public static Title getTitle(ResourceLocation titleKey) {
        return internal().getTitle(titleKey);
    }

    // Simple utility getter method for obtaining the titles capabilty for a player.
    public static LazyOptional<ITitles> getCapability(PlayerEntity player) {
        return player.getCapability(TitlesAPI.TITLES_CAPABILITY);
    }

    public static void setDisplayTitle(PlayerEntity player, ResourceLocation titleKey) {
        getCapability(player).ifPresent(c -> {
            c.setDisplayTitle(getTitle(titleKey));
        });
    }

    // This overloaded method is used to get the title without the player's name included. Used in the title selection
    // screen for the list of obtained titles the player can choose from.
    public static IFormattableTextComponent getFormattedTitle(Title title, boolean isMasculine) {
        return getFormattedTitle(title, null, isMasculine);
    }

    public static IFormattableTextComponent getFormattedTitle(Title title, PlayerEntity player) {
        return getFormattedTitle(title, player.getName(), getCapability(player).orElse(new TitlesCapImpl()).getGenderSetting());
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