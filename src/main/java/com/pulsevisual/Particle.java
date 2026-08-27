package com.pulsevisual;

import java.awt.*;
import java.util.Random;

/**
 * Класс частицы для эффектов визуализации
 */
public class Particle {
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float life;
    private float maxLife;
    private float size;
    private static Random random = new Random();

    public Particle() {
        reset();
    }

    private void reset() {
        this.x = random.nextFloat() * 1200;
        this.y = random.nextFloat() * 700;
        this.vx = (random.nextFloat() - 0.5f) * 4;
        this.vy = (random.nextFloat() - 0.5f) * 4;
        this.size = random.nextFloat() * 5 + 2;
        this.maxLife = random.nextFloat() * 100 + 50;
        this.life = maxLife;
    }

    public void update(float intensity) {
        // Движение под влиянием интенсивности звука
        vx += (random.nextFloat() - 0.5f) * intensity;
        vy += (random.nextFloat() - 0.5f) * intensity;

        x += vx;
        y += vy;

        // Затухание жизни
        life -= 1;

        if (life <= 0) {
            reset();
        }

        // Ограничиваем скорость
        float maxSpeed = 10f;
        float speed = (float) Math.sqrt(vx * vx + vy * vy);
        if (speed > maxSpeed) {
            vx = (vx / speed) * maxSpeed;
            vy = (vy / speed) * maxSpeed;
        }
    }

    public void draw(Graphics2D g, int panelWidth, int panelHeight, Color color) {
        // Проверяем границы
        if (x < 0 || x > panelWidth || y < 0 || y > panelHeight) {
            reset();
        }

        // Вычисляем альфа в зависимости от жизни
        float alpha = life / maxLife;
        Color particleColor = new Color(
            color.getRed(),
            color.getGreen(),
            color.getBlue(),
            (int) (alpha * 200)
        );

        g.setColor(particleColor);
        g.fillOval((int) x, (int) y, (int) size, (int) size);
    }
}
