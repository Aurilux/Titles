package aurilux.titles.common.core;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.common.Titles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TitleRegistry {
    public static final TitleRegistry INSTANCE = new TitleRegistry();

    private final Map<String, TitleInfo> titlesMap = new HashMap<>();
    private final Map<String, TitleInfo> archiveTitles = new HashMap<>();
    private volatile Map<String, TitleInfo> contributorTitles = new HashMap<>();
    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .create();
    private static final String DATA_LOCATION = Titles.MOD_ID;

    private TitleRegistry() {}


    public void init() {
        new ThreadContributorLoader();

        List<ModInfo> mods = ModList.get().getMods();
        Map<Pair<ModInfo, ResourceLocation>, String> foundTitles = new HashMap<>();

        mods.forEach(mod -> {
            String id = mod.getModId();
            String filePath = String.format("data/%s/%s", id, DATA_LOCATION);
            Path modSource = mod.getOwningFile().getFile().getFilePath().resolve(filePath);
            if (Files.exists(modSource)) {
                try {
                    Iterator<Path> itr = Files.walk(modSource).iterator();
                    while (itr.hasNext()) {
                        Titles.LOGGER.info(itr.next().getFileName().toString());
                    }
                }
                catch (IOException ex) {
                    Titles.LOGGER.warn("WARNING!");
                }
            }
        });
/*
        foundBooks.forEach((pair, file) -> {
            ModInfo mod = pair.getLeft();
            Optional<? extends ModContainer> container = ModList.get().getModContainerById(mod.getModId());
            container.ifPresent(c -> {
                ResourceLocation res = pair.getRight();

                Class<?> ownerClass = c.getMod().getClass();
                try (InputStream stream = ownerClass.getResourceAsStream(file)) {
                    loadTitles(mod, ownerClass, res, stream, false);
                } catch (Exception e) {
                    Debug.LOGGER.error("Failed to load book {} defined by mod {}, skipping",
                            res, c.getModInfo().getModId(), e);
                }
            });
        });*/
    }

    public boolean contributorTitleExists(String playerName) {
        return contributorTitles.containsKey(playerName);
    }

    public TitleInfo getContributorTitle(String playerName) {
        return contributorTitles.get(playerName);
    }

    public TitleInfo getTitleFromKey(String key) {
        for (TitleInfo titleInfo : contributorTitles.values()) {
            if (titleInfo.getKey().equals(key)) {
                return titleInfo;
            }
        }
        return TitleInfo.NULL_TITLE;
    }

    private void load(Properties props) {
        for(String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            contributorTitles.put(key, new TitleInfo(value, TitleInfo.TitleRarity.UNIQUE));
        }
    }

    private class ThreadContributorLoader extends Thread {
        private ThreadContributorLoader() {
            setName("Titles Contributor Loader");
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Aurilux/Titles/master/contributors.properties");
                Properties props = new Properties();
                InputStreamReader reader = new InputStreamReader(url.openStream());
                props.load(reader);
                load(props);
            }
            catch (IOException e) {
                Titles.LOGGER.debug("Unable to load contributors list. Most likely you're offline or github is down.");
            }
        }
    }
/*
    public void loadBook(IModInfo mod, Class<?> ownerClass, ResourceLocation res, InputStream stream,
                         boolean external) {
        Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        Book book = GSON.fromJson(reader, Book.class);
        book.build(mod, ownerClass, res, external);
        books.put(res, book);
    }

    // HELPER

    public static boolean findFiles(ModInfo mod, String base, Function<Path, Boolean> preprocessor,
                                    BiFunction<Path, Path, Boolean> processor, boolean defaultUnfoundRoot, boolean visitAllFiles) {
        if (mod.getModId().equals("minecraft") || mod.getModId().equals("forge"))
            return false;

        Path source = mod.getOwningFile().getFile().getFilePath();

        FileSystem fs = null;
        boolean success = true;

        try {
            Path root = null;

            if (Files.isRegularFile(source)) {
                fs = FileSystems.newFileSystem(source, null);
                root = fs.getPath("/" + base);
            } else if (Files.isDirectory(source))
                root = source.resolve(base);

            if (root == null || !Files.exists(root))
                return defaultUnfoundRoot;

            if (preprocessor != null) {
                Boolean cont = preprocessor.apply(root);
                if (cont == null || !cont)
                    return false;
            }

            if (processor != null) {
                Iterator<Path> itr = Files.walk(root).iterator();

                while (itr.hasNext()) {
                    Boolean cont = processor.apply(root, itr.next());

                    if (visitAllFiles)
                        success &= cont != null && cont;
                    else if (cont == null || !cont)
                        return false;
                }
            }
        } catch(IOException ex) {
            throw new UncheckedIOException(ex);
        } finally {
            IOUtils.closeQuietly(fs);
        }

        return success;
    }
    */
}