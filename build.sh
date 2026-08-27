#!/bin/bash

# Скрипт сборки PulseVisual мода для Minecraft Fabric 1.21.4

echo "=== PulseVisual Build Script для Fabric 1.21.4 ==="
echo "Компиляция исходного кода..."

# Создаём директории
mkdir -p Jar

# Компилируем через Maven
if command -v mvn &> /dev/null; then
    echo "Использую Maven для сборки..."
    mvn clean package
    
    if [ $? -eq 0 ]; then
        echo "✓ Сборка Maven успешна!"
        # Копируем JAR в папку Jar
        cp target/PulseVisual-1.21.4.jar Jar/
        echo "✓ JAR файл скопирован в Jar/PulseVisual-1.21.4.jar"
    else
        echo "✗ Ошибка при сборке Maven!"
        exit 1
    fi
else
    echo "Maven не найден. Пожалуйста, установите Maven для компиляции Fabric мода."
    exit 1
fi

echo ""
echo "=== Инструкции по установке ==="
echo "1. Убедитесь, что у вас установлен Minecraft Fabric 1.21.4"
echo "2. Скопируйте Jar/PulseVisual-1.21.4.jar в папку ~/.minecraft/mods/"
echo "3. Запустите Minecraft с Fabric Loader"
echo "4. Нажимайте 'V' в игре для включения/отключения визуализации"
echo "5. Нажимайте 'B' для смены типа визуализации"
echo ""
echo "Горячие клавиши:"
echo "  V - Включить/Выключить визуализацию"
echo "  B - Смена типа визуализации (Волны -> Пульс -> Частицы -> Окружность)"
