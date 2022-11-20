package aurilux.titles.api;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public abstract class TitleProvider implements DataProvider {
    private final Logger LOGGER = LogManager.getLogger();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;

    public TitleProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(HashCache cache) {
        Set<ResourceLocation> foundTitles = Sets.newHashSet();
        registerTitles((title) -> {
            if (!foundTitles.add(title.getID())) {
                LOGGER.warn("Skipping duplicate title: {}", title.getID());
            }
            else {
                saveTitle(cache, title);
            }
        });
    }

    protected abstract void registerTitles(Consumer<Title> consumer);

    private void saveTitle(HashCache cache, Title title) {
        Path outputFolder = generator.getOutputFolder();
        Path saveFile = outputFolder.resolve(String.format("data/%s/titles/%s.json",
                title.getID().getNamespace(), title.getID().getPath()));
        try {
            DataProvider.save(GSON, cache, title.serialize(), saveFile);
        }
        catch (IOException ex) {
            LOGGER.warn("Unable to save title {}", saveFile, ex);
        }
    }

    @Override
    public String getName() {
        return "Titles Provider";
    }
}