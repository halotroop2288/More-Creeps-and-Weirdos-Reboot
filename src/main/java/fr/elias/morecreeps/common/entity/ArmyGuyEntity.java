package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.Random;

import com.google.common.base.Predicate;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ArmyGuyEntity extends MobEntity implements IRangedAttackMob, IEntityAdditionalSpawnData
{
    public ItemStack defaultHeldItem;
    public int weapon;
    public int timeleft;
    public String ss;
    public boolean armright;
    public boolean armleft;
    public boolean legright;
    public boolean legleft;
    public boolean shrunk;
    public boolean helmet;
    public boolean head;
    public float modelsize;
    private Entity targetedEntity;
    public boolean loyal;
    public float distance;
    public int attack;
    public int attackTime;
    public float height = getHeight();
    public float width = getWidth();

    public ArmyGuyEntity(World world)
    {
        super(null, world);
        armright = false;
        armleft = false;
        legright = false;
        legleft = false;
        shrunk = false;
        helmet = false;
        head = false;
        defaultHeldItem = new ItemStack(ItemList.gun, 1);
        modelsize = 1.0F;
        loyal = false;
        attack = 1;
        attackTime = 20;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityCreature.class, 1.0D, false));
        // this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F));
        // this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
        // this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(5, new EntityAILookIdle(this));
        // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        // this.targetTasks.addTask(2, new ArmyGuyEntity.AINearestAttackableTarget(this, CreatureEntity.class, 3, false, false, IMob.mobSelector));
    }

    @Override
	public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(70D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
    }

    /**
    * Makes the entity despawn if requirements are reached
    */
    @Override
    protected void checkDespawn() {
    if (!this.isNoDespawnRequired() && !this.func_213392_I())
    {
        Entity entity = this.world.getClosestPlayer(this, -1.0D);
        net.minecraftforge.eventbus.api.Event.Result result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this);
        if (result == net.minecraftforge.eventbus.api.Event.Result.DENY)
        {
            idleTime = 0;
            entity = null;
        }
        else if (result == net.minecraftforge.eventbus.api.Event.Result.ALLOW)
        {
            this.remove();
            entity = null;
        }
        if (entity != null) {
            double d0 = entity.getDistanceSq(this);
            if (d0 > 16384.0D && this.canDespawn(d0))
            {
                this.remove();
            }

          if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d0 > 1024.0D && this.canDespawn(d0))
            {
             this.remove();
            }
          else if (d0 < 1024.0D)
          {
             this.idleTime = 0;
          }
       }

    }
    else
    {
       this.idleTime = 0;
    }
 }

    public ArmyGuyEntity(World world, Entity entity, double d, double d1, double d2, boolean flag)
    {
        this(world);
        setPosition(d, d1, d2);
    }
    /**
     * Plays living's sound at its position
     */
    @Override
    public void playAmbientSound()
    {
        PlayerEntity playerentity = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (rand.nextInt(7) == 0)
        {
            return SoundsHandler.ARMY;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.ARMY_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.ARMY_DEATH;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void livingTick()
    {
        World world = Minecraft.getInstance().world;
        PlayerEntity playerentity = Minecraft.getInstance().player;
        
        super.livingTick();
        float health = getHealth();
        if (health < 60 && health > 50 && !helmet)
        {
        	helmet = true;
            world.playSound(playerentity, this.getPosition(),SoundsHandler.ARMY_HELMET, SoundCategory.HOSTILE, 1.0F, 0.95F);
            if(world.isRemote)
            {
                MoreCreepsReboot.proxy.blood(world, posX, posY + 2.5D, posZ, false);
            }
        }
        else if (health < 50 && health > 40 && !armleft)
        {
        	helmet = true;
        	armleft = true;
            world.playSound(playerentity, this.getPosition(), SoundsHandler.ARMY_ARM, SoundCategory.HOSTILE, 1.0F, 0.95F);
            ArmyGuyArmEntity creepsentityarmyguyarm = new ArmyGuyArmEntity(world);
            creepsentityarmyguyarm.setLocationAndAngles(posX, posY + 1.0D, posZ, rotationYaw, 0.0F);
            creepsentityarmyguyarm.moveForward = 0.25F;
            creepsentityarmyguyarm.moveVertical = 0.25F;
            creepsentityarmyguyarm.modelsize = modelsize;
            if(!world.isRemote)
            {
                world.addEntity(creepsentityarmyguyarm);
            }
            if(world.isRemote)
            {
                MoreCreepsReboot.proxy.blood(world, posX - 0.5D, posY + 1.0D, posZ, true);
            }
        }
        else if (health < 40 && health > 30 && !legright)
        {
        	helmet = true;
        	armleft = true;
        	legright = true;
            world.playSound(playerentity, this.getPosition(), SoundsHandler.ARMY_LEG, SoundCategory.HOSTILE, 1.0F, 0.95F);
            ArmyGuyArmEntity creepsentityarmyguyarm1 = new ArmyGuyArmEntity(world);
            creepsentityarmyguyarm1.setLocationAndAngles(posX, posY + 1.0D, posZ, rotationYaw, 0.0F);
            creepsentityarmyguyarm1.moveForward = 0.25F;
            creepsentityarmyguyarm1.moveVertical = 0.25F;
            creepsentityarmyguyarm1.texture = new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + 
            		Reference.TEXTURE_ARMY_GUY_LEG);
            creepsentityarmyguyarm1.modelsize = modelsize;
            if(!world.isRemote)
            {
            	world.addEntity(creepsentityarmyguyarm1);
            }

            if(world.isRemote)
            {
                MoreCreepsReboot.proxy.blood(world, posX - 0.5D, posY, posZ, true);
            }
        }
        else if (health < 30 && health > 20 && !legleft)
        {
        	helmet = true;
        	armleft = true;
        	legright = true;
        	legleft = true;
        	world.playSound(playerentity, this.getPosition(), SoundsHandler.ARMY_BOTH_LEGS, SoundCategory.HOSTILE, 1.0F, 0.95F);
            ArmyGuyArmEntity creepsentityarmyguyarm2 = new ArmyGuyArmEntity(world);
            creepsentityarmyguyarm2.setLocationAndAngles(posX, posY + 1.0D, posZ, rotationYaw, 0.0F);
            creepsentityarmyguyarm2.moveForward = 0.25F;
            creepsentityarmyguyarm2.moveVertical = -0.25F;
            creepsentityarmyguyarm2.texture = new ResourceLocation(Reference.MODID, 
            		Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_ARMY_GUY_LEG);
            creepsentityarmyguyarm2.modelsize = modelsize;
            if(!world.isRemote)
            {
                world.addEntity(creepsentityarmyguyarm2);
            }
            if(world.isRemote)
            {
                MoreCreepsReboot.proxy.blood(world, posX + 0.5D, posY, posZ, true);
            }
        }
        else if (health < 20 && health > 10 && !armright)
        {
        	helmet = true;
        	armleft = true;
        	legright = true;
        	legleft = true;
        	armright = true;
        	world.playSound(playerentity, this.getPosition(), SoundsHandler.ARMY_ARM, SoundCategory.HOSTILE, 1.0F, 0.95F);
            ArmyGuyArmEntity creepsentityarmyguyarm3 = new ArmyGuyArmEntity(world);
            creepsentityarmyguyarm3.setLocationAndAngles(posX, posY + 1.0D, posZ, rotationYaw, 0.0F);
            creepsentityarmyguyarm3.moveForward = 0.25F;
            creepsentityarmyguyarm3.moveVertical = 0.25F;
            creepsentityarmyguyarm3.modelsize = modelsize;
            if(!world.isRemote)
            {
                world.addEntity(creepsentityarmyguyarm3);
                if (rand.nextInt(10) == 0)
                {
                    entityDropItem(ItemList.gun, 1);
                }
            }
            defaultHeldItem = null;

            if(world.isRemote)
            {
                MoreCreepsReboot.proxy.blood(world, posX, posY + 1.0D, posZ, true);
            }
        }
        else if (health < 10 && health > 0 && !head)
        {
        	helmet = true;
        	armleft = true;
        	legright = true;
        	legleft = true;
        	armright = true;
        	head = true;
            defaultHeldItem = null;
            world.playSound(playerentity, this.getPosition(), SoundsHandler.ARMY_HEAD, SoundCategory.HOSTILE, 1.0F, 0.95F);

            if(world.isRemote)
            {
                MoreCreepsReboot.proxy.blood(world, posX, posY, posZ, true);
            }
        }
    }
    public double getYOffset()
    {
    	if (legleft && legright && head)
        {
            return -1.4D + (1.0D - (double)modelsize);
        }
        else if (legleft && legright)
        {
            return -0.75D + (1.0D - (double)modelsize);
        }
        else
        {
            return 0.0D;
        }
    }
    @Override
    public void tick()
    {
        if (this.getAttackTarget() instanceof ArmyGuyArmEntity)
        {
           setAttackTarget(null);
        }
        if (loyal && this.getAttackTarget() != null && ((this.getAttackTarget() instanceof ArmyGuyEntity) || (this.getAttackTarget() instanceof GuineaPigEntity) || (this.getAttackTarget() instanceof HunchbackEntity)) && ((ArmyGuyEntity)this.getAttackTarget()).loyal)
        {
        	setAttackTarget(null);
        }
        LivingEntity livingbase = (LivingEntity)this.getAttackTarget();
        if(livingbase != null && (livingbase.getRevengeTarget() instanceof PlayerEntity))
        {
        	setAttackTarget(livingbase);
        }
        if(!loyal && this.getAttackTarget() != null && (this.getAttackTarget() instanceof ArmyGuyEntity))
        {
        	setAttackTarget(null);
        }
    	System.out.println("[ENTITY] ArmyGuy is loyal :" + loyal);
        if (legright)
        {
        	this.setJumping(true);
        }

        super.tick();
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        //fixed light position getter
        int l = world.getLight(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.ACACIA_LOG && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
        // && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        && rand.nextInt(10) == 0 && l > 8;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("ArmRight", armright);
        compound.putBoolean("ArmLeft", armleft);
        compound.putBoolean("LegRight", legright);
        compound.putBoolean("LegLeft", legleft);
        compound.putBoolean("Helmet", helmet);
        compound.putBoolean("Head", head);
        compound.putFloat("ModelSize", modelsize);
        compound.putBoolean("Loyal", loyal);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
	public void readAdditionalT(CompoundNBT compound)
    {
        super.readAdditional(compound);
        armright = compound.getBoolean("ArmRight");
        armleft = compound.getBoolean("ArmLeft");
        legright = compound.getBoolean("LegRight");
        legleft = compound.getBoolean("LegLeft");
        helmet = compound.getBoolean("Helmet");
        head = compound.getBoolean("Head");
        modelsize = compound.getFloat("ModelSize");
        loyal = compound.getBoolean("Loyal");
        
        if (helmet)
        {
            setHealth(60);
        }

        if (armleft)
        {
            setHealth(50);
        }

        if (legright)
        {
            setHealth(40);
        }

        if (armright)
        {
            setHealth(30);
            defaultHeldItem = null;
        }

        if (legleft)
        {
            setHealth(20);
        }

        if (head)
        {
            setHealth(10);
        }
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected Item getDropItem()
    {
        return Items.ARROW;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world, Random rand)
    {
    	if(!world.isRemote)
    	{
            if (rand.nextInt(5) == 0)
            {
                entityDropItem(ItemList.gun, 1);
            }
            else
            {
                entityDropItem(Items.APPLE, rand.nextInt(2));
            }
    	}
    }

    /**
     * Returns the item that this LivingEntity is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return defaultHeldItem;
    }
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor)
    {
        PlayerEntity playerentity = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;
	    double d2 = targetedEntity.posX - posX;
	    double d3 = targetedEntity.posZ - posZ;
	    renderYawOffset = rotationYaw = (-(float)Math.atan2(d2, d3) * 180F) / (float)Math.PI;
	    world.playSound(playerentity, this.getPosition(), SoundsHandler.BULLET, SoundCategory.NEUTRAL, 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
	    BulletEntity creepsentitybullet = new BulletEntity(world);
	    if(!world.isRemote && !armright)
	    world.addEntity(creepsentitybullet);
	}
    static class AINearestAttackableTarget extends NearestAttackableTargetGoal<LivingEntity>
    {
    	public ArmyGuyEntity armyGuy;
        @SuppressWarnings({ "rawtypes", "unchecked" })
		public AINearestAttackableTarget(final ArmyGuyEntity entity, Class targetClass, int i, boolean b1, boolean b2, final Predicate p)
        {
            super(entity, targetClass, i, b1, b2, p);
            armyGuy = entity;
        }
        public boolean shouldExecute()
        {
        	LivingEntity target = (LivingEntity)armyGuy.getAttackTarget();
        	if ((target instanceof PlayerEntity) || (target instanceof ArmyGuyEntity) || (target instanceof HunchbackEntity) || (target instanceof GuineaPigEntity))
            {
                return false;
            }else
        	return super.shouldExecute();
        }
    }


	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeBoolean(loyal);
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		loyal = additionalData.readBoolean();
	}
}
