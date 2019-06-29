package fr.elias.morecreeps.proxy;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.models.CREEPSModelArmyGuy;
import fr.elias.morecreeps.client.models.CREEPSModelArmyGuyArm;
import fr.elias.morecreeps.client.models.CREEPSModelAtom;
import fr.elias.morecreeps.client.models.CREEPSModelBigBaby;
import fr.elias.morecreeps.client.models.CREEPSModelBlorp;
import fr.elias.morecreeps.client.models.CREEPSModelBubbleScum;
import fr.elias.morecreeps.client.models.CREEPSModelBum;
import fr.elias.morecreeps.client.models.CREEPSModelCamel;
import fr.elias.morecreeps.client.models.CREEPSModelCamelJockey;
import fr.elias.morecreeps.client.models.CREEPSModelCastleCritter;
import fr.elias.morecreeps.client.models.CREEPSModelCastleKing;
import fr.elias.morecreeps.client.models.CREEPSModelCaveman;
import fr.elias.morecreeps.client.models.CREEPSModelDesertLizard;
import fr.elias.morecreeps.client.models.CREEPSModelDigBug;
import fr.elias.morecreeps.client.models.CREEPSModelDoghouse;
import fr.elias.morecreeps.client.models.CREEPSModelEvilChicken;
import fr.elias.morecreeps.client.models.CREEPSModelEvilCreature;
import fr.elias.morecreeps.client.models.CREEPSModelEvilLight;
import fr.elias.morecreeps.client.models.CREEPSModelEvilPig;
import fr.elias.morecreeps.client.models.CREEPSModelEvilScientist;
import fr.elias.morecreeps.client.models.CREEPSModelEvilSnowman;
import fr.elias.morecreeps.client.models.CREEPSModelFloob;
import fr.elias.morecreeps.client.models.CREEPSModelFloobShip;
import fr.elias.morecreeps.client.models.CREEPSModelG;
import fr.elias.morecreeps.client.models.CREEPSModelGPig;
import fr.elias.morecreeps.client.models.CREEPSModelGooGoat;
import fr.elias.morecreeps.client.models.CREEPSModelHippo;
import fr.elias.morecreeps.client.models.CREEPSModelHorseHead;
import fr.elias.morecreeps.client.models.CREEPSModelHotdog;
import fr.elias.morecreeps.client.models.CREEPSModelHunchback;
import fr.elias.morecreeps.client.models.CREEPSModelKid;
import fr.elias.morecreeps.client.models.CREEPSModelLawyerFromHell;
import fr.elias.morecreeps.client.models.CREEPSModelLolliman;
import fr.elias.morecreeps.client.models.CREEPSModelManDog;
import fr.elias.morecreeps.client.models.CREEPSModelNonSwimmer;
import fr.elias.morecreeps.client.models.CREEPSModelPreacher;
import fr.elias.morecreeps.client.models.CREEPSModelPyramidGuardian;
import fr.elias.morecreeps.client.models.CREEPSModelRatMan;
import fr.elias.morecreeps.client.models.CREEPSModelRobotTed;
import fr.elias.morecreeps.client.models.CREEPSModelRobotTodd;
import fr.elias.morecreeps.client.models.CREEPSModelRockMonster;
import fr.elias.morecreeps.client.models.CREEPSModelRocketGiraffe;
import fr.elias.morecreeps.client.models.CREEPSModelSchlump;
import fr.elias.morecreeps.client.models.CREEPSModelSneakySal;
import fr.elias.morecreeps.client.models.CREEPSModelSnowDevil;
import fr.elias.morecreeps.client.models.CREEPSModelSquimp;
import fr.elias.morecreeps.client.models.CREEPSModelTombstone;
import fr.elias.morecreeps.client.models.CREEPSModelTowel;
import fr.elias.morecreeps.client.models.CREEPSModelTrophy;
import fr.elias.morecreeps.client.models.CREEPSModelZebra;
import fr.elias.morecreeps.client.other.TickClientHandlerEvent;
import fr.elias.morecreeps.client.particles.CREEPSFxAtoms;
import fr.elias.morecreeps.client.particles.CREEPSFxBlood;
import fr.elias.morecreeps.client.particles.CREEPSFxBubbles;
import fr.elias.morecreeps.client.particles.CREEPSFxConfetti;
import fr.elias.morecreeps.client.particles.CREEPSFxDirt;
import fr.elias.morecreeps.client.particles.CREEPSFxFoam;
import fr.elias.morecreeps.client.particles.CREEPSFxPee;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.client.render.CREEPSRenderArmyGuy;
import fr.elias.morecreeps.client.render.CREEPSRenderArmyGuyArm;
import fr.elias.morecreeps.client.render.CREEPSRenderAtom;
import fr.elias.morecreeps.client.render.CREEPSRenderBabyMummy;
import fr.elias.morecreeps.client.render.CREEPSRenderBigBaby;
import fr.elias.morecreeps.client.render.CREEPSRenderBlackSoul;
import fr.elias.morecreeps.client.render.CREEPSRenderBlorp;
import fr.elias.morecreeps.client.render.CREEPSRenderBubbleScum;
import fr.elias.morecreeps.client.render.CREEPSRenderBum;
import fr.elias.morecreeps.client.render.CREEPSRenderCamel;
import fr.elias.morecreeps.client.render.CREEPSRenderCamelJockey;
import fr.elias.morecreeps.client.render.CREEPSRenderCastleCritter;
import fr.elias.morecreeps.client.render.CREEPSRenderCastleKing;
import fr.elias.morecreeps.client.render.CREEPSRenderCaveman;
import fr.elias.morecreeps.client.render.CREEPSRenderDesertLizard;
import fr.elias.morecreeps.client.render.CREEPSRenderDigBug;
import fr.elias.morecreeps.client.render.CREEPSRenderDoghouse;
import fr.elias.morecreeps.client.render.CREEPSRenderEvilChicken;
import fr.elias.morecreeps.client.render.CREEPSRenderEvilCreature;
import fr.elias.morecreeps.client.render.CREEPSRenderEvilLight;
import fr.elias.morecreeps.client.render.CREEPSRenderEvilPig;
import fr.elias.morecreeps.client.render.CREEPSRenderEvilScientist;
import fr.elias.morecreeps.client.render.CREEPSRenderEvilSnowman;
import fr.elias.morecreeps.client.render.CREEPSRenderFloob;
import fr.elias.morecreeps.client.render.CREEPSRenderFloobShip;
import fr.elias.morecreeps.client.render.CREEPSRenderG;
import fr.elias.morecreeps.client.render.CREEPSRenderGooGoat;
import fr.elias.morecreeps.client.render.CREEPSRenderGuineaPig;
import fr.elias.morecreeps.client.render.CREEPSRenderHippo;
import fr.elias.morecreeps.client.render.CREEPSRenderHorseHead;
import fr.elias.morecreeps.client.render.CREEPSRenderHotdog;
import fr.elias.morecreeps.client.render.CREEPSRenderHunchback;
import fr.elias.morecreeps.client.render.CREEPSRenderKid;
import fr.elias.morecreeps.client.render.CREEPSRenderLawyerFromHell;
import fr.elias.morecreeps.client.render.CREEPSRenderLolliman;
import fr.elias.morecreeps.client.render.CREEPSRenderManDog;
import fr.elias.morecreeps.client.render.CREEPSRenderNonSwimmer;
import fr.elias.morecreeps.client.render.CREEPSRenderPreacher;
import fr.elias.morecreeps.client.render.CREEPSRenderPyramidGuardian;
import fr.elias.morecreeps.client.render.CREEPSRenderRatMan;
import fr.elias.morecreeps.client.render.CREEPSRenderRobotTed;
import fr.elias.morecreeps.client.render.CREEPSRenderRobotTodd;
import fr.elias.morecreeps.client.render.CREEPSRenderRockMonster;
import fr.elias.morecreeps.client.render.CREEPSRenderRocketGiraffe;
import fr.elias.morecreeps.client.render.CREEPSRenderSchlump;
import fr.elias.morecreeps.client.render.CREEPSRenderSneakySal;
import fr.elias.morecreeps.client.render.CREEPSRenderSnowDevil;
import fr.elias.morecreeps.client.render.CREEPSRenderSquimp;
import fr.elias.morecreeps.client.render.CREEPSRenderThief;
import fr.elias.morecreeps.client.render.CREEPSRenderTombstone;
import fr.elias.morecreeps.client.render.CREEPSRenderTowel;
import fr.elias.morecreeps.client.render.CREEPSRenderTrophy;
import fr.elias.morecreeps.client.render.CREEPSRenderZebra;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.ArmyGuyEntity;
import fr.elias.morecreeps.common.entity.ArmyGuyArmEntity;
import fr.elias.morecreeps.common.entity.AtomEntity;
import fr.elias.morecreeps.common.entity.BabyMummyEntity;
import fr.elias.morecreeps.common.entity.BigBabyEntity;
import fr.elias.morecreeps.common.entity.BlackSoulEntity;
import fr.elias.morecreeps.common.entity.BlorpEntity;
import fr.elias.morecreeps.common.entity.BubbleScumEntity;
import fr.elias.morecreeps.common.entity.BumEntity;
import fr.elias.morecreeps.common.entity.CamelEntity;
import fr.elias.morecreeps.common.entity.CamelJockeyEntity;
import fr.elias.morecreeps.common.entity.CastleCritterEntity;
import fr.elias.morecreeps.common.entity.CastleKingEntity;
import fr.elias.morecreeps.common.entity.CavemanEntity;
import fr.elias.morecreeps.common.entity.DesertLizardEntity;
import fr.elias.morecreeps.common.entity.DigBugEntity;
import fr.elias.morecreeps.common.entity.DogHouseEntity;
import fr.elias.morecreeps.common.entity.EvilChickenEntity;
import fr.elias.morecreeps.common.entity.EvilCreatureEntity;
import fr.elias.morecreeps.common.entity.EvilLightEntity;
import fr.elias.morecreeps.common.entity.EvilPigEntity;
import fr.elias.morecreeps.common.entity.EvilScientistEntity;
import fr.elias.morecreeps.common.entity.EvilSnowmanEntity;
import fr.elias.morecreeps.common.entity.FloobEntity;
import fr.elias.morecreeps.common.entity.FloobShipEntity;
import fr.elias.morecreeps.common.entity.FrisbeeEntity;
import fr.elias.morecreeps.common.entity.LetterGEntity;
import fr.elias.morecreeps.common.entity.GooDonutEntity;
import fr.elias.morecreeps.common.entity.EntityGooGoat;
import fr.elias.morecreeps.common.entity.GrowEntity;
import fr.elias.morecreeps.common.entity.GuineaPigEntity;
import fr.elias.morecreeps.common.entity.HippoEntity;
import fr.elias.morecreeps.common.entity.CREEPSEntityHorseHead;
import fr.elias.morecreeps.common.entity.HotdogEntity;
import fr.elias.morecreeps.common.entity.HunchbackEntity;
import fr.elias.morecreeps.common.entity.KidEntity;
import fr.elias.morecreeps.common.entity.LawyerFromHellEntity;
import fr.elias.morecreeps.common.entity.LollimanEntity;
import fr.elias.morecreeps.common.entity.ManDogEntity;
import fr.elias.morecreeps.common.entity.MoneyEntity;
import fr.elias.morecreeps.common.entity.NonSwimmerEntity;
import fr.elias.morecreeps.common.entity.PreacherEntity;
import fr.elias.morecreeps.common.entity.PyramidGuardianEntity;
import fr.elias.morecreeps.common.entity.RatManEntity;
import fr.elias.morecreeps.common.entity.RayEntity;
import fr.elias.morecreeps.common.entity.RobotTedEntity;
import fr.elias.morecreeps.common.entity.RobotToddEntity;
import fr.elias.morecreeps.common.entity.RockMonsterEntity;
import fr.elias.morecreeps.common.entity.RocketGiraffeEntity;
import fr.elias.morecreeps.common.entity.SchlumpEntity;
import fr.elias.morecreeps.common.entity.ShrinkEntity;
import fr.elias.morecreeps.common.entity.SneakySalEntity;
import fr.elias.morecreeps.common.entity.SnowDevilEntity;
import fr.elias.morecreeps.common.entity.SquimpEntity;
import fr.elias.morecreeps.common.entity.ThiefEntity;
import fr.elias.morecreeps.common.entity.TombstoneEntity;
import fr.elias.morecreeps.common.entity.TowelEntity;
import fr.elias.morecreeps.common.entity.TrophyEntity;
import fr.elias.morecreeps.common.entity.ZebraEntity;

