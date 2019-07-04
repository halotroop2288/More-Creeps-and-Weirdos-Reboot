package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class FloobShipEntity extends FlyingEntity
{
    private boolean foundplayer;
    private PathEntity pathToEntity;
    protected Entity playerToAttack;
    protected boolean hasAttacked;
    private double goX;
    private double goZ;
    private float distance;
    public boolean landed;
    public int floobcounter;
    public boolean firstreset;
    public float bump;
    public int lifespan;
    public String texture;

    public FloobShipEntity(World world)
    {
        super(null, world);
        texture = "morecreeps:textures/entity/floobship.png";
        hasAttacked = false;
        foundplayer = false;
        landed = false;
//        setSize(4F, 3F);
        collidedVertically = false;
        floobcounter = rand.nextInt(500) + 400;
        firstreset = false;
        bump = 2.0F;
        moveForward = rand.nextFloat() * 0.8F;
        moveStrafing = rand.nextFloat() * 0.8F;
        lifespan = rand.nextInt(10000) + 1500;
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(150D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("Landed", landed);
        compound.putBoolean("FirstReset", firstreset);
        compound.putInt("FloobCounter", floobcounter);
        compound.putInt("LifeSpan", lifespan);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        landed = compound.getBoolean("Landed");
        firstreset = compound.getBoolean("FirstReset");
        floobcounter = compound.getInt("FloobCounter");
        lifespan = compound.getInt("LifeSpan");
    }

    public void setAngles(float f, float f1)
    {
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    public void fall(float distance, float damageMultiplier)
    {
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return false;
    }

    /**
     * Sets the rotation of the entity
     */
    protected void setRotation(float f, float f1)
    {
    }

    /**
     * Sets the entity's position and rotation. Args: posX, posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation(double d, double d1, double d2, float f, float f1)
    {
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i)
    {
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
    	World world = Minecraft.getInstance().world;
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	
        floobcounter--;
        lifespan--;

        if (handleWaterMovement())
        {
            moveVertical += 0.2800000011920929D;
            moveForward += 0.97999999999999998D;
            moveForward += 0.97999999999999998D;
        }

        isJumping = false;
        super.livingTick();

        if (isJumping || landed)
        {
            moveVertical = 0;
            bump = 0.0F;
        }

        if (!landed || !onGround)
        {
            if (posY < 100D && !firstreset)
            {
                moveVertical = 4;
                bump = 4F;
                firstreset = true;
            }

            moveVertical = -0.2F + bump;
            bump *= 0.95999999999999996D;
            moveForward *= 0.97999999999999998D;
            moveStrafing *= 0.97999999999999998D;

            if (onGround)
            {
                int i = MathHelper.floor(posX);
                int k = MathHelper.floor(getBoundingBox().minY);
                int l = MathHelper.floor(posZ);
                Block i1 = world.getBlockState(new BlockPos(i, k - 1, l)).getBlock();

                if (i1 == Blocks.WATER || i1 == Blocks.OAK_LEAVES || i1 == Blocks.CACTUS)
                {
                    thrusters();
                    bump = 3F;
                    moveForward = rand.nextFloat() * 2.8F;
                    moveStrafing = rand.nextFloat() * 0.6F;
                    moveVertical = rand.nextFloat() * 2.8F;
                }
                else
                {
                    landed = true;
                }
            }
        }
        else if (floobcounter < 1)
        {
            thrusters();
            floobcounter = rand.nextInt(300) + 400;
            world.playSound(playerentity, this.getPosition(), SoundsHandler.FLOOB_SHIP_SPAWN, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

            for (int j = 0; j < rand.nextInt(4) + 3; j++)
            {
                FloobEntity creepsentityfloob = new FloobEntity(world);
                creepsentityfloob.setLocationAndAngles(posX + 3D + (double)j, posY + 1.0D, posZ + (double)j, rotationYaw, 0.0F);
                creepsentityfloob.motionX = rand.nextFloat() * 1.5F;
                creepsentityfloob.motionY = rand.nextFloat() * 2.0F;
                creepsentityfloob.motionZ = rand.nextFloat() * 1.5F;
                creepsentityfloob.fallDistance = -25F;
                world.addEntity(creepsentityfloob);
            }
        }
    }

    /**
     * knocks back this entity
     */
    public void knockBack(Entity entity, int i, double d, double d1)
    {
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i, World world)
    {
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	
        Entity entity = damagesource.getTrueSource();

        if (entity instanceof PlayerEntity)
        {
            thrusters();
            world.playSound(playerentity, this.getPosition(), SoundsHandler.FLOOB_SHIP_CLANG, SoundCategory.HOSTILE, "morecreeps:floobshipclang", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }

        if (rand.nextInt(10) == 0)
        {
            bump = rand.nextInt(3);
            moveForward = rand.nextFloat() * 0.8F;
            moveStrafing = rand.nextFloat() * 0.8F;
        }

        int j = MathHelper.floor(posX);
        int k = MathHelper.floor(getBoundingBox().minY);
        int l = MathHelper.floor(posZ);
        Block i1 = world.getBlockState(new BlockPos(j, k - 1, l)).getBlock();

        if (i1 == Blocks.WATER || i1 == Blocks.OAK_LEAVES || i1 == Blocks.CACTUS)
        {
            thrusters();
            bump = 3F;
            moveVertical = rand.nextFloat() * 0.8F;
            moveForward = rand.nextFloat() * 0.8F;
            moveStrafing = rand.nextFloat() * 0.8F;
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    private void thrusters()
    {
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth()) + (double)i, posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth() - (double)i, posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F) + (double)i) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)i - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth()) + (double)i, posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F) + (double)i) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth() - (double)i, posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)i - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth()) + (double)i, posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F) + (double)i) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth() - (double)i, posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)i - (double)getWidth(), d, d1, d2);
            }
        }
    }

    @Override
    public void onDeath()
    {
    	PlayerEntity player = Minecraft.getInstance().player;
    	
        if (lifespan > 0 && getHealth() > 0)
        {
            return;
        }

        if (lifespan > 0 && CREEPSConfig.floobshipExplode)
        {
            world.playSound(player, this.getPosition(), SoundsHandler.FLOOB_SHIP_EXPLODE, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            world.createExplosion(null, posX, posY, posZ, 8F, true, Mode.NONE);
        }

        setDead();
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        if (lifespan > 0 && getHealth() > 0)
        {
            return;
        }
        else
        {
            super.setHealth(0);
            return;
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.FLOOB_SHIP;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.FLOOB_SHIP;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.FLOOB_SHIP_EXPLODE;
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
        int j1 = world.countEntities(FloobShipEntity.class);
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& world.canBlockSeeSky(new BlockPos(i, j, k)) && posY > 100D && rand.nextInt(100) == 0 /*&& l > 10*/ && (world.getDifficulty() != Difficulty.PEACEFUL || j1 >= 2);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    public float getShadowSize()
    {
        return 2.8F;
    }
}
