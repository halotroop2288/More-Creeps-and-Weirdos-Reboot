package fr.elias.morecreeps.common.items;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.GuineaPigEntity;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemPetRadio extends Item
{
    public boolean pickup;
    public static Random rand = new Random();

    public ItemPetRadio()
    {
        super();
        maxStackSize = 1;
        pickup = false;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity entityplayer)
    {
        if (entityplayer.isSneaking())
        {
            return itemstack;
        }

        if (entityplayer.getRidingEntity() == null)
        {
            if (pickup)
            {
                pickup = false;
                world.playSoundAtEntity(entityplayer, entityplayer.getPosition(), SoundsHandler.GUINEA_PIG_MOUNT, SoundCategory.PLAYERS, 1.0F, 1.0F);

                for (int i = 0; i < 21; i++)
                {
                    Object obj = entityplayer;
                    int k;

                    for (k = 0; ((Entity)obj).riddenByEntity != null && k < 20; k++)
                    {
                        obj = ((Entity)obj).riddenByEntity;
                    }

                    if (k < 20)
                    {
                        ((Entity)obj).fallDistance = -25F;
                        ((Entity)obj).removePassengers();
                    }
                }
            }
            else
            {
                pickup = true;
                world.playSound(entityplayer, "morecreeps:ggpigradio", 1.0F, 1.0F);
                List list = world.getEntitiesWithinAABB(fr.elias.morecreeps.common.entity.GuineaPigEntity.class, new AxisAlignedBB(entityplayer.posX, entityplayer.posY, entityplayer.posZ, entityplayer.posX + 1.0D, entityplayer.posY + 1.0D, entityplayer.posZ + 1.0D).expand(150D, 150D, 150D));

                for (int j = 0; j < list.size(); j++)
                {
                    Entity entity = (Entity)list.get(j);

                    if ((entity instanceof GuineaPigEntity) && ((GuineaPigEntity)entity).wanderstate == 0 && ((GuineaPigEntity)entity).tamed)
                    {
                        Object obj1 = entityplayer;

                        if (entity.getRidingEntity() != obj1 && entity.getRidingEntity() == null)
                        {
                            int l;

                            for (l = 0; ((Entity)obj1).riddenByEntity != null && l < 20; l++)
                            {
                                obj1 = ((Entity)obj1).riddenByEntity;
                            }

                            if (l < 20)
                            {
                                entity.rotationYaw = ((Entity)obj1).rotationYaw;
                                entity.addPassenger((Entity)obj1);
                                world.playSoundAtEntity(entityplayer, "morecreeps:ggpigmount", 1.0F, 1.0F);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            MoreCreepsReboot.proxy.addChatMessage("Get off that creature before using the Guinea Pig Radio");
        }

        return itemstack;
    }
}
