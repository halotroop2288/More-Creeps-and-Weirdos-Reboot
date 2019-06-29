package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelCastleGuard;
import fr.elias.morecreeps.common.entity.CastleGuardEntity;

public class CREEPSRenderCastleGuard extends RenderLiving
{
    protected CREEPSModelCastleGuard modelcastleguardmain;

    public CREEPSRenderCastleGuard(CREEPSModelCastleGuard creepsmodelcastleguard, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelcastleguard, f);
        modelcastleguardmain = creepsmodelcastleguard;
    }

    protected void fattenup(CastleGuardEntity creepsentitycastleguard, float f)
    {
        GL11.glScalef(creepsentitycastleguard.modelsize, creepsentitycastleguard.modelsize, creepsentitycastleguard.modelsize);
    }
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        CastleGuardEntity creepsentitycastleguard = (CastleGuardEntity)entityliving;
        modelcastleguardmain.hammerswing = creepsentitycastleguard.hammerswing;
        fattenup((CastleGuardEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(CastleGuardEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((CastleGuardEntity) entity);
	}
}
