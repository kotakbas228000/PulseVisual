package com.pulsevisual.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import com.lwjgl.glfw.GLFW;
import com.pulsevisual.client.overlay.PulseVisualizerOverlay;

/**
 * Клиентская часть мода PulseVisual для Fabric
 */
public class PulseVisualClient implements ClientModInitializer {
    
    private static KeyBinding toggleKey;
    private static KeyBinding cycleKey;
    private static PulseVisualizerOverlay visualizerOverlay;
    private static boolean isActive = false;
    
    @Override
    public void onInitializeClient() {
        // Регистрируем горячие клавиши
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.pulsevisual.toggle",
            GLFW.GLFW_KEY_V,
            "category.pulsevisual"
        ));
        
        cycleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.pulsevisual.cycle",
            GLFW.GLFW_KEY_B,
            "category.pulsevisual"
        ));
        
        // Инициализируем оверлей
        visualizerOverlay = new PulseVisualizerOverlay();
        
        // Регистрируем обработчик клиентского тика
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKey.wasPressed()) {
                isActive = !isActive;
            }
            
            if (cycleKey.wasPressed() && isActive) {
                if (visualizerOverlay != null) {
                    visualizerOverlay.cycleVisualizationType();
                }
            }
        });
    }
    
    public static boolean isVisualizerActive() {
        return isActive;
    }
    
    public static PulseVisualizerOverlay getVisualizer() {
        return visualizerOverlay;
    }
}
