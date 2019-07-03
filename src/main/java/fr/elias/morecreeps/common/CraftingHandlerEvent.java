package fr.elias.morecreeps.common;

import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.entity.TrophyEntity;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class CraftingHandlerEvent {
    @SubscribeEvent
    public void takenFromCrafting(PlayerEvent.ItemCraftedEvent event)
    {
        ItemStack itemstack = event.getCrafting();
        PlayerEntity player = event.getPlayer();
        if (itemstack.getItem() == ItemList.frisbee)
        {
            player.addStat(ModAdvancementList.frisbee, 1); // Why does this work now?
            event.getPlayer().world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT,
                    SoundCategory.MASTER, 1.0F, 1.0F);
        }

        if (itemstack.getItem() == ItemList.rocket)
        {
            player.addStat(ModAdvancementList.rocket, 1); // Why does this work now?
            event.getPlayer().world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT,
                    SoundCategory.MASTER, 1.0F, 1.0F);
        }

        if (itemstack.getItem() == ItemList.pet_radio)
        {
            player.addStat(ModAdvancementList.radio, 1); // Why does this work now?
            event.getPlayer().world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT,
                    SoundCategory.MASTER, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event)
    {
        ItemStack itemstack = event.getOriginalEntity().getItem();
        PlayerEntity player = event.getPlayer();
        boolean flag = false;
        if (itemstack.getItem() == ItemList.money)
        {
            NonNullList<ItemStack> aitemstack = player.inventory.mainInventory;// Did I do this right??
            int i = 0;

            for (int k = 0; k < aitemstack.size(); k++)
            {
                ItemStack itemstack1 = aitemstack.get(k);// Did I do this right??

                if (itemstack1 != null && itemstack1.getItem() == ItemList.money)
                {
                    itemstack1.setCount(itemstack1.getCount() + i);
                }
            }

            if (i > 99) {
                flag = true;
                // player.addStat(MoreCreepsReboot.achieve100bucks, 1);
            }

            if (i > 499) {
                flag = true;
                // player.addStat(MoreCreepsReboot.achieve500bucks, 1);
            }

            if (i > 999) {
                flag = true;
                // player.addStat(MoreCreepsReboot.achieve1000bucks, 1);
            }
        }
        if (itemstack.getItem() == ItemList.ram_16k)
        {
            NonNullList<ItemStack> aitemstack1 = player.inventory.mainInventory; // Did I do this right??
            int j = 0;

            for (int l = 0; l < aitemstack1.size(); l++)
            {
                ItemStack itemstack2 = aitemstack1.get(l); // Did I do this right??

                if (itemstack2 != null && itemstack2.getItem() == ItemList.ram_16k)
                {
                    itemstack2.setCount(itemstack2.getCount() + j);
                }
            }

            if (j >= 8)
            {
                flag = true;
                // player.addStat(MoreCreepsReboot.achieveram128, 1);
            }

            if (j >= 32)
            {
                flag = true;
                // player.addStat(MoreCreepsReboot.achieveram512, 1);
            }

            if (j >= 64)
            {
                flag = true;
                // player.addStat(MoreCreepsReboot.achieveram1024, 1);
            }
        }
        if(flag)
        {
        	player.world.playSound(player, player.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
        	MoreCreepsReboot.proxy.confettiA(player, player.world);
        }
	}
}
