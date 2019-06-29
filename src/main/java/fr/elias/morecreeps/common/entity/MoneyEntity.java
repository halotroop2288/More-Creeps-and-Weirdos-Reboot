package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import fr.elias.morecreeps.common.MoreCreepsReboot;

public class MoneyEntity extends ThrowableEntity
{
    private int field_20056_b; // TODO deobfuscate this!!!
    private int field_20055_c; // TODO deobfuscate this!!!
    private int field_20054_d; // TODO deobfuscate this!!!
    private int field_20053_e; // TODO deobfuscate this!!!
    private boolean field_20052_f; // TODO deobfuscate this!!!
    public int field_20057_a; // TODO deobfuscate this!!!
    protected double initialVelocity;
    public Entity owner;
    double bounceFactor;

    public MoneyEntity(World world)
    {
        super(world);
        setSize(0.25F, 0.25F);
        initialVelocity = 1.0D;
        bounceFactor = 0.14999999999999999D;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double d)
    {
        double d1 = getEntityBoundingBox().getAverageEdgeLength() * 4D;
        d1 *= 64D;
        return d < d1 * d1;
    }

    public MoneyEntity(World world, Entity entity)
    {
        this(world);
        owner = entity;
        setRotation(entity.rotationYaw, 0.0F);
        double d = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
        motionX = 0.59999999999999998D * d * (double)MathHelper.cos((entity.rotationPitch / 180F) * (float)Math.PI);
        motionY = -0.69999999999999996D * (double)MathHelper.sin((entity.rotationPitch / 180F) * (float)Math.PI);
        motionZ = 0.59999999999999998D * d1 * (double)MathHelper.cos((entity.rotationPitch / 180F) * (float)Math.PI);
        setPosition(entity.posX + d * 0.80000000000000004D, entity.posY, entity.posZ + d1 * 0.80000000000000004D);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
    }

    public MoneyEntity(World world, double d, double d1, double d2)
    {
        this(world);
        setPosition(d, d1, d2);
    }

    public void func_20048_a(double d, double d1, double d2, float f, float f1)
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
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
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
        motionY -= 0.0080000000000000002D;
        motionZ *= 0.97999999999999998D;

        if (isCollidedVertically)
        {
            motionX *= 0.14999999999999999D;
            motionZ *= 0.14999999999999999D;
        }

        if (!onGround)
        {
            Object obj = null;
            List list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(motionX, motionY, motionZ).expand(0.5D, 0.5D, 0.5D));
            double d3 = 0.0D;

            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);

                if (entity.canBeCollidedWith() && (entity instanceof PreacherEntity))
                {
                	PlayerEntity player = world.getClosestPlayer(entity, 2D);
                    
                    if (player != null)
                    {
                        entity.attackEntityFrom(DamageSource.causeMobDamage(player), 10);
                    }

                    if (((LivingEntity)entity).getHealth() <= 0 && !((ServerPlayerEntity)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achievefalseidol))
                    {
                        player.addStat(MoreCreepsReboot.achievefalseidol, 1);
                        double d4 = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
                        double d5 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
                        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
                        creepsentitytrophy.setLocationAndAngles(player.posX + d4 * 3D, player.posY - 2D, player.posZ + d5 * 3D, player.rotationYaw, 0.0F);
                        world.spawnEntityInWorld(creepsentitytrophy);
                    }

                    setDead();
                }

                if (!entity.canBeCollidedWith() || !(entity instanceof LawyerFromHellEntity))
                {
                    continue;
                }

                MoreCreepsReboot.instance.currentfine -= 50;

                if (MoreCreepsReboot.instance.currentfine < 0)
                {
                    MoreCreepsReboot.instance.currentfine = 0;
                }

                setDead();
            }
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(PlayerEntity entityplayer)
    {
        if (rand.nextInt(30) == 0)
        {
            if (entityplayer.inventory.addItemStackToInventory(new ItemStack(MoreCreepsReboot.money, 1, 0)))
            {
                world.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityplayer.onItemPickup(this, 1);
                setDead();
            }

            Object obj = null;
            ItemStack aitemstack[] = entityplayer.inventory.mainInventory;
            int i = 0;

            for (int j = 0; j < aitemstack.length; j++)
            {
                ItemStack itemstack = aitemstack[j];

                if (itemstack != null && itemstack.getItem() == MoreCreepsReboot.money)
                {
                    i += itemstack.stackSize;
                }
            }

            boolean flag = false;
            
            //TODO : Fix the achievement problem... (find an alternative to "hasAchievementUnlocked")...
            //29/12/2015 : Packet Needed for achievement detector... -.-
            if(!world.isRemote)
            {
                if(!(entityplayer instanceof ServerPlayerEntity))
                {
                	ServerPlayerEntity playerMP = (ServerPlayerEntity) entityplayer;
                    if (!playerMP.getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve100bucks) && i > 99)
                    {
                        flag = true;
                        confetti(entityplayer);
                        entityplayer.addStat(MoreCreepsReboot.achieve100bucks, 1);
                    }

                    if (!playerMP.getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve500bucks) && i > 499)
                    {
                        flag = true;
                        confetti(entityplayer);
                        entityplayer.addStat(MoreCreepsReboot.achieve500bucks, 1);
                    }

                    if (!playerMP.getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve1000bucks) && i > 999)
                    {
                        flag = true;
                        confetti(entityplayer);
                        entityplayer.addStat(MoreCreepsReboot.achieve1000bucks, 1);
                    }
                }else{
                	System.out.println("[More Creeps Unofficial] Class cast failed when tried to trigger achievement in this class : " + this.getClass() + ".");
                	System.out.println("[More Creeps Unofficial] Please contact elias54 or Astromojang for this!");
                }
            }

            if (flag)
            {
                world.playSoundAtEntity(this, "morecreeps:achievement", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    public void confetti(PlayerEntity entityplayersp)
    {
        double d = -MathHelper.sin((entityplayersp.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((entityplayersp.rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(entityplayersp.posX + d * 3D, entityplayersp.posY - 2D, ((PlayerEntity)(entityplayersp)).posZ + d1 * 3D, ((PlayerEntity)(entityplayersp)).rotationYaw, 0.0F);
        world.spawnEntityInWorld(creepsentitytrophy);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("xTile", (short)field_20056_b);
        nbttagcompound.setShort("yTile", (short)field_20055_c);
        nbttagcompound.setShort("zTile", (short)field_20054_d);
        nbttagcompound.setByte("inTile", (byte)field_20053_e);
        nbttagcompound.setByte("shake", (byte)field_20057_a);
        nbttagcompound.setByte("inGround", (byte)(field_20052_f ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        field_20056_b = nbttagcompound.getShort("xTile");
        field_20055_c = nbttagcompound.getShort("yTile");
        field_20054_d = nbttagcompound.getShort("zTile");
        field_20053_e = nbttagcompound.getByte("inTile") & 0xff;
        field_20057_a = nbttagcompound.getByte("shake") & 0xff;
        field_20052_f = nbttagcompound.getByte("inGround") == 1;
    }

    public float getShadowSize()
    {
        return 0.2F;
    }
    
    public ItemStack getEntityItem()
    {
    	return new ItemStack(MoreCreepsReboot.money, 1, 0);
    }

	@Override
	protected void onImpact(MovingObjectPosition p_70184_1_)
	{
		
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
