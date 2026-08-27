package com.pulsevisual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Path2D;

/**
 * PulseVisual - Визуализатор звука и пульса для Minecraft мода
 */
public class PulseVisual extends JFrame {
    private PulsePanel pulsePanel;
    private JMenuBar menuBar;
    private AudioAnalyzer audioAnalyzer;

    public PulseVisual() {
        super("PulseVisual - Audio Visualizer");
        initializeUI();
        setupAudioAnalyzer();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Создаём панель для визуализации
        pulsePanel = new PulsePanel();
        add(pulsePanel, BorderLayout.CENTER);

        // Создаём меню
        createMenu();

        // Обработчик закрытия окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (audioAnalyzer != null) {
                    audioAnalyzer.stop();
                }
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void createMenu() {
        menuBar = new JMenuBar();

        // Меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Меню "Визуальные эффекты"
        JMenu effectsMenu = new JMenu("Эффекты");
        
        JMenuItem waveEffect = new JMenuItem("Волны");
        waveEffect.addActionListener(e -> pulsePanel.setVisualizationType(VisualizationType.WAVES));
        
        JMenuItem pulseEffect = new JMenuItem("Пульс");
        pulseEffect.addActionListener(e -> pulsePanel.setVisualizationType(VisualizationType.PULSE));
        
        JMenuItem particlesEffect = new JMenuItem("Частицы");
        particlesEffect.addActionListener(e -> pulsePanel.setVisualizationType(VisualizationType.PARTICLES));
        
        JMenuItem circleEffect = new JMenuItem("Окружность");
        circleEffect.addActionListener(e -> pulsePanel.setVisualizationType(VisualizationType.CIRCLE));
        
        effectsMenu.add(waveEffect);
        effectsMenu.add(pulseEffect);
        effectsMenu.add(particlesEffect);
        effectsMenu.add(circleEffect);

        // Меню "Цвета"
        JMenu colorMenu = new JMenu("Цвета");
        
        JMenuItem blueColor = new JMenuItem("Синий");
        blueColor.addActionListener(e -> pulsePanel.setColorScheme(ColorScheme.BLUE));
        
        JMenuItem redColor = new JMenuItem("Красный");
        redColor.addActionListener(e -> pulsePanel.setColorScheme(ColorScheme.RED));
        
        JMenuItem greenColor = new JMenuItem("Зелёный");
        greenColor.addActionListener(e -> pulsePanel.setColorScheme(ColorScheme.GREEN));
        
        JMenuItem rainbowColor = new JMenuItem("Радуга");
        rainbowColor.addActionListener(e -> pulsePanel.setColorScheme(ColorScheme.RAINBOW));
        
        colorMenu.add(blueColor);
        colorMenu.add(redColor);
        colorMenu.add(greenColor);
        colorMenu.add(rainbowColor);

        // Меню "Громкость"
        JMenu volumeMenu = new JMenu("Громкость");
        
        JMenuItem volumeUp = new JMenuItem("Увеличить");
        volumeUp.addActionListener(e -> audioAnalyzer.increaseAmplitude());
        
        JMenuItem volumeDown = new JMenuItem("Уменьшить");
        volumeDown.addActionListener(e -> audioAnalyzer.decreaseAmplitude());
        
        JMenuItem volumeReset = new JMenuItem("Сбросить");
        volumeReset.addActionListener(e -> audioAnalyzer.resetAmplitude());
        
        volumeMenu.add(volumeUp);
        volumeMenu.add(volumeDown);
        volumeMenu.add(volumeReset);

        // Меню "Помощь"
        JMenu helpMenu = new JMenu("Помощь");
        
        JMenuItem aboutItem = new JMenuItem("О программе");
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(effectsMenu);
        menuBar.add(colorMenu);
        menuBar.add(volumeMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "PulseVisual v1.0.0\n\n" +
                "Визуализатор звука для Minecraft\n" +
                "Создает красивую визуализацию пульса и звука\n\n" +
                "© 2024 PulseVisual",
                "О программе",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PulseVisual());
    }
}
