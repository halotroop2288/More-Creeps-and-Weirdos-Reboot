package fr.elias.morecreeps.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import fr.elias.morecreeps.common.entity.GuineaPigEntity;

public class GuineaPigTrainingGUI extends Screen
{
    private GuineaPigEntity guineapig;
    private TextFieldWidget namescreen;
    private boolean field_28217_m;
    private float xSize_lo;
    private float ySize_lo;

    public GuineaPigTrainingGUI(GuineaPigEntity creepsentityguineapig)
    {
        super(gui);
        guineapig = creepsentityguineapig;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        byte byte0 = -16;
        this.addButton(new Button(2, width / 2 - 110, height / 4 + 8 + byte0, 98, 20, "\2476<-\247f ATTACK \2476->"));
        this.addButton(new Button(3, width / 2 + 12, height / 4 + 8 + byte0, 98, 20, "\2476>> \247f DEFENSE \2476<<"));
        this.addButton(new Button(4, width / 2 - 110, height / 4 + 65 + byte0, 98, 20, "\2476++\247f HEALING \2476++"));
        this.addButton(new Button(5, width / 2 + 12, height / 4 + 65 + byte0, 98, 20, "\2476((\247f SPEED \2476))"));
        this.addButton(new Button(0, width / 2 - 100, height / 4 + 158 + byte0, 98, 20, "BACK"));
        this.addButton(new Button(1, width / 2 + 2, height / 4 + 158 + byte0, 98, 20, "DONE"));
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        char c = '\260';
        char c1 = '\246';
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.renderEngine().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
        int j = (width - c) / 2;
        int k = (height - c1) / 2;
        drawTexturedModalRect(j, k, 0, 0, (int)xSize_lo, (int)ySize_lo);
        drawEntityOnScreen(j + 51, k + 75, 30, (float)(j + 51) - mouseX, (float)(k + 75 - 50) - mouseY, this.minecraft.player);

        /*GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(j + 51, k + 75, 50F);
        float f1 = 30F;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f2 = minecraft.player.renderYawOffset;
        float f3 = minecraft.player.rotationYaw;
        float f4 = minecraft.player.rotationPitch;
        float f5 = (float)(j + 51) - xSize_lo;
        float f6 = (float)((k + 75) - 50) - ySize_lo;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        minecraft.player.renderYawOffset = (float)Math.atan(f5 / 40F) * 20F;
        minecraft.player.rotationYaw = (float)Math.atan(f5 / 40F) * 40F;
        minecraft.player.rotationPitch = -(float)Math.atan(f6 / 40F) * 20F;
        GL11.glTranslatef(0.0F, minecraft.player.yOffset, 0.0F);
        RenderManager.instance.renderEntityWithPosYaw(minecraft.player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        minecraft.player.renderYawOffset = f2;
        minecraft.player.rotationYaw = f3;
        minecraft.player.rotationPitch = f4;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);*/
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
        RenderManager rendermanager = Minecraft.getInstance().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(livingentity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        livingentity.renderYawOffset = f2;
        livingentity.rotationYaw = f3;
        livingentity.rotationPitch = f4;
        livingentity.prevRotationYawHead = f5;
        livingentity.rotationYawHead = f6;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled)
        {
            return;
        }

        if (guibutton.id == 1)
        {
            minecraft.displayGuiScreen(null);
            return;
        }

        if (guibutton.id == 0)
        {
            Minecraft.getInstance().displayGuiScreen(new CREEPSGUIGuineaPig(guineapig));
        }

        PlayerEntity entityplayersp = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;

        if (guibutton.id == 2 && guineapig.skillattack < 5)
        {
            if (!checkLevel(guineapig.skillattack))
            {
                return;
            }

            if (checkWheat())
            {
                guineapig.skillattack++;

                double attackStrength = guineapig.getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
                
                if (guineapig.skillattack < 4)
                {
                    attackStrength += 2;
                }
                else
                {
                	attackStrength += 4;
                }

                world.playSound(entityplayersp, "morecreeps:guineapigtrain", 1.0F, 1.0F);
            }
            else
            {
                world.playSound(entityplayersp, "morecreeps:guineapignowheat", 1.0F, 1.0F);
            }
        }

        if (guibutton.id == 3 && guineapig.skilldefend < 5)
        {
            if (!checkLevel(guineapig.skilldefend))
            {
                return;
            }

            if (checkWheat())
            {
                guineapig.skilldefend++;
                world.playSound(entityplayersp, "morecreeps:guineapigtrain", 1.0F, 1.0F);
            }
            else
            {
                world.playSound(entityplayersp, "morecreeps:guineapignowheat", 1.0F, 1.0F);
            }
        }

        if (guibutton.id == 4 && guineapig.skillhealing < 5)
        {
            if (!checkLevel(guineapig.skillhealing))
            {
                return;
            }

            if (checkWheat())
            {
                guineapig.skillhealing++;
                world.playSound(entityplayersp, "morecreeps:guineapigtrain", 1.0F, 1.0F);
            }
            else
            {
                world.playSound(entityplayersp, "morecreeps:guineapignowheat", 1.0F, 1.0F);
            }
        }

        if (guibutton.id == 5 && guineapig.skillspeed < 5)
        {
            if (!checkLevel(guineapig.skillspeed))
            {
                return;
            }

            if (checkWheat())
            {
                guineapig.skillspeed++;
                guineapig.baseSpeed += 0.05F;
                world.playSound(entityplayersp, "morecreeps:guineapigtrain", 1.0F, 1.0F);
            }
            else
            {
                world.playSound(entityplayersp, "morecreeps:guineapignowheat", 1.0F, 1.0F);
            }
        }
    }


