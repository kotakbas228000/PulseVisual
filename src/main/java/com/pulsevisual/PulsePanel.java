package com.pulsevisual;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Панель для отрисовки визуализации пульса и звука
 */
public class PulsePanel extends JPanel {
    private VisualizationType visualizationType = VisualizationType.WAVES;
    private ColorScheme colorScheme = ColorScheme.BLUE;
    
    private float[] audioData = new float[256];
    private List<Particle> particles = new ArrayList<>();
    private float pulseIntensity = 0f;
    private float smoothedIntensity = 0f;
    
    public PulsePanel() {
        setBackground(new Color(10, 10, 20));
        setDoubleBuffered(true);
        
        // Инициализируем частицы
        for (int i = 0; i < 100; i++) {
            particles.add(new Particle());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Рисуем выбранный тип визуализации
        switch (visualizationType) {
            case WAVES:
                drawWaves(g2d, width, height);
                break;
            case PULSE:
                drawPulse(g2d, width, height);
                break;
            case PARTICLES:
                drawParticles(g2d, width, height);
                break;
            case CIRCLE:
                drawCircleVisualization(g2d, width, height);
                break;
        }

        // Рисуем информацию
        drawInfo(g2d, width, height);
    }

    private void drawWaves(Graphics2D g, int width, int height) {
        g.setColor(getColor(0));
        g.setStroke(new BasicStroke(3));

        Path2D path = new Path2D.Float();
        path.moveTo(0, height / 2);

        for (int i = 0; i < audioData.length; i++) {
            float x = (float) i / audioData.length * width;
            float y = height / 2 - audioData[i] * 100;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        path.lineTo(width, height / 2);
        g.drawPolyline(
            new int[]{0, width},
            new int[]{height / 2, height / 2},
            2
        );

        g.setStroke(new BasicStroke(2));
        g.drawPath(path);

        // Вторая волна с смещением
        g.setColor(getColor(1));
        path = new Path2D.Float();
        path.moveTo(0, height / 2);

        for (int i = 0; i < audioData.length; i++) {
            float x = (float) i / audioData.length * width;
            float y = height / 2 + audioData[i] * 100;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        g.drawPath(path);
    }

    private void drawPulse(Graphics2D g, int width, int height) {
        // Обновляем smoothed intensity
        smoothedIntensity += (pulseIntensity - smoothedIntensity) * 0.1f;

        int centerX = width / 2;
        int centerY = height / 2;

        // Рисуем концентрические круги пульса
        for (int i = 0; i < 5; i++) {
            float alpha = 1 - (i / 5f);
            float size = 100 + (smoothedIntensity * 200) + (i * 30);
            
            Color c = getColor(i);
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(alpha * 200)));
            
            Ellipse2D circle = new Ellipse2D.Float(
                centerX - size / 2,
                centerY - size / 2,
                size,
                size
            );
            g.fillOval((int)(centerX - size / 2), (int)(centerY - size / 2), (int)size, (int)size);
        }

        // Рисуем центральный круг
        g.setColor(Color.WHITE);
        g.fillOval(centerX - 10, centerY - 10, 20, 20);
    }

    private void drawParticles(Graphics2D g, int width, int height) {
        for (Particle p : particles) {
            p.update(smoothedIntensity);
            p.draw(g, width, height, getColor(0));
        }
    }

    private void drawCircleVisualization(Graphics2D g, int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width, height) / 3;

        for (int i = 0; i < audioData.length; i++) {
            double angle = (i / (double) audioData.length) * 2 * Math.PI;
            float barHeight = audioData[i] * 150;

            double x1 = centerX + Math.cos(angle) * radius;
            double y1 = centerY + Math.sin(angle) * radius;
            double x2 = centerX + Math.cos(angle) * (radius + barHeight);
            double y2 = centerY + Math.sin(angle) * (radius + barHeight);

            g.setColor(getColor(i));
            g.setStroke(new BasicStroke(2));
            g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        }
    }

    private void drawInfo(Graphics2D g, int width, int height) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Тип: " + visualizationType, 10, height - 10);
        g.drawString("Цвет: " + colorScheme, width - 150, height - 10);
    }

    private Color getColor(int index) {
        switch (colorScheme) {
            case BLUE:
                return new Color(0, 150 + (index * 20), 255);
            case RED:
                return new Color(255, 100 - (index * 10), 0);
            case GREEN:
                return new Color(0, 255, 100 - (index * 10));
            case RAINBOW:
                float hue = (index / 10f + System.currentTimeMillis() / 3000f) % 1f;
                return Color.getHSBColor(hue, 0.8f, 1f);
            default:
                return Color.WHITE;
        }
    }

    public void updateAudioData(float[] data) {
        if (data.length > 0) {
            System.arraycopy(data, 0, audioData, 0, Math.min(data.length, audioData.length));
            
            // Вычисляем интенсивность
            float sum = 0;
            for (float f : audioData) {
                sum += Math.abs(f);
            }
            pulseIntensity = sum / audioData.length;
        }
        repaint();
    }

    public void setVisualizationType(VisualizationType type) {
        this.visualizationType = type;
    }

    public void setColorScheme(ColorScheme scheme) {
        this.colorScheme = scheme;
    }
}
