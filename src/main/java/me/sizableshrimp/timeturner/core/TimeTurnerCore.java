package me.sizableshrimp.timeturner.core;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import me.sizableshrimp.timeturner.TimeTurnerMod;
import me.sizableshrimp.timeturner.client.ClientTimeTurner;
import me.sizableshrimp.timeturner.network.NetworkHandler;
import me.sizableshrimp.timeturner.network.packet.clientbound.StartTimeRevertPacket;
import me.sizableshrimp.timeturner.network.packet.clientbound.StopTimeRevertPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = TimeTurnerMod.MODID)
public class TimeTurnerCore {
    private static int totalRevertTicks;
    private static int remainingRevertTicks;
    @Nullable
    private static IntSet alreadyNoAi;

    public static boolean revertTime(@NotNull MinecraftServer server, int revertTicks) {
        if (remainingRevertTicks > 0)
            return false;

        remainingRevertTicks = Mth.clamp(revertTicks, 0, TimeTurnerMod.MAX_REVERT_TICKS);
        totalRevertTicks = remainingRevertTicks;
        alreadyNoAi = new IntOpenHashSet();

        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (entity instanceof Mob mob) {
                    if (mob.isNoAi()) {
                        alreadyNoAi.add(mob.getId());
                    } else {
                        mob.setNoAi(true);
                    }
                }

                entity.setDeltaMovement(0, 0, 0);
                entity.hurtMarked = true;
            }
        }

        // Send packet to all players of how many ticks to revert
        NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new StartTimeRevertPacket(remainingRevertTicks));

        return true;
    }

    public static boolean stopRevertingTime() {
        remainingRevertTicks = 0;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null)
            return false;

        NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new StopTimeRevertPacket());

        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (entity instanceof Mob mob && (alreadyNoAi == null || !alreadyNoAi.contains(entity.getId()))) {
                    mob.setNoAi(false);
                }

                RollingList<TimeData> timeDataList = TimeTracker.getEntityTimeDataList(entity);
                if (timeDataList == null)
                    continue;

                TimeData timeData = timeDataList.getTailOffsetPositive(-totalRevertTicks);
                // TODO move insertion index on clientside time turner as well
                timeDataList.moveInsertionIndex(-totalRevertTicks);
                if (timeData == null)
                    continue;

                timeData.apply(entity, true);
            }
        }

        totalRevertTicks = 0;
        return true;
    }

    public static boolean isTimeReverting(Level level) {
        return level.isClientSide ? ClientTimeTurner.isTimeReverting() : remainingRevertTicks > 0;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || remainingRevertTicks == 0)
            return;

        remainingRevertTicks--;

        if (remainingRevertTicks == 0) {
            stopRevertingTime();
        }
    }
}
