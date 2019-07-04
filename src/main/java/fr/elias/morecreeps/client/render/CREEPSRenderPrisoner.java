package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.common.entity.PreacherEntity;
import fr.elias.morecreeps.common.entity.PrisonerEntity;

public class CREEPSRenderPrisoner extends RenderLiving
{
    protected ModelBiped modelBipedMain;

    public CREEPSRenderPrisoner(ModelBiped modelbiped, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), modelbiped, f);
        modelBipedMain = modelbiped;
    }

    protected void fattenup(PrisonerEntity creepsentityprisoner, float f)
    {
        GL11.glScalef(0.75F, 1.0F, 0.9F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        PrisonerEntity creepsentityprisoner = (PrisonerEntity)entityliving;
        fattenup((PrisonerEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(PrisonerEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((PrisonerEntity) entity);
	}
}
