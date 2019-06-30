package fr.elias.morecreeps.common.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class TrophyEntity extends CreatureEntity
{
    public int partytime;
    public int trophylifespan;
    public float spin;
    public ResourceLocation texture;
    
    public TrophyEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "trophy.png");
        partytime = rand.nextInt(30) + 40;
        trophylifespan = 80;
        // setSize(1.0F, 2.5F);
    }

    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
        if (partytime-- > 1)
        {
            MoreCreepsReboot.proxy.confettiB(world, this);
        }

        if (trophylifespan-- < 0)
        {
            setHealth(0);
        }

        super.tick();
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
        return true;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.MUMMY;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.MUMMY_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.MUMMY_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
        for (int i = 0; i < rand.nextInt(25) + 10; i++)
        {
            entityDropItem(ItemList.money, 1);
        }

        super.setHealth(0);
    }
}
