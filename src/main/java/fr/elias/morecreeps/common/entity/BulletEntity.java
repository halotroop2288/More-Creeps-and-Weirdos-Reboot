package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
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
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.particles.CREEPSFxBlood;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class BulletEntity extends Entity
{
    protected int hitX;
    protected int hitY;
    protected int hitZ;
    protected Block blockHit;
    public double motionX = getMotion().x;
    public double motionY = getMotion().y;
    public double motionZ = getMotion().z;

    /** Light value one block more in z axis */
    protected boolean aoLightValueZPos;

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

    public BulletEntity(World world)
    {
        super(null, world);
        hitX = -1;
        hitY = -1;
        hitZ = -1;
        blockHit = Blocks.AIR;
        aoLightValueZPos = false;
        aoLightValueScratchXYNN = 0;
        setSize(0.0325F, 0.01125F);
        playerFire = false;
    }

    public BulletEntity(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        aoLightValueScratchXYZNNN = true;
    }

    public BulletEntity(World world, LivingEntity entityliving, float f)
    {
        this(world);
        shootingEntity = entityliving;
        damage = 2;
        setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        posY -= 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * 0.16F;

        if (entityliving instanceof PlayerEntity)
        {
            posY -= 0.40000000596046448D;
        }

        setPosition(posX, posY, posZ);
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionY = -MathHelper.sin((rotationPitch / 180F) * (float)Math.PI);

        if (entityliving instanceof ArmyGuyEntity)
        {
            damage = 1;
        }

        if ((entityliving instanceof ArmyGuyEntity) && ((ArmyGuyEntity)entityliving).loyal)
        {
            damage = 5;
        }

        if (entityliving instanceof SneakySalEntity)
        {
            PlayerEntity playerentity = world.getClosestPlayer(this, 25D);

            if (playerentity != null)
            {
                posY -= 1.7999999523162842D;
                double d = (playerentity.rotationPitch / 180F) * (float)Math.PI;
                double d1 = playerentity.posY - posY;
                motionY += d1 / 20D;

                if (entityliving instanceof SneakySalEntity)
                {
                    motionY += 0.076999999582767487D;
                }
            }
        }

        float f1 = 1.0F;

        if (entityliving instanceof PlayerEntity)
        {
            playerFire = true;
            damage = 6;
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

        func_22374_a(motionX, motionY, motionZ, (float)(2.380000114440918D + ((double)world.rand.nextFloat() - 0.5D)), f1);
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(PlayerEntity playerentity)
    {
    }

    protected void entityInit()
    {
    }

    public void func_22374_a(double d, double d1, double d2, float f, float f1)
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
        aoLightValueScratchXYZNNP = 0;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    @Override
    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	World world = Minecraft.getInstance().world;
        super.tick();

        if (aoLightValueScratchXYNN == 100)
        {
            setDead();
        }

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / Math.PI);
        }

        if (aoLightValueZPos)
        {
            Block i = world.getBlockState(new BlockPos(hitX, hitY, hitZ)).getBlock();

            if (i != blockHit)
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
        List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (!entity1.canBeCollidedWith() || (entity1 == shootingEntity || shootingEntity != null && entity1 == shootingEntity.getRidingEntity()) && aoLightValueScratchXYNN < 5 || aoLightValueScratchXYZNNN)
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

            double d1 = vec3d.distanceTo(movingobjectposition1.getHitVec());

            if (d1 < d || d == 0.0D)
            {
                entity = entity1;
                d = d1;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new RayTraceResult(entity);
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.entityHit != null)
            {
                if (movingobjectposition.entityHit instanceof PlayerEntity)
                {
                    int k = damage;

                    if (world.getDifficulty() == Difficulty.PEACEFUL)
                    {
                        k = 0;
                    }

                    if (world.getDifficulty() == Difficulty.EASY)
                    {
                        k = k / 3 + 1;
                    }

                    if (world.getDifficulty() == Difficulty.HARD)
                    {
                        k = (k * 3) / 2;
                    }
                }

                if ((movingobjectposition.entityHit instanceof LivingEntity) && playerFire || !(movingobjectposition.entityHit instanceof FloobEntity) || playerFire)
                {
                    if (!(movingobjectposition.entityHit instanceof RobotToddEntity) && !(movingobjectposition.entityHit instanceof RobotTedEntity) && CREEPSConfig.Blood)
                    {
                        blood();
                    }

                    if (movingobjectposition.subHit.attackEntityFrom(DamageSource.causeThrownDamage(this, shootingEntity), damage));

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
                setDead();

                if (blockHit == Blocks.ICE)
                {
                    world.setBlockState(new BlockPos(hitX, hitY, hitZ), Blocks.WATER.getDefaultState());
                }

                if (CREEPSConfig.rayGunFire && blockHit == Blocks.GLASS)
                {
                    world.setBlockState(new BlockPos(hitX, hitY, hitZ), Blocks.AIR.getDefaultState());
                    Blocks.GLASS.onPlayerDestroy(world, new BlockPos(hitX, hitY, hitZ), world.getBlockState(new BlockPos(hitX, hitY, hitZ)));
                }

                setDead();
            }

            world.playSound(playerentity, this.getPosition(), SoundsHandler.RAY_GUN, SoundCategory.NEUTRAL, 0.2F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
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
            for (int l = 0; l < 4; l++)
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

//    /**
//     * (abstract) Protected helper method to write subclass entity data to NBT.
//     */
//    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
//    {
//        nbttagcompound.setShort("xTile", (short)hitX);
//        nbttagcompound.setShort("yTile", (short)hitY);
//        nbttagcompound.setShort("zTile", (short)hitZ);
//        nbttagcompound.setByte("inGround", (byte)(aoLightValueZPos ? 1 : 0));
//    }
//
//    /**
//     * (abstract) Protected helper method to read subclass entity data from NBT.
//     */
//    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
//    {
//        hitX = nbttagcompound.getShort("xTile");
//        hitY = nbttagcompound.getShort("yTile");
//        hitZ = nbttagcompound.getShort("zTile");
//        aoLightValueZPos = nbttagcompound.getByte("inGround") == 1;
//    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public void blast()
    {
    	if(world.isRemote)
    	{
    		MoreCreepsReboot.proxy.smoke2(world, this, rand);
    	}
    }

    public void blood()
    {
    	if(world.isRemote)
    	{
    		MoreCreepsReboot.proxy.blood(world, posX, posY, posZ, true);
    	}
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        blast();
        super.remove();
        shootingEntity = null;
    }

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		// TODO Auto-generated method stub
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		// TODO Auto-generated method stub
		return null;
	}
}
