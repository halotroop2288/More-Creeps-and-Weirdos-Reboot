package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemLolly extends Item
{
    private int healAmount;

    public ItemLolly()
    {
        super(new Item.Properties().maxStackSize(32).group(MoreCreepsReboot.creepsTab));
        healAmount = 2;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity entityplayer)
    {
        world.playSound(entityplayer, entityplayer.getPosition(), SoundsHandler.LOLLY, SoundCategory.PLAYERS, 1.0F, 1.0F);
        itemstack.setCount(itemstack.getCount() - 1);
        entityplayer.heal(healAmount);
        return itemstack;
    }
}
