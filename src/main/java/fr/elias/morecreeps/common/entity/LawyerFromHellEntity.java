package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.MoreCreepsReboot;

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
        super(world);
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
        
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        //this.tasks.addTask(1, new CREEPSEntityLawyerFromHell.AIAttackEntity());
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new LawyerFromHellEntity.AIAttackEntity());
    }

    public void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40D);
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.44D);
    	this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1D);
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
        	PlayerEntity entityplayer = world.getClosestPlayerToEntity(this, 16D);

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
    public void onLivingUpdate()
    {
        texture = undead ? "morecreeps:textures/entity/lawyerfromhellundead.png" : "morecreeps:textures/entity/lawyerfromhell.png";
    	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(undead ? 0.24D : 0.44D);

        if (undead && defaultHeldItem == null)
        {
            defaultHeldItem = new ItemStack(Items.bone, 1);
        }

        super.onLivingUpdate();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
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

        super.onUpdate();
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        if (undead && isCollided)
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
        Entity entity = damagesource.getEntity();

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
                        world.playSoundAtEntity(this, "morecreeps:lawyermoneyhit", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        if(!world.isRemote)
                        dropItem(MoreCreepsReboot.money, 1);
                    }
                }

                if (rand.nextInt(5) == 0)
                {
                    for (int k = 0; k < rand.nextInt(3) + 1; k++)
                    {
                        MoreCreepsReboot.instance.currentfine += 10;
                        world.playSoundAtEntity(this, "morecreeps:lawyermoneyhit", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                        if(!world.isRemote)
                        dropItem(Items.paper, 1);
                    }
                }
            }
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    class AIAttackEntity extends EntityAIBase
    {
		@Override
		public boolean shouldExecute()
		{
			return LawyerFromHellEntity.this.findPlayerToAttack();
		}
		
		public void updateTask()
		{
			float f = LawyerFromHellEntity.this.getDistanceToEntity(getAttackTarget());
			if(f < 256F)
			{
				attackEntity(LawyerFromHellEntity.this.getAttackTarget(), f);
				LawyerFromHellEntity.this.getLookHelper().setLookPositionWithEntity(LawyerFromHellEntity.this.getAttackTarget(), 10.0F, 10.0F);
				LawyerFromHellEntity.this.getNavigator().clearPathEntity();
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

        if (this.getAttackTarget() instanceof EntityPlayer)
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
                float f2 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = ((d / (double)f2) * 0.5D * 0.40000001192092893D + motionX * 0.20000000298023224D) * (double)f1;
                motionZ = ((d1 / (double)f2) * 0.5D * 0.30000001192092896D + motionZ * 0.20000000298023224D) * (double)f1;
                motionY = 0.40000000596046448D;
            }
            else if ((double)f < 2.6000000000000001D)
            {
                if (rand.nextInt(50) == 0 && (entity instanceof EntityPlayer))
                {
                    suckMoney((EntityPlayer) entity);
                }

                if (undead)
                {
                    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D);
                }

                if ((entity instanceof EntityPlayer) && lawyerstate == 5 && !undead && rand.nextInt(10) == 0 && CREEPSConfig.jailActive && MoreCreepsReboot.instance.currentfine >= 2500)
                {
                    for (int i = 0; i < 21; i++)
                    {
                        EntityPlayer entityplayer = (EntityPlayer)entity;
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

                    buildJail((EntityPlayer) entity);
                }

                //super.attackEntity(entity, f);
            }
            else if ((double)f < 5D && (entity instanceof EntityPlayer) && rand.nextInt(25) == 0 && !undead && CREEPSConfig.jailActive && lawyerstate == 5 && MoreCreepsReboot.instance.currentfine >= 2500)
            {
                for (int j = 0; j < 21; j++)
                {
                    EntityPlayer entityplayer1 = (EntityPlayer)entity;
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
                buildJail((EntityPlayer) entity);
            }

            //super.attackEntity(entity, f);
        }
    }

    public boolean buildJail(EntityPlayer entityplayersp)
    {
        int i = rand.nextInt(200) - 100;

        if (rand.nextInt(2) == 0)
        {
            i *= -1;
        }

        jailX = (int)(((EntityPlayer)(entityplayersp)).posX + (double)i);
        jailY = rand.nextInt(20) + 25;
        jailZ = (int)(((EntityPlayer)(entityplayersp)).posZ + (double)i);
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
                            if (world.getBlockState(new BlockPos(jailX + k1, jailY + j, jailZ + l2)).getBlock() == Blocks.air)
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

            if (world.getBlockState(new BlockPos(jailX + 15 + 1, jailY + 20, jailZ + 7)).getBlock() == Blocks.flowing_water || world.getBlockState(new BlockPos(jailX + 15 + 1, jailY + 20, jailZ + 7)).getBlock() == Blocks.water)
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
                                world.setBlockState(new BlockPos(jailX + i3, jailY + k, jailZ + l1), Blocks.gravel.getDefaultState());
                                continue;
                            }

                            if (j4 < 15)
                            {
                                world.setBlockState(new BlockPos(jailX + i3, jailY + k, jailZ + l1), Blocks.mossy_cobblestone.getDefaultState());
                            }
                            else
                            {
                                world.setBlockState(new BlockPos(jailX + i3, jailY + k, jailZ + l1), Blocks.stone.getDefaultState());
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
                            world.setBlockState(new BlockPos(jailX + j3, jailY + l, jailZ + i2 + 1), Blocks.air.getDefaultState());
                            world.setBlockState(new BlockPos(jailX + j3, jailY + l, jailZ + i2 + 1), Blocks.air.getDefaultState());
                        }
                    }
                }

                for (int i1 = 0; i1 < 5; i1++)
                {
                    for (int j2 = 3; j2 < 11; j2++)
                    {
                        for (int k3 = 3; k3 < 11; k3++)
                        {
                            world.setBlockState(new BlockPos(jailX + k3, jailY + i1, jailZ + j2 + 1), Blocks.stone.getDefaultState());
                            world.setBlockState(new BlockPos(jailX + k3, jailY + i1, jailZ + j2 + 1), Blocks.stone.getDefaultState());
                        }
                    }
                }

                for (int j1 = 0; j1 < 5; j1++)
                {
                    for (int k2 = 5; k2 < 9; k2++)
                    {
                        for (int l3 = 5; l3 < 9; l3++)
                        {
                            world.setBlockState(new BlockPos(jailX + l3, jailY + j1, jailZ + k2 + 1), Blocks.air.getDefaultState());
                            world.setBlockState(new BlockPos(jailX + l3, jailY + j1, jailZ + k2 + 1), Blocks.air.getDefaultState());
                        }
                    }
                }

                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ + 4), Blocks.air.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ + 5), Blocks.iron_bars.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY + 1, jailZ + 7), Blocks.glass.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 4, jailY + 1, jailZ + 7), Blocks.glass.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 10, jailY + 1, jailZ + 7), Blocks.air.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ + 7), Blocks.iron_bars.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ + 11), Blocks.air.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 7, jailY + 1, jailZ + 10), Blocks.iron_bars.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 4, jailY, jailZ + 8), Blocks.air.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY, jailZ + 8), Blocks.air.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 4, jailY + 1, jailZ + 8), Blocks.air.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY + 1, jailZ + 8), Blocks.air.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY, jailZ + 8), Blocks.oak_door.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY, jailZ + 8), Blocks.oak_door.getDefaultState(), 0);//setBlockmetadatawithnotify
                world.setBlockState(new BlockPos(jailX + 3, jailY + 1, jailZ + 8), Blocks.oak_door.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 3, jailY + 1, jailZ + 8), Blocks.oak_door.getDefaultState(), 8);//wtf? 8 flag ?
                byte byte0 = 15;
                byte byte1 = 7;
                int i4;

                for (i4 = 80; world.getBlockState(new BlockPos(jailX + byte0, i4, jailZ + byte1)).getBlock() == Blocks.air || world.getBlockState(new BlockPos(jailX + byte0, i4, jailZ + byte1)).getBlock() == Blocks.leaves.getDefaultState() || world.getBlockState(new BlockPos(jailX + byte0, i4, jailZ + byte1)).getBlock() == Blocks.log.getDefaultState(); i4--) { }

                for (int k4 = 0; k4 < i4 - jailY; k4++)
                {
                    for (int i5 = 0; i5 < 2; i5++)
                    {
                        for (int i7 = 0; i7 < 2; i7++)
                        {
                            world.setBlockState(new BlockPos(jailX + i5 + byte0, jailY + k4, jailZ + byte1 + i7), Blocks.air.getDefaultState());
                        }
                    }
                }

                int l4 = 0;

                for (int j5 = 0; j5 < i4 - jailY; j5++)
                {
                    if (l4 == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + byte0, jailY + j5, jailZ + byte1), Blocks.stone_stairs.getDefaultState());
                        world.setBlockState(new BlockPos(jailX + byte0, jailY + j5, jailZ + byte1), Blocks.stone_stairs.getDefaultState(), 3);
                    }

                    if (l4 == 1)
                    {
                        world.setBlockState(new BlockPos(jailX + byte0 + 1, jailY + j5, jailZ + byte1), Blocks.stone_stairs.getDefaultState());
                        world.setBlockState(new BlockPos(jailX + byte0 + 1, jailY + j5, jailZ + byte1), Blocks.stone_stairs.getDefaultState(), 0);
                    }

                    if (l4 == 2)
                    {
                        world.setBlockState(new BlockPos(jailX + byte0 + 1, jailY + j5, jailZ + byte1 + 1), Blocks.stone_stairs.getDefaultState());
                        world.setBlockState(new BlockPos(jailX + byte0 + 1, jailY + j5, jailZ + byte1 + 1), Blocks.stone_stairs.getDefaultState(), 2);
                    }

                    if (l4 == 3)
                    {
                        world.setBlockState(new BlockPos(jailX + byte0, jailY + j5, jailZ + byte1 + 1), Blocks.stone_stairs.getDefaultState());
                        world.setBlockState(new BlockPos(jailX + byte0, jailY + j5, jailZ + byte1 + 1), Blocks.stone_stairs.getDefaultState(), 1);
                    }

                    if (l4++ == 3)
                    {
                        l4 = 0;
                    }
                }

                for (int k5 = 0; k5 < 3; k5++)
                {
                    world.setBlockState(new BlockPos(jailX + 13 + k5, jailY, jailZ + 7), Blocks.air.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 13 + k5, jailY + 1, jailZ + 7), Blocks.air.getDefaultState());
                }

                world.setBlockState(new BlockPos(jailX + 13, jailY, jailZ + byte1), Blocks.iron_door.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 13, jailY, jailZ + byte1), Blocks.iron_door.getDefaultState(), 0);
                world.setBlockState(new BlockPos(jailX + 13, jailY + 1, jailZ + byte1), Blocks.iron_door.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 13, jailY + 1, jailZ + byte1), Blocks.iron_door.getDefaultState(), 8);
                world.setBlockState(new BlockPos(jailX + 15, jailY, jailZ + byte1), Blocks.stone_stairs.getDefaultState());
                world.setBlockState(new BlockPos(jailX + 15, jailY, jailZ + byte1), Blocks.stone_stairs.getDefaultState(), 0);
                world.setBlockState(new BlockPos(jailX + 14, jailY + 2, jailZ + byte1), Blocks.air.getDefaultState());

                for (int l5 = 0; l5 < 32; l5++)
                {
                    for (int j7 = 6; j7 < 9; j7++)
                    {
                        for (int l7 = 0; l7 < 4; l7++)
                        {
                            world.setBlockToAir(new BlockPos(jailX + j7, jailY + l7, jailZ - l5 - 1));
                            world.setBlockToAir(new BlockPos(jailX + j7, jailY + l7, jailZ + l5 + 15));
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
                                world.setBlockToAir(new BlockPos(jailX + 10 + i8, jailY + l8, (jailZ - i6 * 7) + k7));
                                world.setBlockToAir(new BlockPos(jailX + 2 + i8, jailY + l8, (jailZ - i6 * 7) + k7));
                                world.setBlockToAir(new BlockPos(jailX + 10 + i8, jailY + l8, jailZ + i6 * 7 + 12 + k7));
                                world.setBlockToAir(new BlockPos(jailX + 2 + i8, jailY + l8, jailZ + i6 * 7 + 12 + k7));
                            }
                        }
                    }
                }

                world.setBlockToAir(new BlockPos(jailX + 7, jailY, jailZ));
                world.setBlockToAir(new BlockPos(jailX + 7, jailY + 1, jailZ));
                world.setBlockToAir(new BlockPos(jailX + 7, jailY, jailZ + 14));
                world.setBlockToAir(new BlockPos(jailX + 7, jailY + 1, jailZ + 14));

                for (int j6 = 0; j6 < 4; j6++)
                {
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ - j6 * 7 - 5 - 2), Blocks.iron_bars.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY, jailZ - j6 * 7 - 5), Blocks.oak_door.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY, jailZ - j6 * 7 - 5), Blocks.oak_door.getDefaultState(), 2);
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ - j6 * 7 - 5), Blocks.oak_door.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ - j6 * 7 - 5), Blocks.oak_door.getDefaultState(), 10);
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ - j6 * 7 - 5 - 2), Blocks.iron_bars.getDefaultState());
                    world.setBlockToAir(new BlockPos(jailX + 9, jailY, jailZ - j6 * 7 - 5));
                    world.setBlockToAir(new BlockPos(jailX + 9, jailY + 1, jailZ - j6 * 7 - 5));
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ - j6 * 7 - 5), Blocks.oak_door.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ - j6 * 7 - 5), Blocks.oak_door.getDefaultState(), 0);
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ - j6 * 7 - 5), Blocks.oak_door.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ - j6 * 7 - 5), Blocks.oak_door.getDefaultState(), 8);
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ + j6 * 7 + 19 + 2), Blocks.iron_bars.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY, jailZ + j6 * 7 + 19), Blocks.oak_door.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY, jailZ + j6 * 7 + 19), Blocks.oak_door.getDefaultState(), 2);
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ + j6 * 7 + 19), Blocks.oak_door.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 5, jailY + 1, jailZ + j6 * 7 + 19), Blocks.oak_door.getDefaultState(), 10);
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ + j6 * 7 + 19 + 2), Blocks.iron_bars.getDefaultState());
                    world.setBlockToAir(new BlockPos(jailX + 9, jailY, jailZ + j6 * 7 + 19));
                    world.setBlockToAir(new BlockPos(jailX + 9, jailY + 1, jailZ + j6 * 7 + 19));
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ + j6 * 7 + 19), Blocks.oak_door.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY, jailZ + j6 * 7 + 19), Blocks.oak_door.getDefaultState(), 0);
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ + j6 * 7 + 19), Blocks.oak_door.getDefaultState());
                    world.setBlockState(new BlockPos(jailX + 9, jailY + 1, jailZ + j6 * 7 + 19), Blocks.oak_door.getDefaultState(), 8);

                    if (rand.nextInt(1) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 12, jailY + 2, jailZ - j6 * 7 - 5), Blocks.torch.getDefaultState());
                    }

                    if (rand.nextInt(1) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 2, jailY + 2, jailZ - j6 * 7 - 5), Blocks.torch.getDefaultState());
                    }

                    if (rand.nextInt(1) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 12, jailY + 2, jailZ + j6 * 7 + 19), Blocks.torch.getDefaultState());
                    }

                    if (rand.nextInt(1) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 2, jailY + 2, jailZ + j6 * 7 + 19), Blocks.torch.getDefaultState());
                    }
                }

                for (int k6 = 0; k6 < 9; k6++)
                {
                    if (rand.nextInt(2) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 6, jailY + 2, jailZ - k6 * 4 - 2), Blocks.torch.getDefaultState());
                    }

                    if (rand.nextInt(2) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 8, jailY + 2, jailZ - k6 * 4 - 2), Blocks.torch.getDefaultState());
                    }

                    if (rand.nextInt(2) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 6, jailY + 2, jailZ + k6 * 4 + 18), Blocks.torch.getDefaultState());
                    }

                    if (rand.nextInt(2) == 0)
                    {
                        world.setBlockState(new BlockPos(jailX + 8, jailY + 2, jailZ + k6 * 4 + 18), Blocks.torch.getDefaultState());
                    }
                }
            }
            else
            {
                return false;
            }
        }

        world.setBlockState(new BlockPos(jailX + 12, jailY, jailZ + 13), Blocks.chest.getDefaultState());
        TileEntityChest tileentitychest = new TileEntityChest();
        world.setTileEntity(new BlockPos(jailX + 12, jailY, jailZ + 13), tileentitychest);
        world.setBlockState(new BlockPos(jailX + 12, jailY, jailZ + 1), Blocks.chest.getDefaultState());
        TileEntityChest tileentitychest1 = new TileEntityChest();
        world.setTileEntity(new BlockPos(jailX + 12, jailY, jailZ + 1), tileentitychest1);
        world.setBlockState(new BlockPos(jailX, jailY, jailZ + 13), Blocks.chest.getDefaultState());
        TileEntityChest tileentitychest2 = new TileEntityChest();
        world.setTileEntity(new BlockPos(jailX, jailY, jailZ + 13), tileentitychest2);
        world.setBlockState(new BlockPos(jailX, jailY, jailZ + 1), Blocks.chest.getDefaultState());
        TileEntityChest tileentitychest3 = new TileEntityChest();
        world.setTileEntity(new BlockPos(jailX, jailY, jailZ + 1), tileentitychest3);

        for (int l6 = 1; l6 < tileentitychest.getSizeInventory(); l6++)
        {
            tileentitychest.setInventorySlotContents(l6, null);
            tileentitychest1.setInventorySlotContents(l6, null);
            tileentitychest2.setInventorySlotContents(l6, null);
        }

        Object obj = null;
        ItemStack aitemstack[] = ((EntityPlayer)(entityplayersp)).inventory.mainInventory;

        for (int j8 = 0; j8 < aitemstack.length; j8++)
        {
            ItemStack itemstack = aitemstack[j8];

            if (itemstack != null)
            {
                tileentitychest.setInventorySlotContents(j8, itemstack);
                ((EntityPlayer)(entityplayersp)).inventory.mainInventory[j8] = null;
            }
        }

        for (int k8 = 1; k8 < tileentitychest3.getSizeInventory(); k8++)
        {
            int i9 = rand.nextInt(10);

            if (i9 == 1)
            {
                tileentitychest3.setInventorySlotContents(k8, new ItemStack(MoreCreepsReboot.bandaid, rand.nextInt(2) + 1, 0));
            }

            if (i9 == 2)
            {
                tileentitychest3.setInventorySlotContents(k8, new ItemStack(MoreCreepsReboot.money, rand.nextInt(24) + 1, 0));
            }
        }

        world.setBlockState(new BlockPos(jailX + 11, jailY, jailZ + 13), Blocks.mob_spawner.getDefaultState());
        TileEntityMobSpawner tileentitymobspawner = new TileEntityMobSpawner();
        world.setTileEntity(new BlockPos(jailX + 11, jailY, jailZ + 13), tileentitymobspawner);
        tileentitymobspawner.getSpawnerBaseLogic().setEntityName("Skeleton");
        tileentitychest1.setInventorySlotContents(rand.nextInt(5), new ItemStack(Items.stone_pickaxe, 1, 0));
        tileentitychest1.setInventorySlotContents(rand.nextInt(5) + 5, new ItemStack(Items.apple, 1, 0));
        tileentitychest2.setInventorySlotContents(rand.nextInt(5) + 5, new ItemStack(Blocks.torch, rand.nextInt(16), 0));
        tileentitychest2.setInventorySlotContents(rand.nextInt(5), new ItemStack(Items.apple, 1, 0));
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
            creepsentitylawyerfromhell.setLocationAndAngles(jailX + 8, jailY + 1, jailZ - 11 - 1, ((EntityPlayer)(entityplayersp)).rotationYaw, 0.0F);
            creepsentitylawyerfromhell.undead = true;
            creepsentitylawyerfromhell.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10D);
            world.spawnEntityInWorld(creepsentitylawyerfromhell);
            LawyerFromHellEntity creepsentitylawyerfromhell2 = new LawyerFromHellEntity(world);
            creepsentitylawyerfromhell2.setLocationAndAngles(jailX + 8, jailY + 1, jailZ + 11 + 15, ((EntityPlayer)(entityplayersp)).rotationYaw, 0.0F);
            creepsentitylawyerfromhell2.undead = true;
            creepsentitylawyerfromhell.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
            world.spawnEntityInWorld(creepsentitylawyerfromhell2);
        }

        for (int i10 = 2; i10 < rand.nextInt(3) + 3; i10++)
        {
            LawyerFromHellEntity creepsentitylawyerfromhell1 = new LawyerFromHellEntity(world);
            creepsentitylawyerfromhell1.setLocationAndAngles(jailX + i10, jailY + 2, jailZ + 2, ((EntityPlayer)(entityplayersp)).rotationYaw, 0.0F);
            creepsentitylawyerfromhell1.undead = true;
            creepsentitylawyerfromhell1.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10D);
            world.spawnEntityInWorld(creepsentitylawyerfromhell1);
            LawyerFromHellEntity creepsentitylawyerfromhell3 = new LawyerFromHellEntity(world);
            creepsentitylawyerfromhell3.setLocationAndAngles(jailX + 2, jailY + 2, jailZ + i10, ((EntityPlayer)(entityplayersp)).rotationYaw, 0.0F);
            creepsentitylawyerfromhell3.undead = true;
            creepsentitylawyerfromhell1.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10D);
            world.spawnEntityInWorld(creepsentitylawyerfromhell3);
        }

        ((EntityPlayer) entityplayersp).setPosition(jailX + 7, jailY + 2, jailZ + 7);
        ((EntityPlayer) entityplayersp).heal(20F);

        if (rand.nextInt(5) == 0)
        {
            dropTreasure(world, jailX + 8, jailY + 2, jailZ + 8);
        }

        List list = world.getEntitiesWithinAABBExcludingEntity(entityplayersp, ((EntityPlayer)(entityplayersp)).getEntityBoundingBox().expand(4D, 4D, 4D));

        for (int k10 = 0; k10 < list.size(); k10++)
        {
            Entity entity = (Entity)list.get(k10);

            if (entity != null && !(entity instanceof EntityPlayer))
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
                itemstack = new ItemStack(Items.wheat, rand.nextInt(2) + 1);
                break;

            case 2:
                itemstack = new ItemStack(Items.cookie, rand.nextInt(3) + 3);
                break;

            case 3:
                itemstack = new ItemStack(Items.paper, 1);
                break;

            case 4:
                itemstack = new ItemStack(MoreCreepsReboot.blorpcola, rand.nextInt(3) + 1);
                break;

            case 5:
                itemstack = new ItemStack(Items.bread, 1);
                break;

            case 6:
                itemstack = new ItemStack(MoreCreepsReboot.evilegg, rand.nextInt(2) + 1);
                break;

            case 7:
                itemstack = new ItemStack(Items.water_bucket, 1);
                break;

            case 8:
                itemstack = new ItemStack(Items.cake, 1);
                break;

            case 9:
                itemstack = new ItemStack(MoreCreepsReboot.money, rand.nextInt(5) + 5);
                break;

            case 10:
                itemstack = new ItemStack(MoreCreepsReboot.lolly, rand.nextInt(2) + 1);
                break;

            case 11:
                itemstack = new ItemStack(Items.cake, 1);
                break;

            case 12:
                itemstack = new ItemStack(MoreCreepsReboot.goodonut, rand.nextInt(2) + 1);
                break;

            default:
                itemstack = new ItemStack(Items.cookie, rand.nextInt(2) + 1);
                break;
        }

        EntityItem entityitem = new EntityItem(world, i, j, k, itemstack);
        //entityitem.delayBeforeCanPickup = 10;
        if(!world.isRemote)
        world.spawnEntityInWorld(entityitem);
    }

    public void populateCell(int i, int j, World world, EntityPlayer entityplayer, boolean flag)
    {
        if (flag)
        {
            List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.getEntityBoundingBox().expand(26D, 26D, 26D));

            for (int l = 0; l < list.size(); l++)
            {
                Entity entity = (Entity)list.get(l);

                if ((entity instanceof HotdogEntity) && ((HotdogEntity)entity).tamed)
                {
                    ((HotdogEntity)entity).wanderstate = 1;
                    ((HotdogEntity)entity).getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);

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
                ((GuineaPigEntity)entity).getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);

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
                world.spawnEntityInWorld(creepsentityratman);
                break;

            case 2:
                PrisonerEntity creepsentityprisoner = new PrisonerEntity(world);
                creepsentityprisoner.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentityprisoner);
                break;

            case 3:
                CamelJockeyEntity creepsentitycameljockey = new CamelJockeyEntity(world);
                creepsentitycameljockey.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentitycameljockey);
                break;

            case 4:
                MummyEntity creepsentitymummy = new MummyEntity(world);
                creepsentitymummy.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentitymummy);
                break;

            case 5:
                PrisonerEntity creepsentityprisoner1 = new PrisonerEntity(world);
                creepsentityprisoner1.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentityprisoner1);
                break;

            default:
                PrisonerEntity creepsentityprisoner2 = new PrisonerEntity(world);
                creepsentityprisoner2.setLocationAndAngles(i, jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentityprisoner2);
                break;
        }
    }

    public boolean suckMoney(EntityPlayer player)
    {
        Object obj = null;
        ItemStack aitemstack[] = player.inventory.mainInventory;
        int i = 0;

        for (int j = 0; j < aitemstack.length; j++)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack == null || itemstack.getItem() != MoreCreepsReboot.money)
            {
                continue;
            }

            if (!undead)
            {
                world.playSoundAtEntity(this, "morecreeps:lawyersuck", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
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
            world.playSoundAtEntity(this, "morecreeps:lawyertake", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        }

        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("LawyerState", lawyerstate);
        nbttagcompound.setInteger("LawyerTimer", lawyertimer);
        nbttagcompound.setInteger("JailX", jailX);
        nbttagcompound.setInteger("JailY", jailY);
        nbttagcompound.setInteger("JailZ", jailZ);
        nbttagcompound.setBoolean("Undead", undead);
        nbttagcompound.setFloat("ModelSize", modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        lawyerstate = nbttagcompound.getInteger("LawyerState");
        lawyertimer = nbttagcompound.getInteger("LawyerTimer");
        jailX = nbttagcompound.getInteger("JailX");
        jailY = nbttagcompound.getInteger("JailY");
        jailZ = nbttagcompound.getInteger("JailZ");
        undead = nbttagcompound.getBoolean("Undead");
        modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound()
    {
        String s = getLivingSound();

        if (s != null)
        {
            world.playSoundAtEntity(this, s, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
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
    protected String getHurtSound()
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
    protected String getDeathSound()
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
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(getEntityBoundingBox().minY);
        int k = MathHelper.floor_double(posZ);
        //int l = world.getFullBlockLightValue(i, j, k);
        IBlockState i1 = world.getBlockState(new BlockPos(i, j - 1, k));
        return i1 != Blocks.cobblestone.getDefaultState() && i1 != Blocks.log.getDefaultState() && i1 != Blocks.double_stone_slab.getDefaultState() && i1 != Blocks.stone_slab.getDefaultState() && i1 != Blocks.planks.getDefaultState() && i1 != Blocks.wool.getDefaultState() && world.getCollidingBoundingBoxes(this, getEntityBoundingBox()).size() == 0 && world.canSeeSky(new BlockPos(i, j, k)) && rand.nextInt(5) == 0; //&& l > 10;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
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
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2, new int[0]);
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2, new int[0]);
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)((float)i * 0.5F)) - (double)width, d, d1, d2, new int[0]);
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)((float)i * 0.5F) - (double)width, d, d1, d2, new int[0]);
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)((float)i * 0.5F)) - (double)width, d, d1, d2, new int[0]);
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)((float)i * 0.5F) - (double)width, d, d1, d2, new int[0]);
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, ((posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width) + (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F) + (double)((float)i * 0.5F)) - (double)width, d, d1, d2, new int[0]);
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - (double)((float)i * 0.5F), posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)((float)i * 0.5F) - (double)width, d, d1, d2, new int[0]);
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
    	Entity entity = damagesource.getEntity();
        if (rand.nextInt(3) == 0 && !undead && (entity instanceof EntityPlayer))
        {
        	
            for (int i = 0; i < rand.nextInt(4) + 3; i++)
            {
                LawyerFromHellEntity creepsentitylawyerfromhell = new LawyerFromHellEntity(world);
                smoke();
                creepsentitylawyerfromhell.setLocationAndAngles(entity.posX + (double)(rand.nextInt(4) - rand.nextInt(4)), entity.posY - 1.5D, entity.posZ + (double)(rand.nextInt(4) - rand.nextInt(4)), rotationYaw, 0.0F);
                creepsentitylawyerfromhell.undead = true;
                creepsentitylawyerfromhell.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20D);
                if(!world.isRemote)
                world.spawnEntityInWorld(creepsentitylawyerfromhell);
            }
        }
        else if (rand.nextInt(5) == 0 && !undead)
        {
            world.playSoundAtEntity(this, "morecreeps:lawyerbum", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            BumEntity creepsentitybum = new BumEntity(world);
            smoke();
            creepsentitybum.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            world.spawnEntityInWorld(creepsentitybum);
        }

        smoke();
        super.onDeath(damagesource);
    }

    public static boolean blockExists(World parWorld, int x, int y, int z) 
    {
    	IBlockState state = parWorld.getBlockState(new BlockPos(x, y, z));
    	if (state != null)
    	return true;
    	else
    	return false;
    }
    
    static
    {
        defaultHeldItem = new ItemStack(Items.bone, 1);
    }
}
