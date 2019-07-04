package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.common.entity.SnowDevilEntity;
import fr.elias.morecreeps.common.entity.ThiefEntity;

public class CREEPSRenderThief extends RenderLiving
{
    protected ModelBiped modelBipedMain;

    public CREEPSRenderThief(ModelBiped modelbiped, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), modelbiped, f);
        modelBipedMain = modelbiped;
    }

    protected void fattenup(ThiefEntity creepsentitythief, float f)
    {
        GL11.glScalef(creepsentitythief.modelsize, creepsentitythief.modelsize, creepsentitythief.modelsize);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        fattenup((ThiefEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(ThiefEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((ThiefEntity) entity);
	}
}
