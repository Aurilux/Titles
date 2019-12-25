package aurilux.titles.common.handler;

import aurilux.titles.api.TitlesAPI;
import aurilux.titles.common.Titles;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Titles.MOD_ID)
public class AdvancementHandler {
    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        Advancement advancement = event.getAdvancement();
        TitlesAPI.addTitle(player, advancement.getId().toString(), true);
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
            Block block = Block.getBlockFromItem(event.getTo().getItem());
            EntityEquipmentSlot slot = event.getSlot();
            if (block == Blocks.PUMPKIN && slot == EntityEquipmentSlot.HEAD) {
                grantCriterion(player, "melon_lord");
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerMount(EntityMountEvent event) {
        Entity mounted = event.getEntityBeingMounted();
        Entity mounting = event.getEntityMounting();
        if (mounted instanceof EntityBoat && mounting instanceof EntityPlayerMP) {
            grantCriterion((EntityPlayerMP) mounting, "captain");
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.PlaceEvent event) {
        Block placedBlock = event.getPlacedBlock().getBlock();
        if (placedBlock != Blocks.DIAMOND_BLOCK && placedBlock != Blocks.BEACON) {
            return;
        }

        World world = event.getPlayer().world;
        BlockPos beaconBlockPos = null;

        //if the placed block was a diamond block, find the nearest beacon
        if (placedBlock == Blocks.DIAMOND_BLOCK) {
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
        else {
            beaconBlockPos = event.getPos();
        }

        boolean onlyDiamondBlock = true;
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
                        if (world.getBlockState(new BlockPos(k, j, l)).getBlock() != Blocks.DIAMOND_BLOCK) {
                            onlyDiamondBlock = false;
                            break beaconBaseValidation;
                        }
                    }
                }
            }
        }

        if (onlyDiamondBlock && levels == 4) {
            grantCriterion((EntityPlayerMP) event.getPlayer(), "opulent");
        }
    }

    private static void grantCriterion(EntityPlayerMP player, String advancementId) {
        PlayerAdvancements advancements = player.getAdvancements();
        AdvancementManager manager = player.getServerWorld().getAdvancementManager();
        Advancement advancement = manager.getAdvancement(new ResourceLocation(Titles.MOD_ID, advancementId));
        if(advancement != null) {
            advancements.grantCriterion(advancement, "code_triggered");
        }
    }
}
