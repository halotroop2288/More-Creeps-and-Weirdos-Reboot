package fr.elias.morecreeps.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class HunchbackSkeletonEntity extends MobEntity
{
    private static final ItemStack defaultHeldItem;
    public boolean hasAttacked;
    public int weapon;
    public int timeleft;
    public String ss;
    public float modelsize;
    public String texture;
    public double health;

    public HunchbackSkeletonEntity(World world)
    {
        super(null, world);
        texture = "/mob/creeps/hunchbackskeleton1.png";
        health = rand.nextInt(10) + 10;
        timeleft = rand.nextInt(500) + 200;
        modelsize = 1.0F;
//        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
//        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, PlayerEntity.class, 0.45D, true));
//        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
//        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
//        this.tasks.addTask(8, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
//        this.tasks.addTask(8, new EntityAILookIdle(this));
//        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
    }
    
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
    }

    

    public HunchbackSkeletonEntity(World world, Entity entity, double d, double d1, double d2, boolean flag)
    {
        this(world);
        setPosition(d, d1, d2);
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(World world)
    {
        String s = getLivingSound();

        if (s != null)
        {
            world.playSound(this, s, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.skeleton";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.skeletonhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.skeletonhurt";
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void livingTick()
    {
        super.livingTick();

        if (rand.nextInt(2) == 0)
        {
            timeleft--;
        }

        if (timeleft < 500 && timeleft > 300)
        {
            texture = "/mob/creeps/hunchbackskeleton2.png";
        }
        else if (timeleft < 300 && timeleft > 200)
        {
            texture = "/mob/creeps/hunchbackskeleton3.png";
        }
        else if (timeleft < 200 && timeleft > 100)
        {
            texture = "/mob/creeps/hunchbackskeleton4.png";
        }
        else if (timeleft < 100 && timeleft > 1)
        {
            texture = "/mob/creeps/hunchbackskeleton5.png";
        }
        else if (timeleft < 1)
        {
            smoke();
            health = 0;
            setHealth(0);
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
    	LivingEntity entityliving = getClosestTarget(this, 16D);

        if ((this.getAttackTarget() instanceof PlayerEntity) || (this.getAttackTarget() instanceof HunchbackSkeletonEntity))
        {
            return null;
        }
        else
        {
            return entityliving;
        }
    }

    public LivingEntity getClosestTarget(Entity entity, double d)
    {
        double d1 = -1D;
        LivingEntity entityliving = null;

        for (int i = 0; i < world.loadedEntityList.size(); i++)
        {
            Entity entity1 = (Entity)world.loadedEntityList.get(i);

            if (!(entity1 instanceof LivingEntity) || entity1 == entity || entity1 == entity.riddenByEntity || entity1 == entity.getRidingEntity() || (entity1 instanceof PlayerEntity) || (entity1 instanceof GuineaPigEntity) || (entity1 instanceof SnowDevilEntity) || (entity1 instanceof HunchbackSkeletonEntity) || (entity1 instanceof HunchbackEntity))
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
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f, World world)
    {
        if ((entity instanceof PlayerEntity) || (entity instanceof HunchbackEntity) || (entity instanceof HunchbackSkeletonEntity))
        {
            this.setAttackTarget(null);
            return;
        }

        if (f < 10F)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;

            
            	ArrowEntity entityarrow = new ArrowEntity(world, this);
                entityarrow.posY += 1.3999999761581421D;
                double d2 = entity.posY - 0.20000000298023224D - entityarrow.posY;
                float f1 = MathHelper.sqrt(d * d + d1 * d1) * 0.2F;
                world.playSound(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
                world.addEntity(entityarrow);
                entityarrow.setThrowableHeading(d, d2 + (double)f1, d1, 0.6F, 12F);
            

            rotationYaw = (float)((Math.atan2(d1, d) * 180D) / Math.PI) - 90F;
            hasAttacked = true;
        }
    }

    private void smoke()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), posY + (double)(rand.nextFloat() * getHeight()) + (double)i, (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), d, d1, d2);
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Item.getIdFromItem(Items.ARROW);
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems()
    {
        if (rand.nextInt(10) == 0)
        {
            if (rand.nextInt(2) == 0)
            {
                entityDropItem(Items.ARROW, rand.nextInt(3));
            }

            if (rand.nextInt(2) == 0)
            {
                entityDropItem(Items.BONE, rand.nextInt(2));
            }
        }
    }

    /**
     * Returns the item that this LivingEntity is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return defaultHeldItem;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return true;
    }

    static
    {
        defaultHeldItem = new ItemStack(Items.BOW, 1);
    }
}
