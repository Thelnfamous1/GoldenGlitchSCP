package me.Thelnfamous1.goldenglitch_scp.entities;

import me.Thelnfamous1.goldenglitch_scp.entities.ai.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SCP173 extends Monster implements GeoEntity, ObservationTracking {
    protected static final EntityDataAccessor<Boolean> DATA_OBSERVED = SynchedEntityData.defineId(SCP173.class, EntityDataSerializers.BOOLEAN);
    private static final double SPEED_MODIFIER_ATTACKING = 5.0D;
    private static final double SPEED_MODIFIER_WANDERING = 1.0D;
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public SCP173(EntityType<? extends SCP173> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setInvulnerable(true);
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 100.0D) // can follow players up to 100 blocks away
                .add(Attributes.ATTACK_DAMAGE, 100.0D) // 85 damage is enough to kill a player wearing full enchanted netherite armor who is affected by resistance 1
                .add(Attributes.MOVEMENT_SPEED, 0.1D) // 0.1 * 20 = 2 blocks per second
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TrackObservationByPlayersGoal<>(this, 0));
        this.goalSelector.addGoal(0, new FreezeWhenObservedGoal<>(this));
        this.goalSelector.addGoal(1, PredicatedGoal.runIf(new OpenDoorGoal(this, false), this, SCP173::isUnobserved, true));
        this.goalSelector.addGoal(2, PredicatedGoal.runIf(new MeleeAttackGoal(this, SPEED_MODIFIER_ATTACKING, true), this, SCP173::isUnobserved, true));
        this.goalSelector.addGoal(3, PredicatedGoal.runIf(new WaterAvoidingRandomStrollGoal(this, SPEED_MODIFIER_WANDERING, 0.0F), this, SCP173::isUnobserved, true));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        this.setInvulnerable(true); // SCP-173 should always be invulnerable unless summoned specifically as not invulnerable via a command
        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }

    public final boolean isUnobserved(){
        return !this.isObserved();
    }

    @Override
    public float getWalkTargetValue(BlockPos pPos, LevelReader pLevel) {
        return 0.0F;
    }

    @Override
    protected boolean canRide(Entity pVehicle) {
        return false;
    }

    @Override
    public void checkDespawn() {
        if (EventHooks.checkMobDespawn(this)) return;
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean canDisableShield() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return !this.isInvulnerable() && super.isPushable();
    }

    @Override
    public boolean attackable() {
        return !this.isInvulnerable();
    }

    @Override
    public boolean ignoreExplosion(Explosion pExplosion) {
        return this.isInvulnerable();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_OBSERVED, false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public boolean isObserved() {
        return this.entityData.get(DATA_OBSERVED);
    }

    @Override
    public void setIsObserved(boolean observed) {
        this.entityData.set(DATA_OBSERVED, observed);
    }

    @Override
    public void notifyDetectablePlayer(Player player) {
    }
}
