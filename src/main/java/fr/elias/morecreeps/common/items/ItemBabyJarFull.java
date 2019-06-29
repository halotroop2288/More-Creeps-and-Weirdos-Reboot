package fr.elias.morecreeps.common.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.entity.SchlumpEntity;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class ItemBabyJarFull extends Item
{
    private int healAmount;
    private int placedelay;

    public ItemBabyJarFull()
    {
        super(new Item.Properties().maxStackSize(1).group(MoreCreepsReboot.creepsTab));
        healAmount = 20;
        placedelay = 30;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, playerentity
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, PlayerEntity playerentity)
    {
        if (playerentity.isSneaking())
        {
            world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.BARF, SoundCategory.PLAYERS, 1.0F, 1.0F);
            itemstack.setCount(itemstack.getCount() - 1);

            if(world.isRemote)
            {
            	MoreCreepsReboot.proxy.barf(world, playerentity);
            }

            placedelay = 30;
            playerentity.heal(healAmount);
        }
        else if (placedelay <= 0)
        {
            float f = 1.0F;
            float f1 = playerentity.prevRotationPitch + (playerentity.rotationPitch - playerentity.prevRotationPitch) * f;
            float f2 = playerentity.prevRotationYaw + (playerentity.rotationYaw - playerentity.prevRotationYaw) * f;
            double d2 = playerentity.prevPosX + (playerentity.posX - playerentity.prevPosX) * (double)f;
            double d3 = (playerentity.prevPosY + (playerentity.posY - playerentity.prevPosY) * (double)f + 1.6200000000000001D) - playerentity.getYOffset();
            double d4 = playerentity.prevPosZ + (playerentity.posZ - playerentity.prevPosZ) * (double)f;
            Vec3d vec3d = new Vec3d(d2, d3, d4);
            float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
            float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.01745329F);
            float f6 = MathHelper.sin(-f1 * 0.01745329F);
            float f7 = f4 * f5;
            float f8 = f6;
            float f9 = f3 * f5;
            double d5 = 5D;
            Vec3d vec3d1 = vec3d.add((double)f7 * d5, (double)f8 * d5, (double)f9 * d5);
            RayTraceResult raytraceresult = world.rayTraceBlocks(vec3d, vec3d1, true);

            if (raytraceresult == null)
            {
                return itemstack;
            }

            if (raytraceresult.getType() == RayTraceResult.Type.BLOCK)
            {
                int k = raytraceresult.getBlockPos().getX();
                int l = raytraceresult.getBlockPos().getY() + 1;
                int i1 = raytraceresult.getBlockPos().getZ();
                Block j1 = world.getBlockState(new BlockPos(k, l, i1)).getBlock();
                SchlumpEntity creepsentityschlump = new SchlumpEntity(world);
                creepsentityschlump.setLocationAndAngles(k, l, i1, playerentity.rotationYaw, 0.0F);
                world.addEntity(creepsentityschlump);
                placedelay = 30;
                return new ItemStack(ItemList.baby_jar_empty);
            }
        }

        return itemstack;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack stack, World world, ItemEntity entity, int i, boolean flag)
    {
        super.onEntityItemUpdate(stack, entity);

        if (flag && placedelay > 0)
        {
            placedelay--;
        }
    }
}
