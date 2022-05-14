package me.sizableshrimp.timeturner.client;

import me.sizableshrimp.timeturner.TimeTurnerMod;
import me.sizableshrimp.timeturner.core.RollingList;
import me.sizableshrimp.timeturner.core.TimeData;
import me.sizableshrimp.timeturner.core.TimeTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TimeTurnerMod.MODID, value = Dist.CLIENT)
public class ClientTimeTurner {
    private static int remainingRevertTicks;
    private static int totalRevertTicks;

    public static void revertTime(int revertTicks) {
        remainingRevertTicks = revertTicks;
        totalRevertTicks = revertTicks;
    }

    public static void stopRevertingTime() {
        remainingRevertTicks = 0;
        totalRevertTicks = 0;
    }

    public static boolean isTimeReverting() {
        return remainingRevertTicks > 0;
    }

    /**
     * 0 if not reverting time currently, otherwise non-negative number of revert ticks experienced
     */
    public static int getTraversedRevertTicks() {
        return totalRevertTicks - remainingRevertTicks;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || remainingRevertTicks == 0)
            return;

        remainingRevertTicks--;
        int offset = getTraversedRevertTicks();
        if (remainingRevertTicks == 0)
            totalRevertTicks = 0;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return;

        for (Entity entity : level.entitiesForRendering()) {
            RollingList<TimeData> timeDataList = TimeTracker.getEntityTimeDataList(entity);
            if (timeDataList == null)
                continue;

            // TODO Moving the insertion index here doesn't work,
            //  as it would screw up integrated servers where the client is also the server and moves the index at the end as well.
            //  Find a better solution.
            // if (timeDataList.getInsertionIndex() > 0)
            //     timeDataList.moveInsertionIndex(-1);
            // TimeData timeData = timeDataList.getTail();

            TimeData timeData = timeDataList.getTailOffsetPositive(-offset);
            // TODO stop rendering if we run out of time data for an entity? (insertIdx == 0)
            // TODO stop rendering if entity goes back into other dimension?
            if (timeData == null || timeData.dimension() != level.dimension())
                continue;

            timeData.apply(entity, false);
        }
    }
}
