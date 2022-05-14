package me.sizableshrimp.timeturner.network.packet.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import me.sizableshrimp.timeturner.client.network.ClientNetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class StopTimeRevertPacket implements IPacket {
    @Override
    public void handle(NetworkEvent.Context context) {
        ClientNetworkHandler.handleStopTimeRevert(this, context);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {}

    public static StopTimeRevertPacket read(FriendlyByteBuf packetBuf) {
        return new StopTimeRevertPacket();
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, StopTimeRevertPacket.class, StopTimeRevertPacket::read);
    }
}
