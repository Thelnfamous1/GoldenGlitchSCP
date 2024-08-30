package me.Thelnfamous1.goldenglitch_scp.entities.ai;

import me.Thelnfamous1.goldenglitch_scp.entities.ObservationTracking;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FreezeWhenObservedGoal<T extends Mob & ObservationTracking> extends Goal {
    private final T mob;

    public FreezeWhenObservedGoal(T mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.isObserved();
    }

    @Override
    public void tick() {
        this.mob.stopInPlace();
    }
}