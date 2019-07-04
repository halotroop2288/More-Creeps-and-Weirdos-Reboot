package fr.elias.morecreeps.common.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;

public class ArmyGuyArmEntity extends AnimalEntity
{
    protected double attackrange;
    protected int attack;
    public int lifetime;
    public float modelsize;
    public ResourceLocation texture;
    public ArmyGuyArmEntity(World world)
    {
        super(null, world);
//        setSize(0.6F, 0.3F);
        lifetime = 250;
        modelsize = 1.0F;
        texture = new ResourceLocation(Reference.MODID, 
        		Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_ARMY_GUY_ARM);
    }

    public void applyEntityAttributes()
    {
//    	super.applyEntityAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public AnimalEntity spawnBabyAnimal(AnimalEntity entityanimal, World world)
    {
//        return new ArmyGuyArmEntity(world);
    	
    	return null; // ^ whoever wrote that is a fuckin' wierdo
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
        super.tick();

        if (lifetime-- < 0)
        {
            setDead();
        }
    }

    public void blood()
    {
    	if(world.isRemote)
    	{
    		MoreCreepsReboot.proxy.blood(world, posX, posY, posZ, true);
    	}
    }
    public void setDead()
    {
        blood();
        super.setHealth(0.0F);
    }


//	 Snipped null sound references


    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
    	if(!world.isRemote)
    	{
            entityDropItem(ItemList.limbs, 1);   		
    	}
        super.onDeath(damagesource);
    }

	@Override
	public AgeableEntity createChild(AgeableEntity arg0)
	{
		return null;
	}
}
