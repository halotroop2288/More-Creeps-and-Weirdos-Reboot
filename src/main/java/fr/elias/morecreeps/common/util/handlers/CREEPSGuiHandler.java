package fr.elias.morecreeps.common.util.handlers;

import fr.elias.morecreeps.client.gui.GuineaPigTrainingGUI;
import fr.elias.morecreeps.client.gui.HotdogTrainingGUI;
import fr.elias.morecreeps.client.gui.SneakySalGUI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import fr.elias.morecreeps.common.entity.GuineaPigEntity;
import fr.elias.morecreeps.common.entity.HotdogEntity;
import fr.elias.morecreeps.common.entity.SneakySalEntity;

public class CREEPSGuiHandler  implements IGuiHandler
{
	private HotdogEntity hotdog;
	private GuineaPigEntity guineapig;
	private SneakySalEntity sneakysal;

    @Override
    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) 
    {
    	if (ID == 1)
    	{
    		return new GuineaPigTrainingGUI(guineapig);
    	}
    	
    	if (ID == 2)
    	{
    		return new SneakySalGUI(sneakysal, null);
    	}
    	if (ID == 3)
    	{
    		return new HotdogTrainingGUI(hotdog, new StringTextComponent(hotdog.getName() + "'s Training"));
    	}
    	
    	else
            return null;
    }

    @Override
    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
    {
           
    	if (ID == 1)
    	{
    		// Guinea Pig Stats
    		return new GuineaPigTrainingGUI(guineapig);
    	}
    	
    	if (ID == 2)
    	{
    		// Sneaky Sal Trading GUI
    		return new SneakySalGUI(sneakysal, null);
    	}
    	
    	if (ID == 3)
    	{
    		// HotDog Stats
    		return new HotdogTrainingGUI(hotdog);
    	}

            return null;
    }
}