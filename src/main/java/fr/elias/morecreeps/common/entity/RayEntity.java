package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class RayEntity extends ThrowableEntity
{
    protected int damage;

    protected boolean playerFire;

    public RayEntity(World world)
    {
        super(null, world);
		playerFire = false;
		damage = 4;
    }

    public void tick()
    {
		World world = Minecraft.getInstance().world;
    	super.tick();
    	if(onGround)
    	{
    		if(rand.nextInt(3) == 0)
    		{
    			if(!world.isRemote)
    			{
    				if(CREEPSConfig.rayGunFire){
    					world.setBlockState(getPosition(), Blocks.FIRE.getDefaultState());
    				}
    				else{
        				world.setBlockState(getPosition().down(), Blocks.AIR.getDefaultState());
    				}
    			}
    		}
    	}
    	if(!world.isRemote)
    	{
    		checkNearBlock(Blocks.ICE, Blocks.WATER, getPosition());
    		checkNearBlock(Blocks.ICE, Blocks.WATER, getPosition().east());
    		checkNearBlock(Blocks.ICE, Blocks.WATER, getPosition().west());
    		checkNearBlock(Blocks.ICE, Blocks.WATER, getPosition().north());
    		checkNearBlock(Blocks.ICE, Blocks.WATER, getPosition().south());
    		
    		if(rand.nextBoolean())
    		{
        		checkNearBlock2(Blocks.AIR, Blocks.FIRE, getPosition());
        		checkNearBlock2(Blocks.AIR, Blocks.FIRE, getPosition().east());
        		checkNearBlock2(Blocks.AIR, Blocks.FIRE, getPosition().west());
        		checkNearBlock2(Blocks.AIR, Blocks.FIRE, getPosition().north());
        		checkNearBlock2(Blocks.AIR, Blocks.FIRE, getPosition().south());
    		}
    	}
    }
    
    public void checkNearBlock(Block blockToReplace, Block theNewBlock, BlockPos bp)
    {
    	if(world.getBlockState(bp).getBlock() == blockToReplace && CREEPSConfig.rayGunMelt)
    	{
    		world.setBlockState(bp, theNewBlock.getDefaultState());
    	}
    }
    
    public void checkNearBlock2(Block blockToReplace, Block theNewBlock, BlockPos bp)
    {
    	if(world.getBlockState(bp).getBlock() == blockToReplace)
    	{
    		if(CREEPSConfig.rayGunFire)
    		{
        		world.setBlockState(bp, theNewBlock.getDefaultState());
    		}else{
    			world.setBlockState(getPosition(), Blocks.AIR.getDefaultState());
    		}
    	}
    }
    
    public void blast(World world)
    {
    	if(world.isRemote)
    	{
            for (int i = 0; i < 8; i++)
            {
                byte byte0 = 7;

                if (rand.nextInt(4) == 0)
                {
                    byte0 = 11;
                }
                MoreCreepsReboot.proxy.smokeRay(world, this, byte0);
            }
    	}
    }
	
    protected float getGravityVelocity()
    {
        return 0.0F;
    }

	@Override
	protected void onImpact(RayTraceResult result)
	{
		PlayerEntity player = Minecraft.getInstance().player;
		if (result.hitInfo != null)
		{
				if (result.hitInfo instanceof PlayerEntity)
				{
					int k = damage;

					if (world.getDifficulty() == Difficulty.PEACEFUL)
					{
						k = 0;
					}

					if (world.getDifficulty() == Difficulty.EASY)
					{
						k = k / 3 + 1;
					}

					if (world.getDifficulty() == Difficulty.HARD)
					{
						k = (k * 3) / 2;
					}
				}

				if ((result.hitInfo instanceof LivingEntity) && playerFire || !(result.hitInfo instanceof FloobEntity) || playerFire)
				{
					result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
				}
				
				if(!world.isRemote)
				remove();
		}
		if(world.isRemote)
		blast(world);
		
		world.playSound(player, this.getPosition(), SoundsHandler.RAY_GUN, SoundCategory.NEUTRAL, 0.2F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
	}

	@Override
	protected void registerData()
	{}
}
