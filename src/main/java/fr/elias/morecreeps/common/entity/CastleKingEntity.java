package fr.elias.morecreeps.common.entity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class CastleKingEntity extends MobEntity
{
    private boolean foundplayer;
    private PathEntity pathToEntity;
    protected Entity playerToAttack;
    PlayerEntity playerentity;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    public int intrudercheck;
    public ItemStack gem;
    public String texture;
    public double moveSpeed;
    public float attackStrength;
    public double health;
    private static ItemStack defaultHeldItem;
    public static Random random = new Random();
    public float hammerswing;
    

    public CastleKingEntity(World world)
    {
        super(null, world);
        texture = Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "castleking.png";
        moveSpeed = 0.0;
        attackStrength = 4;
        health = rand.nextInt(60) + 60;
        setSize(2.0F, 1.6F);
        foundplayer = false;
        intrudercheck = 25;
        hammerswing = 0.0F;
    }
    
    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(moveSpeed);
    	this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(attackStrength);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate(World world)
    {
        super.onUpdate();

        if (hammerswing < 0.0F)
        {
            hammerswing += 0.45F;
        }
        else
        {
            hammerswing = 0.0F;
        }

        double d = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);
        CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, (posX + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D, ((posY - 1.0D) + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D, (posZ + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D, MoreCreepsReboot.partBlue, 0.55F, 0);
        creepsfxsmoke.renderDistanceWeight = 20D;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);

        if (intrudercheck-- < 0 && this.attackEntityAsMob(null))
        {
            intrudercheck = 25;
            PlayerEntity playerentity = world.getClosestPlayerToEntity(this, 10D);

            if (playerentity != null && canEntityBeSeen(playerentity))
            {
                moveSpeed = 0.222F;
            }
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
            double d2 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d2 * d2);
            moveForward = (float) ((d / (double)f1) * 0.40000000000000002D * 0.20000000192092895D + moveForward * 0.18000000098023225D);
            moveStrafing = (float) ((d2 / (double)f1) * 0.40000000000000002D * 0.17000000192092896D + moveStrafing * 0.18000000098023225D);
        }

        if ((double)f < 6D)
        {
            double d1 = entity.posX - posX;
            double d3 = entity.posZ - posZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d3 * d3);
            moveForward = (float) ((d1 / (double)f2) * 0.40000000000000002D * 0.20000000192092895D + moveForward * 0.18000000098023225D);
            moveStrafing = (float) ((d3 / (double)f2) * 0.40000000000000002D * 0.070000001920928964D + moveStrafing * 0.18000000098023225D);
            rotationPitch = 90F;
        }

        if ((double)f < 3.2000000000000002D && entity.getBoundingBox().maxY > this.getBoundingBox().minY && entity.getBoundingBox().minY < this.getBoundingBox().maxY)
        {
            if (hammerswing == 0.0F)
            {
                hammerswing = -2.6F;
            }

            //attackTime = 10;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackStrength);
        }

        super.attackEntityAsMob(entity);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("FoundPlayer", foundplayer);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        foundplayer = compound.getBoolean("FoundPlayer");
        attackStrength = 8;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.CASTLE_KING;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.CASTLE_KING_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.CASTLE_KING_DEATH;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere(World world)
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(this.getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getBlockLightOpacity(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && world.checkBlockCollision(getBoundingBox()) && world.canBlockSeeSky(getPosition()) && rand.nextInt(5) == 0 && l > 10;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    private void smoke(World world)
    {
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                double d = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)((float)i * 0.5F)) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)((float)i * 0.5F) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)((float)i * 0.5F)) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)((float)i * 0.5F) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)((float)i * 0.5F)) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)((float)i * 0.5F) - (double)width, d, d1, d2);
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

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
        int i = 0;
        gem = new ItemStack(ItemList.sky_gem, 1);

        if (checkGem(gem))
        {
            i++;
        }

        gem = new ItemStack(ItemList.earth_gem, 1);

        if (checkGem(gem))
        {
            i++;
        }

        gem = new ItemStack(ItemList.fire_gem, 1);

        if (checkGem(gem))
        {
            i++;
        }

        gem = new ItemStack(ItemList.healing_gem, 1);

        if (checkGem(gem))
        {
            i++;
        }

        gem = new ItemStack(ItemList.mining_gem, 1);

        if (checkGem(gem))
        {
            i++;
        }

        if (i == 5)
        {
            smoke(world);
            smoke(world);
            entityDropItem(ItemList.gem_sword, 1);
            entityDropItem(ItemList.money, rand.nextInt(100) + 50);
        }
        else
        {
            entityDropItem(Items.IRON_SWORD, 1);
            entityDropItem(Items.BOOK, 1);
        }

        smoke();
        super.onDeath(damagesource);
    }

    public boolean checkGem(ItemStack itemstack)
    {
        
        Object obj = null;
        NonNullList<ItemStack> aitemstack = ((PlayerEntity)(playerentity)).inventory.mainInventory;
        boolean flag = false;
        int i = 0;

        do
        {
            if (i >= aitemstack.length)
            {
                break;
            }

            ItemStack itemstack1 = aitemstack[i];

            if (itemstack1 != null && itemstack1 == itemstack)
            {
                flag = true;
                break;
            }

            i++;
        }
        while (true);

        return flag;
    }

    static
    {
        defaultHeldItem = new ItemStack(ItemList.gem_sword, 1);
    }
}
