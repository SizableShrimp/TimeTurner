package me.sizableshrimp.timeturner.client.network;

import me.sizableshrimp.timeturner.client.ClientTimeTurner;
import me.sizableshrimp.timeturner.network.packet.clientbound.StartTimeRevertPacket;
import me.sizableshrimp.timeturner.network.packet.clientbound.StopTimeRevertPacket;
import net.minecraftforge.network.NetworkEvent;

public class ClientNetworkHandler {
    public static void handleStartTimeRevert(StartTimeRevertPacket packet, NetworkEvent.Context context) {
        context.enqueueWork(() -> ClientTimeTurner.revertTime(packet.revertTicks()));
    }

    public static void handleStopTimeRevert(StopTimeRevertPacket packet, NetworkEvent.Context context) {
        ClientTimeTurner.stopRevertingTime();
    }
}
