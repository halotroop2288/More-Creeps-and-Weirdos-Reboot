package fr.elias.morecreeps.common.entity;

import java.util.Collection;
import java.util.Random;

import com.mojang.datafixers.Dynamic;

import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ThiefEntity extends MobEntity
{
    private boolean foundplayer;
    private boolean stolen;
    private PathEntity pathToEntity;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    protected ItemStack stolengood;
    private double goX;
    private double goZ;
    private float distance;
    public int itemnumber;
    public int stolenamount;
    public int tempDamage;
    public float modelsize;
    public String texture;

    public ThiefEntity(World world)
    {
        super(null, world);
        texture = Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "thief.png";
        stolen = false;
        hasAttacked = false;
        foundplayer = false;
        tempDamage = 0;
        modelsize = 1.0F;
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, new AIThief()); 
        tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.35D));
        tasks.addTask(5, new EntityAIWander(this, 0.35D));
        tasks.addTask(6, new EntityAIWatchClosest(this, PlayerEntity.class, 16F));
        tasks.addTask(7, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.65D);
    	this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected PlayerEntity findPlayerToAttack(World world, PlayerEntity player, Random rand)
    {
        if (!stolen)
        {
            PlayerEntity playerentity = world.getClosestPlayer(this, 16D);

            if (playerentity != null)
            {
                Object obj = null;
                ItemStack aitemstack[] = playerentity.inventory.mainInventory;
                itemnumber = -1;

                for (int i = 0; i < aitemstack.length; i++)
                {
                    ItemStack itemstack1 = aitemstack[i];

                    if (itemstack1 != null)
                    {
                        ItemStack itemstack = itemstack1;
                        itemnumber = i;
                    }
                }

                if (itemnumber >= 0)
                {
                    if (!foundplayer)
                    {
                        if (rand.nextInt(2) == 0)
                        {
                            world.playSound(player, player.getPosition(), SoundsHandler.THIEF_FIND_PLAYER, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
                        }

                        foundplayer = true;
                    }

                    return playerentity;
                }
            }
        }

        return null;
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return stolengood;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick(World world, Random rand)
    {
        super.livingTick();

        if (handleWaterMovement())
        {
            goX *= -1D;
            goZ *= -1D;
            moveForward += goX * 0.10000000149011612D;
            moveStrafing += goZ * 0.10000000149011612D;
            moveVertical += 0.25D;
        }

        PlayerEntity playerentity = world.getClosestPlayer(this, 25D);

        if (playerentity != null && !stolen)
        {
            distance = getDistance(playerentity);
            findPlayerToAttack(world, playerentity, rand);         
        }
        else
        {
            distance = 999F;
            //entityToAttack = null;
            this.setAttackTarget(null);
        }

        if (stolen)
        {
            //entityToAttack = null;
        	this.setAttackTarget(null);
        	
            if (isJumping)
            {
                moveForward += goX * 0.30000001192092896D;
                moveStrafing += goZ * 0.30000001192092896D;
            }
            else
            {
                moveForward += goX;
                moveStrafing += goZ;
            }

            moveEntityWithHeading((float)moveForward, (float)moveStrafing);

            if (prevPosY / posY == 1.0D)
            {
                if (rand.nextInt(25) == 0)
                {
                    moveForward -= goX;
                }
                else
                {
                    moveForward += goX;
                }

                if (rand.nextInt(25) == 0)
                {
                    moveStrafing -= goZ;
                }
                else
                {
                    moveStrafing += goZ;
                }
            }

            if (prevPosX == posX && rand.nextInt(50) == 0)
            {
                goX *= -1D;
            }

            if (prevPosZ == posZ && rand.nextInt(50) == 0)
            {
                goZ *= -1D;
            }

            if (rand.nextInt(500) == 0)
            {
                goX *= -1D;
            }

            if (rand.nextInt(700) == 0)
            {
                goZ *= -1D;
            }
        }

        if (playerentity != null && !stolen && distance < 4F && canEntityBeSeen(playerentity) && getHealth() > 0)
        {
            ItemStack itemstack = null;
            ItemStack aitemstack[] = playerentity.inventory.mainInventory;
            itemnumber = -1;

            for (int i = 0; i < aitemstack.length; i++)
            {
                ItemStack itemstack1 = aitemstack[i];

                if (itemstack1 == null)
                {
                    continue;
                }

                itemstack = itemstack1;
                itemnumber = i;

                if (rand.nextInt(4) == 0)
                {
                    break;
                }
            }

            if (itemstack == null)
            {
                //entityToAttack = null;
            	this.setAttackTarget(null);
            }

            if (itemstack != null && !stolen)
            {
                stolengood = itemstack;
                world.playSound(playerentity, this.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 6.2F + 1.0F);
                stolenamount = rand.nextInt(itemstack.getCount()) + 1;

                if (stolenamount > itemstack.getCount())
                {
                    stolenamount = itemstack.getCount();
                }

                if (stolenamount == 1 && itemstack.isDamaged())
                {
                    tempDamage = itemstack.getDamage();
                }

                if (stolenamount == itemstack.getCount())
                {
                    playerentity.inventory.mainInventory[itemnumber] = null;
                }
                else
                {
                    itemstack.setCount(-1 * stolenamount);
                }

                stolen = true;
                world.playSound(playerentity, this.getPosition(), SoundsHandler.THIEF_STEAL, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                //entityToAttack = null;
                this.setAttackTarget(null);
                goX = 0.044999999999999998D;
                goZ = 0.044999999999999998D;

                if (rand.nextInt(5) == 0)
                {
                    goX *= -1D;
                }

                if (rand.nextInt(5) == 0)
                {
                    goZ *= -1D;
                }

                for (int j = 0; j < 10; j++)
                {
                    double d = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;
                    double d2 = rand.nextGaussian() * 0.02D;
                    world.addParticle(ParticleTypes.LARGE_SMOKE, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
                }
            }
        }
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(World world)
    {
        SoundEvent s = getLivingSound();

        if (s != null)
        {
            world.playSound(this, s, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.THIEF;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.THIEF_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.THIEF_DEATH;
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

        if (j < 50)
        {
            return true;
        }
        else
        {
            return i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG /*&& i1 != Blocks.double_stone_slab*/ && i1 != Blocks.SMOOTH_STONE_SLAB && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(10) == 0 && l > 7;
        }
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    public int getItemDamage()
    {
        return 25;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
            if (getHeldItem() != null)
            {
                if (tempDamage > 0)
                {
                    ItemStack itemstack = getHeldItem().copy();
                    entityDropItem(itemstack, 0.0F);
                    stolengood = null;
                }
                else
                {
                    entityDropItem(getHeldItem().getItem(), stolenamount);
                }
            }
    	}

        super.onDeath(damagesource);
    }
    
    @SuppressWarnings("rawtypes")
	class AIThief extends Brain {

    	@SuppressWarnings("unchecked")
		public AIThief(Collection p_i50378_1_, Collection p_i50378_2_, Dynamic p_i50378_3_) {
			super(p_i50378_1_, p_i50378_2_, p_i50378_3_);
			// TODO Fix this ^ after it gets deobfuscated
		}
		public ThiefEntity thief = ThiefEntity.this;
    	
		public boolean shouldExecute()
		{
			LivingEntity entitylivingbase = this.thief.getAttackTarget();
            return entitylivingbase != null && !this.thief.stolen;
		}
        public void updateTask(PlayerEntity playerentity)
        {
        	LivingEntity entitylivingbase = this.thief.getAttackTarget();
            double d0 = this.thief.getDistanceSq(entitylivingbase);

            if (d0 < 4.0D)
            {
                this.thief.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            else if (d0 < 256.0D)
            {
                this.thief.getLookController().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            }
            else
            {
                this.thief.getNavigator().clearPath();
                this.thief.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
            	
                this.thief.findPlayerToAttack(world, playerentity, rand);
            }
        }
    }
}
