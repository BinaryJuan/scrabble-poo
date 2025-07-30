@echo off
echo ===============================================
echo       COMPILADOR PROYECTO SCRABBLE
echo ===============================================
echo.

REM Verificar que existan los archivos JAR
if not exist "LibreriaRMIMVC.jar" (
    echo ERROR: No se encuentra LibreriaRMIMVC.jar
    pause
    exit /b 1
)

if not exist "jgoodies-forms-1.8.0.jar" (
    echo ERROR: No se encuentra jgoodies-forms-1.8.0.jar
    pause
    exit /b 1
)

echo Creando directorio bin si no existe...
if not exist "bin" mkdir bin

echo.
echo Compilando proyecto...
javac -cp "bin;jgoodies-forms-1.8.0.jar;LibreriaRMIMVC.jar" -d bin -target 8 -source 8 src/modelo/scrabble/*.java src/vista/scrabble/*.java src/controlador/scrabble/*.java src/utilidades/scrabble/*.java src/cliente/scrabble/*.java src/servidor/scrabble/*.java src/principal/scrabble/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===============================================
    echo       COMPILACION EXITOSA
    echo ===============================================
    echo.
    echo Archivos .class generados en la carpeta 'bin'
    echo Ya puede ejecutar servidor.bat y cliente.bat
) else (
    echo.
    echo ===============================================
    echo       ERROR EN LA COMPILACION
    echo ===============================================
    echo Revise los errores mostrados arriba
)

echo.
pause
