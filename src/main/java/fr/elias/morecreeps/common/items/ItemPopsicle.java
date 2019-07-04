package fr.elias.morecreeps.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPopsicle extends Item
{
    private int healAmount;

    public ItemPopsicle()
    {
        super();
        healAmount = 4;
        maxStackSize = 16;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.EAT;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity entityplayer)
    {
        entityplayer.swingItem();
        world.playSoundAtEntity(entityplayer, "morecreeps:lick", 1.0F, 1.0F);
        itemstack.stackSize--;
        entityplayer.heal(healAmount);
        return itemstack;
    }
}
