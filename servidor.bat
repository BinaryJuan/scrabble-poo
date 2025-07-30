@echo off
echo ===============================================
echo       SERVIDOR SCRABBLE - INICIANDO...
echo ===============================================
echo.

REM Verificar que existan los archivos necesarios
if not exist "bin" (
    echo ERROR: No existe la carpeta 'bin'. Compile el proyecto primero.
    pause
    exit /b 1
)

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

echo Iniciando servidor RMI...
echo.
java -cp "bin;LibreriaRMIMVC.jar;jgoodies-forms-1.8.0.jar" servidor.scrabble.AppServidor

echo.
echo ===============================================
echo       SERVIDOR FINALIZADO
echo ===============================================
pause
