package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsReboot;

public class ShrinkEntity extends EntityThrowable
{
    protected int hitX;
    protected int hitY;
    protected int hitZ;
    protected IBlockState blockHit;

    /** Light value one block more in z axis */
    protected boolean aoLightValueZPos;

    /** Light value of the block itself */
    public EntityLivingBase lightValueOwn;

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

    public ShrinkEntity(World world)
    {
        super(world);
        hitX = -1;
        hitY = -1;
        hitZ = -1;
        blockHit = null;
        aoLightValueZPos = false;
        aoLightValueScratchXYNN = 0;
        setSize(0.0325F, 0.01125F);
        playerFire = false;
        shrinksize = 1.0F;
        vibrate = 1;
    }

    public ShrinkEntity(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
        aoLightValueScratchXYZNNN = true;
    }

    public ShrinkEntity(World world, EntityLivingBase entityliving, float f)
    {
        this(world);
        lightValueOwn = entityliving;
        damage = 4;
        setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * 0.16F;
        posY += 0.20000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * 0.16F;

        if (entityliving instanceof EntityPlayer)
        {
            posY -= 0.40000000596046448D;
        }

        setPosition(posX, posY, posZ);
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
        motionY = -MathHelper.sin((rotationPitch / 180F) * (float)Math.PI);
        float f1 = 1.0F;

        if (entityliving instanceof EntityPlayer)
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

        func_22374_a(motionX, motionY, motionZ, (float)(2.5D + ((double)world.rand.nextFloat() - 0.5D)), f1);
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityplayer)
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

