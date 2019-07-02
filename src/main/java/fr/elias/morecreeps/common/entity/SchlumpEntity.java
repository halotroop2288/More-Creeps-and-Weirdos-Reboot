package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
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

public class SchlumpEntity extends AnimalEntity
{
    Block doorGetter[] = { Blocks.ACACIA_DOOR, Blocks.BIRCH_DOOR, Blocks.DARK_OAK_DOOR, Blocks.JUNGLE_DOOR,
            Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR };
    Block bedGetter[] = { Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED,
            Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED,
            Blocks.ORANGE_BED, Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.RED_BED, Blocks.WHITE_BED,
            Blocks.YELLOW_BED };
    Block planksGetter[] = { Blocks.ACACIA_PLANKS, Blocks.BIRCH_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_PLANKS,
            Blocks.JUNGLE_PLANKS, Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS };
    Block woolGetter[] = { Blocks.BLACK_WOOL, Blocks.BLUE_WOOL, Blocks.BROWN_WOOL, Blocks.CYAN_WOOL, Blocks.GRAY_WOOL,
            Blocks.GREEN_WOOL, Blocks.LIGHT_BLUE_WOOL, Blocks.LIGHT_GRAY_WOOL, Blocks.LIME_WOOL, Blocks.MAGENTA_WOOL,
            Blocks.ORANGE_WOOL, Blocks.PINK_WOOL, Blocks.PURPLE_WOOL, Blocks.RED_WOOL, Blocks.WHITE_WOOL,
            Blocks.YELLOW_WOOL };
    World world;
    PlayerEntity playerentity;
    protected double attackRange;
    public float modelsize;
    public boolean saved;
    public int age;
    public int agetimer;
    public int payouttimer;
    public boolean placed;
    public int deathtimer;
    public ResourceLocation texture;
    public double moveSpeed;
    public double health;
    public float width = getWidth();
    public float length = getWidth();
    public float height = getHeight();

