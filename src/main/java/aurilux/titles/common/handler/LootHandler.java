package aurilux.titles.common.handler;

import aurilux.titles.common.Titles;
import aurilux.titles.common.init.ModConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID)
public class LootHandler {
    @SubscribeEvent
    public static void loadLoot(LootTableLoadEvent event) {
        if (!ModConfig.addFragmentsToLoot) {
            return;
        }

        String prefix = "minecraft:chests/";
        String name = event.getName().toString();

        if (name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            switch (file) {
                case "spawn_bonus_chest":
                case "simple_dungeon":
                case "stronghold_corridor":
                case "stronghold_crossing":
                case "stronghold_library": event.getTable().addPool(getInjectPool(file)); break;
                default: break;
            }
        }
    }

    private static LootPool getInjectPool(String entryName) {
        return new LootPool(new LootEntry[] { getInjectEntry(entryName) }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0, 1), "titles_inject_pool");
    }

    private static LootEntry getInjectEntry(String name) {
        return new LootEntryTable(new ResourceLocation(Titles.MOD_ID, "inject/" + name), 1, 0, new LootCondition[0], "titles_inject_entry");
    }
}