package xyz.thaddev.combatutilities.mixin.attackhelper;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.thaddev.combatutilities.CU;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract float getAttackCooldownProgress(float baseTime);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void attack(Entity target, CallbackInfo ci) {
        if (target instanceof LivingEntity && CU.i.mc.player != null && CU.i.mc.world != null && this.getUuidAsString().equals(CU.i.mc.player.getUuidAsString()) && !target.isInvulnerableTo(DamageSource.player(CU.i.mc.player))) {
            //attack damage calculation
            float attackDamage = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            float g = EnchantmentHelper.getAttackDamage(this.getMainHandStack(), ((LivingEntity) target).getGroup());
            float h = this.getAttackCooldownProgress(0.5f);
            g *= h;
            boolean bl = h > 0.9f;
            boolean bl3 = bl && CU.i.mc.player.fallDistance > 0.0f && !CU.i.mc.player.isOnGround() && !CU.i.mc.player.isClimbing() && !CU.i.mc.player.isTouchingWater() && !CU.i.mc.player.hasStatusEffect(StatusEffects.BLINDNESS) && !CU.i.mc.player.hasVehicle();
            if (bl3) {
                attackDamage *= 1.5f;
            }
            attackDamage += g;

            //actual dealt damage calculation
            double armor = ((LivingEntity) target).getArmor();
            double toughness = ((LivingEntity) target).getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
            double enchantmentProtectionFactor = 0;
            double resistance = ((LivingEntity) target).getStatusEffect(StatusEffects.RESISTANCE) == null ? 0 : Objects.requireNonNull(((LivingEntity) target).getStatusEffect(StatusEffects.RESISTANCE)).getAmplifier();
            for (ItemStack itemStack : target.getArmorItems()) {
                if (itemStack.getItem() instanceof ArmorItem armorItem) {
                    toughness += armorItem.getToughness();
                    enchantmentProtectionFactor += EnchantmentHelper.getLevel(Enchantments.PROTECTION, itemStack);
                    enchantmentProtectionFactor += EnchantmentHelper.getLevel(Enchantments.PROJECTILE_PROTECTION, itemStack) * 2;
                    enchantmentProtectionFactor += EnchantmentHelper.getLevel(Enchantments.BLAST_PROTECTION, itemStack) * 2;
                    enchantmentProtectionFactor += EnchantmentHelper.getLevel(Enchantments.FIRE_PROTECTION, itemStack) * 2;
                }
            }
            double damage = Math.round(attackDamage * (1 - Math.min(20, Math.max(armor / 5, armor - attackDamage / (2 + toughness / 4))) / 25) * (1 - Math.min(enchantmentProtectionFactor, 20) / 25) * (1 - Math.min(resistance, 5) / 5));

            CU.i.attackManager.damageLastAttacked((LivingEntity) target, damage, attackDamage);
            //CU.i.printMessage(ColorHelper.from("(%$green)" + this.getName().getString() + " (%$gray)is attacking ((%$green)" + h + "(%$gray)) (%$green)" + target.getName().getString() + " (%$gray)for (%$gold)" + damage + " " + attackDamage));
        }
    }

}
