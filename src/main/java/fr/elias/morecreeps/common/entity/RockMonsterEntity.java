package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.entity.RobotToddEntity.AIAttackEntity;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class RockMonsterEntity extends MobEntity
{
    private static final Item dropItems[];
    protected double attackRange;
    private int angerLevel;
    public float modelsize;
    public String texture;
    public double motionX = getMotion().x;
    public double motionY = getMotion().y;
    public double motionZ = getMotion().z;
    public float width = getWidth();
    public float length = getWidth();
    public float height = getHeight();

    public RockMonsterEntity(World world)
    {
        super(null, world);
        texture = "morecreeps:textures/entity/rockmonster.png";
        angerLevel = 0;
        attackRange = 16D;
        // setSize(width * 3.25F, height * 3.25F);
        height = 4F;
        width = 3F;
        modelsize = 3F;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // tasks.addTask(0, new EntityAISwimming(this));
        // tasks.addTask(2, new AIAttackEntity()); 
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
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
                .setBaseValue(this.getAttackTarget() != null ? 0.6D : 0.35D);
        super.tick();

        if (motionY > 0.0D)
        {
            motionY -= 0.00033000000985339284D;
        }
        if (angerLevel > 0)
        {
            angerLevel--;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        float f1 = MathHelper.sqrt(d * d + d1 * d1);
        motionX = (d / (double)f1) * 0.5D * 0.30000000192092896D + motionX * 0.38000000098023223D;
        motionZ = (d1 / (double)f1) * 0.5D * 0.17000000192092896D + motionZ * 0.38000000098023223D;
    }
    public class AIAttackEntity extends Brain
    {

    	public RockMonsterEntity rockM = RockMonsterEntity.this;
    	public int attackTime;
    	public AIAttackEntity() {}
    	
		@Override
		public boolean shouldExecute() {
            LivingEntity entitylivingbase = this.rockM.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isAlive() && angerLevel > 0;
		}
        public void updateTask()
        {
        	--attackTime;
            LivingEntity entitylivingbase = this.rockM.getAttackTarget();
            double d0 = this.rockM.getDistanceSq(entitylivingbase);

            if (d0 < 4.0D)
            {
                if (this.attackTime <= 0)
                {
                    this.attackTime = 10;
                    entitylivingbase.motionX = motionX * 3D;
                    entitylivingbase.motionY = rand.nextFloat() * 2.533F;
                    entitylivingbase.motionZ = motionZ * 3D;
                    this.rockM.attackEntityAsMob(entitylivingbase); // or entitylivingbase.attackEntityFrom blablabla...
                }
                
                this.rockM.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            else if (d0 < 256.0D)
            {
                // ATTACK ENTITY JUST CALLED HERE :D
            	rockM.attackEntity(entitylivingbase, (float)d0);
                this.rockM.getLookController().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            }
            else
            {
                this.rockM.getNavigator().clearPath();
                this.rockM.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
            }
        }
    }
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("ModelSize");
    }
    
    public boolean getCanSpawnHere(World world)
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getLight(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG
        && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.STONE_SLAB
        && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
        // && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(15) == 0 && l > 8;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @SuppressWarnings("rawtypes")
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getTrueSource();
        if (entity != null) {
            if (entity instanceof PlayerEntity) {
                List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(32D, 32D, 32D));

                for (int j = 0; j < list.size(); j++)
                {
                    Entity entity1 = (Entity)list.get(j);

                    if (entity1 instanceof RockMonsterEntity)
                    {
                        RockMonsterEntity creepsentityrockmonster = (RockMonsterEntity)entity1;
                        creepsentityrockmonster.becomeAngryAt(entity);
                    }
                }

                becomeAngryAt(entity);
            }
    	}

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    private void becomeAngryAt(Entity entity)
    {
        this.setAttackTarget((LivingEntity) entity);
        angerLevel = 400 + rand.nextInt(400);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.ROCK_MONSTER;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.ROCK_MONSTER_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.ROCK_MONSTER_DEATH;
    }

    /**
     * Plays living's sound at its position
     */
    public void playAmbientSound(World world)
    {
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(player, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (3F - modelsize));
        }
    }

    public void onDeath(DamageSource damagesource)
    {
        // Entity entity = damagesource.getTrueSource();

        // if (entity != null && (entity instanceof PlayerEntity) && !((ServerPlayerEntity)entity).getStatFile().hasAchievementUnlocked(ModAdvancementList.rockmonster))
        // {
        //     world.playSound(player, entity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
        //     ((PlayerEntity) entity).addStat(ModAdvancementList.achieverockmonster, 1);
        // }

        super.onDeath(damagesource);
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    @Override
    public Item getDropItem()
    {
    	return dropItems[rand.nextInt(dropItems.length)];
    }

    static
    {
        dropItems = (new Item[]
                {
                    Item.getItemFromBlock(Blocks.COBBLESTONE), Item.getItemFromBlock(Blocks.GRAVEL), Item.getItemFromBlock(Blocks.COBBLESTONE), Item.getItemFromBlock(Blocks.GRAVEL), Item.getItemFromBlock(Blocks.IRON_ORE), Item.getItemFromBlock(Blocks.MOSSY_COBBLESTONE)
                });
    }
}
