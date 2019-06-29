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
import fr.elias.morecreeps.common.entity.ArmyGuyEntity;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemArmyGem extends Item
{
    public static Random random = new Random();

    public ItemArmyGem()
    {
        super(new Item.Properties().maxStackSize(1).group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        playerentity.swingArm(Hand.MAIN_HAND);
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.ARMY_GEM, SoundCategory.PLAYERS, 1.0F, 1.0F);
        double d = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
        ArmyGuyEntity creepsentityarmyguy = new ArmyGuyEntity(world);
        creepsentityarmyguy.setLocationAndAngles(playerentity.posX + d * 1.0D, playerentity.posY + 1.0D, playerentity.posZ + d1 * 1.0D, playerentity.rotationYaw, 0.0F);
        if(!world.isRemote)
        {
            creepsentityarmyguy.loyal = true;
            System.out.println("[ITEM] ArmyGem is Loyal: " + creepsentityarmyguy.loyal);
            world.addEntity(creepsentityarmyguy);
        }
        itemstack.damageItem(2, playerentity, null); // onBroken ??
        return itemstack;
    }
}
