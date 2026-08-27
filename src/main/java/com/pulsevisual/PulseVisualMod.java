package com.pulsevisual;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Основной класс мода PulseVisual для Fabric 1.21.4
 */
public class PulseVisualMod implements ModInitializer {
    public static final String MODID = "pulsevisual";
    public static final String NAME = "PulseVisual";
    public static final String VERSION = "1.0.0";
    
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    
    @Override
    public void onInitialize() {
        LOGGER.info("Инициализация мода {}", NAME);
    }
}