public class ClientProxy extends CommonProxy
{
	public void render()
	{
		MinecraftForge.EVENT_BUS.register(new TickClientHandlerEvent());
		FMLCommonHandler.instance().bus().register(new TickClientHandlerEvent());
		RenderingRegistry.registerEntityRenderingHandler(ArmyGuyEntity.class, new CREEPSRenderArmyGuy(new CREEPSModelArmyGuy(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(ArmyGuyArmEntity.class, new CREEPSRenderArmyGuyArm(new CREEPSModelArmyGuyArm(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(AtomEntity.class, new CREEPSRenderAtom(new CREEPSModelAtom(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(BabyMummyEntity.class, new CREEPSRenderBabyMummy(new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(BigBabyEntity.class, new CREEPSRenderBigBaby(new CREEPSModelBigBaby(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(BlackSoulEntity.class, new CREEPSRenderBlackSoul(new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(BlorpEntity.class, new CREEPSRenderBlorp(new CREEPSModelBlorp(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(BubbleScumEntity.class, new CREEPSRenderBubbleScum(new CREEPSModelBubbleScum(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(BumEntity.class, new CREEPSRenderBum(new CREEPSModelBum(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(CamelEntity.class, new CREEPSRenderCamel(new CREEPSModelCamel(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(CamelJockeyEntity.class, new CREEPSRenderCamelJockey(new CREEPSModelCamelJockey(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(CastleCritterEntity.class, new CREEPSRenderCastleCritter(new CREEPSModelCastleCritter(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(CastleKingEntity.class, new CREEPSRenderCastleKing(new CREEPSModelCastleKing(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(CavemanEntity.class, new CREEPSRenderCaveman(new CREEPSModelCaveman(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(DesertLizardEntity.class, new CREEPSRenderDesertLizard(new CREEPSModelDesertLizard(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(DigBugEntity.class, new CREEPSRenderDigBug(new CREEPSModelDigBug(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(DogHouseEntity.class, new CREEPSRenderDoghouse(new CREEPSModelDoghouse(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EvilChickenEntity.class, new CREEPSRenderEvilChicken(new CREEPSModelEvilChicken(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EvilCreatureEntity.class, new CREEPSRenderEvilCreature(new CREEPSModelEvilCreature(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EvilLightEntity.class, new CREEPSRenderEvilLight(new CREEPSModelEvilLight(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EvilPigEntity.class, new CREEPSRenderEvilPig(new CREEPSModelEvilPig(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EvilScientistEntity.class, new CREEPSRenderEvilScientist(new CREEPSModelEvilScientist(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EvilSnowmanEntity.class, new CREEPSRenderEvilSnowman(new CREEPSModelEvilSnowman(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(FloobEntity.class, new CREEPSRenderFloob(new CREEPSModelFloob(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(FloobShipEntity.class, new CREEPSRenderFloobShip(new CREEPSModelFloobShip(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(LetterGEntity.class, new CREEPSRenderG(new CREEPSModelG(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGooGoat.class, new CREEPSRenderGooGoat(new CREEPSModelGooGoat(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(GuineaPigEntity.class, new CREEPSRenderGuineaPig(new CREEPSModelGPig(), new CREEPSModelGPig(0.5F), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(HippoEntity.class, new CREEPSRenderHippo(new CREEPSModelHippo(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(CREEPSEntityHorseHead.class, new CREEPSRenderHorseHead(new CREEPSModelHorseHead(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(HotdogEntity.class, new CREEPSRenderHotdog(new CREEPSModelHotdog(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(HunchbackEntity.class, new CREEPSRenderHunchback(new CREEPSModelHunchback(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(KidEntity.class, new CREEPSRenderKid(new CREEPSModelKid(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(LawyerFromHellEntity.class, new CREEPSRenderLawyerFromHell(new CREEPSModelLawyerFromHell(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(LollimanEntity.class, new CREEPSRenderLolliman(new CREEPSModelLolliman(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(ManDogEntity.class, new CREEPSRenderManDog(new CREEPSModelManDog(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(NonSwimmerEntity.class, new CREEPSRenderNonSwimmer(new CREEPSModelNonSwimmer(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(PreacherEntity.class, new CREEPSRenderPreacher(new CREEPSModelPreacher(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(PyramidGuardianEntity.class, new CREEPSRenderPyramidGuardian(new CREEPSModelPyramidGuardian(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(RatManEntity.class, new CREEPSRenderRatMan(new CREEPSModelRatMan(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(RobotTedEntity.class, new CREEPSRenderRobotTed(new CREEPSModelRobotTed(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(RobotToddEntity.class, new CREEPSRenderRobotTodd(new CREEPSModelRobotTodd(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(RocketGiraffeEntity.class, new CREEPSRenderRocketGiraffe(new CREEPSModelRocketGiraffe(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(RockMonsterEntity.class, new CREEPSRenderRockMonster(new CREEPSModelRockMonster(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(SchlumpEntity.class, new CREEPSRenderSchlump(new CREEPSModelSchlump(), 0.5F));
		
		RenderingRegistry.registerEntityRenderingHandler(ShrinkEntity.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), MoreCreepsReboot.shrinkshrink, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(RayEntity.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), MoreCreepsReboot.rayray, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(GrowEntity.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), MoreCreepsReboot.shrinkshrink, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(MoneyEntity.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), MoreCreepsReboot.money, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(GooDonutEntity.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), MoreCreepsReboot.goodonut, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(FrisbeeEntity.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), MoreCreepsReboot.frisbee, Minecraft.getMinecraft().getRenderItem()));
		
		RenderingRegistry.registerEntityRenderingHandler(SneakySalEntity.class, new CREEPSRenderSneakySal(new CREEPSModelSneakySal(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(SnowDevilEntity.class, new CREEPSRenderSnowDevil(new CREEPSModelSnowDevil(), new CREEPSModelSnowDevil(0.5F), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(SquimpEntity.class, new CREEPSRenderSquimp(new CREEPSModelSquimp(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(ThiefEntity.class, new CREEPSRenderThief(new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(TombstoneEntity.class, new CREEPSRenderTombstone(new CREEPSModelTombstone(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(TowelEntity.class, new CREEPSRenderTowel(new CREEPSModelTowel(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(TrophyEntity.class, new CREEPSRenderTrophy(new CREEPSModelTrophy(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(ZebraEntity.class, new CREEPSRenderZebra(new CREEPSModelZebra(), 0.5F));
	}
	
	public void renderModelItem()
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.partBubble, 0, new ModelResourceLocation("morecreeps:partBubble", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.partWhite, 0, new ModelResourceLocation("morecreeps:partWhite", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.partRed, 0, new ModelResourceLocation("morecreeps:partRed", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.partBlack, 0, new ModelResourceLocation("morecreeps:partBlack", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.partYellow, 0, new ModelResourceLocation("morecreeps:partYellow", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.partBlue, 0, new ModelResourceLocation("morecreeps:partBlue", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.partShrink, 0, new ModelResourceLocation("morecreeps:partShrink", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.partBarf, 0, new ModelResourceLocation("morecreeps:partBarf", "inventory"));

		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_hell, 0, new ModelResourceLocation("morecreeps:a_hell", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_pig, 0, new ModelResourceLocation("morecreeps:a_pig", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_pyramid, 0, new ModelResourceLocation("morecreeps:a_pyramid", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_floob, 0, new ModelResourceLocation("morecreeps:a_floob", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_rockmonster, 0, new ModelResourceLocation("morecreeps:a_rockmonster", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_bubble, 0, new ModelResourceLocation("morecreeps:a_bubble", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_hotdog, 0, new ModelResourceLocation("morecreeps:a_hotdog", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_camel, 0, new ModelResourceLocation("morecreeps:a_camel", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_zebra, 0, new ModelResourceLocation("morecreeps:a_zebra", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_nonswimmer, 0, new ModelResourceLocation("morecreeps:a_nonswimmer", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.a_caveman, 0, new ModelResourceLocation("morecreeps:a_caveman", "inventory"));
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.zebrahelmet, 0, new ModelResourceLocation("morecreeps:zebraHelmet", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.zebrabody, 0, new ModelResourceLocation("morecreeps:zebraBody", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.zebralegs, 0, new ModelResourceLocation("morecreeps:zebraLegs", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.zebraboots, 0, new ModelResourceLocation("morecreeps:zebraBoots", "inventory"));
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.blorpcola, 0, new ModelResourceLocation("morecreeps:blorpCola", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.bandaid, 0, new ModelResourceLocation("morecreeps:bandAid", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.goodonut, 0, new ModelResourceLocation("morecreeps:gooDonut", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.money, 0, new ModelResourceLocation("morecreeps:money", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.raygun, 0, new ModelResourceLocation("morecreeps:raygun", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.shrinkray, 0, new ModelResourceLocation("morecreeps:shrinkray", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.shrinkshrink, 0, new ModelResourceLocation("morecreeps:shrinkshrink", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.limbs, 0, new ModelResourceLocation("morecreeps:limbs", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.babyjarempty, 0, new ModelResourceLocation("morecreeps:babyJarEmpty", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.babyjarfull, 0, new ModelResourceLocation("morecreeps:babyJarFull", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.mobilephone, 0, new ModelResourceLocation("morecreeps:mobilephone", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.growray, 0, new ModelResourceLocation("morecreeps:growray", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.frisbee, 0, new ModelResourceLocation("morecreeps:frisbee", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.rayray, 0, new ModelResourceLocation("morecreeps:rayray", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.guineapigradio, 0, new ModelResourceLocation("morecreeps:guineapigRadio", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.evilegg, 0, new ModelResourceLocation("morecreeps:evilEgg", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.rocket, 0, new ModelResourceLocation("morecreeps:rocket", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.atompacket, 0, new ModelResourceLocation("morecreeps:atomPacket", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.ram16k, 0, new ModelResourceLocation("morecreeps:ram16k", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.battery, 0, new ModelResourceLocation("morecreeps:battery", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.horseheadgem, 0, new ModelResourceLocation("morecreeps:horseHeadGem", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.armygem, 0, new ModelResourceLocation("morecreeps:armyGem", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.gun, 0, new ModelResourceLocation("morecreeps:gun", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.bullet, 0, new ModelResourceLocation("morecreeps:bullet", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.lifegem, 0, new ModelResourceLocation("morecreeps:lifeGem", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.lolly, 0, new ModelResourceLocation("morecreeps:lolly", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.armsword, 0, new ModelResourceLocation("morecreeps:armSword", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.donut, 0, new ModelResourceLocation("morecreeps:donut", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.extinguisher, 0, new ModelResourceLocation("morecreeps:extinguisher", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.zebrahide, 0, new ModelResourceLocation("morecreeps:zebrahide", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.firegem, 0, new ModelResourceLocation("morecreeps:fireGem", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.earthgem, 0, new ModelResourceLocation("morecreeps:earthGem", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.mininggem, 0, new ModelResourceLocation("morecreeps:miningGem", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.healinggem, 0, new ModelResourceLocation("morecreeps:healingGem", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.skygem, 0, new ModelResourceLocation("morecreeps:skyGem", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.gemsword, 0, new ModelResourceLocation("morecreeps:gemSword", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.moopsworm, 0, new ModelResourceLocation("morecreeps:moopsWorm", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.cavemanclub, 0, new ModelResourceLocation("morecreeps:cavemanClub", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(MoreCreepsReboot.popsicle, 0, new ModelResourceLocation("morecreeps:popsicle", "inventory"));
	}
	
	public void shrinkBlast(World world, Entity entity, Random rand)
	{
        for (int i = 0; i < 8; i++)
        {
            byte byte0 = 7;

            if (rand.nextInt(4) == 0)
            {
                byte0 = 11;
            }
            CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, MoreCreepsReboot.partShrink, 0.5F, 0);
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }
	}
	public void shrinkSmoke(World world, Entity entity)
	{
        for (int k = 0; k < 8; k++)
        {
            CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, MoreCreepsReboot.partShrink, 0.25F, 0);
            creepsfxsmoke.renderDistanceWeight = 30D;
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }
	}
	
	public void rocketGoBoom(World world, Entity entity, Random rand)
	{
        for (int i = 0; i < 20; i++)
        {
            Item j = MoreCreepsReboot.partYellow;

            if (rand.nextInt(3) == 0)
            {
                j = MoreCreepsReboot.partRed;
            }

            CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, j, 1.0F, 0F);
            creepsfxsmoke.renderDistanceWeight = 30D;
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }
	}
	
	public void rocketSmoke(World world, Entity entity, Item item)
	{
        CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, item, 13, 3F);
        creepsfxsmoke.renderDistanceWeight = 15D;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        CREEPSFxSmoke creepsfxsmoke1 = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, item, 14, 3F);
        creepsfxsmoke1.renderDistanceWeight = 15D;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke1);
        CREEPSFxSmoke creepsfxsmoke2 = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, item, 24, 3F);
        creepsfxsmoke2.renderDistanceWeight = 15D;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke2);
	}
	
	public void robotTedSmoke(World world, double posX, double posY, double posZ, int floattimer, float modelspeed)
	{
        for (int i = 0; i < 15; i++)
        {
            CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, posX - 0.40000000000000002D, (posY - 0.5D) + (double)(floattimer / 100), posZ, MoreCreepsReboot.partWhite, 13, 0.4F - (0.51F - modelspeed));
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }

        for (int j = 0; j < 15; j++)
        {
            CREEPSFxSmoke creepsfxsmoke1 = new CREEPSFxSmoke(world, posX + 0.40000000000000002D, (posY - 0.5D) + (double)(floattimer / 100), posZ, MoreCreepsReboot.partWhite, 13, 0.4F - (0.51F - modelspeed));
            creepsfxsmoke1.renderDistanceWeight = 15D;
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke1);
        }
	}
	
	public void barf(World world, EntityPlayer player)
	{
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 50; j++)
            {
                CREEPSFxBlood creepsfxblood = new CREEPSFxBlood(world, player.posX, player.posY + 0.60000000298023224D, player.posZ, MoreCreepsReboot.partBarf, 0.85F);
                creepsfxblood.motionX += d * 0.25D;
                creepsfxblood.motionZ += d1 * 0.25D;
                Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxblood);
            }
        }
	}
	public void blood(World world, double posx, double posy, double posz, boolean realBlood)
	{
        if (CREEPSConfig.Blood)
        {
            for (int i = 0; i < 30; i++)
            {
                CREEPSFxBlood creepsfxblood = new CREEPSFxBlood(world, posx, posy, posz, realBlood ? MoreCreepsReboot.partRed : MoreCreepsReboot.partWhite, 0.255F);
                Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxblood);
            }
        }
	}
	public void confettiB(World world, TrophyEntity trophy)
	{
		if(world.isRemote)
		{
	        for (int i = 1; i < 10; i++)
	        {
	            for (int j = 0; j < 10; j++)
	            {
	                CREEPSFxConfetti creepsfxconfetti = new CREEPSFxConfetti(world, trophy.posX + (double)(world.rand.nextFloat() * 8F - world.rand.nextFloat() * 8F), trophy.posY + (double)world.rand.nextInt(4) + 4D, trophy.posZ + (double)(world.rand.nextFloat() * 8F - world.rand.nextFloat() * 8F), Item.getItemById(world.rand.nextInt(99)));
	                Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxconfetti);
	            }
	        }
		}
	}
	
	public void dirt(World world, EntityPlayer player, Random random, int l, int i1, int k)
	{
        for (int j1 = 0; j1 < 15; j1++)
        {
            CREEPSFxDirt creepsfxdirt = new CREEPSFxDirt(world, (int)(player.posX + (double)l + random.nextGaussian() * 0.02D), (int)(player.posY + (double)k), (int)(player.posZ + (double)i1 + random.nextGaussian() * 0.02D), Item.getItemFromBlock(Blocks.dirt));
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxdirt);
        }
	}
	public void dirtDigBug(World world, DigBugEntity dbug, Random random, int k2)
	{
		CREEPSFxDirt creepsfxdirt2 = new CREEPSFxDirt(world, dbug.posX, dbug.posY + (double)k2, dbug.posZ, Item.getItemFromBlock(Blocks.dirt));
		Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxdirt2);
	}
	public void foam(World world, EntityPlayer player)
	{
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        for (int i = 0; i < 25; i++)
        {
            CREEPSFxFoam creepsfxfoam = new CREEPSFxFoam(world, player.posX + d * 0.20000000000000001D, player.posY * 0.80000001192092896D, player.posZ + d1 * 0.5D, MoreCreepsReboot.partWhite);
            creepsfxfoam.motionX += d * 1.3999999761581421D;
            creepsfxfoam.motionZ += d1 * 1.3999999761581421D;
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxfoam);
        }
	}
	public void foam2(World world, AtomEntity atom)
	{
        for (int i1 = 0; (float)i1 < atom.atomsize; i1++)
        {
            CREEPSFxAtoms creepsfxatoms = new CREEPSFxAtoms(world, atom.posX, atom.posY + (double)(int)(atom.atomsize / 3F), atom.posZ, Item.getItemById(atom.rand.nextInt(99) + 1), 0.3F);
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxatoms);
        }
	}
	public void foam3(World world, CavemanEntity player, int i, int j, int k)
	{
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        for (int i1 = 0; i1 < 25; i1++)
        {
        	CREEPSFxFoam creepsfxfoam = new CREEPSFxFoam(world, i, j, k, MoreCreepsReboot.partWhite);
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxfoam);
        }
	}
	public void smoke(World world, EntityPlayer player, Random random)
	{
        double d = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
        CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, ((player.posX + random.nextGaussian() * 0.25D) - random.nextGaussian() * 0.25D) + d * 1.0D, ((player.posY - 0.5D) + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D, ((player.posZ + random.nextGaussian() * 0.25D) - random.nextGaussian() * 0.25D) + d1 * 1.0D, MoreCreepsReboot.partBubble, 0.05F, 0.0F);
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
	}
	public void smokeHorseHead(World world, CREEPSEntityHorseHead horsehead, Random rand)
	{
		CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, horsehead.posX, (horsehead.posY - 0.5D) + rand.nextGaussian() * 0.20000000000000001D, horsehead.posZ, MoreCreepsReboot.instance.partWhite, 0.25F, 0.0F);
		Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
	}
	public void smoke2(World world, Entity entity, Random random)
	{
        for (int i = 0; i < 8; i++)
        {
            CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, MoreCreepsReboot.partBlack, 0.2F, 0.5F);
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }
	}
	public void smoke3(World world, Entity entity, Random random)
	{
        for (int i = 0; i < 5; i++)
        {
            CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, entity.posX, (entity.posY - 0.5D) + random.nextGaussian() * 0.20000000000000001D, entity.posZ, MoreCreepsReboot.partWhite, 0.65F, 0.0F);
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }
	}
	public void growParticle(World world, Entity entity)
	{
        for (int k = 0; k < 8; k++)
        {
            CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, MoreCreepsReboot.instance.partShrink, 0.25F, 0.0F);
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }
	}
	public void smokeRay(World world, Entity entity, byte b0)
	{
		CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, Items.egg, b0, 0.5F);
		 Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
	}
	public void shrinkParticle(World world, Entity entity)
	{
        for (int i = 0; i < 8; i++)
        {
            byte byte0 = 7;

            if (world.rand.nextInt(4) == 0)
            {
                byte0 = 11;
            }

            CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, MoreCreepsReboot.instance.partShrink, 0.5F, 0.0F);
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }
	}
	public void addChatMessage(String s)
	{
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(s));
	}
	public void playSoundEffectAtPlayer(World world, String s, float volume, float pitch)
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		world.playSoundEffect(player.posX, player.posY, player.posZ, s, volume, pitch);
	}
	public void bubble(World world, EntityLivingBase entity)
	{
        double d = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
        CREEPSFxBubbles creepsfxbubbles = new CREEPSFxBubbles(world, entity.posX + d * 0.5D, entity.posY + 0.75D, entity.posZ + d1 * 0.5D, MoreCreepsReboot.partBubble, 0.7F);
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxbubbles);
	}
	public void bubbleDoghouse(World world, EntityLivingBase entity)
	{
        double d = -MathHelper.sin((entity.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((entity.rotationYaw * (float)Math.PI) / 180F);
        CREEPSFxBubbles creepsfxbubbles = new CREEPSFxBubbles(world, entity.posX + d * 0.10000000000000001D, entity.posY + 2D, (entity.posZ - 0.75D) + d1 * 0.5D, MoreCreepsReboot.partWhite, 1.2F);
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxbubbles);
	}
	public void pee(World world, double posX, double posY, double posZ, float rotationYaw, float modelsize)
	{
        double d = -MathHelper.sin((rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((rotationYaw * (float)Math.PI) / 180F);

        for (int i = 0; i < 25; i++)
        {
            CREEPSFxPee creepsfxpee = new CREEPSFxPee(world, posX + d * 0.20000000000000001D, (posY + 0.75D) - (double)((1.0F - modelsize) * 0.8F), posZ + d1 * 0.20000000000000001D, Item.getItemFromBlock(Blocks.cobblestone));
            creepsfxpee.motionX += d * 0.23999999463558197D;
            creepsfxpee.motionZ += d1 * 0.23999999463558197D;
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxpee);
        }
	}
	public boolean isJumpKeyDown()
	{
		return Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode());
	}
}
