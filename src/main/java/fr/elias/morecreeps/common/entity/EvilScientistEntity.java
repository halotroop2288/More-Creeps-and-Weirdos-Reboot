package fr.elias.morecreeps.common.entity;

import java.util.Random;

import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Explosion.Mode;

public class EvilScientistEntity extends MobEntity
{
    //public int basehealth;
    public int weapon;
    public boolean used;
    public int interest;
    public String ss;
    public float distance;
    public ResourceLocation basetexture;
    public int experimenttimer;
    public boolean experimentstart;
    public int stage;
    public int towerX;
    public int towerY;
    public int towerZ;
    public int towerHeight;
    public boolean water;
    public Block area;
    public int numexperiments;
    public boolean trulyevil;
    public boolean towerBuilt;
    public float modelsize;
    public ResourceLocation texture;
    private Random rand;

    public EvilScientistEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "evilscientist.png");
        basetexture = texture;
        experimenttimer = rand.nextInt(100) + 100;
        experimentstart = false;
        stage = 0;
        water = false;
        trulyevil = false;
        towerBuilt = false;
        numexperiments = rand.nextInt(3) + 1;
        // isImmuneToFire = true;
        modelsize = 1.0F;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(4, new EvilScientistEntity.AITargetingSystem());
        // this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        // this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(8, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        // this.tasks.addTask(8, new EntityAILookIdle(this));
        // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
