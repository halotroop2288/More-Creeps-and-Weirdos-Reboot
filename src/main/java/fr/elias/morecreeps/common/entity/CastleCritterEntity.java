package fr.elias.morecreeps.common.entity;

import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class CastleCritterEntity extends MobEntity
{
    protected double attackrange;
    protected int attack;
    public float modelsize;
    public String texture;
    public int attackTime;

    public CastleCritterEntity(World world)
    {
        super(null, world);
        texture = "morecreeps:textures/entity/castlecritter.png";
//        setSize(0.6F, 0.6F);
        attack = 1;
        attackrange = 16D;
        modelsize = 1.6F;
//        this.targetTasks.addTask(0, new CastleCritterEntity.AIAttackEntity());
    }

    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
    	
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(BlockPos bp, World world)
    {
        if (world.getBlockState(bp.down()).getBlock() == Blocks.SMOOTH_STONE_SLAB || world.getBlockState(bp.down()).getBlock() == Blocks.STONE_SLAB)
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
            if (getPassengers() == entity || getRidingEntity() == entity)
            {
                return true;
            }

            if (entity != this && world.getDifficulty() != Difficulty.PEACEFUL)
            {
                this.setRevengeTarget((LivingEntity) entity);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (onGround)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d1 * d1);
            moveForward = (float) ((d / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + getMotion().x * 0.20000000298023224D));
            moveStrafing = (float) ((d1 / (double)f1) * 0.20000000000000001D * (0.75000001192092891D + getMotion().z * 0.20000000298023224D));
            moveVertical = (float) 0.10000000596246449D;
            fallDistance = -25F;

            if (rand.nextInt(5) == 0)
            {
                double d2 = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
                double d3 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);
                moveForward += d2 * 0.64999997615814209D;
                moveStrafing += d3 * 0.64999997615814209D;
            }
        }
    }
    class AIAttackEntity extends EntityAINearestAttackableTarget
    {
        public AIAttackEntity()
        {
            super(CastleCritterEntity.this, PlayerEntity.class, true);
        }
        
        public boolean shouldExecute()
        {
        	LivingEntity target = CastleCritterEntity.this.getAttackTarget();
        	return target != null && super.shouldExecute();
        }
        public void updateTask()
        {
        	float f = CastleCritterEntity.this.getDistance(CastleCritterEntity.this.getAttackTarget());
        	CastleCritterEntity.this.attackEntity(CastleCritterEntity.this.getAttackTarget(), f);
        }
    }
    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE
        		&& i1 != Blocks.OAK_LOG
        		&& i1 != Blocks.OAK_PLANKS
        		&& i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getEntityBoundingBox()).size() == 0
        		;
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playAmbientSound()
    {
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	World world = Minecraft.getInstance().world;
    	
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.6F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (rand.nextInt(5) == 0)
        {
            return SoundsHandler.CASTLE_CRITTER;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.CASTLE_CRITTER_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.CASTLE_CRITTER_DEATH;
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
                entityDropItem(Items.PORKCHOP, rand.nextInt(3) + 1);
            }

            if (rand.nextInt(10) == 0)
            {
                entityDropItem(Items.BONE, rand.nextInt(3) + 1);
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
