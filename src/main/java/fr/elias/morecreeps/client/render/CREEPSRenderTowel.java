package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import fr.elias.morecreeps.client.models.CREEPSModelTowel;
import fr.elias.morecreeps.common.entity.TowelEntity;

public class CREEPSRenderTowel extends RenderLiving
{
    protected CREEPSModelTowel modelBipedMain;

    public CREEPSRenderTowel(CREEPSModelTowel creepsmodeltowel, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodeltowel, f);
        modelBipedMain = creepsmodeltowel;
    }

    protected ResourceLocation getEntityTexture(TowelEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((TowelEntity) entity);
	}
}
