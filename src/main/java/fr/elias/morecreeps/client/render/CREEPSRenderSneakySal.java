package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelSneakySal;
import fr.elias.morecreeps.common.entity.SchlumpEntity;
import fr.elias.morecreeps.common.entity.SneakySalEntity;

public class CREEPSRenderSneakySal extends RenderLiving
{
    protected CREEPSModelSneakySal modelBipedMain;

    public CREEPSRenderSneakySal(CREEPSModelSneakySal creepsmodelsneakysal, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelsneakysal, f);
        modelBipedMain = creepsmodelsneakysal;
    }

    protected void fattenup(SneakySalEntity creepsentitysneakysal, float f)
    {
        GL11.glScalef(creepsentitysneakysal.modelsize, creepsentitysneakysal.modelsize + 0.1F, creepsentitysneakysal.modelsize);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        SneakySalEntity creepsentitysneakysal = (SneakySalEntity)entityliving;
        modelBipedMain.shooting = creepsentitysneakysal.shooting;
        fattenup((SneakySalEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(SneakySalEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((SneakySalEntity) entity);
	}
}
