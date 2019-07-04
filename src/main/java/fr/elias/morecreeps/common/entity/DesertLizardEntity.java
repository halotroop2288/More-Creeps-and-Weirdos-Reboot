package fr.elias.morecreeps.common.entity;

import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DesertLizardEntity extends MobEntity
{
    public int fireballTime;
    private Entity targetedEntity;
    public float modelsize;
    static final String lizardTextures[] =
    {
        "morecreeps:textures/entity/desertlizard1.png", "morecreeps:textures/entity/desertlizard2.png", "morecreeps:textures/entity/desertlizard3.png", "morecreeps:textures/entity/desertlizard4.png", "morecreeps:textures/entity/desertlizard5.png"
    };
    public String texture;
    public DesertLizardEntity(World world)
    {
        super(null, world);
        texture = lizardTextures[rand.nextInt(lizardTextures.length)];
//        setSize(2.0F, 1.5F);
        fireballTime = rand.nextInt(300) + 250;
        modelsize = 1.0F;
        // this.targetTasks.addTask(0, new DesertLizardEntity.AIAttackEntity());
    }
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.55D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("ModelSize");
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere(World world)
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.SAND || i1 == Blocks.GRAVEL) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG
                && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//                && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(5) == 0 // && l > 10
        ;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk() {
        return 3;
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void livingTick()
    {
        World world = Minecraft.getInstance().world;
        PlayerEntity player = Minecraft.getInstance().player;
        if (fireballTime-- < 1) {
            fireballTime = rand.nextInt(300) + 250;
            double d = 64D;
            targetedEntity = world.getClosestPlayer(this, 30D);

            if (targetedEntity != null && canEntityBeSeen(targetedEntity) && (targetedEntity instanceof PlayerEntity)) {
                double d1 = targetedEntity.getDistanceSq(this);

                if (d1 < d * d && d1 > 10D) {
                    double d2 = targetedEntity.posX - posX;
                    double d3 = (targetedEntity.posZ - posZ) + 0.5D;
                    renderYawOffset = rotationYaw = (-(float)Math.atan2(d2, d3) * 180F) / (float)Math.PI;
                    world.playSound(player, this.getPosition(), SoundsHandler.DESERT_LIZARD_FIREBALL, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    DesertLizardFireballEntity creepsentitydesertlizardfireball = new DesertLizardFireballEntity(world);
                    double d5 = 4D;
                    Vec3d vec3d = getLook(1.0F);
                    creepsentitydesertlizardfireball.posX = posX + vec3d.x * d5;
                    creepsentitydesertlizardfireball.posY = posY + (double)(getHeight() / 2.0F) + 0.5D;
                    creepsentitydesertlizardfireball.posZ = posZ + vec3d.z * d5;
                    world.addEntity(creepsentitydesertlizardfireball);
                }
            }
        }

        super.livingTick();
    }

    /**
     * Plays living's sound at its position
     */
    public void playAmbientSound(World world)
    {
    	SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(this, s, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.DESERT_LIZARD;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.DESERT_LIZARD_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.DESERT_LIZARD_DEATH;
    }

    
    protected void attackEntity(Entity entity, float f)
    {
        if (f > 2.0F && f < 6F && rand.nextInt(10) == 0 && onGround)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f2 = MathHelper.sqrt(d * d + d1 * d1);
            moveForward = (float) ((d / (double)f2) * 0.5D * 0.8000000019209289D + getMotion().x * 0.20000000098023224D);
            moveStrafing = (float) ((d1 / (double)f2) * 0.5D * 0.70000000192092893D + getMotion().z * 0.20000000098023224D);
            moveVertical = (float) 0.40000000196046448D;
        }
    }
    class AIAttackEntity extends EntityAINearestAttackableTarget
    {
        public AIAttackEntity()
        {
            super(DesertLizardEntity.this, PlayerEntity.class, true);
        }
        
        public void updateTask()
        {
        	LivingEntity target = DesertLizardEntity.this.getAttackTarget();
        	float f = getDistance(target);
        	attackEntity(target, f);
        }
        
        public boolean shouldExecute()
        {
        	LivingEntity target = DesertLizardEntity.this.getAttackTarget();
            return target != null && super.shouldExecute();
        }
    }
    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
            if (rand.nextInt(5) == 0)
            {
                entityDropItem(Items.COOKED_PORKCHOP, rand.nextInt(3) + 1);
            }
            else
            {
                entityDropItem(Blocks.SAND), rand.nextInt(3) + 1);
            }
    	}

        super.onDeath(damagesource);
    }
}
