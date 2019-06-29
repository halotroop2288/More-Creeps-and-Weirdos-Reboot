package fr.elias.morecreeps.common.items;

import java.util.Random;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemHealingGem extends Item
{
    public static Random rand = new Random();
    private int healAmount;

    public ItemHealingGem()
    {
        super(new Item.Properties().maxStackSize(1).maxDamage(16).group(MoreCreepsReboot.creepsTab));
        healAmount = 5;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @SuppressWarnings("unused")
	public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        if (playerentity.getHealth() < 20)
        {
            world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HEALING_GEM, SoundCategory.PLAYERS, 1.0F, 1.0F);
            itemstack.damageItem(1, playerentity, null); // onBroken ??
            playerentity.swingArm(Hand.MAIN_HAND);

            for (int i = 0; i < 20; i++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                double d3 = -MathHelper.sin((playerentity.rotationYaw * (float)Math.PI) / 180F);
                double d4 = MathHelper.cos((playerentity.rotationYaw * (float)Math.PI) / 180F);
                world.addParticle(ParticleTypes.EXPLOSION, (playerentity.posX + rand.nextGaussian() * 0.5D) - rand.nextGaussian() * 0.5D, ((playerentity.posY - 0.5D) + rand.nextGaussian() * 0.5D) - rand.nextGaussian() * 0.5D, (playerentity.posZ + rand.nextGaussian() * 0.5D) - rand.nextGaussian() * 0.5D, d, d1, d2);
                world.addParticle(ParticleTypes.HEART, (playerentity.posX + rand.nextGaussian() * 0.5D) - rand.nextGaussian() * 0.5D, ((playerentity.posY - 0.5D) + rand.nextGaussian() * 0.5D) - rand.nextGaussian() * 0.5D, (playerentity.posZ + rand.nextGaussian() * 0.5D) - rand.nextGaussian() * 0.5D, d, d1, d2);
            }

            playerentity.heal(healAmount);
        }

        return itemstack;
    }
}
