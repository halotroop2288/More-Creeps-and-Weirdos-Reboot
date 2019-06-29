package fr.elias.morecreeps.common.items;

import java.util.Random;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemCavemanClub extends SwordItem
{
    public static Random random = new Random();
    public int drumsound;

    public ItemCavemanClub()
    {
        super(ItemTier.WOOD, 0, 64, new Item.Properties().maxDamage(64).group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity entity)
    {
        return 4;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        return itemstack;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        return 3F;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemstack, LivingEntity entityliving, LivingEntity entityliving1)
    {
        itemstack.damageItem(1, entityliving1, null); // onBroken ??
        return true;
    }

    public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, LivingEntity entityliving)
    {
        itemstack.damageItem(2, entityliving, null); // onBroken ??
        return true;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canHarvestBlock(Block block)
    {
        return false;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack itemstack, World world, ItemEntity entity, PlayerEntity player, int i, boolean flag)
    {
        super.onEntityItemUpdate(itemstack, entity);

        if (flag && drumsound-- < 0)
        {
            drumsound = random.nextInt(200) + 150;
            world.playSound(player, player.getPosition(), SoundsHandler.CAVE_DRUMS, SoundCategory.PLAYERS, 0.65F, 0.9F);
        }
    }
}
