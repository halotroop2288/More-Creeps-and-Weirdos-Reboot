package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class HorseHeadEntity extends AnimalEntity
{
    public int galloptime;
    public double floatcycle;
    public int floatdir;
    public double floatmaxcycle;
    public int blastoff;
    public int blastofftimer;
    public String texture;

    public HorseHeadEntity(World world)
    {
        super(null, world);
        texture = "morecreeps:textures/entity/horsehead.png";
//        setSize(0.6F, 2.0F);
        floatdir = 1;
        floatcycle = 0.0D;
        floatmaxcycle = 0.10499999672174454D;
        blastoff = rand.nextInt(500) + 400;
//        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
//        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 0.5D));
//        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
//        this.tasks.addTask(3, new EntityAILookIdle(this));
    }

    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
    }
    /**
     * Called to update the entity's position/logic.
     */
    public void tick(World world)
    {
    	if(world.isRemote){
            for (int i = 0; i < 5; i++)
            {
        		MoreCreepsReboot.proxy.smokeHorseHead(world, this, rand);
            }
    	}

        if (isEntityInsideOpaqueBlock())
        {
            posY += 2.5D;
            floatdir = 1;
            floatcycle = 0.0D;
        }

        fallDistance = -100F;

        if (getRidingEntity() == null && blastoff-- < 0)
        {
            motionY += 0.15049999952316284D;
            double d = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);
            motionX += d * 0.10999999940395355D;
            motionZ += d1 * 0.10999999940395355D;

            if(world.isRemote)
            {
                for (int j = 0; j < 25; j++)
                {
            		MoreCreepsReboot.proxy.smokeHorseHead(world, this, rand);
                }
            }

            if (posY > 100D)
            {
                setDead();
            }
        }

        if (getRidingEntity() == null && blastoff > 0 && world.getBlockState(new BlockPos((int)posX, (int)posY - 1, (int)posZ)) == Blocks.AIR)
        {
            posY -= 0.25D;
        }

        ignoreFrustumCheck = true;

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

        if (getRidingEntity() != null && (getRidingEntity() instanceof PlayerEntity))
        {
            blastoff++;

            if (blastoff > 50000)
            {
                blastoff = 50000;
            }
        }

        super.tick();
    }

    protected void updateAITasks()
    {
        motionY *= 0.80000001192092896D;
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.95D);

        if (getRidingEntity() != null && (getRidingEntity() instanceof PlayerEntity))
        {
            moveForward = 0.0F;
            moveStrafing = 0.0F;
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.95D);
            getRidingEntity().lastTickPosY = 0.0D;
            prevRotationYaw = rotationYaw = getRidingEntity().rotationYaw;
            prevRotationPitch = rotationPitch = 0.0F;
            PlayerEntity playerentity = (PlayerEntity)getRidingEntity();
            float f = 1.0F;

            if (playerentity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() > 0.01D && playerentity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() < 10D)
            {
                f = (float) getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
            }

            moveStrafing = (playerentity.moveStrafing / f) * (float) getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() * 4.95F;
            moveForward = (playerentity.moveForward / f) * (float) getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() * 4.95F;

            if (onGround && (moveStrafing != 0.0F || moveForward != 0.0F))
            {
                motionY += 0.16100040078163147D;
            }

            if (moveStrafing == 0.0F && moveForward == 0.0F)
            {
                isJumping = false;
                galloptime = 0;
            }

            if (moveForward != 0.0F && galloptime++ > 10)
            {
                galloptime = 0;

                if (handleWaterMovement())
                {
                    world.playSound(this, "morecreeps:giraffesplash", getSoundVolume(), 1.2F);
                }
                else
                {
                    world.playSound(this, "morecreeps:giraffegallop", getSoundVolume(), 1.2F);
                }
            }

            if (onGround && !isJumping)
            {
                isJumping = ObfuscationReflectionHelper.getPrivateValue(LivingEntity.class, playerentity, 40);
                if (isJumping)
                {
                    motionY += 0.37000000476837158D;
                }
            }

            if (onGround && isJumping)
            {
                double d = Math.abs(Math.sqrt(motionX * motionX + motionZ * motionZ));

                if (d > 0.13D)
                {
                    double d2 = 0.13D / d;
                    motionX = motionX * d2;
                    motionZ = motionZ * d2;
                }

                motionX *= 6.9500000000000002D;
                motionZ *= 6.9500000000000002D;
            }

            if (MoreCreepsReboot.proxy.isJumpKeyDown() && posY < 120D)
            {
                motionY += 0.15049999952316284D;
                double d1 = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
                double d3 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
                motionX += d1 * 0.15999999642372131D;
                motionZ += d3 * 0.15999999642372131D;

                if (blastofftimer-- < 0)
                {
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.HORSE_HEAD_BLAST_OFF, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    blastofftimer = 10;
                }

                if(world.isRemote)
                {
                    for (int i = 0; i < 25; i++)
                    {
                    	MoreCreepsReboot.proxy.smokeHorseHead(world, this, rand);
                    }
                }
            }

            return;
        }
        else
        {
            //super.updateEntityActionState();
            return;
        }
    }

    public void updateRiderPosition()
    {
        if (getRidingEntity() == null)
        {
            return;
        }

        if (getRidingEntity() instanceof PlayerEntity)
        {
            getRidingEntity().setPosition(posX, (posY + 2.5D) - floatcycle, posZ);
            return;
        }
        else
        {
            return;
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(PlayerEntity playerentity)
    {
        if (playerentity.getRidingEntity() == null)
        {
            rotationYaw = playerentity.rotationYaw;
            rotationPitch = playerentity.rotationPitch;
            playerentity.fallDistance = -15F;
            playerentity.addPassenger(this);
            blastoff += rand.nextInt(500) + 200;
            moveForward = 0;
            moveStrafing = 0;
            moveVertical = 0;

            // Dead code
//            if (this == null)
//            {
//                double d = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
//                playerentity.motionX += 1.5D * d;
//                playerentity.motionZ -= 0.5D;
//            }
        }
        else
        {
            MoreCreepsReboot.proxy.addChatMessage("Unmount all creatures before riding your Horse Head");
        }

        return false;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getLight(new BlockPos(i, j, k));
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.SAND || i1 Blocks.RED_SAND || i1 == Blocks.GRASS || i1 == Blocks.DIRT) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(25) == 0 && l > 7;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Blastoff", blastoff);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        blastoff = nbttagcompound.getInteger("Blastoff");
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.HORSE_HEAD;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.ENTITY_HORSE_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.ENTITY_HORSE_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
            if (rand.nextInt(10) == 0)
            {
                entityDropItem(Items.PORKCHOP, rand.nextInt(3) + 1);
            }

            if (rand.nextInt(10) == 0)
            {
                entityDropItem(Items.WHEAT_SEEDS, rand.nextInt(3) + 1);
            }
    	}
        super.onDeath(damagesource);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

	@Override
	public AgeableEntity createChild(AgeableEntity ageable)
	{
		return new HorseHeadEntity(world);
	}
}
