package aurilux.titles.api;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public abstract class TitleProvider implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected final DataGenerator generator;

    public TitleProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        registerTitles((title) -> {
            if (title.getID() == null || title.getType() == null || title.getRarity() == null || title.getDefaultDisplay() == null) {
                throw new IllegalStateException("One or more mandatory values have not been set! (Either id, type, rarity, or default display)");
            }

            ResourceLocation res = title.getID();
            if (!set.add(res)) {
                throw new IllegalStateException("Duplicate recipe: " + res);
            }
            else {
                saveTitle(cache, title.serializeJSON(),
                        path.resolve(String.format("data/%s/titles/%s.json", res.getNamespace(), res.getPath())));
            }
        });
    }

    private void saveTitle(DirectoryCache cache, JsonElement jsonElement, Path filePath) {
        try {
            IDataProvider.save(GSON, cache, jsonElement, filePath);
        }
        catch (IOException ex) {
            LOGGER.error("Couldn't save title {}", filePath, ex);
        }
    }

    protected abstract void registerTitles(Consumer<Title> consumer);

    @Override
    public String getName() {
        return "Titles Provider";
    }
}