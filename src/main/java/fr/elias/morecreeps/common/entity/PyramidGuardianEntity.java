package fr.elias.morecreeps.common.entity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class PyramidGuardianEntity extends MobEntity
{
    Block bedGetter[] = { Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED,
        Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED,
        Blocks.ORANGE_BED, Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.RED_BED, Blocks.WHITE_BED,
        Blocks.YELLOW_BED };
    public static Random rand = new Random();
    public int rows;
    public int columns;
    public int maze[][];
    public static int backgroundCode;
    public static int wallCode;
    public static int pathCode;
    public static int emptyCode;
    public static int visitedCode;
    public ChestTileEntity chest;
    public int alternate;
    public boolean found;
    public int bedrockcounter;
    public String texture;

    public PyramidGuardianEntity(World world)
    {
        super(null, world);
        texture = "morecreeps:textures/entity/pyramidguardian.png";
        found = false;
        rotationYaw = 0.0F;
        // setSize(0.4F, 0.4F);
        alternate = 1;
        bedrockcounter = 0;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new EntityAIAttackOnCollide(this, PlayerEntity.class, 0.4D, true));
        // this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, PlayerEntity.class, true));
    }

    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("Found", found);
        compound.putInt("BedrockCounter", bedrockcounter);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        found = compound.getBoolean("Found");
        bedrockcounter = compound.getInt("BedrockCounter");
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        setMotion(0.0D, 0.0D, 0.0D);
        super.livingTick();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();

        if (entity != null && (entity instanceof PlayerEntity))
        {
            return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
        }
        else
        {
            return false;
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
        if (damagesource.getTrueSource() != null && damagesource.getTrueSource() instanceof PlayerEntity)
        {
        	// ServerPlayerEntity player = (ServerPlayerEntity) damagesource.getTrueSource();
            // if (!player.getStats().hasAchievementUnlocked(ModAdvancementList.pyramid))
            // {
            //     world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
            //     player.addStat(ModAdvancementList.pyramid, 1);
            //     confetti(player);
            // }

            if(!world.isRemote)
            {
                int i = MathHelper.floor(posX);
                int j = MathHelper.floor(getBoundingBox().minY);
                int k = MathHelper.floor(posZ);

                if (posY > 60D)
                {
                    for (int l = -4; l < 6; l++)
                    {
                        for (int k2 = -40; k2 < 40; k2++)
                        {
                            for (int j3 = -40; j3 < 40; j3++)
                            {
                                if (world.getBlockState(new BlockPos(i + k2, j + l, k + j3)).getBlock() == Blocks.BEDROCK)
                                {
                                    bedrockcounter++;
                                    world.setBlockState(new BlockPos(i + k2, j + l, k + j3), Blocks.SANDSTONE.getDefaultState());
                                }
                            }
                        }
                    }
                }

                if (bedrockcounter > 50)
                {
                    for (int i1 = 3; i1 < 11; i1++)
                    {
                        for (int l2 = 9; l2 < 24; l2++)
                        {
                            for (int k3 = 9; k3 < 25; k3++)
                            {
                                world.setBlockState(new BlockPos(i - l2, j + i1, k - k3), Blocks.AIR.getDefaultState());
                            }
                        }
                    }

                    world.setBlockState(new BlockPos(i - 2, j, k - 2), Blocks.SANDSTONE.getDefaultState());
                    world.setBlockState(new BlockPos(i - 2, j + 1, k - 1), Blocks.AIR.getDefaultState());
                    world.setBlockState(new BlockPos(i - 2, j + 1, k - 2), Blocks.AIR.getDefaultState());
                    world.setBlockState(new BlockPos(i - 2, j + 2, k - 1), Blocks.AIR.getDefaultState());
                    world.setBlockState(new BlockPos(i - 2, j + 2, k - 2), Blocks.AIR.getDefaultState());
                    world.setBlockState(new BlockPos(i - 2, j + 1, k - 3), Blocks.SANDSTONE.getDefaultState());
                    world.setBlockState(new BlockPos(i - 2, j + 2, k - 3), Blocks.AIR.getDefaultState());
                    world.setBlockState(new BlockPos(i - 2, j + 2, k - 4), Blocks.SANDSTONE.getDefaultState());
                    world.setBlockState(new BlockPos(i - 2, j + 3, k - 4), Blocks.AIR.getDefaultState());

                    for (int j1 = 2; j1 < 18; j1++)
                    {
                        world.setBlockState(new BlockPos(i - 2, j + 3, k - j1), Blocks.AIR.getDefaultState());
                        world.setBlockState(new BlockPos(i - 2, j + 4, k - j1), Blocks.AIR.getDefaultState());
                        alternate *= -1;

                        if (alternate > 0)
                        {
                            world.setBlockState(new BlockPos(i - 2, j + 4, k - j1), Blocks.TORCH.getDefaultState());
                        }
                    }

                    for (int k1 = 2; k1 < 20; k1++)
                    {
                        world.setBlockState(new BlockPos(i - k1, j + 3, k - 17), Blocks.AIR.getDefaultState());
                        world.setBlockState(new BlockPos(i - k1, j + 4, k - 17), Blocks.AIR.getDefaultState());
                    }

                    for (int l1 = 9; l1 < 24; l1++)
                    {
                        alternate *= -1;

                        if (alternate > 0)
                        {
                            world.setBlockState(new BlockPos(i - 8, j + 8, k - l1), Blocks.TORCH.getDefaultState());
                            world.setBlockState(new BlockPos(i - 24, j + 8, k - l1), Blocks.TORCH.getDefaultState());
                        }

                        world.setBlockState(new BlockPos(i - l1, j + 8, k - 9), Blocks.TORCH.getDefaultState());
                        world.setBlockState(new BlockPos(i - l1, j + 8, k - 24), Blocks.TORCH.getDefaultState());
                    }

                    for (int i2 = 0; i2 < rand.nextInt(2) + 2; i2++)
                    {
                        EvilCreatureEntity creepsentityevilcreature = new EvilCreatureEntity(world);
                        creepsentityevilcreature.setLocationAndAngles(i - 15, j + 8, k - 10 - i2, rotationYaw, 0.0F);
                        world.addEntity(creepsentityevilcreature);
                    }

                    for (int j2 = 0; j2 < rand.nextInt(7) + 2; j2++)
                    {
                        MummyEntity creepsentitymummy = new MummyEntity(world);
                        creepsentitymummy.setLocationAndAngles(i - 15, j + 8, k - 13 - j2, rotationYaw, 0.0F);
                        world.addEntity(creepsentitymummy);
                    }

                    world.setBlockState(new BlockPos(i - 14, j + 3, k - 15), Blocks.SEA_LANTERN.getDefaultState());
                    world.setBlockState(new BlockPos(i - 16, j + 3, k - 15), Blocks.SEA_LANTERN.getDefaultState());
                    Block blockbed = this.bedGetter[rand.nextInt(bedGetter.length)];
                    int i3 = 0;
                    int l3 = 1;
                    int i4 = 0;
                    world.setBlockState(new BlockPos(i - 15, j + 3, k - 15), blockbed.getDefaultState(), i4);
                    world.setBlockState(new BlockPos((i - 15) + i3, j + 3, (k + l3) - 15), blockbed.getDefaultState(), i4 + 8);
                }
            }
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean processInteract(PlayerEntity playerentity, Hand hand)
    {
        world.playSound(playerentity, this.getPosition(), SoundsHandler.PYRAMID_CURSE, SoundCategory.VOICE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        return true;
    }

    public void makeMaze()
    {
        int l1 = 0;
        int i2 = 0;
        int ai[] = new int[(rows * columns) / 2];
        int ai1[] = new int[(rows * columns) / 2];

        for (int i = 0; i < rows; i++)
        {
            for (int i1 = 0; i1 < columns; i1++)
            {
                maze[i][i1] = wallCode;
            }
        }

        for (int j = 1; j < rows - 1; j += 2)
        {
            for (int j1 = 1; j1 < columns - 1; j1 += 2)
            {
                l1++;
                maze[j][j1] = -l1;

                if (j < rows - 2)
                {
                    ai[i2] = j + 1;
                    ai1[i2] = j1;
                    i2++;
                }

                if (j1 < columns - 2)
                {
                    ai[i2] = j;
                    ai1[i2] = j1 + 1;
                    i2++;
                }
            }
        }

        for (int k = i2 - 1; k > 0; k--)
        {
            int j2 = (int)(Math.random() * (double)k);

            if (ai[j2] % 2 == 1 && maze[ai[j2]][ai1[j2] - 1] != maze[ai[j2]][ai1[j2] + 1])
            {
                fill(ai[j2], ai1[j2] - 1, maze[ai[j2]][ai1[j2] - 1], maze[ai[j2]][ai1[j2] + 1]);
                maze[ai[j2]][ai1[j2]] = maze[ai[j2]][ai1[j2] + 1];
            }
            else if (ai[j2] % 2 == 0 && maze[ai[j2] - 1][ai1[j2]] != maze[ai[j2] + 1][ai1[j2]])
            {
                fill(ai[j2] - 1, ai1[j2], maze[ai[j2] - 1][ai1[j2]], maze[ai[j2] + 1][ai1[j2]]);
                maze[ai[j2]][ai1[j2]] = maze[ai[j2] + 1][ai1[j2]];
            }

            ai[j2] = ai[k];
            ai1[j2] = ai1[k];
        }

        for (int l = 1; l < rows - 1; l++)
        {
            for (int k1 = 1; k1 < columns - 1; k1++)
            {
                if (maze[l][k1] < 0)
                {
                    maze[l][k1] = emptyCode;
                }
            }
        }
    }

    public void tearDown(int i, int j)
    {
        if (i % 2 == 1 && maze[i][j - 1] != maze[i][j + 1])
        {
            fill(i, j - 1, maze[i][j - 1], maze[i][j + 1]);
            maze[i][j] = maze[i][j + 1];
        }
        else if (i % 2 == 0 && maze[i - 1][j] != maze[i + 1][j])
        {
            fill(i - 1, j, maze[i - 1][j], maze[i + 1][j]);
            maze[i][j] = maze[i + 1][j];
        }
    }

    public void fill(int i, int j, int k, int l)
    {
        if (i < 0)
        {
            i = 0;
        }

        if (j < 0)
        {
            i = 0;
        }

        if (i > rows)
        {
            i = rows;
        }

        if (j > columns)
        {
            j = columns;
        }

        if (maze[i][j] == k)
        {
            maze[i][j] = l;
            fill(i + 1, j, k, l);
            fill(i - 1, j, k, l);
            fill(i, j + 1, k, l);
            fill(i, j - 1, k, l);
        }
    }

    public void confetti(PlayerEntity player)
    {
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
        world.addEntity(creepsentitytrophy);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.PYRAMID;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damgagesourceIn)
    {
        return SoundsHandler.PYRAMID_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.PYRAMID_DEATH;
    }
}