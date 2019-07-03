package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class MummyEntity extends MobEntity
{
	public ResourceLocation texture;
    public MummyEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "mummy.png");
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.4D, true));
        // this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
        // this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(4, new EntityAILookIdle(this));
        // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        // this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }
    
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere(World world)
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        // int l = world.getFullBlockLightValue(i, j, k);
        BlockPos bp = new BlockPos(i, j, k);
        Block i1 = world.getBlockState(bp.down()).getBlock();
        int j1 = world.countEntities(MummyEntity.class);
        return (i1 == Blocks.STONE || i1 == Blocks.SAND || i1 == Blocks.GRAVEL || i1 == Blocks.BEDROCK
                || i1 == Blocks.SMOOTH_STONE_SLAB || i1 == Blocks.STONE_SLAB) && i1 != Blocks.COBBLESTONE
                && i1 != Blocks.OAK_LOG && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
                // && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && /* l < 10 && */j1 < 15;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate(World world)
    {
        if (world.isDaytime())
        {
            float f = getBrightness();

            if (f > 0.5F && world.canBlockSeeSky(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY), MathHelper.floor(posZ)))
                    && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F)
            {
                setFire(10);
            }
        }

        super.tick();
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.MUMMY;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.MUMMY_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.MUMMY_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
            if (rand.nextInt(5) == 0)
            {
                entityDropItem(Blocks.SAND, rand.nextInt(6) + 1);
                entityDropItem(Blocks.SANDSTONE, rand.nextInt(3) + 1);
            }
            else if (rand.nextInt(5) == 0)
            {
                entityDropItem(ItemList.band_aid, rand.nextInt(8) + 1);
            }
            else
            {
                entityDropItem(Blocks.SAND, rand.nextInt(2) + 1);
            }
    	}

        super.onDeath(damagesource);
    }
}
