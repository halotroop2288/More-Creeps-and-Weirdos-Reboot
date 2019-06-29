package fr.elias.morecreeps.common.items;

import java.util.Random;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;

public class ItemArmSword extends SwordItem
{
    public static Random random = new Random();

	public ItemArmSword(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties properties) 
	{
		super(tier, attackDamageIn, attackSpeedIn, new Item.Properties().group(MoreCreepsReboot.creepsTab));
	}

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity entity)
    {
        return 5;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity entityplayer)
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
}
