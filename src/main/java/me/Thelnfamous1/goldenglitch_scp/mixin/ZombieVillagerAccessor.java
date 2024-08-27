package me.Thelnfamous1.goldenglitch_scp.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ZombieVillager.class)
public interface ZombieVillagerAccessor {

    @Invoker("finishConversion")
    void goldenglitch_scp$finishConversion(ServerLevel pServerLevel);
}
