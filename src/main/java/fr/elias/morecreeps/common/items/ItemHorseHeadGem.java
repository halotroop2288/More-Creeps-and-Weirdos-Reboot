package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemHorseHeadGem extends Item
{
    public static Random random = new Random();

    public ItemHorseHeadGem()
    {
        super(new Item.Properties().maxStackSize(16).group(MoreCreepsReboot.creepsTab));
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

        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HORSE_HEAD_GEM, SoundCategory.PLAYERS, 1.0F, 1.0F);

        if (!world.isRemote)
        {
            double d = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
            CREEPSEntityHorseHead creepsentityhorsehead = new CREEPSEntityHorseHead(world);
            creepsentityhorsehead.setLocationAndAngles(playerentity.posX + d * 1.0D, playerentity.posY + 1.0D, playerentity.posZ + d1 * 1.0D, playerentity.rotationYaw, 0.0F);
            world.addEntity(creepsentityhorsehead);
        }

        return itemstack;
    }
}
// We're down 600 errors! 11,995 to go!