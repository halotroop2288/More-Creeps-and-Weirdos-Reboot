package fr.elias.morecreeps.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelBlorp;
import fr.elias.morecreeps.common.entity.BlorpEntity;

public class CREEPSRenderBlorp extends RenderLiving
{
    protected CREEPSModelBlorp modelBipedMain;
    public boolean scaled;

    public CREEPSRenderBlorp(CREEPSModelBlorp creepsmodelblorp, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodelblorp, f);
        modelBipedMain = creepsmodelblorp;
    }

    protected float getWingRotation(BlorpEntity creepsentityblorp, float f)
    {
        float f1 = 0.35F;
        float f2 = 0.35F;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        updateBlorpScale((BlorpEntity)entityliving, f);
    }

    protected void updateBlorpScale(BlorpEntity creepsentityblorp, float f)
    {
        GL11.glScalef(creepsentityblorp.blorpsize, creepsentityblorp.blorpsize, creepsentityblorp.blorpsize);
    }

    protected ResourceLocation getEntityTexture(BlorpEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}
    
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((BlorpEntity) entity);
	}
}
