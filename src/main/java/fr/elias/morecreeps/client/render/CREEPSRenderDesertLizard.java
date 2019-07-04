package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelDesertLizard;
import fr.elias.morecreeps.common.entity.CavemanEntity;
import fr.elias.morecreeps.common.entity.DesertLizardEntity;

public class CREEPSRenderDesertLizard extends RenderLiving
{
    public CREEPSRenderDesertLizard(CREEPSModelDesertLizard creepsmodeldesertlizard, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodeldesertlizard, f);
    }

    protected void fattenup(DesertLizardEntity creepsentitydesertlizard, float f)
    {
        GL11.glScalef(creepsentitydesertlizard.modelsize, creepsentitydesertlizard.modelsize, creepsentitydesertlizard.modelsize);
    }
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        fattenup((DesertLizardEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(DesertLizardEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((DesertLizardEntity) entity);
	}
}
