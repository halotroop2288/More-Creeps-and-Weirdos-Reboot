package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RatManEntity extends MobEntity
{
    protected double attackrange;
    protected int attack;
    public boolean jumper;
    public float modelsize;
    public float modelspeed;
    public float stepHeight;
    public String texture;

    public RatManEntity(World world)
    {
        super(world);
        texture = "morecreeps:textures/entity/ratman.png";
        attack = 1;
        attackrange = 16D;
        jumper = false;
        modelsize = 0.75F;
        modelspeed = 0.61F;
        stepHeight = 1.0F;

        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIBreakDoor(this));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.25D, false));
        tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.25D));
        tasks.addTask(4, new EntityAIMoveThroughVillage(this, 0.25D, false));
        tasks.addTask(5, new EntityAIWander(this, 0.25D));
        tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }
    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(7D);
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1D);
    }

    

    public void updateRiderPosition()
    {
        riddenByEntity.setPosition(posX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ);
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 0.5D;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
    	
    	if (modelspeed < 0.05F)
        {
            modelspeed = 0.05F;
        }
    	
        super.onLivingUpdate();

        if (handleWaterMovement())
        {
            motionY = 0.15999999642372131D;
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    //the old "getBlockPathHeight" or something called like this
    public float func_180484_a(BlockPos bp, World world)
    {
        if (world.getBlockState(bp.down()).getBlock() == Blocks.sand || world.getBlockState(bp.down()).getBlock() == Blocks.gravel)
        {
            return 10F;
        }
        else
        {
            return -(float)bp.getY();
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
            if (riddenByEntity == entity || ridingEntity == entity)
            {
                return true;
            }

            if (entity != this && world.getDifficulty() != EnumDifficulty.PEACEFUL)
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
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(getBoundingBox().minY);
        int k = MathHelper.floor_double(posZ);
        //int l = world.getFullBlockLightValue(i, j, k);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.SAND || i1 == Blocks.DIRT || i1 == Blocks.GRAVEL) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG/* TODO fill in with other log types*/ && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS/*TODO fill in with other plank types*/ && i1 != Blocks.WHITE_WOOL /*TODO fill in with other wool types*/ && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && world.canSeeSky(new BlockPos(i, j, k));// && l > 6;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("ModelSpeed", modelspeed);
        nbttagcompound.setFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        modelspeed = nbttagcompound.getFloat("ModelSpeed");
        modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        if (rand.nextInt(2) == 0)
        {
            return "morecreeps:ratman";
        }
        else
        {
            return "morecreeps:ratmanscratch";
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "morecreeps:ratmanhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "morecreeps:ratmanhurt";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
            if (rand.nextInt(10) == 0)
            {
                dropItem(Items.porkchop, rand.nextInt(3) + 1);
            }

            if (rand.nextInt(10) == 0)
            {
                dropItem(Items.wheat_seeds, rand.nextInt(3) + 1);
            }
    	}

        super.onDeath(damagesource);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }
}