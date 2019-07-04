package fr.elias.morecreeps.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import fr.elias.morecreeps.client.models.CREEPSModelTrophy;
import fr.elias.morecreeps.common.entity.TrophyEntity;

public class CREEPSRenderTrophy extends RenderLiving
{
    protected CREEPSModelTrophy modelBipedMain;

    public CREEPSRenderTrophy(CREEPSModelTrophy creepsmodeltrophy, float f)
    {
        super(Minecraft.getInstance().getRenderManager(), creepsmodeltrophy, f);
        modelBipedMain = creepsmodeltrophy;
    }

    protected ResourceLocation getEntityTexture(TrophyEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((TrophyEntity) entity);
	}
}
