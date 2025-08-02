# Diagramas UML del Proyecto Scrabble

Esta carpeta contiene los diagramas UML (PlantUML) que documentan la arquitectura del proyecto.

## Archivos incluidos:

### `scrabble-uml.puml`
- **Tipo**: Diagrama de clases completo
- **Contenido**: Arquitectura completa del proyecto incluyendo:
  - Todas las clases del modelo, vista y controlador
  - Interfaces principales (IJugador, IModeloRemoto, Vista, etc.)
  - Relaciones entre clases
  - Patrón MVC con RMI
- **Recomendado para**: Análisis arquitectural completo

### `scrabble-simple.puml`
- **Tipo**: Diagrama de clases simplificado
- **Contenido**: Vista simplificada mostrando:
  - Clases principales sin detalles
  - Relaciones más importantes
  - Estructura general del proyecto
- **Recomendado para**: Presentaciones y vista general

### `scrabble-clases-simple.puml`
- **Tipo**: Diagrama de clases básico
- **Contenido**: Clases fundamentales del dominio:
  - Entidades principales (Jugador, Letra, Tablero, etc.)
  - Interfaces básicas
  - Relaciones esenciales
- **Recomendado para**: Introducción al modelo de datos

## Cómo visualizar los diagramas:

### Opción 1: VS Code + PlantUML Extension
1. Instalar extensión "PlantUML" en VS Code
2. Abrir cualquier archivo .puml
3. Usar Ctrl+Shift+P → "PlantUML: Preview Current Diagram"

### Opción 2: PlantUML Online
1. Ir a http://www.plantuml.com/plantuml/
2. Copiar y pegar el contenido del archivo .puml
3. Ver el diagrama generado

### Opción 3: PlantUML Local
1. Descargar PlantUML JAR desde https://plantuml.com/download
2. Ejecutar: `java -jar plantuml.jar archivo.puml`
3. Se genera una imagen PNG del diagrama

## Notas:

- Los diagramas están actualizados con la arquitectura actual del proyecto
- Documentan el patrón MVC con distribución RMI
- Incluyen análisis de conceptos POO aplicados
- Útiles para presentaciones académicas y documentación técnica
