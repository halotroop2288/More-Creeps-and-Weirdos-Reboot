package fr.elias.morecreeps.common.entity;

import java.util.List;

import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.util.handlers.SoundsHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EvilLightEntity extends LivingEntity {
    public int lifespan;
    public ResourceLocation texture;

    public EvilLightEntity(World world)
    {
        super(null, world);
        texture = new ResourceLocation(Reference.MODID + Reference.TEXTURE_PATH_ENTITES + ("evillight1.png");
        lifespan = 200;
        moveForward = rand.nextFloat() * 2.0F - 1.0F;
        moveStrafing = rand.nextFloat() * 2.0F - 1.0F;
    }

    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }

    /**
     * Called frequently so the entity can update its state every tick as required.
     * For example, zombies and skeletons use this to react to sunlight and start to
     * burn.
     */
    @Override
    public void livingTick() {
        World world;
        PlayerEntity playerentity;

        float health = this.getHealth();
        if (lifespan-- < 1 || handleWaterMovement()) {
            health = 0;
            setHealth(0);
        }

        Object obj = null;
        List list = world.getEntitiesWithinAABBExcludingEntity(this,
                getBoundingBox().addCoord(moveForward, moveVertical, moveStrafing).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int i = 0; i < list.size(); i++) {
            Entity entity = (Entity) list.get(i);

            if (entity.canBeCollidedWith() && (entity instanceof LivingEntity) && !(entity instanceof EvilLightEntity)
                    && !(entity instanceof EvilScientistEntity) && !(entity instanceof EvilChickenEntity)
                    && !(entity instanceof EvilCreatureEntity) && !(entity instanceof EvilPigEntity))
            {
                entity.setFire(3);
                entity.motionX = rand.nextFloat() * 0.7F;
                entity.motionY = rand.nextFloat() * 0.4F;
                entity.motionZ = rand.nextFloat() * 0.7F;
                world.playSound(playerentity, this.getPosition(), SoundsHandler.EVIL_LIGHT, SoundCategory.HOSTILE, 0.2F,
                        1.0F / (rand.nextFloat() * 0.1F + 0.95F));
            }
        }

        super.livingTick();
    }

    public void damageEntity(int i) {
        this.getHealth();
    }

    public void onCollideWithPlayer(PlayerEntity entityIn) {
        entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeAdditional(CompoundNBT nbttagcompound) {
        super.writeAdditional(nbttagcompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT nbttagcompound) {
        super.readAdditional(nbttagcompound);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected SoundEvent getAmbientSound() {
        return SoundsHandler.EVIL_LIGHT;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected SoundEvent getHurtSound() {
        return SoundsHandler.EVIL_LIGHT;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected SoundEvent getDeathSound() {
        return SoundsHandler.EVIL_LIGHT;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return null;
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return null;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getPrimaryHand() {
        return null;
    }
}