        Vec3 vec3d = new Vec3(posX, posY, posZ);
        Vec3 vec3d1 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition movingobjectposition = world.rayTraceBlocks(vec3d, vec3d1);
        vec3d = new Vec3(posX, posY, posZ);
        vec3d1 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);

        if (movingobjectposition != null)
        {
            vec3d1 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
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
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f4, f4, f4);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

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
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.entityHit != null)
            {
                if (movingobjectposition.entityHit instanceof EntityLiving)
                {
                    boolean flag = false;

                    if (movingobjectposition.entityHit instanceof KidEntity)
                    {
                        if (((KidEntity)movingobjectposition.entityHit).modelsize > 0.25F)
                        {
                            ((KidEntity)movingobjectposition.entityHit).modelsize -= 0.15F;
                        }
                        else
                        {
                            ((KidEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof BigBabyEntity)
                    {
                        if (((BigBabyEntity)movingobjectposition.entityHit).modelsize > 0.5F)
                        {
                            ((BigBabyEntity)movingobjectposition.entityHit).modelsize -= 0.25F;
                        }
                        else
                        {
                            ((BigBabyEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof RatManEntity)
                    {
                        if (((RatManEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((RatManEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                            ((RatManEntity)movingobjectposition.entityHit).modelspeed -= 0.15F;
                        }
                        else
                        {
                            ((RatManEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof GuineaPigEntity)
                    {
                        if (((GuineaPigEntity)movingobjectposition.entityHit).modelsize > 0.35F)
                        {
                            ((GuineaPigEntity)movingobjectposition.entityHit).modelsize -= 0.15F;
                        }
                        else
                        {
                            ((GuineaPigEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof HotdogEntity)
                    {
                        if (((HotdogEntity)movingobjectposition.entityHit).dogsize > 0.35F)
                        {
                            ((HotdogEntity)movingobjectposition.entityHit).dogsize -= 0.15F;
                        }
                        else
                        {
                            ((HotdogEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof RobotTedEntity)
                    {
                        if (((RobotTedEntity)movingobjectposition.entityHit).robotsize > 0.6F)
                        {
                            ((RobotTedEntity)movingobjectposition.entityHit).robotsize -= 0.5F;
                            ((RobotTedEntity)movingobjectposition.entityHit).modelspeed -= 0.25F;
                        }
                        else
                        {
                            ((RobotTedEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof RobotToddEntity)
                    {
                        if (((RobotToddEntity)movingobjectposition.entityHit).robotsize > 0.4F)
                        {
                            ((RobotToddEntity)movingobjectposition.entityHit).robotsize -= 0.4F;
                            ((RobotToddEntity)movingobjectposition.entityHit).modelspeed -= 0.15F;
                        }
                        else
                        {
                            ((RobotToddEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof EntityGooGoat)
                    {
                        if (((EntityGooGoat)movingobjectposition.entityHit).goatsize > 0.4F)
                        {
                            ((EntityGooGoat)movingobjectposition.entityHit).goatsize -= 0.4F;
                            ((EntityGooGoat)movingobjectposition.entityHit).modelspeed -= 0.15F;
                        }
                        else
                        {
                            ((EntityGooGoat)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof LollimanEntity)
                    {
                        if (((LollimanEntity)movingobjectposition.entityHit).modelsize > 0.6F)
                        {
                            ((LollimanEntity)movingobjectposition.entityHit).modelsize -= 0.4F;
                        }
                        else
                        {
                            ((LollimanEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CastleCritterEntity)
                    {
                        if (((CastleCritterEntity)movingobjectposition.entityHit).modelsize > 0.4F)
                        {
                            ((CastleCritterEntity)movingobjectposition.entityHit).modelsize -= 0.3F;
                        }
                        else
                        {
                            ((CastleCritterEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof SneakySalEntity)
                    {
                        if (((SneakySalEntity)movingobjectposition.entityHit).modelsize > 0.6F)
                        {
                            ((SneakySalEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                            ((SneakySalEntity)movingobjectposition.entityHit).dissedmax = 0;

                            if (rand.nextInt(2) == 0)
                            {
                                world.playSoundAtEntity(this, "morecreeps:salnobodyshrinks", 0.5F, 1.0F);
                            }
                        }
                        else
                        {
                            ((SneakySalEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof ArmyGuyEntity)
                    {
                        if (((ArmyGuyEntity)movingobjectposition.entityHit).modelsize > 0.4F)
                        {
                            ((ArmyGuyEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((ArmyGuyEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof EvilChickenEntity)
                    {
                        if (((EvilChickenEntity)movingobjectposition.entityHit).modelsize > 0.5F)
                        {
                            ((EvilChickenEntity)movingobjectposition.entityHit).modelsize -= 0.4F;
                            ((EvilChickenEntity)movingobjectposition.entityHit).getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.015D);
                        }
                        else
                        {
                            ((EvilChickenEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof BumEntity)
                    {
                        if (((BumEntity)movingobjectposition.entityHit).modelsize > 0.4F)
                        {
                            ((BumEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((BumEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof BubbleScumEntity)
                    {
                        if (((BubbleScumEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((BubbleScumEntity)movingobjectposition.entityHit).modelsize -= 0.15F;
                        }
                        else
                        {
                            ((BubbleScumEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof LawyerFromHellEntity)
                    {
                        if (((LawyerFromHellEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((LawyerFromHellEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                            MoreCreepsReboot.instance.currentfine += 50;
                        }
                        else
                        {
                            ((LawyerFromHellEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof LetterGEntity)
                    {
                        if (((LetterGEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((LetterGEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((LetterGEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof RockMonsterEntity)
                    {
                        if (((RockMonsterEntity)movingobjectposition.entityHit).modelsize > 0.4F)
                        {
                            ((RockMonsterEntity)movingobjectposition.entityHit).modelsize -= 0.4F;
                        }
                        else
                        {
                            ((RockMonsterEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof BabyMummyEntity)
                    {
                        if (((BabyMummyEntity)movingobjectposition.entityHit).babysize > 0.2F)
                        {
                            ((BabyMummyEntity)movingobjectposition.entityHit).babysize -= 0.2F;
                        }
                        else
                        {
                            ((BabyMummyEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof BlackSoulEntity)
                    {
                        if (((BlackSoulEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((BlackSoulEntity)movingobjectposition.entityHit).modelsize -= 0.3F;
                        }
                        else
                        {
                            ((BlackSoulEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof BlorpEntity)
                    {
                        if (((BlorpEntity)movingobjectposition.entityHit).blorpsize > 0.3F)
                        {
                            ((BlorpEntity)movingobjectposition.entityHit).blorpsize -= 0.3F;
                        }
                        else
                        {
                            ((BlorpEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CamelEntity)
                    {
                        if (((CamelEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((CamelEntity)movingobjectposition.entityHit).modelsize -= 0.3F;
                        }
                        else
                        {
                            ((CamelEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CamelJockeyEntity)
                    {
                        if (((CamelJockeyEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((CamelJockeyEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((CamelJockeyEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CastleGuardEntity)
                    {
                        if (((CastleGuardEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((CastleGuardEntity)movingobjectposition.entityHit).modelsize -= 0.3F;
                        }
                        else
                        {
                            ((CastleGuardEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CavemanEntity)
                    {
                        if (((CavemanEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((CavemanEntity)movingobjectposition.entityHit).modelsize -= 0.3F;
                        }
                        else
                        {
                            ((CavemanEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof DesertLizardEntity)
                    {
                        if (((DesertLizardEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((DesertLizardEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((DesertLizardEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof DigBugEntity)
                    {
                        if (((DigBugEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((DigBugEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((DigBugEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof EvilCreatureEntity)
                    {
                        if (((EvilCreatureEntity)movingobjectposition.entityHit).modelsize > 0.5F)
                        {
                            ((EvilCreatureEntity)movingobjectposition.entityHit).modelsize -= 0.4F;
                        }
                        else
                        {
                            ((EvilCreatureEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof EvilPigEntity)
                    {
                        if (((EvilPigEntity)movingobjectposition.entityHit).modelsize > 0.4F)
                        {
                            ((EvilPigEntity)movingobjectposition.entityHit).modelsize -= 0.3F;
                        }
                        else
                        {
                            ((EvilPigEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof EvilScientistEntity)
                    {
                        if (((EvilScientistEntity)movingobjectposition.entityHit).modelsize > 0.4F)
                        {
                            ((EvilScientistEntity)movingobjectposition.entityHit).modelsize -= 0.3F;
                        }
                        else
                        {
                            ((EvilScientistEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof EvilSnowmanEntity)
                    {
                        if (((EvilSnowmanEntity)movingobjectposition.entityHit).snowsize > 0.3F)
                        {
                            ((EvilSnowmanEntity)movingobjectposition.entityHit).snowsize -= 0.2F;
                        }
                        else
                        {
                            ((EvilSnowmanEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof FloobEntity)
                    {
                        if (((FloobEntity)movingobjectposition.entityHit).modelsize > 0.4F)
                        {
                            ((FloobEntity)movingobjectposition.entityHit).modelsize -= 0.4F;
                        }
                        else
                        {
                            ((FloobEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof HippoEntity)
                    {
                        if (((HippoEntity)movingobjectposition.entityHit).modelsize > 0.4F)
                        {
                            ((HippoEntity)movingobjectposition.entityHit).modelsize -= 0.3F;
                        }
                        else
                        {
                            ((HippoEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof HunchbackEntity)
                    {
                        if (((HunchbackEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((HunchbackEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((HunchbackEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof HunchbackSkeletonEntity)
                    {
                        if (((HunchbackSkeletonEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((HunchbackSkeletonEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((HunchbackSkeletonEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof InvisibleManEntity)
                    {
                        if (((InvisibleManEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((InvisibleManEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((InvisibleManEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof ManDogEntity)
                    {
                        if (((ManDogEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((ManDogEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((ManDogEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof NonSwimmerEntity)
                    {
                        if (((NonSwimmerEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((NonSwimmerEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((NonSwimmerEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof RocketGiraffeEntity)
                    {
                        if (((RocketGiraffeEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((RocketGiraffeEntity)movingobjectposition.entityHit).modelsize -= 0.15F;
                        }
                        else
                        {
                            ((RocketGiraffeEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof SnowDevilEntity)
                    {
                        if (((SnowDevilEntity)movingobjectposition.entityHit).modelsize > 0.4F)
                        {
                            ((SnowDevilEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((SnowDevilEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof ThiefEntity)
                    {
                        if (((ThiefEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((ThiefEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((ThiefEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof ZebraEntity)
                    {
                        if (((ZebraEntity)movingobjectposition.entityHit).modelsize > 0.3F)
                        {
                            ((ZebraEntity)movingobjectposition.entityHit).modelsize -= 0.2F;
                        }
                        else
                        {
                            ((ZebraEntity)movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (flag)
                    {
                        smoke();
                        world.playSoundAtEntity(this, "morecreeps:shrinkkill", 1.0F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
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
                blockHit = world.getBlockState(new BlockPos(hitX, hitY, hitZ));
                motionX = (float)(movingobjectposition.hitVec.xCoord - posX);
                motionY = (float)(movingobjectposition.hitVec.yCoord - posY);
                motionZ = (float)(movingobjectposition.hitVec.zCoord - posZ);
                float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                posX -= (motionX / (double)f1) * 0.05000000074505806D;
                posY -= (motionY / (double)f1) * 0.05000000074505806D;
                posZ -= (motionZ / (double)f1) * 0.05000000074505806D;
                aoLightValueZPos = true;
                setDead();

                if (blockHit.getBlock() == Blocks.ice)
                {
                    world.setBlockState(new BlockPos(hitX, hitY, hitZ), Blocks.flowing_water.getDefaultState());
                }

                setDead();
            }

            world.playSoundAtEntity(this, "morecreeps:raygun", 0.2F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
            setDead();
        }

        if(world.isRemote)
        	MoreCreepsReboot.proxy.shrinkSmoke(world, this);

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
        //this.vibratePlayer((EntityPlayer)this.getThrower());
        if (handleWaterMovement())
        {
            for (int l = 0; l < 4; l++)
            {
                float f7 = 0.25F;
                world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * (double)f7, posY - motionY * (double)f7, posZ - motionZ * (double)f7, motionX, motionY, motionZ);
            }

            f3 = 0.8F;
            float f6 = 0.03F;
            setDead();
        }

        motionX *= f3;
        motionZ *= f3;
        setPosition(posX, posY, posZ);
    }
    public void vibratePlayer(EntityPlayer player)
    {
    	player.posY += rand.nextGaussian() * 0.34999999999999998D * (double)vibrate;
        vibrate *= -1;
        double d1 = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d3 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        player.posX += (double)((float)vibrate * 0.25F) * d1;
        player.posZ += (double)((float)vibrate * 0.25F) * d3;
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

    public float getShadowSize()
    {
        return 0.0F;
    }

    public void blast(World world)
    {
    	if(world.isRemote)
    	MoreCreepsReboot.proxy.shrinkBlast(world, this, rand);
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
                    world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2, new int[0]);
                }
            }
        }
    }

	@Override
	protected void onImpact(MovingObjectPosition p_70184_1_) {
		// TODO Auto-generated method stub
		
	}
}
