package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemLifeGem extends Item
{
    public ItemLifeGem()
    {
        super(new Item.Properties().maxStackSize(16).group(MoreCreepsReboot.creepsTab));
    }
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, PlayerEntity
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        playerentity.swingArm(Hand.MAIN_HAND);
        return itemstack;
    }
}
