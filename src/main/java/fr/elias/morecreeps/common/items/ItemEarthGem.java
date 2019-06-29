package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemEarthGem extends Item
{
    public static Random random = new Random();

    public ItemEarthGem()
    {
        super(new Item.Properties().maxStackSize(1).maxDamage(32).group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.EARTH_GEM, SoundCategory.PLAYERS, 1.0F, 1.0F);
        itemstack.damageItem(1, playerentity, null); // onBroken ??
        playerentity.swingArm(Hand.MAIN_HAND);

        for (int k = -2; k < 4; k++)
        {
            for (int l = -3; l < 3; l++)
            {
                for (int i1 = -3; i1 < 3; i1++)
                {
                    Block i = world.getBlockState(new BlockPos((int)(playerentity.posX + (double)l), (int)((playerentity.posY - 2D) + (double)k), (int)(playerentity.posZ + (double)i1))).getBlock();
                    Block j = world.getBlockState(new BlockPos((int)(playerentity.posX + (double)l), (int)((playerentity.posY - 1.0D) + (double)k), (int)(playerentity.posZ + (double)i1))).getBlock();

                    if ((i == Blocks.DIRT || i == Blocks.GRASS || i == Blocks.PODZOL) && random.nextInt(5) == 0 && j == Blocks.AIR)
                    {
                    	MoreCreepsReboot.proxy.dirt(world, playerentity, random, l, i1, k);

                        world.setBlockState(new BlockPos((int)(playerentity.posX + (double)l), (int)((playerentity.posY - 2D) + (double)k), (int)(playerentity.posZ + (double)i1)), Blocks.FARMLAND.getDefaultState());
                        world.setBlockState(new BlockPos((int)(playerentity.posX + (double)l), (int)((playerentity.posY - 1.0D) + (double)k), (int)(playerentity.posZ + (double)i1)), Block.getStateById(59));
                    }

                    /*int k1 = world.getBlockMetadata((int)(playerentity.posX + (double)l), (int)((playerentity.posY - 1.0D) + (double)k), (int)(playerentity.posZ + (double)i1));

                    if (k1 < 7 && (i == 59 || i == 60))
                    {
                        world.setBlockMetadataWithNotify((int)(playerentity.posX + (double)l), (int)((playerentity.posY - 1.0D) + (double)k), (int)(playerentity.posZ + (double)i1), k1 + 1);
                    }*/
                }
            }
        }
        return itemstack;
    }
}
