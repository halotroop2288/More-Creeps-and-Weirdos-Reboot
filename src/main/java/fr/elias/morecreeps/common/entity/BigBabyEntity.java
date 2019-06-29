package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ai.EntityBigBabyAI;

public class BigBabyEntity extends MobEntity
{
    public int skinDirection;
    public int skin;
    public int skinTimer;
    public float modelsize;
    public float hammerswing;
    public ResourceLocation texture;
    public int attackTime;
    public float height = getheight();
    public float width = getWidth();
    public float length = getWidth();

    public BigBabyEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID, 
        		Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BIGBABY0);
        setSize(width * 5.25F, height * 5.55F);
        skinDirection = 1;
        skinTimer = 0;
        skin = 0;
        modelsize = 6.5F;
        hammerswing = 0.0F;
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityBigBabyAI(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
    }
    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80D);
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
    	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (hammerswing < 0.0F)
        {
            hammerswing += 0.1000055F;
        }
        else
        {
            hammerswing = 0.0F;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (skinTimer++ > 60)
        {
            skinTimer = 0;
            skin += skinDirection;

            if (skin == 4 || skin == 0)
            {
                skinDirection *= -1;
            }

            if (this.getAttackTarget() != null)
            {
                skin = 0;
            }

            if (skin < 0 || skin > 4)
            {
                skin = 0;
            }

            texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + (new StringBuilder()).append(Reference.TEXTURE_BIGBABY).append(String.valueOf(skin)).append(".png").toString());
        }

        super.onLivingUpdate();
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    public void attackEntity(Entity entity, float f)
    {
        if (onGround)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            motionX = (d / (double)f1) * 0.20000000000000001D * (0.850000011920929D + motionX * 0.20000000298023224D);
            motionZ = (d1 / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + motionZ * 0.20000000298023224D);
            motionY = 0.14400000596246448D;
            fallDistance = -25F;

            if ((double)f < 6D && (double)f > 3D && rand.nextInt(5) == 0)
            {
                double d2 = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
                double d3 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);
                motionX += d2 * 0.25D;
                motionZ += d3 * 0.25D;
                motionY += 0.15000000596046448D;
            }
        }

        if ((double)f < (double)modelsize * 0.69999999999999996D + 1.5D && entity.getEntityBoundingBox().maxY > getEntityBoundingBox().minY && entity.getEntityBoundingBox().minY < getEntityBoundingBox().maxY && rand.nextInt(10) == 0)
        {
            if (hammerswing == 0.0F)
            {
                hammerswing = -1.1F;
            }

            attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i, World world)
    {
        Entity entity = damagesource.getTrueSource();
        texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BIGBABYBOP);
        skinTimer = 45;

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
        {
            if (riddenByEntity == entity || ridingEntity == entity)
            {
                return true;
            }

            if (entity != this && world.getDifficulty() != EnumDifficulty.PEACEFUL)
            {
            	this.setRevengeTarget((LivingEntity) entity);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(PlayerEntity entityplayer, World world)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (itemstack != null && itemstack.getItem() == MoreCreepsReboot.babyjarempty)
        {
            if (modelsize < 1.0F)
            {
                setDead();
                entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(MoreCreepsReboot.babyjarfull));
                MoreCreepsReboot.proxy.addChatMessage("Now turn that Baby into a Schlump on the floor");
                world.playSound(this, "morecreeps:babytakehome", 1.0F, 1.0F);
            }
            else
            {
            	MoreCreepsReboot.proxy.addChatMessage("That baby is too large");
                world.playSound(this, "morecreeps:babyshrink", 1.0F, 1.0F);
            }
        }

        return true;
    }

    public float getEyeHeight()
    {
        return height * 0.15F;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere(World world)
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(getEntityBoundingBox().minY);
        int k = MathHelper.floor_double(posZ);
        int l = world.getBlockLightOpacity(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.cobblestone && i1 != Blocks.log && i1 != Blocks.double_stone_slab && i1 != Blocks.stone_slab && i1 != Blocks.planks && i1 != Blocks.wool && world.getCollidingBoundingBoxes(this, getEntityBoundingBox()).size() == 0 && rand.nextInt(50) == 0 && world.canSeeSky(new BlockPos(i, j, k)) && l > 6;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(World world)
    {
        String s = getLivingSound();

        if (s != null)
        {
            world.playSoundAtEntity(this, s, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (6.5F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "morecreeps:bigbaby";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "morecreeps:bigbabyhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "morecreeps:bigbabyhurt";
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
            dropItem(Items.porkchop, rand.nextInt(6) + 5);
    	}
        super.onDeath(damagesource);
    }
}
