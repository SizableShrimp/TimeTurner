package me.sizableshrimp.timeturner.network.packet.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import me.sizableshrimp.timeturner.client.network.ClientNetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record StartTimeRevertPacket(int revertTicks) implements IPacket {
    @Override
    public void handle(NetworkEvent.Context context) {
        ClientNetworkHandler.handleStartTimeRevert(this, context);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeInt(this.revertTicks);
    }

    public static StartTimeRevertPacket read(FriendlyByteBuf packetBuf) {
        return new StartTimeRevertPacket(packetBuf.readInt());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, StartTimeRevertPacket.class, StartTimeRevertPacket::read);
    }
}
