package fr.elias.morecreeps.common.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class CastleGuardEntity extends MobEntity
{
    private int angerLevel;
    public String basetexture;
    public int attackdamage;
    public boolean isSwinging;
    public boolean swingArm;
    public int swingTick;
    public String texture;
    public boolean attacked;
    public float hammerswing;
    public float modelsize;
	private int randomSoundDelay;
    static final String guardTextures[] =
    {
        "/mob/creeps/castleguard1.png", "/mob/creeps/castleguard2.png", "/mob/creeps/castleguard3.png", "/mob/creeps/castleguard4.png", "/mob/creeps/castleguard5.png"
    };

    public CastleGuardEntity(World world)
    {
        super(null, world);
        angerLevel = 0;
        randomSoundDelay = 0;
        basetexture = guardTextures[rand.nextInt(guardTextures.length)];
        texture = basetexture;
        attacked = false;
        hammerswing = 0.0F;
        modelsize = 1.0F;
        attackdamage = 1;
    }
    
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (hammerswing < 0.0F)
        {
            hammerswing += 0.45F;
        }
        else
        {
            hammerswing = 0.0F;
        }

        super.tick();
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere(World world)
    {
    	AxisAlignedBB x = this.getBoundingBox();
    	
        return world.getDifficulty().getId() > 0
        		&& world.checkNoEntityCollision(getBoundingBox())
//        		&& world.getCollidingBoundingBoxes(this,  x).size() == 0
        		;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT nbttagcompound)
    {
        super.writeAdditional(nbttagcompound);
        nbttagcompound.putShort("Anger", (short)angerLevel);
        nbttagcompound.putBoolean("Attacked", attacked);
        nbttagcompound.putString("BaseTexture", basetexture);
        nbttagcompound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT nbttagcompound)
    {
        super.readAdditional(nbttagcompound);
        angerLevel = nbttagcompound.getShort("Anger");
        nbttagcompound.putBoolean("Attacked", attacked);
        nbttagcompound.putString("BaseTexture", basetexture);
        modelsize = nbttagcompound.getFloat("ModelSize");
        texture = basetexture;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getTrueSource();

        if (entity instanceof PlayerEntity)
        {
            attacked = true;
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
            double d2 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d2 * d2);
            moveForward = (float) ((d / (double)f1) * 0.20000000000000001D * (0.58000001192092898D + getMotion().x * 0.20000000298023224D));
            moveStrafing = (float) ((d2 / (double)f1) * 0.20000000000000001D * (0.52200000119209289D + getMotion().z * 0.20000000298023224D));
            moveVertical = (float) 0.19500000596246447D;
            fallDistance = -25F;
        }

        if ((double)f > 3D && rand.nextInt(10) == 0)
        {
            double d1 = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
            double d3 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);
            moveForward += d1 * 0.10999999940395355D;
            moveStrafing += d3 * 0.10999999940395355D;
            moveVertical += 0.023000000044703484D;
        }

        if ((double)f < 2.2999999999999998D - (1.0D - (double)modelsize) && entity.getBoundingBox().maxY > entity.getBoundingBox().minY && entity.getBoundingBox().minY < entity.getBoundingBox().maxY && !(entity instanceof CastleGuardEntity))
        {
            if (hammerswing == 0.0F)
            {
                hammerswing = -2.6F;
            }

            //attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackdamage);
        }
    }

    @SuppressWarnings("unused")
	private void becomeAngryAt(Entity entity)
    {
        this.attackEntity(entity, 1);
        angerLevel = 400 + rand.nextInt(400);
        randomSoundDelay = rand.nextInt(40);
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playAmbientSound()
    {
    	World world = Minecraft.getInstance().world;
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getAmbientSound()
    {
        if (attacked && rand.nextInt(5) == 0)
        {
            return SoundsHandler.CASTLE_GUARD_MAD;
        }

        if (rand.nextInt(12) == 0)
        {
            return SoundsHandler.CASTLE_GUARD;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.CASTLE_GUARD_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.CASTLE_GUARD_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);

        if (rand.nextInt(3) == 0)
        {
            entityDropItem(ItemList.donut, rand.nextInt(2) + 1);
        }
    }
}
