package com.pulsevisual.client.overlay;

import com.pulsevisual.client.audio.AudioAnalyzer;
import com.pulsevisual.util.ColorScheme;
import com.pulsevisual.util.Particle;
import com.pulsevisual.util.VisualizationType;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

/**
 * Оверлей визуализации для отрисовки эффектов
 */
public class PulseVisualizerOverlay implements HudRenderCallback {
    
    private AudioAnalyzer audioAnalyzer;
    private VisualizationType currentVisualizationType = VisualizationType.WAVES;
    private ColorScheme currentColorScheme = ColorScheme.BLUE;
    private float[] audioData = new float[256];
    private float pulseIntensity = 0f;
    private float smoothedIntensity = 0f;
    private java.util.List<Particle> particles;
    
    public PulseVisualizerOverlay() {
        this.audioAnalyzer = new AudioAnalyzer(this);
        audioAnalyzer.start();
        
        this.particles = new java.util.ArrayList<>();
        for (int i = 0; i < 100; i++) {
            particles.add(new Particle());
        }
        
        // Регистрируем себя как HUD рендерер
        HudRenderCallback.EVENT.register(this);
    }
    
    @Override
    public void onHudRender(GuiGraphics guiGraphics, float tickDelta) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        
        renderVisualization(guiGraphics, width, height);
    }
    
    private void renderVisualization(GuiGraphics guiGraphics, int width, int height) {
        switch (currentVisualizationType) {
            case WAVES:
                drawWaves(guiGraphics, width, height);
                break;
            case PULSE:
                drawPulse(guiGraphics, width, height);
                break;
            case PARTICLES:
                drawParticles(guiGraphics, width, height);
                break;
            case CIRCLE:
                drawCircleVisualization(guiGraphics, width, height);
                break;
        }
        
        drawInfo(guiGraphics, width, height);
    }
    
    private void drawWaves(GuiGraphics guiGraphics, int width, int height) {
        int baseY = height / 2;
        int prevX = 0, prevY = baseY;
        
        // Верхняя волна
        for (int i = 0; i < audioData.length; i++) {
            int x = (int) ((float) i / audioData.length * width);
            int y = (int) (baseY - audioData[i] * 100);
            
            if (i > 0) {
                guiGraphics.drawString(Minecraft.getInstance().font, "", prevX, prevY, getColor(i));
                // Рисуем линию между точками
                drawLine(guiGraphics, prevX, prevY, x, y, getColor(i));
            }
            prevX = x;
            prevY = y;
        }
        
        // Нижняя волна
        prevX = 0;
        prevY = baseY;
        for (int i = 0; i < audioData.length; i++) {
            int x = (int) ((float) i / audioData.length * width);
            int y = (int) (baseY + audioData[i] * 100);
            
            if (i > 0) {
                drawLine(guiGraphics, prevX, prevY, x, y, getColor(audioData.length - i));
            }
            prevX = x;
            prevY = y;
        }
    }
    
    private void drawPulse(GuiGraphics guiGraphics, int width, int height) {
        float intensity = smoothedIntensity;
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Рисуем концентрические круги
        for (int i = 0; i < 5; i++) {
            int size = (int) (100 + (intensity * 200) + (i * 30));
            int x = centerX - size / 2;
            int y = centerY - size / 2;
            
            int color = getColor(i);
            drawCircleOutline(guiGraphics, x, y, size, size, color);
        }
        
        // Центральный круг
        guiGraphics.fill(centerX - 10, centerY - 10, centerX + 10, centerY + 10, 0xFFFFFFFF);
    }
    
    private void drawParticles(GuiGraphics guiGraphics, int width, int height) {
        for (Particle particle : particles) {
            particle.update(smoothedIntensity);
            int x = (int) particle.getX();
            int y = (int) particle.getY();
            int size = (int) particle.getSize();
            guiGraphics.fill(x, y, x + size, y + size, getColor(0));
        }
    }
    
    private void drawCircleVisualization(GuiGraphics guiGraphics, int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width, height) / 3;
        
        for (int i = 0; i < audioData.length; i++) {
            double angle = (i / (double) audioData.length) * 2 * Math.PI;
            float barHeight = audioData[i] * 150;
            
            int x1 = (int) (centerX + Math.cos(angle) * radius);
            int y1 = (int) (centerY + Math.sin(angle) * radius);
            int x2 = (int) (centerX + Math.cos(angle) * (radius + barHeight));
            int y2 = (int) (centerY + Math.sin(angle) * (radius + barHeight));
            
            drawLine(guiGraphics, x1, y1, x2, y2, getColor(i));
        }
    }
    
    private void drawCircleOutline(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        for (int i = 0; i < 360; i += 5) {
            double angle1 = Math.toRadians(i);
            double angle2 = Math.toRadians(i + 5);
            
            int x1 = x + width / 2 + (int) (Math.cos(angle1) * width / 2);
            int y1 = y + height / 2 + (int) (Math.sin(angle1) * height / 2);
            int x2 = x + width / 2 + (int) (Math.cos(angle2) * width / 2);
            int y2 = y + height / 2 + (int) (Math.sin(angle2) * height / 2);
            
            drawLine(guiGraphics, x1, y1, x2, y2, color);
        }
    }
    
    private void drawLine(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        
        int x = x1;
        int y = y1;
        
        while (true) {
            guiGraphics.fill(x, y, x + 1, y + 1, color);
            
            if (x == x2 && y == y2) break;
            
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }
    
    private void drawInfo(GuiGraphics guiGraphics, int width, int height) {
        guiGraphics.drawString(Minecraft.getInstance().font, 
            "Тип: " + currentVisualizationType, 10, height - 20, 0xFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, 
            "Цвет: " + currentColorScheme, width - 150, height - 20, 0xFFFFFF);
        guiGraphics.drawString(Minecraft.getInstance().font, 
            "Нажми V для включения", width - 200, 10, 0xFFFF00);
    }
    
    private int getColor(int index) {
        switch (currentColorScheme) {
            case BLUE:
                return 0xFF0096FF | (0xFF << 24);
            case RED:
                return 0xFFFF6400 | (0xFF << 24);
            case GREEN:
                return 0xFF00FF64 | (0xFF << 24);
            case RAINBOW:
                float hue = (index / 10f + System.currentTimeMillis() / 3000f) % 1f;
                return java.awt.Color.HSBtoRGB(hue, 0.8f, 1f) | 0xFF000000;
            default:
                return 0xFFFFFFFF;
        }
    }
    
    public void updateAudioData(float[] data) {
        if (data.length > 0) {
            System.arraycopy(data, 0, audioData, 0, Math.min(data.length, audioData.length));
            
            float sum = 0;
            for (float f : audioData) {
                sum += Math.abs(f);
            }
            pulseIntensity = sum / audioData.length;
            smoothedIntensity += (pulseIntensity - smoothedIntensity) * 0.1f;
        }
    }
    
    public void cycleVisualizationType() {
        switch (currentVisualizationType) {
            case WAVES:
                currentVisualizationType = VisualizationType.PULSE;
                break;
            case PULSE:
                currentVisualizationType = VisualizationType.PARTICLES;
                break;
            case PARTICLES:
                currentVisualizationType = VisualizationType.CIRCLE;
                break;
            case CIRCLE:
                currentVisualizationType = VisualizationType.WAVES;
                break;
        }
    }
}
