package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelHorseHead;
import fr.elias.morecreeps.common.entity.CREEPSEntityHorseHead;
import fr.elias.morecreeps.common.entity.HorseHeadEntity;
import fr.elias.morecreeps.common.entity.HunchbackSkeletonEntity;

public class CREEPSRenderHorseHead extends RenderLiving
{
    protected CREEPSModelHorseHead modelBipedMain;

    public CREEPSRenderHorseHead(CREEPSModelHorseHead creepsmodelhorsehead, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodelhorsehead, f);
        modelBipedMain = creepsmodelhorsehead;
    }

    protected void fattenup(HorseHeadEntity creepsentityhorsehead, float f)
    {
        GL11.glScalef(1.6F, 1.6F, 1.6F);
    }
    
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        fattenup((HorseHeadEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(HorseHeadEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((HorseHeadEntity) entity);
	}
}
