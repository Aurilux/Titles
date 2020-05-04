package aurilux.titles.common.command.argument;

import aurilux.titles.api.TitleInfo;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class TitleArgument implements ArgumentType<TitleInfo> {
    @Override
    public TitleInfo parse(StringReader reader) throws CommandSyntaxException {
        return null;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return null;
    }

    @Override
    public Collection<String> getExamples() {
        return null;
    }

    public static <S> TitleInfo getTitle(CommandContext<S> context, String name) {
        return context.getArgument(name, TitleInfo.class);
    }
}
