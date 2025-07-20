# Scrabble
##### _Autor: Dante Terranova_

###
###
### Descripción
Proyecto para la Universidad Nacional de Luján (POO). Consiste en desarrollar un videojuego de mesa usando Paradigma Orientado a Objetos.
- Lenguaje: Java
- Interfaz gráfica: consola e interfaz gráfica con Swing
- Patrón de diseño: RMIMVC

### ¿Cómo jugar?
El objetivo del juego es obtener la mayor cantidad de puntos al formar palabras en un tablero que se conecten a las palabras (o no) creadas por los otros jugadores. Para jugar Scrabble, necesitarás al menos otro jugador (de 2 a 4 jugadores).
1. Una vez que esté el tablero, el montón y los atriles con fichas, cada jugador agarrará una ficha del montón, y la que esté más cerca de la letra "A" será el que tenga el primer turno.
2. Ahora que el jugador tiene el turno, deberá decidir si quiere:
    - Intercambiar ficha/s con las del montón: en caso de que el jugador no se de cuenta de qué palabra puede generar, no pueda, o incluso si no quiere, puede pasar el turno y agarrar fichas del montón.
    - Colocar palabra en el tablero: a través de coordenadas en el tablero se selecciona la palabra que se quiere colocar al tablero, sea vertical u horizontalmente.
    - Terminar turno: una vez que el jugador, ya sea porque no puede o no quiere generar una palabra nueva, o si formó una, esta opción permite pasar el turno y previamente preguntarle al jugador si formó o no una palabra. Si eligió la opción afirmativa, el jugador deberá seleccionar dónde empieza y termina su palabra a través de coordenadas en el tablero.
    - Fin de partida: automáticamente termina el juego y se muestra el ganador.

### Reglas generales
- El objetivo es generar la mayor cantidad de puntos.
- Cada jugador toma 7 fichas en sus atriles al comenzar el juego. Cuando termina el turno, el atril debe reponer las fichas que gastó hasta llegar a 7 (salvo que no haya gastado).
- Cada jugador podrá formar solo una palabra por turno.
- Suma de puntos: considerando la cifra de valor que cada letra tiene (las palabras valen la suma de todas las cifras de valor de cada letra). **Al terminar la partida, los jugadores deben restar de su puntaje los valores de las letras sobrantes del atril** (el puntaje final nunca puede ser menor a 0).
- Casillas del tablero: azul cielo o LD duplica el valor de la letra; azul o LT triplica el valor de la letra; rosa o PD duplica el valor de la palabra que cruce sobre ella; roja o PT triplica el valor de la palabra que cruce sobre ella.
- En esta versión se eliminaron los comodines.
- El juego termina cuando: ya no hay fichas en el monton; cuando ningún jugador puede colocar más palabras en el tablero y han pasado 5 veces consecutivas.


![](https://imgur.com/a/8uKQJrX)

### UML
![](https://imgur.com/a/lHepfCw)