//        if(trulyevil)
//        {
//            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, PlayerEntity.class, true));
//        }
    }
    
    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT nbttagcompound)
    {
        super.writeAdditional(nbttagcompound);
        nbttagcompound.putInt("ExperimentTimer", experimenttimer);
        nbttagcompound.putBoolean("ExperimentStart", experimentstart);
        nbttagcompound.putInt("Stage", stage);
        nbttagcompound.putBoolean("Water", water);
        nbttagcompound.putInt("NumExperiments", numexperiments);
        nbttagcompound.putInt("TowerX", towerX);
        nbttagcompound.putInt("TowerY", towerY);
        nbttagcompound.putInt("TowerZ", towerZ);
        nbttagcompound.putInt("TowerHeight", towerHeight);
        nbttagcompound.putBoolean("TrulyEvil", trulyevil);
        nbttagcompound.putBoolean("TowerBuilt", towerBuilt);
        nbttagcompound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT nbttagcompound)
    {
        super.readAdditional(nbttagcompound);
        experimenttimer = nbttagcompound.getInt("ExperimentTimer");
        experimentstart = nbttagcompound.getBoolean("ExperimentStart");
        stage = nbttagcompound.getInt("Stage");
        water = nbttagcompound.getBoolean("Water");
        numexperiments = nbttagcompound.getInt("NumExperiments");
        towerX = nbttagcompound.getInt("TowerX");
        towerY = nbttagcompound.getInt("TowerY");
        towerZ = nbttagcompound.getInt("TowerZ");
        towerHeight = nbttagcompound.getInt("TowerHeight");
        trulyevil = nbttagcompound.getBoolean("TrulyEvil");
        towerBuilt = nbttagcompound.getBoolean("TowerBuilt");
        modelsize = nbttagcompound.getFloat("ModelSize");

        if (trulyevil) {
            texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "evilscientistblown.png");
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void livingTick()
    {
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	World world = Minecraft.getInstance().world;
    	
        if (trulyevil) {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.75D);
        } else {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
        }
        fallDistance = 0.0F;

        if (stage == 3 && posY + 3D < (double) (towerY + towerHeight))
        {
            stage = 2;
        }

        if (stage == 0) {
            if (experimenttimer > 0 && !experimentstart)
            {
                experimenttimer--;
            }

            if (experimenttimer == 0)
            {
                experimentstart = true;
                stage = 1;
                experimenttimer = rand.nextInt(5000) + 100;
            }
        }

        if (stage == 1 && onGround && experimentstart && posY > 63D)
        {
            towerX = MathHelper.floor(posX) + 2;
            towerY = MathHelper.floor(posY);
            towerZ = MathHelper.floor(posZ) + 2;
            towerHeight = rand.nextInt(20) + 10;
            area = Blocks.AIR;

            /*
             * for (int i = 0; i < towerHeight; i++) { for (int i2 = 0; i2 < 3; i2++) { for
             * (int l3 = 0; l3 < 3; l3++) { area += world.getBlockState(new BlockPos(towerX
             * + l3, towerY + i, towerZ + i2 + 1)).getBlock(); area +=
             * world.getBlockState(new BlockPos(towerX + i2 + 1, towerY + i, towerZ +
             * l3)).getBlock(); } } }
             */

            for (int i = 0; i < towerHeight; i++)
            {
                for (int i2 = 0; i2 < 3; i2++)
                {
                    for (int l3 = 0; l3 < 3; l3++)
                    {
                        area = world.getBlockState(new BlockPos(towerX + l3, towerY + i, towerZ + i2 + 1)).getBlock();
                        area = world.getBlockState(new BlockPos(towerX + i2 + 1, towerY + i, towerZ + l3)).getBlock();
                    }
                }
            }

            if (posY > 63D && area == Blocks.AIR
                    && world.getBlockState(new BlockPos(towerX + 2, towerY - 1, towerZ + 2)).getBlock() != Blocks.AIR
                    && world.getBlockState(new BlockPos(towerX + 2, towerY - 1, towerZ + 2))
                            .getBlock() != Blocks.WATER)
            {
                towerBuilt = true;

                for (int j = 0; j < towerHeight; j++)
                {
                    for (int j2 = 0; j2 < 3; j2++) {
                        for (int i4 = 0; i4 < 3; i4++)
                        {
                            // previously called "byte byte0"
                            Block byte0 = Blocks.COBBLESTONE;

                            if (rand.nextInt(5) == 0)
                            {
                                byte0 = Blocks.MOSSY_COBBLESTONE;
                            }

                            world.setBlockState(new BlockPos(towerX + i4, towerY + j, towerZ + j2 + 1),
                                    byte0.getDefaultState());
                            byte0 = Blocks.COBBLESTONE;

                            if (rand.nextInt(5) == 0)
                            {
                                byte0 = Blocks.MOSSY_COBBLESTONE;
                            }

                            world.setBlockState(new BlockPos(towerX + j2 + 1, towerY + j, towerZ + i4),
                                    byte0.getDefaultState());
                        }
                    }
                }

                world.setBlockState(new BlockPos(towerX + 2, towerY + towerHeight, towerZ + 2),
                        Blocks.CRAFTING_TABLE.getDefaultState());

                for (int k = 0; k < towerHeight; k++) {
                    world.setBlockState(new BlockPos(towerX, towerY + k, towerZ), Blocks.LADDER.getDefaultState());
                    // TODO : Fix this !
                    /*
                     * Blocks.ladder.onBlockPlaced(world, new BlockPos(towerX, towerY + k, towerZ),
                     * 65); onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
                     */
                    Blocks.LADDER.onBlockPlaced(world, new BlockPos(towerX, towerY + k, towerZ), EnumFacing.EAST, 0, 0, 0, 0, this);

                }

                stage = 2;
            }
            else
            {
                stage = 0;
                experimenttimer = rand.nextInt(100) + 50;
                experimentstart = false;
            }
        }

        if (stage == 2)
        {
            if (posX < (double) towerX)
            {
            	setMotion(0.20000000298023224D, getMotion().y, getMotion().z);
            }

            if (posZ < (double) towerZ)
            {
            	setMotion(getMotion().x, getMotion().y, 0.20000000298023224D);
            }

            if (Math.abs(posX - (double) towerX) < 0.40000000596046448D
                    && Math.abs(posZ - (double) towerZ) < 0.40000000596046448D)
            {
            	setMotion(0, 0.30000001192092896D, 0);
                int l = MathHelper.floor(posX);
                int k2 = MathHelper.floor(getBoundingBox().minY);
                int j4 = MathHelper.floor(posZ);
                world.setBlockState(new BlockPos(l, k2 + 2, j4), Blocks.AIR.getDefaultState());

                if (posY > (double) (towerY + towerHeight))
                {
                	setMotion(getMotion().x, 0, getMotion().z);
                    posZ++;
                    posX++;
                    stage = 3;
                    experimenttimer = rand.nextInt(1000) + 500;
                    experimentstart = false;
                }
            }
        }

        if (stage == 3)
        {
            posY = towerY + towerHeight;
            posX = towerX + 2;
            posZ = towerZ + 2;
            setPosition(towerX + 2, towerY + towerHeight, towerZ + 2);
            setMotion(0, 0, 0);
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);

            if (experimenttimer > 0)
            {
                experimenttimer--;
            }

            if (rand.nextInt(200) == 0)
            {
                int i1 = MathHelper.floor(posX);
                int l2 = MathHelper.floor(getBoundingBox().minY);
                int k4 = MathHelper.floor(posZ);
//                world.addWeatherEffect(new LightningBoltEntity(world, i1, l2 + 3, k4));
            }

            if (rand.nextInt(150) == 0 && !water)
            {
                world.setBlockState(new BlockPos(towerX + 2, towerY + towerHeight, towerZ + 1),
                        Blocks.WATER.getDefaultState());
                world.setBlockState(new BlockPos(towerX + 3, towerY + towerHeight, towerZ + 2),
                        Blocks.WATER.getDefaultState());
                world.setBlockState(new BlockPos(towerX + 1, towerY + towerHeight, towerZ + 2),
                        Blocks.WATER.getDefaultState());
                world.setBlockState(new BlockPos(towerX + 2, towerY + towerHeight, towerZ + 3),
                        Blocks.WATER.getDefaultState());
                water = true;
            }

            if (rand.nextInt(8) == 0)
            {
                EvilLightEntity creepsentityevillight = new EvilLightEntity(world);
                creepsentityevillight.setLocationAndAngles(towerX, towerY + towerHeight, towerZ, rotationYaw, 0.0F);
                creepsentityevillight.setMotion(rand.nextFloat() * 2.0F - 1.0F, getMotion().y, rand.nextFloat() * 2.0F - 1.0F);
                if (!world.isRemote)
                    world.addEntity(creepsentityevillight);
            }

            if (rand.nextInt(10) == 0)
            {
                for (int j1 = 0; j1 < 4; j1++)
                {
                    for (int i3 = 0; i3 < 10; i3++)
                    {
                        double d = rand.nextGaussian() * 0.02D;
                        double d2 = rand.nextGaussian() * 0.02D;
                        double d4 = rand.nextGaussian() * 0.02D;
                        world.addParticle(ParticleTypes.LARGE_SMOKE,
                                ((double) (2.0F + (float) towerX) + (double) (rand.nextFloat() * getWidth() * 2.0F))
                                        - (double) getWidth(),
                                (double) (1.0F + (float) towerY + (float) towerHeight)
                                        + (double) (rand.nextFloat() * getHeight()) + 2D,
                                (2D + ((double) towerZ + (double) (rand.nextFloat() * getWidth() * 2.0F))) - (double) getWidth(),
                                d, d2, d4);
                    }
                }
            }

            if (!experimentstart) {
                for (int k1 = 0; k1 < 4; k1++) {
                    for (int j3 = 0; j3 < 10; j3++) {
                        double d1 = rand.nextGaussian() * 0.02D;
                        double d3 = rand.nextGaussian() * 0.02D;
                        double d5 = rand.nextGaussian() * 0.02D;
                        world.addParticle(ParticleTypes.LARGE_SMOKE,
                                ((double) towerX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(),
                                (double) (towerY + towerHeight) + (double) (rand.nextFloat() * getHeight()) + 2D,
                                ((double) towerZ + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(), d1, d3, d5);
                    }
                }

                EvilLightEntity creepsentityevillight1 = new EvilLightEntity(world);
                creepsentityevillight1.setLocationAndAngles(towerX, towerY + towerHeight + 10, towerZ, rotationYaw,
                        0.0F);
                world.addEntity(creepsentityevillight1);
                experimentstart = true;
            }

            if (experimenttimer == 0) {
                world.createExplosion(null, towerX + 2, towerY + towerHeight + 4, towerZ + 2, 2.0F, true, Mode.NONE);
                experimentstart = false;
                stage = 4;
            }
        }

        if (stage == 4) {
            int l1 = MathHelper.floor(posX);
            int k3 = MathHelper.floor(getBoundingBox().minY);
            int l4 = MathHelper.floor(posZ);

            for (int i5 = 0; i5 < rand.nextInt(5) + 1; i5++)
            {
                world.addWeatherEffect(new LightningBoltEntity(world, (l1 + rand.nextInt(4)) - 2, k3 + 6, (l4 + rand.nextInt(4)) - 2));
            }

            world.playSound(playerentity, this.getPosition(), SoundsHandler.EVIL_LAUGH, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "evilscientistblown.png");
            trulyevil = true;
            this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(90D);

            for (int j5 = 0; j5 < rand.nextInt(4) + 1; j5++)
            {
                int k5 = rand.nextInt(4);

                if (k5 == 0)
                {
                    for (int l5 = 0; l5 < rand.nextInt(8) + 2; l5++)
                    {
                        EvilSnowmanEntity creepsentityevilsnowman = new EvilSnowmanEntity(world);
                        creepsentityevilsnowman.setLocationAndAngles(towerX + rand.nextInt(3), towerY + towerHeight + 1, towerZ + rand.nextInt(3), rotationYaw, 0.0F);
                        creepsentityevilsnowman.setMotion(rand.nextFloat() * 0.3F, rand.nextFloat() * 0.4F, rand.nextFloat() * 0.4F);
                        creepsentityevilsnowman.fallDistance = -35F;
                        if(!world.isRemote)
                        world.addEntity(creepsentityevilsnowman);
                    }
                }

                if (k5 == 1)
                {
                    EvilCreatureEntity creepsentityevilcreature = new EvilCreatureEntity(world);
                    creepsentityevilcreature.setLocationAndAngles(towerX, towerY + towerHeight + 1, towerZ, rotationYaw, 0.0F);
                    creepsentityevilcreature.fallDistance = -35F;
                    if(!world.isRemote)
                    world.addEntity(creepsentityevilcreature);
                }

                if (k5 == 2)
                {
                    EvilPigEntity creepsentityevilpig = new EvilPigEntity(world);
                    creepsentityevilpig.setLocationAndAngles(towerX, towerY + towerHeight + 1, towerZ, rotationYaw, 0.0F);
                    creepsentityevilpig.fallDistance = -35F;
                    if(!world.isRemote)
                    world.addEntity(creepsentityevilpig);
                }

                if (k5 == 3)
                {
                    for (int i6 = 0; i6 < rand.nextInt(4) + 1; i6++)
                    {
                        EvilChickenEntity creepsentityevilchicken = new EvilChickenEntity(world);
                        creepsentityevilchicken.setLocationAndAngles(towerX + rand.nextInt(3), towerY + towerHeight + 1, towerZ + rand.nextInt(3), rotationYaw, 0.0F);
                        creepsentityevilchicken.setMotion(rand.nextFloat() * 0.3F, rand.nextFloat() * 0.4F, rand.nextFloat() * 0.4F);
                        creepsentityevilchicken.fallDistance = -35F;
                        if(!world.isRemote)
                        world.addEntity(creepsentityevilchicken);
                    }
                }

                if (k5 != 4)
                {
                    continue;
                }

                for (int j6 = 0; j6 < rand.nextInt(8) + 2; j6++)
                {
                    EvilSnowmanEntity creepsentityevilsnowman1 = new EvilSnowmanEntity(world);
                    creepsentityevilsnowman1.setLocationAndAngles(towerX + rand.nextInt(3), towerY + towerHeight + 1, towerZ + rand.nextInt(3), rotationYaw, 0.0F);
                    creepsentityevilsnowman1.setMotion(rand.nextFloat() * 0.3F, rand.nextFloat() * 0.4F, rand.nextFloat() * 0.4F);
                    creepsentityevilsnowman1.fallDistance = -35F;
                    if(!world.isRemote)
                    world.addEntity(creepsentityevilsnowman1);
                }
            }

            numexperiments--;

            if (numexperiments <= 0)
            {
                numexperiments = rand.nextInt(4) + 1;
                stage = 5;
            }
            else
            {
                stage = 3;
                experimenttimer = rand.nextInt(500) + 500;
                experimentstart = false;
            }
        }

        if (stage == 5)
        {
            tearDownTower();
            stage = 6;
        }

        if (stage == 6)
        {
            experimenttimer = rand.nextInt(5000) + 100;
            stage = 0;
        }

        super.livingTick();
    }

    public void tearDownTower()
    {
        if (towerBuilt)
        {
            for (int i = 0; i < towerHeight + 1; i++)
            {
                world.setBlockState(new BlockPos(towerX, towerY + i, towerZ), Blocks.AIR.getDefaultState());

                for (int j = 0; j < 3; j++)
                {
                    for (int k = 0; k < 3; k++)
                    {
                        for (int l = 0; l < 4; l++)
                        {
                            for (int i1 = 0; i1 < 10; i1++)
                            {
                                double d = rand.nextGaussian() * 0.02D;
                                double d1 = rand.nextGaussian() * 0.02D;
                                double d2 = rand.nextGaussian() * 0.02D;
                                world.addParticle(ParticleTypes.LARGE_SMOKE, ((double)(2.0F + (float)towerX) + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), (double)(1.0F + (float)towerY + (float)i) + (double)(rand.nextFloat() * getHeight()) + 2D, (2D + ((double)towerZ + (double)(rand.nextFloat() * getWidth() * 2.0F))) - (double)getWidth(), d, d1, d2);
                            }
                        }

                        world.setBlockState(new BlockPos(towerX + k, towerY + i, towerZ + j + 1), Blocks.AIR.getDefaultState());
                        world.setBlockState(new BlockPos(towerX + j + 1, towerY + i, towerZ + k), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        if (trulyevil)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d1 * d1);
            setMotion(
            		(d / (double)f1) * 0.40000000000000002D * 0.20000000192092895D + getMotion().x * 0.18000000098023225D,
            		getMotion().y,
            		(d1 / (double)f1) * 0.40000000000000002D * 0.14000000192092896D + getMotion().z * 0.18000000098023225D
            		);

            if (onGround)
            {
            	setMotion(getMotion().x, 0.44000000196046446D, getMotion().z);
            }

            /*if ((double)f < 2D - (1.0D - (double)modelsize) && entity.getBoundingBox().maxY > getBoundingBox().minY && entity.getBoundingBox().minY < getBoundingBox().maxY)
            {
                attackTime = 10;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackStrength);
            }*/
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();

        if (entity != null)
        {
            double d = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
            setMotion(d * 4D, getMotion().y, d1 * 4D);
        }

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i))
        {
            if (getPassengers() == entity || getRidingEntity() == entity)
            {
                return true;
            }

            if (entity != this && world.getDifficulty() != Difficulty.PEACEFUL)
            {
                setRevengeTarget((LivingEntity) entity);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        if (trulyevil)
        {
            PlayerEntity entityplayer = world.getClosestPlayer(this, 16D);

            if (entityplayer != null && canEntityBeSeen(entityplayer))
            {
                return entityplayer;
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

    @SuppressWarnings("rawtypes")
	class AITargetingSystem extends Brain
    {
    	public EvilScientistEntity evilscientist = EvilScientistEntity.this;
    	public int attackTime;
    	public AITargetingSystem() {}
    	
		@Override
		public boolean shouldExecute()
		{
            LivingEntity entitylivingbase = this.evilscientist.getAttackTarget();
            return trulyevil && entitylivingbase != null && entitylivingbase.isAlive();
		}
        public void updateTask()
        {
        	--attackTime;
            LivingEntity entitylivingbase = this.evilscientist.getAttackTarget();
            double d0 = this.evilscientist.getDistanceSq(entitylivingbase);
            evilscientist.attackEntity(entitylivingbase, (float)d0);
            /*if (d0 < 4.0D)
            {
                if (this.attackTime <= 0)
                {
                    this.attackTime = 20;
                    this.evilcreature.attackEntityAsMob(entitylivingbase);
                }
                
                this.evilcreature.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            }
            else if (d0 < 256.0D)
            {
                // ATTACK ENTITY GOES HERE
                this.evilcreature.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            }
            else*/
            //{
                this.evilscientist.getNavigator().clearPath();
                this.evilscientist.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
            //}
        }
    }
    
    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        //int l = world.getFullBlockLightValue(i, j, k);
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        int j1 = world.countEntities(EvilScientistEntity.class);
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.STONE_SLAB && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(45) == 0 && /*l > 10 &&*/ j1 < 3;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playAmbientSound()
    {
    	World world = Minecraft.getInstance().world;
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (stage < 3)
        {
            return SoundsHandler.EVIL_LAUGH;
        }
        else
        {
            return null; // "evilexperiment" not found.
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.EVIL_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return null; // "evilexperiment" not found.
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource)
    {
    	World world = Minecraft.getInstance().world;
    	
        tearDownTower();

        if(!world.isRemote)
        {
	        if (rand.nextInt(5) == 0)
	        {
	            entityDropItem(Items.COOKED_PORKCHOP, rand.nextInt(3) + 1);
	        }
	        else
	        {
	            entityDropItem(Blocks.SAND, rand.nextInt(3) + 1);
	        }
        }
        super.onDeath(damagesource);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        tearDownTower();
        super.remove();
    }
}
