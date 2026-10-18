package com.skinsync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SkinAckPacket {
    private final String message;
    private final boolean ok;

    public SkinAckPacket(String message, boolean ok) {
        this.message = message;
        this.ok = ok;
    }

    public static void encode(SkinAckPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.message, 32767);
        buf.writeBoolean(msg.ok);
    }

    public static SkinAckPacket decode(FriendlyByteBuf buf) {
        return new SkinAckPacket(buf.readUtf(32767), buf.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> com.skinsync.client.ClientBootstrap.onAck(message, ok)));
        ctx.setPacketHandled(true);
    }
}
