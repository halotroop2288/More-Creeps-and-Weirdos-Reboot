package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ai.EntityBabyMummyAI;

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
        setSize(0.6F, 0.6F);
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
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("BabySize", babysize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        babysize = nbttagcompound.getFloat("BabySize");
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound(World world)
    {
        String s = getLivingSound();

        if (s != null)
        {
            world.playSoundAtEntity(this, s, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (0.7F - babysize) * 2.0F);
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere(World world)
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(getEntityBoundingBox().minY);
        int k = MathHelper.floor_double(posZ);
        //Fixed the light checker!
        int l = world.getBlockLightOpacity(getPosition());
        Block i1 = world.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
        return (i1 == Blocks.sand || i1 == Blocks.bedrock) && i1 != Blocks.cobblestone && i1 != Blocks.log && i1 != Blocks.planks && i1 != Blocks.wool && world.getCollidingBoundingBoxes(this, getEntityBoundingBox()).size() == 0 && rand.nextInt(15) == 0 && l > 10;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "morecreeps:babymummy";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "morecreeps:babymummyhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "morecreeps:babymummydeath";
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    public void attackEntity(Entity entity, float f, World world)
    {
        float f1 = getBrightness(1.0F);

        if (f1 < 0.5F && rand.nextInt(100) == 0)
        {
            //entityToAttack = null;
            return;
        }

        if (f > 2.0F && f < 6F && rand.nextInt(10) == 0)
        {
            if (onGround)
            {
                int i = MathHelper.floor_double(entity.posX);
                int j = MathHelper.floor_double(entity.posY);
                int k = MathHelper.floor_double(entity.posZ);

                if (world.getBlockState(new BlockPos(i, j - 2, k)).getBlock() == Blocks.sand)
                {
                    if (rand.nextInt(5) == 0)
                    {
                        for (int l = 0; l < rand.nextInt(4) + 1; l++)
                        {
                            world.setBlockToAir(new BlockPos(i, j - (l + 2), k));

                            if (rand.nextInt(5) == 0)
                            {
                                world.setBlockToAir(new BlockPos(i + l, j - 2, k));
                            }

                            if (rand.nextInt(5) == 0)
                            {
                                world.setBlockToAir(new BlockPos(i, j - 2, k + l));
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
                                world.setBlockState(new BlockPos(i + k1, j + i1, k + 2), Blocks.sand.getDefaultState());
                                world.setBlockState(new BlockPos(i + k1, j + i1, k - 2), Blocks.sand.getDefaultState());
                            }
                        }

                        if (rand.nextInt(2) == 0)
                        {
                            int j1 = rand.nextInt(5);

                            for (int l1 = -3; l1 < 3; l1++)
                            {
                                world.setBlockState(new BlockPos(i + 2, j + j1, k + l1), Blocks.sand.getDefaultState());
                                world.setBlockState(new BlockPos(i - 2, j + j1, k + l1), Blocks.sand.getDefaultState());
                            }
                        }
                    }

                    double d = entity.posX - posX;
                    double d1 = entity.posZ - posZ;
                    float f2 = MathHelper.sqrt_double(d * d + d1 * d1);
                    motionX = (d / (double)f2) * 0.5D * 0.8000000019209289D + motionX * 0.18000000098023225D;
                    motionZ = (d1 / (double)f2) * 0.5D * 0.70000000192092893D + motionZ * 0.18000000098023225D;
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
