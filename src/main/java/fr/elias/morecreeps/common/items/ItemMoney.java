package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.advancements.ModAdvancementList;
import fr.elias.morecreeps.common.entity.MoneyEntity;
import fr.elias.morecreeps.common.entity.TrophyEntity;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemMoney extends Item
{
    public static Random rand = new Random();

    public ItemMoney()
    {
        super(new Item.Properties().group(MoreCreepsReboot.creepsTab).maxStackSize(50));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity entityplayer)
    {
        checkAchievements(world, entityplayer);
        itemstack.setCount(itemstack.getCount() - 1);
        world.playSound(entityplayer, "random.bow", 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            world.addEntity(new MoneyEntity(world, entityplayer));
        }

        return itemstack;
    }

    public void checkAchievements(World world, PlayerEntity entityplayer)
    {
        Object obj = null;
        NonNullList<ItemStack> aitemstack = entityplayer.inventory.mainInventory;
        int i = 0;

        for (int j = 0; j < aitemstack.size(); j++)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack != null && itemstack.getItem() == ItemList.money)
            {
                i += itemstack.getCount();
            }
        }

        boolean flag = false;

        if (i > 99)
        {
            flag = true;
            MoreCreepsReboot.proxy.confettiA(entityplayer, world);
            entityplayer.addStat(ModAdvancementList.one_hundred_bucks, 1);
        }

        if (i > 499)
        {
            flag = true;
            MoreCreepsReboot.proxy.confettiA(entityplayer, world);
            entityplayer.addStat(ModAdvancementList.five_hundred_bucks, 1);
        }

        if (i > 999)
        {
            flag = true;
            MoreCreepsReboot.proxy.confettiA(entityplayer, world);
            entityplayer.addStat(ModAdvancementList.one_thousand_bucks, 1);
        }

        if (flag)
        {
            world.playSound(entityplayer, entityplayer.getPosition(), SoundsHandler.ACHIEVEMENT, SoundCategory.MASTER, 1.0F, 1.0F);
        }
    }
}
