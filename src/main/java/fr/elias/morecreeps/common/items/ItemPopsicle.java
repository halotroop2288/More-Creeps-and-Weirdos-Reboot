package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemPopsicle extends Item
{
    private int healAmount;

    public ItemPopsicle()
    {
        super(new Item.Properties().maxStackSize(16));
        healAmount = 4;
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
        entityplayer.swingArm(Hand.MAIN_HAND);
        world.playSound(entityplayer, entityplayer.getPosition(), SoundsHandler.LICK, SoundCategory.PLAYERS, 1.0F, 1.0F);
        itemstack.setCount(itemstack.getCount() - 1);
        entityplayer.heal(healAmount);
        return itemstack;
    }
}
