package fr.elias.morecreeps.common;

import java.util.Properties;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.entity.ArmyGuyEntity;
import fr.elias.morecreeps.common.entity.ArmyGuyArmEntity;
import fr.elias.morecreeps.common.entity.FrisbeeEntity;
import fr.elias.morecreeps.common.entity.GooDonutEntity;
import fr.elias.morecreeps.common.entity.GrowEntity;
import fr.elias.morecreeps.common.entity.MoneyEntity;
import fr.elias.morecreeps.common.entity.RayEntity;
import fr.elias.morecreeps.common.entity.ShrinkEntity;
import fr.elias.morecreeps.common.items.ItemArmSword;
import fr.elias.morecreeps.common.items.ItemArmyGem;
import fr.elias.morecreeps.common.items.ItemAtom;
import fr.elias.morecreeps.common.items.ItemBabyJarEmpty;
import fr.elias.morecreeps.common.items.ItemBabyJarFull;
import fr.elias.morecreeps.common.items.ItemBandAid;
import fr.elias.morecreeps.common.items.ItemBattery;
import fr.elias.morecreeps.common.items.ItemBlorpCola;
import fr.elias.morecreeps.common.items.ItemBullet;
import fr.elias.morecreeps.common.items.ItemCavemanClub;
import fr.elias.morecreeps.common.items.ItemDonut;
import fr.elias.morecreeps.common.items.ItemEarthGem;
import fr.elias.morecreeps.common.items.ItemEvilEgg;
import fr.elias.morecreeps.common.items.ItemExtinguisher;
import fr.elias.morecreeps.common.items.ItemFireGem;
import fr.elias.morecreeps.common.items.ItemFrisbee;
import fr.elias.morecreeps.common.items.ItemGemSword;
import fr.elias.morecreeps.common.items.ItemGooDonut;
import fr.elias.morecreeps.common.items.ItemGrowRay;
import fr.elias.morecreeps.common.items.ItemPetRadio;
import fr.elias.morecreeps.common.items.ItemGun;
import fr.elias.morecreeps.common.items.ItemHealingGem;
import fr.elias.morecreeps.common.items.ItemHorseHeadGem;
import fr.elias.morecreeps.common.items.ItemLifeGem;
import fr.elias.morecreeps.common.items.ItemLimbs;
import fr.elias.morecreeps.common.items.ItemLolly;
import fr.elias.morecreeps.common.items.ItemMiningGem;
import fr.elias.morecreeps.common.items.ItemMobilePhone;
import fr.elias.morecreeps.common.items.ItemMoney;
import fr.elias.morecreeps.common.items.ItemMoopsWorm;
import fr.elias.morecreeps.common.items.ItemPopsicle;
import fr.elias.morecreeps.common.items.ItemRayGun;
import fr.elias.morecreeps.common.items.ItemRayRay;
import fr.elias.morecreeps.common.items.ItemShrinkRay;
import fr.elias.morecreeps.common.items.ItemSkyGem;
import fr.elias.morecreeps.common.items.ItemTombstone;
import fr.elias.morecreeps.common.lists.ArmourMaterialList;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.recipes.CREEPSRecipeHandler;
import fr.elias.morecreeps.common.util.handlers.CREEPSGuiHandler;
import fr.elias.morecreeps.common.world.WorldGenStructures;
import fr.elias.morecreeps.proxy.CommonProxy;
import net.minecraft.advancements.Advancement;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("unused")
@Mod("morecreeps")
public class MoreCreepsReboot
{
	
	public static MoreCreepsReboot instance;
	public static final String modid = "morecreeps";
	private static final Logger logger = LogManager.getLogger(modid);
	
	public static final ItemGroup creepsTab = new CreepsItemGroup();
	
	public static Random rand = new Random();
	
	private int count;
	
	public int spittime = 500;
	
	public int currentJailX;
	public int currentJailY;
	public int currentJailZ;
	public boolean jailBuilt;
	public int currentfine;
	public int creepsTimer;
	
	public static int prisonercount = 0;
	public static int colacount = 0;
	public static int rocketcount = 0;
	public static int floobcount = 0;
	public static int goatcount = 0;
	public static int preachercount = 0;
	public static int cavemancount = 0;
	public static boolean cavemanbuilding = false;
    
    
    public static int aX;
    public static int aY;
    
	public static CommonProxy proxy;

    public MoreCreepsReboot()
    {
    	instance = this;
    	
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    	FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
    
    	MinecraftForge.EVENT_BUS.register(this);
	}
    
    private void setup(final FMLClientSetupEvent event)
    {
    	logger.info("Setup method registered.");
    }
    
