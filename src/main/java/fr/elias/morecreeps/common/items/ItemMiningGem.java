package fr.elias.morecreeps.common.items;

import java.util.Random;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemMiningGem extends Item
{
    public static Random random = new Random();

    public ItemMiningGem()
    {
        super(new Item.Properties().maxStackSize(1).maxDamage(64).group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        playerentity.swingArm(Hand.MAIN_HAND);
        float f = 1.0F;
        float f1 = playerentity.prevRotationPitch + (playerentity.rotationPitch - playerentity.prevRotationPitch) * f;
        float f2 = playerentity.prevRotationYaw + (playerentity.rotationYaw - playerentity.prevRotationYaw) * f;
        double d = playerentity.prevPosX + (playerentity.posX - playerentity.prevPosX) * (double)f;
        double d3 = (playerentity.prevPosY + (playerentity.posY - playerentity.prevPosY) * (double)f + 1.6200000000000001D) - (double)playerentity.getYOffset();
        double d6 = playerentity.prevPosZ + (playerentity.posZ - playerentity.prevPosZ) * (double)f;
        Vec3d vec3d = new Vec3d(d, d3, d6);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d9 = 5D;
        Vec3d vec3d1 = vec3d.addVector((double)f7 * d9, (double)f8 * d9, (double)f9 * d9);
        RayTraceResult movingobjectposition = world.rayTraceBlocks(vec3d, vec3d1, true);

        if (movingobjectposition == null)
        {
            return itemstack;
        }

        if (movingobjectposition.hitInfo == RayTraceResult.Type.BLOCK)
        {
            int i = movingobjectposition.getBlockPos().getX();
            int j = movingobjectposition.getBlockPos().getY();
            int k = movingobjectposition.getBlockPos().getZ();
            Block l = world.getBlockState(new BlockPos(i, j, k)).getBlock();

            if (l == Blocks.STONE || l == Blocks.ANDESITE || l == Blocks.DIORITE || l == Blocks.GRANITE || l == Blocks.COBBLESTONE || l == Blocks.MOSSY_COBBLESTONE || l == Blocks.GRAVEL)
            {
                itemstack.damageItem(1, playerentity, null); // onBroken ??
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.MINING_GEM, SoundCategory.PLAYERS, 1.0F, 1.0F);

                for (int i1 = 0; i1 < 20; i1++)
                {
                    double d1 = random.nextGaussian() * 0.02D;
                    double d4 = random.nextGaussian() * 0.02D;
                    double d7 = random.nextGaussian() * 0.02D;
                    double d10 = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
                    double d12 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
                    world.addParticle(ParticleTypes.EXPLOSION, (double)i + (double)(random.nextFloat() * 1.5F), (double)((float)j + 0.5F) + (double)(random.nextFloat() * 2.5F), (double)k + (double)(random.nextFloat() * 1.5F), d1, d4, d7);
                }

                int j1 = random.nextInt(7);

                switch (j1)
                {
                    case 1:
                        if (random.nextInt(7) == 0)
                        {
                            world.setBlockState(new BlockPos(i, j, k), Blocks.GOLD_ORE.getDefaultState());
                        }

                        break;

                    case 2:
                        if (random.nextInt(5) == 0)
                        {
                            world.setBlockState(new BlockPos(i, j, k), Blocks.IRON_ORE.getDefaultState());
                        }

                        break;

                    case 3:
                        if (random.nextInt(1) == 0)
                        {
                            world.setBlockState(new BlockPos(i, j, k), Blocks.COAL_ORE.getDefaultState());
                        }

                        break;

                    case 4:
                        if (random.nextInt(5) == 0)
                        {
                            world.setBlockState(new BlockPos(i, j, k), Blocks.LAPIS_ORE.getDefaultState());
                        }

                        break;

                    case 5:
                        if (random.nextInt(10) == 0)
                        {
                            world.setBlockState(new BlockPos(i, j, k), Blocks.DIAMOND_ORE.getDefaultState());
                        }

                        break;

                    case 6:
                        if (random.nextInt(3) == 0)
                        {
                            world.setBlockState(new BlockPos(i, j, k), Blocks.REDSTONE_ORE.getDefaultState());
                        }

                        break;

                    case 7:
                        if (random.nextInt(3) == 0)
                        {
                            world.setBlockState(new BlockPos(i, j, k), Blocks.REDSTONE_ORE.getDefaultState());
                        }

                        break;
                        
                    case 8:
                        if (random.nextInt(10) == 0)
                        {
                            world.setBlockState(new BlockPos(i, j, k), Blocks.EMERALD_ORE.getDefaultState());
                        }

                        break;

                    default:
                        world.setBlockState(new BlockPos(i, j, k), Blocks.AIR.getDefaultState());
                        break;
                }
            }
            else
            {
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.MINING_GEM_BAD, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                for (int k1 = 0; k1 < 20; k1++)
                {
                    double d2 = random.nextGaussian() * 0.02D;
                    double d5 = random.nextGaussian() * 0.02D;
                    double d8 = random.nextGaussian() * 0.02D;
                    double d11 = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
                    double d13 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
                    world.addParticle(ParticleTypes.SMOKE, (double)i + (double)(random.nextFloat() * 1.5F), (double)((float)j + 0.5F) + (double)(random.nextFloat() * 2.5F), (double)k + (double)(random.nextFloat() * 1.5F), d2, d5, d8);
                }
            }
        }

        return itemstack;
    }

}
