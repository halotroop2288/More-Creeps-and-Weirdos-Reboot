package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import fr.elias.morecreeps.client.particles.CREEPSFxSpit;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.lists.ParticleList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class CamelEntity extends MobEntity
{
    public int galloptime;
    public boolean tamed;
    public String texture;
    public PlayerEntity owner;
    public boolean used;
    public int interest;
    public String basetexture;
    protected double attackrange;
    protected int attack;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();

    public int spittimer;
    public int tamedcookies;
    public int basehealth;
    public float modelsize;
    public String name;
    static final String Names[] =
    {
        "Stanley", "Cid", "Hunchy", "The Heat", "Herman the Hump", "Dr. Hump", "Little Lousie", "Spoony G", "Mixmaster C", "The Maestro",
        "Duncan the Dude", "Charlie Camel", "Chip", "Charles Angstrom III", "Mr. Charles", "Cranky Carl", "Carl the Rooster", "Tiny the Peach", "Desert Dan", "Dungby",
        "Doofus"
    };
    static final String camelTextures[] =
    {
        "/mob/creeps/camel.png", "/mob/creeps/camel.png", "/mob/creeps/camel.png", "/mob/creeps/camel.png", "/mob/creeps/camelwhite.png", "/mob/creeps/camelblack.png", "/mob/creeps/camelbrown.png", "/mob/creeps/camelgrey.png", "/mob/creeps/camel.png", "/mob/creeps/camel.png",
        "/mob/creeps/camel.png", "/mob/creeps/camel.png", "/mob/creeps/camelwhite.png"
    };

    public CamelEntity(World world)
    {
        super(null, world);
        basetexture = camelTextures[rand.nextInt(camelTextures.length)];
        texture = basetexture;
        setSize(width * 1.5F, height * 4F);
        attack = 2;
        basehealth = 30;
        attackrange = 16D;
        interest = 0;
        spittimer = 30;
        name = "";
        tamed = false;
        tamedcookies = rand.nextInt(7) + 1;
        modelsize = 1.75F;
    }
    
    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
    	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D);
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        if (getPassengers() != null)
        {
            return false;
        }
        else
        {
            return super.isEntityInsideOpaqueBlock();
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    //getBlockPathHeight in 1.8
    public float func_180484_a(BlockPos bp)
    {
        if (world.getBlockState(bp.down()).getBlock() == Blocks.SAND || world.getBlockState(bp.down()).getBlock() == Blocks.GRAVEL)
        {
            return 10F;
        }
        else
        {
            return -(float)bp.getY();
        }
    }


    public void updatePassenger(PlayerEntity passenger1, CamelJockeyEntity passenger2)
    {
        if (this.getPassengers() == null)
        {
            return;
        }

        if (this.getPassengers() instanceof PlayerEntity)
        {
            passenger1.setPosition(posX, (posY + 4.5D) - (double)((1.75F - modelsize) * 2.0F), posZ);
            return;
        }

        if (this.getPassengers() instanceof CamelJockeyEntity)
        {
            passenger2.setPosition(posX, (posY + 3.1500000953674316D) - (double)((1.75F - modelsize) * 2.0F), posZ);
        	return;
        }
        else
        {
            return;
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return !tamed;
    }

    public void onLivingUpdate()
    {
        if (modelsize > 1.75F)
        {
            ignoreFrustumCheck = true;
        }
    }
        public void moveEntityWithHeading(float moveZ, float moveX)
        {
            if (this.getPassengers() != null && this.getPassengers() instanceof LivingEntity)
            {
                this.prevRotationYaw = this.rotationYaw = this.getRidingEntity().rotationYaw;
                this.rotationPitch = this.getRidingEntity().rotationPitch * 0.5F;
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
                moveZ = ((LivingEntity)this.getPassengers()).moveStrafing * 0.5F;
                moveX = ((LivingEntity)this.getPassengers()).moveForward;

                if (moveX <= 0.0F)
                {
                    moveX *= 0.25F;
                }

                if (this.onGround)
                {
                    moveZ = 0.0F;
                    moveZ = 0.0F;
                }

                if (this.onGround)
                {
                    this.moveVertical = (float) 0.43;

                    if (this.isPotionActive(Effects.JUMP_BOOST))
                    {
                        this.moveVertical += (double)((float)(this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
                    }

                    this.setJumping(true);
                    this.isAirBorne = true;

                    if (moveX > 0.0F)
                    {
                        float f2 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
                        float f3 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
                        this.moveForward += (double)(-0.4F * f2);
                        this.moveStrafing += (double)(0.4F * f3);
                    }

                    ForgeHooks.onLivingJump(this);
                }

                this.stepHeight = 1.0F;
                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

                if (!this.world.isRemote)
                {
                    this.setAIMoveSpeed((float)this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
                    super.setMotion(moveX, 0, moveZ); // If this is wrong, it should only cause wonky movement
                }

                

                this.prevLimbSwingAmount = this.limbSwingAmount;
                double d1 = this.posX - this.prevPosX;
                double d0 = this.posZ - this.prevPosZ;
                float f4 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

                if (f4 > 1.0F)
                {
                    f4 = 1.0F;
                }

                this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
                this.limbSwing += this.limbSwingAmount;
            }
            else
            {
                this.stepHeight = 0.5F;
                this.jumpMovementFactor = 0.02F;
                super.setMotion(moveX, 0, moveZ);
            }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        if (world.getDifficulty().getId() > 0 && (getRidingEntity() instanceof CamelJockeyEntity))
        {
            PlayerEntity playerentity = world.getClosestPlayer(this, attackrange);

            if (playerentity != null)
            {
                return playerentity;
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

            if (!(entity1 instanceof LivingEntity) || entity1 == entity || entity1 == entity.getPassengers() || entity1 == entity.getRidingEntity() || (entity1 instanceof PlayerEntity) || (entity1 instanceof MobEntity) || (entity1 instanceof AnimalEntity))
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
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
        {
            if (getPassengers() == entity || getRidingEntity() == entity)
            {
                return true;
            }

            if (entity != this && world.getDifficulty().getId() > 0)
            {
                this.attackEntity(entity, 1);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    
    protected void attackEntity(Entity entity, float f, PlayerEntity player)
    {
        if (onGround)
        {
            double d = entity.posX - posX;
            double d2 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d2 * d2);
            moveForward= (float) ((d / (double)f1) * 0.20000000000000001D * (0.850000011920929D + moveForward * 0.20000000298023224D));
            moveStrafing = (float) ((d2 / (double)f1) * 0.20000000000000001D * (0.80000001192092896D + moveStrafing * 0.20000000298023224D));
            moveVertical = (float) 0.10000000596246449D;
            fallDistance = -25F;
        }

        if ((double)f > 2D && (double)f < 7D && (entity instanceof PlayerEntity) && spittimer-- < 0 && (getPassengers() instanceof CamelJockeyEntity))
        {
            world.playSound(player, this.getPosition(), SoundsHandler.CAMEL_SPITS, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            spittimer = 30;
            faceEntity(entity, 360F, 0.0F);
            double d1 = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
            double d3 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);

            for (int i = 0; i < 40; i++)
            {
                CREEPSFxSpit creepsfxspit = new CREEPSFxSpit(world, posX + d1 * (double)(4.5F - (2.0F - modelsize)), (posY + 2.4000000953674316D) - (double)(2.0F - modelsize), posZ + d3 * (double)(4.5F - (2.0F - modelsize)), ParticleList.CREEPS_BUBBLE);
                creepsfxspit.renderDistanceWeight = 10D;
                Minecraft.gameRenderer.addEffect(creepsfxspit);
            }

            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1);
        }

        if ((double)f < 3.2999999999999998D - (2D - (double)modelsize) && entity.getBoundingBox().maxY > entity.getBoundingBox().minY && entity.getBoundingBox().minY < entity.getBoundingBox().maxY)
        {
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(PlayerEntity playerentity)
    {
    	
    	
        ItemStack itemstack = playerentity.inventory.getCurrentItem();
        used = false;

        if (tamed && playerentity.isSneaking())
        {
            playerentity.openGui(MoreCreepsReboot.instance, 1, world, (int)this.posX, (int)this.posY, (int)this.posZ);
            return true;
        }

        if (itemstack != null && getPassengers() == null && itemstack.getItem() == Items.COOKIE)
        {
            if (itemstack.getCount() - 1 == 0)
            {
                playerentity.inventory.setInventorySlotContents(playerentity.inventory.currentItem, null);
            }
            else
            {
                itemstack.setCount(itemstack.getCount() - 1);
            }

            world.playSound(playerentity, this.getPosition(), SoundsHandler.HOT_DOG_EAT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            used = true;
            int health = basehealth;
            health += 10;
            this.setHealth(health);

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
            	MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("You need \2476").append(String.valueOf(tamedcookies)).append(" cookie").append(String.valueOf(s)).append(" \247fto tame this lovely camel.").toString());
            }

            if (tamedcookies == 0)
            {
                tamed = true;

                
                confetti(playerentity);
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                playerentity.addStat(ModAdvancementList.camel, 1);
                

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
        	MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("You need \2476").append(String.valueOf(tamedcookies)).append(" cookie").append(String.valueOf(s1)).append(" \247fto tame this lovely camel.").toString());
        }

        if (itemstack == null && tamed && this.getHealth() > 0)
        {
            if (playerentity.getPassengers() == null && modelsize > 1.0F)
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
            	MoreCreepsReboot.proxy.addChatMessage("Your Camel is too small to ride!");
            }
            else
            {
            	MoreCreepsReboot.proxy.addChatMessage("Unmount all creatures before riding your Camel");
            }
        }

        return true;
    }

    public void confetti(PlayerEntity player)
    {
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
        if(!world.isRemote)
        {
            world.addEntity(creepsentitytrophy);
        }
    }

    private void smoke()
    {
        for (int i = 0; i < 7; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            double d4 = rand.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.HEART, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d2, d4);
        }

        for (int j = 0; j < 4; j++)
        {
            for (int k = 0; k < 10; k++)
            {
                double d1 = rand.nextGaussian() * 0.02D;
                double d3 = rand.nextGaussian() * 0.02D;
                double d5 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height) + (double)j, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d1, d3, d5);
            }
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int l = world.getLight(getPosition());
        int j1 = world.countEntities(CamelEntity.class);
        Block i1 = world.getBlockState(new BlockPos(getPosition())).getBlock();
        return (i1 == Blocks.SAND || i1 == Blocks.DIRT || i1 == Blocks.GRAVEL) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_CARPET && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 &&world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && world.checkBlockCollision(getBoundingBox()) && world.canBlockSeeSky(getPosition()) && l > 6 && rand.nextInt(40) == 0 && j1 < 25;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putInt("SpitTimer", spittimer);
        compound.putString("BaseTexture", basetexture);
        compound.putString("CamelName", name);
        compound.putBoolean("Tamed", tamed);
        compound.putInt("TamedCookies", tamedcookies);
        compound.putInt("BaseHealth", basehealth);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        spittimer = compound.getInt("SpitTimer");
        basetexture = compound.getString("BaseTexture");
        name = compound.getString("CamelName");
        tamed = compound.getBoolean("Tamed");

        if (basetexture == null)
        {
            basetexture = "/mob/creeps/camel.png";
        }

        texture = basetexture;
        basehealth = compound.getInt("BaseHealth");
        tamedcookies = compound.getInt("TamedCookies");
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(PlayerEntity playerentity)
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
        return SoundsHandler.CAMEL;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.CAMEL_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.CAMEL_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
        if (tamed)
        {
            return;
        }

        if (rand.nextInt(10) == 0)
        {
            entityDropItem(Items.PORKCHOP, rand.nextInt(3) + 1);
        }

        if (rand.nextInt(10) == 0)
        {
            entityDropItem(Items.SUGAR_CANE, rand.nextInt(3) + 1);
        }

        super.onDeath(damagesource);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 4;
    }
}
