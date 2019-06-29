package fr.elias.morecreeps.common.items;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.GuineaPigEntity;
import fr.elias.morecreeps.common.entity.HotdogEntity;
import fr.elias.morecreeps.common.entity.HunchbackEntity;
import fr.elias.morecreeps.common.entity.PreacherEntity;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemFireGem extends Item
{
    public static Random random = new Random();

    public ItemFireGem()
    {
        super(new Item.Properties().maxStackSize(1).maxDamage(64).group(MoreCreepsReboot.creepsTab));
    }
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @SuppressWarnings("rawtypes")
	public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.FIRE_GEM, SoundCategory.PLAYERS, 1.0F, 1.0F);
        itemstack.damageItem(1, playerentity, null); // onBroken ??
        playerentity.swingArm(Hand.MAIN_HAND);
        List list = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(playerentity.posX, playerentity.posY, playerentity.posZ, playerentity.posX + 1.0D, playerentity.posY + 1.0D, playerentity.posZ + 1.0D).expand(10D, 10D, 10D));

        for (int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);

            if (!(entity instanceof LivingEntity))
            {
                continue;
            }

            LivingEntity entityliving = (LivingEntity)entity;

            if ((entityliving instanceof HotdogEntity) || (entityliving instanceof HunchbackEntity) || (entityliving instanceof PlayerEntity) || (entityliving instanceof GuineaPigEntity) || (entityliving instanceof PreacherEntity))
            {
                continue;
            }

            if(world.isRemote)
            {
                for (int j = 0; j < 10; j++)
                {
                    double d = random.nextGaussian() * 0.02D;
                    double d1 = random.nextGaussian() * 0.02D;
                    double d2 = random.nextGaussian() * 0.02D;
                    world.addParticle(ParticleTypes.SMOKE, entityliving.posX + (double)(random.nextFloat() * 1.5F), entityliving.posY + 0.5D + (double)(random.nextFloat() * 2.5F), entityliving.posZ + (double)(random.nextFloat() * 1.5F), d, d1, d2);
                }
            }

            entityliving.attackEntityFrom(DamageSource.IN_FIRE, 2F);
            entityliving.moveVertical += 0.5D;
            entityliving.setFire(15);
        }

        return itemstack;
    }
}
