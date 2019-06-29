package fr.elias.morecreeps.common.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.AtomEntity;

public class ItemAtom extends Item
{
    public ItemAtom()
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

        world.playSound(playerentity, playerentity.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / (Item.random.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            AtomEntity creepsentityatom = new AtomEntity(world);
            creepsentityatom.setLocationAndAngles(playerentity.posX + 3D, playerentity.posY, playerentity.posZ + 3D, playerentity.rotationYaw, 0.0F);
            double d = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
            creepsentityatom.moveForward = (float) (1.7D * d * (double)MathHelper.cos((playerentity.rotationPitch / 180F) * (float)Math.PI));
            creepsentityatom.moveVertical = (float) (-1.8D * (double)MathHelper.sin((playerentity.rotationPitch / 180F) * (float)Math.PI));
            creepsentityatom.moveStrafing = (float) (1.7D * d1 * (double)MathHelper.cos((playerentity.rotationPitch / 180F) * (float)Math.PI));
            creepsentityatom.setPosition(playerentity.posX + d * 0.80000000000000004D, playerentity.posY, playerentity.posZ + d1 * 0.80000000000000004D);
            creepsentityatom.prevPosX = creepsentityatom.posX;
            creepsentityatom.prevPosY = creepsentityatom.posY;
            creepsentityatom.prevPosZ = creepsentityatom.posZ;
            world.addEntity(creepsentityatom);
        }

        return itemstack;
    }
}
// 3 Items down. :D