package fr.elias.morecreeps.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.lists.ItemList;

public class FrisbeeEntity extends ThrowableEntity implements IProjectile
{
    private int xTileNBT;
    private int yTileNBT;
    private int zTileNBT;
    private int inTileNBT;
    public int shakeNBT;
    private LivingEntity livingentity;
    private int field_20050_h;
    private int field_20049_i;
    protected double initialVelocity;
    double bounceFactor;
    public int lifespan;

    public FrisbeeEntity(World world)
    {
        super(null, world);
//        setSize(0.25F, 0.25F);
        initialVelocity = 1.0D;
        bounceFactor = 0.14999999999999999D;
        lifespan = 120;
    }
    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double d)
    {
        double d1 = getBoundingBox().getAverageEdgeLength() * 4D;
        d1 *= 64D;
        return d < d1 * d1;
    }

    public FrisbeeEntity(World world, Entity entity)
    {
        this(world);
        setRotation(entity.rotationYaw, 0.0F);
        double d = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
        motionX = 0.59999999999999998D * d * (double)MathHelper.cos((entity.rotationPitch / 180F) * (float)Math.PI);
        motionY = -0.69999999999999996D * (double)MathHelper.sin((entity.rotationPitch / 180F) * (float)Math.PI);
        motionZ = 0.59999999999999998D * d1 * (double)MathHelper.cos((entity.rotationPitch / 180F) * (float)Math.PI);
        setPosition(entity.posX + d * 0.80000000000000004D, entity.posY, entity.posZ + d1 * 0.80000000000000004D + (3D * d1 + d));
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
    }

    public FrisbeeEntity(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
    }

    public void setThrowableHeading(double d, double d1, double d2, float f, float f1)
    {
        float f2 = MathHelper.sqrt(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d1 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d2 += rand.nextGaussian() * 0.0074999998323619366D * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;
        float f3 = MathHelper.sqrt(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / Math.PI);
        field_20050_h = 0;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double d, double d1, double d2)
    {
        motionX = d;
        motionY = d1;
        motionZ = d2;

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(d * d + d2 * d2);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f) * 180D) / Math.PI);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate(World world)
    {
        double d = motionX;
        double d1 = motionY;
        double d2 = motionZ;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        moveEntity(motionX, motionY, motionZ);

        if (handleWaterMovement())
        {
            motionY += 0.0087999999523162842D;
            motionX *= 0.97999999999999998D;
            motionZ *= 0.68000000000000005D;
        }

        if (motionX != d)
        {
            motionX = -bounceFactor * d;
        }

        if (motionY != d1)
        {
            motionY = -bounceFactor * d1;
        }

        if (motionY != d1)
        {
            motionY = -bounceFactor * d1;
        }
        else if (!handleWaterMovement())
        {
            motionY -= 0.0050000000000000001D;
        }

        motionX *= 0.97999999999999998D;
        motionY *= 0.999D;
        motionZ *= 0.97999999999999998D;

        if (collidedVertically)
        {
            motionX *= 0.25D;
            motionZ *= 0.25D;
        }
		if(onGround && lifespan--<0)
		{
			if(!world.isRemote)
			this.entityDropItem(ItemList.frisbee, 1);
			remove();
		}
    }

    /*public void onCollideWithPlayer(EntityPlayer entityplayer)
    {
        if (onGround && livingentity == entityplayer && shakeNBT <= 0 && !world.isRemote)
        {
            world.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            setDead();
            entityplayer.onItemPickup(this, 1);
            entityplayer.inventory.addItemStackToInventory(new ItemStack(MoreCreepsAndWeirdos.frisbee, 1, 0));
        }
    }*/

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(CompoundNBT nbttagcompound)
    {
        nbttagcompound.putShort("xTile", (short)xTileNBT);
        nbttagcompound.putShort("yTile", (short)yTileNBT);
        nbttagcompound.putShort("zTile", (short)zTileNBT);
        nbttagcompound.putByte("inTile", (byte)inTileNBT);
        nbttagcompound.putByte("shake", (byte)shakeNBT);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(CompoundNBT nbttagcompound)
    {
        xTileNBT = nbttagcompound.getShort("xTile");
        yTileNBT = nbttagcompound.getShort("yTile");
        zTileNBT = nbttagcompound.getShort("zTile");
        inTileNBT = nbttagcompound.getByte("inTile") & 0xff;
        shakeNBT = nbttagcompound.getByte("shake") & 0xff;
    }
	@Override
	protected void onImpact(RayTraceResult result) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void registerData() {
		// TODO Auto-generated method stub
		
	}
}
