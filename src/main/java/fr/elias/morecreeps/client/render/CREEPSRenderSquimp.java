package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import fr.elias.morecreeps.client.models.CREEPSModelSquimp;
import fr.elias.morecreeps.common.entity.SquimpEntity;

public class CREEPSRenderSquimp extends RenderLiving
{
    public CREEPSRenderSquimp(CREEPSModelSquimp creepsmodelsquimp, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodelsquimp, f);
    }

    protected ResourceLocation getEntityTexture(SquimpEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((SquimpEntity) entity);
	}
}
