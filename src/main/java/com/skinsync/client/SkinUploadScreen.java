package com.skinsync.client;

import com.skinsync.Network;
import com.skinsync.SkinUploadPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Files;
import java.nio.file.Path;

public class SkinUploadScreen extends Screen {
    private EditBox pathBox;
    private Component status = Component.literal("选择 64x64 PNG 文件，然后点击上传");
    private boolean slim = false;

    public SkinUploadScreen() {
        super(Component.literal("皮肤上传"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        this.pathBox = new EditBox(this.font, cx - 150, this.height / 2 - 40, 300, 20, Component.literal("文件路径"));
        this.pathBox.setValue(System.getProperty("user.home") + "/skin.png");
        this.addRenderableWidget(this.pathBox);

        this.addRenderableWidget(Button.builder(Component.literal("细手臂: " + (slim ? "是" : "否")), b -> {
            slim = !slim;
            b.setMessage(Component.literal("细手臂: " + (slim ? "是" : "否")));
        }).bounds(cx - 150, this.height / 2 - 8, 140, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("上传"), b -> upload())
                .bounds(cx + 10, this.height / 2 - 8, 140, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("关闭"), b -> this.onClose())
                .bounds(cx - 50, this.height / 2 + 34, 100, 20).build());
    }

    private void upload() {
        try {
            Path path = Path.of(this.pathBox.getValue().trim());
            byte[] data = Files.readAllBytes(path);
            Network.CHANNEL.sendToServer(new SkinUploadPacket(data, slim));
            this.status = Component.literal("已发送，等待服务器响应…");
        } catch (Exception e) {
            this.status = Component.literal("读取失败: " + e.getMessage());
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 40, 0xFFFFFF);
        graphics.drawCenteredString(this.font, this.status, this.width / 2, this.height / 2 + 64, 0xAAAAAA);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
