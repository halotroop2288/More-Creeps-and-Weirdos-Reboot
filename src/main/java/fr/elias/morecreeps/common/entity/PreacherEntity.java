package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
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
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class PreacherEntity extends MobEntity
{
    public boolean rideable;
    protected double attackRange;
    private int angerLevel;
    private boolean ritual;
    private Entity targetedEntity;
    private Entity victimEntity;
    public int raise;
    public boolean getvictim;
    
    private int waittime;
    private int raiselevel;
    public int revenge;
    
    public ResourceLocation texture;

    public PreacherEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "preacher0.png");
        angerLevel = 0;
        attackRange = 16D;
        ritual = false;
        getvictim = false;
        raise = 0;
        victimspeed = 0.0F;
        waittime = rand.nextInt(500) + 500;
        raiselevel = 0;
        revenge = 0;
        // ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(7, new EntityAIWatchClosest(this, PlayerEntity.class, 6.0F));
        // this.tasks.addTask(8, new EntityAILookIdle(this));
        // this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntitySheep.class, true));
        // this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPig.class, true));
    }
    
    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(75D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    	this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick()
    {
        World world = Minecraft.getInstance().world;
        PlayerEntity playerentity = Minecraft.getInstance().player;

    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getAttackTarget() != null ? 0.6D : 0.4D);

        if (rand.nextInt(4) == 0)
        {
            texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "preacher.png");
        }

        super.tick();

        if (isInLava()) // used to be "handleLavaMovement" but that was invalid
        {
            if (rand.nextInt(25) == 0)
            {
                world.playSound(playerentity, this.getPosition(), SoundsHandler.PREACHER_BURN, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            setOnFireFromLava();
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void livingTick()
    {
        World world = Minecraft.getInstance().world;
        PlayerEntity playerentity = Minecraft.getInstance().player;

        if (inWater || isEntityInsideOpaqueBlock())
        {
            int i = MathHelper.floor(posX);
            int l = MathHelper.floor(getBoundingBox().minY);
            int j1 = MathHelper.floor(posZ);
            Block l1 = world.getBlockState(new BlockPos(i, l + 2, j1)).getBlock();

            if (l1 != Blocks.AIR) {
                world.setBlockState(new BlockPos(i, l + 2, j1), Blocks.AIR.getDefaultState());
                setMotion(getMotion().x, getMotion().y += 0.5D, getMotion().z);
            }
        }

        if (getvictim)
        {
            setMotion(0, 0, 0);

            if (raise++ > raiselevel) {
                getvictim = false;
                ritual = false;
                victimEntity.setMotion(victimEntity.getMotion().x, -0.80000001192092896D, victimEntity.getMotion().z);
                raise = 0;
                waittime = rand.nextInt(500) + 500;
            } else {
                int j = MathHelper.floor(victimEntity.posX);
                int i1 = MathHelper.floor(victimEntity.getBoundingBox().minY);
                int k1 = MathHelper.floor(victimEntity.posZ);
                Block i2 = world.getBlockState(new BlockPos(j, i1 + 2, k1)).getBlock();

                if (i2 != Blocks.AIR && (victimEntity instanceof PlayerEntity))
                {
                    world.setBlockState(new BlockPos(j, i1 + 2, k1), Blocks.AIR.getDefaultState());
                }

                victimEntity.setMotion(victimEntity.getMotion().x, 0.20000000298023224D, victimEntity.getMotion().z);
                waittime = 1000;
                smokevictim(victimEntity);
                smoke();

                if (rand.nextInt(10) == 0) {
                    victimEntity.setMotion(rand.nextFloat() * 0.85F - 0.5F, victimEntity.getMotion().y, victimEntity.getMotion().z);
                } else if (rand.nextInt(10) == 0) {
                    victimEntity.setMotion(victimEntity.getMotion().x, victimEntity.getMotion().y, rand.nextFloat() * 0.8F - 0.5F);
                }
            }
        }

        if (ritual && !getvictim) {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
            targetedEntity = null;

            for (int k = 0; k < world.loadedEntityList.size(); k++) {
                targetedEntity = (Entity) world.loadedEntityList.get(k);

                if ((targetedEntity instanceof SheepEntity) || (targetedEntity instanceof PigEntity)) {
                    getvictim = true;
                    victimEntity = targetedEntity;
                    victimEntity.setMotion(0, 0, 0);
                    raiselevel = rand.nextInt(50) + 50;
                    world.playSound(playerentity, this.getPosition(), SoundsHandler.PREACHER_RAISE, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F);
                }
            }

            if (targetedEntity == null) {
                ritual = false;
                getvictim = false;
                this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
            }
        } else if (rand.nextInt(2) == 0 && waittime-- < 1) {
            ritual = true;
            waittime = 1000;
            getvictim = false;
        }

        super.livingTick();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putShort("Anger", (short) angerLevel);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        angerLevel = compound.getShort("Anger");
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
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.SAND && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.SMOOTH_STONE_SLAB
                && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//                && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(25) == 0;// && l > 10;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        PlayerEntity playerentity = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;

        Entity obj = damagesource.getTrueSource();

        if (!handleWaterMovement())
        {
            world.playSound(playerentity, this.getPosition(), SoundsHandler.PREACHER_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F);
        }

        if (getvictim && obj != null && !(obj instanceof RocketEntity))
        {
            obj.setMotion(getMotion().x + rand.nextFloat() * 1.98F, getMotion().y + rand.nextFloat() * 1.98F, getMotion().z + rand.nextFloat() * 1.98F);
            return true;
        }

        if (obj != null && (obj instanceof RocketEntity))
        {
            obj = world.getClosestPlayer(this, 30D);

            if ((obj != null) & (obj instanceof PlayerEntity))
            {
                this.addPassenger(obj);// Acutally mounted "obj" to "null" before, but "obj" has no dismount function.
                getvictim = true;
                victimEntity = ((Entity)(obj));
                victimEntity.setMotion(0, 0, 0);
                raiselevel = rand.nextInt(50) + 50;
                world.playSound(playerentity, this.getPosition(), SoundsHandler.PREACHER_RAISE, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F);
            }
        }

        if (obj != null)
        {
            if (i < 1)
            {
                i = 1;
            }

            if ((obj instanceof GooDonutEntity) || (obj instanceof RocketEntity) || (obj instanceof BulletEntity) || (obj instanceof RayEntity))
            {
                i = 2;
            }

            //wtf is that ?
            //health = (health - rand.nextInt(i)) + 1;
            raise = 1;
            waittime = 0;
            smoke();
            getvictim = true;
            victimEntity = ((Entity)(obj));
            victimEntity.setMotion(0, 0, 0);
            raiselevel = rand.nextInt(50) + 50;
            world.playSound(playerentity, this.getPosition(), SoundsHandler.PREACHER_RAISE, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F);
        }

        return true;
    }

    /**
     * knocks back this entity
     */
    @Override
    public void knockBack(Entity entity, float i, double d, double d1)
    {
        setMotion(1.5D, 1.5D, 0.5D);
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        return null;
    }

    private void smoke()
    {
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION,
                        ((posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth()) + (double) i,
                        posY + (double) (rand.nextFloat() * getHeight()),
                        (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth() - (double) i,
                        posY + (double) (rand.nextFloat() * getHeight()),
                        (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(),
                        posY + (double) (rand.nextFloat() * getHeight()),
                        (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F) + (double) i) - (double) getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth(),
                        posY + (double) (rand.nextFloat() * getHeight()),
                        (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) i - (double) getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        ((posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth()) + (double) i,
                        posY + (double) (rand.nextFloat() * getHeight()),
                        (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F) + (double) i) - (double) getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth() - (double) i,
                        posY + (double) (rand.nextFloat() * getHeight()),
                        (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) i - (double) getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        ((posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth()) + (double) i,
                        posY + (double) (rand.nextFloat() * getHeight()),
                        (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F) + (double) i) - (double) getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) getWidth() - (double) i,
                        posY + (double) (rand.nextFloat() * getHeight()),
                        (posZ + (double) (rand.nextFloat() * getWidth() * 2.0F)) - (double) i - (double) getWidth(), d, d1, d2);
            }
        }
    }

    private void smokevictim(Entity entity)
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION,
                        (entity.posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(),
                        (entity.posY + (double)(rand.nextFloat() * getHeight()) + (double)i) - 2D,
                        (entity.posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), d, d1, d2);
            }
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.PREACHER;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.PREACHER_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.PREACHER_DEATH;
    }

    public void confetti(PlayerEntity player)
    {
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
        if(!world.isRemote)
        world.addEntity(creepsentitytrophy);
    }

    /**
     * Will get destroyed next tick.
     */
    @Override
    public void onDeath(DamageSource damagesource)
    {
    	super.onDeath(damagesource);
    	// ServerPlayerEntity player = (ServerPlayerEntity) damagesource.getTrueSource();
    	// if(player != null)
    	// {
        // 	if (!player.getStats().hasAchievementUnlocked(ModAdvancementList.gotohell))
        // 	{
        		
        // 		world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
        // 		player.addStat(MoreCreepsReboot.achievegotohell, 1);
        // 		confetti(player);
        // 	}
    	// }

    	if(!world.isRemote)
    	{
        	if (rand.nextInt(50) == 0)
        	{
        		entityDropItem(Items.DIAMOND, rand.nextInt(2) + 1);
            }

            if (rand.nextInt(50) == 0)
            {
                entityDropItem(new ItemStack(Items.LAPIS_LAZULI, 1), 1.0F);
            }

            if (rand.nextInt(50) == 0)
            {
                entityDropItem(new ItemStack(Items.COCOA_BEANS, 1), 1.0F);
            }

            if (rand.nextInt(50) == 0)
            {
                entityDropItem(new ItemStack(Items.RED_DYE, 1), 1.0F);
            }

            if (rand.nextInt(2) == 0)
            {
                entityDropItem(Items.GOLD_INGOT, rand.nextInt(5) + 2);
            }

            else
            {
                entityDropItem(Items.BOOK, 1);
                entityDropItem(Items.APPLE, 1);
        	}
    	}
    }
}
