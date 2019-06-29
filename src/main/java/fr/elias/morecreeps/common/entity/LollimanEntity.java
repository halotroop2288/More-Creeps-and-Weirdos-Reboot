package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.particles.CREEPSFxConfetti;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class LollimanEntity extends AnimalEntity
{
    protected double attackrange;
    protected int attack;
    public int kidcheck;
    public boolean kidmounted;
    public int rockettime;
    public float modelsize;
    public int treats;
    public String texture;

    public LollimanEntity(World world)
    {
        super(world);
        texture = Reference.MODID + Reference.TEXTURE_PATH_ENTITES +"lolliman.png";
        setSize(0.9F, 3F);
        attack = 2;
        attackrange = 16D;
        kidcheck = 0;
        kidmounted = false;
        rockettime = 0;
        modelsize = 2.0F;
        treats = 0;
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
    }

    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public AnimalEntity createChild(AgeableEntity entityanimal, World world)
    {
        return new LollimanEntity(world);
    }

    @SuppressWarnings("rawtypes")
	protected void updateAITasks(World world, PlayerEntity playerentity)
    {
        if (kidcheck++ > 25 && !kidmounted)
        {
            kidcheck = 0;
            List list = null;
            list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(2D, 2D, 2D));

            for (int k = 0; k < list.size(); k++)
            {
                Entity entity = (Entity)list.get(k);

                if (!((entity != null) & (entity instanceof KidEntity)) || (entity.getRidingEntity() == null || !(entity.getRidingEntity() instanceof PlayerEntity)))
                {
                    continue;
                }

                world.playSound(this, "morecreeps:lollimantakeoff", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                //smallconfetti();

                //Actually not stable
                PlayerEntity playerentity = world.getClosestPlayer(this, 4D);
                if (!((PlayerEntity)playerentity).getStats.hasAdvancementUnlocked(ModAdvancementList.lolliman))
                {
                    world.playSound(playerentity, playerentity.getPosition(), "morecreeps:achievement", 1.0F, 1.0F);
                    playerentity.addStat(ModAdvancementList.lolliman, 1);
                    confetti();
                }


                addPassenger(entity);
                this.addPassenger(entity);
                this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
                moveVertical = 0.60000002384185791F;
                kidmounted = true;
                kidcheck = 255;
            }
        }

        if (kidcheck > 250 && kidmounted)
        {
            if(world.isRemote)
            	MoreCreepsReboot.proxy.smoke3(world, this, rand);

            moveVertical = 0.25F;

            if (rockettime++ > 5)
            {
                rockettime = 0;

                for (int j = 0; j < rand.nextInt(2) + 1; j++)
                {
                    Object obj = null;
                    int l = rand.nextInt(4);
                    treats++;

                    if (treats == 30)
                    {
                        world.playSound(playerentity, this.getPosition(), SoundsHandler.LOLLIMAN_EXPLODE, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    }

                    if (rand.nextInt(3) == 0)
                    {
                        ItemEntity entityitem;

                        switch (l)
                        {
                            case 1:
                                entityitem = entityDropItem(new ItemStack(Items.COOKIE, 1), 1.0F);
                                break;

                            case 2:
                                entityitem = entityDropItem(new ItemStack(Items.CAKE, 1), 1.0F);
                                break;

                            case 3:
                                entityitem = entityDropItem(new ItemStack(ItemList.lolly, 1), 1.0F);
                                break;

                            default:
                                entityitem = entityDropItem(new ItemStack(ItemList.lolly, 1), 1.0F);
                                break;
                        }

                        entityitem.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                        entityitem.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.2F;
                    }

                    if (posY <= 100D && treats <= 55)
                    {
                        continue;
                    }

                    if (getPassengers() != null)
                    {
                        ((LivingEntity) getPassengers()).setHealth(0);
                    }

                    setHealth(0);

                    if (treats > 50)
                    {
                        world.createExplosion(this, posX, posY + 2D, posZ, 2.5F, true);
                    }
                }
            }
        }

        super.updateEntityActionState();
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        if (getRidingEntity() != null && kidmounted)
        {
            return false;
        }
        else
        {
            return super.isEntityInsideOpaqueBlock();
        }
    }
    
    //this function doesn't exist anymore
    /*
    protected void updateFallState(double d, boolean flag)
    {
        if (!kidmounted)
        {
            super.updateFallState(d, flag);
        }
    }*/

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float func_180484_a(BlockPos bp, World world)
    {
        if (world.getBlockState(bp.down()).getBlock() == Blocks.WATER)
        {
            return 10F;
        }
        else
        {
            return -(float)bp.getY();
        }
    }

    /*protected Entity findPlayerToAttack()
    {
        if (world.getDifficulty() != EnumDifficulty.PEACEFUL)
        {
            float f = getBrightness(1.0F);

            if (f < 0.0F)
            {
                EntityPlayer playerentity = world.getClosestPlayerToEntity(this, attackrange);

                if (playerentity != null)
                {
                    return playerentity;
                }
            }

            if (rand.nextInt(10) == 0)
            {
                EntityLiving entityliving = func_21018_getClosestTarget(this, 15D);
                return entityliving;
            }
        }

        return null;
    }

    public EntityLiving func_21018_getClosestTarget(Entity entity, double d)
    {
        double d1 = -1D;
        EntityLiving entityliving = null;

        for (int i = 0; i < world.loadedEntityList.size(); i++)
        {
            Entity entity1 = (Entity)world.loadedEntityList.get(i);

            if (!(entity1 instanceof EntityLiving) || entity1 == entity || entity1 == entity.riddenByEntity || entity1 == entity.ridingEntity || (entity1 instanceof EntityPlayer) || (entity1 instanceof EntityMob) || (entity1 instanceof EntityAnimal))
            {
                continue;
            }

            double d2 = entity1.getDistanceSq(entity.posX, entity.posY, entity.posZ);

            if ((d < 0.0D || d2 < d * d) && (d1 == -1D || d2 < d1) && ((EntityLiving)entity1).canEntityBeSeen(entity))
            {
                d1 = d2;
                entityliving = (EntityLiving)entity1;
            }
        }

        return entityliving;
    }
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getEntity();
        entityToAttack = entity;
        return super.attackEntityFrom(damagesource, i);
    }
    protected void attackEntity(Entity entity, float f)
    {
        if (onGround)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            motionX = (d / (double)f1) * 0.20000000000000001D * (0.850000011920929D + motionX * 0.20000000298023224D);
            motionZ = (d1 / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + motionZ * 0.20000000298023224D);
            motionY = 0.10000000596246449D;
            fallDistance = -25F;
        }

        if ((double)f < 3.1000000000000001D && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack);
        }
    }*/

    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        //int l = world.getFullBlockLightValue(i, j, k);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.grass || i1 == Blocks.DIRT) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG /*&& i1 != Blocks.double_stone_slab*/ && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && world.canSeeSky(new BlockPos(i, j, k)) && rand.nextInt(15) == 0; //&& l > 7;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putInt("KidCheck", kidcheck);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        kidcheck = compound.getInt("KidCheck");
        kidmounted = false;

        if (getPassengers() != null)
        {
            ((LivingEntity) getPassengers()).setHealth(0);
        }
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(PlayerEntity playerentity)
    {
        SoundEvent s = getLivingSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (2.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        if (!kidmounted)
        {
            return "morecreeps:lolliman";
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.LOLLIMAN_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.LOLLIMAN_DEATH;
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

    /*public void smallconfetti()
    {
        for (int i = 1; i < 20; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                CREEPSFxConfetti creepsfxconfetti = new CREEPSFxConfetti(world, posX + (double)(world.rand.nextFloat() * 8F - world.rand.nextFloat() * 8F), posY + (double)rand.nextInt(4) + 6D, posZ + (double)(world.rand.nextFloat() * 8F - world.rand.nextFloat() * 8F), world.rand.nextInt(99));
                creepsfxconfetti.renderDistanceWeight = 30D;
                creepsfxconfetti.particleMaxAge = 55;
                ModLoader.getMinecraftInstance().effectRenderer.addEffect(creepsfxconfetti);
            }
        }
    }*/

    public void confetti()
    {
    	MoreCreepsReboot.proxy.confettiA(this, world);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }
}
