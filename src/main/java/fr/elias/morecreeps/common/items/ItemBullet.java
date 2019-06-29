package fr.elias.morecreeps.common.items;

import java.util.Random;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ItemBullet extends Item
{
    public static Random rand = new Random();
    public float speed;
    public double wayX;
    public double wayY;
    public double wayZ;
    public int waypoint;
    public int wayvert;
    public double distcheck;
    public double prevdistcheck;
    public boolean superflag;

    public ItemBullet()
    {
        super(new Item.Properties().maxStackSize(1).group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        wayvert = rand.nextInt(8) + 4;
        int i = rand.nextInt(30) + 5;
        int j = rand.nextInt(10) - 5;
        int k = 0;
        wayX = playerentity.posX - (double)i;
        wayZ = playerentity.posZ + (double)j;
        k = getHeightValue(world, (int)wayX, (int)wayZ);
        wayY = k + wayvert;
        world.setBlockState(new BlockPos((int)wayX, (int)wayY, (int)wayZ), Blocks.DIRT.getDefaultState());
        wayX = playerentity.posX + (double)j;
        wayZ = playerentity.posZ + (double)i;
        k = getHeightValue(world, (int)wayX, (int)wayZ);
        wayY = k + wayvert;
        world.setBlockState(new BlockPos((int)wayX, (int)wayY, (int)wayZ), Blocks.ICE.getDefaultState());
        wayX = playerentity.posX + (double)i;
        wayZ = playerentity.posZ + (double)j;
        k = getHeightValue(world, (int)wayX, (int)wayZ);
        wayY = k + wayvert;
        world.setBlockState(new BlockPos((int)wayX, (int)wayY, (int)wayZ), Blocks.SANDSTONE.getDefaultState());
        wayX = playerentity.posX + (double)j;
        wayZ = playerentity.posZ - (double)i;
        k = getHeightValue(world, (int)wayX, (int)wayZ);
        wayY = k + wayvert;
        world.setBlockState(new BlockPos((int)wayX, (int)wayY, (int)wayZ), Blocks.GLASS.getDefaultState());
        return itemstack;
    }
    public static int getHeightValue(World world, int x, int z)
    {
        if(x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000)
        {
            if(!world.getChunkProvider().chunkExists(x >> 4, z >> 4))
            {
                return 0;
            }
            Chunk chunk = world.getChunk(x >> 4, z >> 4);
            return chunk.getHeight();
        }
        return 64;
    }
}
