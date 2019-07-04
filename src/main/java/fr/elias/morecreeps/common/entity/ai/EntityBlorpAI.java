package fr.elias.morecreeps.common.entity.ai;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.MobEntity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.PlayerEntity;
import fr.elias.morecreeps.common.entity.BlorpEntity;

public class EntityBlorpAI extends EntityAIBase {

	public BlorpEntity blorp;
	public Random rand = new Random();
	public EntityBlorpAI(BlorpEntity creepsblorp)
	{
		blorp = creepsblorp;
	}
	@Override
	public boolean shouldExecute()
	{
		return true;
	}
	
	public void updateTask()
    {
		findPlayerToAttack();
		LivingEntity entityToAttack = blorp.getAttackTarget();
		double d0 = this.blorp.getDistanceSq(entityToAttack);

        if (d0 < 4.0D)
        {
            if (blorp.attackTime <= 0)
            {
            	blorp.attackTime = 20;
                this.blorp.attackEntityAsMob(entityToAttack);
            }
            
            this.blorp.getMoveHelper().setMoveTo(entityToAttack.posX, entityToAttack.posY, entityToAttack.posZ, 1.0D);
        }
        else if (d0 < 256.0D)
        {
            // ATTACK ENTITY GOES HERE
        	blorp.attackEntity(entityToAttack, (float)d0);
            this.blorp.getLookController().setLookPositionWithEntity(entityToAttack, 10.0F, 10.0F);
        }
        else
        {
            this.blorp.getNavigator().clearPath();
            this.blorp.getMoveHelper().setMoveTo(entityToAttack.posX, entityToAttack.posY, entityToAttack.posZ, 0.5D);
        }
        
    }
    protected Entity findPlayerToAttack()
    {
        float f = blorp.getBrightness();

        if (f < 0.0F || blorp.angry)
        {
            PlayerEntity entityplayer = blorp.world.getClosestPlayer(blorp, blorp.attackrange);

            if (entityplayer != null)
            {
                return entityplayer;
            }
        }

        if (rand.nextInt(10) == 0)
        {
            LivingEntity entityliving = getClosestTarget(blorp, 6D);
            return entityliving;
        }
        else
        {
            return null;
        }
    }
    public LivingEntity getClosestTarget(Entity entity, double d)
    {
        double d1 = -1D;
        LivingEntity entityliving = null;

        for (int i = 0; i < blorp.world.loadedEntityList.size(); i++)
        {
            Entity entity1 = (Entity)blorp.world.loadedEntityList.get(i);

            if (!(entity1 instanceof LivingEntity) || entity1 == entity || entity1 == entity.riddenByEntity || entity1 == entity.getRidingEntity() || (entity1 instanceof PlayerEntity) || (entity1 instanceof MobEntity) || (entity1 instanceof EntityAnimal) && !(entity1 instanceof BlorpEntity))
            {
                continue;
            }

            double d2 = entity1.getDistanceSq(entity.posX, entity.posY, entity.posZ);

            if ((d < 0.0D || d2 < d * d) && (d1 == -1D || d2 < d1) && ((LivingEntity)entity1).canEntityBeSeen(entity))
            {
                d1 = d2;
                entityliving = (LivingEntity)entity1;
            }
        }

        return entityliving;
    }
}
