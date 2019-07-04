package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelEvilSnowman;
import fr.elias.morecreeps.common.entity.EvilScientistEntity;
import fr.elias.morecreeps.common.entity.EvilSnowmanEntity;

public class CREEPSRenderEvilSnowman extends RenderLiving
{
    public float snowsize;
    protected CREEPSModelEvilSnowman modelBipedMain;

    public CREEPSRenderEvilSnowman(CREEPSModelEvilSnowman creepsmodelevilsnowman, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodelevilsnowman, f);
        modelBipedMain = creepsmodelevilsnowman;
    }

    protected void preRenderScale(EvilSnowmanEntity creepsentityevilsnowman, float f)
    {
        shadowSize = creepsentityevilsnowman.snowsize * 0.5F;
        GL11.glScalef(creepsentityevilsnowman.snowsize, creepsentityevilsnowman.snowsize, creepsentityevilsnowman.snowsize);
    }
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        preRenderScale((EvilSnowmanEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(EvilSnowmanEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((EvilSnowmanEntity) entity);
	}
}
