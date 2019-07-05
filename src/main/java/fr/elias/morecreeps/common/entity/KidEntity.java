package fr.elias.morecreeps.common.entity;

import java.util.List;

import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class KidEntity extends AnimalEntity
{
    protected double attackrange;
    protected int attack;
    public float modelsize;
    public int checktimer;
    public ResourceLocation texture;

    public KidEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "kid.png");
        attack = 1;
        attackrange = 16D;
        checktimer = 0;
        modelsize = 0.6F;
//        setEntitySize(getWidth() * modelsize, getHeight() * modelsize);
//        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
//        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(1, new KidEntity.AIAttackEntity());
//        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
//        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
//        this.tasks.addTask(4, new EntityAILookIdle(this));
//        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
    }

    public void setEntitySize()
    {
//    	setSize(getWidth(), getHeight());
    }
    
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.55D);
    }

    public float getShadowSize()
    {
        return 0.6F - modelsize;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        if (getRidingEntity() != null)
        {
            return false;
        }
        else
        {
            return super.isEntityInsideOpaqueBlock();
        }
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        if (getRidingEntity() instanceof PlayerEntity)
        {
            float f = 0.6F - modelsize;

            if (modelsize > 1.0F)
            {
                f *= 0.55F;
            }

            return (double)1.5F + f;
        }

        if (getRidingEntity() instanceof LollimanEntity)
        {
            return (double)((2.6F + (0.6F - modelsize)) - (2.0F - ((LollimanEntity)getRidingEntity()).modelsize) * 2.75F);
        }
        else
        {
            return (double)1.0;
        }
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
        return 1.75D;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @SuppressWarnings("rawtypes")
    @Override
	public void livingTick()
    {
    	World world = Minecraft.getInstance().world;
    	PlayerEntity playerentity = Minecraft.getInstance().player;
        super.livingTick();

        if (modelsize > 1.0F)
        {
            ignoreFrustumCheck = true;
        }

        if (handleWaterMovement())
        {
            moveVertical = 0.15999999642372131F;
        }

        if (getRidingEntity() != null && checktimer-- < 0)
        {
            checktimer = 60;
            List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(8D, 8D, 8D));

            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);

                if (entity instanceof LollimanEntity)
                {
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.KID_FIND, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (0.6F - modelsize) * 2.0F);
                }
            }
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float func_180484_a(BlockPos bp, World world)
    {
        if (world.getBlockState(bp.down()).getBlock() == Blocks.SAND || world.getBlockState(bp.down()).getBlock() == Blocks.GRAVEL)
        {
            return 10F;
        }
        else
        {
            return -(float)bp.getY();
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(PlayerEntity playerentity)
    {
        if (!(getAttackTarget() instanceof PlayerEntity))
        {
            rotationYaw = playerentity.rotationYaw;
            Object obj = playerentity;

            if (getRidingEntity() != obj)
            {
                rotationYaw = ((Entity)obj).rotationYaw;
                addPassenger((Entity)obj);
                world.playSound(this, "morecreeps:kidup", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (0.6F - modelsize) * 2.0F);
            }
            else
            {
                obj = ((Entity)obj).riddenByEntity;
                rotationYaw = ((Entity)obj).rotationYaw;
                ((Entity)obj).fallDistance = -25F;
                ((Entity)obj).removePassengers();
                double d = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
                double d1 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
                ((Entity)obj).setMotion(d * 0.60000002384185791D, getMotion().y, d1 * 0.60000002384185791D);
                world.playSound(playerentity, this.getPosition(), SoundsHandler.KID_DOWN, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (0.6F - modelsize) * 2.0F);
            }
        }
        else
        {
            world.playSound(this, "morecreeps:kidnontpickup", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (0.6F - modelsize) * 2.0F);
        }

        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();
        setRevengeTarget((LivingEntity) entity);
        return super.attackEntityFrom(damagesource, i);
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
            motionX = (d / (double)f1) * 0.20000000000000001D * (0.850000011920929D + motionX * 0.20000000298023224D);
            motionZ = (d1 / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + motionZ * 0.20000000298023224D);
            motionY = 0.10000000596246449D;
            fallDistance = -25F;
        }
    }

    @SuppressWarnings("rawtypes")
	class AIAttackEntity extends Brain
    {
		@Override
		public boolean shouldExecute()
		{
			return KidEntity.this.getAttackTarget() != null;
		}
		
		public void updateTask()
		{
			float f = KidEntity.this.getDistance(getAttackTarget());
			if(f < 256F)
			{
				attackEntity(KidEntity.this.getAttackTarget(), f);
				KidEntity.this.getLookController().setLookPositionWithEntity(KidEntity.this.getAttackTarget(), 10.0F, 10.0F);
				KidEntity.this.getNavigator().clearPath();
				KidEntity.this.getMoveHelper().setMoveTo(KidEntity.this.getAttackTarget().posX, KidEntity.this.getAttackTarget().posY, KidEntity.this.getAttackTarget().posZ, 0.5D);
			}
			if(f < 1F)
			{
				KidEntity.this.attackEntityAsMob(KidEntity.this.getAttackTarget());
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
        return (i1 == Blocks.GRASS_BLOCK || i1 == Blocks.SAND || i1 == Blocks.DIRT || i1 == Blocks.GRAVEL) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(15) == 0;// && l > 6;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playAmbientSound()
    {
    	PlayerEntity playerentity = Minecraft.getInstance().player;
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (0.6F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (getAttackTarget() instanceof PlayerEntity)
        {
            return null;
        }

        if (rand.nextInt(5) == 0)
        {
            int i = MathHelper.floor(posX);
            int j = MathHelper.floor(getBoundingBox().minY);
            int k = MathHelper.floor(posZ);
            Block l = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
            Block i1 = world.getBlockState(new BlockPos(i, j, k)).getBlock();

            if (i1 == Blocks.SNOW || l == Blocks.SNOW || l == Blocks.ICE)
            {
                return SoundsHandler.KID_COLD;
            }
        }

        if (getRidingEntity() != null)
        {
            return SoundsHandler.KID_RIDE;
        }
        else
        {
            return SoundsHandler.KID;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.KID_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.KID_DEATH;
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
        return 1;
    }

	@Override
	public AgeableEntity createChild(AgeableEntity ageable)
	{
		return new KidEntity(world); // Uhhhmmmm????? Baby kids? Implications????
	}
}
