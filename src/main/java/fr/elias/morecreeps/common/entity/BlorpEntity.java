package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ai.EntityBlorpAI;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class BlorpEntity extends AnimalEntity
{
    public double attackrange;
    public boolean bone;
    protected int attack;
    public float blorpsize;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();

    /** Entity motion Y */
    //public String motionY;
    public int eaten;
    public boolean hungry;
    public int hungrytime;
    public int blorplevel;
    public boolean angry;
    public String texture;
    public int attackTime;

    public BlorpEntity(World world)
    {
        super(null, world);
        bone = false;
        texture = Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "blorp.png";
//        setSize(width * 1.5F, height * 2.5F);
        attack = 2;
        attackrange = 16D;
        blorpsize = 1.0F;
        hungry = false;
        hungrytime = rand.nextInt(20) + 20;
        blorplevel = 1;
        angry = false;
//        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
//        this.tasks.addTask(0, new EntityBlorpAI(this));
//        this.tasks.addTask(1, new EntityAISwimming(this));
//        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
//        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
//        this.tasks.addTask(8, new EntityAILookIdle(this));
//        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
    }

    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void livingTick()
    {
    	World world = Minecraft.getInstance().world;
    	Random rand;
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	
        if (blorpsize > 2.0F)
        {
            ignoreFrustumCheck = true;
        }

        super.livingTick();

        if (getAttackTarget() != null)
        {
            hungry = false;
            hungrytime = 100;
        }

        if (hungry)
        {
            int ai[] = findTree(this, Double.valueOf(2D));

            if (ai[1] != 0)
            {
                int i = ai[0];
                int j = ai[1];
                int k = ai[2];
                world.playSound(playerentity, this.getPosition(), SoundsHandler.BLORP_EAT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                world.isAirBlock(new BlockPos(i, j, k));
                hungrytime = hungrytime + rand.nextInt(100) + 25;

                if (hungrytime > 1000)
                {
                    hungry = false;

                    if (blorpsize < 6F)
                    {
                        blorpsize = blorpsize + 0.3F;
                    }

                    blorplevel++;
                    float health = this.getHealth();
                    health = 10 * blorplevel + 25;
                    setSize(width * blorpsize, 2.0F + height * blorpsize);
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.BLORP_GROW, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }

                faceTreeTop(i, j, k, 10F);
                int l = 0;
                int i1 = 0;

                if (posX < (double)i)
                {
                    l = i - MathHelper.floor(posX);
                    moveStrafing += 0.050000000000000003D;
                }
                else
                {
                    l = MathHelper.floor(posX) - i;
                    moveStrafing -= 0.050000000000000003D;
                }

                if (posZ < (double)k)
                {
                    i1 = k - MathHelper.floor(posZ);
                    moveStrafing += 0.050000000000000003D;
                }
                else
                {
                    i1 = MathHelper.floor(posZ) - k;
                    moveStrafing -= 0.050000000000000003D;
                }

                double d = l + i1;
            }
        }
        else
        {
            hungrytime--;

            if (hungrytime < 1)
            {
                hungry = true;
                hungrytime = 1;
            }
        }
    }

    //previously called : getBlockpathWeight		// OK so I guess that's what I'll call it now.
    public float getBlockpathWeight(BlockPos bp)
    {
        if (world.getBlockState(bp.down()).getBlock() == Blocks.OAK_LEAVES || world.getBlockState(bp.down()).getBlock() == Blocks.OAK_LOG)
        {
            return 10F;
        }
        else
        {
            return -(float)bp.getY();
        }
    }
    @SuppressWarnings("unused")
	public void faceTreeTop(int i, int j, int k, float f)
    {
        double d = (double)i - posX;
        double d1 = (double)k - posZ;
        double d2 = (double)j - posY;
        double d3 = MathHelper.sqrt(d * d + d1 * d1);
        float f1 = (float)((Math.atan2(d1, d) * 180D) / Math.PI);
        float f2 = (float)((Math.atan2(d2, d3) * 180D) / Math.PI);
        rotationYaw = (float)(Math.atan2(moveForward, moveStrafing) / Math.PI);
    }

    /**
     * Called when the entity is attacked.
     */
    @SuppressWarnings("unused")
	public boolean attackEntityFrom(DamageSource damagesource, float f)
    {
        Entity entity = damagesource.getTrueSource();

        if (entity instanceof PlayerEntity)
        {
            angry = true;
        }

        hungry = false;
        hungrytime = 100;
        LivingEntity entityToAttack = getAttackTarget();
        entityToAttack = (LivingEntity) entity;
        return super.attackEntityFrom(damagesource, f);
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    public void attackEntity(Entity entity, float f, PlayerEntity playerentity)
    {
        if (onGround)
        {
            world.playSound(playerentity, this.getPosition(), SoundsHandler.BLORP_BOUNCE, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            double d = entity.posX - posX;
            double d2 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d2 * d2);
            moveForward = (float) ((d / (double)f1) * 0.20000000000000001D * 0.80000001192092896D + moveForward * 0.20000000298023224D);
            moveStrafing = (float) ((d2 / (double)f1) * 0.20000000000000001D * 0.80000001192092896D + moveStrafing * 0.20000000298023224D);
            moveVertical = (float) (0.70000000596246448D + (double)blorpsize * 0.050000004559D);
            fallDistance = -(25F + blorpsize * 5F);
        }
        else
        {
            double d1 = 2.5D + ((double)blorpsize - 1.5D) * 0.80000000000000004D;

            if (d1 > 3.5D)
            {
                d1 = 3.5D;
            }

            if ((double)f < d1 && entity.getBoundingBox().maxY > getBoundingBox().minY && entity.getBoundingBox().minY < getBoundingBox().maxY)
            {
                attackTime = 20;

                if (entity instanceof BlorpEntity)
                {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack + blorplevel);
                }
                else
                {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack);
                }
            }
        }
    }

    public int[] findTree(Entity entity, Double double1)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().expand(double1.doubleValue(), double1.doubleValue(), double1.doubleValue());
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.floor(axisalignedbb.maxX + 1.0D);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.floor(axisalignedbb.maxY + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.floor(axisalignedbb.maxZ + 1.0D);

        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = k; l1 < l; l1++)
            {
                for (int i2 = i1; i2 < j1; i2++)
                {
                    Block j2 = world.getBlockState(new BlockPos(k1, l1, i2)).getBlock();

                    if (j2 != Blocks.AIR && j2 == Blocks.OAK_LEAVES)
                    {
                        return (new int[]
                                {
                                    k1, l1, i2
                                });
                    }

                    if (j2 != Blocks.AIR && blorplevel > 3 && j2 == Blocks.OAK_LOG)
                    {
                        return (new int[]
                                {
                                    k1, l1, i2
                                });
                    }
                }
            }
        }

        return (new int[]
                {
                    -1, 0, 0
                });
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
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
        //int l = world.getFullBlockLightValue(i, j, k);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.GRASS || i1 == Blocks.DIRT) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG /*&& i1 != Blocks.double_stone_slab*/ && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(25) == 0 //&& l > 10
        		;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("Hungry", hungry);
        compound.putInt("BlorpLevel", blorplevel);
        compound.putFloat("BlorpSize", blorpsize);
        compound.putBoolean("Angry", angry);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        hungry = compound.getBoolean("Hungry");
        blorplevel = compound.getInt("BlorpLevel");
        blorpsize = compound.getFloat("BlorpSize");
        angry = compound.getBoolean("Angry");
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(PlayerEntity playerentity)
    {
        SoundEvent s = getLivingSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - blorpsize));
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.BLORP;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.BLORP_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.BLORP_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
            entityDropItem(ItemList.blorp_cola, blorplevel);
    	}
        super.onDeath(damagesource);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 3;
    }

	@Override
	public AgeableEntity createChild(AgeableEntity arg0) {
		return new BlorpEntity(world);
	}
}
