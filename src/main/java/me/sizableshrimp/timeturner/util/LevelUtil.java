package me.sizableshrimp.timeturner.util;

import me.sizableshrimp.timeturner.client.util.ClientLevelUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LevelUtil {
    public static Iterable<Entity> getAllEntities(@NotNull Level level) {
        return level.isClientSide ? ClientLevelUtil.getAllEntities(level) : ((ServerLevel) level).getAllEntities();
    }
}
