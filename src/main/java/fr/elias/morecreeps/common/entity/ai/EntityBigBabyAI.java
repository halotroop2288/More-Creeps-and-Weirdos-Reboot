package fr.elias.morecreeps.common.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAIBase;
import fr.elias.morecreeps.common.entity.BabyMummyEntity;
import fr.elias.morecreeps.common.entity.BigBabyEntity;

public class EntityBigBabyAI extends EntityAIBase {
	BigBabyEntity bbaby;
	public EntityBigBabyAI(BigBabyEntity bigBaby)
	{
		bbaby = bigBaby;
	}
	
	@Override
	public boolean shouldExecute()
	{
		LivingEntity entitylivingbase = this.bbaby.getAttackTarget();
		return entitylivingbase != null && entitylivingbase.isEntityAlive();
	}
	
	public void updateTask()
    {
    	--bbaby.attackTime;
        LivingEntity entitylivingbase = this.bbaby.getAttackTarget();
        double d0 = this.bbaby.getDistanceSq(entitylivingbase);

        if (d0 < 4.0D)
        {
            if (bbaby.attackTime <= 0)
            {
            	bbaby.attackTime = 20;
                this.bbaby.attackEntityAsMob(entitylivingbase);
            }
            
            this.bbaby.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
        }
        else if (d0 < 256.0D)
        {
            // ATTACK ENTITY GOES HERE
        	bbaby.attackEntity(entitylivingbase, (float)d0);
            this.bbaby.getLookController().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
        }
        else
        {
            this.bbaby.getNavigator().clearPath();
            this.bbaby.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
        }
    }
}
