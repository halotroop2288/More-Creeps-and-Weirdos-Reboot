package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class HunchbackEntity extends MobEntity
{
	PlayerEntity playerentity;
	World world;
    public boolean tamed;
    public int basehealth;
    public int weapon;
    public boolean used;
    public int interest;
    public String ss;
    public float distance;
    public int caketimer;
    public String basetexture;
    public float modelsize;
    public String texture;
    public double moveSpeed;
    public double attackStrength;
    public double health;
    public float width = getWidth();
    public float length = getWidth();
    public float height = getHeight();

    public HunchbackEntity(World world)
    {
        super(null, world);
        texture = Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "hunchback.png";
        basetexture = texture;
        moveSpeed = 0.51F;
        attackStrength = 1;
        health = rand.nextInt(15) + 5;
        tamed = false;
        basehealth = rand.nextInt(30) + 10;
        health = basehealth;
        caketimer = 0;
        modelsize = 1.0F;
        
    }
    
    

    

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.getAttackTarget() instanceof HunchbackSkeletonEntity)
        {
            this.setAttackTarget(null);
        }

        if (basetexture != texture)
        {
            texture = basetexture;
        }

        if (tamed && caketimer > 0 && rand.nextInt(10) == 0)
        {
            caketimer--;

            if (caketimer == 0)
            {
                tamed = false;
                texture = "/mob/creeps/hunchback.png";
                basetexture = texture;
                caketimer = rand.nextInt(700) + 300;
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        if (tamed)
        {
            PlayerEntity playerentity = world.getClosestPlayer(this, 16D);

            if (playerentity != null && canEntityBeSeen(playerentity))
            {
                distance = getDistance(playerentity);

                if (distance < 5F)
                {
                    this.setAttackTarget(null);
                    return null;
                }
                else
                {
                    return playerentity;
                }
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
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();
        hurtTime = maxHurtTime = 10;

        if (entity != null)
        {
            double d = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);

            if ((entity instanceof PlayerEntity) && tamed)
            {
                moveVertical = rand.nextFloat() * 0.9F;
                moveStrafing = (float) (d1 * 0.40000000596046448D);
                moveForward = (float) (d * 0.5D);
                world.playSound(playerentity, this.getPosition(), SoundsHandler.HUNCHBACK_THANKS, SoundCategory.NEUTRAL, 2.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F); // He thanks you when you hit him? LMAO
                super.attackEntityFrom(damagesource, i / 6);
            }
            else if (i > 0 && entity != null)
            {
                moveVertical = (rand.nextFloat() - rand.nextFloat()) * 0.3F;
                moveStrafing = (float) (d1 * 0.40000000596046448D);
                moveForward = (float) (d * 0.5D);
                world.playSound(playerentity, this.getPosition(), SoundsHandler.HUNCHBACK_HURT, SoundCategory.NEUTRAL, 2.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                setBeenAttacked();
                this.setAttackTarget((LivingEntityBase) entity);
                super.attackEntityFrom(damagesource, i);
            }
        }

        return true;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (!(this.getAttackTarget() instanceof HunchbackSkeletonEntity) && !(this.getAttackTarget() instanceof GuineaPigEntity) && !(this.getAttackTarget() instanceof HotdogEntity))
        {
            if (onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt(d * d + d1 * d1);
                moveForward = (float) ((d / (double)f1) * 0.5D * 0.80000001192092896D + moveForward * 0.20000000298023224D);
                moveStrafing = (float) ((d1 / (double)f1) * 0.5D * 0.80000001192092896D + moveStrafing * 0.20000000298023224D);
                moveVertical = (float) 0.20000000596046447D;
                super.attackEntityAsMob(entity);
            }
            else if (tamed)
            {
                super.attackEntityAsMob(entity);
            }
        }

        if ((double)f < 16D && (getAttackTarget() instanceof PlayerEntity) && tamed)
        {
        	this.setAttackTarget(null);
        }
        else
        {
            super.attackEntityAsMob(entity);
        }
    }

    public LivingEntity getClosestTarget(Entity entity, double d)
    {
        double d1 = -1D;
        LivingEntity entityliving = null;

        for (int i = 0; i < world.loadedEntityList.size(); i++)
        {
            Entity entity1 = (Entity)world.loadedEntityList.get(i);

            if (!(entity1 instanceof LivingEntity) || entity1 == entity || entity1 == entity.riddenByEntity || entity1 == entity.ridingEntity || (entity1 instanceof PlayerEntity) || (entity1 instanceof EntityMob) || (entity1 instanceof HunchbackSkeletonEntity))
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
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(PlayerEntity playerentity)
    {
        ItemStack itemstack = playerentity.inventory.getCurrentItem();
        used = false;

        if (itemstack != null)
        {
            if (tamed && itemstack.getItem() == Items.BONE)
            {
                smoke();
                used = true;
                smoke();
                world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_ARMOUR, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                world.playSound(playerentity, this.getPosition(), SoundsHandler.HUNCHBACK_ARMY, SoundCategory.NEUTRAL, 2.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

                for (int i = 0; i < 5; i++)
                {
                    HunchbackSkeletonEntity creepsentityhunchbackskeleton = new HunchbackSkeletonEntity(world);
                    creepsentityhunchbackskeleton.setLocationAndAngles(posX + 3D, posY, posZ + (double)i, rotationYaw, 0.0F);
                    creepsentityhunchbackskeleton.modelsize = modelsize;
                    world.addEntity(creepsentityhunchbackskeleton);
                }
            }

            if (itemstack.getItem() == Items.CAKE || Item.getIdFromItem(itemstack.getItem()) == 92)
            {
                
                    world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                    playerentity.addStat(ModAdvancementList.hunchback, 1);
                    confetti();
                

                texture = "/mob/creeps/hunchbackcake.png";
                basetexture = texture;
                world.playSound(playerentity, this.getPosition(), SoundsHandler.HUNCHBACK_THANK_YOU, SoundCategory.NEUTRAL, 2.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                used = true;
                tamed = true;
                interest += 15;
                health += 2;
                smoke();
                getLivingSound();

                if (caketimer < 4000)
                {
                    caketimer += rand.nextInt(500) + 250;
                }
            }

            if (health > basehealth)
            {
                health = basehealth;
            }

            if (used)
            {
                if (itemstack.getCount() - 1 == 0)
                {
                    playerentity.inventory.setInventorySlotContents(playerentity.inventory.currentItem, null);
                }
                else
                {
                    itemstack.setCount(itemstack.getCount() - 1);
                }
            }
        }

        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("Tamed", tamed);
        compound.putInt("CakeTimer", caketimer);
        compound.putString("BaseTexture", basetexture);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        tamed = compound.getBoolean("Tamed");
        basetexture = compound.getString("BaseTexture");
        caketimer = compound.getInt("CakeTimer");
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(this.getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getLight(new BlockPos(i, j, k));
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.WHITE_WOOL && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && world.checkBlockCollision(getBoundingBox()) && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(10) == 0 && l > 10;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    public void confetti()
    {
        
        
        double d = -MathHelper.sin((((PlayerEntity)(playerentity)).rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((((PlayerEntity)(playerentity)).rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(((PlayerEntity)(playerentity)).posX + d * 3D, ((PlayerEntity)(playerentity)).posY - 2D, ((PlayerEntity)(playerentity)).posZ + d1 * 3D, ((PlayerEntity)(playerentity)).rotationYaw, 0.0F);
        world.spawnEntityInWorld(creepsentitytrophy);
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(PlayerEntity playerentity)
    {
        SoundEvent s = getLivingSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        if (tamed)
        {
            return SoundsHandler.HUNCHBACK_QUIET;
        }

        if (rand.nextInt(3) == 0)
        {
            return SoundsHandler.HUNCHBACK_CAKE;
        }
        else
        {
            return SoundsHandler.HUNCHBACK_QUIET;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.HUNCHBACK_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.HUNCHBACK_DEATH;
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
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height) + (double)i, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
        if (tamed && health > 0)
        {
            return;
        }

        if (rand.nextInt(5) == 0)
        {
            entityDropItem(Items.PORKCHOP, rand.nextInt(3) + 1);
        }
        else
        {
            entityDropItem(Blocks.SAND, rand.nextInt(3) + 1);
        }

        super.onDeath(damagesource);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        if (tamed && health > 0)
        {
            setHealth(getHealth() + 1);
            deathTime = 0;
            return;
        }
        else
        {
            super.setHealth(0);
            return;
        }
    }
}
