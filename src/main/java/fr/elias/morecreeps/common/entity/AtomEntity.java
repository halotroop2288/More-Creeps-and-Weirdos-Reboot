package fr.elias.morecreeps.common.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class AtomEntity extends FlyingEntity
{
    public static Random rand = new Random();
    protected double attackRange;
    public boolean jumping;
    public float robotsize;
    public int floattimer;
    public int floatdir;
    public float atomsize;
    public int lifespan;

    public AtomEntity(World world)
    {
        super(null, world); // EntityType??
        setRenderDistanceWeight(10D);
        attackRange = 16D;
        jumping = false;
        floattimer = 0;
        floatdir = 1;
        atomsize = 1.0F;
        lifespan = rand.nextInt(300) + 200;
//        setSize(0.5F, 0.5F);
    }
    public void applyEntityAttributes()
    {
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.61D);
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    /*public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(getBoundingBox().minY);
        int k = MathHelper.floor_double(posZ);
        int l = world.getFullBlockLightValue(i, j, k);
        int i1 = world.getBlockId(i, j - 1, k);
        return i1 != Block.cobblestone.blockID && i1 != Block.wood.blockID && i1 != Block.stairDouble.blockID && i1 != Block.stairSingle.blockID && world.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && world.checkIfAABBIsClear(boundingBox) && world.canBlockSeeTheSky(i, j, k) && rand.nextInt(10) == 0 && l > 8;
    }*/

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void livingTick()
    {
        int i = (int)posX;
        int j = (int)posY;
        int k = (int)posZ;

        for (int l = 0; (float)l < atomsize; l++)
        {
            double d = (float)i + world.rand.nextFloat();
            double d1 = (float)j + world.rand.nextFloat();
            double d2 = (float)k + world.rand.nextFloat();
            double d3 = d - posX;
            double d4 = d1 - posY;
            double d5 = d2 - posZ;
            double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
            d3 /= d6;
            d4 /= d6;
            d5 /= d6;
            double d7 = 0.5D / (d6 / (double)atomsize + 0.10000000000000001D);
            d7 *= world.rand.nextFloat() * world.rand.nextFloat() + 0.3F;
            d3 *= d7;
            d4 *= d7;
            d5 *= d7;
            world.addParticle(ParticleTypes.PORTAL, (d + posX * 1.0D) / 2D, (d1 + posY * 1.0D) / 2D + (double)(int)(atomsize / 4F), (d2 + posZ * 1.0D) / 2D, d3, d4, d5);
        }

        if (rand.nextInt(6) == 0)
        {
        	if(world.isRemote)
        	{
        		MoreCreepsReboot.proxy.foam2(world, this);
        	}
        }

        if (lifespan-- == 0)
        {
        	if(!world.isRemote)
            world.createExplosion(this, posX, posY + (double)atomsize, posZ, 1.0F, true, Mode.DESTROY);
            setHealth(0);
        }

        @SuppressWarnings("rawtypes")
		List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(atomsize * 2.0F, atomsize * 2.0F, atomsize * 2.0F));

        for (int j1 = 0; j1 < list.size(); j1++)
        {
            Entity entity = (LivingEntity)list.get(j1);
            PlayerEntity playerentity = Minecraft.getInstance().player;

            float f = getDistance(entity);

            if ((entity instanceof AtomEntity) && f < 4F)
            {
                world.playSound(playerentity, this.getPosition(), SoundsHandler.ATOM_SUCK, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                lifespan += ((AtomEntity)entity).lifespan;
                entity.remove();
                atomsize += ((AtomEntity)entity).atomsize - 0.5F;
                // setSize(atomsize * 0.6F, atomsize * 0.6F);
                continue;
            }

            if (!(entity instanceof AnimalEntity) && !(entity instanceof ItemEntity))
            {
                continue;
            }

            if (entity instanceof LivingEntity)
            {
                @SuppressWarnings("unused")
            	double targetMoveSpeed = ((LivingEntity)entity).getAttributes().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
            	targetMoveSpeed *= 0.8D;
            }

            float f1 = (0.15F / f) * atomsize;

            if (entity instanceof AtomEntity)
            {
                f1++;
            }

            if (entity.posX > posX)
            {
                moveForward -= f1;
            }

            if (entity.posX < posX)
            {
                moveForward += f1;
            }

            if (entity.posY > posY)
            {
                moveVertical -= f1;
            }

            if (entity.posY < posY)
            {
                moveVertical += f1;
            }

            if (entity.posZ > posZ)
            {
                moveStrafing -= f1;
            }

            if (entity.posZ < posZ)
            {
                moveStrafing += f1;
            }

            if (rand.nextInt(50) == 0)
            {
                world.playSound(null, null, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F)); // TODO Register sound events, set this to "atomblow"
                int k1 = (int)(atomsize / 3F) + 1;
                moveForward += rand.nextInt(k1);
                moveVertical += rand.nextInt(k1);
                moveStrafing += rand.nextInt(k1);
            }
        }
        super.livingTick();
        if (rand.nextInt(70) == 0)
        {
            moveForward += rand.nextFloat() * 1.0F - 0.5F;
        }

        if (rand.nextInt(50) == 0)
        {
            moveVertical += rand.nextFloat() * 2.0F - 0.5F;
        }

        if (rand.nextInt(70) == 0)
        {
            moveStrafing += rand.nextFloat() * 1.0F - 0.5F;
        }

        if (rand.nextInt(10) == 0)
        {
            moveVertical = -(atomsize * 0.015F);
        }
    }


    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.ATOM;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.ATOM_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.ATOM_DEATH;
    }

    public void onDeath(){
    	
    	//Don't know what it was before, but assumed it was to get the dropped items
		Block[] dropItems = (new Block[] {Blocks.COBBLESTONE, Blocks.GRAVEL, Blocks.COBBLESTONE, Blocks.GRAVEL, Blocks.IRON_ORE, Blocks.MOSSY_COBBLESTONE});
        
        //Selects random item by getting a random number, itemchooser, and applying it to item array.
        Random rand = new Random();
        int itemchooser = rand.nextInt(6) + 1;
        
        this.entityDropItem(dropItems[itemchooser], (int) this.getYOffset());
        
    }
}
