package aurilux.titles.common.handler;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.entity.merchant.villager.TitleForEmeraldsAndFragmentsTrade;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Rarity;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;

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
        ResourceLocation table = TitlesMod.prefix("inject/" + entryName);
        LootEntry.Builder<?> entry = TableLootEntry.builder(table).weight(1);
        return LootPool.builder()
                .addEntry(entry)
                .bonusRolls(0, 1)
                .name("titles_inject")
                .build();
    }

    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType().equals(VillagerProfession.LIBRARIAN)) {
            event.getTrades().get(1).add(new TitleForEmeraldsAndFragmentsTrade(Rarity.COMMON, 3, 5));
            event.getTrades().get(3).add(new TitleForEmeraldsAndFragmentsTrade(Rarity.UNCOMMON, 2, 10));
            event.getTrades().get(5).add(new TitleForEmeraldsAndFragmentsTrade(Rarity.RARE, 1, 30));
        }
    }
}