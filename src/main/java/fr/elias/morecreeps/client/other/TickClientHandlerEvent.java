package fr.elias.morecreeps.client.other;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class TickClientHandlerEvent
{
	final Minecraft minecraft = Minecraft.getInstance();
	public boolean creepsLoaded = false;
    public static final String welcome[] =
    { 																	// TODO Add localization support
        "Now, go out there and have some fun!",
        "Don't let those stinky Floobs push you around!",
        "Give a diamond to a level 25 HotDog for a special reward!",
        "Urinating Bums can help with landscaping. Try one today!",
        "You're doing something right!", "Watch out for grumpy G's!",
        "Guinea Pigs make nice pets.",
        "Bring a lost Kid back to a Lolliman for a nice reward.",
        "Robot Ted thinks Robot Todd is a dirty chicken wing.",
        "Sneaky Sal changes his prices. Check back for bargains.",
        "Power your HotDog with redstone for a fire attack!",
        "You want money? Punch a Lawyer From Hell!",
        "Equip your HotDogs with Redstone for fire attacks!",
        "Guinea Pigs eat Wheat and Apples.",
        "A Floob Ship will spit out Floobs until it is destroyed.",
        "Drop a BubbleScum 100 blocks for the MERCILESS advancement!",
        "Throw a BubbleScum down a DigBug hole for a cookie fountain!",
        "Feed lots of cake to a Hunchback and he will stay loyal.",
        "The longer you ride a RocketPony, the more tame it will be.",
        "Visit Sneaky Sal for those hard to find items.",
        "Hitting a Caveman will turn them evil!"
    };
	@SubscribeEvent
	public void moreCreepsWelcomeMessage(PlayerEvent.PlayerLoggedInEvent event, World world)
	{
		PlayerEntity player = event.getPlayer();
		World world1 = player.world;
		if(!creepsLoaded)
		{
			world1.playSound(player, player.chasingPosX, player.posY, player.posZ, SoundsHandler.WELCOME, SoundCategory.VOICE, 1.0F, 1.0F);
            String randomMessage = welcome[world1.rand.nextInt(welcome.length)];
            player.sendMessage(new StringTextComponent("\2476More Creeps and Weirdos Reboot \247ev1 (unofficial) \2476loaded."));
			player.sendMessage(new StringTextComponent(randomMessage));
			creepsLoaded = true;
		}
	}
}
