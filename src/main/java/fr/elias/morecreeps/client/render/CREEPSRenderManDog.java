package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelManDog;
import fr.elias.morecreeps.common.entity.LollimanEntity;
import fr.elias.morecreeps.common.entity.ManDogEntity;

public class CREEPSRenderManDog extends RenderLiving
{
    protected ModelBiped modelBipedMain;
    protected CREEPSModelManDog superdog;

    public CREEPSRenderManDog(CREEPSModelManDog creepsmodelmandog, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelmandog, f);
        superdog = creepsmodelmandog;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        ManDogEntity creepsentitymandog = (ManDogEntity)entityliving;
        superdog.superfly = creepsentitymandog.superfly;
        fattenup((ManDogEntity)entityliving, f);
    }

    protected void fattenup(ManDogEntity creepsentitymandog, float f)
    {
        GL11.glScalef(creepsentitymandog.modelsize, creepsentitymandog.modelsize, creepsentitymandog.modelsize);
    }

    protected ResourceLocation getEntityTexture(ManDogEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((ManDogEntity) entity);
	}
}
