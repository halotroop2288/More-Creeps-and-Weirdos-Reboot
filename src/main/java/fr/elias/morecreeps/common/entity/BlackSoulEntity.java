package fr.elias.morecreeps.common.entity;

import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlackSoulEntity extends MobEntity
{
    private static final ItemStack defaultHeldItem;
    public float modelsize;
    public ResourceLocation texture;

    public BlackSoulEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BLACK_SOUL);
        modelsize = 1.0F;
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, PlayerEntity.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
    }
    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT nbttagcompound)
    {
        super.writeAdditional(nbttagcompound);
        nbttagcompound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT nbttagcompound)
    {
        super.readAdditional(nbttagcompound);
        modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void livingTick()
    {
        if (world.isDaytime())
        {
            float f = getBrightness();

            if (f > 0.5F && world.canBlockSeeSky(new BlockPos(MathHelper.floor(posX),
                    MathHelper.floor(posY), MathHelper.floor(posZ)))
                    && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F)
            {
                setFire(20);
            }
        }

        super.livingTick();
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.BLACK_SOUL;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesource)
    {
        return SoundsHandler.BLACK_SOUL_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.BLACK_SOUL_DEATH;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean canSpawn(World world, SpawnReason spawnReasonIn)
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        // int l = world.getFullBlockLightValue(i, j, k);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        int j1 = world.countEntities(BlackSoulEntity.class);
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.log && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
                && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && rand.nextInt(15) == 0
                && j1 < 10;
    }
    
    public void knockBack(Entity entity, int i, double d, double d1)
    {
        super.knockBack(entity, i, d, d1);
        moveForward *= 0.29999999999999999D;
        moveStrafing *= 0.29999999999999999D;
        moveVertical += 0.02000000238418579D;
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return defaultHeldItem;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
            if (rand.nextInt(50) == 0)
            {
                entityDropItem(Items.DIAMOND, rand.nextInt(2) + 1);
            }
            else
            {
                entityDropItem(Items.COAL, rand.nextInt(5) + 1);
            }
    	}

        super.onDeath(damagesource);
    }

    static
    {
        defaultHeldItem = new ItemStack(Items.COAL, 1);
    }
}
