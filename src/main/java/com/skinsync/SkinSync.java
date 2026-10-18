package com.skinsync;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(SkinSync.MODID)
public class SkinSync {
    public static final String MODID = "skinsync";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SkinSync(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        context.registerConfig(ModConfig.Type.SERVER, SkinConfig.SPEC);
        modBus.addListener(this::commonSetup);
        net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> com.skinsync.client.ClientBootstrap.init(modBus));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        Network.register();
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);
        LOGGER.info("SkinSync common setup complete");
    }
}
