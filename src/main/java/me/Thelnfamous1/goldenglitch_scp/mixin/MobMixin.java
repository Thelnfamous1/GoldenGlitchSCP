package me.Thelnfamous1.goldenglitch_scp.mixin;

import me.Thelnfamous1.goldenglitch_scp.items.SCP500;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "checkAndHandleImportantInteractions", at = @At("TAIL"), cancellable = true)
    private void tail_checkAndHandleImportantInteractions(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir){
        if(!cir.getReturnValue().consumesAction()){
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            if (itemstack.getItem() instanceof SCP500) {
                InteractionResult interactionResult = itemstack.interactLivingEntity(pPlayer, this, pHand);
                if (interactionResult.consumesAction()) {
                    cir.setReturnValue(interactionResult);
                }
            }
        }
    }
}
