package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.common.entity.HunchbackSkeletonEntity;
import fr.elias.morecreeps.common.entity.InvisibleManEntity;

public class CREEPSRenderHunchbackSkeleton extends RenderLiving
{
    protected ModelBiped modelBipedMain;

    public CREEPSRenderHunchbackSkeleton(ModelBiped modelbiped, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), modelbiped, f);
        modelBipedMain = modelbiped;
    }

    protected void fattenup(HunchbackSkeletonEntity creepsentityhunchbackskeleton, float f)
    {
        GL11.glScalef(creepsentityhunchbackskeleton.modelsize, creepsentityhunchbackskeleton.modelsize, creepsentityhunchbackskeleton.modelsize);
    }

    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        fattenup((HunchbackSkeletonEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(HunchbackSkeletonEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((HunchbackSkeletonEntity) entity);
	}
}
