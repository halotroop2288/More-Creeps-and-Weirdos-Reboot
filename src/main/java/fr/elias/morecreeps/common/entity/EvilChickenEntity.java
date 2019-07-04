package fr.elias.morecreeps.common.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.lists.ItemList;

public class EvilChickenEntity extends MobEntity
{
    public boolean field_753_a;
    public float field_752_b;
    public float destPos;
    public float field_757_d;
    public float field_756_e;
    public float field_755_h;
    public int timeUntilNextEgg;

    public float modelsize;
    public String texture;

    public EvilChickenEntity(World world)
    {
        super(world);
        field_753_a = false;
        field_752_b = 0.0F;
        destPos = 0.0F;
        field_755_h = 1.0F;
        texture = "morecreeps:textures/entity/evilchicken.png";
        timeUntilNextEgg = rand.nextInt(300) + 50;
        isImmuneToFire = true;
        modelsize = 1.5F;
//        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, PlayerEntity.class, 0.3D, false));
//        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
//        this.tasks.addTask(6, new EntityAIWatchClosest(this, PlayerEntity.class, 6.0F));
//        this.tasks.addTask(7, new EntityAILookIdle(this));
//        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, PlayerEntity.class, true));
    }

    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
    	this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }
    
    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick(World world)
    {
        super.livingTick();
        field_756_e = field_752_b;
        field_757_d = destPos;
        destPos += (double)(onGround ? -1 : 4) * 0.29999999999999999D;

        if (destPos < 0.0F)
        {
            destPos = 0.0F;
        }

        if (destPos > 1.0F)
        {
            destPos = 1.0F;
        }

        if (!onGround && field_755_h < 1.0F)
        {
            field_755_h = 1.0F;
        }

        field_755_h *= 0.90000000000000002D;

        if (!onGround && motionY < 0.0D)
        {
            motionY *= 0.59999999999999998D;
        }

        field_752_b += field_755_h * 2.0F;

        if (!world.isRemote && --timeUntilNextEgg <= 0)
        {
            world.playSound(this, "morecreeps:evileggbirth", 1.0F, 1.0F);
            world.playSound(this, "mob.chickenplop", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            dropItem(ItemList.evil_egg, 1);
            timeUntilNextEgg = rand.nextInt(600) + 200;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    /*
    protected void attackEntity(Entity entity, float f)
    {
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
        motionX = (d / (double)f1) * 0.40000000000000002D * 0.40000000192092894D + motionX * 0.18000000098023225D;
        motionZ = (d1 / (double)f1) * 0.40000000000000002D * 0.30000000192092896D + motionZ * 0.18000000098023225D;

        if ((double)f < 2.2999999999999998D - (double)(2.0F - 2.0F * modelsize) && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 10;
            entity.motionX *= 1.2999999523162842D;
            entity.motionZ *= 1.2999999523162842D;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackStrength);
        }

        super.attackEntity(entity, f);
    }*/

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i, World world)
    {
        LivingEntity entity = (LivingEntity) damagesource.getTrueSource();
        PlayerEntity player = (PlayerEntity) entity;
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        moveForward = (float) (d * 4D);
        moveStrafing = (float) (d1 * 4D);

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
        {
            if (getPassengers() == entity || getRidingEntity() == entity)
            {
                return true;
            }

            if (entity != this && world.getDifficulty() != Difficulty.PEACEFUL)
            {
                setRevengeTarget(entity);
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
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return true;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    public void fall(float distance, float damageMultiplier) {}

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.chicken.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.chicken.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.chicken.hurt";
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected Item getDropItem()
    {
        return Items.FEATHER;
    }
}
