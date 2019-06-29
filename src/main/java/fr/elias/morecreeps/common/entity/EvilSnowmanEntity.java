package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class EvilSnowmanEntity extends MobEntity
{
    public float snowsize;
    public String texture;

    public EvilSnowmanEntity(World world)
    {
        super(world);
        texture = "morecreeps:textures/entity/evilsnowman.png";
        setSize(0.7F, 1.5F);
        snowsize = 1.0F;
        isImmuneToFire = true;
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new EvilSnowmanEntity.AIAttackTarget());
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, PlayerEntity.class, true));
    }

    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
    	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1D);
    }
    
    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate(World world)
    {
        super.onLivingUpdate();

        if (!onGround && !isJumping)
        {
            motionY -= 0.0020000000949949026D;
        }

        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(getEntityBoundingBox().minY);
        int k = MathHelper.floor_double(posZ);
        Block l = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        Block i1 = world.getBlockState(new BlockPos(i, j, k)).getBlock();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(snowsize + 1);

        if (/*l > 77 && l < 81 || */i1 == Blocks.snow)
        {
            snowsize += 0.001F;
        }
        else
        {
            snowsize -= 0.002F;
        }

        if (inWater)
        {
            snowsize -= 0.02F;
        }

        if (snowsize > 6F)
        {
            snowsize = 6F;
        }

        setSize(snowsize * 0.45F, snowsize * 2.0F);

        if ((double)snowsize < 0.050000000000000003D)
        {
            setDead();
        }
    }

    public float getShadowSize()
    {
        return snowsize * 0.4F;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f, World world)
    {
        if (onGround)
        {
            for (int i = 0; i < 8; i++)
            {
                world.playSoundAtEntity(this, "morecreeps:snowmanbounce", 1.0F, 2.0F - snowsize);
                world.spawnParticle(EnumParticleTypes.SNOWBALL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
            }

            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            motionX = (d / (double)f1) * 0.5D * 0.30000000192092896D + motionX * 0.20000000098023224D;
            motionZ = (d1 / (double)f1) * 0.5D * 0.25000000192092897D + motionZ * 0.20000000098023224D;
            motionY = 0.35000000196046449D;

            if (rand.nextInt(20) == 0)
            {
                
                double d2 = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
                double d5 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
                motionX -= d2 * 0.40000000596046448D;
                motionZ -= d5 * 0.40000000596046448D;
            }

            if (rand.nextInt(20) == 0)
            {
                double d3 = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
                double d6 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
                motionX -= d3 * 1.0D;
                motionY += 0.16599999368190765D;
            }

            if (rand.nextInt(40) == 0)
            {
                double d4 = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
                double d7 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
                motionX -= d4 * 0.30000001192092896D;
                motionZ -= d7 * 0.30000001192092896D;
                motionY += 0.76599997282028198D;
            }
        }
    }

    public class AIAttackTarget extends Entity {

    	public EvilSnowmanEntity evilsnowman = EvilSnowmanEntity.this;
    	public int attackTime;
    	public AIAttackTarget() {}
    	
		@Override
		public boolean shouldExecute() {
            LivingEntity entitylivingbase = this.evilsnowman.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isAlive();
		}
        public void updateTask()
        {
        	--attackTime;
            LivingEntity entitylivingbase = this.evilsnowman.getAttackTarget();
            double d0 = this.evilsnowman.getDistanceSq(entitylivingbase);

            if (d0 < 4.0D)
            {
                if (this.attackTime <= 0)
                {
                    this.attackTime = 20;
                    this.evilsnowman.attackEntityAsMob(entitylivingbase);
                }
                
                this.evilsnowman.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            else if (d0 < 256.0D)
            {
                // ATTACK ENTITY GOES HERE
                evilsnowman.attackEntity(entitylivingbase, (float)d0);
                this.evilsnowman.getMoveHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            }
            else
            {
                this.evilsnowman.getNavigator().clearPath();
                this.evilsnowman.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
            }
        }
    }
    
    /**
     * knocks back this entity
     */
    public void knockBack(Entity entity, int i, double d, double d1, World world)
    {
        for (int j = 0; j < 8 + (int)(snowsize * 20F); j++)
        {
            world.addParticle(ParticleTypes.ITEM_SNOWBALL, posX, posY + (double)snowsize, posZ, 0.0D, 0.0D, 0.0D);
        }

        i *= i;
        motionY += 0.33300000429153442D;
        d *= 8.1999998092651367D;
        d1 *= 8.3000001907348633D;
        super.knockBack(entity, i, d, d1);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();
        PlayerEntity playerentitysp = (PlayerEntity) entity;
        double d = -MathHelper.sin((playerentitysp.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((playerentitysp.rotationYaw * (float)Math.PI) / 180F);
        motionX += d * 2D;
        motionZ += d1 * 2D;
        snowsize -= 0.02F;

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
        {
            if (getRidingEntity() == entity)
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
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    public void fall(float distance, float damageMultiplier)
    {
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return true;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
    	PlayerEntity playerentity = (PlayerEntity) damagesource.getTrueSource();
        boolean flag = false;

        if (!((ServerPlayerEntity)playerentity).getStats().hasAdvancementUnlocked(MoreCreepsReboot.snowtiny) && snowsize < 0.1F)
        {
        	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
            playerentity.addStat(MoreCreepsReboot.snowtiny, 1);
            confetti();
            flag = true;
        }

        if (!((ServerPlayerEntity)playerentity).getStatFile().hasAdvancementUnlocked(MoreCreepsReboot.snowtall) && snowsize > 5F)
        {
            world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
            playerentity.addStat(MoreCreepsReboot.snowtall, 1);
            confetti();
            flag = true;
        }

        if (!((ServerPlayerEntity)playerentity).getStatFile().hasAdvancementUnlocked(MoreCreepsReboot.snow) && !flag)
        {
        	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
            playerentity.addStat(MoreCreepsReboot.snow, 1);
            confetti();
        }

        if(!world.isRemote)
        {
            if (rand.nextInt(10) == 0)
            {
                entityDropItem(Blocks.ICE, rand.nextInt(3) + 1);
                entityDropItem(Blocks.SNOW, rand.nextInt(10) + 1);
            }
            else
            {
                entityDropItem(Blocks.SNOW, rand.nextInt(5) + 2);
            }
        }

        super.onDeath(damagesource);
    }

    public void confetti(World world)
    {
    	MoreCreepsReboot.proxy.confettiA(this, world);
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(World world, PlayerEntity playerentity)
    {
        SoundEvent s = getLivingSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - snowsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.SNOW_MAN;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.SNOW_MAN_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.SNOW_MAN_DEATH;
    }
}
