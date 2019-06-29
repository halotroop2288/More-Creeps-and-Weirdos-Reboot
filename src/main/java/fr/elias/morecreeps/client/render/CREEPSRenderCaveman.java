package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelCaveman;
import fr.elias.morecreeps.common.entity.CastleKingEntity;
import fr.elias.morecreeps.common.entity.CavemanEntity;

public class CREEPSRenderCaveman extends RenderLiving
{
	CavemanEntity ecaveman;
    protected CREEPSModelCaveman modelcavemanmain;

    public CREEPSRenderCaveman(CREEPSModelCaveman creepsmodelcaveman, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelcaveman, f);
        modelcavemanmain = creepsmodelcaveman;
    }

    protected void fattenup(CavemanEntity creepsentitycaveman, float f)
    {
        GL11.glScalef(creepsentitycaveman.modelsize + creepsentitycaveman.fat, creepsentitycaveman.modelsize, creepsentitycaveman.modelsize);
    }
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        CavemanEntity creepsentitycaveman = (CavemanEntity)entityliving;
        modelcavemanmain.hammerswing = creepsentitycaveman.hammerswing;
        modelcavemanmain.frozen = creepsentitycaveman.frozen;
        modelcavemanmain.cavegirl = creepsentitycaveman.cavegirl;
        modelcavemanmain.evil = creepsentitycaveman.evil;
        fattenup((CavemanEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(CavemanEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((CavemanEntity) entity);
	}
}
