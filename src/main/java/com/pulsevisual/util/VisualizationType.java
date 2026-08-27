package com.pulsevisual.util;

/**
 * Типы визуализации звука
 */
public enum VisualizationType {
    WAVES("Волны"),
    PULSE("Пульс"),
    PARTICLES("Частицы"),
    CIRCLE("Окружность");
    
    private final String displayName;
    
    VisualizationType(String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