    public SchlumpEntity(World world) {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + "/mob/creeps/schlump.png");
        moveSpeed = 0.0F;
        health = rand.nextInt(10) + 10;
        saved = false;
        modelsize = 0.4F;
        // setSize(width * modelsize, height * modelsize);
        age = 0;
        agetimer = 0;
        placed = false;
        deathtimer = -1;
    }

    public void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(moveSpeed);
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to
     * generate the new baby animal.
     */
    public AnimalEntity spawnBabyAnimal(AnimalEntity entityanimal) {
        return new SchlumpEntity(world);
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void livingTick() {
        if (inWater) {
            setDead();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        World world = Minecraft.getInstance().world;

        ignoreFrustumCheck = true;

        if (agetimer++ > 50) {
            if (age < 22000) {
                age++;
            }

            if (age > 20000) {
                setDead();
            }

            if (age > 6000) {
                confetti();
                world.playSound(playerentity, this.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.NEUTRAL,
                        1.0F, 1.0F);
                // Give player schlump-raising achievement here
            }

            if (modelsize < 3.5F) {
                modelsize += 0.001F;
            }

            agetimer = 0;
            int i = (age / 100) * 2;

            if (i > 150) {
                i = 150;
            }

            if (age > 200 && rand.nextInt(200 - i) == 0) {
                giveReward();
            }
        }

        if (!placed) {
            placed = true;

            if (!checkHouse()) {
                deathtimer = 200;
            }
        } else if (deathtimer-- == 0) {
            setDead();
        }

        super.tick();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn() {
        return health < 1;
    }

    public boolean checkHouse() {
        PlayerEntity player = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;

        boolean flag = false;
        @SuppressWarnings("rawtypes")
        List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(16D, 16D, 16D));
        int i = 0;

        do {
            if (i >= list.size()) {
                break;
            }

            Entity entity = (Entity) list.get(i);

            if (entity instanceof SchlumpEntity) {
                flag = true;
                break;
            }

            i++;
        } while (true);

        if (flag) {
            MoreCreepsReboot.proxy.addChatMessage("Too close to another Schlump. SCHLUMP OVERLOAD!");
            world.playSound(player, this.getPosition(), SoundsHandler.SCHLUMP_OVERLOAD, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            return false;
        }

        i = MathHelper.floor(posX);
        int j = MathHelper.floor(this.getBoundingBox().minY);
        int k = MathHelper.floor(posZ);

        if (world.canBlockSeeSky(new BlockPos(i, j, k))) {
            MoreCreepsReboot.proxy.addChatMessage("Your Schlump needs to be indoors or it will die!");
            world.playSound(player, this.getPosition(), SoundsHandler.SCHLUMP_INDOORS, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            return false;
        }

        if (world.getLight(new BlockPos(i, j, k)) > 11) {
            MoreCreepsReboot.proxy.addChatMessage("It is too bright in here for your little Schlump!");
            world.playSound(player, this.getPosition(), SoundsHandler.SCHLUMP_BRIGHT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            return false;
        }

        int l = 0;

        for (int i1 = -2; i1 < 2; i1++) {
            for (int k1 = -2; k1 < 2; k1++) {
                for (int i2 = 0; i2 < 5; i2++) {
                    if (world.getBlockState(new BlockPos((int) posX + i1, (int) posY + i2, (int) posZ + k1))
                            .getBlock() == Blocks.AIR) {
                        l++;
                    }
                }
            }
        }

        if (l < 60) {
            MoreCreepsReboot.proxy.addChatMessage("Your Schlump doesn't have enough room to grow!");
            world.playSound(playerentity, this.getPosition(), SoundsHandler.SCHLUMP_ROOM, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            return false;
        }

        int j1 = 0;

        for (int l1 = -5; l1 < 5; l1++) {
            for (int j2 = -5; j2 < 5; j2++) {
                for (int k2 = -5; k2 < 5; k2++) {
                    Block l2 = world.getBlockState(new BlockPos((int) posX + l1, (int) posY + k2, (int) posZ + j2))
                            .getBlock();

                    if (l2 == this.doorGetter[rand.nextInt(doorGetter.length)]) {
                        j1 += 10;
                    }

                    if (l2 == Blocks.IRON_DOOR) {
                        j1 += 20;
                    }

                    if (l2 == Blocks.GLASS) {
                        j1 += 5;
                    }

                    if (l2 == Blocks.CHEST) {
                        j1 += 15;
                    }

                    if (l2 == this.bedGetter[rand.nextInt(bedGetter.length)]) {
                        j1 += 20;
                    }

                    if (l2 == Blocks.BOOKSHELF) {
                        j1 += 15;
                    }

                    if (l2 == Blocks.BRICKS) {
                        j1 += 3;
                    }

                    if (l2 == this.planksGetter[rand.nextInt(planksGetter.length)]) {
                        j1 += 3;
                    }

                    if (l2 == this.woolGetter[rand.nextInt(woolGetter.length)]) {
                        j1 += 2;
                    }

                    if (l2 == Blocks.CAKE) {
                        j1 += 10;
                    }

                    if (l2 == Blocks.FURNACE) {
                        j1 += 15;
                    }

                    if (l2 == Blocks.POPPY) {
                        j1 += 5;
                    }

                    if (l2 == Blocks.DANDELION) {
                        j1 += 5;
                    }

                    if (l2 == Blocks.CRAFTING_TABLE) {
                        j1 += 10;
                    }
                }
            }
        }

        if (j1 > 275) {
            if (age < 10) {
                MoreCreepsReboot.proxy.addChatMessage("This location is great! Your Schlump will love it here! ");
                world.playSound(player, this.getPosition(), SoundsHandler.SCHLUMP_OK, SoundCategory.NEUTRAL, 1.0F,
                        (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            return true;
        } else {
            MoreCreepsReboot.proxy.addChatMessage("This is not a good location for your Schlump. It will die here! ");
            world.playSound(player, this.getPosition(), SoundsHandler.SCHLUMP_SUCKS, SoundCategory.NEUTRAL, 1.0F,
                    (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            return false;
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets
     * into the saddle on a pig.
     */
    @Override
    public boolean processInteract(PlayerEntity playerentity, Hand hand)
    {
        ItemStack itemstack = playerentity.inventory.getCurrentItem();

        if (itemstack != null && itemstack.getItem() == ItemList.baby_jar_empty) {
            if (modelsize > 0.5F) {
                MoreCreepsReboot.proxy.addChatMessage("That Schlump is too big to fit in a jar! ");
                world.playSound(playerentity, this.getPosition(), SoundsHandler.SCHLUMP_BIG, SoundCategory.NEUTRAL, 1.0F,
                        (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                return true;
            }

            setDead();
            playerentity.inventory.setInventorySlotContents(playerentity.inventory.currentItem,
                    new ItemStack(ItemList.baby_jar_full));
        }

        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;

        if (i < 1) {
            i = 1;
        }

        hurtTime = maxHurtTime = 10;
        smoke();

        if (health <= 0) {
            world.playSound(player, this.getPosition(), getDeathSound(), SoundCategory.NEUTRAL, getSoundVolume(),
                    (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            onDeath(damagesource);
        } else {
            world.playSound(player, this.getPosition(), getHurtSound(damagesource), SoundCategory.NEUTRAL,
                    getSoundVolume(),
                    (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }

        super.attackEntityFrom(damagesource, i);
        return true;
    }

    @SuppressWarnings("rawtypes")
    public boolean checkItems() {
        int i = 0;
        List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(6D, 6D, 6D));

        for (int j = 0; j < list.size(); j++) {
            Entity entity = (Entity) list.get(j);

            if (entity instanceof ItemEntity) {
                i++;
            }
        }

        return i > 25;
    }

    public void giveReward() {
        PlayerEntity player = Minecraft.getInstance().player;

        if (!checkHouse()) {
            MoreCreepsReboot.proxy.addChatMessage("This is not a good location for your Schlump. It will die here!");
            world.playSound(player, this.getPosition(), SoundsHandler.SCHLUMP_SUCKS, SoundCategory.NEUTRAL, 1.0F,
                    (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            deathtimer = 200;
            return;
        }

        if (checkItems()) {
            return;
        }

        world.playSound(player, this.getPosition(), SoundsHandler.SCHLUMP_REWARD, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        smallconfetti();
        int i = rand.nextInt(age / 100) + 1;

        if (i > 42) {
            i = 42;
        }

        if (playerentity != null) {
            ItemEntity entityitem = null;

            switch (i) {
            case 1:
                entityitem = entityDropItem(new ItemStack(ItemList.lolly, rand.nextInt(2) + 1), 1.0F);
                break;

            case 2:
                entityitem = entityDropItem(new ItemStack(Items.WHEAT, 1), 1.0F);
                break;

            case 3:
                entityitem = entityDropItem(new ItemStack(Items.WHEAT, 1), 1.0F);
                break;

            case 4:
                entityitem = entityDropItem(new ItemStack(Items.WHEAT, 1), 1.0F);
                break;

            case 5:
                entityitem = entityDropItem(new ItemStack(Items.WHEAT, 1), 1.0F);
                break;

            case 6:
                entityitem = entityDropItem(new ItemStack(ItemList.band_aid, 1), 1.0F);
                break;

            case 7:
                entityitem = entityDropItem(new ItemStack(ItemList.band_aid, 1), 1.0F);
                break;

            case 8:
                entityitem = entityDropItem(new ItemStack(ItemList.band_aid, 1), 1.0F);
                break;

            case 9:
                entityitem = entityDropItem(new ItemStack(ItemList.band_aid, 1), 1.0F);
                break;

            case 10:
                entityitem = entityDropItem(new ItemStack(ItemList.band_aid, 1), 1.0F);
                break;

            case 11:
                entityitem = entityDropItem(new ItemStack(Items.BREAD, 1), 1.0F);
                break;

            case 12:
                entityitem = entityDropItem(new ItemStack(Items.BREAD, 1), 1.0F);
                break;

            case 13:
                entityitem = entityDropItem(new ItemStack(Items.BREAD, 1), 1.0F);
                break;

            case 14:
                entityitem = entityDropItem(new ItemStack(Items.BREAD, 1), 1.0F);
                break;

            case 15:
                entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(4) + 1), 1.0F);
                break;

            case 16:
                entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(4) + 1), 1.0F);
                break;

            case 17:
                entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(4) + 1), 1.0F);
                break;

            case 18:
                entityitem = entityDropItem(new ItemStack(ItemList.lolly, rand.nextInt(2) + 1), 1.0F);
                break;

            case 19:
                entityitem = entityDropItem(new ItemStack(Items.APPLE, 1), 1.0F);
                break;

            case 20:
                entityitem = entityDropItem(new ItemStack(Items.GOLDEN_APPLE, 1), 1.0F);
                break;

            case 21:
                entityitem = entityDropItem(new ItemStack(Items.PORKCHOP, 1), 1.0F);
                break;

            case 22:
                entityitem = entityDropItem(new ItemStack(Items.COAL, 1), 1.0F);
                break;

            case 23:
                entityitem = entityDropItem(new ItemStack(Items.CHARCOAL, 1), 1.0F);
                break;

            case 24:
                entityitem = entityDropItem(new ItemStack(Items.MELON_SEEDS, 1), 1.0F);
                break;

            case 25:
                entityitem = entityDropItem(new ItemStack(Items.PORKCHOP, 1), 1.0F);
                break;

            case 26:
                entityitem = entityDropItem(new ItemStack(Items.PORKCHOP, 1), 1.0F);
                break;

            case 27:
                entityitem = entityDropItem(new ItemStack(Items.IRON_INGOT, 1), 1.0F);
                break;

            case 28:
                entityitem = entityDropItem(new ItemStack(Items.COD, 1), 1.0F);
                break;

            case 29:
                entityitem = entityDropItem(new ItemStack(ItemList.evil_egg, rand.nextInt(5) + 1), 1.0F);
                break;

            case 30:
                entityitem = entityDropItem(new ItemStack(Items.COOKED_COD, 0), 1.0F);
                break;

            case 31:
                entityitem = entityDropItem(new ItemStack(ItemList.gun, 1), 1.0F);
                break;

            case 32:
                entityitem = entityDropItem(new ItemStack(ItemList.extinguisher, rand.nextInt(2) + 1), 1.0F);
                break;

            case 33:
                entityitem = entityDropItem(new ItemStack(ItemList.rocket, 1), 1.0F);
                break;

            case 34:
                entityitem = entityDropItem(new ItemStack(ItemList.atom_packet, rand.nextInt(7) + 1), 1.0F);
                break;

            case 35:
                entityitem = entityDropItem(new ItemStack(ItemList.army_gem, 1), 1.0F);
                break;

            case 36:
                entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(24) + 1), 1.0F);
                break;

            case 37:
                entityitem = entityDropItem(new ItemStack(ItemList.army_gem, 1), 1.0F);
                break;

            case 38:
                entityitem = entityDropItem(new ItemStack(ItemList.horse_head_gem, 1), 1.0F);
                break;

            case 39:
                entityitem = entityDropItem(new ItemStack(Items.GOLD_INGOT, 1), 1.0F);
                break;

            case 40:
                entityitem = entityDropItem(new ItemStack(Items.DIAMOND, 1), 1.0F);
                break;

            case 41:
                entityitem = entityDropItem(new ItemStack(ItemList.ray_gun, 1), 1.0F);
                break;

            case 42:
                entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(49) + 1), 1.0F);
                break;

            default:
                entityitem = entityDropItem(new ItemStack(ItemList.money, rand.nextInt(3) + 1), 1.0F);
                break;
            }

            double d = -MathHelper.sin((rotationYaw * (float) Math.PI) / 180F);
            double d1 = MathHelper.cos((rotationYaw * (float) Math.PI) / 180F);
            entityitem.posX = ((PlayerEntity) (playerentity)).posX + d * 0.5D;
            entityitem.posZ = ((PlayerEntity) (playerentity)).posZ + d1 * 0.5D;
            // entityitem.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.15F;
            // entityitem.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.15F;
        }
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        return health <= 0;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("ModelSize", modelsize);
        compound.putInt("Age", age);
        compound.putInt("DeathTimer", deathtimer);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        modelsize = compound.getFloat("ModelSize");
        age = compound.getInt("Age");
        deathtimer = compound.getInt("DeathTimer");
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    private void smoke() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                world.addParticle(ParticleTypes.EXPLOSION,
                        ((posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width) + (double) i,
                        posY + (double) (rand.nextFloat() * height),
                        (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width - (double) i,
                        posY + (double) (rand.nextFloat() * height),
                        (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width,
                        posY + (double) (rand.nextFloat() * height),
                        (posZ + (double) (rand.nextFloat() * width * 2.0F) + (double) i) - (double) width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width,
                        posY + (double) (rand.nextFloat() * height),
                        (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) i - (double) width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        ((posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width) + (double) i,
                        posY + (double) (rand.nextFloat() * height),
                        (posZ + (double) (rand.nextFloat() * width * 2.0F) + (double) i) - (double) width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width - (double) i,
                        posY + (double) (rand.nextFloat() * height),
                        (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) i - (double) width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        ((posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width) + (double) i,
                        posY + (double) (rand.nextFloat() * height),
                        (posZ + (double) (rand.nextFloat() * width * 2.0F) + (double) i) - (double) width, d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION,
                        (posX + (double) (rand.nextFloat() * width * 2.0F)) - (double) width - (double) i,
                        posY + (double) (rand.nextFloat() * height),
                        (posZ + (double) (rand.nextFloat() * width * 2.0F)) - (double) i - (double) width, d, d1, d2);
            }
        }
    }

    public void playLivingSound2()
    {
        PlayerEntity player = Minecraft.getInstance().player;
        SoundEvent s = getAmbientSound();

        if (s != null) {
            world.playSound(player, this.getPosition(), s, SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (3F - modelsize));
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound() {
        if (rand.nextInt(5) == 0) {
            return SoundsHandler.SCHLUMP;
        } else {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.SCHLUMP_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.SCHLUMP_DEATH;
    }

    public void confetti()
    {
        double d = -MathHelper.sin((((PlayerEntity)(playerentity)).rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((((PlayerEntity)(playerentity)).rotationYaw * (float)Math.PI) / 180F);
        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
        creepsentitytrophy.setLocationAndAngles(((PlayerEntity)(playerentity)).posX + d * 3D, ((PlayerEntity)(playerentity)).posY - 2D, ((PlayerEntity)(playerentity)).posZ + d1 * 3D, ((PlayerEntity)(playerentity)).rotationYaw, 0.0F);
        world.addEntity(creepsentitytrophy);
    }

    public void smallconfetti()
    {
        for (int i = 1; i < 20; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                // CREEPSFxConfetti creepsfxconfetti = new CREEPSFxConfetti(world, posX + (double)(world.rand.nextFloat() * 4F - world.rand.nextFloat() * 4F), posY + (double)rand.nextInt(4) + 6D, posZ + (double)(world.rand.nextFloat() * 4F - world.rand.nextFloat() * 4F), Item.getItemFromBlock(Block.getBlockById(world.rand.nextInt(99))));
                // creepsfxconfetti.renderDistanceWeight = 20D;
                // TODO fix FX entities
                // Minecraft.getInstance().gameRenderer.addEffect(creepsfxconfetti);
            }
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        PlayerEntity player = Minecraft.getInstance().player;

        smoke();
        world.playSound(player, this.getPosition(), getDeathSound(), SoundCategory.NEUTRAL, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        super.setHealth(0);
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource)
    {
        giveReward();
        super.onDeath(damagesource);
    }

	@Override
    public AgeableEntity createChild(AgeableEntity arg0)
    {
		return null;
	}
}
