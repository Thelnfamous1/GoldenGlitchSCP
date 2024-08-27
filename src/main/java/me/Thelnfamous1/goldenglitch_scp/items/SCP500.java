package me.Thelnfamous1.goldenglitch_scp.items;

import me.Thelnfamous1.goldenglitch_scp.mixin.ZombieVillagerAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;

import java.util.Optional;

public class SCP500 extends Item {
    public SCP500(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (pStack.is(this)) {
            if (pInteractionTarget instanceof Mob interactionMob) {
                if(cureMob(interactionMob)){
                    if(!pPlayer.level().isClientSide) pStack.consume(1, pPlayer);
                    return InteractionResult.sidedSuccess(pInteractionTarget.level().isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static boolean cureMob(Mob interactionMob) {
        if(interactionMob instanceof ZombieVillager zombieVillager){
            return convertMob(SCP500::cureZombieVillager, zombieVillager, EntityType.VILLAGER);
        } else if(interactionMob instanceof Witch || interactionMob instanceof Zombie || interactionMob instanceof Skeleton){
            return basicConversion(interactionMob, EntityType.VILLAGER);
        } else if(interactionMob instanceof Zoglin){
            return basicConversion(interactionMob, EntityType.PIG);
        } else{
            return false;
        }
    }

    private static Optional<Mob> cureZombieVillager(ZombieVillager zombieVillager, EntityType<? extends Mob> type, Level level) {
        if(level instanceof ServerLevel serverLevel){
            ((ZombieVillagerAccessor)zombieVillager).goldenglitch_scp$finishConversion(serverLevel); // will turn the zombie villager into a villager
            level.broadcastEntityEvent(zombieVillager, (byte)16);
        }
        return Optional.empty();
    }

    private static boolean basicConversion(Mob mob, EntityType<? extends Mob> type) {
        return convertMob(SCP500::basicConversion, mob, type);
    }

    private static Optional<Mob> basicConversion(Mob mob, EntityType<? extends Mob> et, Level level) {
        if(!level.isClientSide){
            Mob convertedTo = mob.convertTo(et, false);
            return Optional.ofNullable(convertedTo);
        } else{
            if (!mob.isSilent()) {
                mob.level()
                        .playLocalSound(
                                mob.getX(),
                                mob.getEyeY(),
                                mob.getZ(),
                                SoundEvents.ZOMBIE_VILLAGER_CURE,
                                mob.getSoundSource(),
                                1.0F + mob.getRandom().nextFloat(),
                                mob.getRandom().nextFloat() * 0.7F + 0.3F,
                                false
                        );
            }
            return Optional.empty();
        }
    }

    private static <T extends Mob> boolean convertMob(MobConverter<T> converter, T mob, EntityType<? extends Mob> type) {
        if(EventHooks.canLivingConvert(mob, type, i -> {})){
            Optional<Mob> converted = converter.convert(mob, type, mob.level());
            converted.ifPresent(outcome -> EventHooks.onLivingConvert(mob, outcome));
            return true;
        }
        return false;
    }

    @FunctionalInterface
    public interface MobConverter<T extends Mob>{
        Optional<Mob> convert(T mob, EntityType<? extends Mob> convertToType, Level level);
    }
}
