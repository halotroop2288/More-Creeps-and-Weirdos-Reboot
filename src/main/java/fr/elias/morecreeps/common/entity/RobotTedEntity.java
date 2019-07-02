package fr.elias.morecreeps.common.entity;

import java.util.Collection;
import java.util.Random;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class RobotTedEntity extends MobEntity {
    public static Random rand = new Random();
    protected double attackRange;
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

    public ResourceLocation texture;

    public RobotTedEntity(World world) {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "robotted.png");
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
        // targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
        // EntityPlayer.class, true));
        // targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
        // RobotToddEntity.class, true));
    }

    @Override
    public void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(45D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.061D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("RobotSize", robotsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
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
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.SMOOTH_STONE_SLAB
                && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
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

        if (world.isRemote)
        {
            MoreCreepsReboot.proxy.robotTedSmoke(world, posX, posY, posZ, floattimer, modelspeed);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
        if (isEntityInsideOpaqueBlock())
        {
            posY += 2.5D;
            floatdir = 1;
            floatcycle = 0.0D;
        }

        if (floatdir > 0) {
            floatcycle += 0.017999999225139618D;

            if (floatcycle > floatmaxcycle)
            {
                floatdir = floatdir * -1;
                fallDistance += -1.5F;
            }
        } else {
            floatcycle -= 0.0094999996945261955D;

            if (floatcycle < -floatmaxcycle)
            {
                floatdir = floatdir * -1;
                fallDistance += -1.5F;
            }
        }

        super.tick();
    }

    // /**
    //  * Basic mob attack. Default to touch of death in EntityCreature. Overridden by
    //  * each mob to define their attack.
    //  */
    // @Override
    // protected void attackEntity(Entity entity, float f)
    // {
    //     if (entity instanceof PlayerEntity) {
    //         double d = getDistance(entity);

    //         if (posY > entity.posY && d < 6D) {
    //             motionY = -0.02500000037252903D;
    //         }

    //         if (posY < entity.posY - 0.5D && d < 6D) {
    //             motionY = 0.043999999761581421D;
    //         }

    //         if (d < 3D) {
    //             double d2 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
    //             double d4 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
    //             motionX = -(d2 * 0.20000000298023224D);
    //             motionZ = -(d4 * 0.20000000298023224D);

    //             if (posY > entity.posY) {
    //                 motionY = -0.070000000298023224D;
    //             }
    //         }
    //     }

    //     fallDistance = -25F;
    //     double d1 = entity.posX - posX;
    //     double d3 = entity.posZ - posZ;
    //     float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3);
    //     motionX = (d1 / (double) f1) * 0.5D * 0.35000000192092895D + motionX * 0.20000000098023224D;
    //     motionZ = (d3 / (double) f1) * 0.5D * 0.25000000192092897D + motionZ * 0.20000000098023224D;
    //     jumping = true;
    // }

    // to make attackEntity works in 1.8
    @SuppressWarnings("rawtypes")
    public class AIAttackEntity extends Brain {

        public AIAttackEntity(Collection p_i50378_1_, Collection p_i50378_2_, Dynamic p_i50378_3_) {
            super(p_i50378_1_, p_i50378_2_, p_i50378_3_);
        }

        public RobotTedEntity robot = RobotTedEntity.this;
    	public int attackTime;
    	
		public boolean shouldExecute() {
            LivingEntity livingentity = this.robot.getAttackTarget();
            return livingentity != null && livingentity.isAlive();
		}
        public void updateTask()
        {
        	--attackTime;
            LivingEntity livingentity = this.robot.getAttackTarget();
            double d0 = this.robot.getDistanceSq(livingentity);

            if (d0 < 4.0D)
            {
                if (this.attackTime <= 0)
                {
                    this.attackTime = 40;
                    this.robot.attackEntityAsMob(livingentity);// or livingentity.attackEntityFrom blablabla...
                }
                
                this.robot.getMoveHelper().setMoveTo(livingentity.posX, livingentity.posY, livingentity.posZ, 1.0D);
            }
            // else if (d0 < 256.0D)
            // {
            //     // ATTACK ENTITY JUST CALLED HERE :D
            // 	robot.attackEntity(livingentity, (float)d0);
            //     this.robot.getLookController().setLookPositionWithEntity(livingentity, 10.0F, 10.0F);
            // }
            else
            {
                this.robot.getNavigator().clearPath();
                this.robot.getMoveHelper().setMoveTo(livingentity.posX, livingentity.posY, livingentity.posZ, 0.5D);
            }
        }
    }
    
    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();

        if (entity != null)
        {
            double d = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
            motionX = d * 1.25D;
            motionZ = d1 * 1.25D;

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
    @Override
    public void playAmbientSound()
    {
        World world = Minecraft.getInstance().world;
        PlayerEntity playerentity = Minecraft.getInstance().player;
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (2.5F - robotsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.TED_INSULT;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.ROBOT_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.TED_DEATH;
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
                    creepsentityevillight.setMotion(-(rand.nextFloat() * 2.0F), getMotion().y, -(rand.nextFloat() * 2.0F));
                    creepsentityevillight.lifespan = 15;
                }
                    world.addEntity(creepsentityevillight);
            }

            entityDropItem(ItemList.ram_16k, rand.nextInt(2) + 1);
        }
        super.onDeath(damagesource);
    }
}
