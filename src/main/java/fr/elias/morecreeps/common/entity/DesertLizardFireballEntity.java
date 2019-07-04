package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.Explosion.Mode;

public class DesertLizardFireballEntity extends Entity {
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private boolean inGround;
    public int field_9406_a;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public Entity shootingEntity;
    public double motionX = getMotion().x;
    public double motionY = getMotion().y;
    public double motionZ = getMotion().z;

    public DesertLizardFireballEntity(World world) {
        super(null, world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = null;
        inGround = false;
        field_9406_a = 0;
        ticksInAir = 0;
        setSize(0.5F, 0.5F);
    }

    protected void entityInit() {
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and
     * comparing it to its average edge length * 64 * renderDistanceWeight Args:
     * distance
     */
    public boolean isInRangeToRenderDist(double d) {
        double d1 = getBoundingBox().getAverageEdgeLength() * 4D;
        d1 *= 64D;
        return d < d1 * d1;
    }

    public DesertLizardFireballEntity(World world, LivingEntity entityliving, double d, double d1, double d2) {
        super(null, world);
        xTile = -1;
        yTile = -1;
        zTile = -1;
        inTile = null;
        inGround = false;
        field_9406_a = 0;
        ticksInAir = 0;
        shootingEntity = entityliving;
        setSize(0.1F, 0.1F);
        setLocationAndAngles(entityliving.posX, entityliving.posY, entityliving.posZ, entityliving.rotationYaw,
                entityliving.rotationPitch);
        setPosition(posX, posY, posZ);
        motionX = motionY = motionZ = 0.0D;
        d += rand.nextGaussian() * 0.40000000000000002D;
        d1 += rand.nextGaussian() * 0.40000000000000002D;
        d2 += rand.nextGaussian() * 0.40000000000000002D;
        double d3 = MathHelper.sqrt(d * d + d1 * d1 + d2 * d2);
        accelerationX = (d / d3) * 0.10000000000000001D;
        accelerationY = (d1 / d3) * 0.10000000000000001D;
        accelerationZ = (d2 / d3) * 0.10000000000000001D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        super.tick();
        setFire(10);

        if (field_9406_a > 0) {
            field_9406_a--;
        }

        if (inGround) {
            Block i = world.getBlockState(new BlockPos(xTile, yTile, zTile)).getBlock();

            if (i != inTile) {
                inGround = false;
                motionX *= rand.nextFloat() * 0.1F;
                motionY *= rand.nextFloat() * 0.1F;
                motionZ *= rand.nextFloat() * 0.08F;
                ticksAlive = 0;
                ticksInAir = 0;
            } else {
                ticksAlive++;

                if (ticksAlive == 1200) {
                    remove();
                }

                return;
            }
        } else {
            ticksInAir++;
        }

        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        Vec3d vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
        RayTraceResult movingobjectposition = world.rayTraceBlocks(vec3d, vec3d1);
        vec3d = new Vec3d(posX, posY, posZ);
        vec3d1 = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);

        if (movingobjectposition != null) {
            vec3d1 = new Vec3d(movingobjectposition.getHitVec().x, movingobjectposition.getHitVec().y,
                    movingobjectposition.getHitVec().z);
        }

        Entity entity = null;
        List list = world.getEntitiesWithinAABBExcludingEntity(this,
                getBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);

            if (!entity1.canBeCollidedWith() || entity1 == shootingEntity && ticksInAir < 25) {
                continue;
            }

            float f2 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox().expand(f2, f2, f2);
            RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

            if (movingobjectposition1 == null) {
                continue;
            }

            double d1 = vec3d.distanceTo(movingobjectposition1.getHitVec());

            if (d1 < d || d == 0.0D) {
                entity = entity1;
                d = d1;
            }
        }

        if (entity != null) {
            movingobjectposition = new RayTraceResult(entity);
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.hitInfo != null) {
                if (movingobjectposition.hitInfo.attackEntityFrom(DamageSource.causeThrownDamage(this, (LivingEntity) entity), 2))
                    ;
            }

            if (!world.isRemote) {
                world.createExplosion(this, posX, posY, posZ, 1.0F, true, Mode.NONE);
            }
            remove();
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);

        for (rotationPitch = (float) ((Math.atan2(motionY, f) * 180D) / Math.PI); rotationPitch
                - prevRotationPitch < -180F; prevRotationPitch -= 360F) {
        }

        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) {
        }

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) {
        }

        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) {
        }

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
        rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        float f1 = 0.95F;

        if (handleWaterMovement()) {
            for (int k = 0; k < 4; k++) {
                float f3 = 0.25F;
                world.addParticle(ParticleTypes.BUBBLE, posX - motionX * (double) f3, posY - motionY * (double) f3,
                        posZ - motionZ * (double) f3, motionX, motionY, motionZ);
            }

            f1 = 0.8F;
        }

        motionX += accelerationX;
        motionY += accelerationY;
        motionZ += accelerationZ;
        motionX *= f1;
        motionY *= f1;
        motionZ *= f1;
        world.addParticle(ParticleTypes.SMOKE, posX, posY + 0.5D, posZ, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.SMOKE, posX, posY + 0.10000000000000001D, posZ, 0.0D, 0.0D, 0.0D);
        setPosition(posX, posY, posZ);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putShort("xTile", (short) xTile);
        compound.putShort("yTile", (short) yTile);
        compound.putShort("zTile", (short) zTile);
        compound.putByte("shake", (byte) field_9406_a);
        compound.putByte("inGround", (byte) (inGround ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound) {
        xTile = compound.getShort("xTile");
        yTile = compound.getShort("yTile");
        zTile = compound.getShort("zTile");
        field_9406_a = compound.getByte("shake") & 0xff;
        inGround = compound.getByte("inGround") == 1;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this
     * Entity.
     */
    public boolean canBeCollidedWith() {
        return true;
    }

    public float getCollisionBorderSize() {
        return 1.0F;
    }

    public static DamageSource causeLizardFireballDamage(DesertLizardFireballEntity creepsentitydesertlizardfireball,
            Entity entity) {
        return (new EntityDamageSource("lizardfireball", creepsentitydesertlizardfireball, entity)).setFireDamage()
                .setProjectile();
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();
        setBeenAttacked();

        if (entity != null)
        {
            Vec3d vec3d = entity.getLookVec();

            if (vec3d != null)
            {
                motionX = vec3d.x;
                motionY = vec3d.y;
                motionZ = vec3d.z;
                accelerationX = motionX * 0.10000000000000001D;
                accelerationY = motionY * 0.10000000000000001D;
                accelerationZ = motionZ * 0.10000000000000001D;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public float getShadowSize()
    {
        return 0.0F;
    }
}
