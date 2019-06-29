package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemBattery extends Item
{

    public ItemBattery()
    {
    	super(new Item.Properties().group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, Entity entityplayer)
    {
        world.playSound(null, null, SoundEvents.ENTITY_GENERIC_BURN, SoundCategory.PLAYERS, 1.0F, 1.0F); // TODO Register custom sound events, change this to "spark"
        entityplayer.attackEntityFrom(DamageSource.MAGIC, 1.0F);
        return itemstack;
    }
}