    public boolean checkLevel(int i)
    {
        i *= 5;

        if (guineapig.level < i)
        {
            World world = Minecraft.getInstance().world;
            PlayerEntity entityplayersp = Minecraft.getInstance().player;

            if (i == 5)
            {
                world.playSound(entityplayersp, "morecreeps:guineapig5level", 1.0F, 1.0F);
            }

            if (i == 10)
            {
                world.playSound(entityplayersp, "morecreeps:guineapig10level", 1.0F, 1.0F);
            }

            if (i == 15)
            {
                world.playSound(entityplayersp, "morecreeps:guineapig15level", 1.0F, 1.0F);
            }

            if (i == 20)
            {
                world.playSound(entityplayersp, "morecreeps:guineapig20level", 1.0F, 1.0F);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean checkWheat()
    {
        PlayerEntity entityplayersp = Minecraft.getInstance().player;
        Object obj = null;
        NonNullList<ItemStack> aitemstack = ((PlayerEntity)(entityplayersp)).inventory.mainInventory;
        int i = 0;

        for (int j = 0; j < aitemstack.size(); j++)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack != null && itemstack.getItem() == Items.WHEAT)
            {
                itemstack.setCount(itemstack.getCount() + i);
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

                if (itemstack1 == null || itemstack1.getItem() != Items.WHEAT)
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
                        ((PlayerEntity)(entityplayersp)).inventory.mainInventory[i1] = null;
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
    protected void mouseClicked(int i, int j, int k)
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
        drawWorldBackground(1);
        drawCenteredString(minecraft.fontRenderer, (new StringBuilder()).append("\2476").append(String.valueOf(guineapig.name)).append("'s TRAINING").toString(), width / 2, height / 4 - 40, 0xffffff);
        drawCenteredString(minecraft.fontRenderer, (new StringBuilder()).append("\247fGUINEA PIG LEVEL : \2473").append(String.valueOf(guineapig.level)).toString(), width / 2, height / 4 - 25, 0xffffff);
        drawString(minecraft.fontRenderer, buildStat(guineapig.skillattack), (width / 2 - 107) + k, height / 4 + 38 + byte0, 0xff8d13);
        drawString(minecraft.fontRenderer, buildStat(guineapig.skilldefend), width / 2 + 16 + k, height / 4 + 38 + byte0, 0xff8d13);
        drawString(minecraft.fontRenderer, buildStat(guineapig.skillhealing), (width / 2 - 107) + k, height / 4 + 95 + byte0, 0xff8d13);
        drawString(minecraft.fontRenderer, buildStat(guineapig.skillspeed), width / 2 + 16 + k, height / 4 + 95 + byte0, 0xff8d13);
        PlayerEntity entityplayersp = Minecraft.getInstance().player;
        Object obj = null;
        NonNullList<ItemStack> aitemstack = ((PlayerEntity)(entityplayersp)).inventory.mainInventory;
        int l = 0;

        for (int i1 = 0; i1 < aitemstack.size(); i1++)
        {
            ItemStack itemstack = aitemstack[i1];

            if (itemstack != null && itemstack.getItem() == Items.WHEAT)
            {
                itemstack.setCount(itemstack.getCount() + l);
            }
        }

        drawCenteredString(minecraft.fontRenderer, (new StringBuilder()).append("\247fWHEAT REMAINING: \2473").append(String.valueOf(l)).toString(), width / 2 + 2 + k, height / 4 + 120 + byte0, 0xff8d13);
        drawCenteredString(minecraft.fontRenderer, "\2476Each level costs five wheat", width / 2 + 2 + k, height / 4 + 140 + byte0, 0xff8d13);
        super.drawScreen(i, j, f);
        xSize_lo = i;
        ySize_lo = j;
    }
}
