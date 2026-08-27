package com.pulsevisual;

import javax.sound.sampled.*;
import java.util.Random;

/**
 * Анализатор аудио для обработки звука в реальном времени
 */
public class AudioAnalyzer extends Thread {
    private PulsePanel pulsePanel;
    private volatile boolean running = true;
    private float amplitude = 1f;
    private TargetDataLine targetDataLine;
    private float[] audioBuffer = new float[256];

    public AudioAnalyzer(PulsePanel pulsePanel) {
        this.pulsePanel = pulsePanel;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            initAudioCapture();
            captureAndAnalyze();
        } catch (Exception e) {
            e.printStackTrace();
            simulateAudioData();
        }
    }

    private void initAudioCapture() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Audio line not supported");
        }

        targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        targetDataLine.open(format);
        targetDataLine.start();
    }

    private void captureAndAnalyze() {
        byte[] buffer = new byte[4096];

        while (running) {
            try {
                int bytesRead = targetDataLine.read(buffer, 0, buffer.length);

                // Преобразуем байты в аудио данные
                float[] audioData = new float[Math.min(256, bytesRead / 2)];

                for (int i = 0; i < audioData.length; i++) {
                    int sample = ((buffer[i * 2 + 1] & 0xFF) << 8) | (buffer[i * 2] & 0xFF);
                    if (sample > 32767) {
                        sample -= 65536;
                    }
                    audioData[i] = (sample / 32768f) * amplitude;
                }

                pulsePanel.updateAudioData(audioData);

                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void simulateAudioData() {
        Random random = new Random();

        while (running) {
            try {
                // Генерируем синтетические аудио данные
                for (int i = 0; i < audioBuffer.length; i++) {
                    audioBuffer[i] = (float) Math.sin((System.currentTimeMillis() + i) / 100f) * amplitude;
                    audioBuffer[i] += random.nextFloat() * 0.1f;
                }

                pulsePanel.updateAudioData(audioBuffer);

                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        running = false;
        if (targetDataLine != null) {
            targetDataLine.stop();
            targetDataLine.close();
        }
    }

    public void increaseAmplitude() {
        amplitude = Math.min(2f, amplitude + 0.1f);
    }

    public void decreaseAmplitude() {
        amplitude = Math.max(0.1f, amplitude - 0.1f);
    }

    public void resetAmplitude() {
        amplitude = 1f;
    }
}
