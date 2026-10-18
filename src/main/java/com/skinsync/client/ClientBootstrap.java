package com.skinsync.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ClientBootstrap {
    public static final KeyMapping OPEN = new KeyMapping(
            "key.skinsync.open", GLFW.GLFW_KEY_K, "key.categories.misc");

    public static void init(IEventBus modBus) {
        modBus.addListener(ClientBootstrap::registerKeys);
        MinecraftForge.EVENT_BUS.register(ClientBootstrap.class);
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        while (OPEN.consumeClick()) {
            Minecraft.getInstance().setScreen(new SkinUploadScreen());
        }
    }

    public static void onAck(String message, boolean ok) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.displayClientMessage(Component.literal((ok ? "§a" : "§c") + message), false);
        }
    }
}
