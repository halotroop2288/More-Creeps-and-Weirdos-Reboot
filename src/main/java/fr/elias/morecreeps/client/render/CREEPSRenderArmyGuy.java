package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import fr.elias.morecreeps.client.models.CREEPSModelArmyGuy;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ArmyGuyEntity;

public class CREEPSRenderArmyGuy extends RenderLiving
{
    protected CREEPSModelArmyGuy modelBipedMain;
    public static final ResourceLocation texture = new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_ARMY_GUY_DEFAULT);
    public static final ResourceLocation texture_loyal = new ResourceLocation(Reference.MODID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_ARMY_GUY_LOYAL);
    
    public CREEPSRenderArmyGuy(CREEPSModelArmyGuy creepsmodelarmyguy, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodelarmyguy, f);
        modelBipedMain = creepsmodelarmyguy;
        this.addLayer(new LayerHeldItemArmyGuy(this));
    }

    protected void preRenderCallback(ArmyGuyEntity entityliving, float f)
    {
        ArmyGuyEntity creepsentityarmyguy = (ArmyGuyEntity)entityliving;
        modelBipedMain.armright = creepsentityarmyguy.armright;
        modelBipedMain.armleft = creepsentityarmyguy.armleft;
        modelBipedMain.legright = creepsentityarmyguy.legright;
        modelBipedMain.legleft = creepsentityarmyguy.legleft;
        modelBipedMain.helmet = creepsentityarmyguy.helmet;
        modelBipedMain.head = creepsentityarmyguy.head;
        modelBipedMain.modelsize = creepsentityarmyguy.modelsize;
        if (creepsentityarmyguy.legleft && creepsentityarmyguy.legright && creepsentityarmyguy.head)
        {
        	GlStateManager.translated(0.0D, 1.4D, 0.0D);
        }else if (creepsentityarmyguy.legleft && creepsentityarmyguy.legright)
        {
        	GlStateManager.translated(0.0D, 0.75D, 0.0D);
        }
        else
        {
        	GlStateManager.translated(0.0D, 0.0D, 0.0D);
        }
        fattenup(entityliving, f);
    }
    protected void fattenup(ArmyGuyEntity creepsentityarmyguy, float f)
    {
        GL11.glScalef(creepsentityarmyguy.modelsize, creepsentityarmyguy.modelsize, creepsentityarmyguy.modelsize);
    }

    protected void preRenderCallback(LivingEntity p_77041_1_, float p_77041_2_)
    {
        this.preRenderCallback((ArmyGuyEntity)p_77041_1_, p_77041_2_);
    }
    
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        doRender((ArmyGuyEntity)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(ArmyGuyEntity entity) {
		return !entity.loyal ? texture : texture_loyal;
	}
    
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return getEntityTexture((ArmyGuyEntity) entity);
	}
}
