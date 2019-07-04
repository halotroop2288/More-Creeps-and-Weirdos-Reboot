package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ai.EntityBabyMummyAI;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class BabyMummyEntity extends MobEntity
{
    public ResourceLocation basetexture;
    public float babysize;
    static final ResourceLocation mummyTextures[] =
    {
        new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_MUMMY1), 
        new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_MUMMY2), 
        new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_MUMMY3)
    };
    public ResourceLocation texture;
    public int attackTime;
    public BabyMummyEntity(World world)
    {
        super(null, world);
        basetexture = mummyTextures[rand.nextInt(mummyTextures.length)];
        texture = basetexture;
        babysize = rand.nextFloat() * 0.45F + 0.25F;
//        setSize(0.6F, 0.6F);
        attackTime = 20;
        // ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        // this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(1, new EntityBabyMummyAI(this));
        // this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        // this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        // this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        // this.tasks.addTask(8, new EntityAILookIdle(this));
        // this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
    }
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT compound)
    {
        super.writeAdditional(compound);
        compound.putFloat("BabySize", babysize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound)
    {
        super.readAdditional(compound);
        babysize = compound.getFloat("BabySize");
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playLivingSound()
    {
    	World world = Minecraft.getInstance().world;
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, SoundCategory.HOSTILE, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (0.7F - babysize) * 2.0F);
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
        //Fixed the light checker!
        int l = world.getLight(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.SAND || i1 == Blocks.BEDROCK) && i1 != Blocks.COBBLESTONE && i1 != Blocks.log && i1 != Blocks.OAK_PLANKS && i1 != Blocks.WHITE_WOOL
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& rand.nextInt(15) == 0 && l > 10;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        return SoundsHandler.BABY_MUMMY;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        return SoundsHandler.BABY_MUMMY_HURT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundsHandler.BABY_MUMMY_DEATH;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    public void attackEntity(Entity entity, float f, World world)
    {
        float f1 = getBrightness();

        if (f1 < 0.5F && rand.nextInt(100) == 0)
        {
            //entityToAttack = null;
            return;
        }

        if (f > 2.0F && f < 6F && rand.nextInt(10) == 0)
        {
            if (onGround)
            {
                int i = MathHelper.floor(entity.posX);
                int j = MathHelper.floor(entity.posY);
                int k = MathHelper.floor(entity.posZ);

                if (world.getBlockState(new BlockPos(i, j - 2, k)).getBlock() == Blocks.sand)
                {
                    if (rand.nextInt(5) == 0)
                    {
                        for (int l = 0; l < rand.nextInt(4) + 1; l++)
                        {
                            world.setBlockState(new BlockPos(i, j - (l + 2), k), Blocks.AIR.getDefaultState());

                            if (rand.nextInt(5) == 0)
                            {
                                world.setBlockState(new BlockPos(i + l, j - 2, k), Blocks.AIR.getDefaultState());
                            }

                            if (rand.nextInt(5) == 0)
                            {
                                world.setBlockState(new BlockPos(i, j - 2, k + l), Blocks.AIR.getDefaultState());
                            }
                        }
                    }

                    if (rand.nextInt(5) == 0)
                    {
                        if (rand.nextInt(2) == 0)
                        {
                            int i1 = rand.nextInt(5);

                            for (int k1 = -3; k1 < 3; k1++)
                            {
                                world.setBlockState(new BlockPos(i + k1, j + i1, k + 2), Blocks.SAND.getDefaultState());
                                world.setBlockState(new BlockPos(i + k1, j + i1, k - 2), Blocks.SAND.getDefaultState());
                            }
                        }

                        if (rand.nextInt(2) == 0)
                        {
                            int j1 = rand.nextInt(5);

                            for (int l1 = -3; l1 < 3; l1++)
                            {
                                world.setBlockState(new BlockPos(i + 2, j + j1, k + l1), Blocks.SAND.getDefaultState());
                                world.setBlockState(new BlockPos(i - 2, j + j1, k + l1), Blocks.SAND.getDefaultState());
                            }
                        }
                    }

                    double d = entity.posX - posX;
                    double d1 = entity.posZ - posZ;
                    float f2 = MathHelper.sqrt(d * d + d1 * d1);
                    setMotion(
                    		(d / (double)f2) * 0.5D * 0.8000000019209289D + getMotion().x * 0.18000000098023225D, // motionX
                    		getMotion().y, // motionY
                    		(d1 / (double)f2) * 0.5D * 0.70000000192092893D + motionZ * 0.18000000098023225D // motionZ
                    		);
                }
            }
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
                dropItem(Item.getItemFromBlock(Blocks.wool), rand.nextInt(6) + 1);
            }
            else
            {
                dropItem(Item.getItemFromBlock(Blocks.sand), rand.nextInt(3) + 1);
            }
    	}

        super.onDeath(damagesource);
    }
}
