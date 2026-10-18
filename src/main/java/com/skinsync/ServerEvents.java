package com.skinsync;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerEvents {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        SkinHttpServer.start();
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ProfileInjector.apply(player);
        if (player.getServer() != null) {
            player.getServer().execute(() -> ProfileInjector.refresh(player));
        }
    }
}
