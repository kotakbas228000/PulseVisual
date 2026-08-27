package com.pulsevisual.util;

/**
 * Цветовые схемы для визуализации
 */
public enum ColorScheme {
    BLUE("Синий"),
    RED("Красный"),
    GREEN("Зелёный"),
    RAINBOW("Радуга");
    
    private final String displayName;
    
    ColorScheme(String displayName) {
        this.displayName = displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
