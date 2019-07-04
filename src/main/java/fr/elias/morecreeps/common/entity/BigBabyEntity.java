package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ai.EntityBigBabyAI;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class BigBabyEntity extends MobEntity
{
    public int skinDirection;
    public int skin;
    public int skinTimer;
    public float modelsize;
    public float hammerswing;
    public ResourceLocation texture;
    public int attackTime;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();

    public BigBabyEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID, 
        		Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BIGBABY0);
//        setSize(width * 5.25F, height * 5.55F);
        skinDirection = 1;
        skinTimer = 0;
        skin = 0;
        modelsize = 6.5F;
        hammerswing = 0.0F;
//        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
//        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(1, new EntityBigBabyAI(this));
//        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
//        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
//        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
//        this.tasks.addTask(8, new EntityAILookIdle(this));
//        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
    }
    
    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
    	this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
        super.tick();

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
    public void livingTick()
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

            texture = new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + (new StringBuilder()).append(Reference.TEXTURE_BIGBABY).append(String.valueOf(skin)).append(".png").toString());
        }

        super.livingTick();
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
            float f1 = MathHelper.sqrt(d * d + d1 * d1);
            setMotion((d / (double)f1) * 0.20000000000000001D * (0.850000011920929D + getMotion().x * 0.20000000298023224D), // motionX
            		0.14400000596246448D, // motionY
            		(d1 / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + getMotion().z * 0.20000000298023224D)); // motionZ
            fallDistance = -25F;

            if ((double)f < 6D && (double)f > 3D && rand.nextInt(5) == 0)
            {
                double d2 = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
                double d3 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);
                setMotion(getMotion().x + d2 * 0.25D, getMotion().y + d3 * 0.25D, getMotion().z +  0.15000000596046448D);
            }
        }

        if ((double)f < (double)modelsize * 0.69999999999999996D + 1.5D && entity.getBoundingBox().maxY > getBoundingBox().minY && entity.getBoundingBox().minY < getBoundingBox().maxY && rand.nextInt(10) == 0)
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
        texture = new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BIGBABYBOP);
        skinTimer = 45;

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
        {
            if (getPassengers() == entity || getRidingEntity() == entity)
            {
                return true;
            }

            if (entity != this && world.getDifficulty() != Difficulty.PEACEFUL)
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
    @Override
    public boolean processInteract(PlayerEntity playerentity, Hand hand)
    {
    	World world = Minecraft.getInstance().world;
        ItemStack itemstack = playerentity.inventory.getCurrentItem();

        if (itemstack != null && itemstack.getItem() == ItemList.baby_jar_empty)
        {
            if (modelsize < 1.0F)
            {
                setHealth(0);
                playerentity.inventory.setInventorySlotContents(playerentity.inventory.currentItem, new ItemStack(ItemList.baby_jar_full));
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
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getLight(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& rand.nextInt(50) == 0 && world.canBlockSeeSky(new BlockPos(i, j, k)) && l > 6;
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
    @Override
    public void playAmbientSound()
    {
    	World world = Minecraft.getInstance().world;
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (6.5F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.BIG_BABY;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.BIG_BABY_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.BIG_BABY_HURT;
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
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource)
    {
    	World world = Minecraft.getInstance().world;
    	if(!world.isRemote)
    	{
            entityDropItem(Items.PORKCHOP, rand.nextInt(6) + 5);
    	}
        super.onDeath(damagesource);
    }
}
