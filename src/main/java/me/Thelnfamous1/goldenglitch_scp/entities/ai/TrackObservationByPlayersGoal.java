package me.Thelnfamous1.goldenglitch_scp.entities.ai;

import me.Thelnfamous1.goldenglitch_scp.entities.ObservationTracking;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TrackObservationByPlayersGoal<T extends Mob & ObservationTracking> extends Goal {
    protected final T mob;
    private final int cooldownDuration;
    private int cooldown = 0;

    public TrackObservationByPlayersGoal(T mob, int cooldownDuration){
        this.mob = mob;
        this.cooldownDuration = cooldownDuration;
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
        for(Player player : this.mob.level().players()){
            if(EntitySelector.NO_SPECTATORS.test(player) && this.isVisibleBy(player)){
                seen = true;
                break;
            }
        }
        this.mob.setIsObserved(seen);
        this.cooldown = this.adjustedTickDelay(this.cooldownDuration);
    }

    protected boolean isVisibleBy(Player player) {
        Vec3 lookVector = player.getLookAngle().normalize();
        Vec3 vectorToMobEye = player.getEyePosition().vectorTo(this.mob.getEyePosition()).normalize();
        double dotProduct = lookVector.dot(vectorToMobEye);
        // if the dot product is greater than 0, the mob is in front of the player
        // if the dot product is 0, the mob is directly to the side of the player
        // if the dot product is less than 0, the mob is behind the player
        return dotProduct > 0 && player.hasLineOfSight(this.mob);
    }
}
