package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelRatMan;
import fr.elias.morecreeps.common.entity.PyramidGuardianEntity;
import fr.elias.morecreeps.common.entity.RatManEntity;

public class CREEPSRenderRatMan extends RenderLiving
{
    protected CREEPSModelRatMan modelBipedMain;

    public CREEPSRenderRatMan(CREEPSModelRatMan creepsmodelratman, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelratman, f);
        modelBipedMain = creepsmodelratman;
    }

    protected void fattenup(RatManEntity creepsentityratman, float f)
    {
        GL11.glScalef(creepsentityratman.modelsize, creepsentityratman.modelsize, creepsentityratman.modelsize);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        RatManEntity creepsentityratman = (RatManEntity)entityliving;
        modelBipedMain.jumper = creepsentityratman.jumper;
        fattenup((RatManEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(RatManEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((RatManEntity) entity);
	}
}
