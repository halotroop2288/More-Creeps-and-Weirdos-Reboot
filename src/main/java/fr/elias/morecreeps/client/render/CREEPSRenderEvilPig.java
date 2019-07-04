package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelEvilPig;
import fr.elias.morecreeps.common.entity.EvilLightEntity;
import fr.elias.morecreeps.common.entity.EvilPigEntity;

public class CREEPSRenderEvilPig extends RenderLiving
{
    protected CREEPSModelEvilPig modelBipedMain;

    public CREEPSRenderEvilPig(CREEPSModelEvilPig creepsmodelevilpig, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelevilpig, f);
        modelBipedMain = creepsmodelevilpig;
    }

    protected void fattenup(EvilPigEntity creepsentityevilpig, float f)
    {
        GL11.glScalef(creepsentityevilpig.modelsize, creepsentityevilpig.modelsize, creepsentityevilpig.modelsize);
    }
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        fattenup((EvilPigEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(EvilPigEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((EvilPigEntity) entity);
	}
}
