package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.lists.ItemList;

public class FloobEntity extends MobEntity
{
    private boolean foundplayer;
    private boolean stolen;
    protected Entity playerToAttack;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    protected ItemStack heldObj;
    private double goX;
    private double goZ;
    private float distance;
    public int itemnumber;
    public int stolenamount;
    public int rayTime;
    private Entity targetedEntity;
    public float modelsize;
    public String texture;

    public FloobEntity(World world)
    {
        super(null, world);
        texture = "morecreeps:textures/entity/floob.png";
        stolen = false;
        hasAttacked = false;
        foundplayer = false;
        heldObj = new ItemStack(ItemList.ray_gun, 1);
        rayTime = rand.nextInt(50) + 50;
        // isImmuneToFire = true;
        modelsize = 1.0F;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        // this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(8, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        // this.tasks.addTask(8, new EntityAILookIdle(this));
        // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        // this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, PlayerEntity.class, true));
    }
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick(World world)
    {
        if (this.getAttackTarget() instanceof FloobEntity)
        {
        	this.setAttackTarget(null);
        }

        targetedEntity = world.getClosestPlayer(this, 3D);

        if (targetedEntity != null && (targetedEntity instanceof PlayerEntity) && canEntityBeSeen(targetedEntity))
        {
            float f = rotationYaw;

            for (int i = 0; i < 360; i++)
            {
                rotationYaw = i;
            }

            if (rand.nextInt(4) == 0)
            {
                setAttackTarget((LivingEntity) targetedEntity);
            }
        }

        if (rayTime-- < 1)
        {
            rayTime = rand.nextInt(50) + 25;
            double d = 64D;
            targetedEntity = world.getClosestPlayer(this, 30D);

            if (targetedEntity != null && canEntityBeSeen(targetedEntity) && targetedEntity == getAttackTarget() && (targetedEntity instanceof PlayerEntity) && !dead)
            {
                double d1 = targetedEntity.getDistanceSq(this);

                if (d1 < d * d && d1 > 3D)
                {
                    double d2 = targetedEntity.posX - posX;
                    double d3 = (targetedEntity.getBoundingBox().minY + (double)(targetedEntity.getHeight() / 2.0F)) - (posY + (double)(getHeight() / 2.0F));
                    double d4 = targetedEntity.posZ - posZ;
                    renderYawOffset = rotationYaw = (-(float)Math.atan2(d2, d4) * 180F) / (float)Math.PI;
                    world.playSound(this, "morecreeps:raygun", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    RayEntity creepsentityray = new RayEntity(world, this);

                    if (creepsentityray != null && getHealth() > 0)
                    {
                    	if(!world.isRemote)
                        world.addEntity(creepsentityray);
                    }
                }
            }
        }

        super.livingTick();
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack(World world)
    {
    	PlayerEntity entityplayer = world.getClosestPlayer(this, 20D);

        if (entityplayer != null && canEntityBeSeen(entityplayer))
        {
            return entityplayer;
        }
        else
        {
            return null;
        }
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(World world)
    {
        String s = getLivingSound();

        if (s != null)
        {
            world.playSound(this, s, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "morecreeps:floob";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "morecreeps:floobhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "morecreeps:floobdeath";
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere(World world)
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        //int l = world.getFullBlockLightValue(i, j, k);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.STONE_SLAB && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(5) == 0// && l > 10
        		;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return heldObj;
    }

    public void confetti(World world)
    {
    	MoreCreepsReboot.proxy.confettiA(this, world);
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
        Object obj = damagesource.getTrueSource();

        PlayerEntity player = (PlayerEntity) damagesource.getTrueSource();
        
        if ((obj instanceof RocketEntity) && ((RocketEntity)obj).owner != null)
        {
            obj = ((RocketEntity)obj).owner;
        }

        if (obj instanceof PlayerEntity)
        {
            MoreCreepsReboot.floobcount++;

            if (!((PlayerEntityMP)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achievefloobkill))
            {
                world.playSound(player, "morecreeps:achievement", 1.0F, 1.0F);
                player.addStat(MoreCreepsReboot.achievefloobkill, 1);
                confetti();
            }

            if (!((PlayerEntityMP)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achievefloobicide) && MoreCreepsReboot.floobcount >= 20)
            {
                world.playSound(player, "morecreeps:achievement", 1.0F, 1.0F);
                player.addStat(MoreCreepsReboot.achievefloobicide, 1);
                confetti();
            }
        }

        if (rand.nextInt(6) == 0)
        {
            dropItem(MoreCreepsReboot.raygun, 1);
        }

        super.onDeath(damagesource);
    }
}
