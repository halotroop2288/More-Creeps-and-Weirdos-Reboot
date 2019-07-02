package fr.elias.morecreeps.common.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class PrisonerEntity extends MobEntity {
    static final ResourceLocation prisonerTextures[] =
    {
        new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "prisoner1.png"),
        new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "prisoner2.png"),
        new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "prisoner3.png"),
        new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "prisoner4.png"),
        new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "prisoner5.png")
    };
    protected double attackRange;
    public float modelsize;
    public boolean saved;
    public int timeonland;
    public boolean evil;
    public ResourceLocation texture;

    public PrisonerEntity(World world)
    {
        super(null, world);
        texture = prisonerTextures[rand.nextInt(prisonerTextures.length)];
        attackRange = 16D;
        timeonland = 0;
        saved = false;
        modelsize = 1.0F;

        if (rand.nextInt(2) == 0)
        {
            evil = true;
        }
        else
        {
            evil = false;
        }
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new EntityAIAttackOnCollide(this, PlayerEntity.class, 0.4D, true));
        // this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
        // this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(4, new EntityAILookIdle(this));
        // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        // this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, PlayerEntity.class, true));
    }
    
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
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
    
    @Override
    public void tick()
    {
        World world = Minecraft.getInstance().world;
        if (inWater)
        {
            getYOffset();
        }
        else
        {
            PlayerEntity player = world.getClosestPlayer(this, 2D);

            if (player != null)
            {
                float f = player.getDistance(this);

                if (f < 3F && canEntityBeSeen(player) && !saved && timeonland++ > 50 && !evil)
                {
                    giveReward(player);
                }
            }
        }

        super.tick();
    }

    public void giveReward(PlayerEntity player)
    {
        MoreCreepsReboot.prisonercount++;

        // if (!((ServerPlayerEntity)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieveprisoner))
        // {
        //     world.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
        //     player.addStat(ModAdvancementList.prisoner, 1);
        //     confetti(player);
        // }
        // else if (!((ServerPlayerEntity)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve5prisoner) && MoreCreepsReboot.prisonercount == 5)
        // {
        //     world.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
        //     player.addStat(ModAdvancementList.five_prisoner, 1);
        //     confetti(player);
        // }
        // else if (!((ServerPlayerEntity)player).getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieve10prisoner) && MoreCreepsReboot.prisonercount == 10)
        // {
        //     world.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
        //     player.addStat(ModAdvancementList.ten_prisoner, 1);
        //     confetti(player);
        // }

        if (rand.nextInt(4) == 0)
        {
            world.playSound(player, this.getPosition(), SoundsHandler.PRISONER_SORRY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            return;
        }

        world.playSound(player, this.getPosition(), SoundsHandler.PRISONER_REWARD, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        saved = true;
        int i = rand.nextInt(4) + 1;
        faceEntity(player, 0.0F, 0.0F);

        if (player != null)
        {
        	ItemEntity entityitem = null;

            switch (i)
            {
                case 1:
                    entityitem = entityDropItem(new ItemStack(ItemList.lolly, rand.nextInt(2) + 1), 1.0F);
                    break;

                case 2:
                    entityitem = entityDropItem(new ItemStack(Items.BREAD, 1), 1.0F);
                    break;

                case 3:
                    entityitem = entityDropItem(new ItemStack(Items.CAKE, 1), 1.0F);
                    break;

                case 4:
                    entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(20) + 1), 1.0F);
                    break;

                default:
                    entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(5) + 1), 1.0F);
                    break;
            }

            double d = -MathHelper.sin((((PlayerEntity)(player)).rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((((PlayerEntity)(player)).rotationYaw * (float)Math.PI) / 180F);
            entityitem.posX = ((PlayerEntity)(player)).posX + d * 0.5D;
            entityitem.posY = ((PlayerEntity)(player)).posY + 0.5D;
            entityitem.posZ = ((PlayerEntity)(player)).posZ + d1 * 0.5D;
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();

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
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("modelsize", modelsize);
        compound.putBoolean("saved", saved);
        compound.putBoolean("evil", evil);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("modelsize");
        saved = compound.getBoolean("saved");
        evil = compound.getBoolean("evil");
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (rand.nextInt(5) == 0)
        {
            return SoundsHandler.PRISONER;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.PRISONER_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.PRISONER_DEATH;
    }

    public void confetti(PlayerEntity player)
    {
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
        world.addEntity(creepsentitytrophy);
    }
}