package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelEvilCreature;
import fr.elias.morecreeps.common.entity.DogHouseEntity;
import fr.elias.morecreeps.common.entity.EvilCreatureEntity;

public class CREEPSRenderEvilCreature extends RenderLiving
{
    protected CREEPSModelEvilCreature modelBipedMain;
    public boolean scaled;
    private ModelBase scaleAmount;

    public CREEPSRenderEvilCreature(CREEPSModelEvilCreature creepsmodelevilcreature, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelevilcreature, f);
        modelBipedMain = creepsmodelevilcreature;
        scaleAmount = creepsmodelevilcreature;
    }

    /**
     * sets the scale for the slime based on getSlimeSize in EntitySlime
     */
    protected void scaleSlime(EvilCreatureEntity creepsentityevilcreature, float f)
    {
        GL11.glScalef(creepsentityevilcreature.modelsize, creepsentityevilcreature.modelsize, creepsentityevilcreature.modelsize);
    }
    
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        scaleSlime((EvilCreatureEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(EvilCreatureEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((EvilCreatureEntity) entity);
	}
}
