package me.Thelnfamous1.goldenglitch_scp.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import javax.annotation.Nullable;
import java.util.*;

public class InteractWithDoorGoal<T extends Mob> extends Goal {

    private final T mob;
    private final boolean opensDoubleDoors;
    private final boolean requireInteractable;
    private final boolean closesDoors;
    private Node currentNode;
    private final Set<GlobalPos> doorsToClose = new HashSet<>();
    private int cooldown;

    public InteractWithDoorGoal(T mob, boolean opensDoubleDoors, boolean requireInteractable, boolean closesDoors){
        this.mob = mob;
        this.opensDoubleDoors = opensDoubleDoors;
        this.requireInteractable = requireInteractable;
        this.closesDoors = closesDoors;
    }

    @Override
    public boolean canUse() {
        Path path = this.mob.getNavigation().getPath();
        if (path != null && !path.notStarted() && !path.isDone()) {
            if (Objects.equals(this.currentNode, path.getNextNode())) {
                this.cooldown = this.adjustedTickDelay(20);
            } else if (this.cooldown-- > 0) {
                return false;
            }

            this.currentNode = path.getNextNode();
            Node previousNode = path.getPreviousNode();
            if(previousNode != null){
                this.tryOpenDoors(previousNode, false);
            }
            Node nextNode = path.getNextNode();
            this.tryOpenDoors(nextNode, true);

            if(this.closesDoors){
                this.closeDoorsThatIHaveOpenedOrPassedThrough(previousNode, nextNode);
            } else{
                this.doorsToClose.clear();
            }
            return true;
        } else {
            return false;
        }
    }

    protected void tryOpenDoors(Node previousNode, boolean onlyRememberWhenOpened) {
        BlockPos blockPos = previousNode.asBlockPos();
        if(this.opensDoubleDoors){
            for (BlockPos aroundPos : BlockPos.betweenClosed(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1, blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1)) {
                this.tryOpenDoor(aroundPos.immutable(), onlyRememberWhenOpened);
            }
        } else{
            this.tryOpenDoor(blockPos, onlyRememberWhenOpened);
        }
    }

    protected void tryOpenDoor(BlockPos blockPos, boolean onlyRememberWhenOpened) {
        BlockState blockState = this.mob.level().getBlockState(blockPos);
        if ((!this.requireInteractable || blockState.is(BlockTags.MOB_INTERACTABLE_DOORS)) && blockState.getBlock() instanceof DoorBlock doorblock) {
            if (!doorblock.isOpen(blockState)) {
                doorblock.setOpen(this.mob, this.mob.level(), blockState, blockPos, true);
                if(onlyRememberWhenOpened && this.closesDoors){
                    this.rememberDoorToClose(this.mob.level(), blockPos);
                }
            }

            if(!onlyRememberWhenOpened && this.closesDoors){
                this.rememberDoorToClose(this.mob.level(), blockPos);
            }
        }
    }

    protected void rememberDoorToClose(Level pLevel, BlockPos previousBlockPos) {
        this.doorsToClose.add(GlobalPos.of(pLevel.dimension(), previousBlockPos));
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    protected void closeDoorsThatIHaveOpenedOrPassedThrough(@Nullable Node previousNode, @Nullable Node nextNode) {
        Iterator<GlobalPos> iterator = this.doorsToClose.iterator();
        while (iterator.hasNext()) {
            GlobalPos nextDoorToClose = iterator.next();
            BlockPos nextDoorToClosePos = nextDoorToClose.pos();
            if ((previousNode == null || !previousNode.asBlockPos().equals(nextDoorToClosePos)) && (nextNode == null || !nextNode.asBlockPos().equals(nextDoorToClosePos))) {
                if (this.isDoorTooFarAway(this.mob.level(), this.mob, nextDoorToClose)) {
                    iterator.remove();
                } else {
                    BlockState blockstate = this.mob.level().getBlockState(nextDoorToClosePos);
                    if (!((!this.requireInteractable || blockstate.is(BlockTags.MOB_INTERACTABLE_DOORS)) && blockstate.getBlock() instanceof DoorBlock doorBlock)) {
                        iterator.remove();
                    } else {
                        if (!doorBlock.isOpen(blockstate)) {
                            iterator.remove();
                        } else if (this.areOtherMobsComingThroughDoor(this.mob, nextDoorToClosePos)) {
                            iterator.remove();
                        } else {
                            doorBlock.setOpen(this.mob, this.mob.level(), blockstate, nextDoorToClosePos, false);
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    protected boolean isDoorTooFarAway(Level level, Mob mob, GlobalPos doorGlobalPos) {
        return doorGlobalPos.dimension() != level.dimension() || !doorGlobalPos.pos().closerToCenterThan(mob.position(), 3.0D);
    }

    protected boolean areOtherMobsComingThroughDoor(Mob mob, BlockPos doorBlockPos) {
        return this.getNearestVisibleMobs(mob)
                .stream()
                .filter(otherMob -> otherMob.getType() == mob.getType())
                .filter(otherMob -> doorBlockPos.closerToCenterThan(otherMob.position(), 2.0D))
                .anyMatch(otherMob -> this.isMobComingThroughDoor(otherMob, doorBlockPos));
    }

    protected List<Mob> getNearestVisibleMobs(Mob mob) {
        return mob.level().getEntitiesOfClass(Mob.class, mob.getBoundingBox().inflate(16.0D), otherMob -> otherMob != mob && otherMob.isAlive());
    }

    protected boolean isMobComingThroughDoor(Mob mob, BlockPos doorBlockPos) {
        if (mob.getNavigation().getPath() == null) {
            return false;
        } else {
            Path path = mob.getNavigation().getPath();
            if (path.isDone()) {
                return false;
            } else {
                Node previousNode = path.getPreviousNode();
                if (previousNode == null) {
                    return false;
                } else {
                    Node nextNode = path.getNextNode();
                    return doorBlockPos.equals(previousNode.asBlockPos()) || doorBlockPos.equals(nextNode.asBlockPos());
                }
            }
        }
    }
}
