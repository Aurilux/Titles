package aurilux.titles.common.command.argument;

import aurilux.titles.api.Title;
import aurilux.titles.client.ClientOnlyMethods;
import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.core.TitleRegistry;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class TitleArgument implements ArgumentType<Title> {
    private static final List<String> EXAMPLES = Arrays.asList("titles:captain", "titles:opulent", "titles:chicken_chaser");
    public static final Predicate<Title> ANY = t -> !t.getType().equals(Title.AwardType.CONTRIBUTOR);
    public static final Predicate<Title> DISPLAY = t -> {
        boolean[] test = {false};
        Player player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientOnlyMethods::getPlayer);
        TitleManager.doIfPresent(player, cap -> {
            test[0] = TitlesMod.prefix(player.getName().getString().toLowerCase()).equals(t.getID())
                    || cap.getObtainedTitles().contains(t);
        });
        return test[0];
    };
    private final Predicate<Title> predicate;

    private TitleArgument(Predicate<Title> p) {
        predicate = p;
    }

    public static TitleArgument any() {
        return new TitleArgument(ANY);
    }

    public static TitleArgument display() {
        return new TitleArgument(DISPLAY);
    }

    public Predicate<Title> getPredicate() {
        return predicate;
    }

    @Override
    public Title parse(StringReader reader) throws CommandSyntaxException {
        return TitleManager.getTitle(ResourceLocation.read(reader));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(TitleRegistry.get().getTitles().values().stream()
                .filter(predicate)
                .map(t -> t.getID().toString()), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static <S> Title getTitle(CommandContext<S> context, String name) {
        return context.getArgument(name, Title.class);
    }

    public static class Info implements ArgumentTypeInfo<TitleArgument, TitleArgument.Info.Template> {
        public void serializeToNetwork(Info.Template argument, FriendlyByteBuf byteBuf) {
            byteBuf.writeUtf(argument.getPredicate().equals(TitleArgument.ANY) ? "any" : "display");
        }

        public TitleArgument.Info.Template deserializeFromNetwork(FriendlyByteBuf byteBuf) {
            if ("any".equals(byteBuf.readUtf())) {
                return new TitleArgument.Info.Template(TitleArgument.ANY);
            }
            return new TitleArgument.Info.Template(TitleArgument.DISPLAY);
        }

        @Override
        public void serializeToJson(Template argument, JsonObject json) {
            json.addProperty("type", argument.getPredicate().equals(TitleArgument.ANY) ? "any" : "display");
        }

        @Override
        public Template unpack(TitleArgument argument) {
            return new TitleArgument.Info.Template(argument.getPredicate());
        }

        public final class Template implements ArgumentTypeInfo.Template<TitleArgument> {
            private final Predicate<Title> predicate;

            Template(Predicate<Title> p) {
                predicate = p;
            }

            public TitleArgument instantiate(CommandBuildContext p_231294_) {
                return new TitleArgument(predicate);
            }

            public ArgumentTypeInfo<TitleArgument, ?> type() {
                return Info.this;
            }

            public Predicate<Title> getPredicate() {
                return predicate;
            }
        }
    }
}