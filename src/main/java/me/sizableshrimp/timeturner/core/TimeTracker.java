package me.sizableshrimp.timeturner.core;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.sizableshrimp.timeturner.TimeTurnerMod;
import me.sizableshrimp.timeturner.util.LevelUtil;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = TimeTurnerMod.MODID)
public class TimeTracker {
    private static final Int2ObjectMap<RollingList<TimeData>> ENTITY_TIME_DATA_LISTS = new Int2ObjectOpenHashMap<>();

    @NotNull
    public static RollingList<TimeData> getOrCreateEntityTimeDataList(Entity entity) {
        return ENTITY_TIME_DATA_LISTS.computeIfAbsent(entity.getId(), k -> new RollingList<>(TimeTurnerMod.MAX_REVERT_TICKS));
    }

    @Nullable
    public static RollingList<TimeData> getEntityTimeDataList(Entity entity) {
        return ENTITY_TIME_DATA_LISTS.get(entity.getId());
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || TimeTurnerCore.isTimeReverting(event.world))
            return;

        for (Entity entity : LevelUtil.getAllEntities(event.world)) {
            if (!entity.isAlive()) {
                ENTITY_TIME_DATA_LISTS.remove(entity.getId());
                continue;
            }

            getOrCreateEntityTimeDataList(entity).add(TimeData.of(entity));
        }
    }
}
