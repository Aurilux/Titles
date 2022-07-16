package aurilux.titles.common.command.argument;

import aurilux.titles.api.Title;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.core.TitleRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TitleArgument implements ArgumentType<Title> {
    private static final List<String> EXAMPLES = Arrays.asList("titles:captain", "titles:opulent", "titles:chicken_chaser");

    public static TitleArgument title() {
        return new TitleArgument();
    }

    @Override
    public Title parse(StringReader reader) throws CommandSyntaxException {
        return TitleManager.getTitle(ResourceLocation.read(reader));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(TitleRegistry.get().getTitles().keySet().stream().map(ResourceLocation::toString), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static <S> Title getTitle(CommandContext<S> context, String name) {
        return context.getArgument(name, Title.class);
    }
}
