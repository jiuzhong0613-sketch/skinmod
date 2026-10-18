package com.skinsync;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class SkinStore {
    public static Path dir() {
        return FMLPaths.CONFIGDIR.get().resolve("skinsync").resolve("skins");
    }

    public static void save(UUID id, String name, byte[] png, boolean slim) throws IOException {
        Files.createDirectories(dir());
        Files.write(dir().resolve(id + ".png"), png);
        JsonObject o = new JsonObject();
        o.addProperty("name", name);
        o.addProperty("slim", slim);
        o.addProperty("updated", System.currentTimeMillis());
        Files.writeString(dir().resolve(id + ".json"), o.toString());
    }

    public static SkinRecord get(UUID id) {
        try {
            Path png = dir().resolve(id + ".png");
            Path meta = dir().resolve(id + ".json");
            if (!Files.isRegularFile(png) || !Files.isRegularFile(meta)) return null;
            JsonObject o = JsonParser.parseString(Files.readString(meta)).getAsJsonObject();
            return new SkinRecord(
                    o.get("name").getAsString(),
                    o.has("slim") && o.get("slim").getAsBoolean(),
                    o.has("updated") ? o.get("updated").getAsLong() : 0L,
                    Files.readAllBytes(png)
            );
        } catch (Exception e) {
            return null;
        }
    }
}
