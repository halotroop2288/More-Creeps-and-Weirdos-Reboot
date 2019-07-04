package fr.elias.morecreeps.common.util.handlers;

import fr.elias.morecreeps.common.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundsHandler extends SoundEvents
{
	public static SoundEvent
	ACHIEVEMENT,
	ARMY, ARMY_ARM, ARMY_BOTH_LEGS, ARMY_DEATH, ARMY_HEAD, ARMY_HURT, ARMY_LEG, ARMY_GUY, ARMY_HELMET,
	ATOM, ATOM_BLOW, ATOM_DEATH, ATOM_SUCK, ATOM_HURT,
	BABY_JAR_EMPTY, BAND_AID, BARF, BASEBELL,
	BABY_MUMMY, BABY_MUMMY_DEATH, BABY_MUMMY_HURT, BABY_SHRINK,
	BATTLE_CASTLE,
	BELL_BASE,
	BIG_BABY, BIG_BABY_HURT, BABY_TAKE_HOME,
	BLACK_SOUL,	BLACK_SOUL_DEATH, BLACK_SOUL_HURT,
	BLORP, BLORP_BOUNCE, BLORP_DEATH, BLORP_EAT, BLORP_GROW, BLORP_HURT,
	BLORP_COLA, BOMB_SET, BOOMBOX,
	BUBBLE_SCUM, BUBBLE_SCUM_DEATH, BUBBLE_SCUM_HURT, BUBBLE_SCUM_PICK_UP, BUBBLE_SCUM_PUT_DOWN,
	BULLET,	BULLET_HIT, DIODE,
	BUM, BUM_DEATH, BUM_LEAVE_ME_ALONE, BUM_SUCKER, BUM_THANKS, BUM_THANK_YOU, BUM_DONT_WANT, BUM_HURT, BUM_PEE, BUM_PEE_DONE, BUM_LIVING_PEE,
	CAMEL, CAMEL_DEATH, CAMEL_HURT, CAMEL_SPIT, CAMEL_SPITS, CAMEL_SPLASH, // ????
	CAMEL_JOCKEY_GET, CAMEL_JOCKEY_HURT, CAMEL_JOCKEY, CAMEL_JOCKEY_DEATH,
	CASTLE_CRITTER, CASTLE_CRITTER_HURT, CASTLE_CRITTER_DEATH,
	CASTLE_GUARD, CASTLE_GUARD_DEATH, CASTLE_GUARD_HURT, CASTLE_GUARD_MAD,
	CASTLE_KING, CASTLE_KING_DEATH, CASTLE_KING_HURT,
	CAVE_DRUMS,
	CAVEMAN_BUILD,	CAVEMAN_DEATH, CAVEMAN_EVIL, CAVEMAN_FREE, CAVEMAN_FROZEN, CAVEMAN_HURT, CAVEMAN_ICE, CAVEMAN_ICE_FINISHED,
	CAVEWOMAN_DEATH, CAVEWOMAN_FREE, CAVEWOMAN_FROZEN, CAVEWOMAN_HURT,
	CHEW,
	CHICKEN_EAT,
	DESERT_LIZARD, DESERT_LIZARD_DEATH,	DESERT_LIZARD_FIREBALL, DESERT_LIZARD_HURT,
	DIG_BUG_DIG, DIG_BUG_CALL, DIG_BUG_EAT, DIG_BUG_FULL, DIG_BUG_HURT, DIG_BUG_DEATH,
	DISCO_MOLE, DISCO_MOLE_DEATH, DISCO_MOLE_HURT,
	ARMY_GEM, EARTH_GEM, FIRE_GEM, HEALING_GEM, HORSE_HEAD_GEM, MINING_GEM, MINING_GEM_BAD, SKY_GEM, SKY_GEM_DOWN, SKY_GEM_UP,
	EVIL_CREATURE, EVIL_CREATURE_DEATH, EVIL_CREATURE_HURT, EVIL_CREATURE_JUMP,
	EVIL_EGG_BIRTH, EVIL_EGG_CLUCK,
	EVIL_EXPLOSION, EVIL_HURT, EVIL_LAUGH, EVIL_LIGHT,
	EXTINGUISHER,
	FEATURE_NOT_AVAILABLE,
	FLOOB, FLOOB_DEATH, FLOOB_HURT, FLOOB_SHIP, FLOOB_SHIP_CLANG, FLOOB_SHIP_EXPLODE, FLOOB_SHIP_SPAWN,
	G, G_HURT, G_DEATH,
	GEM_SWORD,
	GIRAFFE, GIRAFFE_CHEW, GIRAFFE_GALLOP, GIRAFFE_SPLASH, GIRAFFE_TAMED, GIRAFFE_HURT, GIRAFFE_DEATH,
	GOO_GOAT, GOO_GOAT_DEATH, GOO_GOAT_HURT, GOO_GOAT_STRETCH,
	GREGG, GREGG_DEATH, GREGG_HURT,
	GROW_RAY, RAY_RAY,
	GUINEA_PIG, GUINEA_PIG_ANGRY, GUINEA_PIG_ARMOUR, GUINEA_PIG_DEATH, GUINEA_PIG_EAT, GUINEA_PIG_FULL, GUINEA_PIG_LEVEL_UP, GUINEA_PIG_MOUNT, GUINEA_PIG_UNMOUNT, GUINEA_PIG_HURT,
	GUINEA_PIG_LEVEL_5, GUINEA_PIG_LEVEL_10, GUINEA_PIG_LEVEL_15, GUINEA_PIG_LEVEL_20,
	GUINEA_PIG_CRITICAL_HIT, GUINEA_PIG_HOTEL, GUINEA_PIG_NO_WHEAT, GUINEA_PIG_SPEED_DOWN, GUINEA_PIG_SPEED_UP, GUINEA_PIG_TRAIN,
	GUM_BLOW, HARD_CANDY,
	HIPPO, HIPPO_DEATH, HIPPO_HURT,
	HORSE_HEAD,	HORSE_HEAD_BLAST_OFF,
	HOT_DOG, HOT_DOG_LEVEL_5, HOT_DOG_LEVEL_10, HOT_DOG_LEVEL_15, HOT_DOG_LEVEL_20, HOT_DOG_LEVEL_25, HOT_DOG_ATTACK, HOT_DOG_DEATH, HOT_DOG_EAT, HOT_DOG_HEAVEN, HOT_DOG_KILL, HOT_DOG_NO_BONES, HOT_DOG_PICK_UP, HOT_DOG_PUT_DOWN, HOT_DOG_REDSTONE, HOT_DOG_TAMED, HOT_DOG_TRAIN, HOT_DOG_WHOOSH, HOT_DOG_HURT,
	HUNCHBACK_ARMY, HUNCHBACK_THANKS, HUNCHBACK_CAKE, HUNCHBACK_DEATH, HUNCHBACK_HURT, HUNCHBACK_QUIET, HUNCHBACK_THANK_YOU,
	INVISIBLE_MAN, INVISIBLE_MAN_ANGRY, INVISIBLE_MAN_DEATH, INVISIBLE_MAN_FORGET_IT, INVISIBLE_MAN_HURT,
	KID, KID_COLD, KID_COOKIES, KID_DEATH, KID_DOWN, KID_DONT_PICK_UP, KID_FIND, KID_HURT, KID_RIDE, KID_UP,
	LAWYER, LAWYER_BUM, LAWYER_BUSTED, LAWYER_DEATH, LAWYER_HURT, LAWYER_MONEY_HIT, LAWYER_SUCK, LAWYER_TAKE, LAWYER_UNDEAD, LAWYER_UNDEAD_DEATH, LAWYER_UNDEAD_HURT,
	LICK, LOLLY, MEDICINE, MOBILE,
	LOLLIMAN, LOLLIMAN_DEATH, LOLLIMAN_EXPLODE, LOLLIMAN_HURT, LOLLIMAN_TAKE_OFF,
	MAN_DOG, MAN_DOG_DEATH, MAN_DOG_HURT, MAN_DOG_TAMED,
	MUMMY, MUMMY_DEATH, MUMMY_HURT,
	NON_SWIMMER, NON_SWIMMER_DEATH, NON_SWIMMER_HURT, NON_SWIMMER_REWARD, NON_SWIMMER_SORRY,
	OLD, OLD_DEATH, OLD_HURT, OLD_PICK_UP, OLD_PUT_DOWN,
	PASTA_EAT, PET_RADIO, PET_RADIO_DROP, PET_RADIO_PICK_UP,
	// 72 OF THESE ARE JUST FOR FUCKING PONIES AND PONY-GIRLS
	PONY, PONY_LEVEL_5, PONY_LEVEL_10, PONY_LEVEL_15, PONY_LEVEL_20, PONY_ATTACK, PONY_CLOUD, PONY_CLOUD_BLOW, PONY_CLOUD_KILL, PONY_CLOUD_LIVING, PONY_DEATH, PONY_DRINK, PONY_FULL, PONY_KISS, PONY_MATING, PONY_NO_EXPERIENCE, PONY_POP_OFF, PONY_PRIVACY, PONY_ROMANCE_OVER, PONY_SICK, PONY_TRAIN, PONY_TROT, PONY_GROWN_UP,
	PONY_GIRL_BUY, PONY_GIRL_CELL, PONY_GIRL_DEATH, PONY_GIRL_HURT, PONY_GIRL_MONEY, PONY_GIRL_NO_THANKS, PONY_GIRL_WAIT_HERE,
	PREACHER, PREACHER_BURN, PREACHER_DEATH, PREACHER_RAISE, PREACHER_HURT,
	PRISONER, PRISONER_DEATH, PRISONER_HURT, PRISONER_SORRY, PRISONER_REWARD,
	PYRAMID, PYRAMID_CURSE, PYRAMID_DEATH, PYRAMID_DISCOVERED, PYRAMID_HURT,
	RAT_MAN, RAT_MAN_HURT, RAT_MAN_SCRATCH,
	RAY_GUN,
	ROBOT_HURT,
	ROCKET_EXPLODE, ROCKET_FIRE,
	ROCK_MONSTER, ROCK_MONSTER_DEATH, ROCK_MONSTER_HURT,
	SAL_DEATH, SAL_GREETING, SAL_HURT, SAL_NOBODY_SHRINKS, SAL_NO_MONEY, SAL_RATS, SAL_SALE,
	SCHLUMP, SCHLUMP_BIG, SCHLUMP_BRIGHT, SCHLUMP_DEATH, SCHLUMP_INDOORS, SCHLUMP_OK, SCHLUMP_OVERLOAD, SCHLUMP_REWARD, SCHLUMP_ROOM, SCHLUMP_HURT, SCHLUMP_SUCKS,
	SHRINK_KILL, SHRINK_RAY,
	SLOT_MACHINE_SPIN,
	SNOW_DEVIL, SNOW_DEVIL_DEATH, SNOW_DEVIL_HURT, SNOW_DEVIL_TAMED,
	SNOW_MAN, SNOW_MAN_BOUNCE, SNOW_MAN_DEATH, SNOW_MAN_HURT,
	SPARK,
	SUPER_DOG_APPLE, SUPER_DOG_NAME,
	TED_DEATH, TED_INSULT,
	THIEF, THIEF_DEATH, THIEF_FIND_PLAYER, THIEF_STEAL, THIEF_HURT,
	TODD_DEATH, TODD_INSULT,
	TOMBSTONE,
	TOWEL_FUN, TROPHY_SMASH, WELCOME;
	
	public static void registerSounds()
	{
		// FUCK MY LIFE 1,090 items to register!
		// TODO 100+ lines of this shit.
		
		// Army Man
		ARMY = registerSound("mob.army.idle");
		ARMY_ARM = registerSound("mob.army.arm");
		ARMY_BOTH_LEGS = registerSound("mob.army.bothlegs");
		ARMY_DEATH = registerSound("mob.army.death");
		ARMY_GUY = registerSound("mob.army.guy");
		ARMY_HELMET = registerSound("mob.army.helmet");
		ARMY_HEAD = registerSound("mob.army.head");
		
		// Atom
		ATOM = registerSound("mob.atom.idle");
		ATOM_DEATH = registerSound("mob.atom.death");
		ATOM_BLOW = registerSound("mob.atom.blow");
		ATOM_SUCK = registerSound("mob.atom.suck");
		ATOM_HURT = registerSound("mob.atom.hurt");

		// Big Baby
		BABY_SHRINK = registerSound("mob.baby.shrink");
		BIG_BABY = registerSound("mob.baby.idle");
		BIG_BABY_HURT = registerSound("mob.baby.hurt");
		BABY_TAKE_HOME = registerSound("mob.baby.take_home");
		
		// Black Soul
		BLACK_SOUL = registerSound("mob.black_soul.idle");
		BLACK_SOUL_DEATH = registerSound("mob.black_soul.death");
		BLACK_SOUL_HURT = registerSound("mob.black_soul.hurt");
		
		// Blorp
		BLORP = registerSound("mob.blorp.idle");
		BLORP_EAT = registerSound("mob.blorp.eat");
		BLORP_GROW = registerSound("mob.blorp.grow");
		BLORP_DEATH = registerSound("mob.blorp.death");
		BLORP_BOUNCE = registerSound("mob.blorp.bounce");
		
		// Bubble Scum
		BUBBLE_SCUM = registerSound("mob.bubble_scum.idle");
		BUBBLE_SCUM_HURT = registerSound("mob.bubble_scum.hurt");
		BUBBLE_SCUM_DEATH = registerSound("mob.bubble_scum.death");
		BUBBLE_SCUM_PUT_DOWN = registerSound("mob.bubble_scum.put_down");
		BUBBLE_SCUM_PICK_UP = registerSound("mob.bubble_scum.pick_up");
		
		// Bum
		BUM = registerSound("mob.bum.idle");
		BUM_DEATH = registerSound("mob.bum.death");
		BUM_HURT = registerSound("mob.bum.hurt");
		BUM_DONT_WANT = registerSound("mob.bum.dont_want");
		BUM_LIVING_PEE = registerSound("mob.bum.living_pee");
		BUM_PEE = registerSound("mob.bum.pee");
		BUM_PEE_DONE = registerSound("mob.bum.pee_done");
		BUM_LEAVE_ME_ALONE = registerSound("mob.bum.leave_me_alone");
		BUM_SUCKER = registerSound("mob.bum.sucker");
		BUM_THANKS = registerSound("mob.bum.thanks");
		BUM_THANK_YOU = registerSound("mob.bum.thank_you");
		
		// Camel
		CAMEL = registerSound("mob.camel.idle");
		CAMEL_HURT = registerSound("mob.camel.hurt");
		CAMEL_SPIT = registerSound("mob.camel.spit");
		CAMEL_DEATH = registerSound("mob.camel.death");
		CAMEL_SPLASH = registerSound("mob.camel.splash");
		
		// Camel Jockey
		CAMEL_JOCKEY = registerSound("mob.camel.jockey.idle");
		CAMEL_JOCKEY_DEATH = registerSound("mob.camel.jockey.death");
		CAMEL_JOCKEY_GET = registerSound("mob.camel.jockey.get");
		CAMEL_JOCKEY_HURT = registerSound("mob.camel.jockey.hurt");
		
		// Castle Critter
		CASTLE_CRITTER = registerSound("mob.castle_critter.idle");
		CASTLE_CRITTER_HURT = registerSound("mob.castle_critter.hurt");
		CASTLE_CRITTER_DEATH = registerSound("mob.castle_critter.death");
		
		// Castle Guard
		CASTLE_GUARD = registerSound("mob.castle_guard.idle");
		CASTLE_GUARD_HURT = registerSound("mob.castle_guard.hurt");
		CASTLE_GUARD_DEATH = registerSound("mob.castle_guard.death");
		CASTLE_GUARD_MAD = registerSound("mob.castle_guard.mad");
		
		// Caveman
		CAVEMAN_DEATH = registerSound("mob.caveman.death");
		CAVEMAN_BUILD = registerSound("mob.caveman.build");
		CAVEMAN_EVIL = registerSound("mob.caveman.evil");
		CAVEMAN_FROZEN = registerSound("mob.caveman.frozen");
		CAVEMAN_FREE = registerSound("mob.caveman.free");
		CAVEMAN_ICE = registerSound("mob.caveman.ice");
		CAVEMAN_ICE_FINISHED = registerSound("mob.caveman.ice_finished");
		
		// Cavewoman
		CAVEWOMAN_DEATH = registerSound("mob.cavewoman.death");
		CAVEWOMAN_FROZEN = registerSound("mob.cavewoman.frozen");
		CAVEWOMAN_FREE = registerSound("mob.cavewoman.free");
		
		// Desert Lizard
		DESERT_LIZARD = registerSound("mob.desert_lizard.idle");
		DESERT_LIZARD_HURT = registerSound("mob.desert_lizard.hurt");
		DESERT_LIZARD_DEATH = registerSound("mob.desert_lizard.death");
		DESERT_LIZARD_FIREBALL = registerSound("mob.desert_lizard.fireball");
		
		// Dig bug
		DIG_BUG_DIG = registerSound("mob.dig_bug.dig");
		DIG_BUG_CALL = registerSound("mob.dig_bug.call");
		DIG_BUG_DEATH = registerSound("mob.dig_bug.death");
		DIG_BUG_EAT = registerSound("mob.dig_bug.eat");
		DIG_BUG_HURT = registerSound("mob.dig_bug.hurt");
		DIG_BUG_FULL = registerSound("mob.dig_bug.full");
		
		// Disco Mole
		DISCO_MOLE = registerSound("mob.disco_mole.idle");
		DISCO_MOLE_HURT = registerSound("mob.disco_mole.hurt");
		DISCO_MOLE_DEATH = registerSound("mob.disco_mole.death");
		
		// Evil Creature
		EVIL_CREATURE = registerSound("mob.evil_creature.idle");
		EVIL_CREATURE_HURT = registerSound("mob.evil_creature.hurt");
		EVIL_CREATURE_DEATH = registerSound("mob.evil_creature.death");
		EVIL_CREATURE_JUMP = registerSound("mob.evil_creature.jump");
		
		// Evil Scientist
		EVIL_EXPLOSION = registerSound("mob.evil_scientist.explosion");
		EVIL_HURT = registerSound("mob.evil_scientist.hurt");
		EVIL_LAUGH = registerSound("mob.evil_scientist.laugh");
		EVIL_LIGHT = registerSound("mob.evil_scientist.light");
		
		// Floob
		FLOOB = registerSound("mob.floob.idle");
		FLOOB_HURT = registerSound("mob.floob.hurt");
		FLOOB_DEATH = registerSound("mob.floob.death");
		
		// Floob Ship
		FLOOB_SHIP = registerSound("mob.floob.ship.idle");
		FLOOB_SHIP_CLANG = registerSound("mob.floob.ship.clang");
		FLOOB_SHIP_SPAWN = registerSound("mob.floob.ship.spawn");
		FLOOB_SHIP_EXPLODE = registerSound("mob.floob.ship.explode");
		
		// G
		G = registerSound("mob.g.idle");
		G_HURT = registerSound("mob.g.hurt");
		G_DEATH = registerSound("mob.g.death");
		
		// Giraffe
		GIRAFFE = registerSound("mob.giraffe.idle");
		GIRAFFE_HURT = registerSound("mob.giraffe.hurt");
		GIRAFFE_DEATH = registerSound("mob.giraffe.death");
		GIRAFFE_CHEW = registerSound("mob.giraffe.chew");
		GIRAFFE_SPLASH = registerSound("mob.giraffe.splash");
		GIRAFFE_GALLOP = registerSound("mob.giraffe.gallop");
		GIRAFFE_TAMED = registerSound("mob.giraffe.tamed");
		
		// Goo Goat
		GOO_GOAT = registerSound("mob.goo_goat.idle");
		GOO_GOAT_HURT = registerSound("mob.goo_goat.hurt");
		GOO_GOAT_DEATH = registerSound("mob.goo_goat.death");
		GOO_GOAT_STRETCH = registerSound("mob.goo_goat.stretch");
		
		// Gregg
		GREGG = registerSound("mob.gregg.idle");
		GREGG_HURT = registerSound("mob.gregg.hurt");
		GREGG_DEATH = registerSound("mob.gregg.death");
		
		// Guinea Pig
		GUINEA_PIG = registerSound("mob.guinea_pig.idle");
		GUINEA_PIG_ANGRY = registerSound("mob.guinea_pig.angry");
		GUINEA_PIG_ARMOUR = registerSound("mob.guinea_pig.armour");
		GUINEA_PIG_CRITICAL_HIT = registerSound("mob.guinea_pig.critical_hit");
		GUINEA_PIG_DEATH = registerSound("mob.guinea_pig.death");
		GUINEA_PIG_EAT = registerSound("mob.guinea_pig.eat");
		GUINEA_PIG_FULL = registerSound("mob.guinea_pig.full");
		GUINEA_PIG_HOTEL = registerSound("mob.guinea_pig.hotel");
		GUINEA_PIG_MOUNT = registerSound("mob.guinea_pig.mount");
		GUINEA_PIG_NO_WHEAT = registerSound("mob.guinea_pig.no_wheat");
		GUINEA_PIG_SPEED_UP = registerSound("mob.guinea_pig.speed_up");
		GUINEA_PIG_SPEED_DOWN = registerSound("mob.guinea_pig.speed_down");
		GUINEA_PIG_TRAIN = registerSound("mob.guinea_pig.train");
		GUINEA_PIG_LEVEL_UP = registerSound("mob.guinea_pig.level_up");
		GUINEA_PIG_LEVEL_5 = registerSound("mob.guinea_pig.level_5");
		GUINEA_PIG_LEVEL_10 = registerSound("mob.guinea_pig.level_10");
		GUINEA_PIG_LEVEL_15 = registerSound("mob.guinea_pig.level_15");
		GUINEA_PIG_LEVEL_20 = registerSound("mob.guinea_pig.level_20");
		
		// Hippo
		HIPPO = registerSound("mob.hippo.idle");
		HIPPO_DEATH = registerSound("mob.hippo.death");
		HIPPO_HURT = registerSound("mob.hippo.hurt");
		
		// Rocket-Powered Horse Head (This mod is just WACKY I tell ya.)
		HORSE_HEAD = registerSound("mob.horse_head.idle");
		HORSE_HEAD_BLAST_OFF = registerSound("mob.horse_head.blast_off");
		
		// Hot Dog
		HOT_DOG = registerSound("mob.hot_dog.idle");
		HOT_DOG_ATTACK = registerSound("mob.hot_dog.attack");
		HOT_DOG_DEATH = registerSound("mob.hot_dog.death");
		HOT_DOG_EAT = registerSound("mob.hot_dog.eat");
		HOT_DOG_HEAVEN = registerSound("mob.hot_dog.heaven");
		HOT_DOG_HURT = registerSound("mob.hot_dog.hurt");
		HOT_DOG_KILL = registerSound("mob.hot_dog.kill");
		HOT_DOG_LEVEL_5 = registerSound("mob.hot_dog.level_5");
		HOT_DOG_LEVEL_10 = registerSound("mob.hot_dog.level_10");
		HOT_DOG_LEVEL_15 = registerSound("mob.hot_dog.level_15");
		HOT_DOG_LEVEL_20 = registerSound("mob.hot_dog.level_20");
		HOT_DOG_LEVEL_25 = registerSound("mob.hot_dog.level_25");
		HOT_DOG_NO_BONES = registerSound("mob.hot_dog.no_bones");
		HOT_DOG_PICK_UP = registerSound("mob.hot_dog.pick_up");
		HOT_DOG_PUT_DOWN = registerSound("mob.hot_dog.put_down");
		HOT_DOG_REDSTONE = registerSound("mob.hot_dog.redstone");
		HOT_DOG_TAMED = registerSound("mob.hot_dog.tamed");
		HOT_DOG_TRAIN = registerSound("mob.hot_dog.train");
		HOT_DOG_WHOOSH = registerSound("mob.hot_dog.whoosh");
		
		// Hunchback
		HUNCHBACK_ARMY = registerSound("mob.hunchback.army");
		HUNCHBACK_CAKE = registerSound("mob.hunchback.cake");
		HUNCHBACK_DEATH = registerSound("mob.hunchback.death");
		HUNCHBACK_HURT = registerSound("mob.hunchback.hurt");
		HUNCHBACK_QUIET = registerSound("mob.hunchback.quiet");
		HUNCHBACK_THANK_YOU = registerSound("mob.hunchback.thank_you");
		HUNCHBACK_THANKS = registerSound("mob.hunchback.thanks");
		
		// Invisible Man
		INVISIBLE_MAN = registerSound("mob.invisible_man.idle");
		INVISIBLE_MAN_ANGRY = registerSound("mob.invisible_man.angry");
		INVISIBLE_MAN_DEATH = registerSound("mob.invisible_man.death");
		INVISIBLE_MAN_HURT = registerSound("mob.invisible_man.hurt");
		INVISIBLE_MAN_FORGET_IT = registerSound("mob.invisible_man.forget_it");
		
		// Kid
		KID = registerSound("mob.kid.idle");
		KID_COLD = registerSound("mob.kid.cold");
		KID_COOKIES = registerSound("mob.kid.cookies");
		KID_DEATH = registerSound("mob.kid.death");
		KID_DOWN = registerSound("mob.kid.down");
		KID_FIND = registerSound("mob.kid.find");
		KID_DONT_PICK_UP = registerSound("mob.kid.dont_pick_up");
		KID_DOWN = registerSound("mob.kid.down");
		KID_HURT = registerSound("mob.kid.hurt");
		KID_UP = registerSound("mob.kid.up");
		KID_RIDE = registerSound("mob.kid.ride");
		
		// Castle King
		CASTLE_KING = registerSound("mob.king.idle");
		CASTLE_KING = registerSound("mob.king.hurt");
		CASTLE_KING = registerSound("mob.king.death"); // REGICIDE!
		
		// Lawyer
		LAWYER = registerSound("mob.lawyer.idle");
		LAWYER_BUM = registerSound("mob.lawyer.bum");
		LAWYER_DEATH = registerSound("mob.lawyer.death");
		LAWYER_HURT = registerSound("mob.lawyer.hurt");
		LAWYER_MONEY_HIT = registerSound("mob.lawyer.money_hit");
		LAWYER_SUCK = registerSound("mob.lawyer.suck");
		LAWYER_TAKE = registerSound("mob.lawyer.take");
		
		// Undead Lawyer
		LAWYER_UNDEAD = registerSound("mob.lawyer.undead.idle");
		LAWYER_UNDEAD_HURT = registerSound("mob.lawyer.undead.hurt");
		LAWYER_UNDEAD_DEATH = registerSound("mob.lawyer.undead.death"); // death x2
		
		// Lolliman
		LOLLIMAN = registerSound("mob.lolliman.idle");
		LOLLIMAN_HURT = registerSound("mob.lolliman.hurt");
		LOLLIMAN_DEATH = registerSound("mob.lolliman.death");
		LOLLIMAN_EXPLODE = registerSound("mob.lolliman.explode");
		LOLLIMAN_TAKE_OFF = registerSound("mob.lolliman.take_off");
		
		// Man-dog
		MAN_DOG = registerSound("mob.man_dog.idle");
		MAN_DOG_HURT = registerSound("mob.man_dog.hurt");
		MAN_DOG_DEATH = registerSound("mob.man_dog.death");
		MAN_DOG_TAMED = registerSound("mob.man_dog.tamed");
		
		// Baby Mummy
		BABY_MUMMY = registerSound("mob.mummy.baby.idle");
		BABY_MUMMY_DEATH = registerSound("mob.mummy.baby.death");
		BABY_MUMMY_HURT = registerSound("mob.mummy.baby.hurt");
		
		// Mummy
		MUMMY = registerSound("mob.mummy.idle");
		MUMMY_DEATH = registerSound("mob.mummy.death");
		MUMMY_HURT = registerSound("mob.mummy.hurt");
		
		// Non-Swimmer
		NON_SWIMMER = registerSound("mob.non_swimmer.idle");
		NON_SWIMMER_DEATH = registerSound("mob.non_swimmer.death");
		NON_SWIMMER_HURT = registerSound("mob.non_swimmer.hurt");
		NON_SWIMMER_REWARD = registerSound("mob.non_swimmer.reward");
		NON_SWIMMER_SORRY = registerSound("mob.non_swimmer.sorry");
		
		// Old Woman
		OLD = registerSound("mob.old_woman.idle");
		OLD_HURT = registerSound("mob.old_woman.hurt");
		OLD_DEATH = registerSound("mob.old_woman.death");
		OLD_PICK_UP = registerSound("mob.old_woman.pick_up");
		OLD_PUT_DOWN = registerSound("mob.old_woman.put_down");
		
		// Pony
		PONY = registerSound("mob.pony.idle");
		PONY_ATTACK = registerSound("mob.pony.attack");
		PONY_CLOUD = registerSound("mob.pony.cloud");
		PONY_CLOUD_BLOW = registerSound("mob.pony.cloud_blow");
		PONY_CLOUD_KILL = registerSound("mob.pony.cloud_kill");
		PONY_DEATH = registerSound("mob.pony.death");
		PONY_DRINK = registerSound("mob.pony.drink");
		PONY_FULL = registerSound("mob.pony.full");
		PONY_TROT = registerSound("mob.pony.trot");
		PONY_KISS = registerSound("mob.pony.kiss");
		PONY_POP_OFF = registerSound("mob.pony.pop_off");
		PONY_PRIVACY = registerSound("mob.pony.privacy");
		PONY_MATING = registerSound("mob.pony.mating");
		PONY_ROMANCE_OVER = registerSound("mob.pony.mating");
		PONY_GROWN_UP = registerSound("mob.pony.grown_up");
		PONY_LEVEL_5 = registerSound("mob.pony.level_5");
		PONY_LEVEL_10 = registerSound("mob.pony.level_10");
		PONY_LEVEL_15 = registerSound("mob.pony.level_15");
		PONY_LEVEL_20 = registerSound("mob.pony.level_20");
		PONY_NO_EXPERIENCE = registerSound("mob.pony.no_experience");
		
		// Pony Girl
		PONY_GIRL_BUY = registerSound("mob.pony_girl.buy");
		PONY_GIRL_CELL = registerSound("mob.pony_girl.cell");
		PONY_GIRL_DEATH = registerSound("mob.pony_girl.death");
		PONY_GIRL_HURT = registerSound("mob.pony_girl.hurt");
		PONY_GIRL_MONEY = registerSound("mob.pony_girl.money");
		PONY_GIRL_NO_THANKS = registerSound("mob.pony_girl.no_thanks");
		PONY_GIRL_WAIT_HERE = registerSound("mob.pony_girl.wait_there");
		
		// Preacher
		PREACHER = registerSound("mob.preacher.idle");
		PREACHER_BURN = registerSound("mob.preacher.burn");
		PREACHER_HURT = registerSound("mob.preacher.hurt");
		PREACHER_RAISE = registerSound("mob.preacher.raise");
		PREACHER_DEATH = registerSound("mob.preacher.death");
		
		// Prisoner
		PRISONER = registerSound("mob.prisoner.idle");
		PRISONER_DEATH = registerSound("mob.prisoner.death");
		PRISONER_REWARD = registerSound("mob.prisoner.reward");
		PRISONER_HURT = registerSound("mob.prisoner.hurt");
		PRISONER_SORRY = registerSound("mob.prisoner.sorry");

		
		// Rat Man
		RAT_MAN = registerSound("mob.rat_man.idle");
		RAT_MAN_HURT = registerSound("mob.rat_man.hurt");
		RAT_MAN_SCRATCH = registerSound("mob.rat_man.scratch");
		
		// Robot
		ROBOT_HURT = registerSound("mob.robot.hurt");
			// Robot Ted
		TED_INSULT = registerSound("mob.robot.ted.insult");
		TED_DEATH = registerSound("mob.robot.ted.death");
			// Robot Todd
		TODD_INSULT = registerSound("mob.robot.todd.insult");
		TODD_DEATH = registerSound("mob.robot.todd.death");
		
		// Rock Monster
		ROCK_MONSTER = registerSound("mob.rock_monster.idle");
		ROCK_MONSTER_HURT = registerSound("mob.rock_monster.hurt");
		ROCK_MONSTER_DEATH = registerSound("mob.rock_monster.death");
		
		// Sal
		SAL_GREETING = registerSound("mob.sal.greeting");
		SAL_DEATH = registerSound("mob.sal.death");
		SAL_HURT = registerSound("mob.sal.hurt");
		SAL_NO_MONEY = registerSound("mob.sal.no_money");
		SAL_NOBODY_SHRINKS = registerSound("mob.sal.nobody_shrinks");
		SAL_RATS = registerSound("mob.sal.rats");
		SAL_SALE = registerSound("mob.sal.sale");
		
		// Schlump
		SCHLUMP = registerSound("mob.schlump.idle");
		SCHLUMP_BIG = registerSound("mob.schlump.big");
		SCHLUMP_BRIGHT = registerSound("mob.schlump.bright");
		SCHLUMP_DEATH = registerSound("mob.schlump.death");
		SCHLUMP_HURT = registerSound("mob.schlump.hurt");
		SCHLUMP_INDOORS = registerSound("mob.schlump.indoors");
		SCHLUMP_OK = registerSound("mob.schlump.ok");
		SCHLUMP_OVERLOAD = registerSound("mob.schlump.overload");
		SCHLUMP_REWARD = registerSound("mob.schlump.reward");
		SCHLUMP_ROOM = registerSound("mob.schlump.room");
		SCHLUMP_SUCKS = registerSound("mob.schlump.sucks");
		
		// Snow Devil
		SNOW_DEVIL = registerSound("mob.snow_devil.idle");
		SNOW_DEVIL_HURT = registerSound("mob.snow_devil.hurt");
		SNOW_DEVIL_DEATH = registerSound("mob.snow_devil.death");
		SNOW_DEVIL_TAMED = registerSound("mob.snow_devil.tamed");
		
		// Evil Snow Man
		SNOW_MAN = registerSound("mob.snow_man.idle");
		SNOW_MAN_BOUNCE = registerSound("mob.snow_man.bounce");
		SNOW_MAN_DEATH = registerSound("mob.snow_man.death");
		SNOW_MAN_HURT = registerSound("mob.snow_man.hurt");
		
		// Super Dog
		SUPER_DOG_APPLE = registerSound("mob.super_dog.apple");
		SUPER_DOG_NAME = registerSound("mob.super_dog.name");
		
		// Thief
		THIEF = registerSound("mob.thief.idle");
		THIEF_FIND_PLAYER = registerSound("mob.thief.find_player");
		THIEF_STEAL = registerSound("mob.thief.steal");
		THIEF_HURT = registerSound("mob.thief.hurt");
		THIEF_DEATH = registerSound("mob.thief.death");
		
		CHICKEN_EAT = registerSound("mob.chicken_eat");
		
		// Items
			// Gems
		ARMY_GEM = registerSound("item.army_gem");
		EARTH_GEM = registerSound("item.earth_gem");
		SKY_GEM = registerSound("item.sky_gem");
		SKY_GEM_UP = registerSound("item.sky_gem_up");
		SKY_GEM_DOWN = registerSound("item.sky_gem_down");
		HEALING_GEM = registerSound("item.healing_gem");
		HORSE_HEAD_GEM = registerSound("items.horse_head_gem");
		MINING_GEM = registerSound("item.mining_gem");
		MINING_GEM_BAD = registerSound("item.mining_gem_bad");
			// Everything else
		BABY_JAR_EMPTY = registerSound("item.baby_jar_empty");
		BAND_AID = registerSound("items.band_aid");
		BLORP_COLA = registerSound("items.blorp_cola");
		BOMB_SET = registerSound("items.bomb_set");
		BOOMBOX = registerSound("items.boombox");
		BULLET = registerSound("items.bullet");
		BULLET_HIT = registerSound("items.bullet_hit");
		DIODE = registerSound("items.diode");
		EXTINGUISHER = registerSound("items.extinguisher");
		GEM_SWORD = registerSound("items.gem_sword");
		GROW_RAY = registerSound("items.grow_ray");
		GUM_BLOW = registerSound("items.gum_blow");
		HARD_CANDY = registerSound("items.hard_candy");
		LOLLY = registerSound("items.lolly");
		MEDICINE = registerSound("items.medicine");
		MOBILE = registerSound("items.mobile");
		PASTA_EAT = registerSound("items.pasta_eat");
		PET_RADIO = registerSound("items.pet_radio");
		PET_RADIO_DROP = registerSound("items.pet_radio_drop");
		PET_RADIO_PICK_UP = registerSound("items.pet_radio_pick_up");
		RAY_GUN = registerSound("items.ray_gun");
		ROCKET_EXPLODE = registerSound("items.rocket_explode");
		ROCKET_FIRE = registerSound("items.rocket_fire");
		SHRINK_KILL = registerSound("items.shrink_kill");
		SHRINK_RAY = registerSound("items.shrink_ray");
		TOMBSTONE = registerSound("items.tombstone");

		// Pyramid
		PYRAMID_DISCOVERED = registerSound("random.pyramid.discovered");
		PYRAMID = registerSound("random.pyramid.pyramid");
		PYRAMID_CURSE = registerSound("random.pyramid.curse");
		PYRAMID_HURT = registerSound("random.pyramid.hurt");
		PYRAMID_DEATH = registerSound("random.pyramid.death");
		
		// UI, voice, and random
		ACHIEVEMENT = registerSound("ui.toast.achievement");
		FEATURE_NOT_AVAILABLE = registerSound("random.feature_not_available");
		WELCOME = registerSound("random.welcome");
		SPARK = registerSound("random.spark");
		TROPHY_SMASH = registerSound("random.trophy_smash");
		LICK = registerSound("random.lick");
		BELL_BASE = registerSound("random.bellbase");
		CHEW = registerSound("random.chew");
		BARF = registerSound("random.barf");
		TOWEL_FUN = registerSound("random.towel_fun");
	}
	
	private static SoundEvent registerSound(String name)
	{
		ResourceLocation location = new ResourceLocation(Reference.MODID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}