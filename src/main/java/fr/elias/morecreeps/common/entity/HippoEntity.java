package fr.elias.morecreeps.common.entity;

import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class HippoEntity extends MobEntity
{
    protected double attackrange;
    public boolean bone;
    protected int attack;
    public float goatsize;

    public int eaten;
    public boolean hungry;
    public int hungrytime;
    public int goatlevel;
    public double waterX;
    public double waterY;
    public double waterZ;
    public boolean findwater;
    public float modelsize;
    public boolean hippoHit;
    public String texture;

    public HippoEntity(World world)
    {
        super(null, world);
        bone = false;
        texture = Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "hippo2.png";
        setSize(2.0F, 1.4F);
        attack = 2;
        attackrange = 16D;
        hungry = false;
        findwater = false;
        hungrytime = rand.nextInt(100) + 10;
        goatlevel = 1;
        modelsize = 2.0F;
        hippoHit = false;
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, PlayerEntity.class, 0.45D, true));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, MobEntity.class, 0.45D, false));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, AnimalEntity.class, 0.45D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new HippoEntity.AIAttackEntity(this, PlayerEntity.class, true));
        this.targetTasks.addTask(2, new HippoEntity.AIAttackEntity(this, MobEntity.class, true));
        this.targetTasks.addTask(2, new HippoEntity.AIAttackEntity(this, AnimalEntity.class, true));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
    }

    public float func_180484_a(BlockPos bp, World world)
    {
        if (world.getBlockState(bp.down()).getBlock() == Blocks.water || world.getBlockState(bp.down()) == Blocks.flowing_water)
        {
            return 10F;
        }
        else
        {
            return -(float)bp.getY();
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected LivingEntity findPlayerToAttack()
    {
        if (!hippoHit)
        {
            return null;
        }

        if (world.getDifficulty() != Difficulty.PEACEFUL)
        {
            float f = getBrightness();

            if (f < 0.0F)
            {
                PlayerEntity entityplayer = world.getClosestPlayer(this, attackrange);

                if (entityplayer != null)
                {
                    return entityplayer;
                }
            }

            if (rand.nextInt(10) == 0)
            {
                LivingEntity entityliving = getClosestTarget(this, 15D);
                return entityliving;
            }
        }

        return null;
    }

    public LivingEntity getClosestTarget(Entity entity, double d)
    {
        double d1 = -1D;
        LivingEntity entityliving = null;

        for (int i = 0; i < world.loadedEntityList.size(); i++)
        {
            Entity entity1 = (Entity)world.loadedEntityList.get(i);

            if (!(entity1 instanceof LivingEntity) || entity1 == entity || entity1 == entity.getPassengers() || entity1 == entity.getRidingEntity() || (entity1 instanceof PlayerEntity) || (entity1 instanceof MobEntity) || (entity1 instanceof AnimalEntity))
            {
                continue;
            }

            double d2 = entity1.getDistanceSq(entity.posX, entity.posY, entity.posZ);

            if ((d < 0.0D || d2 < d * d) && (d1 == -1D || d2 < d1) && ((LivingEntity)entity1).canEntityBeSeen(entity))
            {
                d1 = d2;
                entityliving = (LivingEntity)entity1;
            }
        }

        return entityliving;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();
        hungry = false;
        hippoHit = true;

        if (entity != null)
        {
            setRevengeTarget((LivingEntity) entity);
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
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
            moveForward = (float) ((d / (double)f1) * 0.20000000000000001D * (0.850000011920929D + moveForward * 0.20000000298023224D));
            moveStrafing = (float) ((d1 / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + moveStrafing * 0.20000000298023224D));
            moveVertical = (float) 0.10000000596246449D;
            fallDistance = -25F;
        }
    }

    @SuppressWarnings("rawtypes")
	class AIAttackEntity extends NearestAttackableTargetGoal {

		@SuppressWarnings("unchecked")
		public AIAttackEntity(HippoEntity hippoEntity, Class p_i45878_2_,
				boolean p_i45878_3_) {
			super(hippoEntity, p_i45878_2_, p_i45878_3_);
		}

		@Override
		public boolean shouldExecute() {
			return hippoHit && HippoEntity.this.getAttackTarget() != null && super.shouldExecute();
		}
		public void startExecuting()
	    {
			HippoEntity.this.setAttackTarget(findPlayerToAttack());
	        super.startExecuting();
	    }
		
		public void updateTask()
		{
			double d0 = HippoEntity.this.getDistance(HippoEntity.this.getAttackTarget());
			if(d0 < 256D)
			{
				attackEntity(HippoEntity.this.getAttackTarget(), (float)d0);
			}
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
        //int l = world.getFullBlockLightValue(i, j, k);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.GRASS || i1 == Blocks.DIRT) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.STONE_SLAB /*&& i1 != Blocks.double_stone_slab*/ && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && world.canSeeSky(new BlockPos(i, j, k)) && rand.nextInt(35) == 0; //&& l > 7;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("Hungry", hungry);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        hungry = compound.getBoolean("Hungry");
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(PlayerEntity playerentity)
    {
        SoundEvent s = getLivingSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (2.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.HIPPO;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.HIPPO_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.HIPPO_DEATH;
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
                entityDropItem(Items.WHEAT_SEEDS, rand.nextInt(3) + 1);
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
