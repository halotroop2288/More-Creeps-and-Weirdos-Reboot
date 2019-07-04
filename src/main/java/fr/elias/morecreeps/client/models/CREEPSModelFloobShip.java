package fr.elias.morecreeps.client.models;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;

public class CREEPSModelFloobShip extends Model
{
    public ModelRenderer headFloobship;
    public ModelRenderer headFloobship1;
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;

    public CREEPSModelFloobShip()
    {
        this(11);
    }

    public CREEPSModelFloobShip(int i)
    {
        this(i, 0.0F);
    }

    public CREEPSModelFloobShip(int i, float f)
    {
        headFloobship = new ModelRenderer(this, 0, 0);
        headFloobship.addBox(-4F, -6F, -4F, 8, 4, 8, f);
        headFloobship.setRotationPoint(0.0F, i - 2, 0.0F);
        headFloobship1 = new ModelRenderer(this, 14, 0);
        headFloobship1.addBox(-9F, -2F, -9F, 18, 3, 18, f);
        headFloobship1.setRotationPoint(0.0F, i - 2, 0.0F);
        body = new ModelRenderer(this, 0, 24);
        body.addBox(-6F, 1.0F, -6F, 12, 4, 12, f + 1.1F);
        body.setRotationPoint(0.0F, i - 2, 0.0F);
        leg1 = new ModelRenderer(this, 56, 0);
        leg1.addBox(-1F, 0.0F, -1F, 2, i + 2, 2, f - 0.4F);
        leg1.setRotationPoint(-4F, 24 - i, 4F);
        leg2 = new ModelRenderer(this, 56, 0);
        leg2.addBox(-1F, 0.0F, -1F, 2, i + 2, 2, f - 0.4F);
        leg2.setRotationPoint(4F, 24 - i, 4F);
        leg3 = new ModelRenderer(this, 56, 0);
        leg3.addBox(-1F, 0.0F, -1F, 2, i + 2, 2, f - 0.4F);
        leg3.setRotationPoint(-4F, 24 - i, -4F);
        leg4 = new ModelRenderer(this, 56, 0);
        leg4.addBox(-1F, 0.0F, -1F, 2, i + 2, 2, f - 0.4F);
        leg4.setRotationPoint(4F, 24 - i, -4F);
        leg1.rotateAngleX = 0.5F;
        leg2.rotateAngleX = 0.5F;
        leg3.rotateAngleX = -0.5F;
        leg4.rotateAngleX = -0.5F;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        headFloobship.render(f5);
        headFloobship1.render(f5);
        body.render(f5);
        leg1.render(f5);
        leg2.render(f5);
        leg3.render(f5);
        leg4.render(f5);
    }

    /**
     * Sets the models various rotation angles.
     */
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        headFloobship.rotateAngleY = f2 / 10F;
        headFloobship1.rotateAngleX = 0.0F;
        headFloobship1.rotateAngleY = 0.0F;
        body.rotateAngleX = 0.0F;
        body.rotateAngleY = 0.0F;
        body.rotateAngleZ = 0.0F;
    }
}
