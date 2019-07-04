package fr.elias.morecreeps.common.entity;

import java.util.Collection;
import java.util.List;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class LawyerFromHellEntity extends MobEntity
{
    private boolean foundplayer;
    private boolean stolen;
    private EntityPath pathToEntity;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    protected ItemStack stolengood;
    private double goX;
    private double goZ;
    private float distance;
    public int itemnumber;
    public int stolenamount;
    public boolean undead;
    public int jailX;
    public int jailY;
    public int jailZ;
    public int area;
    public int lawyerstate;
    public int lawyertimer;
    private static ItemStack defaultHeldItem;
    public float modelsize;
    public int maxObstruct;
    public String texture;

    public LawyerFromHellEntity(World world)
    {
        super(null, world);
        texture = "morecreeps:textures/entity/lawyerfromhell.png";

        if (undead)
        {
            texture = "morecreeps:textures/entity/lawyerfromhellundead.png";
        }
        stolen = false;
        hasAttacked = false;
        foundplayer = false;
        lawyerstate = 0;
        lawyertimer = 0;

        if (!undead)
        {
            defaultHeldItem = null;
        }

        modelsize = 1.0F;
        maxObstruct = 20;
        
//        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
//        this.tasks.addTask(0, new EntityAISwimming(this));
//        //this.tasks.addTask(1, new CREEPSEntityLawyerFromHell.AIAttackEntity());
//        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
//        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
//        this.tasks.addTask(4, new EntityAILookIdle(this));
//        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
//        this.targetTasks.addTask(2, new LawyerFromHellEntity.AIAttackEntity());
    }

    @Override
    public void registerAttributes()
    {
    	super.registerAttributes();
    	this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40D);
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.44D);
    	this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
    }
    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected boolean findPlayerToAttack(World world)
    {
        if (lawyerstate == 0 && !undead)
        {
            return false;
        }

        if (MoreCreepsReboot.instance.currentfine <= 0 && !undead)
        {
            lawyerstate = 0;
            //pathToEntity = null;
            return false;
        }

        if (lawyerstate > 0)
        {
        	PlayerEntity entityplayer = world.getClosestPlayer(this, 16D);

            if (entityplayer != null && canEntityBeSeen(entityplayer))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void livingTick()
    {
        texture = undead ? "morecreeps:textures/entity/lawyerfromhellundead.png" : "morecreeps:textures/entity/lawyerfromhell.png";
    	this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(undead ? 0.24D : 0.44D);

        if (undead && defaultHeldItem == null)
        {
            defaultHeldItem = new ItemStack(Items.BONE, 1);
        }

        super.livingTick();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        if (MoreCreepsReboot.instance.currentfine > 0 && lawyerstate == 0 && !undead)
        {
            lawyerstate = 1;
        }

        if (MoreCreepsReboot.instance.currentfine > 2500 && lawyerstate < 5 && !undead)
        {
            lawyerstate = 5;
        }

        if (undead)
        {
            lawyerstate = 1;
        }

        super.tick();
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        if (undead && (collidedHorizontally || collidedVertically))
        {
            return false;
        }
        else
        {
            return super.isEntityInsideOpaqueBlock();
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float i, World world)
    {
    	PlayerEntity playerentity = Minecraft.getInstance().player;
        Entity entity = damagesource.getTrueSource();

        if (!undead && (entity instanceof PlayerEntity) || (entity instanceof GuineaPigEntity) && ((GuineaPigEntity)entity).tamed || (entity instanceof HotdogEntity) && ((HotdogEntity)entity).tamed || (entity instanceof ArmyGuyEntity) && ((ArmyGuyEntity)entity).loyal)
        {
        	MoreCreepsReboot.instance.currentfine += 50;
        }

        if (!undead)
        {
            if ((entity instanceof PlayerEntity) || (entity instanceof HotdogEntity) && ((HotdogEntity)entity).tamed || (entity instanceof GuineaPigEntity) && ((GuineaPigEntity)entity).tamed || (entity instanceof ArmyGuyEntity) && ((ArmyGuyEntity)entity).loyal)
            {
                if (lawyerstate == 0)
                {
                    lawyerstate = 1;
                }

                setRevengeTarget((LivingEntity) entity);
            }

            if (entity instanceof PlayerEntity)
            {
                if (lawyerstate == 0)
                {
                    lawyerstate = 1;
                }

                if (rand.nextInt(5) == 0)
                {
                    for (int j = 0; j < rand.nextInt(20) + 5; j++)
                    {
                        MoreCreepsReboot.instance.currentfine += 25;
                        world.playSound(playerentity, this.getPosition(), SoundsHandler.LAWYER_MONEY_HIT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        if(!world.isRemote)
                        entityDropItem(ItemList.money, 1);
                    }
                }

                if (rand.nextInt(5) == 0)
                {
                    for (int k = 0; k < rand.nextInt(3) + 1; k++)
                    {
                        MoreCreepsReboot.instance.currentfine += 10;
                        world.playSound(playerentity, this.getPosition(), SoundsHandler.LAWYER_MONEY_HIT, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        if(!world.isRemote)
                        entityDropItem(Items.PAPER, 1);
                    }
                }
            }
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    @SuppressWarnings("rawtypes")
    class AIAttackEntity extends Brain
    {
		public AIAttackEntity(Collection p_i50378_1_, Collection p_i50378_2_, Dynamic p_i50378_3_) {
			super(p_i50378_1_, p_i50378_2_, p_i50378_3_);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean shouldExecute()
		{
			return LawyerFromHellEntity.this.findPlayerToAttack();
		}
		
		public void updateTask()
		{
			float f = LawyerFromHellEntity.this.getDistance(getAttackTarget());
			if(f < 256F)
			{
				attackEntity(LawyerFromHellEntity.this.getAttackTarget(), f);
				LawyerFromHellEntity.this.getLookController().setLookPositionWithEntity(LawyerFromHellEntity.this.getAttackTarget(), 10.0F, 10.0F);
				LawyerFromHellEntity.this.getNavigator().clearPath();
				LawyerFromHellEntity.this.getMoveHelper().setMoveTo(LawyerFromHellEntity.this.getAttackTarget().posX, LawyerFromHellEntity.this.getAttackTarget().posY, LawyerFromHellEntity.this.getAttackTarget().posZ, 0.5D);
			}
			if(f < 1F)
			{
				//CREEPSEntityLawyerFromHell.this.attackEntityAsMob(CREEPSEntityKid.this.getAttackTarget());
			}
		}
    }
    protected void attackEntity(Entity entity, float f)
    {
    	//TODO put this on "shouldExecute"
        /*if (!undead && lawyerstate == 0)
        {
            entityToAttack = null;
            return;
        }*/

        if (this.getAttackTarget() instanceof PlayerEntity)
        {
            if (MoreCreepsReboot.instance.currentfine <= 0 && !undead)
            {
                pathToEntity = null;
                return;
            }

            if (onGround)
            {
                float f1 = 1.0F;

                if (undead)
                {
                    f1 = 0.5F;
                }

                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f2 = MathHelper.sqrt(d * d + d1 * d1);
                setMotion(
                		((d / (double)f2) * 0.5D * 0.40000001192092893D + getMotion().x * 0.20000000298023224D) * (double)f1,
                		0.40000000596046448D,
                		((d1 / (double)f2) * 0.5D * 0.30000001192092896D + getMotion().z * 0.20000000298023224D) * (double)f1
                		);
            }
            else if ((double)f < 2.6000000000000001D)
            {
                if (rand.nextInt(50) == 0 && (entity instanceof PlayerEntity))
                {
                    suckMoney((PlayerEntity) entity);
                }

                if (undead)
                {
                    this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D);
                }

                if ((entity instanceof PlayerEntity) && lawyerstate == 5 && !undead && rand.nextInt(10) == 0 && CREEPSConfig.jailActive && MoreCreepsReboot.instance.currentfine >= 2500)
                {
                    for (int i = 0; i < 21; i++)
                    {
                        PlayerEntity entityplayer = (PlayerEntity)entity;
                        Object obj = entityplayer;
                        int k;

                        for (k = 0; ((Entity)obj).riddenByEntity != null && k < 20; k++)
                        {
                            obj = ((Entity)obj).riddenByEntity;
                        }

                        if (k < 20)
                        {
                            ((Entity)obj).fallDistance = -25F;
                            ((Entity)obj).mountEntity(null);
                        }
                    }

                    buildJail((PlayerEntity) entity);
                }

                //super.attackEntity(entity, f);
            }
            else if ((double)f < 5D && (entity instanceof PlayerEntity) && rand.nextInt(25) == 0 && !undead && CREEPSConfig.jailActive && lawyerstate == 5 && MoreCreepsReboot.instance.currentfine >= 2500)
            {
                for (int j = 0; j < 21; j++)
                {
                    PlayerEntity entityplayer1 = (PlayerEntity)entity;
                    Object obj1 = entityplayer1;
                    int l;

                    for (l = 0; ((Entity)obj1).riddenByEntity != null && l < 20; l++)
                    {
                        obj1 = ((Entity)obj1).riddenByEntity;
                    }

                    if (l < 20)
                    {
                        ((Entity)obj1).fallDistance = -25F;
                        ((Entity)obj1).mountEntity(null);
                    }
                }

                MoreCreepsReboot.proxy.addChatMessage(" ");
                MoreCreepsReboot.proxy.addChatMessage("\2474  BUSTED! \2476Sending guilty player to Jail");
                MoreCreepsReboot.proxy.addChatMessage(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . .");
                buildJail((PlayerEntity) entity);
            }

            //super.attackEntity(entity, f);
        }
    }

    public boolean buildJail(PlayerEntity entityplayersp)
    {
        int i = rand.nextInt(200) - 100;

        if (rand.nextInt(2) == 0)
        {
            i *= -1;
        }

        jailX = (int)(((PlayerEntity)(entityplayersp)).posX + (double)i);
        jailY = rand.nextInt(20) + 25;
        jailZ = (int)(((PlayerEntity)(entityplayersp)).posZ + (double)i);
        maxObstruct = 0x1869f;

        if (MoreCreepsReboot.instance.jailBuilt)
        {
            jailX = MoreCreepsReboot.instance.currentJailX;
            jailY = MoreCreepsReboot.instance.currentJailY;
            jailZ = MoreCreepsReboot.instance.currentJailZ;
        }
        else
        {
            if (!blockExists(world, jailX, jailY, jailZ - 31) || !blockExists(world, jailX + 14, jailY, jailZ - 31) || !blockExists(world, jailX, jailY, jailZ + 45) || !blockExists(world, jailX + 14, jailY, jailZ + 45))
            {
                return false;
            }

            if (!MoreCreepsReboot.instance.jailBuilt)
            {
                area = 0;
                int j = -1;
                label0:

                do
                {
                    if (j >= 6)
                    {
                        break;
                    }

                    for (int k1 = -1; k1 < 14; k1++)
                    {
                        for (int l2 = -1; l2 < 14; l2++)
                        {
                            if (world.getBlockState(new BlockPos(jailX + k1, jailY + j, jailZ + l2)).getBlock() == Blocks.AIR)
                            {
                                area++;
                            }

                            if (area > maxObstruct)
                            {
                                break label0;
                            }
                        }
                    }

                    j++;
                }
                while (true);
            }

            if (world.getBlockState(new BlockPos(jailX + 15 + 1, jailY + 20, jailZ + 7)).getBlock() == Blocks.WATER)
            {
                area++;
            }

            if (area <= maxObstruct)
            {
                for (int k = -1; k < 6; k++)
                {
                    for (int l1 = -41; l1 < 55; l1++)
                    {
                        for (int i3 = -1; i3 < 16; i3++)
                        {
                            int j4 = rand.nextInt(100);

                            if (j4 < 1)
                            {
                                world.setBlockState(new BlockPos(jailX + i3, jailY + k, jailZ + l1), Blocks.GRAVEL.getDefaultState());
                                continue;
                            }

                            if (j4 < 15)
                            {
                                world.setBlockState(new BlockPos(jailX + i3, jailY + k, jailZ + l1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
                            }
                            else
                            {
                                world.setBlockState(new BlockPos(jailX + i3, jailY + k, jailZ + l1), Blocks.STONE.getDefaultState());
                            }
                        }
                    }
                }

                for (int l = 0; l < 5; l++)
                {
                    for (int i2 = 0; i2 < 13; i2++)
                    {
                        for (int j3 = 0; j3 < 13; j3++)
                        {
                            world.setBlockState(new BlockPos(jailX + j3, jailY + l, jailZ + i2 + 1), Blocks.AIR.getDefaultState());
                            world.setBlockState(new BlockPos(jailX + j3, jailY + l, jailZ + i2 + 1), Blocks.AIR.getDefaultState());
                        }
                    }
                }

                for (int i1 = 0; i1 < 5; i1++)
                {
                    for (int j2 = 3; j2 < 11; j2++)
                    {
                        for (int k3 = 3; k3 < 11; k3++)
                        {
                            world.setBlockState(new BlockPos(jailX + k3, jailY + i1, jailZ + j2 + 1), Blocks.STONE.getDefaultState());
                            world.setBlockState(new BlockPos(jailX + k3, jailY + i1, jailZ + j2 + 1), Blocks.STONE.getDefaultState());
                        }
                    }
                }

                for (int j1 = 0; j1 < 5; j1++)
                {
                    for (int k2 = 5; k2 < 9; k2++)
                    {
                        for (int l3 = 5; l3 < 9; l3++)
                        {
                            world.setBlockState(new BlockPos(jailX + l3, jailY + j1, jailZ + k2 + 1), Blocks.AIR.getDefaultState());
                            world.setBlockState(new BlockPos(jailX + l3, jailY + j1, jailZ + k2 + 1), Blocks.AIR.getDefaultState());
                        }
                    }
                }

                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ + 4), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ + 5), Blocks.IRON_BARS.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY + 1, jailZ + 7), Blocks.GLASS.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 4, jailY + 1, jailZ + 7), Blocks.GLASS.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 10, jailY + 1, jailZ + 7), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ + 7), Blocks.IRON_BARS.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ + 11), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ + 10), Blocks.IRON_BARS.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 4, jailY, jailZ + 8), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY, jailZ + 8), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 4, jailY + 1, jailZ + 8), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY + 1, jailZ + 8), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY, jailZ + 8), Blocks.OAK_DOOR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY, jailZ + 8), Blocks.OAK_DOOR.getDefaultState(), 0);//setBlockmetadatawithnotify
                world.setBlockState(new BlockPos(jailX + 3, jailY + 1, jailZ + 8), Blocks.OAK_DOOR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY + 1, jailZ + 8), Blocks.OAK_DOOR.getDefaultState(), 8);//wtf? 8 flag ?
                byte byte0 = 15;
                byte byte1 = 7;
                int i4;

                for (i4 = 80; world.getBlockState(new BlockPos(jailX + byte0, i4, jailZ + byte1)).getBlock() == Blocks.AIR || world.getBlockState(new BlockPos(jailX + byte0, i4, jailZ + byte1)).getBlock() == Blocks.OAK_LEAVES.getDefaultState() || world.getBlockState(new BlockPos(jailX + byte0, i4, jailZ + byte1)).getBlock() == Blocks.OAK_LOG.getDefaultState(); i4--) { }

                for (int k4 = 0; k4 < i4 - jailY; k4++)
                {
                    for (int i5 = 0; i5 < 2; i5++)
                    {
                        for (int i7 = 0; i7 < 2; i7++)
                        {
                            world.setBlockState(new BlockPos(jailX + i5 + byte0, jailY + k4, jailZ + byte1 + i7), Blocks.AIR.getDefaultState());
                        }
                    }
                }

                int l4 = 0;

                for (int j5 = 0; j5 < i4 - jailY; j5++)
                {
                    if (l4 == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + byte0, jailY + j5, jailZ + byte1), Blocks.STONE_STAIRS.getDefaultState());
                        world.setBlockState(new BlockPos(jailX + byte0, jailY + j5, jailZ + byte1), Blocks.STONE_STAIRS.getDefaultState(), 3);
                    }

                    if (l4 == 1)
                    {
                        world.setBlockState(new BlockPos(jailX + byte0 + 1, jailY + j5, jailZ + byte1), Blocks.STONE_STAIRS.getDefaultState());
                        world.setBlockState(new BlockPos(jailX + byte0 + 1, jailY + j5, jailZ + byte1), Blocks.STONE_STAIRS.getDefaultState(), 0);
                    }

                    if (l4 == 2)
                    {
                        world.setBlockState(new BlockPos(jailX + byte0 + 1, jailY + j5, jailZ + byte1 + 1), Blocks.STONE_STAIRS.getDefaultState());
                        world.setBlockState(new BlockPos(jailX + byte0 + 1, jailY + j5, jailZ + byte1 + 1), Blocks.STONE_STAIRS.getDefaultState(), 2);
                    }

                    if (l4 == 3)
                    {
                        world.setBlockState(new BlockPos(jailX + byte0, jailY + j5, jailZ + byte1 + 1), Blocks.STONE_STAIRS.getDefaultState());
                        world.setBlockState(new BlockPos(jailX + byte0, jailY + j5, jailZ + byte1 + 1), Blocks.STONE_STAIRS.getDefaultState(), 1);
                    }

                    if (l4++ == 3)
                    {
                        l4 = 0;
                    }
                }

                for (int k5 = 0; k5 < 3; k5++)
                {
                    world.setBlockState(new BlockPos(jailX + 13 + k5, jailY, jailZ + 7), Blocks.AIR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 13 + k5, jailY + 1, jailZ + 7), Blocks.AIR.getDefaultState());
                }

                world.setBlockState(new BlockPos(jailX + 13, jailY, jailZ + byte1), Blocks.IRON_DOOR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 13, jailY, jailZ + byte1), Blocks.IRON_DOOR.getDefaultState(), 0);
                world.setBlockState(new BlockPos(jailX + 13, jailY + 1, jailZ + byte1), Blocks.IRON_DOOR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 13, jailY + 1, jailZ + byte1), Blocks.IRON_DOOR.getDefaultState(), 8);
                world.setBlockState(new BlockPos(jailX + 15, jailY, jailZ + byte1), Blocks.STONE_STAIRS.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 15, jailY, jailZ + byte1), Blocks.STONE_STAIRS.getDefaultState(), 0);
                world.setBlockState(new BlockPos(jailX + 14, jailY + 2, jailZ + byte1), Blocks.AIR.getDefaultState());

                for (int l5 = 0; l5 < 32; l5++)
                {
                    for (int j7 = 6; j7 < 9; j7++)
                    {
                        for (int l7 = 0; l7 < 4; l7++)
                        {
                            world.setBlockState(new BlockPos(jailX + j7, jailY + l7, jailZ - l5 - 1), Blocks.AIR.getDefaultState());
                            world.setBlockState(new BlockPos(jailX + j7, jailY + l7, jailZ + l5 + 15), Blocks.AIR.getDefaultState());
                        }
                    }
                }

                for (int i6 = 1; i6 < 5; i6++)
                {
                    for (int k7 = 0; k7 < 3; k7++)
                    {
                        for (int i8 = 0; i8 < 3; i8++)
                        {
                            for (int l8 = 0; l8 < 4; l8++)
                            {
                                world.setBlockState(new BlockPos(jailX + 10 + i8, jailY + l8, (jailZ - i6 * 7) + k7), Blocks.AIR.getDefaultState());
                                world.setBlockState(new BlockPos(jailX + 2 + i8, jailY + l8, (jailZ - i6 * 7) + k7), Blocks.AIR.getDefaultState());
                                world.setBlockState(new BlockPos(jailX + 10 + i8, jailY + l8, jailZ + i6 * 7 + 12 + k7), Blocks.AIR.getDefaultState());
                                world.setBlockState(new BlockPos(jailX + 2 + i8, jailY + l8, jailZ + i6 * 7 + 12 + k7), Blocks.AIR.getDefaultState());
                            }
                        }
                    }
                }

                world.setBlockState(new BlockPos(jailX + 7, jailY, jailZ), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 7, jailY, jailZ + 14), Blocks.AIR.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ + 14), Blocks.AIR.getDefaultState());

                for (int j6 = 0; j6 < 4; j6++)
                {
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ - j6 * 7 - 5 - 2), Blocks.IRON_BARS.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY, jailZ - j6 * 7 - 5), Blocks.OAK_DOOR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY, jailZ - j6 * 7 - 5), Blocks.OAK_DOOR.getDefaultState(), 2);
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ - j6 * 7 - 5), Blocks.OAK_DOOR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ - j6 * 7 - 5), Blocks.OAK_DOOR.getDefaultState(), 10);
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ - j6 * 7 - 5 - 2), Blocks.IRON_BARS.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ - j6 * 7 - 5), Blocks.AIR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ - j6 * 7 - 5), Blocks.AIR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ - j6 * 7 - 5), Blocks.OAK_DOOR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ - j6 * 7 - 5), Blocks.OAK_DOOR.getDefaultState(), 0);
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ - j6 * 7 - 5), Blocks.OAK_DOOR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ - j6 * 7 - 5), Blocks.OAK_DOOR.getDefaultState(), 8);
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ + j6 * 7 + 19 + 2), Blocks.IRON_BARS.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY, jailZ + j6 * 7 + 19), Blocks.OAK_DOOR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY, jailZ + j6 * 7 + 19), Blocks.OAK_DOOR.getDefaultState(), 2);
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ + j6 * 7 + 19), Blocks.OAK_DOOR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ + j6 * 7 + 19), Blocks.OAK_DOOR.getDefaultState(), 10);
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ + j6 * 7 + 19 + 2), Blocks.IRON_BARS.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ + j6 * 7 + 19), Blocks.AIR.getDefaultState());
                    world.setBlockToAir(new BlockPos(jailX + 9, jailY + 1, jailZ + j6 * 7 + 19), Blocks.AIR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ + j6 * 7 + 19), Blocks.OAK_DOOR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ + j6 * 7 + 19), Blocks.OAK_DOOR.getDefaultState(), 0);
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ + j6 * 7 + 19), Blocks.OAK_DOOR.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ + j6 * 7 + 19), Blocks.OAK_DOOR.getDefaultState(), 8);

                    if (rand.nextInt(1) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 12, jailY + 2, jailZ - j6 * 7 - 5), Blocks.TORCH.getDefaultState());
                    }

                    if (rand.nextInt(1) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 2, jailY + 2, jailZ - j6 * 7 - 5), Blocks.TORCH.getDefaultState());
                    }

                    if (rand.nextInt(1) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 12, jailY + 2, jailZ + j6 * 7 + 19), Blocks.TORCH.getDefaultState());
                    }

                    if (rand.nextInt(1) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 2, jailY + 2, jailZ + j6 * 7 + 19), Blocks.TORCH.getDefaultState());
                    }
                }

                for (int k6 = 0; k6 < 9; k6++)
                {
                    if (rand.nextInt(2) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 6, jailY + 2, jailZ - k6 * 4 - 2), Blocks.TORCH.getDefaultState());
                    }

                    if (rand.nextInt(2) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 8, jailY + 2, jailZ - k6 * 4 - 2), Blocks.TORCH.getDefaultState());
                    }

                    if (rand.nextInt(2) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 6, jailY + 2, jailZ + k6 * 4 + 18), Blocks.TORCH.getDefaultState());
                    }

                    if (rand.nextInt(2) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 8, jailY + 2, jailZ + k6 * 4 + 18), Blocks.TORCH.getDefaultState());
                    }
                }
            }
            else
            {
                return false;
            }
        }

        world.setBlockState(new BlockPos(jailX + 12, jailY, jailZ + 13), Blocks.CHEST.getDefaultState());
        ChestTileEntity tileentitychest = new ChestTileEntity();
        world.setTileEntity(new BlockPos(jailX + 12, jailY, jailZ + 13), tileentitychest);
        world.setBlockState(new BlockPos(jailX + 12, jailY, jailZ + 1), Blocks.CHEST.getDefaultState());
        ChestTileEntity tileentitychest1 = new ChestTileEntity();
        world.setTileEntity(new BlockPos(jailX + 12, jailY, jailZ + 1), tileentitychest1);
        world.setBlockState(new BlockPos(jailX, jailY, jailZ + 13), Blocks.CHEST.getDefaultState());
        ChestTileEntity tileentitychest2 = new ChestTileEntity();
        world.setTileEntity(new BlockPos(jailX, jailY, jailZ + 13), tileentitychest2);
        world.setBlockState(new BlockPos(jailX, jailY, jailZ + 1), Blocks.CHEST.getDefaultState());
        ChestTileEntity tileentitychest3 = new ChestTileEntity();
        world.setTileEntity(new BlockPos(jailX, jailY, jailZ + 1), tileentitychest3);

        for (int l6 = 1; l6 < tileentitychest.getSizeInventory(); l6++)
        {
            tileentitychest.setInventorySlotContents(l6, null);
            tileentitychest1.setInventorySlotContents(l6, null);
            tileentitychest2.setInventorySlotContents(l6, null);
        }

        Object obj = null;
        NonNullList<ItemStack> aitemstack = ((PlayerEntity)(entityplayersp)).inventory.mainInventory;

        for (int j8 = 0; j8 < aitemstack.size(); j8++)
        {
            ItemStack itemstack = aitemstack[j8];

            if (itemstack != null)
            {
                tileentitychest.setInventorySlotContents(j8, itemstack);
                ((PlayerEntity)(entityplayersp)).inventory.mainInventory[j8] = null;
            }
        }

        for (int k8 = 1; k8 < tileentitychest3.getSizeInventory(); k8++)
        {
            int i9 = rand.nextInt(10);

            if (i9 == 1)
            {
                tileentitychest3.setInventorySlotContents(k8, new ItemStack(ItemList.band_aid, rand.nextInt(2) + 1));
            }

            if (i9 == 2)
            {
                tileentitychest3.setInventorySlotContents(k8, new ItemStack(ItemList.money, rand.nextInt(24) + 1));
            }
        }

        world.setBlockState(new BlockPos(jailX + 11, jailY, jailZ + 13), Blocks.SPAWNER.getDefaultState());
        MobSpawnerTileEntity tileentitymobspawner = new MobSpawnerTileEntity();
        world.setTileEntity(new BlockPos(jailX + 11, jailY, jailZ + 13), tileentitymobspawner);
        tileentitymobspawner.getSpawnerBaseLogic().setEntityName("Skeleton");
        tileentitychest1.setInventorySlotContents(rand.nextInt(5), new ItemStack(Items.STONE_PICKAXE, 1));
        tileentitychest1.setInventorySlotContents(rand.nextInt(5) + 5, new ItemStack(Items.APPLE, 1));
        tileentitychest2.setInventorySlotContents(rand.nextInt(5) + 5, new ItemStack(Blocks.TORCH, rand.nextInt(16)));
        tileentitychest2.setInventorySlotContents(rand.nextInt(5), new ItemStack(Items.APPLE, 1));
        world.setBlockState(new BlockPos(jailX + 6, jailY + 2, jailZ + 9), Blocks.torch.getDefaultState());
        int j9 = rand.nextInt(11);

        for (int k9 = 0; k9 < 4; k9++)
        {
            for (int j10 = 0; j10 < 4; j10++)
            {
                int l10 = 0;
                int i11 = 0;

                switch (j10 + 1)
                {
                    case 1:
                        l10 = jailX + 12;
                        i11 = jailZ - k9 * 7 - 5;
                        break;

                    case 2:
                        l10 = jailX + 2;
                        i11 = jailZ - k9 * 7 - 5;
                        break;

                    case 3:
                        l10 = jailX + 12;
                        i11 = jailZ + k9 * 7 + 19;
                        break;

                    case 4:
                        l10 = jailX + 2;
                        i11 = jailZ + k9 * 7 + 19;
                        break;

                    default:
                        l10 = jailX + 12;
                        i11 = jailZ - k9 * 7 - 5;
                        break;
                }

                if (k9 * 4 + j10 == j9)
                {
                    populateCell(l10, i11, world, entityplayersp, true);
                }
                else
                {
                    populateCell(l10, i11, world, entityplayersp, false);
                }

                if (rand.nextInt(3) == 0)
                {
                    dropTreasure(world, jailX + 12, jailY + 2, jailZ - k9 * 7 - 5 - 1);
                }

                if (rand.nextInt(3) == 0)
                {
                    dropTreasure(world, jailX + 2, jailY + 2, jailZ - k9 * 7 - 5 - 1);
                }

                if (rand.nextInt(3) == 0)
                {
                    dropTreasure(world, jailX + 12, jailY + 2, jailZ + k9 * 7 + 19 + 1);
                }

                if (rand.nextInt(3) == 0)
                {
                    dropTreasure(world, jailX + 2, jailY + 2, jailZ + k9 * 7 + 19 + 1);
                }
            }
        }

        for (int l9 = 1; l9 < rand.nextInt(5) + 3; l9++)
        {
            LawyerFromHellEntity creepsentitylawyerfromhell = new LawyerFromHellEntity(world);
            creepsentitylawyerfromhell.setLocationAndAngles(jailX + 8, jailY + 1, jailZ - 11 - 1, ((PlayerEntity)(entityplayersp)).rotationYaw, 0.0F);
            creepsentitylawyerfromhell.undead = true;
            creepsentitylawyerfromhell.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10D);
            world.addEntity(creepsentitylawyerfromhell);
            LawyerFromHellEntity creepsentitylawyerfromhell2 = new LawyerFromHellEntity(world);
            creepsentitylawyerfromhell2.setLocationAndAngles(jailX + 8, jailY + 1, jailZ + 11 + 15, ((PlayerEntity)(entityplayersp)).rotationYaw, 0.0F);
            creepsentitylawyerfromhell2.undead = true;
            creepsentitylawyerfromhell.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
            world.addEntity(creepsentitylawyerfromhell2);
        }

        for (int i10 = 2; i10 < rand.nextInt(3) + 3; i10++)
        {
            LawyerFromHellEntity creepsentitylawyerfromhell1 = new LawyerFromHellEntity(world);
            creepsentitylawyerfromhell1.setLocationAndAngles(jailX + i10, jailY + 2, jailZ + 2, ((PlayerEntity)(entityplayersp)).rotationYaw, 0.0F);
            creepsentitylawyerfromhell1.undead = true;
            creepsentitylawyerfromhell1.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10D);
            world.addEntity(creepsentitylawyerfromhell1);
            LawyerFromHellEntity creepsentitylawyerfromhell3 = new LawyerFromHellEntity(world);
            creepsentitylawyerfromhell3.setLocationAndAngles(jailX + 2, jailY + 2, jailZ + i10, ((PlayerEntity)(entityplayersp)).rotationYaw, 0.0F);
            creepsentitylawyerfromhell3.undead = true;
            creepsentitylawyerfromhell1.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10D);
            world.addEntity(creepsentitylawyerfromhell3);
        }

        ((PlayerEntity) entityplayersp).setPosition(jailX + 7, jailY + 2, jailZ + 7);
        ((PlayerEntity) entityplayersp).heal(20F);

        if (rand.nextInt(5) == 0)
        {
            dropTreasure(world, jailX + 8, jailY + 2, jailZ + 8);
        }

        List list = world.getEntitiesWithinAABBExcludingEntity(entityplayersp, ((PlayerEntity)(entityplayersp)).getBoundingBox().expand(4D, 4D, 4D));

        for (int k10 = 0; k10 < list.size(); k10++)
        {
            Entity entity = (Entity)list.get(k10);

            if (entity != null && !(entity instanceof PlayerEntity))
            {
                entity.setDead();
            }
        }

        MoreCreepsReboot.instance.currentfine = 0;
        boolean flag = false;

        if (MoreCreepsReboot.instance.currentfine < 0)
        {
            MoreCreepsReboot.instance.currentfine = 0;
        }

        MoreCreepsReboot.instance.currentJailX = jailX;
        MoreCreepsReboot.instance.currentJailY = jailY;
        MoreCreepsReboot.instance.currentJailZ = jailZ;
        MoreCreepsReboot.instance.jailBuilt = true;
        return true;
    }

    public void dropTreasure(World world, int i, int j, int k)
    {
        int l = rand.nextInt(12);
        ItemStack itemstack = null;

        switch (l)
        {
            case 1:
                itemstack = new ItemStack(Items.WHEAT, rand.nextInt(2) + 1);
                break;

            case 2:
                itemstack = new ItemStack(Items.COOKIE, rand.nextInt(3) + 3);
                break;

            case 3:
                itemstack = new ItemStack(Items.PAPER, 1);
                break;

            case 4:
                itemstack = new ItemStack(ItemList.blorp_cola, rand.nextInt(3) + 1);
                break;

            case 5:
                itemstack = new ItemStack(Items.BREAD, 1);
                break;

            case 6:
                itemstack = new ItemStack(ItemList.evil_egg, rand.nextInt(2) + 1);
                break;

            case 7:
                itemstack = new ItemStack(Items.WATER_BUCKET, 1);
                break;

            case 8:
                itemstack = new ItemStack(Items.CAKE, 1);
                break;

            case 9:
                itemstack = new ItemStack(ItemList.money, rand.nextInt(5) + 5);
                break;

            case 10:
                itemstack = new ItemStack(ItemList.lolly, rand.nextInt(2) + 1);
                break;

            case 11:
                itemstack = new ItemStack(Items.CAKE, 1);
                break;

            case 12:
                itemstack = new ItemStack(ItemList.goo_donut, rand.nextInt(2) + 1);
                break;

            default:
                itemstack = new ItemStack(Items.COOKIE, rand.nextInt(2) + 1);
                break;
        }

        ItemEntity entityitem = new ItemEntity(world, i, j, k, itemstack);
        //entityitem.delayBeforeCanPickup = 10;
        if(!world.isRemote)
        world.addEntity(entityitem);
    }

    public void populateCell(int i, int j, World world, PlayerEntity entityplayer, boolean flag)
    {
        if (flag)
        {
            List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.getBoundingBox().expand(26D, 26D, 26D));

            for (int l = 0; l < list.size(); l++)
            {
                Entity entity = (Entity)list.get(l);

                if ((entity instanceof HotdogEntity) && ((HotdogEntity)entity).tamed)
                {
                    ((HotdogEntity)entity).wanderstate = 1;
                    ((HotdogEntity)entity).getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                    if (((HotdogEntity)entity).dogsize > 1.0F)
                    {
                        ((HotdogEntity)entity).dogsize = 1.0F;
                    }

                    ((HotdogEntity)entity).setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                    continue;
                }

                if (!(entity instanceof GuineaPigEntity) || !((GuineaPigEntity)entity).tamed)
                {
                    continue;
                }

                ((GuineaPigEntity)entity).wanderstate = 1;
                ((GuineaPigEntity)entity).getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);

                if (((GuineaPigEntity)entity).modelsize > 1.0F)
                {
                    ((GuineaPigEntity)entity).modelsize = 1.0F;
                }

                ((GuineaPigEntity)entity).setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
            }

            return;
        }

        int k = rand.nextInt(5);

        switch (k + 1)
        {
            case 1:
                RatManEntity creepsentityratman = new RatManEntity(world);
                creepsentityratman.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.addEntity(creepsentityratman);
                break;

            case 2:
                PrisonerEntity creepsentityprisoner = new PrisonerEntity(world);
                creepsentityprisoner.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.addEntity(creepsentityprisoner);
                break;

            case 3:
                CamelJockeyEntity creepsentitycameljockey = new CamelJockeyEntity(world);
                creepsentitycameljockey.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.addEntity(creepsentitycameljockey);
                break;

            case 4:
                MummyEntity creepsentitymummy = new MummyEntity(world);
                creepsentitymummy.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.addEntity(creepsentitymummy);
                break;

            case 5:
                PrisonerEntity creepsentityprisoner1 = new PrisonerEntity(world);
                creepsentityprisoner1.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.addEntity(creepsentityprisoner1);
                break;

            default:
                PrisonerEntity creepsentityprisoner2 = new PrisonerEntity(world);
                creepsentityprisoner2.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.addEntity(creepsentityprisoner2);
                break;
        }
    }

    public boolean suckMoney(PlayerEntity player)
    {
        Object obj = null;
        NonNullList<ItemStack> aitemstack = player.inventory.mainInventory;
        int i = 0;

        for (int j = 0; j < aitemstack.size(); j++)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack == null || itemstack.getItem() != ItemList.money)
            {
                continue;
            }

            if (!undead)
            {
            	world.playSound(playerentity, this.getPosition(), SoundsHandler.LAWYER_SUCK, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            i = rand.nextInt(itemstack.stackSize) + 1;

            if (i > itemstack.stackSize)
            {
                i = itemstack.stackSize;
            }

            if (i == itemstack.stackSize)
            {
                player.inventory.mainInventory[j] = null;
            }
            else
            {
                itemstack.stackSize -= i;
            }
        }

        if (i > 0 && !undead)
        {
            world.playSound(player, this.getPosition(), SoundsHandler.LAWYER_TAKE, SoundCategory.HOSTILE, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }

        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT nbttagcompound)
    {
        super.writeAdditional(nbttagcompound);
        nbttagcompound.putInt("LawyerState", lawyerstate);
        nbttagcompound.putInt("LawyerTimer", lawyertimer);
        nbttagcompound.putInt("JailX", jailX);
        nbttagcompound.putInt("JailY", jailY);
        nbttagcompound.putInt("JailZ", jailZ);
        nbttagcompound.putBoolean("Undead", undead);
        nbttagcompound.putFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT nbttagcompound)
    {
        super.readAdditional(nbttagcompound);
        lawyerstate = nbttagcompound.getInt("LawyerState");
        lawyertimer = nbttagcompound.getInt("LawyerTimer");
        jailX = nbttagcompound.getInt("JailX");
        jailY = nbttagcompound.getInt("JailY");
        jailZ = nbttagcompound.getInt("JailZ");
        undead = nbttagcompound.getBoolean("Undead");
        modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound()
    {
    	PlayerEntity playerentity = Minecraft.getInstance().player;
        SoundEvent s = getAmbientSound();

        if (s != null)
        {
            world.playSound(playerentity, this.getPosition(), s, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected SoundEvent getAmbientSound()
    {
        if (!undead)
        {
            return "morecreeps:lawyer";
        }
        else
        {
            return "morecreeps:lawyerundead";
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected SoundEvent getHurtSound(DamageSource damagesourceIn)
    {
        if (!undead)
        {
            return "morecreeps:lawyerhurt";
        }
        else
        {
            return "morecreeps:lawyerundeadhurt";
        }
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected SoundEvent getDeathSound()
    {
        if (!undead)
        {
            return "morecreeps:lawyerdeath";
        }
        else
        {
            return "morecreeps:lawyerundeaddeath";
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
        BlockState i1 = world.getBlockState(new BlockPos(i, j - 1, k));
        return i1 != Blocks.COBBLESTONE.getDefaultState() && i1 != Blocks.OAK_LOG.getDefaultState() && i1 != Blocks.SMOOTH_STONE_SLAB.getDefaultState()
        		&& i1 != Blocks.STONE_SLAB.getDefaultState() && i1 != Blocks.OAK_PLANKS.getDefaultState() && i1 != Blocks.WHITE_WOOL.getDefaultState()
//        		&& world.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        		&& world.canBlockSeeSky(new BlockPos(i, j, k)) && rand.nextInt(5) == 0; //&& l > 10;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk()
    {
        return 8;
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
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth()) + (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth() - (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F) + (double)((float)i * 0.5F)) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth(), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)((float)i * 0.5F) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth()) + (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F) + (double)((float)i * 0.5F)) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth() - (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)((float)i * 0.5F) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, ((posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth()) + (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F) + (double)((float)i * 0.5F)) - (double)getWidth(), d, d1, d2);
                world.addParticle(ParticleTypes.EXPLOSION, (posX + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)getWidth() - (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * getHeight()), (posZ + (double)(rand.nextFloat() * getWidth() * 2.0F)) - (double)((float)i * 0.5F) - (double)getWidth(), d, d1, d2);
            }
        }
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return defaultHeldItem;
    }

    public void onDeath(DamageSource damagesource)
    {
    	PlayerEntity playerentity = Minecraft.getInstance().player;
    	Entity entity = damagesource.getTrueSource();
        if (rand.nextInt(3) == 0 && !undead && (entity instanceof PlayerEntity))
        {
        	
            for (int i = 0; i < rand.nextInt(4) + 3; i++)
            {
                LawyerFromHellEntity creepsentitylawyerfromhell = new LawyerFromHellEntity(world);
                smoke();
                creepsentitylawyerfromhell.setLocationAndAngles(entity.posX + (double)(rand.nextInt(4) - rand.nextInt(4)), entity.posY - 1.5D, entity.posZ + (double)(rand.nextInt(4) - rand.nextInt(4)), rotationYaw, 0.0F);
                creepsentitylawyerfromhell.undead = true;
                creepsentitylawyerfromhell.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
                if(!world.isRemote)
                world.addEntity(creepsentitylawyerfromhell);
            }
        }
        else if (rand.nextInt(5) == 0 && !undead)
        {
            world.playSound(playerentity, this.getPosition(), SoundsHandler.LAWYER_BUM, SoundCategory.NEUTRAL, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            BumEntity creepsentitybum = new BumEntity(world);
            smoke();
            creepsentitybum.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.addEntity(creepsentitybum);
        }

        smoke();
        super.onDeath(damagesource);
    }

    public static boolean blockExists(World parWorld, int x, int y, int z) 
    {
    	BlockState state = parWorld.getBlockState(new BlockPos(x, y, z));
    	if (state != null)
    	return true;
    	else
    	return false;
    }
    
    static
    {
        defaultHeldItem = new ItemStack(Items.BONE, 1);
    }
}
