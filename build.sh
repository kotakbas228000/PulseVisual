#!/bin/bash

# Компиляция и создание JAR файла для PulseVisual мода

echo "=== PulseVisual Build Script ==="
echo "Компиляция исходного кода..."

# Создаём директорию для скомпилированных файлов
mkdir -p out/classes
mkdir -p Jar

# Компилируем Java файлы
javac -d out/classes src/main/java/com/pulsevisual/*.java

# Проверяем успешность компиляции
if [ $? -eq 0 ]; then
    echo "✓ Компиляция успешна!"
    
    # Создаём JAR файл
    echo "Создание JAR файла..."
    cd out/classes
    jar cfm ../../Jar/PulseVisual.jar ../../MANIFEST.MF com/
    cd ../../
    
    echo "✓ JAR файл создан: Jar/PulseVisual.jar"
    echo ""
    echo "=== Инструкции по установке ==="
    echo "1. Скопируйте Jar/PulseVisual.jar в папку mods вашего Minecraft"
    echo "2. Установите Forge для вашей версии Minecraft"
    echo "3. Запустите Minecraft и наслаждайтесь визуализацией!"
else
    echo "✗ Ошибка при компиляции!"
    exit 1
fi
