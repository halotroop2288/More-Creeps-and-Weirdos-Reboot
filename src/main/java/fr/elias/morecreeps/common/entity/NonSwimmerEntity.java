package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
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
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class NonSwimmerEntity extends AnimalEntity
{
    protected double attackRange;
    public boolean swimming;
    public float modelsize;
    public boolean saved;
    public int timeonland;
    public boolean towel;
    public boolean wet;
    public ResourceLocation texture;

    public NonSwimmerEntity(World world)
    {
        super(null, world); // EntityType??
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES+ "nonswimmer.png");
        attackRange = 16D;
        timeonland = 0;
        wet = false;
        swimming = false;
        saved = false;
        towel = false;
        modelsize = 1.0F;
        // ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        // this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
        // this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(7, new EntityAIWatchClosest(this, PlayerEntity.class, 6.0F));
        // this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to
     * generate the new baby animal.
     */
    public AnimalEntity createChild(AgeableEntity animalentity, World world) {
        return new NonSwimmerEntity(world);
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void livingTick() {
        if (inWater) {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.05D);
            swimming = true;
            wet = true;
        } else {
            this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
            int i = MathHelper.floor(posX);
            int k = MathHelper.floor(getBoundingBox().minY);
            int i1 = MathHelper.floor(posZ);
            BlockPos bp = new BlockPos(i, k, i1);
            Block k1 = world.getBlockState(bp).getBlock();

            if (k1 != Blocks.WATER) {
                swimming = false;
                PlayerEntity entityplayersp = world.getClosestPlayer(this, 3F);

                if (entityplayersp != null) {
                    float f = entityplayersp.getDistance(this);

                    if (f < 4F && !saved && timeonland++ > 155 && wet) {
                        giveReward((ServerPlayerEntity) entityplayersp);
                    }
                }
            }
        }

        if (saved && rand.nextInt(100) == 0 && !towel && onGround) {
            int j = MathHelper.floor(posX);
            int l = MathHelper.floor(getBoundingBox().minY);
            int j1 = MathHelper.floor(posZ);
            BlockPos bp = new BlockPos(j, l, j1);
            Block l1 = world.getBlockState(bp).getBlock();

            if (l1 != Blocks.WATER) {
                towel = true;
                TowelEntity towel = new TowelEntity(world);
                towel.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                int i2 = rand.nextInt(6);
                towel.texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "towel.png");
                // creepsentitytowel.basetexture = (new
                // StringBuilder()).append("/mob/creeps/towel").append(String.valueOf(i2)).append(".png").toString();
                world.addEntity(towel);
                towel.addPassenger(this);
            }
        }

        super.livingTick();
    }

    public void giveReward(ServerPlayerEntity entityplayersp)
    {
        PlayerEntity playerentity = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;
        // if
        // (!entityplayersp.getStatFile().hasAchievementUnlocked(MoreCreepsReboot.achievenonswimmer))
        // {
        // confetti(entityplayersp);
        // world.playSound(entityplayersp, "morecreeps:achievement", 1.0F, 1.0F);
        // entityplayersp.addStat(MoreCreepsReboot.achievenonswimmer, 1);
        // }

        if (rand.nextInt(5) == 0) {
            world.playSound(playerentity, this.getPosition(), SoundsHandler.NON_SWIMMER_SORRY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            return;
        }

        world.playSound(playerentity, this.getPosition(), SoundsHandler.NON_SWIMMER_REWARD, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        saved = true;
        int i = rand.nextInt(5) + 1;
        faceEntity(entityplayersp, 0.0F, 0.0F);

        if (!world.isRemote) {
            if (entityplayersp != null) {
                ItemEntity entityitem = null;

                switch (i) {
                case 1:
                    entityitem = entityDropItem(new ItemStack(ItemList.lolly, rand.nextInt(2) + 1), 1.0F);
                    break;

                case 2:
                    entityitem = entityDropItem(new ItemStack(ItemList.lolly, 1), 1.0F);
                    break;

                case 3:
                    entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(10) + 1), 1.0F);
                    break;

                case 4:
                    entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(30) + 1), 1.0F);
                    break;

                case 5:
                    entityitem = entityDropItem(new ItemStack(Items.GOLD_INGOT, 1), 1.0F);
                    break;

                default:
                    entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(3) + 1), 1.0F);
                    break;
                }

                double d = -MathHelper.sin((entityplayersp.rotationYaw * (float) Math.PI) / 180F);
                double d1 = MathHelper.cos((entityplayersp.rotationYaw * (float) Math.PI) / 180F);
                entityitem.posX = entityplayersp.posX + d * 0.5D;
                entityitem.posY = entityplayersp.posY + 0.5D;
                entityitem.posZ = entityplayersp.posZ + d1 * 0.5D;
            }
        }
    }

    /*
     * public int[] findTree(Entity entity, Double double1) { AxisAlignedBB
     * axisalignedbb = entity.boundingBox.expand(double1.doubleValue(),
     * double1.doubleValue(), double1.doubleValue()); int i =
     * MathHelper.floor_double(axisalignedbb.minX); int j =
     * MathHelper.floor_double(axisalignedbb.maxX + 1.0D); int k =
     * MathHelper.floor_double(axisalignedbb.minY); int l =
     * MathHelper.floor_double(axisalignedbb.maxY + 1.0D); int i1 =
     * MathHelper.floor_double(axisalignedbb.minZ); int j1 =
     * MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);
     * 
     * for (int k1 = i; k1 < j; k1++) { for (int l1 = k; l1 < l; l1++) { for (int i2
     * = i1; i2 < j1; i2++) { int j2 = world.getBlockId(k1, l1, i2);
     * 
     * if (j2 != 0 && (j2 == Block.waterStill || j2 == Block.waterMoving)) { return
     * (new int[] { k1, l1, i2 }); } } } }
     * 
     * return (new int[] { -1, 0, 0 }); }
     */

    /**
     * Takes a coordinate in and returns a weight to determine how likely this
     * creature will try to path to the block. Args: x, y, z
     */
    @Override
    public float getBlockPathWeight(BlockPos bp) {
        if (world.getBlockState(bp.down()).getBlock() == Blocks.WATER) {
            return 10F;
        } else {
            return -(float) bp.getY();
        }
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset() {
        if (getRidingEntity() instanceof TowelEntity) {
            return -1.85D;
        } else {
            return 0.0D;
        }
    }

    public void updateRiderPosition() {
        getRidingEntity().setPosition(posX, posY + getMountedYOffset() + getRidingEntity().getYOffset(), posZ);
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this
     * one.
     */
    public double getMountedYOffset() {
        return 0.5D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i)
    {
        Entity entity = damagesource.getTrueSource();

        if (entity instanceof PlayerEntity) {
            motionY = 0.25D;

            if (i == 1) {
                i = 0;
            }
        }

        this.dismountEntity(null);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.55D);
        super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("modelsize", modelsize);
        compound.putBoolean("saved", saved);
        compound.putBoolean("towel", towel);
        compound.putBoolean("wet", wet);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        modelsize = compound.getFloat("modelsize");
        saved = compound.getBoolean("saved");
        towel = compound.getBoolean("towel");
        wet = compound.getBoolean("wet");
    }

    /**
     * knocks back this entity
     */
    @Override
    public void knockBack(Entity entity, float i, double d, double d1) {
        if (entity instanceof PlayerEntity) {
            double d2 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
            double d3 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
            setMotion(d2 * 0.5D, getMotion().y, d3 * 0.5D);
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this
     * entity.
     */
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        // int l = world.getFullBlockLightValue(i, j, k);
        BlockPos bp = new BlockPos(i, j, k);
        Block i1 = world.getBlockState(bp.down()).getBlock();
        int j1 = world.countEntities(NonSwimmerEntity.class);
        return (i1 == Blocks.WATER) && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG
                && i1 != Blocks.SMOOTH_STONE_SLAB && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
                // && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.canBlockSeeSky(bp) && rand.nextInt(25) == 0 && /* l > 9 && */j1 < 4;
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
        PlayerEntity playerentity = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;

        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (swimming)
        {
            return SoundsHandler.NON_SWIMMER;
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
        return SoundsHandler.NON_SWIMMER_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.NON_SWIMMER_DEATH;
    }

    public void confetti(PlayerEntity player)
    {
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
        world.addEntity(creepsentitytrophy);
    }

    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }
}
