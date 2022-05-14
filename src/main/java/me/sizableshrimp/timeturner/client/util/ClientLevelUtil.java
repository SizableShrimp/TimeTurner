package me.sizableshrimp.timeturner.client.util;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ClientLevelUtil {
    public static Iterable<Entity> getAllEntities(Level level) {
        return ((ClientLevel) level).entitiesForRendering();
    }
}