    private void clientRegistries(final FMLClientSetupEvent event)
    {
    	logger.info("clientRegistries method registered");
    }
    
	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegsitryEvents
	{
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event)
		{
			event.getRegistry().registerAll
			(
					ItemList.zebra_helmet = new ArmorItem(ArmourMaterialList.armorZEBRA, EquipmentSlotType.HEAD, new Item.Properties().group(creepsTab)).setRegistryName(location("zebra_helmet")),
					ItemList.zebra_chestplate = new ArmorItem(ArmourMaterialList.armorZEBRA, EquipmentSlotType.CHEST, new Item.Properties().group(creepsTab)).setRegistryName(location("zebra_chestplate")),
					ItemList.zebra_leggings = new ArmorItem(ArmourMaterialList.armorZEBRA, EquipmentSlotType.LEGS, new Item.Properties().group(creepsTab)).setRegistryName(location("zebra_leggings")),
					ItemList.zebra_boots = new ArmorItem(ArmourMaterialList.armorZEBRA, EquipmentSlotType.FEET, new Item.Properties().group(creepsTab)).setRegistryName(location("zebra_boots")),
					ItemList.arm_sword = new ItemArmSword(ItemTier.WOOD, 3, -2.4F, new Item.Properties().group(creepsTab)),
					ItemList.raging_helmet = new ArmorItem(ArmourMaterialList.armorRAGING, EquipmentSlotType.HEAD, new Item.Properties().group(creepsTab)).setRegistryName(location("zebra_helmet")),
					ItemList.raging_chestplate = new ArmorItem(ArmourMaterialList.armorRAGING, EquipmentSlotType.CHEST, new Item.Properties().group(creepsTab)).setRegistryName(location("zebra_chestplate")),
					ItemList.raging_leggings = new ArmorItem(ArmourMaterialList.armorRAGING, EquipmentSlotType.LEGS, new Item.Properties().group(creepsTab)).setRegistryName(location("zebra_leggings")),
					ItemList.raging_boots = new ArmorItem(ArmourMaterialList.armorRAGING, EquipmentSlotType.FEET, new Item.Properties().group(creepsTab)).setRegistryName(location("zebra_boots")),
					ItemList.arm_sword = new ItemArmSword(ItemTier.WOOD, 3, -2.4F, new Item.Properties().group(creepsTab)),
					ItemList.army_gem = new ItemArmyGem(),
					ItemList.atom_packet = new ItemAtom(),
					ItemList.baby_jar_empty = new ItemBabyJarEmpty(),
					ItemList.baby_jar_full = new ItemBabyJarFull(),
					ItemList.band_aid = new ItemBandAid(),
					ItemList.battery = new ItemBattery(),
					ItemList.blorp_cola = new ItemBlorpCola(),
					ItemList.bullet = new ItemBullet(),
					ItemList.caveman_club = new ItemCavemanClub(),
					ItemList.donut = new ItemDonut(),
					ItemList.earth_gem = new ItemEarthGem(),
					ItemList.fire_gem = new ItemFireGem(),
					ItemList.evil_egg = new ItemEvilEgg(),
					ItemList.extinguisher = new ItemExtinguisher(),
					ItemList.frisbee = new ItemFrisbee(),
					ItemList.gem_sword = new ItemGemSword(),
					ItemList.goo_donut = new ItemGooDonut(),
					ItemList.grow_ray = new ItemGrowRay(),
					ItemList.pet_radio = new ItemPetRadio(),
					ItemList.gun = new ItemGun(),
					ItemList.healing_gem = new ItemHealingGem(),
					ItemList.horse_head_gem = new ItemHorseHeadGem(),
					ItemList.life_gem = new ItemLifeGem(),
					ItemList.limbs = new ItemLimbs(),
					ItemList.lolly = new ItemLolly(),
					ItemList.mining_gem = new ItemMiningGem(),
					ItemList.mobile_phone = new ItemMobilePhone(),
					ItemList.money = new ItemMoney(),
					ItemList.moops_worm = new ItemMoopsWorm(),
					ItemList.popsicle = new ItemPopsicle(),
					ItemList.ray_gun = new ItemRayGun(),
					ItemList.ray_ray = new ItemRayRay(),
					ItemList.shrink_ray = new ItemShrinkRay(),
					ItemList.sky_gem = new ItemSkyGem(),
					ItemList.tombstone = new ItemTombstone(0),
					ItemList.zebra_hide = new Item(new Item.Properties().group(creepsTab)),
					// Icons
					ItemList.a_camel = new Item(new Item.Properties().maxStackSize(1).group(creepsTab)),
					ItemList.a_hell = new Item(new Item.Properties().maxStackSize(1).group(creepsTab)),
					ItemList.a_caveman = new Item(new Item.Properties().maxStackSize(1).group(creepsTab)),
					ItemList.a_floob = new Item(new Item.Properties().maxStackSize(1).group(creepsTab)),
					ItemList.a_hotdog = new Item(new Item.Properties().maxStackSize(1).group(creepsTab)),
					ItemList.a_nonswimmer = new Item(new Item.Properties().maxStackSize(1).group(creepsTab)),
					ItemList.a_pig = new Item(new Item.Properties().maxStackSize(1).group(creepsTab)),
					ItemList.a_pyramid = new Item(new Item.Properties().maxStackSize(1).group(creepsTab)),
					ItemList.a_rockmonster = new Item(new Item.Properties().maxStackSize(1).group(creepsTab)),
					ItemList.a_zebra = new Item(new Item.Properties().maxStackSize(1).group(creepsTab))
					);
			
			logger.info("Items registered.");
		}
		
		private static ResourceLocation location(String name)
		{
			return new ResourceLocation(modid, name);
		}
	}
}