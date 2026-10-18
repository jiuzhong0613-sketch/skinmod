package com.skinsync;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network {
    private static final String VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SkinSync.MODID, "main"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.messageBuilder(SkinUploadPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SkinUploadPacket::encode)
                .decoder(SkinUploadPacket::decode)
                .consumerMainThread(SkinUploadPacket::handle)
                .add();
        CHANNEL.messageBuilder(SkinAckPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SkinAckPacket::encode)
                .decoder(SkinAckPacket::decode)
                .consumerMainThread(SkinAckPacket::handle)
                .add();
    }
}
