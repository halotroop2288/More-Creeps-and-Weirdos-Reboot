package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.MoneyEntity;
import fr.elias.morecreeps.common.entity.TrophyEntity;

public class ItemMoney extends Item
{
    public static Random rand = new Random();

    public ItemMoney()
    {
        super();
        maxStackSize = 50;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        checkAchievements(world, entityplayer);
        itemstack.stackSize--;
        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new MoneyEntity(world, entityplayer));
        }

        return itemstack;
    }

    public void checkAchievements(World world, EntityPlayer entityplayer)
    {
        Object obj = null;
        ItemStack aitemstack[] = entityplayer.inventory.mainInventory;
        int i = 0;

        for (int j = 0; j < aitemstack.length; j++)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack != null && itemstack.getItem() == MoreCreepsReboot.money)
            {
                i += itemstack.stackSize;
            }
        }

        boolean flag = false;

        if (i > 99)
        {
            flag = true;
            MoreCreepsReboot.proxy.confettiA(entityplayer, world);
            entityplayer.addStat(MoreCreepsReboot.achieve100bucks, 1);
        }

        if (i > 499)
        {
            flag = true;
            MoreCreepsReboot.proxy.confettiA(entityplayer, world);
            entityplayer.addStat(MoreCreepsReboot.achieve500bucks, 1);
        }

        if (i > 999)
        {
            flag = true;
            MoreCreepsReboot.proxy.confettiA(entityplayer, world);
            entityplayer.addStat(MoreCreepsReboot.achieve1000bucks, 1);
        }

        if (flag)
        {
            world.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
        }
    }
}
