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

    public static final float SCP_173_MODEL_WIDTH = 12.0F / 16.0F;
    public static final float SCP_173_MODEL_HEIGHT = 34.0F / 16.0F;
    public static final float SCP_173_SCALE = scaleToFitThroughDoor(SCP_173_MODEL_WIDTH, SCP_173_MODEL_HEIGHT);
    public static final DeferredHolder<EntityType<?>, EntityType<SCP173>> SCP_173 = registerEntityType(
            EntityType.Builder.of(SCP173::new, MobCategory.MONSTER)
                    .sized(SCP_173_SCALE * SCP_173_MODEL_WIDTH, SCP_173_SCALE * SCP_173_MODEL_HEIGHT)
                    .fireImmune()
                    .canSpawnFarFromPlayer()
                    .clientTrackingRange(16),
            "scp_173");
    public static final float MAX_WIDTH_TO_PASS_THROUGH_DOOR = 0.625F;
    public static final float MAX_HEIGHT_TO_PASS_THROUGH_DOOR = 2.0F;

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntityType(EntityType.Builder<T> builder, String name) {
        return ENTITY_TYPES.register(name, () -> builder.build(SCPMod.MODID + ":" + name));
    }

    private static float scaleToFitThroughDoor(float width, float height){
        if(width <= MAX_WIDTH_TO_PASS_THROUGH_DOOR && height <= MAX_HEIGHT_TO_PASS_THROUGH_DOOR){
            return 1.0F;
        }
        return Math.min(MAX_WIDTH_TO_PASS_THROUGH_DOOR / width, MAX_HEIGHT_TO_PASS_THROUGH_DOOR / height);
    }
}
