package me.sizableshrimp.timeturner.core;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public record TimeData(Vec3 position, Vec2 rotation, float yHeadRot, ResourceKey<Level> dimension) {
    public static TimeData of(Entity entity) {
        float yHeadRot = entity instanceof LivingEntity livingEntity ? livingEntity.getYHeadRot() : entity.getYRot();
        return new TimeData(entity.position(), entity.getRotationVector(), yHeadRot, entity.level.dimension());
    }

    /**
     * @param finalDestination if {@code true}, the old position and rotations will also be overwritten as in a teleport
     * and syncs to players if necessary.
     * Otherwise, only sets the current position and rotation.
     */
    public void apply(Entity entity, boolean finalDestination) {
        // TODO support changing dimensions?
        if (entity.level.dimension() != this.dimension)
            return;

        if (finalDestination) {
            entity.moveTo(position.x, position.y, position.z, rotation.y, rotation.x);

            if (entity instanceof ServerPlayer player)
                player.connection.teleport(position.x, position.y, position.z, rotation.y, rotation.x, Set.of(), false);
        } else {
            entity.setPos(position);
            entity.setYRot(rotation.y);
            entity.setXRot(rotation.x);

            if (entity instanceof LivingEntity livingEntity)
                livingEntity.setYHeadRot(livingEntity.getYHeadRot());
        }
    }
}
