package aurilux.titles.common.init;

import aurilux.titles.api.TitleInfo;
import aurilux.titles.common.Titles;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ContributorLoader {
    private volatile static Map<String, TitleInfo> contributorTitles = new HashMap<>();

    public static void init() {
        new ThreadContributorLoader();
    }

    public static boolean contributorTitleExists(String playerName) {
        return contributorTitles.containsKey(playerName);
    }

    public static TitleInfo getContributorTitle(String playerName) {
        return contributorTitles.get(playerName);
    }

    public static TitleInfo getTitleFromKey(String key) {
        for (TitleInfo titleInfo : contributorTitles.values()) {
            if (titleInfo.getKey().equals(key)) {
                return titleInfo;
            }
        }
        return TitleInfo.NULL_TITLE;
    }

    private static void load(Properties props) {
        for(String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            contributorTitles.put(key, new TitleInfo(value, TitleInfo.TitleRarity.UNIQUE));
        }
    }

    private static class ThreadContributorLoader extends Thread {
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
                Titles.console("Unable to load contributors list. Most likely you're offline or github is down.");
            }
        }
    }
}
