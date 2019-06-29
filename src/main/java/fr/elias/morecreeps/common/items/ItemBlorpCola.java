package fr.elias.morecreeps.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemBlorpCola extends Item
{
    private int healAmount;

    public ItemBlorpCola()
    {
        super(new Item.Properties().maxStackSize(24).group(MoreCreepsReboot.creepsTab));
        healAmount = 2;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        playerentity.swingArm(Hand.MAIN_HAND);
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.BLORP_COLA, SoundCategory.PLAYERS, 1.0F, 1.0F);
        itemstack.setCount(itemstack.getCount() - 1);

        if (MoreCreepsReboot.colacount++ >= 10)
        {
        	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            // TODO Unlock advancement "chug cola"
            MoreCreepsReboot.proxy.confettiA(playerentity, world);
        }

        playerentity.heal(healAmount);
        return itemstack;
    }
}
