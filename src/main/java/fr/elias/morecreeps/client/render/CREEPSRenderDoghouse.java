package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelDoghouse;
import fr.elias.morecreeps.common.entity.DigBugEntity;
import fr.elias.morecreeps.common.entity.DogHouseEntity;

public class CREEPSRenderDoghouse extends RenderLiving
{
    protected CREEPSModelDoghouse modelBipedMain;

    public CREEPSRenderDoghouse(CREEPSModelDoghouse creepsmodeldoghouse, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodeldoghouse, f);
        modelBipedMain = creepsmodeldoghouse;
    }

    protected void fattenup(DogHouseEntity creepsentitydoghouse, float f)
    {
        GL11.glScalef(2.5F, 2.5F, 2.5F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        fattenup((DogHouseEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(DogHouseEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((DogHouseEntity) entity);
	}
}
