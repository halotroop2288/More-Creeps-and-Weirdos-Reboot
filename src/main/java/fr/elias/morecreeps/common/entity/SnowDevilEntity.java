package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.world.World;
import net.minecraft.world.Explosion.Mode;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class SnowDevilEntity extends MobEntity
{
	PlayerEntity playerentity;
	ServerPlayerEntity playermp;
	World world;
    public boolean rideable;
    public int interest;
    // private boolean primed;
    public boolean tamed;
    public int basehealth;
    // private float distance;
    public int armor;
    public String basetexture;
    public boolean used;
    public float modelsize;
    public String name;
    public double moveSpeed;
    public double attackStrength;
    public double health;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();
    public double motionX = getMotion().x;
    public double motionY = getMotion().y;
    public double motionZ = getMotion().z;
    static final String Names[] =
    {
        "Satan", "The Butcher", "Killer", "Tad", "Death Spanker", "Death Toll", "Bruiser", "Bones", "The Devil", "Little Devil",
        "Skinny", "Death to All", "I Will Hurt You", "Pierre", "Bonecruncher", "Bone Breaker", "Blood 'N Guts", "Kill Kill", "Murder", "The Juicer",
        "Scream", "Bloody Buddy", "Sawblade", "Ripper", "Razor", "Valley Strangler", "Choppy Joe", "Wiconsin Shredder", "Urinal", "Johnny Choke",
        "Annihilation", "Bloodshed", "Destructo", "Rub Out", "Massacre", "Felony", "The Mangler", "Destroyer", "The Marauder", "Wreck",
        "Vaporizer", "Wasteland", "Demolition Duo", "Two Knocks", "Double Trouble", "Thing One & Thing Two", "Wipeout", "Devil Duo", "Two Shot", "Misunderstood",
        "Twice As Nice"
    };
    static final String snowTextures[] =
    {
        "/mob/creeps/snowdevil1.png", "/mob/creeps/snowdevil2.png"
    };
    
    public String texture;

    public SnowDevilEntity(World world)
    {
        super(null, world);
        // primed = false;
        basetexture = snowTextures[rand.nextInt(snowTextures.length)];
        texture = basetexture;
        setSize(width * 1.6F, height * 1.6F);
        height = 2.0F;
        width = 2.0F;
        moveSpeed = 0.6F;
        rideable = false;
        basehealth = rand.nextInt(50) + 15;
        health = basehealth;
        attackStrength = 3;
        tamed = false;
        name = "";
        modelsize = 1.0F;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // tasks.addTask(0, new EntityAISwimming(this));
        // tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.35D));
        // tasks.addTask(5, new EntityAIWander(this, 0.35D));
        // tasks.addTask(6, new EntityAIWatchClosest(this, PlayerEntity.class, 16F));
        // tasks.addTask(7, new EntityAILookIdle(this));
        // targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }
    
    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(moveSpeed);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(attackStrength);
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        PlayerEntity playerentity = world.getClosestPlayer(this, 15D);

        if (playerentity != null)
        {
            if (!tamed)
            {
                return playerentity;
            }

            if (rand.nextInt(10) == 0)
            {
                return playerentity;
            }
        }

        if (rand.nextInt(6) == 0)
        {
            LivingEntity entityliving = getClosestTarget(this, 10D);
            return entityliving;
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public LivingEntity getClosestTarget(Entity entity, double d)
    {
        List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(20D, 20D, 20D));

        for (int i = 0; i < list.size(); i++) {
            Entity entity1 = (Entity) list.get(i);

            if (!(entity1 instanceof CreatureEntity)) {
                continue;
            }

            CreatureEntity creatureentity = (CreatureEntity) entity1;

            if (creatureentity.getAttackTarget() instanceof PlayerEntity) {
                return creatureentity;
            }
        }

        return null;
    }

    /**
     * Basic mob attack. Default to touch of death in CreatureEntity. Overridden by
     * each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f) {
        if (onGround && !tamed) {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d1 * d1);
            motionX = (d / (double) f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
            motionZ = (d1 / (double) f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
            motionY = 0.40000000596046448D;
        } else if (tamed) {
            super.attackEntityAsMob(entity);
        }

        if ((getAttackTarget() instanceof PlayerEntity) && tamed) {
            this.setAttackTarget(null);
            super.attackEntityAsMob(entity);
        } else if ((getAttackTarget() instanceof SnowDevilEntity) && tamed) {
            this.setAttackTarget(null);
        } else {
            super.attackEntityAsMob(entity);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i) {
        Entity entity = damagesource.getTrueSource();

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
            if (getPassengers() == entity || getRidingEntity() == entity) {
                return true;
            }

            if (entity != this) {
                this.setAttackTarget((LivingEntity) entity);
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditonal(CompoundNBT nbttagcompound)
    {
        super.writeAdditional(nbttagcompound);
        nbttagcompound.putInt("Interest", interest);
        nbttagcompound.putInt("BaseHealth", basehealth);
        nbttagcompound.putBoolean("Tamed", tamed);
        nbttagcompound.putString("Name", name);
        nbttagcompound.putString("BaseTexture", basetexture);
        nbttagcompound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT nbttagcompound) // This shit is so repetetive. I wish I had a script to replace all of this for me. It could definitely work.
    {
        super.readAdditional(nbttagcompound);
        interest = nbttagcompound.getInt("Interest");
        basehealth = nbttagcompound.getInt("BaseHealth");
        tamed = nbttagcompound.getBoolean("Tamed");
        name = nbttagcompound.getString("Name");
        basetexture = nbttagcompound.getString("BaseTexture");
        modelsize = nbttagcompound.getFloat("ModelSize");
        texture = basetexture;
    }

    // Never used ?
    // private void explode()
    // {
    //     float f = 2.0F;
    //     world.createExplosion(null, posX, posY, posZ, f, true, Mode.NONE);
    // }

    private void smoke()
    {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width,
                        posY + (double) (rand.nextFloat() * height) + (double) i,
                        (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
            }
        }
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
        Block j1 = world.getBlockState(new BlockPos(i, j, k)).getBlock();
        return (i1 == Blocks.SNOW || j1 == Blocks.SNOW) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_PLANKS
                && i1 != Blocks.WHITE_WOOL && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.checkBlockCollision(getBoundingBox()) && world.canBlockSeeSky(new BlockPos(i, j, k))
                && rand.nextInt(5) == 0 && l > 6;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 2;
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
        	playerentity.openGui(MoreCreepsReboot.instance, 7, world, (int)this.posX, (int)this.posY, (int)this.posZ);
        }

        if (itemstack != null)
        {
            if (tamed && texture.length() == 2222)
            {
                armor = Item.getIdFromItem(itemstack.getItem());
                smoke();

                if (armor > 297 && armor < 302)
                {
                    used = true;
                    basehealth += 5;
                    attackStrength++;
                    health = basehealth;
                    String s = basetexture.substring(0, 18);
                    s = (new StringBuilder()).append(s).append("L.png").toString();
                    texture = s;
                    smoke();
                }

                if (armor > 313 && armor < 318)
                {
                    used = true;
                    basehealth += 10;
                    attackStrength += 2;
                    health = basehealth;
                    String s1 = basetexture.substring(0, 18);
                    s1 = (new StringBuilder()).append(s1).append("G.png").toString();
                    texture = s1;
                    smoke();
                }

                if (armor > 305 && armor < 310)
                {
                    used = true;
                    basehealth += 20;
                    health = basehealth;
                    attackStrength += 4;
                    String s2 = basetexture.substring(0, 18);
                    s2 = (new StringBuilder()).append(s2).append("I.png").toString();
                    texture = s2;
                    smoke();
                }

                if (armor > 309 && armor < 314)
                {
                    smoke();
                    used = true;
                    basehealth += 30;
                    health = basehealth;
                    attackStrength += 10;
                    String s3 = basetexture.substring(0, 18);
                    s3 = (new StringBuilder()).append(s3).append("D.png").toString();
                    texture = s3;
                    smoke();
                }
            }

            if (itemstack.getItem() == Items.SNOWBALL)
            {
            	if (!world.isRemote){
            		if (!playermp.getStatFile().hasAchievementUnlocked(ModAdvancementList.snowdevil))
                	{
                    	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                    	playermp.addStat(ModAdvancementList.snowdevil, 1);
                    	confetti();
                	}
            	}
            	
            	if(world.isRemote){
            		if (!Minecraft.getInstance().player.getStatFileWriter().hasAchievementUnlocked(ModAdvancementList.snowdevil))
                	{
                    	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                    	playerentity.addStat(ModAdvancementList.snowdevil, 1);
                    	confetti();
                	}
            	}
                

                used = true;
                health += 2;
                smoke();
                smoke();
                tamed = true;
                health = basehealth;

                if (name.length() < 1)
                {
                    name = Names[rand.nextInt(Names.length)];
                }

                world.playSound(playerentity, this.getPosition(), SoundsHandler.SNOW_DEVIL_TAMED, SoundCategory.MASTER, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            if (health > basehealth)
            {
                health = basehealth;
            }

            if (used)
            {
                if (itemstack.getCount() - 1 == 0)
                {
                    playerentity.inventory.setInventorySlotContents(playerentity.inventory.currentItem, null);
                }
                else
                {
                    itemstack.setCount(itemstack.getCount() - 1);
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playAmbientSound()
    {
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.SNOW_DEVIL;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.SNOW_DEVIL_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.SNOW_DEVIL_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
        if (tamed && health > 0)
        {
            return;
        }

        super.onDeath(damagesource);

        if (rand.nextInt(10) == 0)
        {
            entityDropItem(Blocks.ICE, rand.nextInt(3) + 1);
            entityDropItem(Blocks.SNOW, rand.nextInt(10) + 1);
        }
        else
        {
        	entityDropItem(Blocks.SNOW, rand.nextInt(5) + 2);
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        if (tamed && health > 0)
        {
            dead = false;
            deathTime = 0;
            return;
        }
        else
        {
            super.setHealth(0);
            return;
        }
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
