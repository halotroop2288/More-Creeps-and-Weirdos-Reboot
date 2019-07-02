package fr.elias.morecreeps.common.entity;

import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class RatManEntity extends MobEntity
{
    protected double attackrange;
    protected int attack;
    public boolean jumper;
    public float modelsize;
    public float modelspeed;
    public float stepHeight;
    public ResourceLocation texture;

    public RatManEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ratman.png");
        attack = 1;
        attackrange = 16D;
        jumper = false;
        modelsize = 0.75F;
        modelspeed = 0.61F;
        stepHeight = 1.0F;

        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // tasks.addTask(0, new EntityAISwimming(this));
        // tasks.addTask(1, new EntityAIBreakDoor(this));
        // tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.25D,
        // false));
        // tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.25D));
        // tasks.addTask(4, new EntityAIMoveThroughVillage(this, 0.25D, false));
        // tasks.addTask(5, new EntityAIWander(this, 0.25D));
        // tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        // tasks.addTask(6, new EntityAILookIdle(this));
        // targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        // targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
        // EntityPlayer.class, true));
    }

    @Override
    public void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    public void updateRiderPosition()
    {
        getRidingEntity().setPosition(posX, posY + getMountedYOffset() + getRidingEntity().getYOffset(), posZ);
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this
     * one.
     */
    @Override
    public double getMountedYOffset()
    {
        return 0.5D;
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void tick()
    {

        if (modelspeed < 0.05F)
        {
            modelspeed = 0.05F;
        }

        super.tick();

        if (handleWaterMovement())
        {
            setMotion(getMotion().x, 0.15999999642372131D, getMotion().z);
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this
     * creature will try to path to the block. Args: x, y, z
     */
    // the old "getBlockPathHeight" or something called like this
    public float func_180484_a(BlockPos bp, World world) {
        if (world.getBlockState(bp.down()).getBlock() == Blocks.SAND
                || world.getBlockState(bp.down()).getBlock() == Blocks.GRAVEL) {
            return 10F;
        } else {
            return -(float) bp.getY();
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
        {
            if (getPassengers() == entity || getRidingEntity() == entity)
            {
                return true;
            }

            if (entity != this && world.getDifficulty() != Difficulty.PEACEFUL)
            {
                setRevengeTarget((LivingEntity) entity);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this
     * entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        // int l = world.getFullBlockLightValue(i, j, k);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.SAND || i1 == Blocks.DIRT || i1 == Blocks.GRAVEL) && i1 != Blocks.COBBLESTONE
                && i1 != Blocks.OAK_LOG && i1 != Blocks.ACACIA_LOG && i1 != Blocks.BIRCH_LOG && i1 != Blocks.DARK_OAK_LOG && i1 != Blocks.JUNGLE_LOG
                && i1 != Blocks.STONE_SLAB && i1 != Blocks.SMOOTH_STONE_SLAB
                && i1 != Blocks.OAK_PLANKS && i1 != Blocks.ACACIA_PLANKS && i1 != Blocks.BIRCH_PLANKS && i1 != Blocks.DARK_OAK_PLANKS && i1 != Blocks.JUNGLE_PLANKS && i1 != Blocks.SPRUCE_PLANKS
                && i1 != Blocks.BLACK_WOOL && i1 != Blocks.GRAY_WOOL && i1 != Blocks.MAGENTA_WOOL && i1 != Blocks.BROWN_WOOL && i1 != Blocks.LIGHT_GRAY_WOOL && i1 != Blocks.LIGHT_BLUE_WOOL
                && i1 != Blocks.PINK_WOOL && i1 != Blocks.LIME_WOOL && i1 != Blocks.PURPLE_WOOL && i1 != Blocks.ORANGE_WOOL && i1 != Blocks.GREEN_WOOL && i1 != Blocks.BLUE_WOOL
                && i1 != Blocks.RED_WOOL&& i1 != Blocks.WHITE_WOOL && i1 != Blocks.YELLOW_WOOL
                // && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.canBlockSeeSky(new BlockPos(i, j, k));// && l > 6;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("ModelSpeed", modelspeed);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelspeed = compound.getFloat("ModelSpeed");
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (rand.nextInt(2) == 0)
        {
            return SoundsHandler.RAT_MAN;
        }
        else
        {
            return SoundsHandler.RAT_MAN_SCRATCH;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.RAT_MAN_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.RAT_MAN_HURT;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource)
    {
        World world = Minecraft.getInstance().world;
    	if(!world.isRemote)
    	{
            if (rand.nextInt(10) == 0)
            {
                entityDropItem(Items.PORKCHOP, rand.nextInt(3) + 1);
            }

            if (rand.nextInt(10) == 0)
            {
                entityDropItem(Items.WHEAT_SEEDS, rand.nextInt(3) + 1);
            }
    	}

        super.onDeath(damagesource);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }
}