package fr.elias.morecreeps.common.lists;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.registry.Registry;

@SuppressWarnings({ "deprecation", "unused" })
public class ParticleList extends ParticleTypes
{
	public static BasicParticleType CREEPS_BUBBLE = register("creeps_bubble", false);
	public static BasicParticleType CREEPS_WHITE = register("creeps_white", false);
	public static BasicParticleType CREEPS_RED = register("creeps_red", false);
	public static BasicParticleType CREEPS_BLACK = register("creeps_black", false);
	public static BasicParticleType CREEPS_YELLOW = register("creeps_yellow", false);
	public static BasicParticleType CREEPS_BLUE = register("creeps_blue", false);
	public static BasicParticleType CREEPS_SHRINK = register("creeps_shrink", false);
	public static BasicParticleType CREEPS_BARF = register("creeps_barf", false);

private static BasicParticleType register(String key, boolean alwaysShow)
   {
	      return (BasicParticleType)Registry.<ParticleType<? extends IParticleData>>register(Registry.PARTICLE_TYPE, key, new BasicParticleType(alwaysShow));
   }

private static <T extends IParticleData> ParticleType<T> register(String key, IParticleData.IDeserializer<T> deserializer)
   {
	      return Registry.register(Registry.PARTICLE_TYPE, key, new ParticleType<>(false, deserializer));
   }
}
