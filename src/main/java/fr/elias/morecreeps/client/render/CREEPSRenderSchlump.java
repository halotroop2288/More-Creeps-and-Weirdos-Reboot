package fr.elias.morecreeps.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelSchlump;
import fr.elias.morecreeps.common.entity.RockMonsterEntity;
import fr.elias.morecreeps.common.entity.SchlumpEntity;

@SuppressWarnings("rawtypes")
public class CREEPSRenderSchlump extends RenderLivingEvent
{
    protected CREEPSModelSchlump modelBipedMain;

    public CREEPSRenderSchlump(CREEPSModelSchlump creepsmodelschlump, float f)
    {
        super(Minecraft.getMinecraft().getRenderManager(), creepsmodelschlump, f);
        modelBipedMain = creepsmodelschlump;
    }

    protected void fattenup(SchlumpEntity creepsentityschlump, float f)
    {
        GL11.glScalef(creepsentityschlump.modelsize, creepsentityschlump.modelsize, creepsentityschlump.modelsize);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(LivingEntity entityliving, float f)
    {
        SchlumpEntity creepsentityschlump = (SchlumpEntity)entityliving;
        modelBipedMain.age = creepsentityschlump.age;
        fattenup((SchlumpEntity)entityliving, f);
    }
    
    protected int shouldRenderPass(LivingEntity entityliving, int i, float f)
    {
        return eyeGlow((SchlumpEntity)entityliving, i, f);
    }

    protected int eyeGlow(SchlumpEntity creepsentityschlump, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        if (i != 0)
        {
            return -1;
        }
        else
        {
//            loadTexture("/mob/creeps/schlumpnight.png"); // TODO load texture properly
            float f1 = (1.0F - creepsentityschlump.getBrightness()) * 0.5F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
            return 1;
        }
    }

    protected ResourceLocation getEntityTexture(SchlumpEntity entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((SchlumpEntity) entity);
	}
}
