package fr.elias.morecreeps.client.render;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelArmyGuyArm;
import fr.elias.morecreeps.common.entity.ArmyGuyArmEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class CREEPSRenderArmyGuyArm extends RenderLiving
{
    protected CREEPSModelArmyGuyArm modelBipedMain;

    public CREEPSRenderArmyGuyArm(CREEPSModelArmyGuyArm creepsmodelarmyguyarm, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelarmyguyarm, f);
       // setRenderPassModel(new CREEPSModelArmyGuyArm());
        modelBipedMain = creepsmodelarmyguyarm;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        ArmyGuyArmEntity creepsentityarmyguyarm = (ArmyGuyArmEntity)entityliving;
        modelBipedMain.modelsize = creepsentityarmyguyarm.modelsize;
        fattenup((ArmyGuyArmEntity)entityliving, f);
    }

    protected void fattenup(ArmyGuyArmEntity creepsentityarmyguyarm, float f)
    {
        GL11.glScalef(creepsentityarmyguyarm.modelsize, creepsentityarmyguyarm.modelsize, creepsentityarmyguyarm.modelsize);
    }

	protected ResourceLocation getEntityTexture(ArmyGuyArmEntity entity)
	{
		return entity.texture;
	}
    
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		
		return getEntityTexture((ArmyGuyArmEntity) entity);
	}
}
