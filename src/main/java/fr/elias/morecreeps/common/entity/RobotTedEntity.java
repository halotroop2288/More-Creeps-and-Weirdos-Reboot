package fr.elias.morecreeps.common.entity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;

public class RobotTedEntity extends MobEntity
{
    public static Random rand = new Random();
    protected double attackRange;
    private int angerLevel;
    public boolean jumping;
    public float robotsize;
    public int floattimer;
    public int floatdir;
    protected Entity playerToAttack;
    protected boolean hasAttacked;
    public float modelspeed;
    public double floatcycle;
    public double floatmaxcycle;
    public double motionX = getMotion().x;
    public double motionY = getMotion().y;
    public double motionZ = getMotion().z;
    public float width = getWidth();
    public float length = getWidth();
    public float height = getHeight();

    public String texture;
    
    public RobotTedEntity(World world)
    {
        super(null, world);
        texture = "morecreeps:textures/entity/robotted.png";
        angerLevel = 0;
        attackRange = 16D;
        jumping = false;
        robotsize = 2.5F;
        // setSize(width * (robotsize * 0.8F), height * (robotsize * 0.8F));
        modelspeed = 0.61F;
        floattimer = 0;
        floatdir = 1;
        floatcycle = 0.0D;
        floatmaxcycle = 0.10499999672174454D;
        
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // tasks.addTask(0, new EntityAISwimming(this));
        // tasks.addTask(1, new EntityAIBreakDoor(this));
        // tasks.addTask(2, new AIAttackEntity()); 
        // tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.061D));
        // tasks.addTask(5, new EntityAIWander(this, 0.25D));
        // tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        // tasks.addTask(6, new EntityAIWatchClosest(this, RobotToddEntity.class, 8F));
        // tasks.addTask(7, new EntityAILookIdle(this));
        // targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        // targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        // targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, RobotToddEntity.class, true));
    }

    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(45D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.061D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    public float getEyeHeight() {
        return 2.0F;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("RobotSize", robotsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        robotsize = compound.getFloat("RobotSize");
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this
     * entity.
     */
    public boolean getCanSpawnHere()
    {
        World world = Minecraft.getInstance().world;
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getLight(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.log && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.STONE_SLAB
                && i1 != Blocks.planks && i1 != Blocks.wool
                // && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(10) == 0 && l > 8;
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void livingTick()
    {
        World world = Minecraft.getInstance().world;
        super.livingTick();

        if (modelspeed < 0.05F)
        {
            modelspeed = 0.05F;
        }

        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(modelspeed);
        isJumping = false;

        if(world.isRemote)
        {
        	MoreCreepsReboot.proxy.robotTedSmoke(world, posX, posY, posZ, floattimer, modelspeed);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (isEntityInsideOpaqueBlock())
        {
            posY += 2.5D;
            floatdir = 1;
            floatcycle = 0.0D;
        }

        if (floatdir > 0)
        {
            floatcycle += 0.017999999225139618D;

            if (floatcycle > floatmaxcycle)
            {
                floatdir = floatdir * -1;
                fallDistance += -1.5F;
            }
        }
        else
        {
            floatcycle -= 0.0094999996945261955D;

            if (floatcycle < -floatmaxcycle)
            {
                floatdir = floatdir * -1;
                fallDistance += -1.5F;
            }
        }

        super.tick();
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (entity instanceof PlayerEntity)
        {
            double d = getDistance(entity);

            if (posY > entity.posY && d < 6D)
            {
                motionY = -0.02500000037252903D;
            }

            if (posY < entity.posY - 0.5D && d < 6D)
            {
                motionY = 0.043999999761581421D;
            }

            if (d < 3D)
            {
                double d2 = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
                double d4 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
                motionX = -(d2 * 0.20000000298023224D);
                motionZ = -(d4 * 0.20000000298023224D);

                if (posY > entity.posY)
                {
                    motionY = -0.070000000298023224D;
                }
            }
        }

        fallDistance = -25F;
        double d1 = entity.posX - posX;
        double d3 = entity.posZ - posZ;
        float f1 = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
        motionX = (d1 / (double)f1) * 0.5D * 0.35000000192092895D + motionX * 0.20000000098023224D;
        motionZ = (d3 / (double)f1) * 0.5D * 0.25000000192092897D + motionZ * 0.20000000098023224D;
        jumping = true;
    }

    //to make attackEntity works in 1.8
    public class AIAttackEntity extends EntityAIBase {

    	public RobotTedEntity robot = RobotTedEntity.this;
    	public int attackTime;
    	public AIAttackEntity() {}
    	
		@Override
		public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.robot.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
		}
        public void updateTask()
        {
        	--attackTime;
            EntityLivingBase entitylivingbase = this.robot.getAttackTarget();
            double d0 = this.robot.getDistanceSqToEntity(entitylivingbase);

            if (d0 < 4.0D)
            {
                if (this.attackTime <= 0)
                {
                    this.attackTime = 40;
                    this.robot.attackEntityAsMob(entitylivingbase);// or entitylivingbase.attackEntityFrom blablabla...
                }
                
                this.robot.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            else if (d0 < 256.0D)
            {
                // ATTACK ENTITY JUST CALLED HERE :D
            	robot.attackEntity(entitylivingbase, (float)d0);
                this.robot.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            }
            else
            {
                this.robot.getNavigator().clearPathEntity();
                this.robot.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
            }
        }
    }
    
    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getEntity();

        if (entity != null)
        {
            double d = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
            motionX = d * 1.25D;
            motionZ = d1 * 1.25D;

            if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
            {
                if (riddenByEntity == entity || ridingEntity == entity)
                {
                    return true;
                }

                if (entity != this && world.getDifficulty() != EnumDifficulty.PEACEFUL)
                {
                	this.setRevengeTarget((EntityLivingBase) entity);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(World world)
    {
        String s = getLivingSound();

        if (s != null)
        {
            world.playSound(this, s, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (2.5F - robotsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "morecreeps:tedinsult";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "morecreeps:robothurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "morecreeps:teddead";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
        if(!world.isRemote)
        {
            for (int i = 0; i < 4; i++)
            {
                EvilLightEntity creepsentityevillight = new EvilLightEntity(world);
                creepsentityevillight.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);

                if (damagesource != null)
                {
                    creepsentityevillight.motionX = -(rand.nextFloat() * 2.0F);
                    creepsentityevillight.motionZ = -(rand.nextFloat() * 2.0F);
                    creepsentityevillight.lifespan = 15;
                }
                    world.spawnEntityInWorld(creepsentityevillight);
            }

            dropItem(MoreCreepsReboot.ram16k, rand.nextInt(2) + 1);
        }
        super.onDeath(damagesource);
    }
}
