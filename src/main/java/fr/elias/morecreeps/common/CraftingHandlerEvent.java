package fr.elias.morecreeps.common;

import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.entity.TrophyEntity;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class CraftingHandlerEvent
{
	@SubscribeEvent
	public void takenFromCrafting(PlayerEvent.ItemCraftedEvent event)
	{
		ItemStack itemstack = event.getCrafting();
		PlayerEntity player = event.getPlayer();
        if (itemstack.getItem() == ItemList.frisbee)
        {
            player.addStat(ModAdvancementList.frisbee, 1);
            event.getPlayer().world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
        }

        if (itemstack.getItem() == ModAdvancementList.rocket)
        {
            player.addStat(ModAdvancementList.rocket, 1);
            event.getPlayer().world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
        }

        if (itemstack.getItem() == ModAdvancementList.guineapigradio)
        {
            player.addStat(ModAdvancementList.radio, 1);
            event.getPlayer().world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
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
