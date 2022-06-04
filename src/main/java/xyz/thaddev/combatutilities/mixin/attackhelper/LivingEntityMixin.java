package xyz.thaddev.combatutilities.mixin.attackhelper;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.thaddev.combatutilities.CU;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() != null && CU.i.mc.world != null) {
//            CU.i.printMessage(ColorHelper.from("(%$green)" + source.getAttacker().getName().getString() + " (%$gray)is attacking (%$green)" + this.getName().getString()));
            if (CU.i.mc.player != null && this.getUuidAsString().equals(CU.i.mc.player.getUuidAsString())) {

                if (!this.isInvulnerableTo(source)) {
                    float actualAmount = amount;
                    actualAmount = DamageUtil.getDamageLeft(actualAmount, (float) ((LivingEntity) ((Entity) this)).getArmor(), (float) ((LivingEntity) ((Entity) this)).getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
                    actualAmount = applyEnchToDamage(source, actualAmount);

                    CU.i.attackManager.damageLastAttackedBy((LivingEntity) source.getAttacker(), actualAmount, amount);
                    //CU.i.printMessage(ColorHelper.from("(%$green)" + source.getAttacker().getName().getString() + " (%$gray)is attacking (%$green)" + this.getName().getString() + " (%$gray)for (%$gold)" + actualAmount + " " + amount));
                }


            }
        }
    }

    float applyEnchToDamage(DamageSource source, float amount) {
        if (source.isUnblockable()) {
            return amount;
        } else {
            int i;
            if (((LivingEntity) ((Entity) this)).hasStatusEffect(StatusEffects.RESISTANCE) && source != DamageSource.OUT_OF_WORLD) {
                i = (Objects.requireNonNull(((LivingEntity) ((Entity) this)).getStatusEffect(StatusEffects.RESISTANCE)).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = amount * (float) j;
                float g = amount;
                amount = Math.max(f / 25.0F, 0.0F);
                float h = g - amount;
            }

            if (amount <= 0.0F) {
                return 0.0F;
            } else {
                i = EnchantmentHelper.getProtectionAmount(this.getArmorItems(), source);
                if (i > 0) {
                    amount = DamageUtil.getInflictedDamage(amount, (float) i);
                }

                return amount;
            }
        }
    }

}
