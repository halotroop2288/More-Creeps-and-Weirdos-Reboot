package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class DogHouseEntity extends AnimalEntity
{
    public float modelsize;
    public boolean houseoccupied;
    public String texture;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();

    public DogHouseEntity(World world)
    {
        super(null, world);
        texture = Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "doghouse.png";
        modelsize = 2.5F;
        setSize(width * modelsize, height * modelsize);
        houseoccupied = false;
    }

    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        if (getRidingEntity() instanceof HotdogEntity)
        {
            return - (double)1.1F;
        }
        else
        {
            return super.getYOffset();
        }
    }

    public void updateRiderPosition()
    {
        if (getRidingEntity() == null)
        {
            return;
        }

        if (getRidingEntity() instanceof HotdogEntity)
        {
        	getRidingEntity().setPosition(posX, posY, posZ);
            return;
        }
        else
        {
            return;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate(World world)
    {
        if (rand.nextInt(10) == 0 && getRidingEntity() != null)
        {
        	if(world.isRemote)
        		MoreCreepsReboot.proxy.bubbleDoghouse(world, this);
        }

        if (inWater)
        {
            setDead();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        ignoreFrustumCheck = true;
        super.tick();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return getHealth() < 1;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @SuppressWarnings("rawtypes")
	public boolean interact(PlayerEntity playerentity, World world)
    {
        ItemStack itemstack = playerentity.inventory.getCurrentItem();

        if (getRidingEntity() == null)
        {
            List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(16D, 16D, 16D));
            int i = 0;

            do
            {
                if (i >= list.size())
                {
                    break;
                }

                Entity entity = (Entity)list.get(i);

                if ((entity instanceof HotdogEntity) && entity.getRidingEntity() == null)
                {
                    HotdogEntity creepsentityhotdog = (HotdogEntity)entity;

                    if (creepsentityhotdog.tamed)
                    {
                        creepsentityhotdog.addPassenger(this);
                        houseoccupied = true;
                        world.playSound(playerentity, this.getPosition(), SoundsHandler.HOT_DOG_PICK_UP, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        break;
                    }
                }

                i++;
            }
            while (true);
        }
        else
        {
            getRidingEntity().fallDistance = -10F;
            world.playSound(playerentity, this.getPosition(), SoundsHandler.HOT_DOG_PUT_DOWN, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            houseoccupied = false;
            getRidingEntity().removePassengers();
        }

        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i, World world, PlayerEntity playerentity)
    {
        if (i < 1)
        {
            i = 1;
        }

        hurtTime = maxHurtTime = 10;
        smoke(world);

        if (getHealth() <= 0)
        {
        	world.playSound(playerentity, this.getPosition(), getDeathSound(), SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            onDeath(damagesource);
        }
        else
        {
        	world.playSound(playerentity, this.getPosition(), getHurtSound(), SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }

        super.attackEntityFrom(damagesource, i);
        return true;
    }

    @SuppressWarnings("rawtypes")
	public void loadHouse(World world)
    {
        List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(16D, 16D, 16D));

        for (int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);

            if (entity != null && (entity instanceof HotdogEntity) && ((HotdogEntity)entity).tamed)
            {
                entity.removePassengers();
                houseoccupied = true;
                return;
            }
        }

        houseoccupied = false;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        return getHealth() <= 0;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("ModelSize", modelsize);
        compound.putBoolean("Occupied", houseoccupied);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("ModelSize");
        houseoccupied = compound.getBoolean("Occupied");
        loadHouse(world);
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
    	if(world.isRemote)
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)i) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)i - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)i) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)i - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)i) - (double)width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)i - (double)width, d, d1, d2);
            }
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return null;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return null;
    }

    public void confetti(World world)
    {
    	List<PlayerEntity> list = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).expand(8D, 4D, 8D));
    	for(int i = 0; i < list.size(); i++)
    	{
    		Entity entity = (Entity) list.get(i);
    		float f = this.getDistance(entity);
    		if(f < 6F)
    		{
    			if(!world.isRemote)
    			{
        	    	MoreCreepsReboot.proxy.confettiA((PlayerEntity) entity, world);
    			}
    		}
    	}
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead(PlayerEntity playerentity)
    {
        smoke();
        world.playSound(playerentity, this.getPosition(), getDeathSound(), SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        super.setHealth(0.0F);
    }

	@Override
	public AgeableEntity createChild(AgeableEntity ageable) {
		return new DogHouseEntity(world);
	}
}
