package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class FoamEntity extends Entity implements IProjectile
{
    protected int hitX;
    protected int hitY;
    protected int hitZ;
    protected Block blockHit;

    /** Light value one block more in z axis */
    protected boolean aoLightValueZPos;

    /** Light value of the block itself */
    public LivingEntity lightValueOwn;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/west corner.
     */
    protected int aoLightValueScratchXYZNNP;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the north face.
     */
    protected int aoLightValueScratchXYNN;
    protected int damage;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/east corner.
     */
    protected boolean aoLightValueScratchXYZNNN;
    protected boolean playerFire;
    public Entity shootingEntity;

    public FoamEntity(World world)
    {
        super(null, world);
        hitX = -1;
        hitY = -1;
        hitZ = -1;
        blockHit = Blocks.AIR;
        aoLightValueZPos = false;
        aoLightValueScratchXYNN = 0;
        setSize(0.00325F, 0.001125F);
        playerFire = false;
    }

    public FoamEntity(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        aoLightValueScratchXYZNNN = true;
    }

    public FoamEntity(World world, LivingEntity entityliving, float f)
    {
        this(world);
        shootingEntity = entityliving;
        damage = 1;
        double d = -MathHelper.sin((entityliving.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((entityliving.rotationYaw * (float)Math.PI) / 180F);
        setLocationAndAngles(entityliving.posX + d * 0.40000000596046448D, (entityliving.posY + (double)entityliving.getEyeHeight()) - 0.25D, entityliving.posZ + d1 * 0.80000001192092896D, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * 0.16F;

        if (entityliving instanceof PlayerEntity)
        {
            posY += 0.20000000298023224D;
        }

        setPosition(posX, posY, posZ);
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionY = -MathHelper.sin((rotationPitch / 180F) * (float)Math.PI);
        float f1 = 1.0F;

        if (entityliving instanceof PlayerEntity)
        {
            playerFire = true;
            float f2 = 0.3333333F;
            float f3 = f2 / 0.1F;

            if (f3 > 0.0F)
            {
                f1 = (float)((double)f1 * (1.0D + 2D / (double)f3));
            }
        }

        if (Math.abs(entityliving.motionX) > 0.10000000000000001D || Math.abs(entityliving.motionY) > 0.10000000000000001D || Math.abs(entityliving.motionZ) > 0.10000000000000001D)
        {
            f1 *= 2.0F;
        }

        if (entityliving.onGround);

        setThrowableHeading(motionX, motionY, motionZ, (float)(2.380000114440918D + ((double)world.rand.nextFloat() - 0.5D)), f1);
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(PlayerEntity entityplayer)
    {
    }

    protected void entityInit()
    {
    }

    public void setThrowableHeading(double d, double d1, double d2, float f, float f1)
    {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
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
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float)((Math.atan2(d, d2) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float)((Math.atan2(d1, f3) * 180D) / Math.PI);
        aoLightValueScratchXYZNNP = 0;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate(PlayerEntity playerentity)
    {
        super.onUpdate();

        for (int i = 0; i < 15; i++)
        {
            double d = rand.nextGaussian() * 0.10000000000000001D;
            double d1 = rand.nextGaussian() * 0.10000000000000001D;
            double d2 = rand.nextGaussian() * 0.10000000000000001D;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (double)(rand.nextFloat() * 2.0F), posY, posZ + (double)(rand.nextFloat() * 2.0F), d, d1, d2);
        }

        if (aoLightValueScratchXYNN == 100)
        {
            setDead();
        }

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / Math.PI);
        }

        if (aoLightValueZPos)
        {
            Block j = world.getBlockState(new BlockPos(hitX, hitY, hitZ)).getBlock();

            if (j != blockHit)
            {
                aoLightValueZPos = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionY *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                aoLightValueScratchXYZNNP = 0;
                aoLightValueScratchXYNN = 0;
            }
            else
            {
                aoLightValueScratchXYZNNP++;

                if (aoLightValueScratchXYZNNP == 100)
                {
                    setDead();
                }

                return;
            }
        }
        else
        {
            aoLightValueScratchXYNN++;
        }

        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        Vec3d vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
        RayTraceResult movingobjectposition = world.rayTraceBlocks(vec3d, vec3d1);
        vec3d = new Vec3d(posX, posY, posZ);
        vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

        if (movingobjectposition != null)
        {
            vec3d1 = new Vec3d(movingobjectposition.getHitVec().x, movingobjectposition.getHitVec().y, movingobjectposition.getHitVec().z);
        }

        Entity entity = null;
        @SuppressWarnings("rawtypes")
		List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d3 = 0.0D;

        for (int k = 0; k < list.size(); k++)
        {
            Entity entity1 = (Entity)list.get(k);

            if (!entity1.canBeCollidedWith() || (entity1 == shootingEntity || shootingEntity != null && entity1 == shootingEntity.ridingEntity) && aoLightValueScratchXYNN < 5 || aoLightValueScratchXYZNNN)
            {
                if (motionZ != 0.0D || !((motionX == 0.0D) & (motionY == 0.0D)))
                {
                    continue;
                }

                setDead();
                break;
            }

            float f4 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox().expand(f4, f4, f4);
            RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

            if (movingobjectposition1 == null)
            {
                continue;
            }

            double d4 = vec3d.distanceTo(movingobjectposition1.hitVec);

            if (d4 < d3 || d3 == 0.0D)
            {
                entity = entity1;
                d3 = d4;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new World(entity);
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.getHitVec() != null)
            {
                if (movingobjectposition.getHitVec(). instanceof LivingEntity)
                {
                    splash();
                    movingobjectposition.getHitVec().setFire(0);

                    movingobjectposition.getHitVec().attackEntityFrom(DamageSource.causeThrownDamage(this, (LivingEntity)entity), 0);

                    setDead();
                }
                else
                {
                    setDead();
                }
            }
            else
            {
                hitX = movingobjectposition.getBlockPos().getX();
                hitY = movingobjectposition.getBlockPos().getY();
                hitZ = movingobjectposition.getBlockPos().getZ();
                blockHit = world.getBlockState(new BlockPos(hitX, hitY, hitZ)).getBlock();
                motionX = (float)(movingobjectposition.getHitVec().x - posX);
                motionY = (float)(movingobjectposition.getHitVec().y - posY);
                motionZ = (float)(movingobjectposition.getHitVec().z - posZ);
                float f1 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                posX -= (motionX / (double)f1) * 0.05000000074505806D;
                posY -= (motionY / (double)f1) * 0.05000000074505806D;
                posZ -= (motionZ / (double)f1) * 0.05000000074505806D;
                aoLightValueZPos = true;

                for (int l = -2; l < 2; l++)
                {
                    for (int i1 = -2; i1 < 2; i1++)
                    {
                        for (int j1 = -2; j1 < 2; j1++)
                        {
                            if (world.getBlockState(new BlockPos(hitX + l, hitY + j1, hitZ + i1)).getBlock() == Blocks.fire)
                            {
                                world.setBlockToAir(new BlockPos(hitX + l, hitY + j1, hitZ + i1));
                            }
                        }
                    }
                }

                setDead();
            }

            world.playSound(playerentity, this.getPosition(), SoundsHandler.RAY_GUN, SoundCategory.PLAYERS, 0.2F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
            setDead();
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f2 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);

        for (rotationPitch = (float)((Math.atan2(motionY, f2) * 180D) / Math.PI); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }

        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }

        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f3 = 0.99F;
        float f5 = 0.0F;

        if (handleWaterMovement())
        {
            for (int k1 = 0; k1 < 4; k1++)
            {
                float f7 = 0.25F;
                world.addParticle(ParticleTypes.BUBBLE, posX - motionX * (double)f7, posY - motionY * (double)f7, posZ - motionZ * (double)f7, motionX, motionY, motionZ);
            }

            f3 = 0.8F;
            float f6 = 0.03F;
            setDead();
        }

        motionX *= f3;
        motionZ *= f3;
        setPosition(posX, posY, posZ);
    }


    public static DamageSource causeFoamDamage(EntityFoam creepsentityfoam, Entity entity)
    {
        return (new EntityDamageSourceIndirect("foam", creepsentityfoam, entity)).setFireDamage().setProjectile();
    }

    public void blast()
    {
    	if(world.isRemote)
    		MoreCreepsReboot.proxy.smoke2(world, this, rand);
    }

    public void splash()
    {
    	if(world.isRemote)
    		MoreCreepsReboot.proxy.blood(world, posX, posY, posZ, true);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        blast();
        super.setDead();
        shootingEntity = null;
    }

	@Override
	public void shoot(double arg0, double arg1, double arg2, float arg3, float arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
	@Override
	protected void readAdditional(CompoundNBT compound) {
        hitX = compound.getShort("xTile");
        hitY = compound.getShort("yTile");
        hitZ = compound.getShort("zTile");
        aoLightValueZPos = compound.getByte("inGround") == 1;
		
	}
	
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
	@Override
	protected void writeAdditional(CompoundNBT compound) {
        compound.putShort("xTile", (short)hitX);
        compound.putShort("yTile", (short)hitY);
        compound.putShort("zTile", (short)hitZ);
        compound.putByte("inGround", (byte)(aoLightValueZPos ? 1 : 0));
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		// TODO Auto-generated method stub
		return null;
	}
}
