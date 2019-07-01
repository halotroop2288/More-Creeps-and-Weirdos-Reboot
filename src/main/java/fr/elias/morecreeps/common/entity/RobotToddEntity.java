package fr.elias.morecreeps.common.entity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;

public class RobotToddEntity extends MobEntity
{
    public static Random rand = new Random();
    protected double attackRange;
    public boolean jumping;
    public float robotsize;
    public int texswitch;
    public int texnumber;
    public float modelspeed;
    public String texture;

    public RobotToddEntity(World world) {
        super(null, world);
        texnumber = 0;
        texture = "morecreeps:textures/entity/robottodd1.png";
        attackRange = 16D;
        jumping = false;
        robotsize = 2.5F;
        //yOffset *= 1.5F;
        setSize(1.5F, 2.5F);
        modelspeed = 0.4F;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // tasks.addTask(0, new EntityAISwimming(this));
        // tasks.addTask(1, new EntityAIBreakDoor(this));
        // // tasks.addTask(2, new AIAttackEntity()); 
        // tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.061D));
        // tasks.addTask(5, new EntityAIWander(this, 0.25D));
        // tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        // tasks.addTask(6, new EntityAIWatchClosest(this, RobotTedEntity.class, 8F));
        // tasks.addTask(7, new EntityAILookIdle(this));
        // targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        // targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        // targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, RobotTedEntity.class, true));
    }

    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(45D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.setFloat("RobotSize", robotsize);
        compound.setFloat("ModelSpeed", modelspeed);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        robotsize = compound.getFloat("RobotSize");
        modelspeed = compound.getFloat("ModelSpeed");
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
        return i1 != Blocks.cobblestone && i1 != Blocks.log && i1 != Blocks.double_stone_slab && i1 != Blocks.stone_slab
                && i1 != Blocks.planks && i1 != Blocks.wool
                && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(10) == 0 && l > 8;
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void livingTick() {
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(modelspeed);
        super.livingTick();

        if (texswitch++ > 40)
        {
            if (texnumber++ > 1)
            {
                texnumber = 0;
            }

            if (texnumber == 0)
            {
                texture = "morecreeps:textures/entity/robottodd1.png";
            }

            if (texnumber == 1)
            {
                texture = "morecreeps:textures/entity/robottodd2.png";
            }
        }
    }

    
    protected void attackEntity(Entity entity, float f)
    {
        if (onGround)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d1 * d1);
            motionX = (d / (double)f1) * 0.5D * 0.40000000192092894D + motionX * 0.20000000098023224D;
            motionZ = (d1 / (double)f1) * 0.5D * 0.30000000192092896D + motionZ * 0.20000000098023224D;
            motionY = 0.35000000196046449D;
            jumping = true;
        }
    }

    //to make attackEntity works in 1.8
    public class AIAttackEntity extends EntityAIBase {

    	public RobotToddEntity robot = RobotToddEntity.this;
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
        return "morecreeps:toddinsult";
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
        return "morecreeps:todddead";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
    		dropItem(MoreCreepsReboot.battery, rand.nextInt(2) + 1);
    	}
        super.onDeath(damagesource);
    }

	public void setHealth(int i) {
	}
}
