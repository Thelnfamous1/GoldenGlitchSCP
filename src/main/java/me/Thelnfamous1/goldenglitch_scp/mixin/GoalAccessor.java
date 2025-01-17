package me.Thelnfamous1.goldenglitch_scp.mixin;

import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Goal.class)
public interface GoalAccessor {

    @Invoker("adjustedTickDelay")
    int goldenglitch_scp$callAdjustedTickDelay(int pAdjustment);
}
