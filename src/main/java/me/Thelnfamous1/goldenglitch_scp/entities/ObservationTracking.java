package me.Thelnfamous1.goldenglitch_scp.entities;

import net.minecraft.world.entity.player.Player;

public interface ObservationTracking {

    boolean isObserved();

    void setIsObserved(boolean observed);

    void notifyDetectablePlayer(Player player);
}
