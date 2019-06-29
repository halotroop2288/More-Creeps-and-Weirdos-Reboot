package fr.elias.morecreeps.common.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.BigBabyEntity;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemBabyJarEmpty extends Item
{
    private boolean messagegiven;
	public ItemBabyJarEmpty()
    {
        super(new Item.Properties().group(MoreCreepsReboot.creepsTab));
        messagegiven = false;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity entityplayer)
    {
        if (!messagegiven)
        {
            boolean flag = false;
            @SuppressWarnings("rawtypes")
			List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.getBoundingBox().expand(8D, 8D, 8D));

            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);

                if (entity instanceof BigBabyEntity)
                {
                    flag = true;
                }
            }

            if (!flag)
            {
                world.playSound(entityplayer, entityplayer.getPosition(), SoundsHandler.BABY_JAR_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
                messagegiven = true;
            }
        }

        return itemstack;
    }
}
// 4 items down. This is going quickly now. :D