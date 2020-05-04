package aurilux.titles.common.handler;

import aurilux.titles.common.Titles;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LootHandler {
    @SubscribeEvent
    public static void loadLoot(LootTableLoadEvent event) {
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
        return LootPool.builder()
                .addEntry(getInjectEntry(entryName))
                .bonusRolls(0, 1)
                .name("titles_inject")
                .build();
    }

    private static LootEntry.Builder getInjectEntry(String name) {
        ResourceLocation table = new ResourceLocation(Titles.MOD_ID, "inject/" + name);
        return TableLootEntry.builder(table) .weight(1);
    }
}