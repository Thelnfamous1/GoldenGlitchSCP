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

    public static final float SCP_173_SCALE = scaleToFitIn2x1(12.0F/16.0F, 34.0F/16.0F);
    public static final DeferredHolder<EntityType<?>, EntityType<SCP173>> SCP_173 = registerEntityType(
            EntityType.Builder.of(SCP173::new, MobCategory.MONSTER)
                    .sized(SCP_173_SCALE * 12.0F/16.0F, SCP_173_SCALE * 34.0F/16.0F)
                    .fireImmune()
                    .canSpawnFarFromPlayer()
                    .clientTrackingRange(16),
            "scp_173");

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntityType(EntityType.Builder<T> builder, String name) {
        return ENTITY_TYPES.register(name, () -> builder.build(SCPMod.MODID + ":" + name));
    }

    private static float scaleToFitIn2x1(float width, float height){
        if(width <= 0.975F && height <= 1.95F){
            return 1.0F;
        }
        return Math.min(0.975F / width, 1.95F / height);
    }
}
