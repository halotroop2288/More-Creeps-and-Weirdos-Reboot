package fr.elias.morecreeps.common.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.MobEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
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
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class BumEntity extends MobEntity
{
	ResourceLocation resource;
    public boolean rideable;
    protected double attackRange;
    private int angerLevel;
    private int value;
    private boolean bumgave;
    public int timetopee;
    public float bumrotation;
    public float modelsize;
    public ResourceLocation texture;
    public Random rand;
    public float width = getWidth();
    public float length = getWidth();
    public float height = getHeight();

    public BumEntity(World world)
    {
        super(null, world);
        //The texture reference
        texture = new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BUM);
        angerLevel = 0;
        attackRange = 16D;
        bumgave = false;
        timetopee = rand.nextInt(900) + 500;
        bumrotation = 999F;
        modelsize = 1.0F;
    }

    public void tick() {
        super.tick();
    }

    public void regsiterAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5D);
    }

    public String pingText() {
        return (new StringBuilder()).append("angerLevel ").append(angerLevel).toString();
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    public void tick(World world) {
        super.tick();
        if (timetopee-- < 0 && !bumgave && CREEPSConfig.publicUrination) {
            isJumping = false;

            if (bumrotation == 999F) {
                bumrotation = rotationYaw;
            }

            rotationYaw = bumrotation;
            if (!onGround) {
                moveVertical -= 0.5D;
            }

            if (world.isRemote) {
                MoreCreepsReboot.proxy.pee(world, posX, posY, posZ, rotationYaw, modelsize);
            }

            if (timetopee < -200) {
                timetopee = rand.nextInt(600) + 600;
                bumrotation = 999F;
                int j = MathHelper.floor(posX);
                int k = MathHelper.floor(getBoundingBox().minY);
                int l = MathHelper.floor(posZ);

                for (int i1 = -1; i1 < 2; i1++) {
                    for (int j1 = -1; j1 < 2; j1++) {
                        if (rand.nextInt(3) != 0) {
                            continue;
                        }

                        Block k1 = world.getBlockState(new BlockPos(j + j1, k - 1, l - i1)).getBlock();
                        Block l1 = world.getBlockState(new BlockPos(j + j1, k, l - i1)).getBlock();

                        if (rand.nextInt(2) == 0) {
                            if ((k1 == Blocks.GRASS || k1 == Blocks.DIRT) && l1 == Blocks.AIR) {
                                world.setBlockState(new BlockPos(j + j1, k, l - i1),
                                        Blocks.DANDELION.getDefaultState());
                            }

                            continue;
                        }

                        if ((k1 == Blocks.GRASS_BLOCK || k1 == Blocks.DIRT) && l1 == Blocks.AIR) {
                            world.setBlockState(new BlockPos(j + j1, k, l - i1), Blocks.POPPY.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getTrueSource();

        if (entity instanceof PlayerEntity) {
            setRevengeTarget((LivingEntity) entity);
            becomeAngryAt(entity);
        }

        timetopee = rand.nextInt(900) + 500;
        bumrotation = 999F;
        super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeAdditonal(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putShort("Anger", (short) angerLevel);
        compound.putBoolean("BumGave", bumgave);
        compound.putInt("TimeToPee", timetopee);
        compound.putFloat("BumRotation", bumrotation);
        compound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditonal(CompoundNBT compound) {
        super.readAdditional(compound);
        angerLevel = compound.getShort("Anger");
        bumgave = compound.getBoolean("BumGave");
        timetopee = compound.getInt("TimeToPee");
        bumrotation = compound.getFloat("BumRotation");
        modelsize = compound.getFloat("ModelSize");

        if (bumgave) {
            texture = new ResourceLocation(Reference.MODID,
                    Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BUM_DRESSED);
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this
     * entity.
     */
    public boolean getCanSpawnHere(World world) {
        int i = MathHelper.floor(posX);
        int j = MathHelper.floor(getBoundingBox().minY);
        int k = MathHelper.floor(posZ);
        int l = world.getLight(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG && i1 != Blocks.STONE_SLAB /*&& i1 != Blocks.double_stone_slab*/
                && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
                && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
                && world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(10) == 0 && l > 8;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }


    /**TEMPORARILY REMOVED TO FIND AN ALTERNATIVE TO THESE FUNCTIONS**/
    /*protected Entity findPlayerToAttack()
    {
        if (angerLevel == 0)
        {
            return null;EntityPigZombie
        }
        else
        {
            return super.findPlayerToAttack();
        }
    }

    
    
    public boolean canAttackEntity(Entity entity, int i)
    {
        if (entity instanceof PlayerEntity)
        {
            List list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(32D, 32D, 32D));

            for (int j = 0; j < list.size(); j++)
            {
                Entity entity1 = (Entity)list.get(j);

                if (entity1 instanceof CREEPSEntityBum)
                {
                    CREEPSEntityBum creepsentitybum = (CREEPSEntityBum)entity1;
                    creepsentitybum.becomeAngryAt(entity);
                }
            }

            becomeAngryAt(entity);
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    
    private void becomeAngryAt(Entity entity)
    {
        entityToAttack = entity;
        angerLevel = 400 + rand.nextInt(400);
    }*/

    /**Simple try if it work or not**/
    private void becomeAngryAt(Entity p_70835_1_)
    {
        this.angerLevel = 400 + this.rand.nextInt(400);

        if (p_70835_1_ instanceof LivingEntity)
        {
            this.setRevengeTarget((LivingEntity)p_70835_1_);
        }
    }
    
    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(PlayerEntity PlayerEntity)
    {
        ItemStack itemstack = PlayerEntity.inventory.getCurrentItem();

        if (!bumgave && angerLevel == 0)
        {
            if (itemstack != null && (itemstack.getItem() == Items.DIAMOND || itemstack.getItem() == Items.GOLD_INGOT || itemstack.getItem() == Items.IRON_INGOT))
            {
                if (itemstack.getItem() == Items.IRON_INGOT)
                {
                    value = rand.nextInt(2) + 1;
                }
                else if (itemstack.getItem() == Items.GOLD_INGOT)
                {
                    value = rand.nextInt(5) + 1;
                }
                else if (itemstack.getItem() == Items.DIAMOND)
                {
                    value = rand.nextInt(10) + 1;
                }

                if (itemstack.getCount() - 1 == 0)
                {
                    PlayerEntity.inventory.setInventorySlotContents(PlayerEntity.inventory.currentItem, null);
                }
                else
                {
                    itemstack.setCount(itemstack.getCount() - 1);
                }

                for (int i = 0; i < 4; i++)
                {
                    for (int i1 = 0; i1 < 10; i1++)
                    {
                        double d1 = rand.nextGaussian() * 0.02D;
                        double d3 = rand.nextGaussian() * 0.02D;
                        double d6 = rand.nextGaussian() * 0.02D;
                        world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height) + (double)i, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d1, d3, d6);
                    }
                }

                texture = new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES +
                		Reference.TEXTURE_BUM_DRESSED);
                angerLevel = 0;
                //findPlayerToAttack();

                if (rand.nextInt(5) == 0)
                {
                    world.playSound(PlayerEntity, this.getPosition(), SoundsHandler.BUM_SUCKER, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    bumgave = true;
                }
                else
                {
                    world.playSound(PlayerEntity, this.getPosition(), SoundsHandler.BUM_THANK_YOU, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    bumgave = true;

                    for (int j = 0; j < 10; j++)
                    {
                        double d = rand.nextGaussian() * 0.02D;
                        double d2 = rand.nextGaussian() * 0.02D;
                        double d5 = rand.nextGaussian() * 0.02D;
                        world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d2, d5);
                    }

                    for (int k = 0; k < value; k++)
                    {
                        entityDropItem(Item.getItemById(rand.nextInt(95)), 1);
                        entityDropItem(Items.IRON_SHOVEL, 1);
                    }

                    return true;
                }
            }
            else if (itemstack != null)
            {
                if (timetopee > 0)
                {
                    world.playSound(PlayerEntity, this.getPosition(), SoundsHandler.BUM_DONT_WANT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
                else if (itemstack != null && (itemstack.getItem() == Item.getItemFromBlock(Blocks.DANDELION) || itemstack.getItem() == Item.getItemFromBlock(Blocks.POPPY)))
                {
                	if(!((ServerPlayerEntity)PlayerEntity).getStats().hasAchievementUnlocked(ModAdvancementList.bumflower))
                	{
                    	world.playSound(PlayerEntity, PlayerEntity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                    	PlayerEntity.addStat(ModAdvancementList.bumflower, 1);
                    	confetti(PlayerEntity);
                	}

                    world.playSound(PlayerEntity, this.getPosition(), SoundsHandler.BUM_THANKS, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    timetopee = rand.nextInt(1900) + 1500;

                    if (itemstack.getCount() - 1 == 0)
                    {
                        PlayerEntity.inventory.setInventorySlotContents(PlayerEntity.inventory.currentItem, null);
                    }
                    else
                    {
                        itemstack.setCount(itemstack.getCount() - 1);
                    }
                }
                else if (itemstack != null && itemstack.getItem() == Items.BUCKET)
                {
                    if (!((ServerPlayerEntity)PlayerEntity).getStats().hasAchievementUnlocked(ModAdvancementList.bumpot) && ((ServerPlayerEntity)PlayerEntity).getStats().hasAchievementUnlocked(MoreCreepsReboot.bumflower))
                    {
                        world.playSound(PlayerEntity, PlayerEntity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                        PlayerEntity.addStat(ModAdvancementList.bumpot, 1);
                        confetti(PlayerEntity);
                    }
                    PlayerEntity.addStat(ModAdvancementList.bumpot, 1);
                    world.playSound(PlayerEntity, PlayerEntity.getPosition(), SoundsHandler.BUM_THANKS, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    timetopee = rand.nextInt(1900) + 1500;

                    if (itemstack.getCount() - 1 == 0)
                    {
                        PlayerEntity.inventory.setInventorySlotContents(PlayerEntity.inventory.currentItem, null);
                    }
                    else
                    {
                        itemstack.setCount(itemstack.getCount() - 1);
                    }
                }
                else if (itemstack != null && itemstack.getItem() == Items.LAVA_BUCKET)
                {
                    if (!((ServerPlayerEntity)PlayerEntity).getStats().hasAchievementUnlocked(MoreCreepsReboot.bumlava) && ((ServerPlayerEntity)PlayerEntity).getStats().hasAchievementUnlocked(MoreCreepsReboot.achievebumpot))
                    {
                        world.playSound(PlayerEntity, PlayerEntity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
                        PlayerEntity.addStat(MoreCreepsReboot.bumlava, 1);
                        confetti(PlayerEntity);
                    }

                    PlayerEntity.addStat(MoreCreepsReboot.bumpot, 1);
                    world.playSound(PlayerEntity, PlayerEntity.getPosition(), SoundsHandler.BUM_THANKS, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                    timetopee = rand.nextInt(1900) + 1500;

                    if (itemstack.getCount() - 1 == 0)
                    {
                        PlayerEntity.inventory.setInventorySlotContents(PlayerEntity.inventory.currentItem, null);
                    }
                    else
                    {
                        itemstack.setCount(itemstack.getCount() - 1);
                    }

                    int l = (int)posX;
                    int j1 = (int)posY;
                    int k1 = (int)posZ;

                    if (rand.nextInt(4) == 0)
                    {
                        for (int l1 = 0; l1 < rand.nextInt(3) + 1; l1++)
                        {
                            entityDropItem(Blocks.OBSIDIAN);
                        }
                    }

                    for (int i2 = 0; i2 < 15; i2++)
                    {
                        double d4 = (float)l + world.rand.nextFloat();
                        double d7 = (float)j1 + world.rand.nextFloat();
                        double d8 = (float)k1 + world.rand.nextFloat();
                        double d9 = d4 - posX;
                        double d10 = d7 - posY;
                        double d11 = d8 - posZ;
                        double d12 = MathHelper.sqrt(d9 * d9 + d10 * d10 + d11 * d11);
                        d9 /= d12;
                        d10 /= d12;
                        d11 /= d12;
                        double d13 = 0.5D / (d12 / 10D + 0.10000000000000001D);
                        d13 *= world.rand.nextFloat() * world.rand.nextFloat() + 0.3F;
                        d9 *= d13;
                        d10 *= d13;
                        d11 *= d13;
                        world.addParticle(ParticleTypes.SMOKE, (d4 + posX * 1.0D) / 2D, (d7 + posY * 1.0D) / 2D + 2D, (d8 + posZ * 1.0D) / 2D, d9, d10, d11);
                        world.addParticle(ParticleTypes.SMOKE, d4, d7, d8, d9, d10, d11);
                    }

                    if (rand.nextInt(4) == 0)
                    {
                        PlayerEntity.inventory.setInventorySlotContents(PlayerEntity.inventory.currentItem, new ItemStack(Items.BUCKET));
                    }
                }
                else if (!bumgave)
                {
                    world.playSound(PlayerEntity, this.getPosition(), SoundsHandler.BUM_PEE, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
            }
        }
        else
        {
            world.playSound(PlayerEntity, this.getPosition(), SoundsHandler.BUM_LEAVE_ME_ALONE, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }

        return false;
    }

    public void confetti(PlayerEntity player)
    {
    	MoreCreepsReboot.proxy.confettiA(player, world);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (timetopee > 0 || bumgave || !CREEPSConfig.publicUrination)
        {
            return SoundsHandler.BUM;
        }
        else
        {
            return SoundsHandler.BUM_LIVING_PEE;
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesource)
    {
        return SoundsHandler.BUM_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.BUM_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        if(!world.isRemote)
        {
            entityDropItem(Items.COOKED_PORKCHOP, 1);
        }
    }
}
