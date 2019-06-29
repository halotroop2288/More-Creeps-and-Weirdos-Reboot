package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.GrowEntity;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemGrowRay extends Item
{
    public static Random rand = new Random();

    public ItemGrowRay()
    {
        super(new Item.Properties().maxStackSize(1).maxDamage(64).group(MoreCreepsReboot.creepsTab));
    }
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @SuppressWarnings("unused")
	public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.GROW_RAY, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            double d = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
            double d1 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
            double d2 = 0.0D;
            double d3 = 0.0D;
            double d4 = 0.012999999999999999D;
            double d5 = 4D;
            GrowEntity creepsentitygrow = new GrowEntity(world, playerentity, 0.0F);

            if (creepsentitygrow != null)
            {
                itemstack.damageItem(1, playerentity, null); // onBroken ??
                world.addEntity(creepsentitygrow);
            }
        }

        return itemstack;
    }
}
