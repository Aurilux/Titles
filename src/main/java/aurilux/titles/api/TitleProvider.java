package aurilux.titles.api;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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