package fr.elias.morecreeps.common.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAIBase;
import fr.elias.morecreeps.common.entity.BabyMummyEntity;

public class EntityBabyMummyAI extends EntityAIBase
{
	BabyMummyEntity bmummy;
	public EntityBabyMummyAI(BabyMummyEntity babyMummy)
	{
		bmummy = babyMummy;
	}
	
	@Override
	public boolean shouldExecute()
	{
		LivingEntity entitylivingbase = this.bmummy.getAttackTarget();
		return entitylivingbase != null && entitylivingbase.isAlive();
	}
	
	public void updateTask()
    {
    	--bmummy.attackTime;
        LivingEntity entitylivingbase = this.bmummy.getAttackTarget();
        double d0 = this.bmummy.getDistanceSq(entitylivingbase);

        if (d0 < 4.0D)
        {
            if (bmummy.attackTime <= 0)
            {
            	bmummy.attackTime = 20;
                this.bmummy.attackEntityAsMob(entitylivingbase);
            }
            
            this.bmummy.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
        }
        else if (d0 < 256.0D)
        {
            // ATTACK ENTITY GOES HERE
        	bmummy.attackEntity(entitylivingbase, (float)d0);
            this.bmummy.getLookController().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
        }
        else
        {
            this.bmummy.getNavigator().clearPathEntity();
            this.bmummy.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
        }
    }
}
