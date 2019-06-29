package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemBandAid extends Item
{
    private int healAmount;

    public ItemBandAid()
    {
        super(new Item.Properties().maxStackSize(24).group(MoreCreepsReboot.creepsTab));
        healAmount = 4;
    }
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        if (playerentity.getHealth() < 20)
        {
            world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.BAND_AID, SoundCategory.PLAYERS, 1.0F, 1.0F);
            itemstack.setCount(itemstack.getCount() - 1);
            playerentity.heal(healAmount);
        }

        return itemstack;
    }
}
