package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelHippo;
import fr.elias.morecreeps.common.entity.GuineaPigEntity;
import fr.elias.morecreeps.common.entity.HippoEntity;

public class CREEPSRenderHippo extends RenderLiving
{
    protected CREEPSModelHippo modelBipedMain;

    public CREEPSRenderHippo(CREEPSModelHippo creepsmodelhippo, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelhippo, f);
        modelBipedMain = creepsmodelhippo;
    }

    protected void fattenup(HippoEntity creepsentityhippo, float f)
    {
        GL11.glScalef(creepsentityhippo.modelsize, creepsentityhippo.modelsize, creepsentityhippo.modelsize);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        fattenup((HippoEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(HippoEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((HippoEntity) entity);
	}
}
