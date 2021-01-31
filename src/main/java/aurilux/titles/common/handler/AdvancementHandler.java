package aurilux.titles.common.handler;

import aurilux.titles.api.TitlesAPI;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
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

@Mod.EventBusSubscriber(modid = TitlesAPI.MOD_ID)
public class AdvancementHandler {
    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        Advancement advancement = event.getAdvancement();
        TitlesAPI.internal().unlockTitle(player, advancement.getId().toString());
    }

    @SubscribeEvent
    public static void onArrowHit(LivingDamageEvent event) {
        if (!event.getSource().damageType.equals("arrow") || !(event.getEntity() instanceof ServerPlayerEntity)) {
            return;
        }

        if (event.getEntityLiving().getArrowCountInEntity() >= 7) {
            grantCriterion((ServerPlayerEntity) event.getEntity(), "pincushion");
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
            Block block = Block.getBlockFromItem(event.getTo().getItem());
            EquipmentSlotType slot = event.getSlot();
            if (block == Blocks.CARVED_PUMPKIN && slot == EquipmentSlotType.HEAD) {
                grantCriterion(player, "melon_lord");
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerMount(EntityMountEvent event) {
        Entity mounted = event.getEntityBeingMounted();
        Entity mounting = event.getEntityMounting();
        if (mounted instanceof BoatEntity && mounting instanceof PlayerEntity) {
            grantCriterion((ServerPlayerEntity) mounting, "captain");
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Block placedBlock = event.getPlacedBlock().getBlock();
        Set<ResourceLocation> blockTags = placedBlock.getTags();
        ResourceLocation opulentTag = new ResourceLocation(TitlesAPI.MOD_ID, "opulent");
        ResourceLocation beaconBaseTag = new ResourceLocation("minecraft", "beacon_base_blocks");
        boolean beaconBaseAndOpulent = blockTags.contains(opulentTag) && blockTags.contains(beaconBaseTag);
        if (placedBlock != Blocks.BEACON && !beaconBaseAndOpulent) {
            return;
        }

        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        if (player == null) {
            return;
        }

        World world = player.world;
        BlockPos beaconBlockPos = null;

        if (placedBlock == Blocks.BEACON) {
            beaconBlockPos = event.getPos();
        }
        else {
            // The placed block was an opulent block, so find the nearest beacon
            BlockPos placedPos = event.getPos();
            beaconSearch:
            for (int x = -4; x <= 4; x++) {
                for (int z = -4; z <= 4; z++) {
                    for (int y = 0; y <= 4; y++) {
                        BlockPos tempPos = placedPos.add(x, y, z);
                        if (world.getBlockState(tempPos).getBlock() == Blocks.BEACON) {
                            beaconBlockPos = tempPos;
                            break beaconSearch;
                        }
                    }
                }
            }
        }

        //Determine how many levels the beacon has
        boolean onlyOpulentBlock = true;
        int levels = 0;
        if (beaconBlockPos != null) {
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
        }

        if (onlyOpulentBlock && levels == 4) {
            grantCriterion(player, "opulent");
        }
    }

    private static void grantCriterion(ServerPlayerEntity player, String advancementId) {
        PlayerAdvancements advancements = player.getAdvancements();
        AdvancementManager manager = player.getServerWorld().getServer().getAdvancementManager();
        Advancement advancement = manager.getAdvancement(new ResourceLocation(TitlesAPI.MOD_ID, advancementId));
        if(advancement != null) {
            advancements.grantCriterion(advancement, "code_triggered");
        }
    }
}