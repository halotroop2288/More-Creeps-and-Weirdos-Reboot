package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.EntityFoam;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemExtinguisher extends Item
{
    public static Random rand = new Random();

    public ItemExtinguisher()
    {
        super(new Item.Properties().maxStackSize(1).maxDamage(1024).group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.EXTINGUISHER, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            EntityFoam creepsentityfoam = new EntityFoam(world, playerentity, 0.0F);

            if (creepsentityfoam != null)
            {
                itemstack.damageItem(1, playerentity, null); // onBroken ??
                world.addEntity(creepsentityfoam);
            }
        }

        if(world.isRemote)
        {
        	MoreCreepsReboot.proxy.foam(world, playerentity);
        }
        
        return itemstack;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }
}
