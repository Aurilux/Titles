package aurilux.titles.api;

import aurilux.titles.api.capability.ITitles;
import aurilux.titles.api.handler.DummyMethodHandler;
import aurilux.titles.api.handler.IInternalMethodHandler;
import aurilux.titles.common.impl.TitlesCapImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Rarity;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;

public class TitlesAPI {
    public static final String MOD_ID = "titles";

    @CapabilityInject(ITitles.class)
    public static Capability<ITitles> TITLES_CAPABILITY = null;

    private static final LazyValue<IInternalMethodHandler> internalHandler = new LazyValue<>(() -> {
        try {
            return (IInternalMethodHandler) Class.forName("aurilux.titles.common.impl.InternalHandlerImpl").newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find InternalHandlerImpl, using a dummy");
            return new DummyMethodHandler();
        }
    });

    public static IInternalMethodHandler internal() {
        return internalHandler.getValue();
    }

    public static void registerCommonTitles(String modId, String... titles) {
        registerTitles(modId, Rarity.COMMON, titles);
    }

    public static void registerUncommonTitles(String modId, String... titles) {
        registerTitles(modId, Rarity.UNCOMMON, titles);
    }

    public static void registerRareTitles(String modId, String... titles) {
        registerTitles(modId, Rarity.RARE, titles);
    }

    private static void registerTitles(String modId, Rarity rarity, String... titles) {
        String verifiedModId = modId == null || modId.isEmpty() ? TitlesAPI.MOD_ID : modId;
        for (String title : titles) {
            internal().registerTitle(rarity, verifiedModId + ":" + title);
        }
    }

    public static LazyOptional<ITitles> getCapability(PlayerEntity player) {
        return player.getCapability(TitlesAPI.TITLES_CAPABILITY);
    }

    public static void setDisplayTitle(PlayerEntity player, String titleKey) {
        getCapability(player).ifPresent(c -> {
            c.setDisplayTitle(internal().getTitle(titleKey));
        });
    }

    public static ITextComponent getFormattedTitle(Title title, boolean isMasculine) {
        return getFormattedTitle(title, null, isMasculine);
    }

    public static ITextComponent getFormattedTitle(Title title, PlayerEntity player) {
        return getFormattedTitle(title, player.getName(), getCapability(player).orElse(new TitlesCapImpl()).getGenderSetting());
    }

    public static ITextComponent getFormattedTitle(Title title, ITextComponent playerName, boolean isMasculine) {
        ITextComponent titleComponent = title.getComponent(isMasculine);
        if (playerName == null) {
            return titleComponent;
        }
        else {
            if (title.isNull()) {
                return playerName;
            }
            else {
                return playerName.deepCopy().appendString(", ").append(titleComponent);
            }
        }
    }
}