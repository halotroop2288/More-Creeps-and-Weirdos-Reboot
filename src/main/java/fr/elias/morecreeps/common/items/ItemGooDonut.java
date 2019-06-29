package fr.elias.morecreeps.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.GooDonutEntity;

public class ItemGooDonut extends Item
{
    public ItemGooDonut()
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

        world.playSound(playerentity, playerentity.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            world.addEntity(new GooDonutEntity(world, playerentity));
        }

        return itemstack;
    }
}
