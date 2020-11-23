package aurilux.titles.api;

import aurilux.titles.api.capability.TitlesCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;

public interface TitlesAPI {
    LazyValue<TitlesAPI> INSTANCE = new LazyValue<>(() -> {
        try {
            return (TitlesAPI) Class.forName("aurilux.titles.common.impl.TitlesAPIImpl").newInstance();
        } catch (ReflectiveOperationException e) {
            LogManager.getLogger().warn("Unable to find TitlesAPIImpl, using a dummy");
            return new TitlesAPI() {};
        }
    });

    static TitlesAPI instance() {
        return INSTANCE.getValue();
    }

    default void unlockTitle(ServerPlayerEntity player, String titleKey) {
    }

    default void setDisplayTitle(PlayerEntity player, String titleKey) {
    }

    default boolean titleExists(String titleId) {
        return false;
    }

    default Title getTitle(String titleId) {
        return Title.NULL_TITLE;
    }

    default LazyOptional<TitlesCapability> getCapability(PlayerEntity player) {
        return null;
    }

    default String getFormattedTitle(Title title, boolean addComma) {
        return "";
    }
}