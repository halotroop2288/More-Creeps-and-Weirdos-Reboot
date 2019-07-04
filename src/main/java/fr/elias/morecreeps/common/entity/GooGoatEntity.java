package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

@SuppressWarnings("unused")
public class GooGoatEntity extends SheepEntity
{
	protected double attackrange;
    public boolean bone;
    protected int attack;
    public float goatsize;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();

    /** Entity motion Y */
    public int eaten;
    public boolean hungry;
    public int hungrytime;
    public int goatlevel;
    public float modelspeed;
    public boolean angry;
    private int angerLevel;
    private int randomSoundDelay;
    public String texture;

    public GooGoatEntity(World world)
    {
        super(null, world);
        bone = false;
        texture = "morecreeps:textures/entity/googoat1.png";
        attack = 1;
        attackrange = 16D;
        goatsize = 0.7F;
        hungry = false;
        angry = false;
        hungrytime = rand.nextInt(100) + 10;
        goatlevel = 1;
        modelspeed = 0.45F;
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, PlayerEntity.class, 0.45D, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new GooGoatEntity.AIAttackEntity(this, PlayerEntity.class, true));
    }

    @Override
    protected void registerAttributess()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate(World world, PlayerEntity playerentity)
    {
        if (modelspeed < 0.05F)
        {
            modelspeed = 0.05F;
        }

        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)modelspeed);
        super.onLivingUpdate();

        if (hungry)
        {
            int i = MathHelper.floor(posX);
            int j = MathHelper.floor(getBoundingBox().minY);
            int k = MathHelper.floor(posZ);
            Block l = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();

            if (l == Blocks.GRASS && rand.nextInt(10) == 0)
            {
                world.setBlockState(new BlockPos(i, j - 1, k), Blocks.DIRT.getDefaultState());
                hungrytime = hungrytime + rand.nextInt(100) + 25;

                if (hungrytime > 300 && goatlevel < 5)
                {
                    hungry = false;
                    hungrytime = 0;
                    goatsize = goatsize + 0.2F;
                    goatlevel++;
                    attack++;
                    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(15 * goatlevel + 25));
                    texture = (new StringBuilder()).append("morecreeps:textures/entity/googoat").append(goatlevel).append(".png").toString();
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.GOO_GOAT_STRETCH, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
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

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    /*protected Entity findPlayerToAttack()
    {
        float f = getBrightness(1.0F);

        if (f < 0.0F || angry)
        {
            PlayerEntity playerentity = world.getClosestPlayerToEntity(this, attackrange);

            if (playerentity != null)
            {
                return playerentity;
            }
        }

        if (rand.nextInt(10) == 0)
        {
            LivingEntityBase livingentity = getClosestTarget(this, 6D);
            return livingentity;
        }
        else
        {
            return null;
        }
    }

    public LivingEntityBase getClosestTarget(Entity entity, double d)
    {
        double d1 = -1D;
        LivingEntity livingentity = null;

        for (int i = 0; i < world.loadedEntityList.size(); i++)
        {
            Entity entity1 = (Entity)world.loadedEntityList.get(i);

            if (!(entity1 instanceof LivingEntity) || entity1 == entity || entity1 == entity.riddenByEntity || entity1 == entity.ridingEntity || (entity1 instanceof PlayerEntity) || (entity1 instanceof EntityMob) || (entity1 instanceof EntityAnimal))
            {
                continue;
            }

            double d2 = entity1.getDistanceSq(entity.posX, entity.posY, entity.posZ);

            if ((d < 0.0D || d2 < d * d) && (d1 == -1D || d2 < d1) && ((LivingEntity)entity1).canEntityBeSeen(entity))
            {
                d1 = d2;
                livingentity = (LivingEntity)entity1;
            }
        }

        return livingentity;
    }*/
    class AIAttackEntity extends EntityAINearestAttackableTarget {

		public AIAttackEntity(CreatureEntity creature, Class class, boolean bool)
		{
			super(creature, class, bool);
		}

		public boolean shouldExecute() {
			return angry && GooGoatEntity.this.getAttackTarget() != null && super.shouldExecute();
		}
		public void startExecuting()
	    {
	        if (onGround)//TODO move this on updateTask() if isn't working 
	        {
	            double d = targetEntity.posX - posX;
	            double d1 = targetEntity.posZ - posZ;
	            float f1 = MathHelper.sqrt(d * d + d1 * d1);
	            motionX = (d / (double)f1) * 0.20000000000000001D * (0.850000011920929D + (double)goatlevel * 0.10000000000000001D) + motionX * 0.20000000298023224D;
	            motionZ = (d1 / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + (double)goatlevel * 0.10000000000000001D) + motionZ * 0.20000000298023224D;
	            motionY = 0.10000000596246449D + (double)goatlevel * 0.070000002559000005D;
	            fallDistance = -25F;
	        }
	        super.startExecuting();
	    }
		/*
		public void updateTask()
		{
		}*/

	}
    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getTrueSource();
        hungry = false;

        if (entity instanceof PlayerEntity)
        {
            angry = true;
        }
        return super.attackEntityFrom(damagesource, i);
    }

    private void becomeAngryAt(Entity entity)
    {
        setRevengeTarget((LivingEntity) entity);
        angerLevel = 400 + rand.nextInt(400);
        randomSoundDelay = rand.nextInt(40);
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    /*protected void attackEntity(Entity entity, float f)
    {
        if (onGround)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            motionX = (d / (double)f1) * 0.20000000000000001D * (0.850000011920929D + (double)goatlevel * 0.10000000000000001D) + motionX * 0.20000000298023224D;
            motionZ = (d1 / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + (double)goatlevel * 0.10000000000000001D) + motionZ * 0.20000000298023224D;
            motionY = 0.10000000596246449D + (double)goatlevel * 0.070000002559000005D;
            fallDistance = -25F;
        }

        if ((double)f < 2D + (double)goatlevel * 0.10000000000000001D && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack);
        }
    }*/

    /*public int[] findTree(Entity entity, Material material, Double double1)
    {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().expand(double1.doubleValue(), double1.doubleValue(), double1.doubleValue());
        int i = MathHelper.floor_double(axisalignedbb.minX);
        int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
        int k = MathHelper.floor_double(axisalignedbb.minY);
        int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(axisalignedbb.minZ);
        int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);

        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = k; l1 < l; l1++)
            {
                for (int i2 = i1; i2 < j1; i2++)
                {
                    int j2 = world.getBlockId(k1, l1, i2);

                    if (j2 != 0 && Block.blocksList[j2].blockMaterial == material)
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
    }*/

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere(World world)
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getBlockLightOpacity(new BlockPos(i, j, k));
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.GRASS || i1 == Blocks.DIRT) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(40) == 0 && l > 7;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("Hungry", hungry);
        compound.putInt("GoatLevel", goatlevel);
        compound.putFloat("GoatSize", goatsize);
        compound.putFloat("ModelSpeed", modelspeed);
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
        goatlevel = compound.getInt("GoatLevel");
        goatsize = compound.getFloat("GoatSize");
        modelspeed = compound.getFloat("ModelSpeed");
        angry = compound.getBoolean("Angry");
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.GOO_GOAT;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.GOO_GOAT_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.GOO_GOAT_DEATH;
    }

    public void confetti(World world)
    {
    	MoreCreepsReboot.proxy.confettiA(this, world);
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
        Object obj = damagesource.getTrueSource();

        if ((obj instanceof RocketEntity) && ((RocketEntity)obj).owner != null)
        {
            obj = ((RocketEntity)obj).owner;
        }

        if (obj instanceof PlayerEntity)
        {
            MoreCreepsReboot.goatcount++;
            boolean flag = false;
            ServerPlayerEntity player = (ServerPlayerEntity) obj;

            if (!player.getStatFile().hasAdvancementUnlocked(MoreCreepsReboot.gookill))
            {
                flag = true;
                player.addStat(MoreCreepsReboot.gookill, 1);
                confetti();
            }

            if (!player.getStatFile().hasAdvancementUnlocked(MoreCreepsReboot.gookill10) && MoreCreepsReboot.goatcount >= 10)
            {
                flag = true;
                player.addStat(MoreCreepsReboot.gookill10, 1);
                confetti();
            }

            if (!player.getStatFile().hasAdvancementUnlocked(MoreCreepsReboot.gookill25) && MoreCreepsReboot.goatcount >= 25)
            {
                flag = true;
                player.addStat(MoreCreepsReboot.gookill25, 1);
                confetti();
            }

            if (flag)
            {
                world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
            }
        }

        int i = (goatlevel - 1) + rand.nextInt(2);

        if (i > 0)
        {
            if(!world.isRemote)
            entityDropItem(ItemList.goo_donut, i);
        }

        super.onDeath(damagesource);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }

	public AgeableEntity createChild(AgeableEntity ageable, World world)
	{
		return new GooGoatEntity(world);
	}
}
