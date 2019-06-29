package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

@SuppressWarnings("unused")
public class ItemGemSword extends SwordItem
{
    public static Random random = new Random();

    public ItemGemSword()
    {
        super(ItemTier.DIAMOND, 256, 25, new Item.Properties().group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity entity)
    {
        return 25;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        return itemstack;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemstack, LivingEntity livingentity, LivingEntity livingentity1)
    {
        itemstack.damageItem(1, livingentity1, null); // onBreak ??
        return true;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, LivingEntity livingentity)
    {
        itemstack.damageItem(2, livingentity, null); // onBreak ??
        return true;
    }

    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canHarvestBlock(Block block)
    {
        return true;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack itemstack, World world, LivingEntity livingentity, int i, boolean flag)
    {
    	PlayerEntity player = (PlayerEntity)livingentity;
        super.onUsingTick(itemstack, livingentity, i);

        if (flag)
        {
            if (player.isSwingInProgress)
            {
                world.playSound(player, player.getPosition(), SoundsHandler.GEM_SWORD, SoundCategory.PLAYERS, 0.6F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            }

            MoreCreepsReboot.proxy.smoke(world, player, random);
        }
    }
}
