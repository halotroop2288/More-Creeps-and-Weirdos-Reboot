package fr.elias.morecreeps.common.entity;

import fr.elias.morecreeps.client.particles.CREEPSFxBubbles;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.lists.ParticleList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BaseCreepsCreatureEntity extends CreatureEntity
{
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();
    
	// TODO There are many repeated functions in other entity classes, this should be used to house those functions, and they should call this class instead of defining it themselves.
    
	public BaseCreepsCreatureEntity(EntityType<? extends CreatureEntity> entityTypeIn, World worldIn)
	{
		super(entityTypeIn, worldIn);
	}
	
	@SuppressWarnings("unused")
	public void smoke(World world)
    {
        for (int i = 0; i < 7; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            double d4 = rand.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.HEART, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d2, d4);
        }

        for (int j = 0; j < 4; j++)
        {
            for (int k = 0; k < 10; k++)
            {
                double d1 = rand.nextGaussian() * 0.02D;
                double d3 = rand.nextGaussian() * 0.02D;
                double d5 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height) + (double)j, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d1, d3, d5);
            }
        }
    }

    @SuppressWarnings("unused")
	public void smokePlain(World world)
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height) + (double)i, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
        }
    }
    
    public void setDead()
    {
        smokePlain(world);
        super.setHealth(0);

        for (int i = 0; i < 25; i++)
        {
            double d = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);
            CREEPSFxBubbles creepsfxbubbles = new CREEPSFxBubbles(world, posX + d * 0.5D, posY, posZ + d1 * 0.5D, ParticleList.CREEPS_RED, 0.5F);
            Minecraft.getInstance().effectRenderer.addEffect(creepsfxbubbles);
        }
        
        if(!world.isRemote)
        {
            if (rand.nextInt(25) == 0)
            {
                entityDropItem(Items.COOKIE, 1);
            }
        }
    }

	@Override
	protected void registerData()
	{
		
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
        super.readAdditional(compound);
        compound.getFloat("ModelSize");
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
        super.writeAdditional(compound);
        compound.putFloat("ModelSize", 0);
	}

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return null;
	}

}
