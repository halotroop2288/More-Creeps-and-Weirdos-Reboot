package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemGun extends Item
{
    public static Random rand = new Random();

    public ItemGun()
    {
        super(new Item.Properties().maxStackSize(1).maxDamage(128).group(MoreCreepsReboot.creepsTab));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @SuppressWarnings("unused")
	public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity entityplayer)
    {
        world.playSound(entityplayer, entityplayer.getPosition(), SoundsHandler.BULLET, SoundCategory.PLAYERS, 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            double d = -MathHelper.sin((entityplayer.rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((entityplayer.rotationYaw * (float)Math.PI) / 180F);
            double d2 = 0.0D;
            double d3 = 0.0D;
            double d4 = 0.012999999999999999D;
            double d5 = 4D;
            CREEPSEntityBullet creepsentitybullet = new CREEPSEntityBullet(world);

            if (creepsentitybullet != null)
            {
                itemstack.damageItem(2, entityplayer, null); // onBroken ??
                world.addEntity(creepsentitybullet);
            }
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
