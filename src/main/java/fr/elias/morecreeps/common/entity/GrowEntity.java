package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class GrowEntity extends ItemEntity implements IProjectile
{
    public int hurtTime;
    public int maxHurtTime;
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
    protected float shrinksize;
    protected int vibrate;
    public PlayerEntity playerOwnTheProjectile;

    public GrowEntity(World world)
    {
        super(world);
        hitX = -1;
        hitY = -1;
        hitZ = -1;
        blockHit = Blocks.AIR;
        aoLightValueZPos = false;
        aoLightValueScratchXYNN = 0;
        setSize(0.0325F, 0.01125F);
        playerFire = false;
        shrinksize = 1.0F;
        vibrate = 1;
    }

    public GrowEntity(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        aoLightValueScratchXYZNNN = true;
    }

    public GrowEntity(World world, LivingEntity livingentity, float f)
    {
        this(world);
        playerOwnTheProjectile = (PlayerEntity) livingentity;
        lightValueOwn = livingentity;
        damage = 4;
        setLocationAndAngles(livingentity.posX, livingentity.posY + (double)livingentity.getEyeHeight(), livingentity.posZ, livingentity.rotationYaw, livingentity.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        posY += 0.20000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * 0.16F;

        if (livingentity instanceof PlayerEntity)
        {
            posY -= 0.40000000596046448D;
        }

        setPosition(posX, posY, posZ);
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionY = -MathHelper.sin((rotationPitch / 180F) * (float)Math.PI);
        float f1 = 1.0F;

        if (livingentity instanceof PlayerEntity)
        {
            playerFire = true;
            float f2 = 0.3333333F;
            float f3 = f2 / 0.1F;

            if (f3 > 0.0F)
            {
                f1 = (float)((double)f1 * (1.0D + 2D / (double)f3));
            }
        }

        if (Math.abs(livingentity.motionX) > 0.10000000000000001D || Math.abs(livingentity.motionY) > 0.10000000000000001D || Math.abs(livingentity.motionZ) > 0.10000000000000001D)
        {
            f1 *= 2.0F;
        }

        if (livingentity.onGround);

        setThrowableHeading(motionX, motionY, motionZ, (float)(2.5D + ((double)world.rand.nextFloat() - 0.5D)), f1);
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
    public void onUpdate(World world)
    {
        super.onUpdate();

        if (aoLightValueScratchXYNN == 5)
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
                motionZ *= rand.nextFloat() * 0.2F;
                aoLightValueScratchXYZNNP = 0;
                aoLightValueScratchXYNN = 0;
            }
            else
            {
                aoLightValueScratchXYZNNP++;

                if (aoLightValueScratchXYZNNP == 5)
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
            vec3d1 = new Vec3d(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);

            if (!entity1.canBeCollidedWith() || (entity1 == lightValueOwn || lightValueOwn != null && entity1 == lightValueOwn.ridingEntity) && aoLightValueScratchXYNN < 5 || aoLightValueScratchXYZNNN)
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

            double d2 = vec3d.distanceTo(movingobjectposition1.hitVec);

            if (d2 < d || d == 0.0D)
            {
                entity = entity1;
                d = d2;
            }
        }

        if (entity != null)
        {
            movingobjectposition = new RayTraceResult(entity);
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.hitInfo != null)
            {
                if (movingobjectposition.hitInfo instanceof LivingEntity)
                {
                    boolean flag = false;

                    if ((movingobjectposition.hitInfo instanceof KidEntity) && ((KidEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((KidEntity)movingobjectposition.hitInfo).modelsize += 0.15F;
                        ((KidEntity)movingobjectposition.hitInfo).setEntitySize(((KidEntity)movingobjectposition.hitInfo).modelsize, ((KidEntity)movingobjectposition.hitInfo).modelsize);
                    }

                    if ((movingobjectposition.hitInfo instanceof BigBabyEntity) && ((BigBabyEntity)movingobjectposition.hitInfo).modelsize < 8F)
                    {
                        ((BigBabyEntity)movingobjectposition.hitInfo).modelsize += 0.25F;
                    }

                    if ((movingobjectposition.hitInfo instanceof RatManEntity) && ((RatManEntity)movingobjectposition.hitInfo).modelsize < 3F)
                    {
                        ((RatManEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                        ((RatManEntity)movingobjectposition.hitInfo).modelspeed += 0.15F;
                    }

                    if ((movingobjectposition.hitInfo instanceof GuineaPigEntity) && ((GuineaPigEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((GuineaPigEntity)movingobjectposition.hitInfo).modelsize += 0.15F;
                    }

                    if ((movingobjectposition.hitInfo instanceof HotdogEntity) && ((HotdogEntity)movingobjectposition.hitInfo).dogsize < 5F)
                    {
                        ((HotdogEntity)movingobjectposition.hitInfo).dogsize += 0.15F;
                    }

                    if ((movingobjectposition.hitInfo instanceof RobotTedEntity) && ((RobotTedEntity)movingobjectposition.hitInfo).robotsize < 6F)
                    {
                        ((RobotTedEntity)movingobjectposition.hitInfo).robotsize += 0.25F;
                        ((RobotTedEntity)movingobjectposition.hitInfo).modelspeed += 0.15F;
                    }

                    if ((movingobjectposition.hitInfo instanceof RobotToddEntity) && ((RobotToddEntity)movingobjectposition.hitInfo).robotsize < 4F)
                    {
                        ((RobotToddEntity)movingobjectposition.hitInfo).robotsize += 0.25F;
                        ((RobotToddEntity)movingobjectposition.hitInfo).modelspeed += 0.05F;
                    }

                    if ((movingobjectposition.hitInfo instanceof GooGoatEntity) && ((GooGoatEntity)movingobjectposition.hitInfo).goatsize < 4F)
                    {
                        ((GooGoatEntity)movingobjectposition.hitInfo).goatsize += 0.24F;
                        ((GooGoatEntity)movingobjectposition.hitInfo).modelspeed += 0.15F;
                    }

                    if ((movingobjectposition.hitInfo instanceof LollimanEntity) && ((LollimanEntity)movingobjectposition.hitInfo).modelsize < 6F)
                    {
                        ((LollimanEntity)movingobjectposition.hitInfo).modelsize += 0.24F;
                    }

                    if ((movingobjectposition.hitInfo instanceof CastleCritterEntity) && ((CastleCritterEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((CastleCritterEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof SneakySalEntity) && ((SneakySalEntity)movingobjectposition.hitInfo).modelsize < 6F)
                    {
                        ((SneakySalEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                        ((SneakySalEntity)movingobjectposition.hitInfo).dissedmax = 0;
                    }

                    if ((movingobjectposition.hitInfo instanceof ArmyGuyEntity) && ((ArmyGuyEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((ArmyGuyEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof EvilChickenEntity) && ((EvilChickenEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((EvilChickenEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                        ((EvilChickenEntity)movingobjectposition.hitInfo).getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D + 0.015D);
                    }

                    if ((movingobjectposition.hitInfo instanceof BumEntity) && ((BumEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((BumEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof BubbleScumEntity) && ((BubbleScumEntity)movingobjectposition.hitInfo).modelsize < 3F)
                    {
                        ((BubbleScumEntity)movingobjectposition.hitInfo).modelsize += 0.15F;
                    }

                    if ((movingobjectposition.hitInfo instanceof LawyerFromHellEntity) && ((LawyerFromHellEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((LawyerFromHellEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                        MoreCreepsReboot.instance.currentfine += 50;
                    }

                    if ((movingobjectposition.hitInfo instanceof LetterGEntity) && ((LetterGEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((LetterGEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof RockMonsterEntity) && ((RockMonsterEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((RockMonsterEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof BabyMummyEntity) && ((BabyMummyEntity)movingobjectposition.hitInfo).babysize < 4F)
                    {
                        ((BabyMummyEntity)movingobjectposition.hitInfo).babysize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof BlackSoulEntity) && ((BlackSoulEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((BlackSoulEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof BlorpEntity) && ((BlorpEntity)movingobjectposition.hitInfo).blorpsize < (float)CREEPSConfig.sblorpmaxsize)
                    {
                        ((BlorpEntity)movingobjectposition.hitInfo).blorpsize += 0.25F;
                        setSize(((BlorpEntity)movingobjectposition.hitInfo).blorpsize + 1.0F, ((BlorpEntity)movingobjectposition.hitInfo).blorpsize * 2.0F + 2.0F);
                    }

                    if ((movingobjectposition.hitInfo instanceof CamelEntity) && ((CamelEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((CamelEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof CamelJockeyEntity) && ((CamelJockeyEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((CamelJockeyEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof CastleGuardEntity) && ((CastleGuardEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((CastleGuardEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof CavemanEntity) && ((CavemanEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((CavemanEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof DesertLizardEntity) && ((DesertLizardEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((DesertLizardEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof DigBugEntity) && ((DigBugEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((DigBugEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof EvilCreatureEntity) && ((EvilCreatureEntity)movingobjectposition.hitInfo).modelsize < 7F)
                    {
                        ((EvilCreatureEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof EvilPigEntity) && ((EvilPigEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((EvilPigEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof EvilScientistEntity) && ((EvilScientistEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((EvilScientistEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof EvilSnowmanEntity) && ((EvilSnowmanEntity)movingobjectposition.hitInfo).snowsize < 7F)
                    {
                        ((EvilSnowmanEntity)movingobjectposition.hitInfo).snowsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof FloobEntity) && ((FloobEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((FloobEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof HippoEntity) && ((HippoEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((HippoEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof HunchbackEntity) && ((HunchbackEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((HunchbackEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof HunchbackSkeletonEntity) && ((HunchbackSkeletonEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((HunchbackSkeletonEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof InvisibleManEntity) && ((InvisibleManEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((InvisibleManEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof ManDogEntity) && ((ManDogEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((ManDogEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof NonSwimmerEntity) && ((NonSwimmerEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((NonSwimmerEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof RocketGiraffeEntity) && ((RocketGiraffeEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((RocketGiraffeEntity)movingobjectposition.hitInfo).modelsize += 0.15F;
                    }

                    if ((movingobjectposition.hitInfo instanceof SnowDevilEntity) && ((SnowDevilEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((SnowDevilEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof ThiefEntity) && ((ThiefEntity)movingobjectposition.hitInfo).modelsize < 4F)
                    {
                        ((ThiefEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if ((movingobjectposition.hitInfo instanceof ZebraEntity) && ((ZebraEntity)movingobjectposition.hitInfo).modelsize < 5F)
                    {
                        ((ZebraEntity)movingobjectposition.hitInfo).modelsize += 0.2F;
                    }

                    if (flag)
                    {
                        smoke();
                        world.playSound(this, this.getPosition(), SoundsHandler.SHRINK_KILL, SoundCategory.NEUTRAL, 1.0F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
                        setDead();
                    }
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
                motionX = (float)(movingobjectposition.hitVec.xCoord - posX);
                motionY = (float)(movingobjectposition.hitVec.yCoord - posY);
                motionZ = (float)(movingobjectposition.hitVec.zCoord - posZ);
                float f1 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                posX -= (motionX / (double)f1) * 0.05000000074505806D;
                posY -= (motionY / (double)f1) * 0.05000000074505806D;
                posZ -= (motionZ / (double)f1) * 0.05000000074505806D;
                aoLightValueZPos = true;
                setDead();

                if (blockHit == Blocks.ice)
                {
                    world.setBlockState(new BlockPos(hitX, hitY, hitZ), Blocks.WATER.getDefaultState());
                }

                setDead();
            }

            world.playSound(this, this.getPosition(), SoundsHandler.RAY_GUN, SoundCategory.NEUTRAL, 0.2F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
            setDead();
        }

        
        if(world.isRemote)
        	MoreCreepsReboot.proxy.growParticle(world, this);

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
        playerOwnTheProjectile.posY += rand.nextGaussian() * 0.34999999999999998D * (double)vibrate;
        vibrate *= -1;
        double d1 = -MathHelper.sin((playerOwnTheProjectile.rotationYaw * (float)Math.PI) / 180F);
        double d3 = MathHelper.cos((playerOwnTheProjectile.rotationYaw * (float)Math.PI) / 180F);
        playerOwnTheProjectile.posX += (double)((float)vibrate * 0.25F) * d1;
        playerOwnTheProjectile.posZ += (double)((float)vibrate * 0.25F) * d3;

        if (handleWaterMovement())
        {
            for (int l = 0; l < 4; l++)
            {
                float f7 = 0.25F;
                world.spawnParticle(ParticleTypes.BUBBLE, posX - motionX * (double)f7, posY - motionY * (double)f7, posZ - motionZ * (double)f7, motionX, motionY, motionZ);
            }

            f3 = 0.8F;
            float f6 = 0.03F;
            setDead();
        }

        motionX *= f3;
        motionZ *= f3;
        setPosition(posX, posY, posZ);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)hitX);
        nbttagcompound.setShort("yTile", (short)hitY);
        nbttagcompound.setShort("zTile", (short)hitZ);
        nbttagcompound.setByte("inGround", (byte)(aoLightValueZPos ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        hitX = nbttagcompound.getShort("xTile");
        hitY = nbttagcompound.getShort("yTile");
        hitZ = nbttagcompound.getShort("zTile");
        aoLightValueZPos = nbttagcompound.getByte("inGround") == 1;
    }

    public void blast(World world)
    {
    	if(world.isRemote)
    		MoreCreepsReboot.proxy.shrinkParticle(world, this);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        blast();
        super.setDead();
        lightValueOwn = null;
    }

    private void smoke()
    {
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                for (int k = 0; k < 5; k++)
                {
                    double d = rand.nextGaussian() * 0.12D;
                    double d1 = rand.nextGaussian() * 0.12D;
                    double d2 = rand.nextGaussian() * 0.12D;
                    world.spawnParticle(ParticleTypes.EXPLOSION_NORMAL, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
                }
            }
        }
    }
    public ItemStack getEntityItem()
    {
    	return new ItemStack(MoreCreepsReboot.shrinkshrink, 1, 0);
    }
}
