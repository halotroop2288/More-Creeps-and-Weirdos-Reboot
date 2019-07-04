package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class LetterGEntity extends MobEntity
{
    public float modelsize;
    public ResourceLocation texture;
    public float width = getWidth();
    public float length = getWidth();
    public float height = getHeight();

    public LetterGEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES+ "g.png");
//        setSize(width * 2.0F, height * 2.5F);
        modelsize = 2.0F;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(2, new LetterGEntity.AIAttackEntity());
        // this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        // this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        // this.tasks.addTask(8, new EntityAILookIdle(this));
        // this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        // this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }
    
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT nbttagcompound)
    {
        super.writeAdditional(nbttagcompound);
        nbttagcompound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT nbttagcompound)
    {
        super.readAdditional(nbttagcompound);
        modelsize = nbttagcompound.getFloat("ModelSize");
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
            moveForward = (float) (d * 11D);
            moveVertical = (float) (d1 * 11D);
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    protected void attackEntity(Entity entity, float f)
    {
        if (onGround)
        {
            double d = entity.posX - posX;
            double d2 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt(d * d + d2 * d2);
            moveForward = (float) ((d / (double)f1) * 0.40000000000000002D * 0.50000000192092897D + moveForward * 0.18000000098023225D);
            moveStrafing = (float) ((d2 / (double) f1) * 0.40000000000000002D * 0.37000000192092897D
                    + moveStrafing * 0.18000000098023225D);
            moveVertical = (float) 0.15000000019604645D;
        }

        if ((double)f < 6D)
        {
            double d1 = entity.posX - posX;
            double d3 = entity.posZ - posZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d3 * d3);
            moveForward = (float) ((d1 / (double)f2) * 0.40000000000000002D * 0.40000000192092894D + moveForward * 0.18000000098023225D);
            moveStrafing = (float) ((d3 / (double) f2) * 0.40000000000000002D * 0.27000000192092893D
                    + moveStrafing * 0.18000000098023225D);
            rotationPitch = 90F;
        }
    }
    
    class AIAttackEntity extends EntityAIBase
    {
		@Override
		public boolean shouldExecute() {
			return LetterGEntity.this.getAttackTarget() != null;
		}
		
		public void updateTask()
		{
			float f = LetterGEntity.this.getDistance(getAttackTarget());
			if(f < 256F)
			{
				attackEntity(LetterGEntity.this.getAttackTarget(), f);
				LetterGEntity.this.getLookController().setLookPositionWithEntity(LetterGEntity.this.getAttackTarget(), 10.0F, 10.0F);
				LetterGEntity.this.getNavigator().clearPath();
				LetterGEntity.this.getMoveHelper().setMoveTo(LetterGEntity.this.getAttackTarget().posX, LetterGEntity.this.getAttackTarget().posY, LetterGEntity.this.getAttackTarget().posZ, 0.5D);
			}
			if(f < 2F)
			{
				LetterGEntity.this.attackEntityAsMob(LetterGEntity.this.getAttackTarget());
			}
		}
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
        int j1 = world.countEntities(MummyEntity.class);
        return i1 != Blocks.SAND && i1 != Blocks.COBBLESTONE && i1 != Blocks.OAK_LOG /*&& i1 != Blocks.double_stone_slab*/
            && i1 != Blocks.STONE_SLAB && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//            && world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
            && rand.nextInt(15) == 0 && j1 < 5;
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
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (2.0F - modelsize) * 2.0F);
        }
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
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.G;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.G_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.G_DEATH;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource, World world)
    {
    	if(!world.isRemote)
    	{
            if (rand.nextInt(200) == 98)
            {
                entityDropItem(Blocks.GOLD_BLOCK, 1);
            }
            else if (rand.nextInt(5) == 0)
            {
                entityDropItem(Items.GOLD_INGOT, rand.nextInt(2) + 1);
            } else if (rand.nextInt(150) > 145) {
                entityDropItem(Items.GOLDEN_SWORD, 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(Items.GOLDEN_PICKAXE, 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(Items.GOLDEN_SHOVEL, 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(Items.GOLDEN_AXE, 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(Items.GOLDEN_HELMET, 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(Items.GOLDEN_CHESTPLATE, 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(Items.GOLDEN_BOOTS, 1);
            } else if (rand.nextInt(100) > 80) {
                entityDropItem(Items.WHEAT, rand.nextInt(6) + 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(Blocks.GLASS, rand.nextInt(6) + 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(ItemList.goo_donut, rand.nextInt(3) + 1);
            } else if (rand.nextInt(100) > 88) {
                entityDropItem(Blocks.GRASS, rand.nextInt(6) + 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(Blocks.GLOWSTONE, rand.nextInt(2) + 1);
            } else if (rand.nextInt(100) > 98) {
                entityDropItem(Items.GLOWSTONE_DUST, rand.nextInt(2) + 1);
            }
    	}
        super.onDeath(damagesource);
    }
}
