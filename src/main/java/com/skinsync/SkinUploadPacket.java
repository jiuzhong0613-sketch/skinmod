package com.skinsync;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.function.Supplier;

public class SkinUploadPacket {
    private final byte[] png;
    private final boolean slim;

    public SkinUploadPacket(byte[] png, boolean slim) {
        this.png = png;
        this.slim = slim;
    }

    public static void encode(SkinUploadPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.slim);
        buf.writeByteArray(msg.png);
    }

    public static SkinUploadPacket decode(FriendlyByteBuf buf) {
        boolean slim = buf.readBoolean();
        byte[] data = buf.readByteArray(1024 * 1024);
        return new SkinUploadPacket(data, slim);
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            if (!SkinConfig.ALLOW_UPLOADS.get()) {
                ack(player, "服务器已关闭皮肤上传", false);
                return;
            }
            if (png.length > SkinConfig.MAX_KB.get() * 1024) {
                ack(player, "文件太大，上限 " + SkinConfig.MAX_KB.get() + "KB", false);
                return;
            }
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(png));
                if (image == null) {
                    ack(player, "不是有效的图片文件", false);
                    return;
                }
                int w = image.getWidth();
                int h = image.getHeight();
                boolean valid = (w == 64 && h == 64) || (w == 64 && h == 32) || (w == 128 && h == 128);
                if (!valid) {
                    ack(player, "尺寸必须是 64x64、64x32 或 128x128", false);
                    return;
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(image, "PNG", out);
                SkinStore.save(player.getUUID(), player.getGameProfile().getName(), out.toByteArray(), slim);
                ProfileInjector.apply(player);
                ProfileInjector.refresh(player);
                ack(player, "皮肤已保存，其他玩家可能需要重新登录才能看到", true);
            } catch (Exception e) {
                ack(player, "上传失败: " + e.getMessage(), false);
            }
        });
        ctx.setPacketHandled(true);
    }

    private static void ack(ServerPlayer player, String message, boolean ok) {
        Network.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SkinAckPacket(message, ok));
    }
}
