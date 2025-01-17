package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class InvisibleManEntity extends MobEntity
{
    private static final ItemStack defaultHeldItem;
    private static final Item dropItems[];
    protected double attackRange;
    private int angerLevel;
    private int randomSoundDelay;
    public float modelsize;
    public String texture;

    public InvisibleManEntity(World world)
    {
        super(null, world);
        texture = "morecreeps:textures/entity/invisibleman.png";
        angerLevel = 0;
        modelsize = 1.0F;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new InvisibleManEntity.AIAttackEntity());
        // this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
        // this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(4, new EntityAILookIdle(this));
        // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        // if(angerLevel > 0)
        // this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, PlayerEntity.class, true));
    }
    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        World world = Minecraft.getInstance().world;
        if ((getAttackTarget() instanceof PlayerEntity) && angerLevel == 0) {
            texture = "morecreeps:textures/entity/invisiblemanmad.png";
            angerLevel = rand.nextInt(15) + 5;
        }
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                .setBaseValue(getAttackTarget() != null ? 0.75D : 0.5D);

        super.tick();

        if (rand.nextInt(30) == 0 && angerLevel > 0)
        {
            angerLevel--;

            if (angerLevel == 0)
            {
                world.playSound(this, "morecreeps:invisiblemanforgetit", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                this.setAttackTarget(null);
                texture = "morecreeps:textures/entity/invisibleman.png";
            }
        }
    }

    class AIAttackEntity extends EntityAIBase
    {
		@Override
		public boolean shouldExecute()
		{
			return InvisibleManEntity.this.findPlayerToAttack();
		}
		
		public void updateTask()
		{
			float f = InvisibleManEntity.this.getDistance(getAttackTarget());
			if(f < 256F)
			{
				attackEntity(InvisibleManEntity.this.getAttackTarget(), f);
				InvisibleManEntity.this.getLookController().setLookPositionWithEntity(InvisibleManEntity.this.getAttackTarget(), 10.0F, 10.0F);
				InvisibleManEntity.this.getNavigator().clearPath();
				InvisibleManEntity.this.getMoveHelper().setMoveTo(InvisibleManEntity.this.getAttackTarget().posX, InvisibleManEntity.this.getAttackTarget().posY, InvisibleManEntity.this.getAttackTarget().posZ, 0.5D);
			}
			if(f < 1F)
			{
				InvisibleManEntity.this.attackEntityAsMob(InvisibleManEntity.this.getAttackTarget());
			}
		}
    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short)angerLevel);
        nbttagcompound.setFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        angerLevel = nbttagcompound.getShort("Anger");
        modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
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
        return i1 != Blocks.SAND && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.STONE_SLAB
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(15) == 0; //&& l > 7;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected boolean findPlayerToAttack()
    {
        if (angerLevel == 0)
        {
            return false;
        }
        else
        {
            texture = "morecreeps:textures/entity/invisiblemanmad.png";
            return true;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (onGround)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d1 * d1);
            moveForward = (float) ((d / (double)f1) * 0.20000000000000001D * 0.80000001192092896D + getMotion().x * 0.20000000298023224D);
            moveStrafing = (float) ((d1 / (double)f1) * 0.20000000000000001D * 0.80000001192092896D + getMotion().z * 0.20000000298023224D);
            moveVertical = (float) 0.20000000596246448D;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void livingTick()
    {
        super.livingTick();
    }

    public boolean canAttackEntity22(Entity entity, float i)
    {
        if (entity instanceof PlayerEntity)
        {
            List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(25D, 25D, 25D));

            for (int j = 0; j < list.size(); j++)
            {
                Entity entity1 = (Entity)list.get(j);

                if (entity1 instanceof InvisibleManEntity)
                {
                    InvisibleManEntity creepsentityinvisibleman = (InvisibleManEntity)entity1;
                    creepsentityinvisibleman.becomeAngryAt(entity);
                }
            }

            becomeAngryAt(entity);
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    private void becomeAngryAt(Entity entity)
    {
    	setRevengeTarget((LivingEntity) entity);
        angerLevel += rand.nextInt(40);
        texture = "morecreeps:textures/entity/invisiblemanmad.png";
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
        if (angerLevel == 0)
        {
            return "morecreeps:invisibleman";
        }
        else
        {
            return "morecreeps:invisiblemanangry";
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "morecreeps:invisiblemanhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "morecreeps:invisiblemandeath";
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return defaultHeldItem;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected Item getDropItem()
    {
        return dropItems[rand.nextInt(dropItems.length)];
    }

    static
    {
        defaultHeldItem = new ItemStack(Items.STICK, 1);
        dropItems = (new Item[]
                {
                    Items.CAKE, Items.STICK, Items.STICK,Items.APPLE,
                });
    }
}
