package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;

public class PrisonerEntity extends MobEntity
{
    static final String prisonerTextures[] =
    {
        "morecreeps:textures/entity/prisoner1.png", "morecreeps:textures/entity/prisoner2.png", "morecreeps:textures/entity/prisoner3.png", "morecreeps:textures/entity/prisoner4.png", "morecreeps:textures/entity/prisoner5.png"
    };
    protected double attackRange;
    private int waittime;
    public float modelsize;
    public boolean saved;
    public int timeonland;
    public boolean evil;
    public String texture;

    public PrisonerEntity(World world)
    {
        super(world);
        texture = prisonerTextures[rand.nextInt(prisonerTextures.length)];
        attackRange = 16D;
        timeonland = 0;
        saved = false;
        waittime = rand.nextInt(1500) + 500;
        modelsize = 1.0F;

        if (rand.nextInt(2) == 0)
        {
            evil = true;
        }
        else
        {
            evil = false;
        }
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.4D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }
    
    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        return false;
    }
    
    public double getYOffset()
    {
        return inWater ? -1.4D : 0.0D;
    }
    
    public void onLivingUpdate(World world)
    {
        if (inWater)
        {
            getYOffset();
        }
        else
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(getBoundingBox().minY);
            int k = MathHelper.floor_double(posZ);
            Block l = world.getBlockState(new BlockPos(i, j, k)).getBlock();
            PlayerEntity entityplayersp = world.getClosestPlayer(this, 2D);

            if (entityplayersp != null)
            {
                float f = entityplayersp.getDistance(this);

                if (f < 3F && canEntityBeSeen(entityplayersp) && !saved && timeonland++ > 50 && !evil)
                {
                    giveReward(entityplayersp);
                }
            }
        }

        super.onLivingUpdate();
    }

    public void giveReward(PlayerEntity player)
    {
        MoreCreepsReboot.prisonercount++;

        if (!((ServerPlayerEntity)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieveprisoner))
        {
            world.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
            player.addStat(MoreCreepsReboot.achieveprisoner, 1);
            confetti(player);
        }
        else if (!((ServerPlayerEntity)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve5prisoner) && MoreCreepsReboot.prisonercount == 5)
        {
            world.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
            player.addStat(MoreCreepsReboot.achieve5prisoner, 1);
            confetti(player);
        }
        else if (!((ServerPlayerEntity)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve10prisoner) && MoreCreepsReboot.prisonercount == 10)
        {
            world.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
            player.addStat(MoreCreepsReboot.achieve10prisoner, 1);
            confetti(player);
        }

        if (rand.nextInt(4) == 0)
        {
            world.playSoundAtEntity(this, "morecreeps:prisonersorry", 1.0F, 1.0F);
            return;
        }

        world.playSoundAtEntity(this, "morecreeps:prisonerreward", 1.0F, 1.0F);
        saved = true;
        int i = rand.nextInt(4) + 1;
        faceEntity(player, 0.0F, 0.0F);

        if (player != null)
        {
        	ItemEntity entityitem = null;

            switch (i)
            {
                case 1:
                    entityitem = entityDropItem(new ItemStack(MoreCreepsReboot.lolly, rand.nextInt(2) + 1, 0), 1.0F);
                    break;

                case 2:
                    entityitem = entityDropItem(new ItemStack(Items.bread, 1, 0), 1.0F);
                    break;

                case 3:
                    entityitem = entityDropItem(new ItemStack(Items.cake, 1, 0), 1.0F);
                    break;

                case 4:
                    entityitem = entityDropItem(new ItemStack(MoreCreepsReboot.money, rand.nextInt(20) + 1, 0), 1.0F);
                    break;

                default:
                    entityitem = entityDropItem(new ItemStack(MoreCreepsReboot.money, rand.nextInt(5) + 1, 0), 1.0F);
                    break;
            }

            double d = -MathHelper.sin((((EntityPlayer)(player)).rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((((EntityPlayer)(player)).rotationYaw * (float)Math.PI) / 180F);
            entityitem.posX = ((EntityPlayer)(player)).posX + d * 0.5D;
            entityitem.posY = ((EntityPlayer)(player)).posY + 0.5D;
            entityitem.posZ = ((EntityPlayer)(player)).posZ + d1 * 0.5D;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getEntity();

        if (entity instanceof PlayerEntity)
        {
            evil = true;
        }

        super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("modelsize", modelsize);
        nbttagcompound.setBoolean("saved", saved);
        nbttagcompound.setBoolean("evil", evil);
        nbttagcompound.setString("Texture", texture);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        modelsize = nbttagcompound.getFloat("modelsize");
        saved = nbttagcompound.getBoolean("saved");
        evil = nbttagcompound.getBoolean("evil");
        texture = nbttagcompound.getString("Texture");
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    private void smoke()
    {
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2, new int[0]);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2, new int[0]);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)i) - (double)width, d, d1, d2, new int[0]);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)i - (double)width, d, d1, d2, new int[0]);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)i) - (double)width, d, d1, d2, new int[0]);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)i - (double)width, d, d1, d2, new int[0]);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)i) - (double)width, d, d1, d2, new int[0]);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)i, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)i - (double)width, d, d1, d2, new int[0]);
            }
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        if (rand.nextInt(5) == 0)
        {
            return "morecreeps:prisoner";
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "morecreeps:prisonerhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "morecreeps:prisonerdeath";
    }

    public void confetti(PlayerEntity player)
    {
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
        world.spawnEntityInWorld(creepsentitytrophy);
    }
}