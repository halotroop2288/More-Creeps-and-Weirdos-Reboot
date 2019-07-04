package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelPyramidGuardian;
import fr.elias.morecreeps.common.entity.PrisonerEntity;
import fr.elias.morecreeps.common.entity.PyramidGuardianEntity;

public class CREEPSRenderPyramidGuardian extends RenderLiving
{
    protected CREEPSModelPyramidGuardian modelBipedMain;
    public boolean scaled;
    private ModelBase scaleAmount;

    public CREEPSRenderPyramidGuardian(CREEPSModelPyramidGuardian creepsmodelpyramidguardian, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodelpyramidguardian, f);
        modelBipedMain = creepsmodelpyramidguardian;
        scaleAmount = creepsmodelpyramidguardian;
    }

    /**
     * sets the scale for the slime based on getSlimeSize in EntitySlime
     */
    protected void scaleSlime(PyramidGuardianEntity creepsentitypyramidguardian, float f)
    {
        GL11.glScalef(0.55F, 0.55F, 0.75F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        scaleSlime((PyramidGuardianEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(PyramidGuardianEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((PyramidGuardianEntity) entity);
	}
}
