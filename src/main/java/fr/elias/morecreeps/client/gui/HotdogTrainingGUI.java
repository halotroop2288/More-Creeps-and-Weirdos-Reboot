package fr.elias.morecreeps.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import fr.elias.morecreeps.common.entity.HotdogEntity;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class HotdogTrainingGUI extends Screen
{
    private HotdogEntity hotdog;
    private float xSize_lo;
    private float ySize_lo;

    public HotdogTrainingGUI(HotdogEntity hotdogentity, StringTextComponent title)
    {
    	super(title = new StringTextComponent(hotdogentity.getName() + "'s Training"));
        hotdog = hotdogentity;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void init()
    {
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        byte byte0 = -16;
        this.addButton( new Button(this.width / 2 - 110, this.height / 4 + 8 + byte0, 98, 20, "\2476<-\247f ATTACK \2476->", actionPerformed(1)));
        this.addButton( new Button(this.width / 2 + 12, this.height / 4 + 8 + byte0, 98, 20, "\2476>> \247f DEFENSE \2476<<", actionPerformed(2)));
        this.addButton( new Button(this.width / 2 - 110, this.height / 4 + 65 + byte0, 98, 20, "\2476++\247f HEALING \2476++", actionPerformed(3)));
        this.addButton( new Button(this.width / 2 + 12, this.height / 4 + 65 + byte0, 98, 20, "\2476((\247f SPEED \2476))", actionPerformed(4)));
        this.addButton( new Button(this.width / 2 - 100, this.height / 4 + 158 + byte0, 98, 20, I18n.format("gui.back"), actionPerformed(0)));
        this.addButton( new Button(this.width / 2 + 2, this.height / 4 + 158 + byte0, 98, 20, I18n.format("gui.done"), actionPerformed(0)));
    }

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        char c = '\260';
        char c1 = '\246';
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().getTexture(new ResourceLocation("textures/gui/container/inventory.png"));
        int j = (width - c) / 2;
        int k = (height - c1) / 2;
        drawTexturedModalRect(j, k, 0, 0, (int)xSize_lo, (int)ySize_lo);
        drawEntityOnScreen(j + 51, k + 75, 30, (float)(j + 51) - mouseX, (float)(k + 75 - 50) - mouseY, this.minecraft.player);
    }

    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, LivingEntity livingentity)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)p_147046_0_, (float)p_147046_1_, 50.0F);
        GlStateManager.scalef((float)(-p_147046_2_), (float)p_147046_2_, (float)p_147046_2_);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = livingentity.renderYawOffset;
        float f3 = livingentity.rotationYaw;
        float f4 = livingentity.rotationPitch;
        float f5 = livingentity.prevRotationYawHead;
        float f6 = livingentity.rotationYawHead;
        GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(-((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        livingentity.renderYawOffset = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 20.0F;
        livingentity.rotationYaw = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 40.0F;
        livingentity.rotationPitch = -((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F;
        livingentity.rotationYawHead = livingentity.rotationYaw;
        livingentity.prevRotationYawHead = livingentity.rotationYaw;
        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRendererManager rendermanager = Minecraft.getInstance().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(livingentity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false); // This bool is to display debug bounding box inside this gui
        rendermanager.setRenderShadow(true);
        livingentity.renderYawOffset = f2;
        livingentity.rotationYaw = f3;
        livingentity.rotationPitch = f4;
        livingentity.prevRotationYawHead = f5;
        livingentity.rotationYawHead = f6;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
//        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture();
//        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
    	this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     * @return 
     */
    protected IPressable actionPerformed(Integer button)
    {
        if (button == null)
        {
            return null;
        }

        if (button == 0)
        {
            this.minecraft.displayGuiScreen(null);
            return null;
        }

        PlayerEntity playerentity = this.minecraft.player;
        World world = this.minecraft.world.getWorld();

        if (button == 1 && hotdog.skillattack < 5)
        {
            if (!checkLevel(hotdog.skillattack))
            {
                return null;
            }

            if (checkBones())
            {
                hotdog.skillattack++;

                if (hotdog.skillattack < 4)
                {
                    hotdog.attackStrength += 2;
                }
                else
                {
                    hotdog.attackStrength += 4;
                }

                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_TRAIN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
            else
            {
            	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_NO_BONES, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }

        if (button == 3 && hotdog.skilldefend < 5)
        {
            if (!checkLevel(hotdog.skilldefend))
            {
                return null;
            }

            if (checkBones())
            {
                hotdog.skilldefend++;
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_TRAIN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
            else
            {
            	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_NO_BONES, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }

        if (button == 4 && hotdog.skillhealing < 5)
        {
            if (!checkLevel(hotdog.skillhealing))
            {
                return null;
            }

            if (checkBones())
            {
                hotdog.skillhealing++;
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_NO_BONES, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
            else
            {
            	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_NO_BONES, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }

        if (button == 5 && hotdog.skillspeed < 5)
        {
            if (!checkLevel(hotdog.skillspeed))
            {
                return null;
            }

            if (checkBones())
            {
                hotdog.skillspeed++;
                hotdog.baseSpeed += 0.05F;
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_TRAIN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
            else
            {
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_NO_BONES, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }
		return null;
    }

    public boolean checkLevel(int i)
    {
        i *= 5;

        if (hotdog.level < i)
        {
            PlayerEntity playerentity = this.minecraft.player;
            World world = this.minecraft.world.getWorld();

            if (i == 5)
            {
            	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_LEVEL_5, SoundCategory.VOICE, 1.0F, 1.0F);
            }

            if (i == 10)
            {
            	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_LEVEL_10, SoundCategory.VOICE, 1.0F, 1.0F);
            }

            if (i == 15)
            {
            	world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_LEVEL_15, SoundCategory.VOICE, 1.0F, 1.0F);
            }

            if (i == 20)
            {
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.HOT_DOG_LEVEL_20, SoundCategory.VOICE, 1.0F, 1.0F);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean checkBones()
    {
        PlayerEntity playerentity = this.minecraft.player;
        Object obj = null;
        NonNullList<ItemStack> aitemstack = ((PlayerEntity)(playerentity)).inventory.mainInventory;
        int i = 0;

        for (int j = 0; j < aitemstack.size(); j++)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack != null && itemstack.getItem() == Items.BONE)
            {
                i += itemstack.getCount();
            }
        }

        if (i >= 5)
        {
            int k = 5;
            boolean flag = false;
            label0:

            for (int i1 = 0; i1 < aitemstack.size(); i1++)
            {
                ItemStack itemstack1 = aitemstack[i1];

                if (itemstack1 == null || itemstack1.getItem() != Items.BONE)
                {
                    continue;
                }

                int l = itemstack1.getCount();

                do
                {
                    if (itemstack1.getCount() <= 0 || k <= 0)
                    {
                        continue label0;
                    }

                    k--;

                    if (itemstack1.getCount() - 1 == 0)
                    {
                        ((PlayerEntity)(playerentity)).inventory.mainInventory[i1] = null;
                        continue label0;
                    }

                    itemstack1.setCount(itemstack1.getCount() - 1);
                }
                while (true);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public String buildStat(int i)
    {
        String s = "";

        for (int j = 0; j < i; j++)
        {
            s = (new StringBuilder()).append(s).append("\2473(*) ").toString();
        }

        if (i < 5)
        {
            for (int k = i; k < 5; k++)
            {
                s = (new StringBuilder()).append(s).append("\2478(*) ").toString();
            }
        }

        return s;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return true;
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int i, int j, int k) throws IOException
    {
        super.mouseClicked(i, j, k);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int i, int j, float f)
    {
        byte byte0 = -16;
        int k = 0;
//        drawWorldBackground(1);
        drawCenteredString(this.font, (new StringBuilder()).append("\2476").append(String.valueOf(hotdog.name)).append("'s TRAINING").toString(), width / 2, height / 4 - 40, 0xffffff);
        drawCenteredString(this.font, (new StringBuilder()).append("\247fHOTDOG LEVEL : \2473").append(String.valueOf(hotdog.level)).toString(), width / 2, height / 4 - 25, 0xffffff);
        drawString(this.font, buildStat(hotdog.skillattack), (width / 2 - 107) + k, height / 4 + 38 + byte0, 0xff8d13);
        drawString(this.font, buildStat(hotdog.skilldefend), width / 2 + 16 + k, height / 4 + 38 + byte0, 0xff8d13);
        drawString(this.font, buildStat(hotdog.skillhealing), (width / 2 - 107) + k, height / 4 + 95 + byte0, 0xff8d13);
        drawString(this.font, buildStat(hotdog.skillspeed), width / 2 + 16 + k, height / 4 + 95 + byte0, 0xff8d13);
        PlayerEntity playerentity = this.minecraft.player;
        Object obj = null;
        NonNullList<ItemStack> aitemstack = playerentity.inventory.mainInventory;
        int l = 0;

        for (int i1 = 0; i1 < aitemstack.get; i1++)
        {
            ItemStack itemstack = aitemstack;

            if (itemstack != null && itemstack.getItem() == Items.BONE)
            {
                l += itemstack.getCount();
            }
        }

        drawCenteredString(this.font, (new StringBuilder()).append("\247fBONES REMAINING: \2473").append(String.valueOf(l)).toString(), width / 2 + 2 + k, height / 4 + 120 + byte0, 0xff8d13);
        drawCenteredString(this.font, "\2476Each level costs five bones", width / 2 + 2 + k, height / 4 + 140 + byte0, 0xff8d13);
//        super.drawScreen(i, j, f);
        xSize_lo = i;
        ySize_lo = j;
    }
}
