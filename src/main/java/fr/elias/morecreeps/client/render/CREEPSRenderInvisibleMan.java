package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.common.entity.HippoEntity;
import fr.elias.morecreeps.common.entity.InvisibleManEntity;

public class CREEPSRenderInvisibleMan extends RenderLiving
{
    public CREEPSRenderInvisibleMan(ModelBiped modelbiped, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), modelbiped, f);
    }

    protected void fattenup(InvisibleManEntity creepsentityinvisibleman, float f)
    {
        GL11.glScalef(creepsentityinvisibleman.modelsize, creepsentityinvisibleman.modelsize, creepsentityinvisibleman.modelsize);
    }

    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        fattenup((InvisibleManEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(InvisibleManEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((InvisibleManEntity) entity);
	}
}
