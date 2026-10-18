package com.skinsync;

import com.google.gson.JsonObject;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class ProfileInjector {
    public static void apply(ServerPlayer player) {
        SkinRecord rec = SkinStore.get(player.getUUID());
        if (rec == null) return;

        String url = SkinConfig.baseUrl() + "/skins/" + player.getUUID() + ".png";
        JsonObject skin = new JsonObject();
        skin.addProperty("url", url);
        if (rec.slim()) {
            JsonObject metadata = new JsonObject();
            metadata.addProperty("model", "slim");
            skin.add("metadata", metadata);
        }
        JsonObject textures = new JsonObject();
        textures.add("SKIN", skin);

        JsonObject root = new JsonObject();
        root.addProperty("timestamp", System.currentTimeMillis());
        root.addProperty("profileId", player.getUUID().toString().replace("-", ""));
        root.addProperty("profileName", player.getGameProfile().getName());
        root.add("textures", textures);

        String encoded = Base64.getEncoder().encodeToString(root.toString().getBytes(StandardCharsets.UTF_8));
        player.getGameProfile().getProperties().removeAll("textures");
        player.getGameProfile().getProperties().put("textures", new Property("textures", encoded));
    }

    public static void refresh(ServerPlayer target) {
        MinecraftServer server = target.getServer();
        if (server == null) return;
        ClientboundPlayerInfoRemovePacket remove = new ClientboundPlayerInfoRemovePacket(List.of(target.getUUID()));
        ClientboundPlayerInfoUpdatePacket add = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(target));
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(remove);
            player.connection.send(add);
        }
    }
}
