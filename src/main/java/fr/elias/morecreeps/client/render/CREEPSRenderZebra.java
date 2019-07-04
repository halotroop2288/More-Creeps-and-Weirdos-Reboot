package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelZebra;
import fr.elias.morecreeps.common.entity.TrophyEntity;
import fr.elias.morecreeps.common.entity.ZebraEntity;

public class CREEPSRenderZebra extends RenderLiving
{
    protected CREEPSModelZebra modelBipedMain;

    public CREEPSRenderZebra(CREEPSModelZebra creepsmodelzebra, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodelzebra, f);
        modelBipedMain = creepsmodelzebra;
    }

    protected void fattenup(ZebraEntity creepsentityzebra, float f)
    {
        GL11.glScalef(creepsentityzebra.modelsize, creepsentityzebra.modelsize, creepsentityzebra.modelsize);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        ZebraEntity creepsentityzebra = (ZebraEntity)entityliving;
        modelBipedMain.tamed = creepsentityzebra.tamed;
        fattenup((ZebraEntity)entityliving, f);
    }

    public void doRenderLiving(LivingEntity entityliving, double d, double d1, double d2, float f, float f1)
    {
        super.doRender(entityliving, d, d1, d2, f, f1);
        float f2 = 1.6F;
        float f3 = 0.01666667F * f2;
        float f4 = entityliving.getDistanceToEntity(renderManager.livingPlayer);
        String s = "";
        s = (new StringBuilder()).append(s).append(((ZebraEntity)entityliving).name).toString();

        if (f4 < 25F && s.length() > 0 && ((ZebraEntity)entityliving).riddenByEntity == null)
        {
            s = (new StringBuilder()).append("\2476").append(s).toString();
            FontRenderer fontrenderer = getFontRendererFromRenderManager();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d + 0.0F, (float)d1 + 1.1F, (float)d2);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f3, -f3, f3);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            float f5 = (2.0F - ((ZebraEntity)entityliving).modelsize) * 80F;
            int i = -100 + (int)f5;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
//            worldRenderer.startDrawingQuads();
            int j = fontrenderer.getStringWidth(s) / 2;
//            worldRenderer.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
//            worldRenderer.addVertex(-j - 1, -1 + i, 0.0D);
//            worldRenderer.addVertex(-j - 1, 8 + i, 0.0D);
//            worldRenderer.addVertex(j + 1, 8 + i, 0.0D);
//            worldRenderer.addVertex(j + 1, -1 + i, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, i, 0x20ffffff);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, i, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        doRenderLiving((LivingEntity)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(ZebraEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((ZebraEntity) entity);
	}
}
