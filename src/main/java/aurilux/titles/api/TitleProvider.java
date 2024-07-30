package aurilux.titles.api;

import com.google.common.collect.Sets;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class TitleProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public TitleProvider(PackOutput packOutput) {
        pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "titles");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        var duplicateCheck = Sets.newHashSet();
        var list = new ArrayList<CompletableFuture<?>>();
        registerTitles((title) -> {
            if (!duplicateCheck.add(title.getID())) {
                throw new IllegalStateException("Duplicate title: {} " + title.getID());
            }
            else {
                list.add(DataProvider.saveStable(cache, title.serialize(), pathProvider.json(title.getID())));
            }
        });
        return CompletableFuture.allOf();
    }

    protected abstract void registerTitles(Consumer<Title> consumer);

    @Override
    public String getName() {
        return "Titles Provider";
    }
}