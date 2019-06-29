package fr.elias.morecreeps.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemLimbs extends Item
{
    public ItemLimbs()
    {
        super(new Item.Properties().maxStackSize(24).group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.BARF, SoundCategory.PLAYERS, 1.0F, 1.0F);
        itemstack.setCount(itemstack.getCount() - 1);
        playerentity.attackEntityFrom(DamageSource.STARVE, 1F);
        if(world.isRemote)
        {
        	MoreCreepsReboot.proxy.barf(world, playerentity);
        }

        return itemstack;
    }
}
