package aurilux.titles.common.handler;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;

public class LootHandler {
    public static void addLoot(LootTableLoadEvent event) {
        String prefix = "minecraft:chests/";
        String name = event.getName().toString();

        if (name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            switch (file) {
                case "simple_dungeon":
                case "stronghold_corridor":
                case "stronghold_crossing":
                case "stronghold_library": event.getTable().addPool(getInjectPool(file)); break;
            }
        }
    }

    private static LootPool getInjectPool(String entryName) {
        ResourceLocation table = new ResourceLocation(TitlesAPI.MOD_ID, "inject/" + entryName);
        LootEntry.Builder<?> entry = TableLootEntry.builder(table).weight(1);
        return LootPool.builder()
                .addEntry(entry)
                .bonusRolls(0, 1)
                .name("titles_inject")
                .build();
    }
}