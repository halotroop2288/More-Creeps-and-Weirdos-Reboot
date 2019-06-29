package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.EvilEggEntity;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemEvilEgg extends Item
{
    public static Random random = new Random();

    public ItemEvilEgg()
    {
        super(new Item.Properties().maxStackSize(44).group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        itemstack.setCount(itemstack.getCount() - 1);
        playerentity.swingArm(Hand.MAIN_HAND);

        if (itemstack.getCount() < 1)
        {
            playerentity.inventory.setInventorySlotContents(playerentity.inventory.currentItem, null);
            itemstack.setCount(0);
        }

        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.EVIL_EGG_CLUCK, SoundCategory.PLAYERS, 1.0F, 1.0F);

        if (!world.isRemote)
        {
            world.addEntity(new EvilEggEntity(world, playerentity));
        }

        return itemstack;
    }
}
