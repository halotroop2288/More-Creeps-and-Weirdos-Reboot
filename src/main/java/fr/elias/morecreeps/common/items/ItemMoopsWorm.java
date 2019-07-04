package fr.elias.morecreeps.common.items;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ArmyGuyEntity;

public class ItemMoopsWorm extends Item
{
    public static Random random = new Random();

    public ItemMoopsWorm()
    {
        super();
        maxStackSize = 55;
        setMaxDamage(16);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity entityplayer)
    {
        entityplayer.swingArm(Hand.MAIN_HAND);
        world.playSound(entityplayer, "morecreeps:armygem", 1.0F, 1.0F);
        double d = -MathHelper.sin((entityplayer.rotationYaw * (float)Math.PI) / 180F);
        double d1 = MathHelper.cos((entityplayer.rotationYaw * (float)Math.PI) / 180F);
        ArmyGuyEntity creepsentityarmyguy = new ArmyGuyEntity(world);
        creepsentityarmyguy.setLocationAndAngles(entityplayer.posX + d * 1.0D, entityplayer.posY + 1.0D, entityplayer.posZ + d1 * 1.0D, entityplayer.rotationYaw, 0.0F);
        creepsentityarmyguy.loyal = true;
        world.spawnEntityInWorld(creepsentityarmyguy);
        itemstack.damageItem(2, entityplayer);
        return itemstack;
    }
}
