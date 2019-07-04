package fr.elias.morecreeps.common.lists;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import com.mojang.datafixers.types.Type;

import fr.elias.morecreeps.common.Reference;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("rawtypes")
public class EntityTypes extends EntityType
{

	public EntityTypes(IFactory factoryIn, EntityClassification classificationIn, boolean serializableIn,
			boolean summonableIn, boolean immuneToFireIn, Type dataFixerType, EntitySize sizeIn,
			Predicate velocityUpdateSupplier, ToIntFunction trackingRangeSupplier, ToIntFunction updateIntervalSupplier,
			BiFunction customClientFactory) {
		super(factoryIn, classificationIn, serializableIn, summonableIn, immuneToFireIn, dataFixerType, sizeIn,
				velocityUpdateSupplier, trackingRangeSupplier, updateIntervalSupplier, customClientFactory);
		// TODO Auto-generated constructor stub
	}

	public static final EntityType ARMY_GUY_ARM, ARMY_GUY;
	public static final EntityType ATOM;
	public static final EntityType BABY_MUMMY;
	public static final EntityType BASE;
	public static final EntityType BIG_BABY;
	public static final EntityType BLACK_SOUL;
	public static final EntityType BLORP;
	public static final EntityType BUBBLE_SCUM;
	public static final EntityType BULLET;
	public static final EntityType BUM;
	public static final EntityType CAMEL;
	public static final EntityType CAMEL_JOCKEY;
	public static final EntityType CASTLE_CRITTER;
	public static final EntityType CASTLE_GUARD;
	public static final EntityType CASTLE_KING;
	public static final EntityType CAVEMAN;
	public static final EntityType DESERT_LIZARD;
	public static final EntityType DESERT_LIZARD_FIREBALL;
	public static final EntityType DIG_BUG;
	public static final EntityType EVIL_CHICKEN;
	public static final EntityType EVIL_CREATURE;
	public static final EntityType EVIL_EGG;
	public static final EntityType EVIL_LIGHT;
	public static final EntityType EVIL_PIG;
	public static final EntityType EVIL_SCIENTIST;
	public static final EntityType EVIL_SNOWMAN;
	public static final EntityType FLOOB;
	public static final EntityType FLOOB_SHIP;
	public static final EntityType FOAM;
	public static final EntityType FRISBEE;
	public static final EntityType GOO_DONUT;
	public static final EntityType GROW;
	public static final EntityType GUINEA_PIG;
	public static final EntityType HIPPO;
	public static final EntityType HORSE_HEAD;
	public static final EntityType HOTDOG;
	public static final EntityType HUNCHBACK;
	public static final EntityType HUNCHBACK_SKELETON;
	public static final EntityType INVISIBLE_MAN;
	public static final EntityType KID;
	public static final EntityType LAWYER;
	public static final EntityType LETTER_G;
	public static final EntityType LOLLIMAN;
	public static final EntityType MAN_DOG;
	public static final EntityType MONEY;
	public static final EntityType MUMMY;
	public static final EntityType NON_SWIMMER;
	public static final EntityType PREACHER;
	public static final EntityType PRISONER;
	public static final EntityType PYRAMID_GUARDIAN;
	public static final EntityType RATMAN;
	public static final EntityType RAY_ENTITY;
	public static final EntityType ROBOT_TED;
	public static final EntityType ROBOT_TODD;
	public static final EntityType ROCKET;
	public static final EntityType ROCKET_GIRAFFE;
	public static final EntityType ROCK_MONSTER;
	public static final EntityType SCHLUMP;
	public static final EntityType SHRINK;
	public static final EntityType SNEAKY_SAL;
	public static final EntityType SNOW_DEVIL;
	public static final EntityType SQUIMP;
	public static final EntityType THEIF;
	public static final EntityType TOMBSTONE;
	public static final EntityType TOWEL;
	public static final EntityType TROPHY;
	public static final EntityType ZEBRA;

	private static EntityType registerSound(String name)
	{
		ResourceLocation location = new ResourceLocation(Reference.MODID, name);
		EntityType event = new Entity(location);
		event.setRegistryName(name);
		ForgeRegistries.ENTITIES.register(event);
		return event;
	}
}
