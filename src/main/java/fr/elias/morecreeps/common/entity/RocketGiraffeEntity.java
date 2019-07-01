package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class RocketGiraffeEntity extends CreatureEntity
{
	PlayerEntity playerentity;
	World world;
	ServerPlayerEntity playermp;
    private boolean foundplayer;
    protected Entity playerToAttack;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();
    public double motionX = getMotion().x;
    public double motionY = getMotion().y;
    public double motionZ = getMotion().z;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    public boolean used;
    public boolean tamed;
    public int basehealth;
    public int tamedfood;
    public int attempts;
    public double dist;
    public double prevdist;
    public int facetime;
    public String basetexture;
    public int rockettime;
    public int rocketcount;
    public int galloptime;
    public float modelsize;
    public double floatcycle;
    public int floatdir;
    public double floatmaxcycle;
    public String name;
    public String texture;
    public double moveSpeed;
    public double health;
    static final String Names[] =
    {
        "Rory",
        "Stan",
        "Clarence",
        "FirePower",
        "Lightning",
        "Rocket Jockey",
        "Rocket Ralph",
        "Tim"
        // TODO add more fun names
    };

    public RocketGiraffeEntity(World world)
    {
        super(null, world);
        basetexture = "/mob/creeps/rocketgiraffe.png";
        texture = basetexture;
        moveSpeed = 0.65F;
        basehealth = rand.nextInt(50) + 30;
        health = basehealth;
        hasAttacked = false;
        foundplayer = false;
        // setSize(1.5F, 4F);
        tamedfood = rand.nextInt(8) + 5;
        rockettime = rand.nextInt(10) + 5;
        tamed = false;
        name = "";
        modelsize = 1.0F;
        floatdir = 1;
        floatcycle = 0.0D;
        floatmaxcycle = 0.10499999672174454D;
        // tasks.addTask(0, new EntityAISwimming(this));
        // tasks.addTask(1, new EntityAIBreakDoor(this));
        // tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.061D));
        // tasks.addTask(5, new EntityAIWander(this, 0.25D));
        // tasks.addTask(6, new EntityAIWatchClosest(this, PlayerEntity.class, 8F));
        // tasks.addTask(7, new EntityAILookIdle(this));
        // targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
    }
    
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(moveSpeed);
    }

    

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putInt("BaseHealth", basehealth);
        compound.putInt("Attempts", attempts);
        compound.putBoolean("Tamed", tamed);
        compound.putBoolean("FoundPlayer", foundplayer);
        compound.putInt("TamedFood", tamedfood);
        compound.putString("BaseTexture", basetexture);
        compound.putString("Name", name);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        basehealth = compound.getInt("BaseHealth");
        attempts = compound.getInt("Attempts");
        tamed = compound.getBoolean("Tamed");
        foundplayer = compound.getBoolean("FoundPlayer");
        tamedfood = compound.getInt("TamedFood");
        basetexture = compound.getString("BaseTexture");
        name = compound.getString("Name");
        texture = basetexture;
        modelsize = compound.getFloat("ModelSize");
    }

    protected void updateAITick()
    {
        PlayerEntity player = Minecraft.getInstance().player;

        if (modelsize > 1.0F)
        {
            ignoreFrustumCheck = true;
        }

        moveSpeed = 0.35F;

        if (getPassengers() != null && (getPassengers() instanceof PlayerEntity))
        {
            moveForward = 0.0F;
            moveStrafing = 0.0F;
            moveSpeed = 1.95F;
            getRidingEntity().lastTickPosY = 0.0D;
            prevRotationYaw = rotationYaw = getRidingEntity().rotationYaw;
            prevRotationPitch = rotationPitch = 0.0F;
            PlayerEntity playerentity = (PlayerEntity)getPassengers();
            float f = 1.0F;

            if (playerentity.getAIMoveSpeed() > 0.01F && playerentity.getAIMoveSpeed() < 10F)
            {
                f = playerentity.getAIMoveSpeed();
            }

            moveStrafing = (float) ((playerentity.moveStrafing / f) * moveSpeed * 1.95F);
            moveForward = (float) ((playerentity.moveForward / f) * moveSpeed * 1.95F);

            if (onGround && (moveStrafing != 0.0F || moveForward != 0.0F))
            {
                motionY += 0.16100040078163147D;
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
                    world.playSound(player, this.getPosition(), SoundsHandler.GIRAFFE_SPLASH, SoundCategory.NEUTRAL, getSoundVolume(), 1.0F);
                }
                else
                {
                    world.playSound(player, this.getPosition(), SoundsHandler.GIRAFFE_GALLOP, SoundCategory.NEUTRAL, getSoundVolume(), 1.0F);
                }
            }

            if (onGround && !isJumping)
            {
                isJumping = Minecraft.getInstance().gameSettings.keyBindJump.isPressed();

                if (isJumping)
                {
                    motionY += 0.38999998569488525D;
                }
            }

            if (onGround && isJumping)
            {
                double d = Math.abs(Math.sqrt(motionX * motionX + motionZ * motionZ));

                if (d > 0.13D)
                {
                    double d1 = 0.13D / d;
                    motionX = motionX * d1;
                    motionZ = motionZ * d1;
                }

                motionX *= 5.9500000000000002D;
                motionZ *= 5.9500000000000002D;
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
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getTrueSource();

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
        {
            if (getPassengers() == entity || getRidingEntity() == entity)
            {
                return true;
            }

            if (entity != this && !(entity instanceof RocketEntity))
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
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        this.setAttackTarget(null);

        if (!(entity instanceof PlayerEntity))
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d1 * d1);
            motionX = (d / (double)f1) * 0.40000000000000002D * 0.10000000192092896D + motionX * 0.18000000098023225D;
            motionZ = (d1 / (double)f1) * 0.40000000000000002D * 0.14000000192092896D + motionZ * 0.18000000098023225D;

            if ((double)f < 2D - (2D - (double)modelsize) && entity.getBoundingBox().maxY > this.getBoundingBox().minY
                    && entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                // attackTime = 10;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
            }

            super.attackEntityAsMob(entity);
        }
    }

    public void updateRiderPosition() {
        if (getPassengers() == null) {
            return;
        }

        double d = Math.cos(((double) rotationYaw * Math.PI) / 180D) * 0.20000000000000001D;
        double d1 = Math.sin(((double) rotationYaw * Math.PI) / 180D) * 0.20000000000000001D;
        float f = 3.35F - (1.0F - modelsize) * 2.0F;

        if (modelsize > 1.0F) {
            f *= 1.1F;
        }

        getRidingEntity().setPosition(posX + d, (posY + (double) f) - floatcycle, posZ + d1);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets
     * into the saddle on a pig.
     */
    public boolean interact(PlayerEntity playerentity)
    {
        ItemStack itemstack = playerentity.inventory.getCurrentItem();
        used = false;

        if (itemstack == null && tamed) {
            if (playerentity.getPassengers() == null && modelsize > 0.5F) {
                rotationYaw = playerentity.rotationYaw;
                rotationPitch = playerentity.rotationPitch;
                playerentity.fallDistance = -5F;
                this.addPassenger(playerentity);;

                // Dead code
                // if (this == null)
                // {
                // double d = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
                // playerentity.motionX += 1.5D * d;
                // playerentity.motionZ -= 0.5D;
                // }
            } else if (modelsize < 0.5F && tamed) {
                MoreCreepsReboot.proxy.addChatMessage("Your Rocket Giraffe is too small to ride!");
            } else {
                MoreCreepsReboot.proxy.addChatMessage("Unmount all creatures before riding your Rocket Giraffe.");
            }
        }

        if (itemstack != null && getPassengers() == null && itemstack.getItem() == Items.COOKIE) {
            used = true;
            tamedfood--;
            String s = "";

            if (tamedfood > 1) {
                s = "s";
            }

            if (tamedfood > 0) {
                MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("You need \2476")
                        .append(String.valueOf(tamedfood)).append(" cookie").append(String.valueOf(s))
                        .append(" \247fto tame this Rocket Giraffe.").toString());
            }

            if (itemstack.getCount() - 1 == 0) {
                playerentity.inventory.setInventorySlotContents(playerentity.inventory.currentItem, null);
            } else {
                itemstack.setCount(itemstack.getCount() - 1);
            }

            world.playSound(playerentity, this.getPosition(), SoundsHandler.GIRAFFE_CHEW, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

            // for (int i = 0; i < 35; i++) {
            //     double d2 = -MathHelper.sin((rotationYaw * (float) Math.PI) / 180F);
            //     double d4 = MathHelper.cos((rotationYaw * (float) Math.PI) / 180F);
            //     // CREEPSFxDirt creepsfxdirt = new CREEPSFxDirt(world, posX + d2 * 0.40000000596046448D, posY + 4.5D, posZ + d4 * 0.40000000596046448D, Item.getItemById(12));
            //     // creepsfxdirt.renderDistanceWeight = 6D;
            //     // creepsfxdirt.setParticleTextureIndex(12);
            //     // Minecraft.getInstance().gameRenderer.addEffect(creepsfxdirt);
            // }

            if (tamedfood < 1) {
                if (world.isRemote)
                {
                    // if (!Minecraft.getInstance().player.getStatFileWriter().hasAchievementUnlocked(MoreCreepsReboot.achieverocketgiraffe)) {
                        // confetti();
                        // world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                        // playerentity.addStat(MoreCreepsReboot.achieverocketgiraffe, 1);
                    // }
                }

                if (!world.isRemote)
                {
                    // if (!playermp.getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achieverocketgiraffe)) {
                    //     confetti();
                    //     world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                    //     playermp.addStat(MoreCreepsReboot.achieverocketgiraffe, 1);
                    // }
                }

                smoke();
                world.playSound(playerentity, this.getPosition(), SoundsHandler.GIRAFFE_TAMED, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                tamed = true;

                if (name.length() < 1) {
                    name = Names[rand.nextInt(Names.length)];
                }

                MoreCreepsReboot.proxy.addChatMessage("");
                MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\2476").append(String.valueOf(name))
                        .append(" \247fhas been tamed!").toString());
                health = basehealth;
                basetexture = "/mob/creeps/rocketgiraffetamed.png";
                texture = basetexture;
            }
        }

        String s1 = "";

        if (tamedfood > 1) {
            s1 = "s";
        }

        if (!used && !tamed) {
            MoreCreepsReboot.proxy.addChatMessage(
                    (new StringBuilder()).append("You need \2476").append(String.valueOf(tamedfood)).append(" cookie")
                            .append(String.valueOf(s1)).append(" \247fto tame this Rocket Giraffe.").toString());
        }

        if (itemstack != null && getPassengers() != null && (getPassengers() instanceof LivingEntity)
                && itemstack.getItem() == ItemList.rocket) {
            if (itemstack.getCount() - 1 == 0) {
                playerentity.inventory.setInventorySlotContents(playerentity.inventory.currentItem, null);
            } else {
                itemstack.setCount(itemstack.getCount() - 1);
            }

            // double d1 = -MathHelper.sin((playerentity.rotationYaw * (float) Math.PI) / 180F);
            // double d3 = MathHelper.cos((playerentity.rotationYaw * (float) Math.PI) / 180F);
            // double d5 = 0.0D;
            // double d6 = 0.0D;
            // double d7 = 0.012999999999999999D;
            // double d8 = 4D;
            world.playSound(playerentity, this.getPosition(), SoundsHandler.ROCKET_FIRE, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            RocketEntity creepsentityrocket = new RocketEntity(world, playerentity, 0.0F);

            if (creepsentityrocket != null) {
                world.addEntity(creepsentityrocket);
            }
        }

        return true;
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset() {
        if (getRidingEntity() instanceof PlayerEntity) {
            return (double) (this.getYOffset() - 1.1F);
        } else {
            return (double) this.getYOffset();
        }
    }

    private void smoke() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                double d = rand.nextGaussian() * 0.059999999999999998D;
                double d1 = rand.nextGaussian() * 0.059999999999999998D;
                double d2 = rand.nextGaussian() * 0.059999999999999998D;
                world.addParticle(ParticleTypes.SMOKE,
                    (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, posY + (double) (rand.nextFloat() * height) + (double) i, (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
            }
        }
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playAmbientSound()
    {
        PlayerEntity player = Minecraft.getInstance().player;
        SoundEvent s = getAmbientSound();

        if (s != null) {
            world.playSound(player, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getAmbientSound()
    {
        if (rand.nextInt(10) == 0)
        {
            return SoundsHandler.GIRAFFE;
        } else {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.GIRAFFE_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.GIRAFFE_DEATH;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this
     * entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(this.getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getLight(new BlockPos(i, j, k));
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.SNOW && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
                // && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.checkBlockCollision(getBoundingBox()) && world.canBlockSeeSky(new BlockPos(i, j, k))
                && rand.nextInt(15) == 0 && l > 8;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

   /**
    * Makes the entity despawn if requirements are reached
    */
    @Override
    protected void checkDespawn()
    {
        if (!tamed)
        {

            if (!this.isNoDespawnRequired() && !this.func_213392_I())
            {
            Entity entity = this.world.getClosestPlayer(this, -1.0D);
            net.minecraftforge.eventbus.api.Event.Result result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this);
            if (result == net.minecraftforge.eventbus.api.Event.Result.DENY) {
                idleTime = 0;
                entity = null;
            } else if (result == net.minecraftforge.eventbus.api.Event.Result.ALLOW) {
                this.remove();
                entity = null;
            }
            if (entity != null)
            {
                double d0 = entity.getDistanceSq(this);
                if (d0 > 16384.0D && this.canDespawn(d0))
                {
                    this.remove();
                }
    
                if (this.idleTime > 600 && this.rand.nextInt(800) == 0 && d0 > 1024.0D && this.canDespawn(d0))
                {
                    this.remove();
                }
                else if (d0 < 1024.0D)
                {
                    this.idleTime = 0;
                }
            }
    
            }
            else
            {
           this.idleTime = 0;
            }
        }
     }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource)
    {
        smoke();

        if (rand.nextInt(10) == 0)
        {
            entityDropItem(ItemList.rocket, rand.nextInt(5) + 1);
        }

        super.onDeath(damagesource);
    }

    public void confetti()
    {
        double d = -MathHelper.sin((((PlayerEntity)(playerentity)).rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((((PlayerEntity)(playerentity)).rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(((PlayerEntity)(playerentity)).posX + d * 3D, ((PlayerEntity)(playerentity)).posY - 2D, ((PlayerEntity)(playerentity)).posZ + d1 * 3D, ((PlayerEntity)(playerentity)).rotationYaw, 0.0F);
        world.addEntity(creepsentitytrophy);
    }
}
