package me.sizableshrimp.timeturner.network;

import com.google.common.collect.ImmutableList;
import me.sizableshrimp.timeturner.TimeTurnerMod;
import me.sizableshrimp.timeturner.network.packet.clientbound.StartTimeRevertPacket;
import me.sizableshrimp.timeturner.network.packet.clientbound.StopTimeRevertPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.function.ObjIntConsumer;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TimeTurnerMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int nextId = 0;

    public static void register() {
        List<ObjIntConsumer<SimpleChannel>> packets = ImmutableList.<ObjIntConsumer<SimpleChannel>>builder()
                .add(StartTimeRevertPacket::register)
                .add(StopTimeRevertPacket::register)
                .build();

        packets.forEach(consumer -> consumer.accept(CHANNEL, getNextId()));
    }

    private static int getNextId() {
        return nextId++;
    }
}
