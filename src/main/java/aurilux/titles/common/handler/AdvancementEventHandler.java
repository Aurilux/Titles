package aurilux.titles.common.handler;

import aurilux.titles.common.TitlesMod;
import aurilux.titles.common.core.TitleManager;
import aurilux.titles.common.init.ModTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TitlesMod.MOD_ID)
public class AdvancementEventHandler {
    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        Advancement advancement = event.getAdvancement();
        TitleManager.unlockTitle((ServerPlayer) event.getEntity(), advancement.getId());
    }

    @SubscribeEvent
    public static void onEntityDamage(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (!(target instanceof ServerPlayer)) {
            return;
        }

        if (event.getSource().msgId.equals("arrow") && target.getArrowCount() >= 7) {
            grantCriterion((ServerPlayer) target, "pincushion");
        }
        else if (event.getSource() == DamageSource.FREEZE) {
            grantCriterion((ServerPlayer) target, "frigid");
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        Entity player = event.getEntity();
        if (player instanceof ServerPlayer) {
            Block block = Block.byItem(event.getTo().getItem());
            EquipmentSlot slot = event.getSlot();
            if (block == Blocks.CARVED_PUMPKIN && slot == EquipmentSlot.HEAD) {
                grantCriterion((ServerPlayer) player, "melon_lord");
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerMount(EntityMountEvent event) {
        Entity mounting = event.getEntityMounting();
        if (mounting instanceof ServerPlayer && event.getEntityBeingMounted() instanceof Boat) {
            grantCriterion((ServerPlayer) mounting, "captain");
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Block placedBlock = event.getPlacedBlock().getBlock();
        Entity player = event.getEntity();
        boolean beaconBaseAndOpulent = placedBlock.defaultBlockState().is(ModTags.Blocks.OPULENT)
                && placedBlock.defaultBlockState().is(BlockTags.BEACON_BASE_BLOCKS);
        if (!(player instanceof ServerPlayer) || (placedBlock != Blocks.BEACON && !beaconBaseAndOpulent)) {
            return;
        }

        if (verifyBeaconLevelAndComposition(event.getPos(), player.level, placedBlock)) {
            grantCriterion((ServerPlayer) player, "opulent");
        }
    }

    private static boolean verifyBeaconLevelAndComposition(BlockPos placedPos, Level world, Block placedBlock) {
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

    private static BlockPos findBeacon(BlockPos placedPos, Level world, Block placedBlock) {
        if (placedBlock == Blocks.BEACON) {
            return placedPos;
        }
        else {
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    for (int y = 0; y <= 4; y++) {
                        BlockPos tempPos = placedPos.offset(x, y, z);
                        if (world.getBlockState(tempPos).getBlock() == Blocks.BEACON) {
                            return tempPos;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void grantCriterion(ServerPlayer player, String advancementId) {
        PlayerAdvancements advancements = player.getAdvancements();
        ServerAdvancementManager manager = player.getLevel().getServer().getAdvancements();
        Advancement advancement = manager.getAdvancement(TitlesMod.prefix(advancementId));
        if(advancement != null) {
            advancements.award(advancement, "code_triggered");
        }
    }
}