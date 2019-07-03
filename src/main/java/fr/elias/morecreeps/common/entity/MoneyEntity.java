package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class MoneyEntity extends ThrowableEntity {
    private int xTileNBT;
    private int yTileNBT;
    private int zTileNBT;
    private int inTileNBT;
    private boolean inGroundNBT;
    public int shakeNBT;
    protected double initialVelocity;
    public Entity owner;
    double bounceFactor;

    public MoneyEntity(World world) {
        super(null, world);
        // setSize(0.25F, 0.25F);
        initialVelocity = 1.0D;
        bounceFactor = 0.14999999999999999D;
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

    public MoneyEntity(World world, Entity entity) {
        this(world);
        owner = entity;
        setRotation(entity.rotationYaw, 0.0F);
        double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
        double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
        setMotion(0.59999999999999998D * d * (double) MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI), // motionX
                -0.69999999999999996D * (double) MathHelper.sin((entity.rotationPitch / 180F) * (float) Math.PI), // motionY
                0.59999999999999998D * d1 * (double) MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI) // motionZ
        );
        setPosition(entity.posX + d * 0.80000000000000004D, entity.posY, entity.posZ + d1 * 0.80000000000000004D);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
    }

    public MoneyEntity(World world, double d, double d1, double d2) {
        this(world);
        setPosition(d, d1, d2);
    }

    public void func_20048_a(double d, double d1, double d2, float f, float f1) {
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
        setMotion(d, d1, d2);
        float f3 = MathHelper.sqrt(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float) ((Math.atan2(d1, f3) * 180D) / Math.PI);
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double d, double d1, double d2) {
        setMotion(d, d1, d2);

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(d * d + d2 * d2);
            prevRotationYaw = rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float) ((Math.atan2(d1, f) * 180D) / Math.PI);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick(World world) {
        double d = getMotion().x;
        double d1 = getMotion().y;
        double d2 = getMotion().z;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        setMotion(getMotion().x, getMotion().y, getMotion().z); // Pointless now?

        if (handleWaterMovement()) {
            setMotion // Adds a pointlessly specific amount of speed in all directions
            (getMotion().x + 0.0087999999523162842D, // motionX
                    getMotion().y + 0.97999999999999998D, // motionY
                    getMotion().z + 0.68000000000000005D // motionZ
            );
        }

        if (getMotion().x != d) // if the current x motion isn't equal to the previous x motion then
        {
            setMotion(-bounceFactor * d, getMotion().y, getMotion().z); // sets x motion equal to the negative of
                                                                        // "bounceFactor" times the previous x motion
        }

        if (getMotion().y != d1) // if the current y motion isn't equal to the previous y motion, then
        {
            setMotion(getMotion().x, -bounceFactor * d1, getMotion().z); // sets y motion equal to the negative of
                                                                         // "bounceFactor" times the previous y motion
        }

        if (getMotion().y != d1) // if the current y motion isn't equal to the previous y motion, then
        {
            setMotion(getMotion().x, -bounceFactor * d1, getMotion().z); // sets y motion equal to the negative of
                                                                         // "bounceFactor" times the previous y motion
        }
        // Why does it perform this exact same opperation twice in a row per cycle?
        // Redundant?

        else if (!handleWaterMovement()) {
            setMotion(getMotion().x, getMotion().y - 0.0050000000000000001D, getMotion().z); // Adds downward motion
                                                                                             // when in water... So it
                                                                                             // moves *faster* downward
                                                                                             // in water, when it should
                                                                                             // float to the top
        }

        setMotion(getMotion().x * 0.97999999999999998D, getMotion().y - 0.0080000000000000002D,
                getMotion().z * 0.97999999999999998D); // removes a pointlessly specific amount of speed in all axes,
                                                       // especially Y. Like WTF?!

        if (collidedVertically) // if it hits something above or below it
        {
            setMotion(getMotion().x * 0.14999999999999999D, getMotion().y, getMotion().z * 0.14999999999999999D); // removes
                                                                                                                  // a
                                                                                                                  // pointlessly
                                                                                                                  // specific
                                                                                                                  // amount
                                                                                                                  // of
                                                                                                                  // speed
                                                                                                                  // in
                                                                                                                  // the
                                                                                                                  // X
                                                                                                                  // and
                                                                                                                  // Z
                                                                                                                  // axes
        }

        if (!onGround) {
            Object obj = null;
            @SuppressWarnings("rawtypes")
            List list = world.getEntitiesWithinAABBExcludingEntity(this,
                    getBoundingBox().addCoord(getMotion().x, getMotion().y, getMotion().z).expand(0.5D, 0.5D, 0.5D));
            double d3 = 0.0D;

            for (int i = 0; i < list.size(); i++) {
                Entity entity = (Entity) list.get(i);

                if (entity.canBeCollidedWith() && (entity instanceof PreacherEntity)) {
                    PlayerEntity player = world.getClosestPlayer(entity, 2D);

                    if (player != null) {
                        entity.attackEntityFrom(DamageSource.causeMobDamage(player), 10);
                    }

                    // if (((LivingEntity)entity).getHealth() <= 0 &&
                    // !((ServerPlayerEntity)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achievefalseidol))
                    // {
                    // player.addStat(MoreCreepsReboot.achievefalseidol, 1);
                    // double d4 = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
                    // double d5 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
                    // TrophyEntity creepsentitytrophy = new TrophyEntity(world);
                    // creepsentitytrophy.setLocationAndAngles(player.posX + d4 * 3D, player.posY -
                    // 2D, player.posZ + d5 * 3D, player.rotationYaw, 0.0F);
                    // world.spawnEntityInWorld(creepsentitytrophy);
                    // }

                    remove();
                }

                if (!entity.canBeCollidedWith() || !(entity instanceof LawyerFromHellEntity)) {
                    continue;
                }

                MoreCreepsReboot.instance.currentfine -= 50;

                if (MoreCreepsReboot.instance.currentfine < 0) {
                    MoreCreepsReboot.instance.currentfine = 0;
                }

                remove();
            }
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(PlayerEntity playerentity) {
        if (rand.nextInt(30) == 0) {
            if (playerentity.inventory.addItemStackToInventory(new ItemStack(ItemList.money, 1))) {
                world.playSound(playerentity, this.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                        0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                playerentity.onItemPickup(this, 1);
                remove();
            }

            Object obj = null;
            NonNullList<ItemStack> aitemstack = playerentity.inventory.mainInventory;
            int i = 0;

            for (int j = 0; j < aitemstack.size(); j++)
            {
                ItemStack itemstack = aitemstack[j];

                if (itemstack != null && itemstack.getItem() == ItemList.money)
                {
                    i += itemstack.getCount();
                }
            }

            boolean flag = false;
            
            // TODO : Fix the achievement problem... (find an alternative to "hasAchievementUnlocked")...
            // 12/29/2015 : Packet Needed for achievement detector... -.-
            // 7/1/2019 : Need to set custom criterion for JSON-based advancements - halo
            if(!world.isRemote)
            {
                // if(!(playerentity instanceof ServerPlayerEntity))
                // {
                // 	ServerPlayerEntity playerMP = (ServerPlayerEntity) playerentity;
                //     if (!playerMP.getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve100bucks) && i > 99)
                //     {
                //         flag = true;
                //         confetti(playerentity);
                //         playerentity.addStat(MoreCreepsReboot.achieve100bucks, 1);
                //     }

                //     if (!playerMP.getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve500bucks) && i > 499)
                //     {
                //         flag = true;
                //         confetti(playerentity);
                //         playerentity.addStat(MoreCreepsReboot.achieve500bucks, 1);
                //     }

                //     if (!playerMP.getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve1000bucks) && i > 999)
                //     {
                //         flag = true;
                //         confetti(playerentity);
                //         playerentity.addStat(MoreCreepsReboot.achieve1000bucks, 1);
                //     }
                // }
                // else
                {
                	System.out.println("[More Creeps Unofficial] Class cast failed when trying to trigger advancement in this class : " + this.getClass() + ".");
                	System.out.println("[More Creeps Unofficial] Please contact elias54, Astromojang, or halotroop2288 for this!");
                }
            }

            if (flag)
            {
                world.playSound(playerentity, this.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    public void confetti(PlayerEntity playerentitysp)
    {
        double d = -MathHelper.sin((playerentitysp.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((playerentitysp.rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(playerentitysp.posX + d * 3D, playerentitysp.posY - 2D, ((PlayerEntity)(playerentitysp)).posZ + d1 * 3D, ((PlayerEntity)(playerentitysp)).rotationYaw, 0.0F);
        world.addEntity(creepsentitytrophy);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT nbttagcompound)
    {
        nbttagcompound.putShort("xTile", (short) xTileNBT);
        nbttagcompound.putShort("yTile", (short) yTileNBT);
        nbttagcompound.putShort("zTile", (short) zTileNBT);
        nbttagcompound.putByte("inTile", (byte) inTileNBT);
        nbttagcompound.putByte("shake", (byte) shakeNBT);
        nbttagcompound.putByte("inGround", (byte) (inGroundNBT ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT nbttagcompound)
    {
        xTileNBT = nbttagcompound.getShort("xTile");
        yTileNBT = nbttagcompound.getShort("yTile");
        zTileNBT = nbttagcompound.getShort("zTile");
        inTileNBT = nbttagcompound.getByte("inTile") & 0xff;
        shakeNBT = nbttagcompound.getByte("shake") & 0xff;
        inGroundNBT = nbttagcompound.getByte("inGround") == 1;
    }

    public float getShadowSize()
    {
        return 0.2F;
    }
    
    public ItemStack getEntityItem()
    {
    	return new ItemStack(ItemList.money, 1);
    }



	@Override
    protected void onImpact(RayTraceResult result)
    {}

	@Override
    protected void registerData()
    {}
}
