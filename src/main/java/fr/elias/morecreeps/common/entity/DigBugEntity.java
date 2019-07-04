package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

@SuppressWarnings("deprecation")
public class DigBugEntity extends MobEntity
{
    private static final Item dropItems[];
    protected double attackRange;
    private int angerLevel;
    public int digstage;
    public int digtimer;
    public double holeX;
    public double holeY;
    public double holeZ;
    public double xx;
    public double yy;
    public double zz;
    public int holedepth;
    public int skinframe;
    public int lifespan;
    public int hunger;
    public int waittimer;
    public float modelsize;
    public ResourceLocation texture;
	private int attackTime;

    public DigBugEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "digbug0.png");
        angerLevel = 0;
        attackRange = 16D;
//        setSize(0.5F, 1.2F);
        hunger = rand.nextInt(3) + 1;
        digstage = 0;
        digtimer = rand.nextInt(500) + 500;
        lifespan = 5000;
        holedepth = 0;
        modelsize = 1.0F;
//        this.targetTasks.addTask(0, new DigBugEntity.AIFindPlayerToAttack());
//        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
    }
    
    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    	this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D);
    }

    public float getShadowSize()
    {
        return 0.4F;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
        if (lifespan >= 0 && holedepth > 0)
        {
            lifespan--;

            if (lifespan <= 0)
            {
                digtimer = rand.nextInt(20);
                xx = -1D;
                yy = holedepth;
                zz = -1D;
                digstage = 4;
            }
        }
        super.tick();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn()
    {
        return lifespan < 0;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @SuppressWarnings("rawtypes")
	@Override
    public void livingTick()
    {
    	World world = Minecraft.getInstance().world;
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	
        super.livingTick();

        if (prevPosX != posX || prevPosY != posY)
        {
        	texture = new ResourceLocation("mob/creeps/digbug.png")
            skinframe++;

            if (skinframe > 3)
            {
                skinframe = 0;
            }
        }

        if (digstage == 0 && posY < 90D && digtimer-- < 1)
        {
            int i = MathHelper.floor(posX);
            int i1 = MathHelper.floor(getBoundingBox().minY);
            int i2 = MathHelper.floor(posZ);
            Block l2 = world.getBlockState(new BlockPos(i, i1 - 1, i2)).getBlock();
            holedepth = rand.nextInt(2) + 3;

            if (l2 == Blocks.GRASS_BLOCK)
            {
                if (checkHole(i, i1, i2, holedepth))
                {
                    digstage = 1;
                    holeX = i;
                    holeY = i1;
                    holeZ = i2;
                    xx = 0.0D;
                    yy = 1.0D;
                    zz = 0.0D;
                }
                else
                {
                    digtimer = rand.nextInt(200);
                }
            }
        }

        if (digstage == 1)
        {
            int j = MathHelper.floor(posX);
            int j1 = MathHelper.floor(getBoundingBox().minY);
            int j2 = MathHelper.floor(posZ);
            world.setBlockState(new BlockPos(j, j1, j2), Blocks.AIR.getDefaultState());
            world.setBlockState(new BlockPos(j, j1 + 1, j2), Blocks.AIR.getDefaultState());

            if (posX < holeX + xx)
            {
            	setMotion(getMotion().x + 0.20000000298023224D, getMotion().y, getMotion().z);
            }
            else
            {
            	setMotion(getMotion().x - 0.20000000298023224D, getMotion().y, getMotion().z);
            }

            if (posZ < holeZ + zz)
            {
            	setMotion(getMotion().x, getMotion().y, getMotion().z + 0.20000000298023224D);
            }
            else
            {
            	setMotion(getMotion().x, getMotion().y, getMotion().z - 0.20000000298023224D);
            }
            if(world.isRemote)
            {
            	MoreCreepsReboot.proxy.dirtDigBug(world, this, rand, 1);
            }

            if (digtimer-- < 1)
            {
                digtimer = rand.nextInt(20);
                setPosition((int)(holeX + xx), (int)(holeY - yy), (int)(holeZ + zz));
                Block i3 = world.getBlockState(new BlockPos((int)(holeX + xx), (int)(holeY - yy), (int)(holeZ + zz))).getBlock();

                if (rand.nextInt(50) == 0)
                {
                    i3 = Blocks.COAL_ORE;
                }

                if (i3 != Blocks.SAND && i3 != Blocks.OAK_LOG)
                {
                    for (int j3 = 0; j3 < rand.nextInt(2) + 1; j3++)
                    {
                    	ItemEntity entityitem1 = new ItemEntity(world, (int)(holeX + xx), (int)((holeY - yy) + 1.0D), (int)(holeZ + zz), new ItemStack(i3, 1));
                        world.addEntity(entityitem1);
                    }
                }

                world.setBlockState(new BlockPos((int)(holeX + xx), (int)(holeY - yy), (int)(holeZ + zz)), Blocks.AIR.getDefaultState());

                if (zz++ > 1.0D)
                {
                    zz = 0.0D;
                    setPosition((int)(holeX + xx), (int)(holeY - yy), (int)(holeZ + zz));

                    if (xx++ > 1.0D)
                    {
                        xx = 0.0D;
                        setPosition((int)(holeX + xx), (int)(holeY - yy), (int)(holeZ + zz));

                        if (yy++ > (double)holedepth)
                        {
                            for (int k3 = 0; k3 < rand.nextInt(8) + 5; k3++)
                            {
                                int l3 = rand.nextInt(40) + 40;
                                int i4 = rand.nextInt(40) + 40;

                                if (rand.nextInt(1) == 0)
                                {
                                    l3 *= -1;
                                }

                                if (rand.nextInt(1) == 0)
                                {
                                    i4 *= -1;
                                }

                                BubbleScumEntity creepsentitybubblescum = new BubbleScumEntity(world);
                                creepsentitybubblescum.setLocationAndAngles(posX + (double)l3, posY + (double)holedepth + 2D, posZ + (double)i4, rotationYaw, 0.0F);
                                creepsentitybubblescum.setMotion(rand.nextFloat() * 1.5F, rand.nextFloat() * 2.0F, rand.nextFloat() * 1.5F);
                                creepsentitybubblescum.fallDistance = -25F;
                                if(!world.isRemote)
                                world.addEntity(creepsentitybubblescum);
                            }

                            digstage = 2;
                            double moveSpeed = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
                            moveSpeed = 0.0D;
                            lifespan = 5000;
                            setMotion(getMotion().x, 0.44999998807907104D, getMotion().z); // motionY
                            setPosition((int)(holeX + 1.0D), (int)(holeY - yy), (int)(holeZ + 1.0D));
                            digtimer = rand.nextInt(5) + 5;
                        }
                    }
                }
            }
        }

        if (digstage == 2 && digtimer-- < 1)
        {
            digtimer = rand.nextInt(20);

            for (int k = 0; k < 20 + digtimer; k++)
            {
                MoreCreepsReboot.proxy.bubble(world, this);
            }
            

            digtimer = 50;
            List list = null;
            list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(5D, 1.0D, 5D));

            for (int k1 = 0; k1 < list.size(); k1++)
            {
                Entity entity = (Entity)list.get(k1);

                if ((entity != null) & (entity instanceof BubbleScumEntity))
                {
                    entity.remove();
                    double moveSpeed = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
                    moveSpeed = 0.4F;
                    motionY = 0.60000002384185791D;
                    digstage = 3;
                    digtimer = 50;
                }
            }
        }

        if (digstage == 3)
        {
            int l = rand.nextInt(25) + 15;
            world.playSound(playerentity, this.getPosition(), SoundsHandler.DIG_BUG_EAT, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);

            for (int l1 = 0; l1 < l; l1++)
            {
            	if(world.isRemote)
            	{
                    for (int k2 = 0; k2 < 45; k2++)
                    {
                		MoreCreepsReboot.proxy.dirtDigBug(world, this, rand, k2);
                    }
            	}

            	ItemEntity entityitem = entityDropItem(new ItemStack(Items.COOKIE, 1), 1.0F);
            	entityitem.setMotion(
            			getMotion().x + rand.nextFloat() * 2.0F + 3F, // motionX
            			getMotion().y + (rand.nextFloat() - rand.nextFloat()) * 0.33F, // motionY
            			getMotion().z + (rand.nextFloat() - rand.nextFloat()) * 0.33F); // motionZ
            }

            if (hunger-- < 1)
            {
                digtimer = rand.nextInt(20);
                xx = -1D;
                yy = holedepth;
                zz = -1D;
                digstage = 4;
                world.playSound(playerentity, this.getPosition(), SoundsHandler.DIG_BUG_FULL, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }
            else
            {
                digstage = 2;
                digtimer = 50;
            }
        }

        if (digstage == 4)
        {
            if (posX < holeX + xx)
            {
                motionX += 0.20000000298023224D;
            }
            else
            {
                motionX -= 0.20000000298023224D;
            }

            if (posZ < holeZ + zz)
            {
                motionZ += 0.20000000298023224D;
            }
            else
            {
                motionZ -= 0.20000000298023224D;
            }

            if(world.isRemote)
            {
            	MoreCreepsReboot.proxy.dirtDigBug(world, this, rand, 1);
            }

            if (digtimer-- < 1)
            {
                digtimer = rand.nextInt(10);

                if (world.getBlockState(new BlockPos((int)(holeX + xx), (int)(holeY - yy), (int)(holeZ + zz))).getBlock() == Blocks.AIR)
                {
                    world.setBlockState(new BlockPos((int)(holeX + xx), (int)(holeY - yy), (int)(holeZ + zz)), Blocks.DIRT.getDefaultState());
                }

                if (zz++ > 2D)
                {
                    zz = -1D;
                    setPosition((int)(holeX + xx), (int)(holeY - yy) + 1, (int)(holeZ + zz));

                    if (xx++ > 2D)
                    {
                        xx = -1D;
                        setPosition((int)(holeX + xx), (int)(holeY - yy) + 1, (int)(holeZ + zz));

                        if (yy-- == 1.0D)
                        {
                            digstage = 0;
                            digtimer = rand.nextInt(8000) + 1000;
                            setPosition((int)(holeX + 1.0D), (int)(holeY + yy + 1.0D), (int)(holeZ + 1.0D));
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        if (digstage == 1 || digstage == 4)
        {
            return false;
        }
        else
        {
            return super.isEntityInsideOpaqueBlock();
        }
    }

    public boolean checkHole(int i, int j, int k, int l, World world)
    {
        for (int i1 = 0; i1 < l; i1++)
        {
            for (int j1 = 0; j1 < 3; j1++)
            {
                for (int k1 = 0; k1 < 3; k1++)
                {
                    Block l1 = world.getBlockState(new BlockPos(i + j1, j - i1 - 1, k + k1)).getBlock();

                    if (l1 == Blocks.AIR)
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity entity, float f)
    {
        double d = entity.posX - posX;
        double d1 = entity.posZ - posZ;
        float f1 = MathHelper.sqrt(d * d + d1 * d1);
        setMotion((d / (double)f1) * 0.40000000000000002D * 0.10000000192092896D + getMotion().x * 0.18000000098023225D,
        		getMotion().y,
        		(d1 / (double)f1) * 0.40000000000000002D * 0.070000001920928964D + getMotion().z * 0.18000000098023225D);

        if ((double)f < 2D - (1.0D - (double)modelsize) && entity.getBoundingBox().maxY > getBoundingBox().minY && entity.getBoundingBox().minY < getBoundingBox().maxY)
        {
            attackTime = 10;
            entity.setMotion(-(getMotion().x * 3D), rand.nextFloat() * 2.133F, -(getMotion().z * 3D));
            this.attackEntityAsMob(entity);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putShort("Anger", (short)angerLevel);
        compound.putInt("DigStage", digstage);
        compound.putInt("DigTimer", digtimer);
        compound.putInt("LifeSpan", lifespan);
        compound.putInt("HoleDepth", holedepth);
        compound.putDouble("XX", xx);
        compound.putDouble("YY", yy);
        compound.putDouble("ZZ", zz);
        compound.putDouble("holeX", holeX);
        compound.putDouble("holeY", holeY);
        compound.putDouble("holeZ", holeZ);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        angerLevel = compound.getShort("Anger");
        digstage = compound.getInt("DigStage");
        digtimer = compound.getInt("DigTimer");
        lifespan = compound.getInt("LifeSpan");
        holedepth = compound.getInt("HoleDepth");
        xx = compound.getDouble("XX");
        yy = compound.getDouble("YY");
        zz = compound.getDouble("ZZ");
        holeX = compound.getDouble("holeX");
        holeY = compound.getDouble("holeY");
        holeZ = compound.getDouble("holeZ");
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
        int j1 = world.countEntities(DigBugEntity.class);
        return (i1 == Blocks.GRASS || i1 == Blocks.DIRT) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(25) == 0 && /*l > 10 &&*/ j1 < 10;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    public boolean attackEntityFrom(Entity entity, float i)
    {
        if (entity instanceof PlayerEntity)
        {
            List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(32D, 32D, 32D));

            for (int j = 0; j < list.size(); j++)
            {
                Entity entity1 = (Entity)list.get(j);

                if (entity1 instanceof DigBugEntity)
                {
                    DigBugEntity creepsentitydigbug = (DigBugEntity)entity1;
                    creepsentitydigbug.becomeAngryAt(entity);
                }
            }

            becomeAngryAt(entity);
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    class AIFindPlayerToAttack extends EntityAINearestAttackableTarget
    {
        public AIFindPlayerToAttack()
        {
            super(DigBugEntity.this, PlayerEntity.class, true);
        }
        
        public void updateTask()
        {
        	LivingEntity target = DigBugEntity.this.getAttackTarget();
        	float f = getDistance(target);
        	attackEntity(target, f);
        }
        
        public boolean shouldExecute()
        {
            if (angerLevel > 0)
            {
                angerLevel--;
            }

            if (angerLevel == 0)
            {
                return false;
            }
            else
            {
                return super.shouldExecute();
            }
        }
    }
    
    private void becomeAngryAt(Entity entity)
    {
        setRevengeTarget((LivingEntity) entity);
        angerLevel = 400 + rand.nextInt(400);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (digstage == 0)
        {
            return null; // Sound "digbug" not found
        }

        if (digstage == 1 || digstage == 4)
        {
            return SoundsHandler.DIG_BUG_DIG;
        }

        if (digstage == 2)
        {
            return SoundsHandler.DIG_BUG_CALL;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.DIG_BUG_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.DIG_BUG_DEATH;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected Item getDropItemId()
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
