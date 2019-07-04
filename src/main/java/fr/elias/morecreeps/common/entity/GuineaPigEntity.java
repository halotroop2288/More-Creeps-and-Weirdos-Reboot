package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.Explosion.Mode;
import net.minecraftforge.common.extensions.IForgeBlockState;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.particles.CREEPSFxBlood;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class GuineaPigEntity extends MobEntity
{
	PlayerEntity playerentity;
	Entity entitymain;
	World world;
    public boolean rideable;
    public int interest;
    private boolean primed;
    public boolean tamed;
    public int basehealth;
    private float distance;
    public Item armor;
    public String basetexture;
    public boolean used;
    public boolean grab;
    public List piglist;
    public int pigstack;
    public int level;
    public float totaldamage;
    public int alt;
    public boolean hotelbuilt;
    public int wanderstate;
    public int speedboost;
    public int totalexperience;
    public float baseSpeed;
    public float modelsize;
    public int unmounttimer;
    public int skillattack;
    public int skilldefend;
    public int skillhealing;
    public int skillspeed;
    public int firenum;
    public int firepower;
    public int healtimer;
    public LivingEntity owner;
    public int criticalHitCooldown;
    public String name;
    public String texture;
    public double health;
    public double attackStrength;
    public double moveSpeed;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();
    
    static final String Names[] =
    {
        "Sugar", "Clover", "CoCo", "Sprinkles", "Mr. Rabies", "Stinky", "The Incredible Mr. CoCoPants", "Butchie", "Lassie", "Fuzzy",
        "Nicholas", "Natalie", "Pierre", "Priscilla", "Mrs. McGillicutty", "Dr. Tom Jones", "Peter the Rat", "Wiskers", "Penelope", "Sparky",
        "Tinkles", "Ricardo", "Jimothy", "Captain Underpants", "CoCo Van Gough", "Chuck Norris", "PeeWee", "Quasimodo", "ZSA ZSA", "Yum Yum",
        "Deputy Dawg", "Henrietta Pussycat", "Bob Dog", "King Friday", "Jennifer", "The Situation", "Prince Charming", "Sid", "Sunshine", "Bubbles",
        "Carl", "Snowy", "Dorf", "Chilly Willy", "Angry Bob", "George W. Bush", "Ted Lange from The Love Boat", "Notch", "Frank", "A Very Young Pig",
        "Blaster", "Darwin", "Ruggles", "Chang", "Spaz", "Fluffy", "Fuzzy", "Charrlotte", "Tootsie", "Mary",
        "Caroline", "Michelle", "Sandy", "Peach", "Scrappy", "Roxanne", "James the Pest", "Lucifer", "Shaniqua", "Wendy",
        "Zippy", "Prescott Pig", "Pimpin' Pig", "Big Daddy", "Little Butchie", "The Force", "The Handler", "Little Louie", "Satin", "Sparkly Puff",
        "Dr. Chews", "Pickles", "Longtooth", "Jeffry", "Pedro the Paunchy", "Wee Willy Wiskers", "Tidy Smith", "Johnson", "Big Joe", "Tiny Mackeral",
        "Wonderpig", "Wee Wonderpig", "The Polish Baron", "Inconceivable", "Double Danny Dimples", "Jackie Jones", "Pistol", "Tiny Talker", "Strum", "Disco the Pig",
        "Banjo", "Fingers", "Clean Streak", "Little Sweet", "Fern", "Youngblood", "Lazy Cottonball", "Foxy", "SlyFoxHound"
    };
    static final String pigTextures[] =
    {
        Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpig1.png", Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpig2.png",
        Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpig3.png", Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpig4.png",
        Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpig5.png", Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpig6.png",
        Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpig7.png", Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpig8.png",
        Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpig9.png", Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "ggpiga.png"
    };
    public static final int leveldamage[] =
    {
        0, 200, 600, 1000, 1500, 2000, 2700, 3500, 4400, 5400,
        6600, 7900, 9300, 10800, 12400, 14100, 15800, 17600, 19500, 21500,
        25000, 30000
    };
    public static final String levelname[] =
    {
        "Guinea Pig", "A nothing pig", "An inexperienced pig", "Trainee", "Private", "Private First Class", "Corporal", "Sergeant", "Staff Sergeant", "Sergeant First Class",
        "Master Sergeant", "First Sergeant", "Sergeant Major", "Command Sergeant Major", "Second Lieutenant", "First Lieutenant", "Captain", "Major", "Lieutenant Colonel", "Colonel",
        "General of the Pig Army", "General of the Pig Army"
    };

    public GuineaPigEntity(World world)
    {
        super(null, world);
        primed = false;
        basetexture = pigTextures[rand.nextInt(pigTextures.length)];
        texture = basetexture;
        setSize(0.6F, 0.6F);
        rideable = false;
        basehealth = rand.nextInt(5) + 5;
        health = basehealth;
        attackStrength = 1;
        tamed = false;
        name = "";
        pigstack = 0;
        level = 1;
        totaldamage = 0.0F;
        alt = 1;
        hotelbuilt = false;
        wanderstate = 0;
        baseSpeed = 0.6F;
        moveSpeed = baseSpeed;
        speedboost = 0;
        totalexperience = 0;
        fallDistance = -25F;
        modelsize = 1.0F;
        unmounttimer = 0;
        skillattack = 0;
        skilldefend = 0;
        skillhealing = 0;
        skillspeed = 0;
        firepower = 0;
        criticalHitCooldown = 5;
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAILookIdle(this));
    }
    
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(health);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(moveSpeed);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(attackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(attackStrength);
    }

    protected void updateAITick()
    {
        super.updateEntityActionState();

        if (!this.attackEntityAsMob(entitymain) && !hasPath() && tamed && ridingEntity == null && wanderstate != 2)
        {
            

            if (playerentity != null)
            {
                float f = playerentity.getDistance(this);

                if (f <= 5F);
            }
        }

        if (this.getAttackTarget() instanceof PlayerEntity)
        {
            

            if (getDistance(playerentity) < 6F)
            {
                this.setAttackTarget(null);
            }
        }

        if ((float)health < (float)basehealth * (0.1F * (float)skillhealing) && skillhealing > 0)
        {
            this.attackEntity(playerentity, (float) attackStrength);
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        Object obj = null;

        if (tamed && wanderstate == 0)
        {
            List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(16D, 16D, 16D));

            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);

                if (entity instanceof LivingEntity)
                {
                	LivingEntity entitycreature = (LivingEntity)entity;

                    if ((((MobEntity) entitycreature).getAttackTarget() instanceof PlayerEntity) && !(entitycreature instanceof HotdogEntity) && !(entitycreature instanceof HunchbackEntity) && !(entitycreature instanceof GuineaPigEntity) && (!(entitycreature instanceof ArmyGuyEntity) || !((ArmyGuyEntity)entitycreature).loyal))
                    {
                        obj = entitycreature;
                    }
                }

                if (!(entity instanceof PlayerEntity) || wanderstate != 0)
                {
                    continue;
                }

                PlayerEntity playerentity = (PlayerEntity)entity;

                if (playerentity == null || obj != null && !(obj instanceof PlayerEntity))
                {
                    continue;
                }

                distance = getDistance(playerentity);

                if (distance < 6F)
                {
                    obj = null;
                }
                else
                {
                    obj = playerentity;
                }
            }
        }

        return ((Entity)(obj));
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (!(this.getAttackTarget() instanceof PlayerEntity) && !(this.getAttackTarget() instanceof GuineaPigEntity) && !(this.getAttackTarget() instanceof HotdogEntity) && ridingEntity == null)
        {
            if (onGround && tamed)
            {
                double d = entity.posX - posX;
                double d2 = entity.posZ - posZ;
                float f2 = MathHelper.sqrt(d * d + d2 * d2);
                moveForward = (float) ((d / (double)f2) * 0.5D * 0.80000001192092896D + moveForward * 0.20000000298023224D);
                moveVertical = (float) ((d2 / (double)f2) * 0.5D * 0.80000001192092896D + moveStrafing * 0.20000000298023224D);
                moveStrafing = (float) 0.40000000596046448D;
            }
            else if (tamed && (double)f < 2.5D)
            {
                if (rand.nextInt(5) == 0)
                {
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_ANGRY, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }

                double d1 = 1.0D;
                d1 += level * 5 + skillattack * 4;

                if (d1 < 5D)
                {
                    d1 = 5D;
                }

                super.attackEntityAsMob(entity);

                if ((double)rand.nextInt(100) > 100D - d1)
                {
                    if (CREEPSConfig.Blood)
                    {
                        for (int i = 0; i < 10; i++)
                        {
                            CREEPSFxBlood creepsfxblood = new CREEPSFxBlood(world, entity.posX, entity.posY + 1.0D, entity.posZ, MoreCreepsReboot.partRed, 0.255F);
                            creepsfxblood.renderDistanceWeight = 20D;
                            Minecraft.getInstance().effectRenderer.addEffect(creepsfxblood);
                        }
                    }

                    float f1 = (float)attackStrength * 0.25F;

                    if (f1 < 1.0F)
                    {
                        f1 = 1.0F;
                    }

                    if (skillattack > 1 && rand.nextInt(100) > 100 - skillattack * 2 && criticalHitCooldown-- < 1)
                    {
                        totaldamage += 25F;
                        totalexperience += 25;

                        if (f1 < (float)((LivingEntity)entity).getHealth())
                        {
                            f1 = ((LivingEntity)entity).getHealth();
                        }

                        ((LivingEntity)entity).setHealth(0);
                        world.playSound(playerentity, entity.getPosition(), SoundsHandler.GUINEA_PIG_CRITICAL_HIT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                        criticalHitCooldown = 50 - skillattack * 8;
                    }

                    if ((float)((LivingEntity)entity).getHealth() - f1 <= 0.0F)
                    {
                        world.playSound(playerentity, entity.getPosition(), SoundsHandler.GUINEA_PIG_ANGRY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    }

                    ((LivingEntity)entity).attackEntityFrom(DamageSource.causeThrownDamage(this, (LivingEntity)entity), (int)f1);
                    totaldamage += (int)((double)f1 * 1.5D + (double)skillattack);
                    totalexperience += (int)((double)f1 * 1.5D + (double)skillattack);
                }

                if (totaldamage > (float)leveldamage[level] && level < 20)
                {
                    level++;
                    totaldamage = 0.0F;
                    boolean flag = false;

                    if (level == 5)
                    {
                        flag = true;
                        confetti();
                        playerentity.addStat(MoreCreepsReboot.piglevel5, 1);
                    }

                    if (level == 10)
                    {
                        flag = true;
                        confetti();
                        playerentity.addStat(MoreCreepsReboot.piglevel10, 1);
                    }

                    if (level == 20)
                    {
                        flag = true;
                        confetti();
                        playerentity.addStat(MoreCreepsReboot.piglevel20, 1);
                    }

                    if (flag)
                    {
                        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                    }

                    MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\247b").append(name).append(" \247fincreased to level \2476").append(String.valueOf(level)).append("!").toString());
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_LEVEL_UP, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    baseSpeed += 0.025F;
                    basehealth += rand.nextInt(4) + 1;
                    attackStrength++;
                }

                super.setAttackTarget((LivingEntity) entity);
            }
        }

        if ((double)f < 16D && (this.getAttackTarget() instanceof PlayerEntity))
        {
            this.setAttackTarget(null);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getTrueSource();

        if (entity != this.getAttackTarget())
        {
            this.setAttackTarget((LivingEntity) entity);
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        if (ridingEntity != null || unmounttimer-- > 0)
        {
            return false;
        }
        else
        {
            return super.isEntityInsideOpaqueBlock();
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (getRidingEntity() != null && getRidingEntity() == null)
        {
            if (getRidingEntity() != null)
            {
            	getRidingEntity().addPassenger(null);
            }

            if (getRidingEntity() != null)
            {
            	getRidingEntity().addPassenger(null);
            }

            this.addPassenger(null);
        }

        if (getRidingEntity() != null && !(getRidingEntity() instanceof PlayerEntity) && !(getRidingEntity() instanceof GuineaPigEntity) && !(getRidingEntity() instanceof HotdogEntity))
        {
        	this.addPassenger(null);
            unmounttimer = 20;
        }

        if (speedboost-- == 0 && name.length() > 0)
        {
            world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_SPEED_DOWN, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\247b").append(name).append("\2476 has run out of speedboost.").toString());

            if (wanderstate != 1)
            {
                moveSpeed = baseSpeed;
            }
        }

        if (healtimer-- < 1 && health < basehealth && skillhealing > 0)
        {
            healtimer = (6 - skillhealing) * 200;
            health += skillhealing;

            if (health > basehealth)
            {
                health = basehealth;
            }

            for (int i = 0; i < skillhealing; i++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.HEART, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
        }

        if (handleWaterMovement())
        {
            moveVertical += 0.028799999505281448D;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putInt("Interest", interest);
        compound.putBoolean("Tamed", tamed);
        compound.putString("Name", name);
        compound.putInt("BaseHealth", basehealth);
        compound.putInt("Level", level);
        compound.putString("BaseTexture", basetexture);
        compound.putFloat("TotalDamage", totaldamage);
        compound.putBoolean("HotelBuilt", hotelbuilt);
        compound.putInt("AttackStrength", (int) attackStrength);
        compound.putInt("WanderState", wanderstate);
        compound.putInt("SpeedBoost", speedboost);
        compound.putInt("TotalExperience", totalexperience);
        compound.putFloat("BaseSpeed", baseSpeed);
        compound.putFloat("ModelSize", modelsize);
        compound.putInt("SkillAttack", skillattack);
        compound.putInt("SkillDefense", skilldefend);
        compound.putInt("SkillHealing", skillhealing);
        compound.putInt("SkillSpeed", skillspeed);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        interest = compound.getInt("Interest");
        tamed = compound.getBoolean("Tamed");
        name = compound.getString("Name");
        basetexture = compound.getString("BaseTexture");
        basehealth = compound.getInt("BaseHealth");
        level = compound.getInt("Level");
        totaldamage = compound.getFloat("TotalDamage");
        hotelbuilt = compound.getBoolean("HotelBuilt");
        attackStrength = compound.getInt("AttackStrength");
        wanderstate = compound.getInt("WanderState");
        speedboost = compound.getInt("SpeedBoost");
        totalexperience = compound.getInt("TotalExperience");
        baseSpeed = compound.getFloat("BaseSpeed");
        modelsize = compound.getFloat("ModelSize");
        skillattack = compound.getInt("SkillAttack");
        skilldefend = compound.getInt("SkillDefense");
        skillhealing = compound.getInt("SkillHealing");
        skillspeed = compound.getInt("SkillSpeed");
        texture = basetexture;

        if (wanderstate == 1)
        {
            moveSpeed = 0.0F;
        }
        else
        {
            moveSpeed = speedboost <= 0 ? baseSpeed : baseSpeed + 0.75F;
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        if (interest == 0 || health <= 0)
        {
            if (tamed)
            {
                TombstoneEntity creepsentitytombstone = new TombstoneEntity(world);
                creepsentitytombstone.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                creepsentitytombstone.interest = interest;
                creepsentitytombstone.tamed = tamed;
                creepsentitytombstone.name = name;
                creepsentitytombstone.basehealth = basehealth;

                if (level > 1)
                {
                    level--;
                }

                creepsentitytombstone.level = level;
                creepsentitytombstone.basetexture = basetexture;
                creepsentitytombstone.totaldamage = 0.0F;
                creepsentitytombstone.hotelbuilt = hotelbuilt;
                creepsentitytombstone.attackStrength = (int) attackStrength;
                creepsentitytombstone.wanderstate = wanderstate;
                creepsentitytombstone.speedboost = speedboost;
                creepsentitytombstone.totalexperience = totalexperience;
                creepsentitytombstone.baseSpeed = baseSpeed;
                creepsentitytombstone.modelsize = modelsize;
                creepsentitytombstone.skillattack = skillattack;
                creepsentitytombstone.skilldefend = skilldefend;
                creepsentitytombstone.skillhealing = skillhealing;
                creepsentitytombstone.skillspeed = skillspeed;
                creepsentitytombstone.deathtype = "GuineaPig";
                world.addEntity(creepsentitytombstone);
            }

            super.setHealth(0);
        }
        else
        {
            setHealth(0);
            deathTime = 0;
            return;
        }
    }

    @SuppressWarnings("unused")
	private void explode()
    {
        float f = 2.0F;
        world.createExplosion(null, posX, posY, posZ, f, true, Mode.NONE);
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

    private void smokePlain()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height) + (double)i, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
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
        int l = world.getBlockLightOpacity(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.WHITE_WOOL && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 && world.checkBlockCollision(getBoundingBox()) && world.canBlockSeeSky(getPosition()) && rand.nextInt(5) == 0 && l > 8;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 2;
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        if (getRidingEntity() instanceof PlayerEntity)
        {
            return (double)(this.getYOffset() - 1.15F);
        }
        else
        {
            return (double)this.getYOffset();
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @SuppressWarnings("deprecation")
	public boolean interact(PlayerEntity playerentity)
    {
        ItemStack itemstack = playerentity.inventory.getCurrentItem();
        used = false;

        if (tamed && playerentity.isSneaking())
        {
            playerentity.openGui(MoreCreepsReboot.instance, 3, world, (int)playerentity.posX, (int)playerentity.posY, (int)playerentity.posZ); // TODO register custom gui
            return true;
        }

        if (itemstack == null && tamed && health > 0)
        {
            rotationYaw = playerentity.rotationYaw;
            Object obj = playerentity;

            if (getRidingEntity() != obj)
            {
                int k;

                for (k = 0; ((Entity)obj).getRidingEntity() != null && k < 20; k++)
                {
                    obj = ((Entity)obj).getRidingEntity();
                }

                if (k < 20)
                {
                    rotationYaw = ((Entity)obj).rotationYaw;
                    this.addPassenger((Entity)obj);
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_MOUNT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
            }
            else
            {
                int l;

                for (l = 0; ((Entity)obj).getRidingEntity() != null && l < 20; l++)
                {
                    obj = ((Entity)obj).getRidingEntity();
                }

                if (l < 20)
                {
                    rotationYaw = ((Entity)obj).rotationYaw;
                    ((Entity)obj).fallDistance = -25F;
                    ((Entity)obj).mountEntity(null);

                    if ((Entity)obj instanceof GuineaPigEntity)
                    {
                        ((GuineaPigEntity)obj).unmounttimer = 20;
                    }

                    world.playSound(playerentity, this.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
            }
        }

        if (itemstack != null && health > 0)
        {
            if ((itemstack.getItem() == Items.BOOK || itemstack.getItem() == Items.PAPER || itemstack.getItem() == ItemList.pet_radio) && tamed)
            {
            	playerentity.openGui(MoreCreepsReboot.instance, 4, world, (int)playerentity.posX, (int)playerentity.posY, (int)playerentity.posZ);
            }

            if (itemstack.getItem() == Items.DIAMOND && tamed)
            {
                if (getRidingEntity() != null)
                {
                	MoreCreepsReboot.proxy.addChatMessage("Put your Guinea Pig down before building the Guinea Pig Hotel!");
                }
                else if (!hotelbuilt)
                {
                    if (level >= 20)
                    {
                        
                            world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                            playerentity.addStat(MoreCreepsReboot.pighotel, 1);
                        

                        world.playSound(playerentity, this.getPosition(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 1.0F, 0.5F);
                        int i = MathHelper.floor(playerentity.posX);
                        int i1 = MathHelper.floor(playerentity.getBoundingBox().minY);
                        int j1 = MathHelper.floor(playerentity.posZ);
                        createDisco(playerentity, i + 2, i1, j1 + 2);
                    }
                    else
                    {
                    	MoreCreepsReboot.proxy.addChatMessage("Your Guinea Pig must be level 20 to build a Hotel.");
                    	MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\247b").append(name).append(" is only level \247f").append(String.valueOf(level)).toString());
                    }
                }
                else
                {
                	MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\247b").append(name).append("\247f has already built a Hotel.").toString());
                }
            }

            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.POPPY) && tamed || itemstack.getItem() == Item.getItemFromBlock(Blocks.DANDELION) && tamed)
            {
                smokePlain();

                if (wanderstate == 0)
                {
                	MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\2473").append(name).append("\2476 will \2474STAY\2476 right here.").toString());
                    wanderstate = 1;
                    moveSpeed = 0.0F;
                }
                else if (wanderstate == 1)
                {
                	MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\2473").append(name).append("\2476 will \247dWANDER\2476 around and have fun.").toString());
                    wanderstate = 2;
                    moveSpeed = speedboost <= 0 ? baseSpeed : baseSpeed + 0.5F;
                }
                else if (wanderstate == 2)
                {
                	MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\2473").append(name).append("\2476 will \2472FIGHT\2476 and follow you!").toString());
                    wanderstate = 0;
                    moveSpeed = speedboost <= 0 ? baseSpeed : baseSpeed + 0.5F;
                }
            }

            if (itemstack.getItem() == Items.SUGAR_CANE && tamed)
            {
                smokePlain();
                used = true;

                if (speedboost < 0)
                {
                    speedboost = 0;
                }

                speedboost += 13000;

                if (wanderstate != 1)
                {
                    moveSpeed = baseSpeed + 0.5F;
                }

                int j = speedboost / 21;
                j /= 60;
                String s = "";

                if (j < 0)
                {
                    j = 0;
                }

                if (j > 1)
                {
                    s = "s";
                }

                world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_SPEED_UP, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                MoreCreepsReboot.proxy.addChatMessage((new StringBuilder()).append("\2473").append(name).append("\247f ").append(String.valueOf(j)).append("\2476 minute").append(s).append(" of speedboost left.").toString());
            }

            if (itemstack.getItem() == Items.EGG)
            {
                used = true;
                world.playSound(playerentity, this.getPosition(), SoundEvents.ENTITY_TNT_PRIMED, 1.0F, 0.5F);
                setLocationAndAngles(playerentity.posX, playerentity.posY + (double)playerentity.getEyeHeight(), playerentity.posZ, playerentity.rotationYaw, playerentity.rotationPitch);
                moveForward = -MathHelper.sin((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
                moveStrafing = MathHelper.cos((rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((rotationPitch / 180F) * (float)Math.PI);
                double d = moveForward / 100D;
                double d1 = moveStrafing / 100D;

                for (int l1 = 0; l1 < 2000; l1++)
                {
                    setPosition(d, 0.0D, d1);
                    double d2 = rand.nextGaussian() * 0.02D;
                    double d3 = rand.nextGaussian() * 0.02D;
                    double d4 = rand.nextGaussian() * 0.02D;
                    world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d2, d3, d4);
                }

                world.createExplosion(null, posX, posY, posZ, 1.1F, true, Mode.NONE);
                interest = 0;
                health = 0;
                setDead();
            }
            else
            {
                primed = false;
            }

            byte byte0 = 0;
            byte byte1 = 0;

            if (tamed && texture.length() == 23 + 14)
            {
                String s1 = basetexture.substring(18 + 14, 19 + 14);
                char c = s1.charAt(0);

                if (c == 'L')
                {
                    byte0 = 5;
                    byte1 = 1;
                }

                if (c == 'I')
                {
                    byte0 = 9;
                    byte1 = 2;
                }

                if (c == 'G')
                {
                    byte0 = 15;
                    byte1 = 3;
                }

                if (c == 'D')
                {
                    byte0 = 22;
                    byte1 = 6;
                }
            }

            if (tamed)
            {
                armor = itemstack.getItem();
                smoke();
                int k1 = 0;

                if (armor == Items.LEATHER_BOOTS || armor == Items.LEATHER_CHESTPLATE || armor == Items.LEATHER_HELMET || armor == Items.LEATHER_LEGGINGS)
                {
                    used = true;
                    basehealth += 5 - byte0;
                    attackStrength += 1 - byte1;
                    health = basehealth;
                    String s2 = basetexture.substring(0, 18 + 14);
                    s2 = (new StringBuilder()).append(s2).append("L.png").toString();
                    texture = s2;
                    basetexture = s2;
                    smoke();
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_ARMOUR, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }

                if (armor == Items.GOLDEN_BOOTS || armor == Items.GOLDEN_CHESTPLATE || armor == Items.GOLDEN_HELMET || armor == Items.GOLDEN_LEGGINGS)
                {
                    used = true;
                    basehealth += 15 - byte0;
                    attackStrength += 3 - byte1;
                    health = basehealth;
                    String s3 = basetexture.substring(0, 18 + 14);
                    s3 = (new StringBuilder()).append(s3).append("G.png").toString();
                    texture = s3;
                    basetexture = s3;
                    smoke();
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_ARMOUR, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }

                if (armor == Items.IRON_BOOTS || armor == Items.IRON_CHESTPLATE || armor == Items.IRON_HELMET || armor == Items.IRON_LEGGINGS)
                {
                    used = true;
                    basehealth += 9 - byte0;
                    attackStrength += 2 - byte1;
                    health = basehealth;
                    String s4 = basetexture.substring(0, 18 + 14);
                    s4 = (new StringBuilder()).append(s4).append("I.png").toString();
                    texture = s4;
                    basetexture = s4;
                    smoke();
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_ARMOUR, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }

                if (armor == Items.DIAMOND_BOOTS || armor == Items.DIAMOND_CHESTPLATE || armor == Items.DIAMOND_HELMET || armor == Items.DIAMOND_LEGGINGS)
                {
                    used = true;
                    basehealth += 22 - byte0;
                    attackStrength += 6 - byte1;
                    health = basehealth;
                    String s5 = basetexture.substring(0, 18 + 14);
                    s5 = (new StringBuilder()).append(s5).append("D.png").toString();
                    texture = s5;
                    basetexture = s5;
                    smoke();
                	world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_ARMOUR, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
            }

            if (itemstack.getItem() == Items.WHEAT)
            {
            	world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_EAT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                used = true;
                interest += 15;
                health += 10;
                dead = false;
                smoke();
            }

            if (itemstack.getItem() == Items.COOKIE)
            {
            	world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_EAT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                used = true;
                interest += 30;
                health += 15;
                dead = false;
                smoke();
            }

            if (itemstack.getItem() == Items.APPLE)
            {
            	world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_EAT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                used = true;
                interest += 55;
                health += 25;
                dead = false;
                smoke();
            }

            if (itemstack.getItem() == Items.GOLDEN_APPLE)
            {
            	world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_EAT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                used = true;
                interest += 111;
                health += 75;
                dead = false;
                smoke();
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

            if (!primed && interest > 100)
            {
                
                    confetti();
                    world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                    playerentity.addStat(MoreCreepsReboot.pigtaming, 1);
                

                if (used)
                {
                    smoke();
                }

                tamed = true;

                if (name.length() < 1)
                {
                    name = Names[rand.nextInt(Names.length)];
                }

                world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_FULL, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                interest = 100;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void createDisco(PlayerEntity playerentity, int i, int j, int k)
    {
        byte byte0 = 16;
        byte byte1 = 6;
        byte byte2 = 16;
        alt = 1;
        int l = 0;

        for (int i1 = 0; i1 < byte1 + 4; i1++)
        {
            for (int k3 = -2; k3 < byte0 + 2; k3++)
            {
                for (int i5 = -2; i5 < byte2 + 2; i5++)
                {
                	
                    if (world.getBlockState(new BlockPos(i + k3, j + i1, k + i5)).getBlock() != Blocks.AIR)
                    {
                        l++;
                    }
                }
            }
        }

        if (l < 900)
        {
            used = true;
            hotelbuilt = true;
            world.playSound(playerentity, this.getPosition(), SoundsHandler.GUINEA_PIG_HOTEL, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            MoreCreepsReboot.proxy.addChatMessage("GUINEA PIG HOTEL BUILT!");

            for (int j1 = 0; j1 < byte1 + 4; j1++)
            {
                for (int l3 = -2; l3 < byte0 + 2; l3++)
                {
                    for (int j5 = -2; j5 < byte2 + 2; j5++)
                    {
                    	//Minecraft fucking changing things so much...  (DITTO - Halo)
                    	//It went from one line to four!!!!!
                        //old code (for reference)
                    	//world.setBlockWithNotify(i + l3, j + j1, k + j5, 0);
                    	Block blk = Blocks.AIR;
                    	BlockPos pos0 = new BlockPos(i + l3, j + j1, k + j5);
                    	BlockState state0 = blk.getDefaultState();
                    	world.setBlockState(pos0, state0);
                    }
                }
            }

            for (int k1 = 0; k1 < byte1; k1++)
            {
                for (int i4 = 0; i4 < byte2; i4++)
                {
                    alt *= -1;

                    for (int k5 = 0; k5 < byte0; k5++)
                    {
                        //world.setBlockWithNotify(i + i4, j + k1, k + 0, 35);
                    	Block blk = Blocks.WHITE_WOOL;
                    	BlockPos pos0 = new BlockPos(i + i4, j + k1, k + 0);
                    	BlockState state0 = blk.getDefaultState();
                    	world.setBlockState(pos0, state0);
                        //world.setBlockWithNotify(i + i4, j + k1, (k + byte0) - 1, 35);
                    	BlockPos pos1 = new BlockPos(i + i4, j + k1, (k + byte0) - 1);
                    	BlockState state1 = blk.getDefaultState();
                    	world.setBlockState(pos1, state1);
                        //world.setBlockAndMetadataWithNotify(i + 0, j + k1, k + k5, Block.cloth.blockID, 1);
                    	ItemStack orngwool = new ItemStack(Blocks.ORANGE_WOOL);
                    	orngwool.getItem();
                    	Block blkowool = Blocks.BLACK_WOOL;
                    	BlockPos pos2 = new BlockPos(i + i4, j + k1, (k + byte0) - 1);
                    	BlockState state2 = blkowool.getDefaultState();
                    	world.setBlockState(pos2, state2);
                        //world.setBlockAndMetadataWithNotify(i + byte2, j + k1, k + k5, Block.cloth.blockID, 1);
                    	BlockPos pos3 = new BlockPos(i + byte2, j + k1, k + k5);
                    	world.setBlockState(pos3, state2);
                    	
                        alt *= -1;

                        if (alt > 0)
                        {
                            //world.setBlockAndMetadataWithNotify(i + i4, j, k + k5, Block.cloth.blockID, 10);
                        	ItemStack prplwool = new ItemStack(Blocks.PURPLE_WOOL);
                        	prplwool.getItem();
                        	Block blkpwool = Blocks.BLACK_WOOL;
                        	BlockPos pos4 = new BlockPos(i + i4, j, k + k5);
                        	BlockState state3 = blkpwool.getDefaultState();
                        	world.setBlockState(pos4, state3);
                        	
                        }
                        else
                        {
                            //world.setBlockAndMetadataWithNotify(i + i4, j, k + k5, Block.cloth.blockID, 11);
                        	ItemStack wooltype = new ItemStack(Blocks.WHITE_WOOL);
                        	wooltype.getItem();
                        	Block blkwool = Blocks.BLACK_WOOL;
                        	BlockPos pos4 = new BlockPos(i + i4, j, k + k5);
                        	BlockState state3 = blkwool.getDefaultState();
                        	world.setBlockState(pos4, state3);
                        }

                        //world.setBlockWithNotify(i + i4, j + byte1, k + k5, Block.glass.blockID);
                    	Block blk1 = Blocks.GLASS;
                    	BlockPos pos4 = new BlockPos(i + i4, j + byte1, k + k5);
                    	BlockState state3 = blk1.getDefaultState();
                    	world.setBlockState(pos4, state3);
                    }
                }
            }

            //world.setBlockWithNotify(i + 7, j, k - 1, 43);
            														// Commented out until a solution to place double slabs can be found
//            Block blk = Blocks.DOUBLE_STONE_SLAB;
//        	BlockPos pos0 = new BlockPos(i + 7, j, k - 1);
//        	BlockState state0 = blk.getDefaultState();
//        	world.setBlockState(pos0, state0);
            //world.setBlockWithNotify(i + 10, j, k - 1, 43);
            //world.setBlock(i + 7, j + 2, k - 1, Block.torchWood.blockID);
        	Block blk2 = Blocks.TORCH;
        	BlockPos pos2 = new BlockPos(i + 7, j + 2, k - 1);
        	BlockState state1 = blk2.getDefaultState();
        	world.setBlockState(pos2, state1);
            //world.setBlock(i + 10, j + 2, k - 1, Block.torchWood.blockID);
        	BlockPos pos3 = new BlockPos(i + 10, j + 2, k - 1);
        	world.setBlockState(pos3, state1);
            //world.setBlockWithNotify(i + 8, j, k - 1, 44);
        	Block blk3 = Blocks.STONE_SLAB;
        	BlockPos pos4 = new BlockPos(i + 7, j, k - 1);
        	BlockState state2 = blk3.getDefaultState();
        	world.setBlockState(pos4, state2);
            //world.setBlockWithNotify(i + 9, j, k - 1, 44);
        	BlockPos pos5 = new BlockPos(i + 9, j, k - 1);
        	world.setBlockState(pos5, state2);
            //world.setBlockWithNotify(i + 8, j + 1, k, Block.doorWood.blockID);
        	
        	Block doorgetter[] = {
        			Blocks.ACACIA_DOOR, Blocks.BIRCH_DOOR, Blocks.DARK_OAK_DOOR, Blocks.JUNGLE_DOOR, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR
        	};
        	Block blk4 = doorgetter[rand.nextInt(doorgetter.length)];
        	BlockPos pos6 = new BlockPos(i + 7, j, k - 1);
        	BlockState state3 = blk4.getDefaultState();
        	world.setBlockState(pos6, state3);
            //world.setBlockMetadataWithNotify(i + 8, j + 1, k, 0);
        	Block blk1 = Blocks.AIR;
        	BlockPos pos7 = new BlockPos(i + 8, j + 1, k);
        	BlockState state4 = blk1.getDefaultState();
        	world.setBlockState(pos7, state4);
            //world.setBlockWithNotify(i + 8, j + 2, k, Block.doorWood.blockID);
        	Block blk5 = doorgetter[rand.nextInt(doorgetter.length)];
        	BlockPos pos8 = new BlockPos(i + 8, j + 2, k);
        	BlockState state5 = blk5.getDefaultState();
        	world.setBlockState(pos8, state5);
            //world.setBlockMetadataWithNotify(i + 8, j + 2, k, 8);
//        	Block blk6 = Blocks.WATER;
//        	BlockPos pos9 = new BlockPos(i + 8, j + 1, k);
//        	IBlockState state6 = blk6.getDefaultState();
//        	world.setBlockState(pos9, state6);
            //world.setBlockWithNotify(i + 9, j + 1, k, Block.doorWood.blockID);
        	BlockPos pos10 = new BlockPos(i + 9, j + 1, k);
        	world.setBlockState(pos10, state5);
            //world.setBlockMetadataWithNotify(i + 9, j + 1, k, 1);
        	Block blk7 = Blocks.STONE;
        	BlockPos pos11 = new BlockPos(i + 9, j + 1, k);
        	BlockState state7 = blk7.getDefaultState();
        	world.setBlockState(pos11, state7);
            //world.setBlockWithNotify(i + 9, j + 2, k, Block.doorWood.blockID);
        	BlockPos pos12 = new BlockPos(i + 9, j + 2, k);
        	world.setBlockState(pos12, state5);
            //world.setBlockMetadataWithNotify(i + 9, j + 2, k, 9);
        	Block blk8 = Blocks.WATER;
        	BlockPos pos13 = new BlockPos(i + 9, j + 2, k);
        	BlockState state8 = blk8.getDefaultState();
        	world.setBlockState(pos13, state8);
            //world.setBlockWithNotify(i + 8, j + 1, k + 5, Block.sandStone.blockID);
        	Block blk9 = Blocks.SANDSTONE;
        	BlockPos pos14 = new BlockPos(i + 9, j + 2, k);
        	BlockState state9 = blk9.getDefaultState();
        	world.setBlockState(pos14, state9);
            //world.setBlockWithNotify(i + 9, j + 1, k + 5, Block.sandStone.blockID);
        	//Wow found out there is an easier way :(
//        	world.setBlockState(new BlockPos(i + 9, j + 1, k + 5), Blocks.DOUBLE_STONE_SLAB.getDefaultState());
            //world.setBlock(i + 8, j + 2, k + 5, Block.torchWood.blockID);
        	world.setBlockState(new BlockPos(i + 8, j + 2, k + 5), Blocks.TORCH.getDefaultState());
            //world.setBlock(i + 9, j + 2, k + 5, Block.torchWood.blockID);
        	world.setBlockState(new BlockPos(i + 9, j + 2, k + 5), Blocks.TORCH.getDefaultState());

            for (int l1 = 4; l1 < byte2 - 4; l1 += 3)
            {
                //world.setBlock(i + 1, j + 4, k + l1, Block.torchWood.blockID);
            	world.setBlockState(new BlockPos(i + 1, j + 4, k + l1), Blocks.TORCH.getDefaultState());
                //world.setBlock((i + byte2) - 1, j + 4, k + l1, Block.torchWood.blockID);
            	world.setBlockState(new BlockPos((i + byte2) - 1, j + 4, k + l1), Blocks.TORCH.getDefaultState());
                //world.setBlock(i + l1 + 2, j + 4, (k + byte0) - 2, Block.torchWood.blockID);
            	world.setBlockState(new BlockPos(i + l1 + 2, j + 4, (k + byte0) - 2), Blocks.TORCH.getDefaultState());
            }

            for (int i2 = 0; i2 < 9; i2++)
            {
                for (int j4 = 1; j4 < byte2; j4++)
                {
                    //world.setBlockWithNotify(i + j4, j + 1, k + i2 + 6, Block.dirt.blockID);
                	world.setBlockState(new BlockPos(i + j4, j + 1, k + i2 + 6), Blocks.DIRT.getDefaultState());
                }
            }

            for (int j2 = 0; j2 < 5; j2++)
            {
                for (int k4 = 1; k4 < byte2; k4++)
                {
                    //world.setBlockWithNotify(i + k4, j + 2, k + j2 + 10, Block.dirt.blockID);
                	world.setBlockState(new BlockPos(i + k4, j + 2, k + j2 + 10), Blocks.DIRT.getDefaultState());
                }
            }

            for (int k2 = 3; k2 < byte2 - 3; k2++)
            {
                //world.setBlockWithNotify(i + k2, j + 1, k + 6, 0);
            	world.setBlockState(new BlockPos(i + k2, j + 1, k + 6), Blocks.AIR.getDefaultState());
            }

            for (int l2 = 7; l2 < byte2 - 4; l2++)
            {
                //world.setBlockWithNotify(i + l2, j + 1, k + 7, 0);
            	world.setBlockState(new BlockPos(i + l2, j + 1, k + 7), Blocks.AIR.getDefaultState());
            }

            for (int i3 = 7; i3 < 12; i3++)
            {
                //world.setBlockWithNotify(i + 1, j + 2, k + i3, 37);
            	world.setBlockState(new BlockPos(i + 1, j + 2, k + i3), Blocks.DANDELION.getDefaultState());
                //world.setBlockWithNotify(i + 2, j + 2, k + i3, 37);
            	world.setBlockState(new BlockPos(i + 2, j + 2, k + i3), Blocks.DANDELION.getDefaultState());
                //world.setBlockWithNotify(i + 14, j + 2, k + i3, 38);
            	world.setBlockState(new BlockPos(i + 14, j + 2, k + i3), Blocks.DANDELION.getDefaultState());
                //world.setBlockWithNotify(i + 15, j + 2, k + i3, 38);
            	world.setBlockState(new BlockPos(i + 15, j + 2, k + i3), Blocks.POPPY.getDefaultState());
            }

            for (int j3 = 0; j3 < 3; j3++)
            {
                for (int l4 = 6; l4 < byte2 - 3; l4++)
                {
                    ///world.setBlockWithNotify(i + l4, j + 2, k + j3 + 11, 8);
//                	world.setBlockState(new BlockPos(i + 9, j + 1, k + 5), Blocks.DOUBLE_STONE_SLAB.getDefaultState());
                    ///world.setBlockWithNotify(i + l4, j + 1, k + j3 + 11, 8);
//                	world.setBlockState(new BlockPos(i + 9, j + 1, k + 5), Blocks.DOUBLE_STONE_SLAB.getDefaultState());
                }
            }

            //world.setBlock(i + 5, j + 2, k + 12, 8);
            //world.setBlock(i + 5, j + 2, k + 13, 8);
            //world.setBlockWithNotify(i + 9, j + 1, k + 8, 2);
            world.setBlockState(new BlockPos(i + 9, j + 1, k + 8), Blocks.GRASS.getDefaultState());
            //world.setBlockWithNotify(i + 5, j + 3, k, 20);
            world.setBlockState(new BlockPos(i + 5, j + 3, k), Blocks.GLASS.getDefaultState());
            //world.setBlockWithNotify(i + 5, j + 2, k, 20);
            world.setBlockState(new BlockPos(i + 5, j + 2, k), Blocks.GLASS.getDefaultState());
            //world.setBlockWithNotify(i + 4, j + 3, k, 20);
            world.setBlockState(new BlockPos(i + 4, j + 3, k), Blocks.GLASS.getDefaultState());
            //world.setBlockWithNotify(i + 4, j + 2, k, 20);
            world.setBlockState(new BlockPos(i + 4, j + 2, k), Blocks.GLASS.getDefaultState());
            //world.setBlockWithNotify(i + 13, j + 3, k, 20);
            world.setBlockState(new BlockPos(i + 13, j + 3, k), Blocks.GLASS.getDefaultState());
            //world.setBlockWithNotify(i + 13, j + 2, k, 20);
            world.setBlockState(new BlockPos(i + 13, j + 2, k), Blocks.GLASS.getDefaultState());
            //world.setBlockWithNotify(i + 12, j + 3, k, 20);
            world.setBlockState(new BlockPos(i + 12, j + 3, k), Blocks.GLASS.getDefaultState());
            //world.setBlockWithNotify(i + 12, j + 2, k, 20);
            world.setBlockState(new BlockPos(i + 12, j + 2, k), Blocks.GLASS.getDefaultState());
            //world.setBlock(i + 1, j + 1, k + 3, 54);
            world.setBlockState(new BlockPos(i + 1, j + 1, k + 3), Blocks.CHEST.getDefaultState());
            ChestTileEntity tileentitychest = new ChestTileEntity();
            //setBlockTileEntity is about the same as setBlockState except is a tile entity, like a chest's
            //world.setBlockTileEntity(i + 1, j + 1, k + 3, tileentitychest);
            world.setTileEntity(new BlockPos(i + 1, j + 1, k + 3), tileentitychest);
            //world.setBlock(i + 1, j + 1, k + 4, 54);
//            world.setBlockState(new BlockPos(i + 9, j + 1, k + 5), Blocks.double_stone_slab.getDefaultState());
            ChestTileEntity tileentitychest1 = new ChestTileEntity();
            //world.setBlockTileEntity(i + 1, j + 1, k + 4, tileentitychest1);
            world.setTileEntity(new BlockPos(i + 1, j + 1, k + 4), tileentitychest1);

            for (int l5 = 0; l5 < tileentitychest.getSizeInventory(); l5++)
            {
                if (rand.nextInt(10) == 0)
                {
                    tileentitychest.setInventorySlotContents(l5, new ItemStack(Items.GOLDEN_APPLE));
                    tileentitychest1.setInventorySlotContents(l5, new ItemStack(Items.GOLDEN_APPLE));
                }
                else
                {
                    tileentitychest.setInventorySlotContents(l5, new ItemStack(Items.APPLE));
                    tileentitychest1.setInventorySlotContents(l5, new ItemStack(Items.WHEAT, rand.nextInt(16)));
                }
            }

            //world.setBlock((i + byte2) - 1, j + 1, k + 3, 54);
            world.setBlockState(new BlockPos((i + byte2) - 1, j + 1, k + 3), Blocks.GLASS.getDefaultState());
            ChestTileEntity tileentitychest2 = new ChestTileEntity();
            //world.setBlockTileEntity((i + byte2) - 1, j + 1, k + 3, tileentitychest2);
            world.setTileEntity(new BlockPos((i + byte2) - 1, j + 1, k + 3),tileentitychest2);
            //world.setBlock((i + byte2) - 1, j + 1, k + 4, 54);
            world.setBlockState(new BlockPos((i + byte2) - 1, j + 1, k + 4), Blocks.GLASS.getDefaultState());
            ChestTileEntity tileentitychest3 = new ChestTileEntity();
            //world.setBlockTileEntity((i + byte2) - 1, j + 1, k + 4, tileentitychest3);
            world.setTileEntity(new BlockPos((i + byte2) - 1, j + 1, k + 4),tileentitychest3);

            for (int i6 = 0; i6 < tileentitychest1.getSizeInventory(); i6++)
            {
                if (rand.nextInt(15) == 0)
                {
                    tileentitychest2.setInventorySlotContents(i6, new ItemStack(Items.GOLDEN_APPLE));
                    tileentitychest3.setInventorySlotContents(i6, new ItemStack(Items.APPLE));
                }
                else
                {
                    tileentitychest2.setInventorySlotContents(i6, new ItemStack(Items.APPLE));
                    tileentitychest3.setInventorySlotContents(i6, new ItemStack(Items.WHEAT, rand.nextInt(16)));
                }
            }
        }
        else
        {
        	MoreCreepsReboot.proxy.addChatMessage("Too many obstructions, choose another spot!");
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

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getLivingSound()
    {
        if (getRidingEntity() == null)
        {
            if (rand.nextInt(5) == 0)
            {
                return SoundsHandler.GUINEA_PIG;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound()
    {
        return SoundsHandler.GUINEA_PIG_ANGRY;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.GUINEA_PIG_DEATH;
    }

    public void onDeath(Entity entity)
    {
        if (tamed)
        {
            return;
        }
        else
        {
            super.setHealth(0);
            entityDropItem(Items.PORKCHOP, 1);
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
}
