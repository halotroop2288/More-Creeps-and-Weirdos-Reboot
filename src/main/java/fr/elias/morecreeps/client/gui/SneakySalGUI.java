package fr.elias.morecreeps.client.gui;

import java.io.IOException;
import java.util.Random;

import com.mojang.blaze3d.platform.GlStateManager;
import com.sun.jna.platform.KeyboardUtils;

import net.minecraft.client.KeyboardListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.elias.morecreeps.common.MoreCreepsReboot;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.RatManEntity;
import fr.elias.morecreeps.common.entity.SneakySalEntity;
import fr.elias.morecreeps.common.lists.ItemList;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;

public class SneakySalGUI extends Screen {
    private SneakySalEntity sneakysal;
    private float xSize_lo;
    private float ySize_lo;
    public int playercash;
    public float saleprice;
    public static Random rand = new Random();
    protected int xSize;
    protected int ySize;
    private ItemRenderer itemRender;

    public SneakySalGUI(SneakySalEntity creepsentitysneakysal, ITextComponent title)
    {
        super(title);
        sneakysal = creepsentitysneakysal;
        xSize = 512;
        ySize = 512;
        itemRender = Minecraft.getInstance().getItemRenderer();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui(PlayerEntity playerentity)
    {
        KeyboardListener keyboardlistener;
        keyboardlistener.enableRepeatEvents(true);
        buttonList.clear();
        byte byte0 = -18;
        ClientPlayerEntity PlayerEntity = Minecraft.getInstance().player;
        ClientWorld world = Minecraft.getInstance().world;
        world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.SAL_GREETING, SoundCategory.NEUTRAL,
                1.0F, 1.0F);
        saleprice = sneakysal.saleprice;
        this.addButton(new Button(2, width / 2 - 170, height / 4 + 8 + byte0, 155, 20,
                (new StringBuilder()).append("\2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[0]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[0]]))
                        .toString()));
        this.addButton(new Button(3, width / 2 + 2, height / 4 + 8 + byte0, 155, 20,
                (new StringBuilder()).append("   \2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[1]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[1]]))
                        .toString()));
        this.addButton(new Button(4, width / 2 - 170, height / 4 + 35 + byte0, 155, 20,
                (new StringBuilder()).append("\2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[2]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[2]]))
                        .toString()));
        this.addButton(new Button(5, width / 2 + 2, height / 4 + 35 + byte0, 155, 20,
                (new StringBuilder()).append("\2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[3]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[3]]))
                        .toString()));
        this.addButton(new Button(6, width / 2 - 170, height / 4 + 65 + byte0, 155, 20,
                (new StringBuilder()).append("\2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[4]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[4]]))
                        .toString()));
        this.addButton(new Button(7, width / 2 + 2, height / 4 + 65 + byte0, 155, 20,
                (new StringBuilder()).append("\2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[5]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[5]]))
                        .toString()));
        this.addButton(new Button(8, width / 2 - 170, height / 4 + 95 + byte0, 155, 20,
                (new StringBuilder()).append("\2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[6]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[6]]))
                        .toString()));
        this.addButton(new Button(9, width / 2 + 2, height / 4 + 95 + byte0, 155, 20,
                (new StringBuilder()).append("\2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[7]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[7]]))
                        .toString()));
        this.addButton(new Button(10, width / 2 - 170, height / 4 + 125 + byte0, 155, 20,
                (new StringBuilder()).append("\2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[8]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[8]]))
                        .toString()));
        this.addButton(new Button(11, width / 2 + 2, height / 4 + 125 + byte0, 155, 20,
                (new StringBuilder()).append("\2472    $\2476")
                        .append(String.valueOf(
                                Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[9]] * saleprice)))
                        .append("\247f ").append(String.valueOf(SneakySalEntity.saldescriptions[sneakysal.salslots[9]]))
                        .toString()));
        this.addButton(new Button(0, width / 2 - 100, height / 4 + 158 + byte0, 98, 20, "RIPOFF SAL"));
        this.addButton(new Button(1, width / 2 + 2, height / 4 + 158 + byte0, 98, 20, "DONE"));
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        char c = '\260';
        char c1 = '\246';
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
        int j = (width - c) / 2;
        int k = (height - c1) / 2;
        drawTexturedModalRect(j, k, 0, 0, (int) xSize_lo, (int) ySize_lo);
        drawEntityOnScreen(j + 51, k + 75, 30, (float) (j + 51) - mouseX, (float) (k + 75 - 50) - mouseY,
                this.minecraft.player);
    }

    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_,
            float p_147046_4_, LivingEntity livingentity) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) p_147046_0_, (float) p_147046_1_, 50.0F);
        GlStateManager.scalef((float) (-p_147046_2_), (float) p_147046_2_, (float) p_147046_2_);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = livingentity.renderYawOffset;
        float f3 = livingentity.rotationYaw;
        float f4 = livingentity.rotationPitch;
        float f5 = livingentity.prevRotationYawHead;
        float f6 = livingentity.rotationYawHead;
        GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(-((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        livingentity.renderYawOffset = (float) Math.atan((double) (p_147046_3_ / 40.0F)) * 20.0F;
        livingentity.rotationYaw = (float) Math.atan((double) (p_147046_3_ / 40.0F)) * 40.0F;
        livingentity.rotationPitch = -((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F;
        livingentity.rotationYawHead = livingentity.rotationYaw;
        livingentity.prevRotationYawHead = livingentity.rotationYaw;
        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRendererManager rendermanager = Minecraft.getInstance().getRenderManager();
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
        KeyboardUtils.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(Button guibutton)
    {
        PlayerEntity playerentity = Minecraft.getInstance().player;
        World world = Minecraft.getInstance().world;

        if (!guibutton.active)
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
            sneakysal.dissedmax--;

            if (rand.nextInt(9) == 0)
            {
                world.playSound(playerentity, playerentity.getPosition(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.MASTER, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                int i = rand.nextInt(15) + 1;

                switch (i)
                {
                    case 1:
                        sneakysal.entityDropItem(ItemList.army_gem, 1);
                    break;

                case 2:
                    sneakysal.entityDropItem(ItemList.horse_head_gem, 1);
                    break;

                case 3:
                    sneakysal.entityDropItem(ItemList.band_aid, 1);
                    break;

                case 4:
                    sneakysal.entityDropItem(ItemList.shrink_ray, 1);
                    break;

                case 5:
                    sneakysal.entityDropItem(ItemList.extinguisher, 1);
                    break;

                case 6:
                    sneakysal.entityDropItem(ItemList.grow_ray, 1);
                    break;

                case 7:
                    sneakysal.entityDropItem(ItemList.frisbee, 1);
                    break;

                case 8:
                    sneakysal.entityDropItem(ItemList.life_gem, 1);
                    break;

                case 9:
                    sneakysal.entityDropItem(ItemList.gun, 1);
                    break;

                case 10:
                    sneakysal.entityDropItem(ItemList.ray_gun, 1);
                    break;

                default:
                    sneakysal.entityDropItem(ItemList.band_aid, 1);
                    break;
                }

                minecraft.displayGuiScreen(null);
                return;
            }

            for (int j = 0; j < rand.nextInt(15) + 5; j++) {
                double d = -MathHelper.sin((sneakysal.rotationYaw * (float) Math.PI) / 180F);
                double d1 = MathHelper.cos((sneakysal.rotationYaw * (float) Math.PI) / 180F);
                RatManEntity creepsentityratman = new RatManEntity(world);
                creepsentityratman.setLocationAndAngles((sneakysal.posX + d * 1.0D + (double) rand.nextInt(4)) - 2D,
                        sneakysal.posY - 1.0D, (sneakysal.posZ + d1 * 1.0D + (double) rand.nextInt(4)) - 2D,
                        sneakysal.rotationYaw, 0.0F);
                creepsentityratman.setMotion(creepsentityratman.getMotion().x, 1.0D, creepsentityratman.getMotion().z);
                world.addEntity(creepsentityratman);
            }

            world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.SAL_RATS, SoundCategory.HOSTILE, 1.0F, 1.0F);
            minecraft.displayGuiScreen(null);
            return;
        }

        int k = guibutton.id;

        if (k > 1 && k < 12) {
            k -= 2;
            SneakySalEntity _tmp = sneakysal;
            int l = Math.round((float) SneakySalEntity.salprices[sneakysal.salslots[k]] * saleprice);
            playercash = checkCash();

            if (playercash < l) {
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.SAL_NO_MONEY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            } else {
                removeCash(l);
                SneakySalEntity _tmp1 = sneakysal;
                sneakysal.entityDropItem(SneakySalEntity.salitems[sneakysal.salslots[k]], 1);
                world.playSound(playerentity, playerentity.getPosition(), SoundsHandler.SAL_SALE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }
    }

    public boolean removeCash(int i)
    {
        PlayerEntity playerentity = Minecraft.getInstance().player;
        Object obj = null;
        ItemStack aitemstack[] = ((PlayerEntity)(playerentity)).inventory.mainInventory;
        boolean flag = false;
        label0:

        for (int j = 0; j < aitemstack.length; j++)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack == null || itemstack.getItem() != ItemList.money)
            {
                continue;
            }

            do
            {
                if (itemstack.getCount() <= 0 || i <= 0)
                {
                    continue label0;
                }

                i--;

                if (itemstack.getCount() - 1 == 0)
                {
                    ((PlayerEntity)(playerentity)).inventory.mainInventory[j] = null;
                    continue label0;
                }

                itemstack.setCount(itemstack.getCount() - 1);
            }
            while (true);
        }

        return true;
    }

    public int checkCash()
    {
        PlayerEntity playerentity = Minecraft.getInstance().player;
        Object obj = null;
        ItemStack aitemstack[] = ((PlayerEntity)(playerentity)).inventory.mainInventory;
        int i = 0;

        for (int j = 0; j < aitemstack.length; j++)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack != null && itemstack.getItem() == ItemList.money)
            {
                itemstack.setCount(itemstack.getCount() + i);
            }
        }

        return i;
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
        drawWorldBackground(0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MODID + "textures/gui/gui-screensal.png"));
        int l = (width - xSize) / 2;
        int i1 = (height - (ySize + 16)) / 2;
        drawTexturedModalRect(20, 20, 0, 0, xSize + 400, ySize);
        byte byte0 = -18;
        boolean flag = false;
        playercash = checkCash();
        drawCenteredString(Minecraft.getInstance().fontRenderer, "\2475******* \247fWELCOME TO SAL'S SHOP \2475*******", width / 2, height / 4 - 40, 0xffffff);
        drawCenteredString(Minecraft.getInstance().fontRenderer, (new StringBuilder()).append("\247eYour cash : \2472$\2476 ").append(String.valueOf(playercash)).toString(), width / 2, height / 4 - 25, 0xffffff);

        for (int j1 = 0; j1 < 5; j1++)
        {
            zLevel = 200F;
            itemRender.zLevel = 200F;
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            SneakySalEntity _tmp = sneakysal;
            itemRender.renderItemIntoGUI(SneakySalEntity.itemstack[sneakysal.salslots[j1 * 2]], width / 2 - 160, height / 4 + 8 + byte0 + j1 * 30);
            SneakySalEntity _tmp1 = sneakysal;
            itemRender.renderItemIntoGUI(SneakySalEntity.itemstack[sneakysal.salslots[j1 * 2 + 1]], width / 2 + 12, height / 4 + 8 + byte0 + j1 * 30);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
        }

        super.drawScreen(i, j, f);
        xSize_lo = i;
        ySize_lo = j;
    }
}
