package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

@SuppressWarnings("rawtypes")
public class TombstoneEntity extends AnimalEntity
{
    public int interest;
    // private boolean primed;
    public boolean tamed;
    public int basehealth;
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
    public int attackStrength;
    public float modelsize;
    public boolean heavenbuilt;
    public boolean angrydog;
    public int firenum;
    public int firepower;
    public int healtimer;
    public LivingEntity owner;
    public float dogsize;
    public String name;
    public int skillattack;
    public int skilldefend;
    public int skillhealing;
    public int skillspeed;
    public String deathtype;
    public String basetexture;
    public ResourceLocation texture;
    public float width = getWidth();
    public float length = getWidth();
    public float height = getHeight();

    public TombstoneEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "tombstone.png");
        basetexture = "";
        interest = 0;
        tamed = false;
        basehealth = 0;
        used = false;
        grab = false;
        pigstack = 0;
        level = 0;
        totaldamage = 0.0F;
        hotelbuilt = false;
        wanderstate = 0;
        speedboost = 0;
        totalexperience = 0;
        baseSpeed = 0.0F;
        modelsize = 1.0F;
        heavenbuilt = false;
        angrydog = false;
        firenum = 0;
        firepower = 0;
        healtimer = 0;
        owner = null;
        attackStrength = 0;
        dogsize = 0.0F;
        name = "";
        skillattack = 0;
        skilldefend = 0;
        skillhealing = 0;
        skillspeed = 0;
        deathtype = "";
    }
    
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
    }


    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public AnimalEntity createChild(AgeableEntity entityanimal, World world)
    {
        return new TombstoneEntity(world);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean processInteract(PlayerEntity player, Hand hand)
    {
        PlayerEntity playerentity = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;
        ItemStack itemstack = playerentity.inventory.getCurrentItem();
        used = false;

        if (itemstack == null)
        {
            playerentity.sendMessage(new StringTextComponent("Use a " + I18n.format("item.life_gem.name") + " on this tombstone to bring your pet back to life!"));
            return false;
        }

        if (itemstack != null && itemstack.getItem() != ItemList.life_gem)
        {
        	playerentity.sendMessage(new StringTextComponent("Use a " + I18n.format("item.life_gem.name") + " on this tombstone to bring your pet back to life!"));
            return false;
        }

        if (itemstack != null && itemstack.getItem() == ItemList.life_gem)
        {
            itemstack.setCount(itemstack.getCount() - 1);
            playerentity.swingArm(Hand.MAIN_HAND);;

            if (itemstack.getCount() < 1)
            {
                playerentity.inventory.setInventorySlotContents(playerentity.inventory.currentItem, null);
                itemstack.setCount(0);
            }

            smoke(world);

            if (deathtype.equals("GuineaPig"))
            {
                GuineaPigEntity creepsentityguineapig = new GuineaPigEntity(world);
                creepsentityguineapig.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                creepsentityguineapig.interest = interest;
                creepsentityguineapig.tamed = tamed;
                creepsentityguineapig.name = name;
                creepsentityguineapig.basehealth = basehealth;
                creepsentityguineapig.level = level;
                creepsentityguineapig.basetexture = basetexture;
                creepsentityguineapig.totaldamage = totaldamage;
                creepsentityguineapig.hotelbuilt = hotelbuilt;
                creepsentityguineapig.attackStrength = attackStrength;
                creepsentityguineapig.wanderstate = wanderstate;
                creepsentityguineapig.speedboost = speedboost;
                creepsentityguineapig.totalexperience = totalexperience;
                creepsentityguineapig.baseSpeed = baseSpeed;
                creepsentityguineapig.health = 5;
                creepsentityguineapig.modelsize = modelsize;
                creepsentityguineapig.skillattack = skillattack;
                creepsentityguineapig.skilldefend = skilldefend;
                creepsentityguineapig.skillhealing = skillhealing;
                creepsentityguineapig.skillspeed = skillspeed;
                creepsentityguineapig.texture = basetexture;

                if (wanderstate == 1)
                {
                    creepsentityguineapig.moveSpeed = 0.0F;
                }
                else
                {
                    creepsentityguineapig.moveSpeed = speedboost <= 0 ? baseSpeed : baseSpeed + 0.5F;
                }

                world.addEntity(creepsentityguineapig);
                setHealth(0);
            }

            if (deathtype.equals("Hotdog"))
            {
                HotdogEntity creepsentityhotdog = new HotdogEntity(world);
                creepsentityhotdog.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                creepsentityhotdog.interest = interest;
                creepsentityhotdog.tamed = tamed;
                creepsentityhotdog.name = name;
                creepsentityhotdog.basehealth = basehealth;
                creepsentityhotdog.level = level;
                creepsentityhotdog.basetexture = basetexture;
                creepsentityhotdog.totaldamage = totaldamage;
                creepsentityhotdog.heavenbuilt = heavenbuilt;
                creepsentityhotdog.attackStrength = attackStrength;
                creepsentityhotdog.wanderstate = wanderstate;
                creepsentityhotdog.speedboost = speedboost;
                creepsentityhotdog.totalexperience = totalexperience;
                creepsentityhotdog.baseSpeed = baseSpeed;
                creepsentityhotdog.skillattack = skillattack;
                creepsentityhotdog.skilldefend = skilldefend;
                creepsentityhotdog.skillhealing = skillhealing;
                creepsentityhotdog.skillspeed = skillspeed;
                creepsentityhotdog.firepower = firepower;
                creepsentityhotdog.dogsize = dogsize;
                creepsentityhotdog.health = 5;
                creepsentityhotdog.texture = basetexture;

                if (wanderstate == 1)
                {
                    creepsentityhotdog.moveSpeed = 0.0F;
                }
                else
                {
                    creepsentityhotdog.moveSpeed = speedboost <= 0 ? baseSpeed : baseSpeed + 0.5F;
                }

                if(!world.isRemote)
                world.addEntity(creepsentityhotdog);
                
                setHealth(0);
            }
        }

        return true;
    }

    private void smoke(World world)
    {
        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 30; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                double d4 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.HEART, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d2, d4);
            }

            for (int k = 0; k < 4; k++)
            {
                for (int l = 0; l < 10; l++)
                {
                    double d1 = rand.nextGaussian() * 0.02D;
                    double d3 = rand.nextGaussian() * 0.02D;
                    double d5 = rand.nextGaussian() * 0.02D;
                    world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height) + (double)k, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d1, d3, d5);
                }
            }
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void tick()
    {
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (rand.nextInt(10) == 0)
        {
            return SoundsHandler.TOMBSTONE;
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
        return null;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return null;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putString("DeathType", deathtype);
        compound.putInt("Interest", interest);
        compound.putBoolean("Tamed", tamed);
        compound.putString("Name", name);
        compound.putInt("BaseHealth", basehealth);
        compound.putInt("Level", level);
        compound.putString("BaseTexture", basetexture);
        compound.putFloat("TotalDamage", totaldamage);
        compound.putBoolean("heavenbuilt", heavenbuilt);
        compound.putBoolean("hotelbuilt", hotelbuilt);
        compound.putInt("AttackStrength", attackStrength);
        compound.putInt("WanderState", wanderstate);
        compound.putInt("SpeedBoost", speedboost);
        compound.putInt("TotalExperience", totalexperience);
        compound.putFloat("BaseSpeed", baseSpeed);
        compound.putInt("SkillAttack", skillattack);
        compound.putInt("SkillDefense", skilldefend);
        compound.putInt("SkillHealing", skillhealing);
        compound.putInt("SkillSpeed", skillspeed);
        compound.putInt("FirePower", firepower);
        compound.putFloat("DogSize", dogsize);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        deathtype = compound.getString("DeathType");
        interest = compound.getInt("Interest");
        tamed = compound.getBoolean("Tamed");
        name = compound.getString("Name");
        basetexture = compound.getString("BaseTexture");
        basehealth = compound.getInt("BaseHealth");
        level = compound.getInt("Level");
        totaldamage = compound.getFloat("TotalDamage");
        heavenbuilt = compound.getBoolean("heavenbuilt");
        hotelbuilt = compound.getBoolean("hotelbuilt");
        attackStrength = compound.getInt("AttackStrength");
        wanderstate = compound.getInt("WanderState");
        speedboost = compound.getInt("SpeedBoost");
        totalexperience = compound.getInt("TotalExperience");
        baseSpeed = compound.getFloat("BaseSpeed");
        skillattack = compound.getInt("SkillAttack");
        skilldefend = compound.getInt("SkillDefense");
        skillhealing = compound.getInt("SkillHealing");
        skillspeed = compound.getInt("SkillSpeed");
        firepower = compound.getInt("FirePower");
        dogsize = compound.getFloat("DogSize");
        modelsize = compound.getFloat("ModelSize");
    }

    public void onDeath(Entity entity)
    {
    }

   /**
    * Makes the entity despawn if requirements are reached
    */
    @Override
    protected void checkDespawn()
    {
        // Simply blanking this *should* prevent despawning...
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    @Override
    public boolean isEntityInsideOpaqueBlock()
    {
        return false;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }
}
// ZERO ERRORS! YAY!!!!