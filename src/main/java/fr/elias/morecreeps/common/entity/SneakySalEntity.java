package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.gui.SneakySalGUI;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.lists.ParticleList;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.RockMonsterEntity.AIAttackEntity;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class SneakySalEntity extends MobEntity
{
	World world;
	PlayerEntity playerentity;
    public int salslots[];
    public static final Item[] salitems;
    public static final int salprices[] =
    {
        10, 200, 100, 20, 175, 150, 225, 50, 350, 100,
        150, 10, 200, 150, 250
    };
    public static final String saldescriptions[] =
    {
        "BLORP COLA", "ARMY GEM", "HORSE HEAD GEM", "BAND AID", "SHRINK RAY", "EXTINGUISHER", "GROW RAY", "FRISBEE", "LIFE GEM", "GUN",
        "RAYGUN", "POPSICLE", "EARTH GEM", "FIRE GEM", "SKY GEM"
    };
    public static final ItemStack itemstack[];
    // private boolean foundplayer;
    // private PathNavigator pathToEntity;
    protected Entity playerToAttack;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    // private float distance;
    public boolean tamed;
    public int basehealth;
    public int tamedfood;
    public int attempts;
    public double dist;
    public double prevdist;
    public int facetime;
    public ResourceLocation basetexture;
    public int rockettime;
    public int rocketcount;
    public int galloptime;
    public String name;
    public int sale;
    public float saleprice;
    public int dissedmax;
    public ItemStack defaultHeldItem;
    private Entity targetedEntity;
    public int bulletTime;
    public float modelsize;
    public boolean shooting;
    public int shootingdelay;
    public int itemused;
    public int itemnew;
    public ResourceLocation texture;
    public double moveSpeed;
    public double attackStrength;
    public double health;
    public float length = getWidth();
    public float width = getWidth();
    public float height = getHeight();

    public SneakySalEntity(World world)
    {
        super(null, world);
        salslots = new int[30];
        basetexture = new ResourceLocation("/mob/creeps/sneakysal.png");
        texture = basetexture;
        moveSpeed = 0.65F;
        attackStrength = 3;
        basehealth = rand.nextInt(50) + 50;
        health = basehealth;
        hasAttacked = false;
        // foundplayer = false;
        // setSize(1.5F, 4F);
        dissedmax = rand.nextInt(4) + 1;
        defaultHeldItem = new ItemStack(ItemList.gun, 1);
        sale = rand.nextInt(2000) + 100;
        saleprice = 0.0F;
        shooting = false;
        shootingdelay = 20;
        modelsize = 1.5F;
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
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT nbttagcompound)
    {
        super.writeAdditional(nbttagcompound);
        nbttagcompound.putInt("Sale", sale);
        nbttagcompound.putInt("DissedMax", dissedmax);
        nbttagcompound.putFloat("SalePrice", saleprice);
        nbttagcompound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        sale = compound.getInt("Sale");
        dissedmax = compound.getInt("DissedMax");
        saleprice = compound.getFloat("SalePrice");
        modelsize = compound.getFloat("ModelSize");
        saleprice = 0.0F;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
        if (playerToAttack instanceof SneakySalEntity)
        {
            playerToAttack = null;
        }

        super.tick();
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(PlayerEntity playerentity)
    {
        ItemStack itemstack1 = playerentity.inventory.getCurrentItem();

        if (dissedmax > 0)
        {
            if (saleprice == 0.0F || sale < 1)
            {
                restockSal();
            }

            if (dissedmax > 0 && !(playerToAttack instanceof PlayerEntity))
            {
                playerentity.openGui(MoreCreepsReboot.instance, 6, world, (int)this.posX, (int)this.posY, (int)this.posZ);
            }
        }

        return false;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        if (dissedmax < 1)
        {
            PlayerEntity playerentity = world.getClosestPlayer(this, 16D);

            if (playerentity != null && canEntityBeSeen(playerentity)) {
                return playerentity;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by
     * each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f) {
        if (dissedmax < 1) {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d1 * d1);
            moveForward = (float) ((d / (double) f1) * 0.40000000000000002D * 0.20000000192092895D + getMotion().x * 0.18000000098023225D);
            moveStrafing = (float) ((d1 / (double) f1) * 0.40000000000000002D * 0.14000000192092896D + getMotion().z * 0.18000000098023225D);

            if ((double) f < 2.7999999999999998D
                    && entity.getBoundingBox().maxY > this.getBoundingBox().minY
                    && entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                // attackTime = 10;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) attackStrength);
            }

            super.attackEntityAsMob(entity);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i) {
        Entity entity = damagesource.getTrueSource();

        if (entity instanceof PlayerEntity) {
            dissedmax = 0;
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void livingTick() {
        if (shootingdelay-- < 1) {
            shooting = false;
        }

        targetedEntity = world.getClosestPlayer(this, 3D);

        if (targetedEntity != null && (targetedEntity instanceof PlayerEntity) && canEntityBeSeen(targetedEntity))
        {

            for (int i = 0; i < 360; i++) {
                rotationYaw = i;
            }

            if (rand.nextInt(4) == 0) {
                attackEntity(targetedEntity, 1.0F);
            }
        }

        if (bulletTime-- < 1 && dissedmax < 1) {
            bulletTime = rand.nextInt(50) + 25;
            double d = 64D;
            targetedEntity = world.getClosestPlayer(this, 30D);

            if (targetedEntity != null && canEntityBeSeen(targetedEntity) && (targetedEntity instanceof PlayerEntity)
                    && !dead && !(targetedEntity instanceof SneakySalEntity)
                    && !(targetedEntity instanceof RatManEntity)) {
                double d2 = targetedEntity.getDistanceSq(this);

                if (d2 < d * d && d2 > 3D) {
                    double d4 = targetedEntity.posX - posX - (posY + (double) (height / 2.0F));
                    double d6 = targetedEntity.posZ - posZ;
                    renderYawOffset = rotationYaw = (-(float) Math.atan2(d4, d6) * 180F) / (float) Math.PI;
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.BULLET, SoundCategory.NEUTRAL, 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
                    shooting = true;
                    shootingdelay = 10;
                    BulletEntity creepsentitybullet = new BulletEntity(world, this, 0.0F);

                    if (creepsentitybullet != null) {
                        world.addEntity(creepsentitybullet);
                    }
                }
            }
        }

        sale--;

            // Commented out until I figure out FX in this version.
        // if (rand.nextInt(10) == 0) {
        //     double d1 = -MathHelper.sin((rotationYaw * (float) Math.PI) / 180F);
        //     double d3 = MathHelper.cos((rotationYaw * (float) Math.PI) / 180F);
        //     CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, posX + d1 * 0.5D, posY + 2D, posZ + d3 * 0.5D,
        //             ParticleList.CREEPS_WHITE, 0.5F, 0.5F);
        //     creepsfxsmoke.renderDistanceWeight = 15D;
        //     Minecraft.getInstance().effectRenderer.addEffect(creepsfxsmoke);
        // }

        if (dissedmax < 1 && playerToAttack == null) {
            findPlayerToAttack();
        }

        super.livingTick();
    }

    public void restockSal() {
        sale = rand.nextInt(2000) + 100;
        saleprice = 1.0F - (rand.nextFloat() * 0.25F - rand.nextFloat() * 0.25F);
        itemnew = rand.nextInt(salitems.length);
        itemused = 0;

        for (int i = 0; i < salitems.length; i++) {
            salslots[i] = i;
        }

        for (int j = 0; j < salitems.length; j++) {
            int k = rand.nextInt(salitems.length);
            int l = salslots[j];
            salslots[j] = salslots[k];
            salslots[k] = l;
        }
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem() {
        return defaultHeldItem;
    }

    private void smoke() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                double d = rand.nextGaussian() * 0.059999999999999998D;
                double d1 = rand.nextGaussian() * 0.059999999999999998D;
                double d2 = rand.nextGaussian() * 0.059999999999999998D;
                world.addParticle(ParticleTypes.LARGE_SMOKE,
                        (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width,
                        posY + (double) (rand.nextFloat() * height) + (double) i,
                        (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
            }
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound() {
        if (rand.nextInt(10) == 0) {
            return SoundsHandler.GIRAFFE; // Why though?
        } else {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundsHandler.SAL_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound() {
        return SoundsHandler.SAL_DEATH;
    }

    // /**
    //  * Checks if the entity's current position is a valid location to spawn this
    //  * entity.
    //  */
    // @Override
    // public boolean getCanSpawnHere() {
    //     int i = MathHelper.floor(posX);
    //     int j = MathHelper.floor(this.getBoundingBox().minY);
    //     int k = MathHelper.floor(posZ);
    //     int l = world.getLight(new BlockPos(i, j, k));
    //     Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
    //     return i1 != Blocks.SNOW && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
    //             && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
    //             && world.checkBlockCollision(getBoundingBox()) && world.canBlockSeeSky(new BlockPos(i, j, k))
    //             && rand.nextInt(15) == 0 && l > 8;
    // }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Called when the mob's health reaches 0.
     */
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

    static
    {
        salitems = (new Item[]
                {
                    ItemList.blorp_cola, ItemList.army_gem, ItemList.horse_head_gem, ItemList.band_aid, ItemList.shrink_ray, ItemList.extinguisher, ItemList.grow_ray, ItemList.frisbee, ItemList.life_gem, ItemList.gun,
                    ItemList.ray_gun, ItemList.popsicle, ItemList.earth_gem, ItemList.fire_gem, ItemList.sky_gem
                }
                );
        itemstack = (new ItemStack[]
                {
                    new ItemStack(ItemList.blorp_cola), new ItemStack(ItemList.army_gem), new ItemStack(ItemList.horse_head_gem), new ItemStack(ItemList.band_aid), new ItemStack(ItemList.shrink_ray),
                    new ItemStack(ItemList.extinguisher), new ItemStack(ItemList.grow_ray), new ItemStack(ItemList.frisbee), new ItemStack(ItemList.life_gem), new ItemStack(ItemList.gun),
                    new ItemStack(ItemList.ray_gun), new ItemStack(ItemList.popsicle), new ItemStack(ItemList.earth_gem), new ItemStack(ItemList.fire_gem), new ItemStack(ItemList.sky_gem)
                }
                );
    }
}
