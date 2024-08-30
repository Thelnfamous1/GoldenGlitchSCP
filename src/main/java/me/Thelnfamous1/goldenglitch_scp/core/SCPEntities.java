package me.Thelnfamous1.goldenglitch_scp.core;

import me.Thelnfamous1.goldenglitch_scp.SCPMod;
import me.Thelnfamous1.goldenglitch_scp.entities.SCP173;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SCPEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, SCPMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<SCP173>> SCP_173 = registerEntityType(
            EntityType.Builder.of(SCP173::new, MobCategory.MONSTER)
                    .sized(1.0F, 34.0F/16.0F)
                    .fireImmune()
                    .canSpawnFarFromPlayer()
                    .clientTrackingRange(16),
            "scp_173");

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntityType(EntityType.Builder<T> builder, String name) {
        return ENTITY_TYPES.register(name, () -> builder.build(SCPMod.MODID + ":" + name));
    }
}
