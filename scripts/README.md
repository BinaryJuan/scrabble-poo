# Scripts del Proyecto Scrabble

Esta carpeta contiene los scripts de automatización para compilar y ejecutar el proyecto Scrabble.

## Archivos incluidos:

### `compilar.bat`
- **Propósito**: Compila todo el proyecto Java
- **Prerequisitos**: Java JDK 8 o superior instalado
- **Uso**: Ejecutar desde esta carpeta o hacer doble clic
- **Resultado**: Genera archivos .class en la carpeta `bin/`

### `servidor.bat`
- **Propósito**: Inicia el servidor RMI del juego
- **Prerequisitos**: Proyecto compilado (ejecutar `compilar.bat` primero)
- **Puerto por defecto**: 1099 (puerto estándar RMI)
- **Uso**: Ejecutar después de compilar el proyecto

### `cliente.bat`
- **Propósito**: Inicia un cliente con interfaz gráfica
- **Prerequisitos**: Servidor ejecutándose
- **Puertos sugeridos**: 9999, 9998, 9997, etc. (para múltiples clientes)
- **Uso**: Puede ejecutar múltiples instancias para varios jugadores

## Orden de ejecución recomendado:

1. `compilar.bat` - Compila el proyecto
2. `servidor.bat` - Inicia el servidor
3. `cliente.bat` - Inicia cliente(s) para jugar

## Notas importantes:

- Todos los scripts verifican automáticamente las dependencias
- Los paths están configurados relativamente desde esta carpeta
- Para múltiples clientes, usar puertos diferentes cuando se solicite
- Los archivos JAR deben estar en la carpeta raíz del proyecto
