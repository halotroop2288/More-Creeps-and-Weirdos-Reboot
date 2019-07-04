package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelCastleKing;
import fr.elias.morecreeps.common.entity.CastleGuardEntity;
import fr.elias.morecreeps.common.entity.CastleKingEntity;

public class CREEPSRenderCastleKing extends RenderLiving
{
    protected CREEPSModelCastleKing modelcastlekingmain;

    public CREEPSRenderCastleKing(CREEPSModelCastleKing creepsmodelcastleking, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodelcastleking, f);
        modelcastlekingmain = creepsmodelcastleking;
    }

    protected void fattenup(CastleKingEntity creepsentitycastleking, float f)
    {
        GL11.glScalef(2.0F, 1.5F, 2.0F);
    }
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        CastleKingEntity creepsentitycastleking = (CastleKingEntity)entityliving;
        modelcastlekingmain.hammerswing = creepsentitycastleking.hammerswing;
        fattenup((CastleKingEntity)entityliving, f);
    }

    protected ResourceLocation getEntityTexture(CastleKingEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((CastleKingEntity) entity);
	}
}
