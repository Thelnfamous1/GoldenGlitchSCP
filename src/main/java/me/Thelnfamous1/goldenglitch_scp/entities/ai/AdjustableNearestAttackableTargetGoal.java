package me.Thelnfamous1.goldenglitch_scp.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class AdjustableNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public AdjustableNearestAttackableTargetGoal(Mob pMob, Class<T> pTargetType, boolean pMustSee) {
        this(pMob, pTargetType, 10, pMustSee, false, null);
    }

    public AdjustableNearestAttackableTargetGoal(Mob pMob, Class<T> pTargetType, boolean pMustSee, Predicate<LivingEntity> pTargetPredicate) {
        this(pMob, pTargetType, 10, pMustSee, false, pTargetPredicate);
    }

    public AdjustableNearestAttackableTargetGoal(Mob pMob, Class<T> pTargetType, boolean pMustSee, boolean pMustReach) {
        this(pMob, pTargetType, 10, pMustSee, pMustReach, null);
    }

    public AdjustableNearestAttackableTargetGoal(
            Mob pMob, Class<T> pTargetType, int pRandomInterval, boolean pMustSee, boolean pMustReach, @Nullable Predicate<LivingEntity> pTargetPredicate
    ) {
        super(pMob, pTargetType, pRandomInterval, pMustSee, pMustReach, pTargetPredicate);
    }

    public AdjustableNearestAttackableTargetGoal<T> ignoreLineOfSight(){
        this.targetConditions.ignoreLineOfSight();
        return this;
    }

    public AdjustableNearestAttackableTargetGoal<T> ignoreInvisibility(){
        this.targetConditions.ignoreInvisibilityTesting();
        return this;
    }
}
