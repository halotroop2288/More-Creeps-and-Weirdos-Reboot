package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ZebraEntity extends LivingEntity
{
	ServerPlayerEntity playermp;
	PlayerEntity playerentity;
	World world;
    public int galloptime;
    public boolean tamed;
    public PlayerEntity owner;
    public boolean used;
    public String basetexture;
    protected double attackrange;
    protected int attack;

    public int spittimer;
    public int tamedcookies;
    public int basehealth;
    public double floatcycle;
    public int floatdir;
    public double floatmaxcycle;
    public float modelsize;
    public String name;
    public String texture;
    public double moveSpeed;
    public double health;
    static final String Names[] =
    {
        "Stanley", "Cid", "Hunchy", "The Heat", "Herman the Hump", "Dr. Hump", "Little Lousie", "Spoony G", "Mixmaster C", "The Maestro",
        "Duncan the Dude", "Charlie Camel", "Chip", "Charles Angstrom III", "Mr. Charles", "Cranky Carl", "Carl the Rooster", "Tiny the Peach", "Desert Dan", "Dungby",
        "Doofus"
    };
    static final String camelTextures[] =
    {
        "/mob/creeps/zebra.png"
    };

    public ZebraEntity(World world)
    {
        super(null, world);
        basetexture = camelTextures[rand.nextInt(camelTextures.length)];
        texture = basetexture;
        health = 25;
        basehealth = (int) health;
        attack = 2;
        moveSpeed = 0.65F;
        floatdir = 1;
        floatcycle = 0.0D;
        floatmaxcycle = 0.10499999672174454D;
        name = "";
        tamed = false;
        tamedcookies = rand.nextInt(7) + 1;
        modelsize = 2.0F;
        setSize(width * modelsize, getHeight() * modelsize);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIBreakDoor(this));
        tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.061D));
        tasks.addTask(5, new EntityAIWander(this, 0.25D));
        tasks.addTask(6, new EntityAIWatchClosest(this, PlayerEntity.class, 8F));
        tasks.addTask(7, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }
    
    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(moveSpeed);
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public AnimalEntity spawnBabyAnimal(AnimalEntity entityanimal)
    {
        return new ZebraEntity(world);
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(int i, int j, int k)
    {
        if (world.getBlockState(new BlockPos(i, j - 1, k)).getBlock() == Blocks.leaves || world.getBlockState(new BlockPos(i, j - 1, k)).getBlock() == Blocks.grass)
        {
            return 10F;
        }
        else
        {
            return -(float)j;
        }
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        if (getRidingEntity() instanceof PlayerEntity)
        {
            return (double)(getYOffset() + 1.1F);
        }
        else
        {
            return (double)getYOffset();
        }
    }

    public void updateRiderPosition()
    {
        if (getRidingEntity() == null)
        {
            return;
        }

        if (getRidingEntity() instanceof PlayerEntity)
        {
            getRidingEntity().setPosition(posX, (posY + 3.0999999046325684D) - (double)((2.0F - modelsize) * 1.1F) - floatcycle, posZ);
            return;
        }
        else
        {
            return;
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        if (world.getDifficulty().getId() > 0)
        {
            float f = getBrightness();

            if (f < 0.0F)
            {
                PlayerEntity playerentity = world.getClosestPlayer(this, attackrange);

                if (playerentity != null)
                {
                    return playerentity;
                }
            }

            if (rand.nextInt(10) == 0)
            {
                LivingEntity entityliving = getClosestTarget(this, 15D);
                return entityliving;
            }
        }

        return null;
    }

    public LivingEntity getClosestTarget(Entity entity, double d)
    {
        double d1 = -1D;
        LivingEntity entityliving = null;

        for (int i = 0; i < world.loadedEntityList.size(); i++)
        {
            Entity entity1 = (Entity)world.loadedEntityList.get(i);

            if (!(entity1 instanceof LivingEntity) || entity1 == entity || entity1 == entity.getRidingEntity() || entity1 == entity.getRidingEntity() || (entity1 instanceof PlayerEntity) || (entity1 instanceof MobEntity) || (entity1 instanceof AnimalEntity))
            {
                continue;
            }

            double d2 = entity1.getDistanceSq(entity.posX, entity.posY, entity.posZ);

            if ((d < 0.0D || d2 < d * d) && (d1 == -1D || d2 < d1) && ((LivingEntity)entity1).canEntityBeSeen(entity))
            {
                d1 = d2;
                entityliving = (LivingEntity)entity1;
            }
        }

        return entityliving;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getTrueSource();

        if (entity != null && (entity instanceof PlayerEntity) && tamed)
        {
            this.setAttackTarget(null);
            return false;
        }

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
        {
            if (getRidingEntity() == entity)
            {
                return true;
            }

            if (entity != this && world.getDifficulty().getId() > 0)
            {
                this.setAttackTarget((LivingEntity) entity);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return !tamed;
    }

    protected void updateAITick()
    {
        ignoreFrustumCheck = true;
        moveSpeed = 0.45F;

        if (getRidingEntity() != null && (getRidingEntity() instanceof PlayerEntity))
        {
            moveForward = 0.0F;
            moveStrafing = 0.0F;
            moveSpeed = 1.95F;
            getRidingEntity().lastTickPosY = 0.0D;
            prevRotationYaw = rotationYaw = getRidingEntity().rotationYaw;
            prevRotationPitch = rotationPitch = 0.0F;
            PlayerEntity playerentity = (PlayerEntity)getRidingEntity();
            float f = 1.0F;

            if (playerentity.getAIMoveSpeed() > 0.01F && playerentity.getAIMoveSpeed() < 10F)
            {
                f = playerentity.getAIMoveSpeed();
            }

            moveStrafing = (float) ((playerentity.moveStrafing / f) * moveSpeed * 4.95F);
            moveForward = (float) ((playerentity.moveForward / f) * moveSpeed * 4.95F);

            if (onGround && (moveStrafing != 0.0F || moveForward != 0.0F))
            {
                moveVertical += 0.06100039929151535D;
            }

            if (moveStrafing != 0.0F || moveForward != 0.0F)
            {
                if (floatdir > 0)
                {
                    floatcycle += 0.035999998450279236D;

                    if (floatcycle > floatmaxcycle)
                    {
                        floatdir = floatdir * -1;
                        fallDistance += -1.5F;
                    }
                }
                else
                {
                    floatcycle -= 0.017999999225139618D;

                    if (floatcycle < -floatmaxcycle)
                    {
                        floatdir = floatdir * -1;
                        fallDistance += -1.5F;
                    }
                }
            }

            if (moveStrafing == 0.0F && moveForward == 0.0F)
            {
                isJumping = false;
                galloptime = 0;
            }

            if (moveForward != 0.0F && galloptime++ > 10)
            {
                galloptime = 0;

                if (handleWaterMovement())
                {
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.GIRAFFE_SPLASH, SoundCategory.NEUTRAL, getSoundVolume(), 1.2F);
                }
                else
                {
                	world.playSound(playerentity, this.getPosition(), SoundsHandler.GIRAFFE_GALLOP, SoundCategory.NEUTRAL, getSoundVolume(), 1.2F);
                }
            }

            if (onGround && !isJumping)
            {
                isJumping = Minecraft.getInstance().gameSettings.keyBindJump.isPressed();

                if (isJumping)
                {
                    moveVertical += 0.37000000476837158D;
                }
            }

            if (onGround && isJumping)
            {
                double d = Math.abs(Math.sqrt(moveForward * moveForward + moveStrafing * moveStrafing));

                if (d > 0.13D)
                {
                    double d1 = 0.13D / d;
                    moveForward = (float) (moveForward * d1);
                    moveStrafing = (float) (moveStrafing * d1);
                }

                moveForward *= 2.9500000000000002D;
                moveStrafing *= 2.9500000000000002D;
            }

            return;
        }
        else
        {
            super.updateEntityActionState();
            return;
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(PlayerEntity playerentity)
    {
        ItemStack itemstack = playerentity.inventory.getCurrentItem();
        used = false;

        if (tamed && playerentity.isSneaking() && getRidingEntity() != null)
        {
            this.addPassenger(playerentity);
        }

        if (tamed && playerentity.isSneaking() && getRidingEntity() == null)
        {
        	playerentity.openGui(MoreCreepsReboot.instance, 7, world, (int)this.posX, (int)this.posY, (int)this.posZ);
            return true;
        }

        if (itemstack != null && getRidingEntity() == null && itemstack.getItem() == Items.COOKIE)
        {
        	world.playSound(playerentity, this.getPosition(), SoundsHandler.HOT_DOG_EAT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            used = true;
            health += 10;

            if (health > basehealth)
            {
                health = basehealth;
            }

            tamedcookies--;
            String s = "";

            if (tamedcookies > 1)
            {
                s = "s";
            }

            if (tamedcookies > 0)
            {
            	MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("You need \2476").append(String.valueOf(tamedcookies)).append(" cookie").append(String.valueOf(s)).append(" \247fto tame this speedy Zebra.").toString());
            }

            if (tamedcookies == 0)
            {
                tamed = true;

                if (world.isRemote){
            		if (!Minecraft.getInstance().thePlayer.getStatFileWriter().hasAdvancementUnlocked(ModAdvancementList.zebra))
            		{
                    	confetti();
                    	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                    	playerentity.addStat(ModAdvancementList.zebra, 1);
                		}
                
            	}
            	
            	if (!world.isRemote){
                    if (!playermp.getStatFile().hasAchievementUnlocked(ModAdvancementList.zebra))
                    {
                        confetti();
                        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                        playermp.addStat(ModAdvancementList.zebra, 1);
                    }
            	}

                owner = playerentity;

                if (name.length() < 1)
                {
                    name = Names[rand.nextInt(Names.length)];
                }

                MoreCreepsReboot.proxy.addChatMessage("");
                MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\2476").append(String.valueOf(name)).append(" \247fhas been tamed!").toString());
                world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_LEVEL_UP, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            smoke();
        }

        if (used)
        {
            smoke();
        }

        String s1 = "";

        if (tamedcookies > 1)
        {
            s1 = "s";
        }

        if (!used && !tamed)
        {
        	MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("You need \2476").append(String.valueOf(tamedcookies)).append(" cookie").append(String.valueOf(s1)).append(" \247fto tame this speedy Zebra.").toString());
        }

        if (itemstack == null && tamed && health > 0)
        {
            if (playerentity.getRidingEntity() == null && modelsize > 1.0F)
            {
                rotationYaw = playerentity.rotationYaw;
                rotationPitch = playerentity.rotationPitch;
                playerentity.fallDistance = -5F;
                this.addPassenger(playerentity);

                // Dead code
//                if (this == null)
//                {
//                    double d = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
//                    playerentity.motionX += 1.5D * d;
//                    playerentity.motionZ -= 0.5D;
//                }
            }
            else if (modelsize < 1.0F && tamed)
            {
            	MoreCreepsReboot.proxy.addChatMessage("Your Zebra is too small to ride!");
            }
            else if (playerentity.getRidingEntity() != null)
            {
            	MoreCreepsReboot.proxy.addChatMessage("Unmount all creatures before riding your Zebra");
            }
        }

        return true;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (onGround)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d1 * d1);
            moveForward = (float) ((d / (double)f1) * 0.20000000000000001D * (0.850000011920929D + moveForward * 0.20000000298023224D));
            moveStrafing = (float) ((d1 / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + moveStrafing * 0.20000000298023224D));
            moveVertical = (float) 0.10000000596246449D;
            fallDistance = -25F;
        }

        if ((double)f < 3.1000000000000001D && entity.getBoundingBox().maxY > this.getBoundingBox().minY && entity.getBoundingBox().minY < this.getBoundingBox().maxY)
        {
            //attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack);
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
    	int i = MathHelper.floor(posX);
        int j = MathHelper.floor(this.getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getBlockLightOpacity(new BlockPos(i, j, k));
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        int j1 = world.countEntities(NonSwimmerEntity.class); // TODO Count the number of NonSwimmers around the area ...?
        return (i1 == Blocks.GRASS || i1 == Blocks.DIRT) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL && world.getCollisionBoundingBoxes(this, getBoundingBox()).size() == 0 && world.checkBlockCollision(getBoundingBox()) && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(5) == 0 && l > 7 && j1 < 15;
    }

    public void confetti()
    {
        
        double d = -MathHelper.sin((((PlayerEntity)(playerentity)).rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((((PlayerEntity)(playerentity)).rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(((PlayerEntity)(playerentity)).posX + d * 3D, ((PlayerEntity)(playerentity)).posY - 2D, ((PlayerEntity)(playerentity)).posZ + d1 * 3D, ((PlayerEntity)(playerentity)).rotationYaw, 0.0F);
        world.addEntity(creepsentitytrophy);
    }

    private void smoke()
    {
        for (int i = 0; i < 7; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            double d4 = rand.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.HEART, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d2, d4);
        }

        for (int j = 0; j < 4; j++)
        {
            for (int k = 0; k < 10; k++)
            {
                double d1 = rand.nextGaussian() * 0.02D;
                double d3 = rand.nextGaussian() * 0.02D;
                double d5 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * getHeight()) + (double)j, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d1, d3, d5);
            }
        }
    }

//    /**
//     * (abstract) Protected helper method to write subclass entity data to NBT.
//     */
//    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
//    {
//        super.writeEntityToNBT(nbttagcompound);
//        nbttagcompound.setString("BaseTexture", basetexture);
//        nbttagcompound.setString("ZebraName", name);
//        nbttagcompound.setBoolean("Tamed", tamed);
//        nbttagcompound.setInteger("TamedCookies", tamedcookies);
//        nbttagcompound.setInteger("BaseHealth", basehealth);
//        nbttagcompound.setFloat("ModelSize", modelsize);
//    }
//
//    /**
//     * (abstract) Protected helper method to read subclass entity data from NBT.
//     */
//    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
//    {
//        super.readEntityFromNBT(nbttagcompound);
//        basetexture = nbttagcompound.getString("BaseTexture");
//        name = nbttagcompound.getString("ZebraName");
//        tamed = nbttagcompound.getBoolean("Tamed");
//
//        if (basetexture == null)
//        {
//            basetexture = "/mob/creeps/zebra.png";
//        }
//
//        texture = basetexture;
//        basehealth = nbttagcompound.getInteger("BaseHealth");
//        tamedcookies = nbttagcompound.getInteger("TamedCookies");
//        modelsize = nbttagcompound.getFloat("ModelSize");
//    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound()
    {
        SoundEvent s = getLivingSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (2.0F - modelsize));
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        return SoundsHandler.ENTITY_HORSE_AMBIENT;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.ENTITY_HORSE_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.ENTITY_HORSE_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
        if (rand.nextInt(10) == 0)
        {
            entityDropItem(Items.PORKCHOP, rand.nextInt(3) + 1);
        }

        if (rand.nextInt(2) == 0)
        {
            entityDropItem(ItemList.zebra_hide, 1);
        }

        super.onDeath(damagesource);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 5;
    }

	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandSide getPrimaryHand() {
		// TODO Auto-generated method stub
		return null;
	}
}
