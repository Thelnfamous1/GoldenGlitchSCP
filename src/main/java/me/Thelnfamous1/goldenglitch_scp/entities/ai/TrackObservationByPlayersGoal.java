package me.Thelnfamous1.goldenglitch_scp.entities.ai;

import me.Thelnfamous1.goldenglitch_scp.entities.ObservationTracking;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class TrackObservationByPlayersGoal<T extends Mob & ObservationTracking> extends Goal {
    protected final T mob;
    protected final int cooldownDuration;
    protected int cooldown = 0;
    protected final Predicate<BlockState> seeThroughBlocks;

    public TrackObservationByPlayersGoal(T mob){
        this(mob, 0);
    }

    public TrackObservationByPlayersGoal(T mob, int cooldownDuration){
        this(mob, cooldownDuration, state -> false);
    }

    public TrackObservationByPlayersGoal(T mob, Predicate<BlockState> seeThroughBlocks){
        this(mob, 0, seeThroughBlocks);
    }

    public TrackObservationByPlayersGoal(T mob, int cooldownDuration, Predicate<BlockState> seeThroughBlocks){
        this.mob = mob;
        this.cooldownDuration = cooldownDuration;
        this.seeThroughBlocks = seeThroughBlocks;
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        } else{
            return true;
        }
    }

    @Override
    public void tick() {
        boolean seen = false;
        double followRangeSqr = Mth.square(this.getFollowRange());
        for(Player player : this.mob.level().players()){
            if(EntitySelector.NO_SPECTATORS.test(player)){
                if(this.mob.distanceToSqr(player) <= followRangeSqr){
                    this.mob.notifyDetectablePlayer(player);
                }
                if(this.isVisibleBy(player)){
                    seen = true;
                }
            }
        }
        this.mob.setIsObserved(seen);
        this.cooldown = this.adjustedTickDelay(this.cooldownDuration);
    }

    protected double getFollowRange() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    protected boolean isVisibleBy(Player player) {
        Vec3 lookVector = player.getLookAngle().normalize();
        Vec3 vectorToMobEye = player.getEyePosition().vectorTo(this.mob.getEyePosition()).normalize();
        double dotProduct = lookVector.dot(vectorToMobEye);
        // if the dot product is greater than 0, the mob is in front of the player
        // if the dot product is 0, the mob is directly to the side of the player
        // if the dot product is less than 0, the mob is behind the player
        return dotProduct > 0 && this.hasLineOfSight(player);
    }

    protected boolean hasLineOfSight(Player player) {
        // can't use LivingEntity#hasLineOfSight because it enforces a distance limit of 128 blocks
        if (this.mob.level() != player.level()) {
            return false;
        } else {
            Vec3 from = player.getEyePosition();
            Vec3 to = this.mob.getEyePosition();
            BlockHitResult clipResult = player.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            return clipResult.getType() == HitResult.Type.MISS || this.seeThroughBlocks.test(player.level().getBlockState(clipResult.getBlockPos()));
        }
    }
}
