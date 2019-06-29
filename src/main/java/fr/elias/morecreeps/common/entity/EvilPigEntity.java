package fr.elias.morecreeps.common.entity;

import fr.elias.morecreeps.common.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class EvilPigEntity extends MobEntity
{
	
    public float modelsize;
    public ResourceLocation texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "evilpig.png");
    public float width = getWidth();
    public float height = getHeight();
    public float length = getWidth();

    public EvilPigEntity(World world)
    {
        super(null, world);
        // setSize(width * 2.2F, height * 1.6F);
        modelsize = 1.0F;
        fallDistance = -25F;
        // ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(2, new EntityAIAttackOnCollide(this, PlayerEntity.class, 0.45D, true));
        // this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(7, new EntityAIWatchClosest(this, PlayerEntity.class, 6.0F));
        // this.tasks.addTask(8, new EntityAILookIdle(this));
    }
    
    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /*protected void attackEntity(Entity entity, float f)
    {
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
        motionX = (d / (double)f1) * 0.40000000000000002D * 0.50000000192092897D + motionX * 0.18000000098023225D;
        motionZ = (d1 / (double)f1) * 0.40000000000000002D * 0.34000000192092894D + motionZ * 0.18000000098023225D;

        if ((double)f < 2.5D - (2D - (double)modelsize) && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackStrength);
        }

        super.attackEntity(entity, f);
    }*/

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i, World world)
    {
        Entity entity = damagesource.getTrueSource();
        PlayerEntity player = (PlayerEntity) entity;
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        moveForward += d * 2D;
        moveStrafing += d1 * 2D;

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
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT nbttagcompound)
    {
        super.writeAdditional(nbttagcompound);
        nbttagcompound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT nbttagcompound)
    {
        super.readAdditional(nbttagcompound);
        modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playAmbientSound()
    {
        PlayerEntity player = Minecraft.getInstance().player;
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(player, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_PIG_DEATH;
    }
}
