package fr.elias.morecreeps.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemDonut extends Item
{
    private int healAmount;

    public ItemDonut()
    {
        super(new Item.Properties().maxStackSize(32).group(MoreCreepsReboot.creepsTab));
        healAmount = 2;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        playerentity.swingArm(Hand.MAIN_HAND);
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.CHEW, SoundCategory.PLAYERS, 1.0F, 1.0F);
        itemstack.setCount(itemstack.getCount() - 1);
        playerentity.heal(healAmount);
        return itemstack;
    }
}
