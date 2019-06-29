package fr.elias.morecreeps.common.items;

import java.util.Random;

import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemMobilePhone extends Item
{
	public static Random random = new Random();
	
    public ItemMobilePhone()
    {
        super(new Item.Properties().maxStackSize(1));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        playerentity.swingArm(Hand.MAIN_HAND);
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.MOBILE, SoundCategory.PLAYERS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        return itemstack;
    }
}
