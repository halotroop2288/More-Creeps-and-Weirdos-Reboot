package fr.elias.morecreeps.common.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.WorldGenRegion;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.CastleKingEntity;
import fr.elias.morecreeps.common.lists.ItemList;

public class CREEPSWorldGenCastle extends WorldGenRegion {
    // public static Map stringToClassMapping = EntityList.stringToClassMapping;
    public static Random rand = new Random();
    public int floor;
    private int topFloor;
    private int castleHeight;
    private boolean alternate;
    public boolean chunky;

    public CREEPSWorldGenCastle(World world, ChunkPrimer chunkPrimers)
    {
        super(null, null);
    }

    public static boolean blockExists(World parWorld, int x, int y, int z) {
        BlockState state = parWorld.getBlockState(new BlockPos(x, y, z));
    	if (state != null)
    	return true;
    	else
    	return false;
    }
    
    public boolean generate(World world, Random random, int i, int j, int k)
    {
        boolean flag = false;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        castleHeight = 5;
        alternate = true;
        boolean flag1 = false;
        char c = 300;
        int k1 = (i + random.nextInt(8)) - random.nextInt(8);
        int l1 = 200;
        int i2 = (k + random.nextInt(8)) - random.nextInt(8);
        l = k1;
        i1 = l1;
        j1 = i2;
        int j2 = 0;
        chunky = false;
        
        // TODO get the closest player (hope it will not overload the server...) -elias
        // Done. That wasn't so hard... -halo
        PlayerEntity player = world.getClosestPlayer(i, j, k, 64D, false); // get player far from 64 blocks

        if (player == null)
        {
            return false;
        }

        if (Math.abs((double)k1 - player.posX) < 10D || Math.abs((double)i2 - player.posZ) < 10D)
        {
            return false;
        }

        int k2 = 0;
        int l2 = 0;
        boolean flag2 = false;

        while (k2 < 3)
        {
            k2 = 0;
            Block i3 = world.getBlockState(new BlockPos(l - 4, i1 - l2, j1 - 4)).getBlock();
            Block k3 = world.getBlockState(new BlockPos(l + 30, i1 - l2, j1 - 4)).getBlock();
            Block i4 = world.getBlockState(new BlockPos(l - 4, i1 - l2, j1 + 30)).getBlock();
            Block k4 = world.getBlockState(new BlockPos(l + 30, i1 - l2, j1 + 30)).getBlock();

            if (i3 != Blocks.AIR && i3 != Blocks.ACACIA_LEAVES && i3 != Blocks.BIRCH_LEAVES && i3 != Blocks.DARK_OAK_LEAVES && i3 != Blocks.JUNGLE_LEAVES && i3 != Blocks.OAK_LEAVES && i3 != Blocks.SPRUCE_LEAVES && i3 != Blocks.WATER)
            {
                k2++;
            }

            if (k3 != Blocks.AIR && k3 != Blocks.ACACIA_LEAVES && k3 != Blocks.BIRCH_LEAVES && k3 != Blocks.DARK_OAK_LEAVES && k3 != Blocks.JUNGLE_LEAVES && k3 != Blocks.OAK_LEAVES && k3 != Blocks.SPRUCE_LEAVES && k3 != Blocks.WATER)
            {
                k2++;
            }

            if (i4 != Blocks.AIR && i4 != Blocks.ACACIA_LEAVES && i4 != Blocks.BIRCH_LEAVES && i4 != Blocks.DARK_OAK_LEAVES && i4 != Blocks.JUNGLE_LEAVES && i4 != Blocks.OAK_LEAVES && i4 != Blocks.SPRUCE_LEAVES && i4 != Blocks.WATER)
            {
                k2++;
            }

            if (k4 != Blocks.AIR && k4 != Blocks.ACACIA_LEAVES && k4 != Blocks.BIRCH_LEAVES && k4 != Blocks.DARK_OAK_LEAVES && k4 != Blocks.JUNGLE_LEAVES && k4 != Blocks.OAK_LEAVES && k4 != Blocks.SPRUCE_LEAVES && k4 != Blocks.WATER)
            {
                k2++;
            }

            if (i3 == Blocks.WATER)
            {
                flag2 = true;
            }

            if (k3 == Blocks.WATER)
            {
                flag2 = true;
            }

            if (i4 == Blocks.WATER)
            {
                flag2 = true;
            }

            if (k4 == Blocks.WATER)
            {
                flag2 = true;
            }

            l2++;
        }

        i1 = l1 = (i1 - l2) + 2;

        if (blockExists(world, k1 - 4, l1, i2 - 4) && blockExists(world, k1 + 30, l1, i2 - 4) && blockExists(world, k1 + 30, l1, i2 + 30) && blockExists(world, k1 - 4, l1, i2 + 30))
        {
            chunky = true;
        }

        if (!chunky)
        {
            return false;
        }

        int j3 = 0;
        label0:

        do
        {
            if (j3 >= castleHeight * 7 + 7)
            {
                break;
            }

            for (int l3 = -12 + j3; l3 < 38; l3 += 2)
            {
                for (int j4 = -12 + j3; j4 < 38; j4 += 2)
                {
                    Block l4 = world.getBlockState(new BlockPos(i + l3, i1 + j3, k + j4)).getBlock();
                    //TODO check if "2" blocks is a good idea
                    if (l4 != Blocks.AIR && l4 != Blocks.OAK_LEAVES && l4 != Blocks.JUNGLE_LEAVES && l4 != Blocks.OAK_LOG && l4 != Blocks.JUNGLE_LOG)
                    {
                        j2++;
                    }

                    if (j2 > c)
                    {
                        break label0;
                    }
                }
            }

            j3 += 2;
        }
        while (true);

        if (j2 > c)
        {
            flag2 = true;
        }

        if (flag2)
        {
            return false;
        }

        j3 = 0;
        boolean flag3 = false;
        boolean flag4 = false;
        int i5 = i1 - 6;
        int j5 = random.nextInt(3);
        floor = 1;
        topFloor = 0;

        for (int k5 = -1; k5 < castleHeight * 7 + 7; k5++)
        {
            for (int k6 = -12; k6 < 38; k6++)
            {
                for (int i8 = -12; i8 < 38; i8++)
                {
                    world.setBlockState(new BlockPos(l + k6, i1 + k5, j1 + i8), Blocks.AIR.getDefaultState());
                }
            }
        }

        for (int l5 = 2; l5 < 5; l5++)
        {
            for (int l6 = -12; l6 < 38; l6++)
            {
                for (int j8 = -12; j8 < 38; j8++)
                {
                    if (world.getBlockState(new BlockPos(l + l6, i1 - l5, j1 + j8)).getBlock() == Blocks.AIR)
                    {
                        world.setBlockState(new BlockPos(l + l6, i1 - l5, j1 + j8), Blocks.STONE.getDefaultState());
                    }
                }
            }
        }

        for (int i6 = -10; i6 < 36; i6++)
        {
            for (int i7 = -10; i7 < 36; i7++)
            {
                world.setBlockState(new BlockPos(l + i6, i1 - 1, j1 + i7), Blocks.WATER.getDefaultState());
            }
        }

        label1:

        for (int j6 = -7; j6 < castleHeight * 7 - 7; j6 += 7)
        {
            for (int j7 = -4; j7 < 30; j7++)
            {
                for (int k8 = -4; k8 < 30; k8++)
                {
                    if ((world.getBlockState(new BlockPos(l + j7, i1 + 6 + j6, j1 + k8)) == Blocks.AIR.getDefaultState() || world.getBlockState(new BlockPos(l + j7, i1 + 6 + j6, j1 + k8)).getBlock() == Blocks.WATER) && rand.nextInt(50) != 0)
                    {
                        world.setBlockState(new BlockPos(l + j7, i1 + 6 + j6, j1 + k8), Blocks.STONE_SLAB.getDefaultState());
                    }

                    if (world.getBlockState(new BlockPos(l + j7, i1 + 7 + j6, j1 + k8)).getBlock() != Blocks.AIR || random.nextInt(25) != 0 || j6 >= (castleHeight - 1) * 7 - 7)
                    {
                        continue;
                    }

                    world.setBlockState(new BlockPos(l + j7, i1 + 7 + j6, j1 + k8), Blocks.COBWEB.getDefaultState());

                    if (rand.nextInt(10) == 0) {
                        world.setBlockState(new BlockPos(l + j7, i1 + 8 + j6, j1 + k8), Blocks.COBWEB.getDefaultState());
                    }
                }
            }

            int k7 = 0;

            do
            {
                if (k7 >= 2)
                {
                    break;
                }

                int l8 = rand.nextInt(20) - 10;
                int l12 = rand.nextInt(20) - 10;

                if (world.getBlockState(new BlockPos(l + 10 + l8, i1 + 7 + j6, j1 + 5 + l12)).getBlock() == Blocks.AIR)
                {
                    world.setBlockState(new BlockPos(l + 10 + l8, i1 + 7 + j6, j1 + 5 + l12), Blocks.SPAWNER.getDefaultState());
                    MobSpawnerTileEntity tileentitymobspawner = (MobSpawnerTileEntity) world.getTileEntity(new BlockPos(l + 10 + l8, i1 + 7 + j6, j1 + 5 + l12));
                    k7++;
                }
            } while (true);

            if (j6 != (castleHeight - 1) * 7 - 7) {
                continue;
            }

            k7 = 0;

            do {
                int i9;
                int i13;

                do {
                    if (k7 >= 2) {
                        continue label1;
                    }

                    i9 = rand.nextInt(20) - 10;
                    i13 = rand.nextInt(20) - 10;
                } while (world.getBlockState(new BlockPos(l + 10 + i9, i1 + 7 + j6, j1 + 5 + i13)) != Blocks.AIR.getDefaultState());

                world.setBlockState(new BlockPos(l + 10 + i9, i1 + 7 + j6, j1 + 5 + i13),
                        Blocks.SPAWNER.getDefaultState());
                MobSpawnerTileEntity tileentitymobspawner1 = (MobSpawnerTileEntity)world.getTileEntity(new BlockPos(l + 10 + i9, i1 + 7 + j6, j1 + 5 + i13));
                k7++;
            }
            while (true);
        }

        ItemStack itemstack = new ItemStack(ItemList.earth_gem, 1);
        int l7 = random.nextInt(castleHeight) + 1;
        buildTower(world, random, k1, l1, i2, l, i1, j1, true, itemstack, l7);

        for (int j9 = 5; j9 < 20; j9++)
        {
            for (int j13 = 0; j13 < 10; j13++)
            {
                for (int i16 = -2; i16 < castleHeight * 7 - 6; i16++)
                {
                    world.setBlockState(new BlockPos(l + j9, i1 + i16, j1 - 5), cobbler(1, random));
                }
            }
        }

        for (int k9 = 4; k9 < 21; k9++)
        {
            if (alternate)
            {
                world.setBlockState(new BlockPos(l + k9, i1 + 29, j1 - 5), cobbler(1, random));
            }

            alternate = !alternate;
            world.setBlockState(new BlockPos(l + k9, i1 + 20, j1 - 6), cobbler(1, random));
            world.setBlockState(new BlockPos(l + k9, i1 + 16, j1 - 6), cobbler(1, random));

            if (alternate)
            {
                world.setBlockState(new BlockPos(l + k9, i1 + 10, j1 - 6), cobbler(1, random));
            }

            world.setBlockState(new BlockPos(l + k9, i1 + 9, j1 - 6), cobbler(1, random));
        }

        itemstack = new ItemStack(ItemList.mining_gem, 1);
        l7 = random.nextInt(castleHeight) + 1;
        buildTower(world, random, k1, l1, i2, l + 25, i1, j1, false, itemstack, l7);

        for (int l9 = 5; l9 < 20; l9++)
        {
            for (int k13 = 0; k13 < 10; k13++)
            {
                for (int j16 = -2; j16 < castleHeight * 7 - 6; j16++)
                {
                    world.setBlockState(new BlockPos(l + l9, i1 + j16, j1 + 29), cobbler(1, random));
                }
            }
        }

        for (int i10 = 4; i10 < 21; i10++)
        {
            if (alternate)
            {
                world.setBlockState(new BlockPos(l + i10, i1 + 29, j1 + 29), cobbler(1, random));
            }

            alternate = !alternate;
            world.setBlockState(new BlockPos(l + i10, i1 + 20, j1 + 30), cobbler(1, random));
            world.setBlockState(new BlockPos(l + i10, i1 + 16, j1 + 30), cobbler(1, random));

            if (alternate)
            {
                world.setBlockState(new BlockPos(l + i10, i1 + 10, j1 + 30), cobbler(1, random));
            }

            world.setBlockState(new BlockPos(l + i10, i1 + 9, j1 + 30), cobbler(1, random));
        }

        itemstack = new ItemStack(ItemList.sky_gem, 1);
        l7 = random.nextInt(castleHeight) + 1;
        buildTower(world, random, k1, l1, i2, l + 25, i1, j1 + 25, false, itemstack, l7);

        for (int j10 = 5; j10 < 20; j10++)
        {
            for (int l13 = 0; l13 < 10; l13++)
            {
                for (int k16 = -2; k16 < castleHeight * 7 - 6; k16++)
                {
                    world.setBlockState(new BlockPos(l - 5, i1 + k16, j1 + j10), cobbler(1, random));
                }
            }
        }

        for (int k10 = 4; k10 < 21; k10++)
        {
            if (alternate)
            {
                world.setBlockState(new BlockPos(l - 5, i1 + 29, j1 + k10), cobbler(1, random));
            }

            alternate = !alternate;
            world.setBlockState(new BlockPos(l - 6, i1 + 20, j1 + k10), cobbler(1, random));
            world.setBlockState(new BlockPos(l - 6, i1 + 16, j1 + k10), cobbler(1, random));

            if (alternate)
            {
                world.setBlockState(new BlockPos(l - 6, i1 + 10, j1 + k10), cobbler(1, random));
            }

            world.setBlockState(new BlockPos(l - 6, i1 + 9, j1 + k10), cobbler(1, random));
        }

        itemstack = new ItemStack(ItemList.healing_gem, 1);
        l7 = random.nextInt(castleHeight) + 1;
        buildTower(world, random, k1, l1, i2, l, i1, j1 + 25, true, itemstack, l7);

        for (int l10 = 5; l10 < 20; l10++)
        {
            for (int i14 = 0; i14 < 10; i14++)
            {
                for (int l16 = -2; l16 < castleHeight * 7 - 6; l16++)
                {
                    world.setBlockState(new BlockPos(l + 29, i1 + l16, j1 + l10), cobbler(1, random));
                }
            }
        }

        for (int i11 = 4; i11 < 21; i11++)
        {
            if (alternate)
            {
                world.setBlockState(new BlockPos(l + 29, i1 + 29, j1 + i11), cobbler(1, random));
            }

            alternate = !alternate;
            world.setBlockState(new BlockPos(l + 30, i1 + 20, j1 + i11), cobbler(1, random));
            world.setBlockState(new BlockPos(l + 30, i1 + 16, j1 + i11), cobbler(1, random));

            if (alternate)
            {
                world.setBlockState(new BlockPos(l + 30, i1 + 10, j1 + i11), cobbler(1, random));
            }

            world.setBlockState(new BlockPos(l + 30, i1 + 9, j1 + i11), cobbler(1, random));
        }

        for (int j11 = 0; j11 < (castleHeight - 1) * 7 - 6; j11 += 7)
        {
            for (int j14 = 6; j14 < 20; j14 += 3)
            {
                world.setBlockState(new BlockPos(l + j14, i1 + 4 + j11, j1 - 4), Blocks.TORCH.getDefaultState());
                world.setBlockState(new BlockPos(l + j14, i1 + 4 + j11, j1 + 28), Blocks.TORCH.getDefaultState());

                if (j14 > 6 && j14 < 17) {
                    world.setBlockState(new BlockPos(l - 4, i1 + 4 + j11, j1 + j14), Blocks.TORCH.getDefaultState());
                    world.setBlockState(new BlockPos(l + 28, i1 + 4 + j11, j1 + j14), Blocks.TORCH.getDefaultState());
                }
            }
        }

        for (int k11 = 11; k11 < 15; k11++) {
            for (int k14 = 0; k14 < 4; k14++) {
                world.setBlockState(new BlockPos(l + k11, i1 + k14, j1 - 5), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(l + k11, i1 + k14, j1 + 29), Blocks.AIR.getDefaultState());
            }
        }

        for (int l11 = 11; l11 < 15; l11++) {
            for (int l14 = 0; l14 < 6; l14++) {
                for (int i17 = 0; i17 < 4; i17++) {
                    world.setBlockState(new BlockPos(l + l11, i1 - 1, j1 - 6 - l14),
                            Blocks.OAK_PLANKS.getDefaultState());
                    world.setBlockState(new BlockPos(l + l11, i1 - 1, j1 + 30 + l14),
                            Blocks.OAK_PLANKS.getDefaultState());
                }
            }
        }

        world.setBlockState(new BlockPos(l + 9, i1 + 5, j1 + 30), Blocks.TORCH.getDefaultState());
        world.setBlockState(new BlockPos(l + 16, i1 + 5, j1 + 30), Blocks.TORCH.getDefaultState());
        world.setBlockState(new BlockPos(l + 9, i1 + 5, j1 - 6), Blocks.TORCH.getDefaultState());
        world.setBlockState(new BlockPos(l + 16, i1 + 5, j1 - 6), Blocks.TORCH.getDefaultState());

        for (int i12 = 0; i12 < castleHeight * 7; i12 += 7) {
            for (int i15 = 0; i15 < 2; i15++) {
                world.setBlockState(new BlockPos(l + 17, i1 + i12, j1 + -1 + i15),
                        Blocks.STONE_STAIRS.getDefaultState());
                world.setBlockState(new BlockPos(l + 5, i1 + i12, j1 + -1 + i15),
                        Blocks.STONE_STAIRS.getDefaultState());
                world.setBlockState(new BlockPos(l + 17, i1 + i12, j1 + 24 + i15),
                        Blocks.STONE_STAIRS.getDefaultState());
                world.setBlockState(new BlockPos(l + 5, i1 + i12, j1 + 24 + i15),
                        Blocks.STONE_STAIRS.getDefaultState());
                world.setBlockState(new BlockPos(l + 19, i1 + i12, j1 + -1 + i15),
                        Blocks.STONE_STAIRS.getDefaultState());
                world.setBlockState(new BlockPos(l + 7, i1 + i12, j1 + -1 + i15),
                        Blocks.STONE_STAIRS.getDefaultState());
                world.setBlockState(new BlockPos(l + 19, i1 + i12, j1 + 24 + i15),
                        Blocks.STONE_STAIRS.getDefaultState());
                world.setBlockState(new BlockPos(l + 7, i1 + i12, j1 + 24 + i15),
                        Blocks.STONE_STAIRS.getDefaultState());
                // old method: world.setBlockMetadataWithNotify(x, y, z, block id, meta id (if
                // needed, used for wool color etc.));
                // world.setBlockMetadataWithNotify(l + 19, i1 + i12, j1 + -1 + i15, 1);
                world.setBlockState(new BlockPos(l + 19, i1 + i12, j1 + -1 + i15), Blocks.STONE.getDefaultState());
                // world.setBlockMetadataWithNotify(l + 7, i1 + i12, j1 + -1 + i15, 1);
                world.setBlockState(new BlockPos(l + 7, i1 + i12, j1 + -1 + i15), Blocks.STONE.getDefaultState());
                // world.setBlockMetadataWithNotify(l + 19, i1 + i12, j1 + 24 + i15, 1);
                world.setBlockState(new BlockPos(l + 19, i1 + i12, j1 + 24 + i15), Blocks.STONE.getDefaultState());
                // world.setBlockMetadataWithNotify(l + 7, i1 + i12, j1 + 24 + i15, 1);
                world.setBlockState(new BlockPos(l + 7, i1 + i12, j1 + 24 + i15), Blocks.STONE.getDefaultState());
                // fixed :)
                world.setBlockState(new BlockPos(l + 19, i1 + i12, j1 + -1 + i15),
                        Blocks.STONE_STAIRS.getDefaultState(), 1);
                world.setBlockState(new BlockPos(l + 7, i1 + i12, j1 + -1 + i15), Blocks.STONE_STAIRS.getDefaultState(),
                        1);
                world.setBlockState(new BlockPos(l + 19, i1 + i12, j1 + 24 + i15),
                        Blocks.STONE_STAIRS.getDefaultState(), 1);
                world.setBlockState(new BlockPos(l + 7, i1 + i12, j1 + 24 + i15), Blocks.STONE_STAIRS.getDefaultState(),
                        1);
                // TODO check if it works
            }
        }

        for (int j12 = 0; j12 < castleHeight * 7; j12 += 7) {
            for (int j15 = 0; j15 < 7; j15++) {
                for (int j17 = 0; j17 < 9; j17++) {
                    world.setBlockState(new BlockPos(l + 9 + j15, i1 + j12, (j1 + 16) - j17),
                            Blocks.STONE_SLAB.getDefaultState());
                }
            }
        }

        for (int k12 = 0; k12 < castleHeight * 7; k12 += 7) {
            for (int k15 = 0; k15 < 9; k15 += 3) {
                world.setBlockState(new BlockPos(l + 9 + k15, i1 + k12, j1 + 16),
                        Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                world.setBlockState(new BlockPos(l + 9 + k15, i1 + k12 + 1, j1 + 16),
                        Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                world.setBlockState(new BlockPos(l + 9 + k15, i1 + k12 + 2, j1 + 16), Blocks.TORCH.getDefaultState());
                world.setBlockState(new BlockPos(l + 9 + k15, i1 + k12, j1 + 8),
                        Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                world.setBlockState(new BlockPos(l + 9 + k15, i1 + k12 + 1, j1 + 8),
                        Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                world.setBlockState(new BlockPos(l + 9 + k15, i1 + k12 + 2, j1 + 8), Blocks.TORCH.getDefaultState());
            }
        }

        l7 = random.nextInt(castleHeight) + 1;
        itemstack = new ItemStack(ItemList.fire_gem, 1);
        boolean flag5 = false;

        for (int l15 = 0; l15 < castleHeight * 7; l15 += 7)
        {
            world.setBlockState(new BlockPos(l + 9 + 3, i1 + l15, (j1 + 16) - 3), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
            world.setBlockState(new BlockPos(l + 9 + 3, i1 + l15, (j1 + 16) - 4), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
            world.setBlockState(new BlockPos(l + 9 + 3, i1 + l15 + 1, (j1 + 16) - 4), Blocks.CHEST.getDefaultState());
            world.setBlockState(new BlockPos(l + 9 + 3, i1 + l15 + 1, (j1 + 16) - 3), Blocks.CHEST.getDefaultState());
            ChestTileEntity tileentitychest = (ChestTileEntity)world.getTileEntity(new BlockPos(l + 9 + 3, i1 + l15 + 1, (j1 + 16) - 3));

            for (int k17 = 0; k17 < random.nextInt(20); k17++)
            {
                ItemStack itemstack1 = populateChest(floor, random);

                if (itemstack1 != null)
                {
                    tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack1);
                }

                if (!flag5 && l7 == l15 / 7)
                {
                    tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack);
                    flag5 = true;
                }
            }
        }

        CastleKingEntity creepsentitycastleking = new CastleKingEntity(world);
        creepsentitycastleking.setLocationAndAngles(l + 9 + 6, i1 + castleHeight * 7, (j1 + 16) - 4, world.rand.nextFloat() * 360F, 0.0F);
        creepsentitycastleking.setPosition(l + 9 + 6, i1 + castleHeight * 7, (j1 + 16) - 4);
        world.addEntity(creepsentitycastleking);
        return true;
    }

    public void buildTower(World world, Random random, int i, int j, int k, int l, int i1, int j1, boolean flag, ItemStack itemstack, int k1)
    {
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        int l1 = i1 - 13;
        int i2 = random.nextInt(3);
        boolean flag4 = false;
        floor = 1;
        topFloor = 0;

        for (int j2 = 0; j2 < castleHeight + 1; j2++)
        {
            l1 += 7;

            if (j2 == castleHeight)
            {
                topFloor = 1;
            }

            for (int k2 = 0; k2 < 7; k2++)
            {
                if (l1 == i1 - 6 && k2 < 4)
                {
                    k2 = 4;
                }

                for (int j3 = -7; j3 < 7; j3++)
                {
                    for (int l3 = -7; l3 < 7; l3++)
                    {
                        int k4 = j3 + l;
                        int i5 = k2 + l1;
                        int j5 = l3 + j1;

                        if (l3 == -7)
                        {
                            if (j3 > -5 && j3 < 4)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                            }

                            continue;
                        }

                        if (l3 == -6 || l3 == -5)
                        {
                            if (j3 == -5 || j3 == 4)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                                continue;
                            }

                            if (l3 == -6)
                            {
                                if (j3 == (k2 + 1) % 7 - 3)
                                {
                                    world.setBlockState(new BlockPos(k4, i5, j5), Blocks.STONE_STAIRS.getDefaultState());

                                    if (k2 == 5)
                                    {
                                        world.setBlockState(new BlockPos(k4 - 7, i5, j5), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                                    }

                                    if (k2 == 6 && topFloor == 1)
                                    {
                                        world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                                    }

                                    continue;
                                }

                                if (j3 < 4 && j3 > -5)
                                {
                                    world.setBlockState(new BlockPos(k4, i5, j5), Blocks.AIR.getDefaultState());
                                }

                                continue;
                            }

                            if (l3 != -5 || j3 <= -5 || j3 >= 5)
                            {
                                continue;
                            }

                            if (k2 != 0 && k2 != 6 || j3 != -4 && j3 != 3)
                            {
                                if (k2 == 5 && (j3 == 3 || j3 == -4))
                                {
                                    world.setBlockState(new BlockPos(k4, i5, j5), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                                }
                                else
                                {
                                    world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                                }
                            }
                            else
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), Blocks.AIR.getDefaultState());
                            }

                            continue;
                        }

                        if (l3 == -4 || l3 == -3 || l3 == 2 || l3 == 3)
                        {
                            if (j3 == -6 || j3 == 5)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                                continue;
                            }

                            if (j3 <= -6 || j3 >= 5)
                            {
                                continue;
                            }

                            if (k2 == 5)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                                continue;
                            }

                            if (world.getBlockState(new BlockPos(k4, i5, j5)).getBlock() != Blocks.CHEST)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), Blocks.AIR.getDefaultState());
                            }

                            continue;
                        }

                        if (l3 > -3 && l3 < 2)
                        {
                            if (j3 == -7 || j3 == 6)
                            {
                                if (k2 < 0 || k2 > 3 || j3 != -7 && j3 != 6 || l3 != -1 && l3 != 0)
                                {
                                    world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                                    continue;
                                }

                                if (j3 == -7 && flag || j3 == 6 && !flag)
                                {
                                    world.setBlockState(new BlockPos(k4, i5, j5), Blocks.GLASS.getDefaultState());
                                }

                                continue;
                            }

                            if (j3 <= -7 || j3 >= 6)
                            {
                                continue;
                            }

                            if (k2 == 5)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                            }
                            else
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), Blocks.AIR.getDefaultState());
                            }

                            continue;
                        }

                        if (l3 == 4)
                        {
                            if (j3 == -5 || j3 == 4)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                                continue;
                            }

                            if (j3 <= -5 || j3 >= 4)
                            {
                                continue;
                            }

                            if (k2 == 5)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                            }
                            else
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), Blocks.AIR.getDefaultState());
                            }

                            continue;
                        }

                        if (l3 == 5)
                        {
                            if (j3 == -4 || j3 == -3 || j3 == 2 || j3 == 3)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                                continue;
                            }

                            if (j3 <= -3 || j3 >= 2)
                            {
                                continue;
                            }

                            if (k2 == 5)
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                            }
                            else
                            {
                                world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                            }

                            continue;
                        }

                        if (l3 != 6 || j3 <= -3 || j3 >= 2)
                        {
                            continue;
                        }

                        if (k2 < 0 || k2 > 3 || j3 != -1 && j3 != 0)
                        {
                            world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                        }
                        else
                        {
                            world.setBlockState(new BlockPos(k4, i5, j5), cobbler(i2, random));
                        }
                    }
                }
            }

            if (floor == 2)
            {
                world.setBlockState(new BlockPos(l + 3, l1, j1 - 5), cobbler(i2, random));
                world.setBlockState(new BlockPos(l + 3, l1 - 1, j1 - 5), cobbler(i2, random));
            }

            if (topFloor == 1)
            {
                double d = l;
                double d1 = l1 + 6;
                double d2 = (double)j1 + 0.5D;
            }
            else
            {
                if (rand.nextInt(5) == 0)
                {
                    world.setBlockState(new BlockPos(l + 2, l1 + 6, j1 + 2), Blocks.SPAWNER.getDefaultState());
                    MobSpawnerTileEntity tileentitymobspawner = (MobSpawnerTileEntity)world.getTileEntity(new BlockPos(l + 2, l1 + 6, j1 + 2));
                }

                world.setBlockState(new BlockPos(l - 3, l1 + 6, j1 + 2), Blocks.SPAWNER.getDefaultState());
                MobSpawnerTileEntity tileentitymobspawner1 = (MobSpawnerTileEntity)world.getTileEntity(new BlockPos(l - 3, l1 + 6, j1 + 2));
            }

            if (topFloor != 1)
            {
                world.setBlockState(new BlockPos(l, l1 + 6, j1 - 3), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
                world.setBlockState(new BlockPos(l - 1, l1 + 6, j1 - 3), Blocks.SMOOTH_STONE_SLAB.getDefaultState());
            }

            if (l1 + 56 >= 120 && floor == 1)
            {
                floor = 2;
            }

            if (topFloor != 1)
            {
                for (int l2 = 0; l2 < 2; l2++)
                {
                    world.setBlockState(new BlockPos(l - l2, l1 + 7, j1 - 3), Blocks.CHEST.getDefaultState());
                    ChestTileEntity tileentitychest = (ChestTileEntity)world.getTileEntity(new BlockPos(l - l2, l1 + 7, j1 - 3));

                    for (int i4 = 0; i4 < 1 + l2 + i2; i4++)
                    {
                        ItemStack itemstack1 = populateChest(floor, random);

                        if (itemstack1 != null)
                        {
                            tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), itemstack1);
                        }
                    }

                    if (k1 == floor && !flag4)
                    {
                        tileentitychest.setInventorySlotContents(0, itemstack);
                        flag4 = true;
                    }
                }
            }

            world.setBlockState(new BlockPos(l + 3, l1, j1 - 6), Blocks.TORCH.getDefaultState());
            world.setBlockState(new BlockPos(l - 4, l1, j1 - 6), Blocks.TORCH.getDefaultState());
            world.setBlockState(new BlockPos(l + 1, l1, j1 - 4), Blocks.TORCH.getDefaultState());
            world.setBlockState(new BlockPos(l - 2, l1, j1 - 4), Blocks.TORCH.getDefaultState());

            for (int i3 = 0; i3 < (floor * 4 + i2) - 8 && topFloor != 1; i3++)
            {
                int k3 = 5 - random.nextInt(12);
                int j4 = l1 + 5;
                int l4 = 5 - random.nextInt(10);

                if (l4 < -2 && k3 < 4 && k3 > -5 && k3 != 1 && k3 != -2)
                {
                    continue;
                }

                k3 += l;
                l4 += j1;

                if (world.getBlockState(new BlockPos(k3, j4, l4)).getBlock() == Blocks.STONE_SLAB && world.getBlockState(new BlockPos(k3, j4 + 1, l4)).getBlock() != Blocks.SPAWNER)
                {
                    world.setBlockState(new BlockPos(k3, j4, l4), Blocks.AIR.getDefaultState());
                }
            }

            floor++;
        }
    }

    private ItemStack populateChest(int i, Random random)
    {
        int j = random.nextInt(8 * i);

        if (j > 40)
        {
            j = 40;
        }

        switch (j)
        {
            case 1:
                return new ItemStack(Items.WHEAT, random.nextInt(12) + 3);

            case 2:
                return new ItemStack(Items.SUGAR_CANE, random.nextInt(6) + 6);

            case 3:
                return new ItemStack(Items.COOKIE, random.nextInt(6) + 6);

            case 4:
                return new ItemStack(Items.ARROW, random.nextInt(30) + 10);

            case 5:
                return new ItemStack(ItemList.money, random.nextInt(4) + 1);

            case 6:
                return new ItemStack(ItemList.evil_egg, random.nextInt(4) + 1);

            case 7:
                return new ItemStack(Items.LEATHER, 1);

            case 8:
                return new ItemStack(Items.PAPER, 1);

            case 9:
                return new ItemStack(Items.APPLE, 1);

            case 10:
                return new ItemStack(Items.WOODEN_AXE, 1);

            case 11:
                return new ItemStack(Items.BOW, 1);

            case 12:
                return new ItemStack(ItemList.band_aid, random.nextInt(15) + 1);

            case 13:
                return new ItemStack(ItemList.blorp_cola, random.nextInt(10) + 5);

            case 14:
                return new ItemStack(Items.OAK_SIGN, 1);

            case 15:
                return new ItemStack(Items.WHEAT, random.nextInt(10) + 5);

            case 16:
                return new ItemStack(Items.BREAD, 1);

            case 17:
                return new ItemStack(Items.IRON_PICKAXE, 1);

            case 18:
                return new ItemStack(Items.IRON_AXE, 1);

            case 19:
                return new ItemStack(Items.BUCKET, 1);

            case 20:
                return new ItemStack(Items.IRON_SHOVEL, 1);

            case 21:
                return new ItemStack(ItemList.evil_egg, random.nextInt(15) + 1);

            case 22:
                return new ItemStack(ItemList.goo_donut, random.nextInt(15) + 1);

            case 23:
                return new ItemStack(ItemList.money, random.nextInt(10) + 1);

            case 24:
                return new ItemStack(Items.WATER_BUCKET, 1);

            case 25:
                return new ItemStack(ItemList.frisbee, 1);

            case 26:
                return new ItemStack(Items.CAKE, 1);

            case 27:
                return new ItemStack(ItemList.money, random.nextInt(10) + 5);

            case 28:
                return new ItemStack(Items.MILK_BUCKET, 1);

            case 29:
                return new ItemStack(ItemList.lolly, random.nextInt(4) + 1);

            case 30:
                return new ItemStack(ItemList.money, random.nextInt(24) + 1);

            case 32:
                return new ItemStack(Items.DIAMOND, 1);

            case 33:
                return new ItemStack(Items.GOLDEN_HELMET, 1);

            case 34:
                return new ItemStack(Items.DIAMOND_HELMET, 1);

            case 35:
                return new ItemStack(Items.GOLDEN_BOOTS, 1);

            case 36:
                return new ItemStack(ItemList.shrink_ray, 1);

            case 37:
                return new ItemStack(ItemList.horse_head_gem, 1);

            case 38:
                return new ItemStack(Items.DIAMOND, 1);

            case 39:
                return new ItemStack(Items.GOLDEN_APPLE, 1);

            case 40:
                return new ItemStack(ItemList.money, random.nextInt(49) + 1);

            case 31:
            default:
                return null;
        }
    }

    private String populateSpawner(Random random)
    {
        int i = floor;
        int j = rand.nextInt(10);

        if (topFloor == 1)
        {
            return "CastleGuard";
        }

        if (i == 0)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "Skeleton";
            }
        }

        if (i == 1)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "CastleGuard";
            }
        }

        if (i == 2)
        {
            if (mobExists("Mummy") && j > 5)
            {
                return "Mummy";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "CastleGuard";
            }
        }

        if (i == 3)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "Skeleton";
            }
        }

        if (i == 4)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "Spider";
            }
            else
            {
                return "Skeleton";
            }
        }

        if (i == 5)
        {
            if (mobExists("PrefixSkeleton") && j > 5)
            {
                return "PrefixSkeleton";
            }

            if (j < 5)
            {
                return "CastleCritter";
            }
            else
            {
                return "CastleGuard";
            }
        }
        else
        {
            return "CastleGuard";
        }
    }

    private BlockState cobbler(int i, Random random)
    {
        if (i == 0)
        {
            return Blocks.COBBLESTONE.getDefaultState();
        }

        if (i == 1)
        {
            if (random.nextInt(3) == 0)
            {
                return Blocks.COBBLESTONE.getDefaultState();
            }
            else
            {
                return Blocks.MOSSY_COBBLESTONE.getDefaultState();
            }
        }
        else
        {
            return Blocks.COBBLESTONE.getDefaultState();
        }
    }

	public boolean generate(World worldIn, Random p_180709_2_, BlockPos bp)
	{
		return generate(worldIn, p_180709_2_, bp.getX(), bp.getY(), bp.getZ());
	}
}
