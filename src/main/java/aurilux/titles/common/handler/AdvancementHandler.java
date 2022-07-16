package aurilux.titles.common.handler;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.init.ModTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = TitlesMod.MOD_ID)
public class AdvancementHandler {
    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        Advancement advancement = event.getAdvancement();
        TitleManager.unlockTitle((ServerPlayerEntity) event.getPlayer(), advancement.getId());
    }

    @SubscribeEvent
    public static void onArrowHit(LivingDamageEvent event) {
        LivingEntity player = event.getEntityLiving();
        if (!event.getSource().damageType.equals("arrow") || !(player instanceof ServerPlayerEntity)) {
            return;
        }

        if (player.getArrowCountInEntity() >= 7) {
            grantCriterion((ServerPlayerEntity) player, "pincushion");
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        Entity player = event.getEntity();
        if (player instanceof ServerPlayerEntity) {
            Block block = Block.getBlockFromItem(event.getTo().getItem());
            EquipmentSlotType slot = event.getSlot();
            if (block == Blocks.CARVED_PUMPKIN && slot == EquipmentSlotType.HEAD) {
                grantCriterion((ServerPlayerEntity) player, "melon_lord");
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerMount(EntityMountEvent event) {
        Entity mounting = event.getEntityMounting();
        if (mounting instanceof ServerPlayerEntity && event.getEntityBeingMounted() instanceof BoatEntity) {
            grantCriterion((ServerPlayerEntity) mounting, "captain");
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Block placedBlock = event.getPlacedBlock().getBlock();
        Set<ResourceLocation> blockTags = placedBlock.getTags();
        Entity player = event.getEntity();

        boolean beaconBaseAndOpulent = blockTags.contains(ModTags.Blocks.OPULENT.getName())
                && blockTags.contains(BlockTags.BEACON_BASE_BLOCKS.getName());
        if (!(player instanceof ServerPlayerEntity) || (placedBlock != Blocks.BEACON && !beaconBaseAndOpulent)) {
            return;
        }

        if (verifyBeaconLevelAndComposition(event.getPos(), player.world, placedBlock)) {
            grantCriterion((ServerPlayerEntity) player, "opulent");
        }
    }

    private static boolean verifyBeaconLevelAndComposition(BlockPos placedPos, World world, Block placedBlock) {
        BlockPos beaconBlockPos = findBeacon(placedPos, world, placedBlock);
        if (beaconBlockPos == null) {
            return false;
        }

        boolean onlyOpulentBlock = true;
        int levels = 0;
        beaconBaseValidation:
        for(int i = 1; i <= 4; levels = i++) {
            int j = beaconBlockPos.getY() - i;
            if (j < 0) {
                break;
            }

            for(int k = beaconBlockPos.getX() - i; k <= beaconBlockPos.getX() + i; k++) {
                for(int l = beaconBlockPos.getZ() - i; l <= beaconBlockPos.getZ() + i; l++) {
                    if (world.getBlockState(new BlockPos(k, j, l)).getBlock() != placedBlock) {
                        onlyOpulentBlock = false;
                        break beaconBaseValidation;
                    }
                }
            }
        }

        return onlyOpulentBlock && levels == 4;
    }

    private static BlockPos findBeacon(BlockPos placedPos, World world, Block placedBlock) {
        if (placedBlock == Blocks.BEACON) {
            return placedPos;
        }
        else {
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    for (int y = 0; y <= 4; y++) {
                        BlockPos tempPos = placedPos.add(x, y, z);
                        if (world.getBlockState(tempPos).getBlock() == Blocks.BEACON) {
                            return tempPos;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void grantCriterion(ServerPlayerEntity player, String advancementId) {
        PlayerAdvancements advancements = player.getAdvancements();
        AdvancementManager manager = player.getServerWorld().getServer().getAdvancementManager();
        Advancement advancement = manager.getAdvancement(TitlesMod.prefix(advancementId));
        if(advancement != null) {
            advancements.grantCriterion(advancement, "code_triggered");
        }
    }
}