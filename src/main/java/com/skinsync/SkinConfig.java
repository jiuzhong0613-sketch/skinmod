package com.skinsync;

import net.minecraftforge.common.ForgeConfigSpec;

public class SkinConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.IntValue HTTP_PORT;
    public static final ForgeConfigSpec.ConfigValue<String> PUBLIC_BASE_URL;
    public static final ForgeConfigSpec.IntValue MAX_KB;
    public static final ForgeConfigSpec.BooleanValue ALLOW_UPLOADS;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("SkinSync server settings").push("server");
        HTTP_PORT = b.comment("HTTP port used to serve skin images").defineInRange("httpPort", 25567, 1024, 65535);
        PUBLIC_BASE_URL = b.comment("Base URL clients use to download skins, for example http://10.227.127.36:25567")
                .define("publicBaseUrl", "http://127.0.0.1:25567");
        MAX_KB = b.comment("Maximum uploaded PNG size in KB").defineInRange("maxKb", 256, 8, 1024);
        ALLOW_UPLOADS = b.comment("Allow players to upload skins").define("allowUploads", true);
        b.pop();
        SPEC = b.build();
    }

    public static String baseUrl() {
        String s = PUBLIC_BASE_URL.get();
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }
}
