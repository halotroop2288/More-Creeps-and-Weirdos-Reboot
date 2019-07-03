package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ManDogEntity extends MobEntity
{
    private boolean foundplayer;

    /** The Entity this EntityCreature is set to attack. */
    protected Entity entityToAttack;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    protected ItemStack stolengood;
    private float distance;
    public int frisbeetime;
    public boolean chase;
    protected Entity frisbeeent;
    protected ItemStack frisbeestack;
    public boolean fetch;
    public boolean tamed;
    public int tamedfood;
    public int attempts;
    public double dist;
    public double prevdist;
    public int facetime;
    public boolean frisbeehold;
    public boolean superdog;
    public int superstate;
    public int supertimer;
    public double superX;
    public double superY;
    public double superZ;
    public boolean flapswitch;
    public boolean superfly;
    public int superdistance;
    public int superdistancetimer;
    public float speed;
    public double wayX;
    public double wayY;
    public double wayZ;
    public int waypoint;
    public int wayvert;
    public double distcheck;
    public double prevdistcheck;
    public boolean superflag;
    public double wayXa;
    public double wayYa;
    public double wayZa;
    public double wayXb;
    public double wayYb;
    public double wayZb;
    public float modelsize;

    public ResourceLocation texture;
    
    public ManDogEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES+ "mandog.png");
        hasAttacked = false;
        foundplayer = false;
        frisbeetime = 0;
        chase = false;
        fetch = false;
        tamed = false;
        tamedfood = rand.nextInt(3) + 1;
        attempts = 0;
        dist = 0.0D;
        prevdist = 0.0D;
        facetime = 0;
        frisbeehold = false;
        superdog = false;
        superstate = 0;
        supertimer = 0;
        superdistance = rand.nextInt(10) + 5;
        superdistancetimer = rand.nextInt(100) + 50;
        frisbeestack = new ItemStack(Items.STICK, 1);
        modelsize = 1.0F;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
        // this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(4, new EntityAILookIdle(this));
        // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        // this.targetTasks.addTask(2, new AIAttackEntity());
    }

    public void registerAttributes()
    {
    	super.registerAttributes();
        super.registerGoals();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(45D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.666D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putInt("Attempts", attempts);
        compound.putBoolean("Tamed", tamed);
        compound.putBoolean("FrisbeeHold", frisbeehold);
        compound.putBoolean("Chase", chase);
        compound.putBoolean("Fetch", fetch);
        compound.putBoolean("FoundPlayer", foundplayer);
        compound.putInt("TamedFood", tamedfood);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        attempts = compound.getInt("Attempts");
        tamed = compound.getBoolean("Tamed");
        frisbeehold = compound.getBoolean("FrisbeeHold");
        chase = compound.getBoolean("Chase");
        fetch = compound.getBoolean("Fetch");
        foundplayer = compound.getBoolean("FoundPlayer");
        tamedfood = compound.getInt("TamedFood");
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock() {
        if (superflag) {
            return false;
        } else {
            return super.isEntityInsideOpaqueBlock();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick(World world)
    {
        super.tick();

        if (tamed) {
            frisbeetime++;

            if (frisbeetime >= 20 && isAlive() && !chase && !fetch) {
                frisbeeent = null;
                List list = world.getEntitiesWithinAABBExcludingEntity(this,
                        getBoundingBox().expand(25D, 25D, 25D));

                for (int i = 0; i < list.size(); i++) {
                    Entity entity = (Entity) list.get(i);

                    if (entity instanceof FrisbeeEntity) {
                        frisbeeent = entity;
                        faceEntity(frisbeeent, 360F, 0.0F);
                        entityToAttack = frisbeeent;
                        chase = true;
                        attempts = 0;
                    }
                }
            }

            if (chase && (frisbeeent == null || frisbeeent.isAlive())) {
                chase = false;
                frisbeetime = 0;
            }

            if (chase) {
                if (frisbeeent != null) {
                    entityToAttack = frisbeeent;

                    if (Math.abs(posY - frisbeeent.posY) < 2D) {
                        faceEntity(frisbeeent, 360F, 0.0F);
                    }
                }

                moveEntityWithHeading((float) motionX, (float) motionZ);
                fallDistance = -25F;
                entityToAttack = frisbeeent;
                prevdist = dist;
                dist = frisbeeent.posX - posX;

                if (dist == prevdist) {
                    if (rand.nextInt(2) == 0) {
                        motionX += 0.75D;
                        motionZ += 0.75D;
                    } else {
                        motionX -= 0.75D;
                        motionZ -= 0.75D;
                    }
                }

                if (Math.abs(frisbeeent.posX - posX) < 1.0D && Math.abs(frisbeeent.posY - posY) < 1.0D && Math.abs(frisbeeent.posZ - posZ) < 1.0D)
                        {
                    world.playSound(playerentity, this.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    frisbeestack = new ItemStack(ItemList.frisbee, 1);
                    frisbeeent.remove();
                    chase = false;
                    fetch = true;
                    frisbeehold = true;
                    PlayerEntity playerentity = world.getClosestPlayer(this, 32D);

                    if (playerentity != null) {
                        frisbeeent = playerentity;
                        faceEntity(playerentity, 360F, 0.0F);
                    }
                }

                double d = frisbeeent.posX - posX;
                double d2 = frisbeeent.posZ - posZ;
                float f = MathHelper.sqrt(d * d + d2 * d2);
                motionX = (d / (double) f) * 0.40000000000000002D * 0.50000000192092897D + getMotion().x * 0.18000000098023225D;
                motionZ = (d2 / (double) f) * 0.40000000000000002D * 0.40000000192092894D + getMotion().z * 0.18000000098023225D;

                if (onGround) {
                    double d4 = (frisbeeent.posY - posY) * 0.18000000098023225D;

                    if (d4 > 0.5D) {
                        d4 = 0.5D;
                    }

                    motionY = d4;
                }

                if (Math.abs(frisbeeent.posX - posX) < 5D && Math.abs(frisbeeent.posZ - posZ) < 5D
                        && frisbeeent.getMotion().x == 0.0D)
                        {
                    attempts++;

                    if (attempts > 100) {
                        chase = false;
                        frisbeetime = 0;
                        frisbeeent = null;
                        fetch = true;
                        frisbeehold = false;
                        PlayerEntity playerentity1 = world.getClosestPlayer(this, 50D);

                        if (playerentity1 != null) {
                            frisbeeent = playerentity1;
                            faceEntity(playerentity1, 360F, 0.0F);
                        }
                    }
                }
            }

            if (fetch && frisbeeent != null)
            {
                if (Math.abs(frisbeeent.posX - posX) < 2D && Math.abs(frisbeeent.posY - posY) < 2D && Math.abs(frisbeeent.posZ - posZ) < 2D)
                    {
                        PlayerEntity playerentity = Minecraft.getInstance().player;
                        world.playSound(playerentity, this.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP,
                            SoundCategory.PLAYERS, 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

                    if (frisbeehold)
                        {
                        entityDropItem(ItemList.frisbee, 1);
                        }

                    chase = false;
                    fetch = false;
                    }

                fallDistance = -25F;
                double d1 = frisbeeent.posX - posX;
                double d3 = frisbeeent.posZ - posZ;
                float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3);
                motionX = (d1 / (double) f1) * 0.40000000000000002D * 0.50000000192092897D + getMotion().x * 0.18000000098023225D;
                motionZ = (d3 / (double) f1) * 0.40000000000000002D * 0.40000000192092894D + getMotion().z * 0.18000000098023225D;

                if (onGround) {
                    double d5 = (frisbeeent.posY - posY) * 0.18000000098023225D;

                    if (d5 > 0.5D) {
                        d5 = 0.5D;
                    }

                    motionY = d5;
                }
            }
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by
     * each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f) {
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        float f1 = MathHelper.sqrt(d * d + d1 * d1);
        motionX = (d / (double) f1) * 0.40000000000000002D * 0.30000000192092896D + motionX * 0.18000000098023225D;
        motionZ = (d1 / (double) f1) * 0.40000000000000002D * 0.44000000192092897D + motionZ * 0.18000000098023225D;
    }

    class AIAttackEntity extends Brain
    {
        public AIAttackEntity()
        {
            ManDogEntity.this.entityToAttack = ManDogEntity.this.getAttackTarget();
        }

        @Override
        public boolean shouldExecute()
        {
            return !(ManDogEntity.this.entityToAttack instanceof PlayerEntity) || !ManDogEntity.this.tamed;
        }

        public void updateTask() {
            float f = ManDogEntity.this.getDistance(getAttackTarget());
            if (f < 256F) {
                attackEntity(ManDogEntity.this.getAttackTarget(), f);
                ManDogEntity.this.getLookController().setLookPositionWithEntity(ManDogEntity.this.entityToAttack, 10.0F,
                        10.0F);
                ManDogEntity.this.getNavigator().clearPath();
                ManDogEntity.this.getMoveHelper().setMoveTo(ManDogEntity.this.entityToAttack.posX,
                        ManDogEntity.this.entityToAttack.posY, ManDogEntity.this.entityToAttack.posZ, 0.5D);
            }
            if (f < 1F) {
                ManDogEntity.this.attackEntityAsMob(ManDogEntity.this.entityToAttack);
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Object obj = damagesource.getTrueSource();

        if (obj != null && (obj instanceof RocketEntity)) {
            obj = world.getClosestPlayer(this, 30D);
        }

        super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
        return true;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity
     * isn't interested in attacking (Animals, Spiders at day, peaceful PigZombies).
     */
    protected boolean findPlayerToAttack()
    {
        if (tamed) {
            entityToAttack = null;
            return false;
        } else {
            return false;
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets
     * into the saddle on a pig.
     */
    public boolean interact(PlayerEntity playerentity) {
        ItemStack itemstack = playerentity.inventory.getCurrentItem();

        if (itemstack == null && tamed) {
            chase = false;
            fetch = false;
            frisbeeent = null;
        }

        if (itemstack != null) {
            if (itemstack.getItem() == Items.COOKED_PORKCHOP) {
                tamedfood--;
                smoke();

                if (tamedfood < 1) {
                    smoke();
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.MAN_DOG_TAMED, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    tamed = true;
                    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(45D);
                    texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "mandogtamed.png");
                }
            }

            if (itemstack.getItem() == Items.BONE) {
                this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(45D);
            }
        }

        return true;
    }

    private void smoke() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                double d = rand.nextGaussian() * 0.059999999999999998D;
                double d1 = rand.nextGaussian() * 0.059999999999999998D;
                double d2 = rand.nextGaussian() * 0.059999999999999998D;
                world.addParticle(ParticleTypes.LARGE_SMOKE,
                        (posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(),
                        posY + (double) (rand.nextFloat() * getHeight()) + (double) i,
                        (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(), d, d1, d2);
            }
        }
    }

    /**
     * Plays living's sound at its position
     */
    public void playAmbientSound(World world)
    {
        SoundEvent s = getAmbientSound();

        if (s != null) {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(),
                    (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getAmbientSound() {
        if (superdog) {
            return SoundsHandler.SUPER_DOG_NAME;
        } else {
            return SoundsHandler.MAN_DOG;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound(DamageSource damagesourcIn)
    {
        return SoundsHandler.MAN_DOG_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound() {
        return SoundsHandler.MAN_DOG_DEATH;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this
     * entity.
     */
    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        // int l = world.getFullBlockLightValue(i, j, k);
        BlockPos bp = new BlockPos(i, j, k);
        Block i1 = world.getBlockState(bp.down()).getBlock();
        return i1 != Blocks.SAND && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.SMOOTH_STONE_SLAB
                && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
                // && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.canBlockSeeSky(bp) && rand.nextInt(25) == 0; // && l > 10;
    }
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        if (tamed)
        {
            this.isAlive();
            deathTime = 0;
            return;
        }
        else
        {
            this.setHealth(0);
            return;
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
        if (tamed)
        {
            return;
        }
        else
        {
            smoke();
            if(!world.isRemote)
            {
                entityDropItem(Items.BONE, rand.nextInt(5));
            }
            super.onDeath(damagesource);
            return;
        }
    }
}
