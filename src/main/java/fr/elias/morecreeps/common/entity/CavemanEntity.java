package fr.elias.morecreeps.common.entity;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class CavemanEntity extends MobEntity
{
    protected double attackrange;
    protected int attack;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();

    public int eaten;
    public boolean hungry;
    public int hungrytime;
    public float hammerswing;
    public int frozen;
    public float modelsize;
    public float fat;
    public boolean cavegirl;
    public boolean evil;
    public float modelsizebase;
    public int wanderstate;
    public int houseX;
    public int houseY;
    public int houseZ;
    public int housechunk;
    public int area;
    public int talkdelay;

    public String texture;
	public int attackTime;

    public CavemanEntity(World world)
    {
        super(null, world);
        scoreValue = 4;
        attack = 1;
        attackrange = 16D;
        hammerswing = 0.0F;
        hungry = false;
        hungrytime = rand.nextInt(100) + 10;

        if (rand.nextInt(100) > 50)
        {
            cavegirl = true;
        }

        evil = false;
        wanderstate = 0;
        frozen = 5;
        fat = rand.nextFloat() * 1.0F - rand.nextFloat() * 0.55F;
        modelsize = (1.25F + rand.nextFloat() * 1.0F) - rand.nextFloat() * 0.75F;
        modelsizebase = modelsize;
        setSize(width * 0.8F + fat, height * 1.3F + fat);
        setCaveTexture();
        this.targetTasks.addTask(0, new CavemanEntity.AIFindPlayerToAttack());
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
    }

    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
    	
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate(World world)
    {
        super.onUpdate();

        if (frozen > 0 && world.getBlockState(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY), MathHelper.floor(posZ))).getBlock() == Blocks.AIR)
        {
            posY--;
        }

        if (isWet())
        {
            frozen = 0;
        }
        /*double moveSpeed = this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed).getAttributeValue();
        moveSpeed = frozen >= 1 ? 0.0F : 0.45F;*/

        if (wanderstate == 0 && frozen < 1 && !evil && !MoreCreepsReboot.cavemanbuilding && rand.nextInt(100) == 0)
        {
            wanderstate = 1;
        }

        if (wanderstate == 1 && rand.nextInt(201) == 200 && !MoreCreepsReboot.cavemanbuilding && !evil && checkArea())
        {
            wanderstate = 2;
            housechunk = 0;

            for (int i = 0; i < 4; i++)
            {
                if (world.getBlockState(new BlockPos(houseX, houseY, houseZ)).getBlock() == Blocks.AIR || world.getBlockState(new BlockPos(houseX + 1, houseY, houseZ)).getBlock() == Blocks.AIR || world.getBlockState(new BlockPos(houseX + 2, houseY, houseZ + 4)) == Blocks.air || world.getBlockState(new BlockPos(houseX, houseY, houseZ + 2)).getBlock() == Blocks.air)
                {
                    houseY--;
                }
            }

            MoreCreepsReboot.cavemanbuilding = true;
        }

        if (wanderstate == 2)
        {
            posX = houseX - 1;
            //moveSpeed = 0.0F; TODO
            setRotation(45F, rotationPitch);
        }

        if (wanderstate == 2 && rand.nextInt(50) == 0)
        {
            if (housechunk == 0)
            {
                hammerswing = -2.8F;
                world.setBlockState(new BlockPos(houseX + 1, houseY, houseZ), Blocks.SNOW.getDefaultState());
                housechunk++;
                snowFX(houseX + 1, houseY, houseZ, world);
            }
            else if (housechunk == 1)
            {
                hammerswing = -2.8F;
                world.setBlockState(new BlockPos(houseX + 1, houseY + 1, houseZ), Blocks.SNOW.getDefaultState());
                housechunk++;
                snowFX(houseX + 1, houseY + 1, houseZ, world);
            }
            else if (housechunk == 2)
            {
                hammerswing = -2.8F;
                world.setBlockState(new BlockPos(houseX + 3, houseY, houseZ), Blocks.SNOW.getDefaultState());
                snowFX(houseX + 3, houseY, houseZ, world);
                housechunk++;
            }
            else if (housechunk == 3)
            {
                hammerswing = -2.8F;
                world.setBlockState(new BlockPos(houseX + 3, houseY + 1, houseZ), Blocks.SNOW.getDefaultState());
                snowFX(houseX + 3, houseY + 1, houseZ, world);
                housechunk++;
            }
            else if (housechunk == 4)
            {
                hammerswing = -2.8F;

                for (int j = 1; j < 4; j++)
                {
                    world.setBlockState(new BlockPos(houseX, houseY, houseZ + j), Blocks.SNOW.getDefaultState());
                    snowFX(houseX, houseY, houseZ + j, world);
                }

                housechunk++;
            }
            else if (housechunk == 5)
            {
                hammerswing = -2.8F;

                for (int k = 1; k < 4; k++)
                {
                    world.setBlockState(new BlockPos(houseX, houseY + 1, houseZ + k), Blocks.SNOW.getDefaultState());
                    snowFX(houseX, houseY + 1, houseZ + k, world);
                }

                housechunk++;
            }
            else if (housechunk == 6)
            {
                hammerswing = -2.8F;

                for (int l = 1; l < 4; l++)
                {
                    world.setBlockState(new BlockPos(houseX + 4, houseY, houseZ + l), Blocks.SNOW.getDefaultState());
                    snowFX(houseX + 4, houseY, houseZ + l, world);
                }

                housechunk++;
            }
            else if (housechunk == 7)
            {
                hammerswing = -2.8F;

                for (int i1 = 1; i1 < 4; i1++)
                {
                    world.setBlockState(new BlockPos(houseX + 4, houseY + 1, houseZ + i1), Blocks.SNOW.getDefaultState());
                    snowFX(houseX + 4, houseY + 1, houseZ + i1, world);
                }

                housechunk++;
            }
            else if (housechunk == 8)
            {
                hammerswing = -2.8F;

                for (int j1 = 1; j1 < 4; j1++)
                {
                    world.setBlockState(new BlockPos(houseX + j1, houseY, houseZ + 4), Blocks.SNOW.getDefaultState());
                    snowFX(houseX + j1, houseY, houseZ + 4, world);
                }

                housechunk++;
            }
            else if (housechunk == 9)
            {
                hammerswing = -2.8F;

                for (int k1 = 1; k1 < 4; k1++)
                {
                    world.setBlockState(new BlockPos(houseX + k1, houseY + 1, houseZ + 4), Blocks.SNOW.getDefaultState());
                    snowFX(houseX + k1, houseY + 1, houseZ + 4, world);
                }

                housechunk++;
            }
            else if (housechunk == 10)
            {
                hammerswing = -2.8F;

                for (int l1 = 1; l1 < 4; l1++)
                {
                    for (int j2 = 1; j2 < 4; j2++)
                    {
                        world.setBlockState(new BlockPos(houseX + j2, houseY + 2, houseZ + l1), Blocks.SNOW.getDefaultState());
                        snowFX(houseX + j2, houseY + 2, houseZ + l1, world);
                    }
                }

                housechunk++;
            }
            else if (housechunk == 11)
            {
                hammerswing = -2.8F;
                world.setBlockState(new BlockPos(houseX + 2, houseY + 3, houseZ + 2), Blocks.SNOW.getDefaultState());
                snowFX(houseX + 2, houseY + 3, houseZ + 2, world);
                housechunk++;
            }
            else if (housechunk == 12)
            {
                Item i2;

                if (rand.nextInt(5) == 0)
                {
                    i2 = Items.COD;
                }
                else
                {
                    i2 = ItemList.popsicle;
                }

                if(!world.isRemote)
                {
                	ItemEntity entityitem = new ItemEntity(world, houseX + 3, houseY, houseZ + 3, new ItemStack(i2, rand.nextInt(4) + 1));
                    world.addEntity(entityitem);
                }
                //moveSpeed = maxspeed;
                MoreCreepsReboot.cavemanbuilding = false;
                wanderstate = 3;
            }
        }

        if (hammerswing < 0.0F)
        {
            hammerswing += 0.4F;
        }
        else
        {
            hammerswing = 0.0F;
        }

        /*PlayerEntitySP entityplayersp = ModLoader.getMinecraftInstance().thePlayer;
        float f = getDistanceToEntity(entityplayersp);

        if (f < 8F)
        {
            ignoreFrustumCheck = true;
        }
        else
        {
            ignoreFrustumCheck = false;
        }*/
    }
    
    public boolean isMovementBlocked()
    {
    	return frozen >= 1 || this.wanderstate == 2 ? true : false;
    }

    public boolean checkArea()
    {
        houseX = MathHelper.floor(posX);
        houseY = MathHelper.floor(posY);
        houseZ = MathHelper.floor(posZ);

        if (world.getBlockState(new BlockPos(houseX, houseY - 1, houseZ)).getBlock() == Blocks.air)
        {
            houseY--;
        }

        area = 0;

        for (int i = -3; i < 7; i++)
        {
            for (int k = -3; k < 7; k++)
            {
                for (int i1 = 0; i1 < 3; i1++)
                {
                    if (world.getBlockState(new BlockPos(houseX + k, houseY + i1, houseZ + i)).getBlock() == Blocks.air)
                    {
                        area++;
                    }
                }
            }
        }

        if (area < 220)
        {
            return false;
        }

        for (int j = -2; j < 7; j++)
        {
            for (int l = -2; l < 7; l++)
            {
                Block j1 = world.getBlockState(new BlockPos(houseX + l, houseY, houseZ + j)).getBlock();
                Block k1 = world.getBlockState(new BlockPos(houseX + l, houseY - 1, houseZ + j)).getBlock();

                if (j1 == Blocks.SNOW || j1 == Blocks.ICE)
                {
                    area++;
                }

                if (k1 == Blocks.SNOW || k1 == Blocks.ICE)
                {
                    area++;
                }
            }
        }

        return area > 75;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    /*protected Entity findPlayerToAttack()
    {
        if (!evil || frozen > 0)
        {
            return null;
        }
        else
        {
            return super.findPlayerToAttack();
        }
    }*/

    class AIFindPlayerToAttack extends EntityAINearestAttackableTarget
    {
        public AIFindPlayerToAttack()
        {
            super(CavemanEntity.this, PlayerEntity.class, true);
        }
        
        public void updateTask()
        {
        	LivingEntity target = CavemanEntity.this.getAttackTarget();
        	float f = getDistanceToEntity(target);
        	attackEntity(target, f);
        }
        
        public boolean shouldExecute()
        {
            return !evil || frozen > 0 && super.shouldExecute();
        }
    }
    
    /**
     * knocks back this entity
     */
    public void knockBack(Entity entity, int i, double d, double d1)
    {
        if (frozen < 1)
        {
            super.knockBack(entity, i, d, d1);
        }
    }

    public void updateRiderPosition()
    {
        riddenByEntity.setPosition(posX, posY + getMountedYOffset() + riddenByEntity.getYOffset(), posZ);
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 0.5D;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (frozen < 1)
        {
            super.onLivingUpdate();
        }

        if (handleWaterMovement())
        {
            frozen = 0;
        }

        if (isWet())
        {
            frozen = 0;
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float func_180484_a(BlockPos bp)
    {
        if (world.getBlockState(bp.down()) == Blocks.gravel.getDefaultState() || world.getBlockState(bp.down()) == Blocks.stone.getDefaultState())
        {
            return 10F;
        }
        else
        {
            return -(float)bp.getY();
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i, PlayerEntity playerentity)
    {
        Entity entity = damagesource.getTrueSource();
        hungry = false;

        if (frozen < 1)
        {
            evil = true;
            setCaveTexture();

            if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
            {
                if (getPassengers() == entity || getRidingEntity() == entity)
                {
                    return true;
                }

                if (entity != this && world.getDifficulty() != Difficulty.PEACEFUL)
                {
                    setRevengeTarget((LivingEntity) entity);
                }

                return true;
            }
            else
            {
                return false;
            }
        }

        if (entity != null && (entity instanceof PlayerEntity) && frozen > 0)
        {
            world.playSound(playerentity, this.getPosition(), SoundsHandler.CAVEMAN_ICE, SoundCategory.NEUTRAL, 0.5F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

            if (rand.nextInt(100) > 65)
            {
                frozen--;
            }

            for (int j = 0; j < 35; j++)
            {
                world.addParticle(ParticleTypes.ITEM_SNOWBALL, posX, posY + 1.0D, posZ, 0.0D, 0.0D, 0.0D);
            }
        }

        if (frozen > 0)
        {
            hurtTime = 0;
        }

        return false;
    }

    public void snowFX(int i, int j, int k, World world)
    {
        for (int l = 0; l < 40; l++)
        {
            world.addParticle(ParticleTypes.ITEM_SNOWBALL, i, (double)j + 0.5D, k, 1.0D, 1.0D, 1.0D);
        }
        if(world.isRemote)
        {
            MoreCreepsReboot.proxy.foam3(world, this, i, j, k);
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f, PlayerEntity playerentity)
    {
        if (frozen < 1)
        {
            if (onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt(d * d + d1 * d1);
                moveForward = (float) ((d / (double)f1) * 0.20000000000000001D * (0.45000001192092898D + moveForward * 0.20000000298023224D));
                moveStrafing = (float) ((d1 / (double)f1) * 0.20000000000000001D * (0.40000001192092893D + moveStrafing * 0.20000000298023224D));
                moveVertical = (float) 0.46000000596246449D;
                fallDistance = -25F;
            }

            if ((double)f < 2.8999999999999999D && entity.getBoundingBox().maxY > getBoundingBox().minY && entity.getBoundingBox().minY < getBoundingBox().maxY)
            {
                if (hammerswing == 0.0F)
                {
                    hammerswing = -2.8F;
                }

                if (talkdelay-- < 0)
                {
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.CAVEMAN_EVIL, SoundCategory.HOSTILE, 0.5F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    talkdelay = 2;
                }
            }

            if ((double)f < 2.3500000000000001D && entity.getBoundingBox().maxY > getBoundingBox().minY && entity.getBoundingBox().minY < getBoundingBox().maxY)
            {
                attackTime = 20;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack);
            }
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        //int l = world.getFullBlockLightValue(i, j, k);
        IBlockState i1 = world.getBlockState(new BlockPos(i, j - 1, k));
        return (i1 == Blocks.SNOW.getDefaultState() || i1 == Blocks.ICE.getDefaultState() || i1 == Blocks.SNOW.getDefaultState()) && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putBoolean("Hungry", hungry);
        compound.putFloat("ModelSize", modelsize);
        compound.putFloat("ModelSizeBase", modelsizebase);
        compound.putFloat("Fat", fat);
        compound.putBoolean("Cavegirl", cavegirl);
        compound.putBoolean("Evil", evil);
        compound.putInt("Frozen", frozen);
        compound.putInt("WanderState", wanderstate);
        compound.putInt("HouseX", houseX);
        compound.putInt("HouseY", houseY);
        compound.putInt("HouseZ", houseZ);
        compound.putInt("HouseChunk", housechunk);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        hungry = compound.getBoolean("Hungry");
        modelsize = compound.getFloat("ModelSize");
        modelsizebase = compound.getFloat("ModelSizeBase");
        fat = compound.getFloat("Fat");
        frozen = compound.getInt("Frozen");
        cavegirl = compound.getBoolean("Cavegirl");
        evil = compound.getBoolean("Evil");
        wanderstate = compound.getInt("WanderState");
        houseX = compound.getInt("HouseX");
        houseY = compound.getInt("HouseY");
        houseZ = compound.getInt("HouseZ");
        housechunk = compound.getInt("HouseChunk");

        if (wanderstate == 2)
        {
            MoreCreepsReboot.cavemanbuilding = true;
        }

        setCaveTexture();
    }

    public void setCaveTexture()
    {
        if (evil)
        {
            if (cavegirl)
            {
                texture = "morecreeps:textures/entity/cavemanladyevil.png";
            }
            else
            {
                texture = "morecreeps:textures/entity/cavemanevil.png";
            }
        }
        else if (cavegirl)
        {
            texture = "morecreeps:textures/entity/cavemanlady.png";
        }
        else
        {
            texture = "morecreeps:textures/entity/caveman.png";
        }
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(World world, PlayerEntity playerentity)
    {
        SoundEvent s = getLivingSound();

        if (s != null)
        {
            world.playSound(playerentity, this, s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + (1.0F - (modelsizebase - modelsize) * 2.0F));
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        if (evil)
        {
            return SoundsHandler.CAVEMAN_EVIL;
        }

        LivingEntity entityToAttack = this.getAttackTarget();
        
        if (entityToAttack != null)
        {
            if (cavegirl)
            {
                return SoundsHandler.CAVEWOMAN_FREE;
            }
            else
            {
            	return SoundsHandler.CAVEMAN_FREE;
            }
        }

        if (cavegirl)
        {
            if (frozen < 1)
            {
            	return SoundsHandler.CAVEWOMAN_FREE;
            }
            else
            {
            	return SoundsHandler.CAVEWOMAN_FROZEN;
            }
        }

        if (frozen < 1)
        {
        	return SoundsHandler.CAVEMAN_FREE;
        }
        else
        {
            return SoundsHandler.CAVEMAN_FROZEN;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        if (frozen > 0)
        {
            return null;
        }

        if (cavegirl)
        {
        	return SoundsHandler.CAVEWOMAN_HURT;
        }
        else
        {
            return SoundsHandler.CAVEMAN_HURT;
        }
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        if (cavegirl)
        {
            return SoundsHandler.CAVEWOMAN_DEATH;
        }
        else
        {
            return SoundsHandler.CAVEMAN_DEATH;
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, AdvancementManager advancement)
    {
        Object obj = damagesource.getTrueSource();

        if ((obj instanceof RocketEntity) && ((RocketEntity)obj).owner != null)
        {
            obj = ((RocketEntity)obj).owner;
        }

        PlayerEntity player = (PlayerEntity) damagesource.getTrueSource();
        if(player != null)
        {
        	MoreCreepsReboot.cavemancount++;
            if (!((ServerPlayerEntity)player).getAdvancements().getProgress(ModAdvancementList.one_caveman).isDone() == true)
            {
                world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                player.addStat(ModAdvancementList.one_caveman, 1);
                confetti(world);
            }

            if (!((ServerPlayerEntity)player).getAdvancements().getProgress(ModAdvancementList.one_caveman).isDone() == true && MoreCreepsReboot.cavemancount >= 10)
            {
            	world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                player.addStat(ModAdvancementList.ten_caveman, 1);
                confetti(world);
            }

            if (!((ServerPlayerEntity)player).getAdvancements().getProgress(ModAdvancementList.ten_caveman).isDone() && MoreCreepsReboot.cavemancount >= 50)
            {
            	world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                advancement.getAllAdvancements().add(ModAdvancementList.fifty_caveman);
                confetti(world);
            }
        }
        if(!world.isRemote)
        {
            if (rand.nextInt(10) == 0)
            {
                entityDropItem(Items.PORKCHOP, rand.nextInt(3) + 1);
            }

            if (rand.nextInt(10) == 0)
            {
                entityDropItem(ItemList.popsicle, rand.nextInt(3) + 1);
            }

            if (rand.nextInt(8) == 0)
            {
                entityDropItem(ItemList.caveman_club, 1);
            }
        }

        super.onDeath(damagesource);
    }

    public void confetti(World world)
    {
    	MoreCreepsReboot.proxy.confettiA(this, world);
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return !evil ? 180 : 120;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }
}
