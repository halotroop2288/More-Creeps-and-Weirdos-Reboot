package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
import net.minecraft.world.Explosion.Mode;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class RocketEntity extends Entity
{
    protected int hitX;
    protected int hitY;
    protected int hitZ;
    protected BlockState blockHit;

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
    protected Entity playerToAttack;
    public boolean playerAttack;
    public int explodedelay;
    public LivingEntity owner;
    public Entity shootingEntity;

    public RocketEntity(World world)
    {
        super(world);
        hitX = -1;
        hitY = -1;
        hitZ = -1;
        blockHit = null;
        aoLightValueZPos = false;
        aoLightValueScratchXYNN = 0;
        setSize(0.325F, 0.1425F);
        playerFire = false;
        explodedelay = 30;
    }

    public RocketEntity(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        aoLightValueScratchXYZNNN = true;
    }

    public RocketEntity(World world, LivingEntity entityliving, float f)
    {
        this(world);
        shootingEntity = entityliving;
        owner = entityliving;
        damage = 15;
        setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        posY += 0.10000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * 0.66F;

        if (entityliving instanceof PlayerEntity)
        {
            posY -= 1.3999999761581421D;
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

        func_22374_a(motionX, motionY, motionZ, (float)(1.1499999761581421D + ((double)world.rand.nextFloat() - 0.5D)), f1);
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

    public void func_22374_a(double d, double d1, double d2, float f, float f1)
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
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return explodedelay <= 0;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (explodedelay > 0)
        {
            explodedelay--;
        }
        if(world.isRemote)
        {
        	MoreCreepsReboot.proxy.rocketSmoke(world, this, MoreCreepsReboot.partWhite); // or partBlack ?
        }
        double d = rand.nextGaussian() * 0.02D;
        double d1 = rand.nextGaussian() * 0.02D;
        double d2 = rand.nextGaussian() * 0.02D;
        world.addParticle(ParticleTypes.SMOKE, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2, new int[0]);

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
        RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d1);
        vec3d = new Vec3d(posX, posY, posZ);
        vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
       
        if (raytraceresult != null)
        {
            vec3d1 =  new Vec3(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
        }

        Entity entity = null;
        List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d3 = 0.0D;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

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
            RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

            if (raytraceresult1 == null)
            {
            	if(!world.isRemote)
            	{
                    world.createExplosion(null, posX, posY, posZ, 1.0F, true);
            	}
                checkSplashDamage();
                setDead();
                continue;
            }

            double d4 = vec3d.distanceTo(raytraceresult1.hitVec);

            if (d4 < d3 || d3 == 0.0D)
            {
            	if(!world.isRemote)
            	{
                    world.createExplosion(null, posX, posY, posZ, 1.0F, true, Mode.NONE);
            	}
                checkSplashDamage();
                entity = entity1;
                d3 = d4;
            }
        }

        if (entity != null)
        {
            raytraceresult = new RayTraceResult(entity);
        }

        if (raytraceresult != null)
        {
            if (raytraceresult.entityHit != null)
            {
                if (raytraceresult.entityHit instanceof PlayerEntity)
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

                if ((raytraceresult.entityHit instanceof LivingEntity) && !(raytraceresult.entityHit instanceof RocketGiraffeEntity))
                {
                    if (raytraceresult.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, (LivingEntity)entity), damage));

                    world.playSound(playerentity, this.getPosition(), SoundsHandler.ROCKET_EXPLODE, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    if(!world.isRemote)
                    world.createExplosion(null, posX, posY, posZ, 1.5F, true);
                    setDead();
                }
                else
                {
                    setDead();
                }
            }
            else
            {
                hitX = raytraceresult.getBlockPos().getX();
                hitY = raytraceresult.getBlockPos().getY();
                hitZ = raytraceresult.getBlockPos().getZ();
                blockHit = world.getBlockState(new BlockPos(hitX, hitY, hitZ));
                motionX = (float)(raytraceresult.getHitVec().x - posX);
                motionY = (float)(raytraceresult.getHitVec().y - posY);
                motionZ = (float)(raytraceresult.getHitVec().z - posZ);
                float f1 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                posX -= (motionX / (double)f1) * 0.05000000074505806D;
                posY -= (motionY / (double)f1) * 0.05000000074505806D;
                posZ -= (motionZ / (double)f1) * 0.05000000074505806D;
                aoLightValueZPos = true;
                world.playSound(playerentity, this.getPosition(), SoundsHandler.ROCKET_EXPLODE, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                if(!world.isRemote)
                world.createExplosion(null, posX, posY, posZ, 1.5F, true);
                checkSplashDamage();
                setDead();
            }

            world.playSound(playerentity, this.getPosition(), SoundsHandler.RAY_GUN, SoundCategory.NEUTRAL, 0.2F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
            setDead();
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f2 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
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
                world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * (double)f7, posY - motionY * (double)f7, posZ - motionZ * (double)f7, motionX, motionY, motionZ, new int[0]);
            }

            f3 = 0.8F;
            float f6 = 0.03F;
            setDead();
        }

        motionX *= f3;
        motionZ *= f3;
        setPosition(posX, posY, posZ);
    }

    public void checkSplashDamage()
    {
        PlayerEntity entityplayer = world.getClosestPlayer(this, 50D);
        List list = null;
        list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(5D, 5D, 5D));

        for (int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);

            if (!((entity != null) & (entity instanceof CreatureEntity)) || (entity instanceof RocketGiraffeEntity))
            {
                continue;
            }

            if (entityplayer != null)
            {
                ((CreatureEntity)entity).setRevengeTarget(entityplayer);
            }

            playerAttack = true;
            entity.velocityChanged = true;
            entity.motionY += 0.20000000298023224D;
            entity.attackEntityFrom(DamageSource.generic, 10);

            if (((LivingEntity)entity).getHealth() <= 0 && !entity.isDead && !((PlayerEntityMP)entityplayer).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.rocketrampage) && MoreCreepsReboot.rocketcount >= 50)
            {
                world.playSound(entityplayer, entityplayer.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                entityplayer.addStat(MoreCreepsReboot.rocketrampage, 1);
                confetti(entityplayer);
            }
        }
    }

    public void goBoom()
    {
    	if(world.isRemote)
    	{
    		MoreCreepsReboot.proxy.rocketGoBoom(world, this, rand);
    	}
    }

    public void confetti(PlayerEntity player)
    {
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
        world.spawnEntityInWorld(creepsentitytrophy);
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        super.setDead();
        goBoom();
        shootingEntity = null;
    }

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
	@Override
	protected void readAdditional(CompoundNBT compound)
	{
        hitX = compound.getShort("xTile");
        hitY = compound.getShort("yTile");
        hitZ = compound.getShort("zTile");
        aoLightValueZPos = compound.getByte("inGround") == 1;
	}
	
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
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
