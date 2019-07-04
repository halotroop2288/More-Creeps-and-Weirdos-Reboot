package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelEvilScientist;
import fr.elias.morecreeps.common.entity.EvilPigEntity;
import fr.elias.morecreeps.common.entity.EvilScientistEntity;

public class CREEPSRenderEvilScientist extends RenderLiving
{
    protected CREEPSModelEvilScientist modelBipedMain;

    public CREEPSRenderEvilScientist(CREEPSModelEvilScientist creepsmodelevilscientist, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodelevilscientist, f);
        modelBipedMain = creepsmodelevilscientist;
    }

    protected void fattenup(EvilScientistEntity creepsentityevilscientist, float f)
    {
        GL11.glScalef(creepsentityevilscientist.modelsize, creepsentityevilscientist.modelsize, creepsentityevilscientist.modelsize);
    }
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        fattenup((EvilScientistEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(EvilScientistEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((EvilScientistEntity) entity);
	}
}
