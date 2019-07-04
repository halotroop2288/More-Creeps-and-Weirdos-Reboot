package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.common.entity.BabyMummyEntity;

public class CREEPSRenderBabyMummy extends RenderLiving
{
    protected ModelBiped modelBipedMain;
    public boolean scaled;

    public CREEPSRenderBabyMummy(ModelBiped modelbiped, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), modelbiped, f);
        modelBipedMain = modelbiped;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        updateBabyMummyScale((BabyMummyEntity)entityliving, f);
    }

    protected void updateBabyMummyScale(BabyMummyEntity creepsentitybabymummy, float f)
    {
        GL11.glScalef(creepsentitybabymummy.babysize, creepsentitybabymummy.babysize, creepsentitybabymummy.babysize);
    }

	protected ResourceLocation getEntityTexture(BabyMummyEntity entity)
	{
		return entity.texture;
	}
    
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return getEntityTexture((BabyMummyEntity) entity);
	}
}
