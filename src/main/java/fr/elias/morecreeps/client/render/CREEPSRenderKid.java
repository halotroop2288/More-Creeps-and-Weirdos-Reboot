package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelKid;
import fr.elias.morecreeps.common.entity.HunchbackEntity;
import fr.elias.morecreeps.common.entity.KidEntity;

public class CREEPSRenderKid extends RenderLiving
{
    protected CREEPSModelKid modelBipedMain;

    public CREEPSRenderKid(CREEPSModelKid creepsmodelkid, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelkid, f);
        modelBipedMain = creepsmodelkid;
    }

    protected void fattenup(KidEntity creepsentitykid, float f)
    {
        GL11.glScalef(creepsentitykid.modelsize, creepsentitykid.modelsize, creepsentitykid.modelsize);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        fattenup((KidEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(KidEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((KidEntity) entity);
	}
}
