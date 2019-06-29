package fr.elias.morecreeps.common;

import fr.elias.morecreeps.common.entity.TrophyEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class CraftingHandlerEvent
{
	@SubscribeEvent
	public void takenFromCrafting(PlayerEvent.ItemCraftedEvent event)
	{
		ItemStack itemstack = event.crafting;
		EntityPlayer player = event.player;
        if (itemstack.getItem() == MoreCreepsReboot.frisbee)
        {
            player.addStat(MoreCreepsReboot.achievefrisbee, 1);
            event.player.worldObj.playSoundAtEntity(player, "morecreeps:creepsounds.achievement", 1.0F, 1.0F);
        }

        if (itemstack.getItem() == MoreCreepsReboot.rocket)
        {
            player.addStat(MoreCreepsReboot.achieverocket, 1);
            event.player.worldObj.playSoundAtEntity(player, "morecreeps:creepsounds.achievement", 1.0F, 1.0F);
        }

        if (itemstack.getItem() == MoreCreepsReboot.guineapigradio)
        {
            player.addStat(MoreCreepsReboot.achieveradio, 1);
            event.player.worldObj.playSoundAtEntity(player, "morecreeps:creepsounds.achievement", 1.0F, 1.0F);
        }
	}
	
	@SubscribeEvent
	public void onItemPickup(PlayerEvent.ItemPickupEvent event)
	{
		ItemStack itemstack = event.pickedUp.getEntityItem();
		EntityPlayer player = event.player;
		boolean flag = false;
        if (itemstack.getItem() == MoreCreepsReboot.money)
        {
            ItemStack aitemstack[] = player.inventory.mainInventory;
            int i = 0;

            for (int k = 0; k < aitemstack.length; k++)
            {
                ItemStack itemstack1 = aitemstack[k];

                if (itemstack1 != null && itemstack1.getItem() == MoreCreepsReboot.money)
                {
                    i += itemstack1.stackSize;
                }
            }

            if (i > 99)
            {
                flag = true;
                player.addStat(MoreCreepsReboot.achieve100bucks, 1);
            }

            if (i > 499)
            {
                flag = true;
                player.addStat(MoreCreepsReboot.achieve500bucks, 1);
            }

            if (i > 999)
            {
                flag = true;
                player.addStat(MoreCreepsReboot.achieve1000bucks, 1);
            }
        }
        if (itemstack.getItem() == MoreCreepsReboot.ram16k)
        {
            ItemStack aitemstack1[] = player.inventory.mainInventory;
            int j = 0;

            for (int l = 0; l < aitemstack1.length; l++)
            {
                ItemStack itemstack2 = aitemstack1[l];

                if (itemstack2 != null && itemstack2.getItem() == MoreCreepsReboot.ram16k)
                {
                    j += itemstack2.stackSize;
                }
            }

            if (j >= 8)
            {
                flag = true;
                player.addStat(MoreCreepsReboot.achieveram128, 1);
            }

            if (j >= 32)
            {
                flag = true;
                player.addStat(MoreCreepsReboot.achieveram512, 1);
            }

            if (j >= 64)
            {
                flag = true;
                player.addStat(MoreCreepsReboot.achieveram1024, 1);
            }
        }
        if(flag)
        {
        	player.worldObj.playSoundAtEntity(player, "morecreeps:creepsounds.achievement", 1.0F, 1.0F);
        	MoreCreepsReboot.proxy.confettiA(player, player.worldObj);
        }
	}
}
