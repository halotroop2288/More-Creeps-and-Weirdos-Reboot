package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ShrinkEntity extends ThrowableEntity {
    protected double hitX;
    protected double hitY;
    protected double hitZ;
    protected BlockState blockHit;
    public double motionX = getMotion().x;
    public double motionY = getMotion().y;
    public double motionZ = getMotion().z;

    /** Light value one block more in z axis */
    protected boolean aoLightValueZPos;

    /** Light value of the block itself */
    public LivingEntity lightValueOwn;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/west
     * corner.
     */
    protected int aoLightValueScratchXYZNNP;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and
     * the north face.
     */
    protected int aoLightValueScratchXYNN;
    protected int damage;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/east
     * corner.
     */
    protected boolean aoLightValueScratchXYZNNN;
    protected boolean playerFire;
    protected float shrinksize;
    protected int vibrate;

    public ShrinkEntity(World world) {
        super(null, world);
        hitX = -1;
        hitY = -1;
        hitZ = -1;
        blockHit = null;
        aoLightValueZPos = false;
        aoLightValueScratchXYNN = 0;
        // setSize(0.0325F, 0.01125F);
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

    public ShrinkEntity(World world, LivingEntity livingentity, float f)
    {
        this(world);
        lightValueOwn = livingentity;
        damage = 4;
        setLocationAndAngles(livingentity.posX, livingentity.posY + (double) livingentity.getEyeHeight(),
                livingentity.posZ, livingentity.rotationYaw, livingentity.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * (float) Math.PI) * 0.16F;
        posY += 0.20000000149011612D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * (float) Math.PI) * 0.16F;

        if (livingentity instanceof PlayerEntity) {
            posY -= 0.40000000596046448D;
        }

        setPosition(posX, posY, posZ);
        motionX = -MathHelper.sin((rotationYaw / 180F) * (float) Math.PI)
                * MathHelper.cos((rotationPitch / 180F) * (float) Math.PI);
        motionZ = MathHelper.cos((rotationYaw / 180F) * (float) Math.PI)
                * MathHelper.cos((rotationPitch / 180F) * (float) Math.PI);
        motionY = -MathHelper.sin((rotationPitch / 180F) * (float) Math.PI);
        float f1 = 1.0F;

        if (livingentity instanceof PlayerEntity) {
            playerFire = true;
            float f2 = 0.3333333F;
            float f3 = f2 / 0.1F;

            if (f3 > 0.0F) {
                f1 = (float) ((double) f1 * (1.0D + 2D / (double) f3));
            }
        }

        if (Math.abs(livingentity.getMotion().x) > 0.10000000000000001D || Math.abs(livingentity.getMotion().y) > 0.10000000000000001D || Math.abs(livingentity.getMotion().z) > 0.10000000000000001D)
        {
            f1 *= 2.0F;
        }
        
        func_22374_a(motionX, motionY, motionZ, (float) (2.5D + ((double) world.rand.nextFloat() - 0.5D)), f1);
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(PlayerEntity playerentity) {
    }

    protected void entityInit() {
    }

    public void func_22374_a(double d, double d1, double d2, float f, float f1)
    {
        float f2 = MathHelper.sqrt(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += rand.nextGaussian() * 0.0074999998323619366D * (double) f1;
        d1 += rand.nextGaussian() * 0.0074999998323619366D * (double) f1;
        d2 += rand.nextGaussian() * 0.0074999998323619366D * (double) f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        motionX = d;
        motionY = d1;
        motionZ = d2;
        float f3 = MathHelper.sqrt(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float) ((Math.atan2(d1, f3) * 180D) / Math.PI);
        aoLightValueScratchXYZNNP = 0;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and
     * comparing it to its average edge length * 64 * renderDistanceWeight Args:
     * distance
     */
    public boolean isInRangeToRenderDist(double d) {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
        PlayerEntity player = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;
        super.tick();

        if (aoLightValueScratchXYNN == 5) {
            setDead();
        }

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float) ((Math.atan2(motionY, f) * 180D) / Math.PI);
        }

        if (aoLightValueZPos) {
            BlockState i = world.getBlockState(new BlockPos(hitX, hitY, hitZ)).getBlockState();

            if (i != blockHit) {
                aoLightValueZPos = false;
                motionX *= rand.nextFloat() * 0.2F;
                motionZ *= rand.nextFloat() * 0.2F;
                aoLightValueScratchXYZNNP = 0;
                aoLightValueScratchXYNN = 0;
            } else {
                aoLightValueScratchXYZNNP++;

                if (aoLightValueScratchXYZNNP == 5) {
                    setDead();
                }

                return;
            }
        } else {
            aoLightValueScratchXYNN++;
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
        @SuppressWarnings("rawtypes")
        List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);

            if (!entity1.canBeCollidedWith()
                    || (entity1 == lightValueOwn || lightValueOwn != null && entity1 == lightValueOwn.getRidingEntity())
                            && aoLightValueScratchXYNN < 5
                    || aoLightValueScratchXYZNNN) {
                if (motionZ != 0.0D || !((motionX == 0.0D) & (motionY == 0.0D)))
                {
                    continue;
                }

                setDead();
                break;
            }

            float f4 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox().expand(f4, f4, f4);
            // RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
            RayTraceResult movingobjectposition1 = null;

            if (movingobjectposition1 == null) {
                continue;
            }

            double d2 = vec3d.distanceTo(movingobjectposition1.getHitVec());

            if (d2 < d || d == 0.0D) {
                entity = entity1;
                d = d2;
            }
        }

        if (entity != null) {
            movingobjectposition = new RayTraceResult(entity);
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.hitInfo != null) {
                if (movingobjectposition.hitInfo instanceof Entity) {
                    boolean flag = false;

                    if (movingobjectposition.hitInfo instanceof KidEntity) {
                        if (((KidEntity) movingobjectposition.hitInfo).modelsize > 0.25F) {
                            ((KidEntity) movingobjectposition.hitInfo).modelsize -= 0.15F;
                        } else {
                            ((KidEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof BigBabyEntity) {
                        if (((BigBabyEntity) movingobjectposition.hitInfo).modelsize > 0.5F) {
                            ((BigBabyEntity) movingobjectposition.hitInfo).modelsize -= 0.25F;
                        } else {
                            ((BigBabyEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof RatManEntity) {
                        if (((RatManEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((RatManEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                            ((RatManEntity) movingobjectposition.hitInfo).modelspeed -= 0.15F;
                        } else {
                            ((RatManEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof GuineaPigEntity) {
                        if (((GuineaPigEntity) movingobjectposition.hitInfo).modelsize > 0.35F) {
                            ((GuineaPigEntity) movingobjectposition.hitInfo).modelsize -= 0.15F;
                        } else {
                            ((GuineaPigEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof HotdogEntity) {
                        if (((HotdogEntity) movingobjectposition.hitInfo).dogsize > 0.35F) {
                            ((HotdogEntity) movingobjectposition.hitInfo).dogsize -= 0.15F;
                        } else {
                            ((HotdogEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof RobotTedEntity) {
                        if (((RobotTedEntity) movingobjectposition.hitInfo).robotsize > 0.6F) {
                            ((RobotTedEntity) movingobjectposition.hitInfo).robotsize -= 0.5F;
                            ((RobotTedEntity) movingobjectposition.hitInfo).modelspeed -= 0.25F;
                        } else {
                            ((RobotTedEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof RobotToddEntity) {
                        if (((RobotToddEntity) movingobjectposition.hitInfo).robotsize > 0.4F) {
                            ((RobotToddEntity) movingobjectposition.hitInfo).robotsize -= 0.4F;
                            ((RobotToddEntity) movingobjectposition.hitInfo).modelspeed -= 0.15F;
                        } else {
                            ((RobotToddEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof GooGoatEntity) {
                        if (((GooGoatEntity) movingobjectposition.hitInfo).goatsize > 0.4F) {
                            ((GooGoatEntity) movingobjectposition.hitInfo).goatsize -= 0.4F;
                            ((GooGoatEntity) movingobjectposition.hitInfo).modelspeed -= 0.15F;
                        } else {
                            ((GooGoatEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof LollimanEntity) {
                        if (((LollimanEntity) movingobjectposition.hitInfo).modelsize > 0.6F) {
                            ((LollimanEntity) movingobjectposition.hitInfo).modelsize -= 0.4F;
                        } else {
                            ((LollimanEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof CastleCritterEntity) {
                        if (((CastleCritterEntity) movingobjectposition.hitInfo).modelsize > 0.4F) {
                            ((CastleCritterEntity) movingobjectposition.hitInfo).modelsize -= 0.3F;
                        } else {
                            ((CastleCritterEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof SneakySalEntity) {
                        if (((SneakySalEntity) movingobjectposition.hitInfo).modelsize > 0.6F) {
                            ((SneakySalEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                            ((SneakySalEntity) movingobjectposition.hitInfo).dissedmax = 0;

                            if (rand.nextInt(2) == 0) {
                                world.playSound(player, this.getPosition(), SoundsHandler.SAL_NOBODY_SHRINKS, SoundCategory.NEUTRAL, 0.5F, 1.0F);
                            }
                        } else {
                            ((SneakySalEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof ArmyGuyEntity) {
                        if (((ArmyGuyEntity) movingobjectposition.hitInfo).modelsize > 0.4F) {
                            ((ArmyGuyEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((ArmyGuyEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof EvilChickenEntity) {
                        if (((EvilChickenEntity) movingobjectposition.hitInfo).modelsize > 0.5F) {
                            ((EvilChickenEntity) movingobjectposition.hitInfo).modelsize -= 0.4F;
                            ((EvilChickenEntity) movingobjectposition.hitInfo)
                                    .getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.015D);
                        } else {
                            ((EvilChickenEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof BumEntity) {
                        if (((BumEntity) movingobjectposition.hitInfo).modelsize > 0.4F) {
                            ((BumEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((BumEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof BubbleScumEntity) {
                        if (((BubbleScumEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((BubbleScumEntity) movingobjectposition.hitInfo).modelsize -= 0.15F;
                        } else {
                            ((BubbleScumEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof LawyerFromHellEntity) {
                        if (((LawyerFromHellEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((LawyerFromHellEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                            MoreCreepsReboot.instance.currentfine += 50;
                        } else {
                            ((LawyerFromHellEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof LetterGEntity) {
                        if (((LetterGEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((LetterGEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((LetterGEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof RockMonsterEntity) {
                        if (((RockMonsterEntity) movingobjectposition.hitInfo).modelsize > 0.4F) {
                            ((RockMonsterEntity) movingobjectposition.hitInfo).modelsize -= 0.4F;
                        } else {
                            ((RockMonsterEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof BabyMummyEntity) {
                        if (((BabyMummyEntity) movingobjectposition.hitInfo).babysize > 0.2F) {
                            ((BabyMummyEntity) movingobjectposition.hitInfo).babysize -= 0.2F;
                        } else {
                            ((BabyMummyEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof BlackSoulEntity) {
                        if (((BlackSoulEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((BlackSoulEntity) movingobjectposition.hitInfo).modelsize -= 0.3F;
                        } else {
                            ((BlackSoulEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof BlorpEntity) {
                        if (((BlorpEntity) movingobjectposition.hitInfo).blorpsize > 0.3F) {
                            ((BlorpEntity) movingobjectposition.hitInfo).blorpsize -= 0.3F;
                        } else {
                            ((BlorpEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof CamelEntity) {
                        if (((CamelEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((CamelEntity) movingobjectposition.hitInfo).modelsize -= 0.3F;
                        } else {
                            ((CamelEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof CamelJockeyEntity) {
                        if (((CamelJockeyEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((CamelJockeyEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((CamelJockeyEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof CastleGuardEntity) {
                        if (((CastleGuardEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((CastleGuardEntity) movingobjectposition.hitInfo).modelsize -= 0.3F;
                        } else {
                            ((CastleGuardEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof CavemanEntity) {
                        if (((CavemanEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((CavemanEntity) movingobjectposition.hitInfo).modelsize -= 0.3F;
                        } else {
                            ((CavemanEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof DesertLizardEntity) {
                        if (((DesertLizardEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((DesertLizardEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((DesertLizardEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof DigBugEntity) {
                        if (((DigBugEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((DigBugEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((DigBugEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof EvilCreatureEntity) {
                        if (((EvilCreatureEntity) movingobjectposition.hitInfo).modelsize > 0.5F) {
                            ((EvilCreatureEntity) movingobjectposition.hitInfo).modelsize -= 0.4F;
                        } else {
                            ((EvilCreatureEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof EvilPigEntity) {
                        if (((EvilPigEntity) movingobjectposition.hitInfo).modelsize > 0.4F) {
                            ((EvilPigEntity) movingobjectposition.hitInfo).modelsize -= 0.3F;
                        } else {
                            ((EvilPigEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof EvilScientistEntity) {
                        if (((EvilScientistEntity) movingobjectposition.hitInfo).modelsize > 0.4F) {
                            ((EvilScientistEntity) movingobjectposition.hitInfo).modelsize -= 0.3F;
                        } else {
                            ((EvilScientistEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof EvilSnowmanEntity) {
                        if (((EvilSnowmanEntity) movingobjectposition.hitInfo).snowsize > 0.3F) {
                            ((EvilSnowmanEntity) movingobjectposition.hitInfo).snowsize -= 0.2F;
                        } else {
                            ((EvilSnowmanEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof FloobEntity) {
                        if (((FloobEntity) movingobjectposition.hitInfo).modelsize > 0.4F) {
                            ((FloobEntity) movingobjectposition.hitInfo).modelsize -= 0.4F;
                        } else {
                            ((FloobEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof HippoEntity) {
                        if (((HippoEntity) movingobjectposition.hitInfo).modelsize > 0.4F) {
                            ((HippoEntity) movingobjectposition.hitInfo).modelsize -= 0.3F;
                        } else {
                            ((HippoEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof HunchbackEntity) {
                        if (((HunchbackEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((HunchbackEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((HunchbackEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof HunchbackSkeletonEntity) {
                        if (((HunchbackSkeletonEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((HunchbackSkeletonEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((HunchbackSkeletonEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof InvisibleManEntity) {
                        if (((InvisibleManEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((InvisibleManEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((InvisibleManEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof ManDogEntity) {
                        if (((ManDogEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((ManDogEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((ManDogEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof NonSwimmerEntity) {
                        if (((NonSwimmerEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((NonSwimmerEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((NonSwimmerEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof RocketGiraffeEntity) {
                        if (((RocketGiraffeEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((RocketGiraffeEntity) movingobjectposition.hitInfo).modelsize -= 0.15F;
                        } else {
                            ((RocketGiraffeEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof SnowDevilEntity) {
                        if (((SnowDevilEntity) movingobjectposition.hitInfo).modelsize > 0.4F) {
                            ((SnowDevilEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((SnowDevilEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof ThiefEntity) {
                        if (((ThiefEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((ThiefEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((ThiefEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (movingobjectposition.hitInfo instanceof ZebraEntity) {
                        if (((ZebraEntity) movingobjectposition.hitInfo).modelsize > 0.3F) {
                            ((ZebraEntity) movingobjectposition.hitInfo).modelsize -= 0.2F;
                        } else {
                            ((ZebraEntity) movingobjectposition.hitInfo).setHealth(0);
                            flag = true;
                        }
                    }

                    if (flag) {
                        smoke();
                        world.playSound(player, this.getPosition(), SoundsHandler.SHRINK_KILL, SoundCategory.NEUTRAL, 1.0F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
                        setDead();
                    }
                } else {
                    setDead();
                }
            } else {
                hitX = movingobjectposition.getHitVec().x;
                hitY = movingobjectposition.getHitVec().y;
                hitZ = movingobjectposition.getHitVec().z;
                blockHit = world.getBlockState(new BlockPos(hitX, hitY, hitZ));
                motionX = (float) (movingobjectposition.getHitVec().x - posX);
                motionY = (float) (movingobjectposition.getHitVec().y - posY);
                motionZ = (float) (movingobjectposition.getHitVec().z - posZ);
                float f1 = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                posX -= (motionX / (double) f1) * 0.05000000074505806D;
                posY -= (motionY / (double) f1) * 0.05000000074505806D;
                posZ -= (motionZ / (double) f1) * 0.05000000074505806D;
                aoLightValueZPos = true;
                setDead();

                if (blockHit.getBlock() == Blocks.ICE) {
                    world.setBlockState(new BlockPos(hitX, hitY, hitZ), Blocks.WATER.getDefaultState());
                }

                setDead();
            }

            world.playSound(player, this.getPosition(), SoundsHandler.RAY_GUN, SoundCategory.NEUTRAL, 0.2F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
            setDead();
        }

        if (world.isRemote)
            MoreCreepsReboot.proxy.shrinkSmoke(world, this);

        posX += motionX;
        posY += motionY;
        posZ += motionZ;
        float f2 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);

        for (rotationPitch = (float) ((Math.atan2(motionY, f2) * 180D) / Math.PI); rotationPitch
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
        float f3 = 0.99F;
        float f5 = 0.0F;
        // this.vibratePlayer((PlayerEntity)this.getThrower());
        if (handleWaterMovement()) {
            for (int l = 0; l < 4; l++) {
                float f7 = 0.25F;
                world.addParticle(ParticleTypes.BUBBLE, posX - motionX * (double) f7,
                        posY - motionY * (double) f7, posZ - motionZ * (double) f7, motionX, motionY, motionZ);
            }

            f3 = 0.8F;
            float f6 = 0.03F;
            setDead();
        }

        motionX *= f3;
        motionZ *= f3;
        setPosition(posX, posY, posZ);
    }

    public void vibratePlayer(PlayerEntity player) {
        player.posY += rand.nextGaussian() * 0.34999999999999998D * (double) vibrate;
        vibrate *= -1;
        double d1 = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
        double d3 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
        player.posX += (double) ((float) vibrate * 0.25F) * d1;
        player.posZ += (double) ((float) vibrate * 0.25F) * d3;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        compound.putShort("xTile", (short) hitX);
        compound.putShort("yTile", (short) hitY);
        compound.putShort("zTile", (short) hitZ);
        compound.putByte("inGround", (byte) (aoLightValueZPos ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        hitX = compound.getShort("xTile");
        hitY = compound.getShort("yTile");
        hitZ = compound.getShort("zTile");
        aoLightValueZPos = compound.getByte("inGround") == 1;
    }

    public float getShadowSize() {
        return 0.0F;
    }

    public void blast(World world) {
        if (world.isRemote)
            MoreCreepsReboot.proxy.shrinkBlast(world, this, rand);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead() {
        blast(world);
        super.remove();
        lightValueOwn = null;
    }

    private void smoke()
    {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 5; k++) {
                    double d = rand.nextGaussian() * 0.12D;
                    double d1 = rand.nextGaussian() * 0.12D;
                    double d2 = rand.nextGaussian() * 0.12D;
                    world.addParticle(ParticleTypes.EXPLOSION,
                    (posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(), posY + (double) (rand.nextFloat() * getHeight()),
                    (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(), d, d1, d2);
                }
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {

    }

    @Override
    protected void registerData() {

    }
}
