package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class BubbleScumEntity extends BaseCreepsCreatureEntity
{
    public boolean rideable;
    public int interest;
    public boolean tamed;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();
    public int armor;
    public boolean used;
    public boolean grab;
    @SuppressWarnings("rawtypes")
	public List piglist;
    public int pigstack;
    public int level;
    public float totaldamage;
    public int alt;
    public boolean hotelbuilt;
    public int wanderstate;
    public int speedboost;
    public int totalexperience;
    public int part;
    public boolean tossed;
    public float modelsize;
    public int attackTime;
    public String texture;
    static final int leveldamage[] =
    {
        0, 200, 600, 1000, 1500, 2000, 2700, 3500, 4400, 5400,
        6600, 7900, 9300, 10800, 12400, 14100, 15800, 17600, 19500, 21500,
        25000
    };
    static final String levelname[] =
    {
        "Guinea Pig", "A nothing pig", "An inexperienced pig", "Trainee", "Private", "Private First Class", "Corporal", "Sergeant", "Staff Sergeant", "Sergeant First Class",
        "Master Segeant", "First Sergeant", "Sergeant Major", "Command Sergeant Major", "Second Lieutenant", "First Lieutenant", "Captain", "Major", "Lieutenant Colonel", "Colonel",
        "General of the Pig Army", "General of the Pig Army"
    };

    public BubbleScumEntity(World world)
    {
        super(null, world);
        texture = Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "bubblescum.png";
//        setSize(0.6F, 0.6F);
        rideable = false;
        pigstack = 0;
        level = 1;
        totaldamage = 0.0F;
        alt = 1;
        hotelbuilt = false;
        wanderstate = 0;
        speedboost = 0;
        totalexperience = 0;
        fallDistance = -5F;
        tossed = false;
        modelsize = 1.0F;
//        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
//        this.tasks.addTask(1, new EntityAISwimming(this));
//        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
//        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
//        this.tasks.addTask(4, new EntityAILookIdle(this));
//        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, true, new Class[0]));
    }
    public void applyEntityAttributes()
    {
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6D);
    }
    //Don't need this anymore...
    /*protected void attackEntity(Entity entity, float f)
    {
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
        motionX = (d / (double)f1) * 0.40000000000000002D * 0.16000000192092895D + motionX * 0.18000000098023225D;
        motionZ = (d1 / (double)f1) * 0.40000000000000002D * 0.12000000192092895D + motionZ * 0.18000000098023225D;

        if ((double)f < 2D && entity.getBoundingBox().maxY > getBoundingBox().minY && entity.getBoundingBox().minY < getBoundingBox().maxY)
        {
            attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1);
        }
    }*/

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();
        knockBack(this, i, 3D, 5D);
        getAttackTarget();
        return super.attackEntityFrom(damagesource, i);
    }

    /**finally, i will not use this..**/
    /*public PlayerEntity getPlayer()
    {
        PlayerEntity playerentity = null;

        for (int i = 0; i < world.playerEntities.size(); ++i)
        {
            PlayerEntity playerentity1 = (PlayerEntity)world.playerEntities.get(i);

            if (IEntitySelector.NOT_SPECTATING.apply(playerentity1))
            {
            	playerentity = playerentity1;
            }
        }

        return playerentity;
    }*/
    
    public void onLivingUpdate(World world)
    {

        if (modelsize > 1.0F)
        {
            ignoreFrustumCheck = true;
        }
        PlayerEntity player = world.getClosestPlayer(this, 1);
        float f = fallDistance;

        //TODO find another player instance...
        if (f > 10F && tossed)
        {
            confetti();
            tossed = false;
            world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
            player.addStat(ModAdvancementList.ten_bubble, 1);
        }

        if (f > 25F && tossed)
        {
            confetti();
            tossed = false;
            world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
            player.addStat(ModAdvancementList.twenty_five_bubble, 1);
        }

        if (f > 50F && tossed)
        {
            confetti();
            tossed = false;
            world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
            player.addStat(ModAdvancementList.fifty_bubble, 1);
        }

        if (f > 100F && tossed)
        {
            confetti();
            tossed = false;
            world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
            player.addStat(ModAdvancementList.one_hundred_bubble, 1);
        }

        if (rand.nextInt(3) == 0)
        {
        	if(world.isRemote)
        	{
        		MoreCreepsReboot.proxy.bubble(world, this);
        	}
        }

        if (onGround)
        {
            tossed = false;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditonal(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditonal(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("ModelSize");
    }


    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @SuppressWarnings("unused")
    public boolean getCanSpawnHere(World world)
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        return
//        		world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 &&
        		rand.nextInt(5) == 0;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 12;
    }

    public double getYOffset()
    {
        if (getRidingEntity() instanceof PlayerEntity)
        {
            float f = 1.0F - modelsize;

            if (modelsize > 1.0F)
            {
                f *= 0.55F;
            }
            
            return (double)((getMountedYOffset() - 1.5F) + f * 0.6F);
        }
        else
        {
            return (double)getMountedYOffset();
        }
    }

    public void updateRiderPosition()
    {
        getRidingEntity().setPosition(posX, posY + getMountedYOffset() + getRidingEntity().getYOffset(), posZ);
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 0.5D;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(PlayerEntity playerentity)
    {
        ItemStack itemstack = playerentity.inventory.getCurrentItem();
        used = false;

        if (itemstack == null && !(getAttackTarget() instanceof PlayerEntity))
        {
            rotationYaw = playerentity.rotationYaw;
            Object obj = playerentity;

            if (getRidingEntity() != obj)
            {
                int i;

                for (i = 0; ((Entity)obj).getRidingEntity() != null && i < 20; i++)
                {
                    obj = ((Entity)obj).getRidingEntity();
                }

                if (i < 20)
                {
                    rotationYaw = ((Entity)obj).rotationYaw;
                    this.addPassenger((Entity)obj);
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.BUBBLE_SCUM_PICK_UP, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
            }
            else
            {
                int j;

                for (j = 0; ((Entity)obj).getRidingEntity() != null && j < 20; j++)
                {
                    obj = ((Entity)obj).getRidingEntity();
                }

                if (j < 20)
                {
                    rotationYaw = ((Entity)obj).rotationYaw;
                    ((Entity)obj).fallDistance = -5F;
                    ((Entity)obj).removePassengers();
                    double d = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
                    double d1 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
                    double d2 = -MathHelper.sin((playerentity.rotationPitch / 180F) * (float)Math.PI);
                    ((Entity)obj).setMotion(d, d1, d2);
//                    ((Entity)obj).motionX = 1.0D * d;
//                    ((Entity)obj).motionZ = 1.0D * d1;
//                    ((Entity)obj).motionY = 1.0D * d2;
                    tossed = true;
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.BUBBLE_SCUM_PUT_DOWN, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
            }
        }

        return true;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (getRidingEntity() == null)
        {
            if (rand.nextInt(1) == 0)
            {
                return SoundsHandler.BUBBLE_SCUM;
            }
            else
            {
                return null;
            }
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
        return SoundsHandler.BUBBLE_SCUM_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.BUBBLE_SCUM_DEATH;
    }

    public void confetti()
    {
    	MoreCreepsReboot.proxy.confettiA(this, world);
    }
}
