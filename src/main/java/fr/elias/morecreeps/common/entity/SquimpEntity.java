package fr.elias.morecreeps.common.entity;

import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SquimpEntity extends WaterMobEntity
{
    protected Entity playerToAttack;
    public float height = getHeight();
    public float width = getWidth();
    public float length = getWidth();
    public boolean stolen;
    public boolean foundplayer;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    protected ItemStack stolengood;
    private float distance;
    public int itemnumber;
    public int stolenamount;
    public String texture;
    public double moveSpeed;
    public double health;
    public double motionX = getMotion().x;
    public double motionY = getMotion().y;
    public double motionZ = getMotion().z;

    public SquimpEntity(World world)
    {
        super(null, world);
        texture = "/mob/creeps/squimp.png";
        moveSpeed = 0.0F;
        health = rand.nextInt(20) + 10;
        stolen = false;
        hasAttacked = false;
        foundplayer = false;
        // tasks.addTask(6, new EntityAIWatchClosest(this, PlayerEntity.class, 8F));
    }

    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(moveSpeed);
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack(World world)
    {
        PlayerEntity playerentity = world.getClosestPlayer(this, 20D);

        if (playerentity != null)
        {
            distance = getDistance(playerentity);

            if (distance < 16F && inWater)
            {
                motionY += 2D;

                for (int i = 0; i < 4; i++)
                {
                    float f = 0.25F;
                    world.addParticle(ParticleTypes.BUBBLE, posX - motionX * (double)f, posY - motionY * (double)f, posZ - motionZ * (double)f, motionX, motionY, motionZ);
                }

                for (int j = 0; j < 3; j++)
                {
                    double xDif = playerentity.posX - posX;
                    // double yDif = (playerentity.getBoundingBox().minY + (double) (playerentity.getHeight() / 2.0F)) - (posY + (double) (height / 2.0F));
                    double zDif = (playerentity.posZ - posZ) + 0.5D;
                    renderYawOffset = rotationYaw = (-(float) Math.atan2(xDif, zDif) * 180F) / (float) Math.PI;
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.DESERT_LIZARD_FIREBALL, SoundCategory.HOSTILE, getSoundVolume(),
                            (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    DesertLizardFireballEntity creepsentitydesertlizardfireball = new DesertLizardFireballEntity(world);
                    double d3 = 4D;
                    Vec3d vec3d = getLook(1.0F);
                    creepsentitydesertlizardfireball.posX = posX + vec3d.x * d3;
                    creepsentitydesertlizardfireball.posY = posY + (double) (height / 2.0F) + 0.5D + 1.0D;
                    creepsentitydesertlizardfireball.posZ = posZ + vec3d.z * d3 + (double) (1 - j);
                    world.addEntity(creepsentitydesertlizardfireball);
                }
            }
        }

        return null;
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void livingTick() {
        super.livingTick();
        motionY *= 0.87999999523162842D;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.THIEF;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn) {
        return SoundsHandler.THIEF_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.THIEF_DEATH;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this
     * entity.
     */
    public boolean getCanSpawnHere(World world) {
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
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource)
    {
        entityDropItem(Items.COD, rand.nextInt(2) + 1);
        super.onDeath(damagesource);
    }
}
