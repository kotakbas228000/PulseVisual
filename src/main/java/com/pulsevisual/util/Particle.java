package com.pulsevisual.util;

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
        vx += (random.nextFloat() - 0.5f) * intensity;
        vy += (random.nextFloat() - 0.5f) * intensity;
        
        x += vx;
        y += vy;
        
        life -= 1;
        
        if (life <= 0) {
            reset();
        }
        
        float maxSpeed = 10f;
        float speed = (float) Math.sqrt(vx * vx + vy * vy);
        if (speed > maxSpeed) {
            vx = (vx / speed) * maxSpeed;
            vy = (vy / speed) * maxSpeed;
        }
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public float getSize() {
        return size;
    }
    
    public float getLife() {
        return life;
    }
    
    public float getMaxLife() {
        return maxLife;
    }
}
