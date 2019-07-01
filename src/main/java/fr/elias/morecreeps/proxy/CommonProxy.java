package fr.elias.morecreeps.proxy;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.entity.AtomEntity;
import fr.elias.morecreeps.common.entity.CavemanEntity;
import fr.elias.morecreeps.common.entity.DigBugEntity;
import fr.elias.morecreeps.common.entity.HorseHeadEntity;
import fr.elias.morecreeps.common.entity.RayEntity;
import fr.elias.morecreeps.common.entity.TrophyEntity;

public class CommonProxy
{
	public void render(){}
	public void renderModelItem(){}
	public void smoke(World world, PlayerEntity player, Random random){}
	public void smoke2(World world, Entity entity, Random random){}
	public void smoke3(World world, Entity entity, Random random){}
	public void blood(World world, double posx, double posy, double posz, boolean realBlood){}
	public void dirt(World world, PlayerEntity player, Random random, int l, int i1, int k){}
	public void foam(World world, PlayerEntity player){}
	public void foam2(World world, AtomEntity atom){}
	public void confettiA(LivingEntity player, World world)
	{
		if(!world.isRemote)
		{
	        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
	        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
	        TrophyEntity creepsentitytrophy = new TrophyEntity(world);
	        creepsentitytrophy.setLocationAndAngles(player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
	        world.addEntity(creepsentitytrophy);
		}
	}
	public void shrinkBlast(World world, Entity entity, Random rand){}
	public void shrinkSmoke(World world, Entity entity){}
	public void rocketGoBoom(World world, Entity entity, Random rand) {}
	public void rocketSmoke(World world, Entity entity, BasicParticleType CREEPS_WHITE){}
	public void robotTedSmoke(World world, double posX, double posY, double posZ, int floattimer, float modelspeed){}
	public void confettiB(World world, TrophyEntity trophy){} // for the confetti particles
	public void barf(World world, PlayerEntity player){}
	public void bubble(World world, LivingEntity entity){}
	public void addChatMessage(String s){}
	public void playSoundEffectAtPlayer(World world, String s, float volume, float pitch){}
	public void pee(World world, double posX, double posY, double posZ, float rotationYaw, float modelsize){}
	public void foam3(World world, CavemanEntity player, int i, int j, int k){}
	public void dirtDigBug(World world, DigBugEntity dbug, Random random, int k2){}
	public void bubbleDoghouse(World world, LivingEntity entity){}
	public void growParticle(World world, Entity entity){}
	public void shrinkParticle(World world, Entity entity){}
	public void smokeHorseHead(World world, HorseHeadEntity horsehead, Random rand) {}
	public boolean isJumpKeyDown(){return false;}
	public void smokeRay(World worldObj, RayEntity creepsEntityRay, byte byte0) {}
}
