package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelGooGoat;
import fr.elias.morecreeps.common.entity.LetterGEntity;
import fr.elias.morecreeps.common.entity.GooGoatEntity;

public class CREEPSRenderGooGoat extends RenderLiving
{
    private ModelBase scaleAmount;
    protected CREEPSModelGooGoat modelBipedMain;

    public CREEPSRenderGooGoat(CREEPSModelGooGoat creepsmodelgoogoat, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelgoogoat, f);
        modelBipedMain = creepsmodelgoogoat;
        scaleAmount = creepsmodelgoogoat;
        this.addLayer(new LayerGooGoat(this));
    }

    /*protected int func_179_a(CREEPSGooGoatEntity creepsentitygoogoat, int i, float f)
    {
        if (i == 0)
        {
            GL11.glEnable(GL11.GL_NORMALIZE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            return 1;
        }

        if (i == 1)
        {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        return -1;
    }*/

    /**
     * sets the scale for the slime based on getSlimeSize in EntitySlime
     */
    protected void scaleSlime(GooGoatEntity creepsentitygoogoat, float f)
    {
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glScalef(creepsentitygoogoat.goatsize, creepsentitygoogoat.goatsize, creepsentitygoogoat.goatsize + 0.5F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        scaleSlime((GooGoatEntity)entityliving, f);
    }

    /*protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return func_179_a((CREEPSGooGoatEntity)entityliving, i, f);
    }*/

    protected ResourceLocation getEntityTexture(GooGoatEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((GooGoatEntity) entity);
	}
}
