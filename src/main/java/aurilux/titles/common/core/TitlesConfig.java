package aurilux.titles.common.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class TitlesConfig {
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    static {
        final Pair<Client, ForgeConfigSpec> specPairClient = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPairClient.getRight();
        CLIENT = specPairClient.getLeft();

        final Pair<Common, ForgeConfigSpec> specPairCommon = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPairCommon.getRight();
        COMMON = specPairCommon.getLeft();

        final Pair<Server, ForgeConfigSpec> specPairServer = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPairServer.getRight();
        SERVER = specPairServer.getLeft();
    }

    // For client only options. Primarily rendering.
    public static class Client {
        public Client(ForgeConfigSpec.Builder builder) {
            //NOOP
        }
    }

    // For both client and server. These options will be available for single-player and server worlds. Most config
    // options should be here.
    public static class Common {
        public final ForgeConfigSpec.BooleanValue fragmentLoot;
        public final ForgeConfigSpec.BooleanValue nickname;
        public final ForgeConfigSpec.BooleanValue holidayTitles;

        public Common(ForgeConfigSpec.Builder builder) {
            fragmentLoot = builder
                    .comment("Set to false to disable title fragments from generating as loot.")
                    .define("fragmentLoot", true);

            nickname = builder
                    .comment("Set to false to disable players from setting nicknames.")
                    .define("nickname", true);

            holidayTitles = builder
                    .comment("Set to false to prevent holiday starter titles being available")
                    .define("holidayTitles", true);
        }
    }

    // For server only options. These options will not be available on single-player worlds.
    public static class Server {
        public final ForgeConfigSpec.BooleanValue showInTablist;

        public Server(ForgeConfigSpec.Builder builder) {
            showInTablist = builder
                    .comment("Set to false to disable titles from rendering next to player names in the tablist")
                    .define("showInTablist", true);
        }
    }
}