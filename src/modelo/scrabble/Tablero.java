package modelo.scrabble;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import modelo.scrabble.*;

public class Tablero implements Serializable{
	
	private static final long serialVersionUID = 4452019195532452667L;
	private ModeloJuego modelo;
	private Casillero[][] tablero = new Casillero[16][16];
	private String casilleroVacio = "__";
	
	//CONSTRUCTOR
	public Tablero(ModeloJuego modelo) {
		this.modelo = modelo;
		cargarTablero();
		cargarPremios();
	}
	
	//INTERFAZ
	
	//Comenzar Primer Partida
	
	public void comenzarPartida(ArrayList<Jugador> jugadores, BolsaFichas bolsaDeFichas) throws RemoteException {
		
		//Genero el grupo de las vocales (al menos 4 vocales en el atril)
		char vocales[] = {'A','E','I','O','U'};
		
		//Represento la cantidad actual de jugadores
		int cantidadJugadores = jugadores.size();
		
		// Crear una instancia de Random para todo el método (mejor práctica)
		Random random = new Random();
		
		//Le repartimos aleatoriamente las fichas iniciales a cada jugador
		for(int j = 0; j < cantidadJugadores; j++) {
			Jugador jugadorActual = jugadores.get(j);
			
			// Primero repartir 4 vocales para asegurar jugabilidad
			for(int v = 0; v < ConfiguracionJuego.MIN_VOCALES_INICIALES; v++) {
				Letra letraVocal = obtenerLetraAleatoria(vocales, bolsaDeFichas, random);
				if(letraVocal != null) {
					repartirFichas(bolsaDeFichas, jugadorActual, letraVocal);
				}
			}
			
			// Luego repartir las consonantes restantes hasta completar 7 fichas
			int fichasRestantes = ConfiguracionJuego.FICHAS_INICIALES_POR_JUGADOR - ConfiguracionJuego.MIN_VOCALES_INICIALES;
			for(int c = 0; c < fichasRestantes; c++) {
				Letra letraConsonante = obtenerLetraAleatoriaDisponible(bolsaDeFichas, random);
				if(letraConsonante != null) {
					repartirFichas(bolsaDeFichas, jugadorActual, letraConsonante);
				}
			}
		}
		
		// Mostrar estado final de todos los atriles después de la distribución
		System.out.println("\n=== ESTADO FINAL DE ATRILES ===");
		for(int i = 0; i < jugadores.size(); i++) {
			Jugador jug = jugadores.get(i);
			System.out.println("Jugador " + jug.getNombre() + " (ID de jugador: " + jug.getId() + "):");
			for(Letra letra : jug.getAtril()) {
				System.out.println("  - " + letra.getDescripcion() + " (ID letra: " + letra.getId() + ")");
			}
			System.out.println("");
		}
		System.out.println("===============================\n");
	}
	
	
	//Repartir fichas
	
	public void repartirFichas(BolsaFichas bolsaDeFichas, Jugador jugadorActual, Letra letra) {
		
		// Validar que la letra esté disponible en la bolsa
		if(!bolsaDeFichas.tieneFichasDisponibles(letra.getDescripcion())) {
			// Si no hay fichas disponibles, no hacer nada
			return;
		}
		
		System.out.println("Repartiendo ficha '" + letra.getDescripcion() + "' (ID: " + letra.getId() + ") al jugador: " + jugadorActual.getNombre());
		
		//Le agrego las fichas al atril del jugador
		jugadorActual.getAtril().add(letra);
		
		System.out.println("Atril del jugador " + jugadorActual.getNombre() + " después de agregar ficha:");
		for(Letra l : jugadorActual.getAtril()) {
			System.out.println("  - " + l.getDescripcion() + " (ID: " + l.getId() + ")");
		}

		//Le quito las fichas a la bolsa
		if(bolsaDeFichas.getCantidadFichas() > 0) {
			int cantidadActual = bolsaDeFichas.get(letra.getDescripcion());
			bolsaDeFichas.put(letra.getDescripcion(), cantidadActual - 1);
			bolsaDeFichas.setCantidadFichas(bolsaDeFichas.getCantidadFichas() - 1);			
		}
	}
	
	/**
	 * Obtiene una letra aleatoria de un conjunto específico (ej: vocales)
	 * que esté disponible en la bolsa
	 */
	private Letra obtenerLetraAleatoria(char[] letrasPermitidas, BolsaFichas bolsaDeFichas, Random random) {
		// Crear lista de letras disponibles del conjunto permitido
		ArrayList<Character> letrasDisponibles = new ArrayList<>();
		
		for(char letra : letrasPermitidas) {
			String letraStr = String.valueOf(letra);
			if(bolsaDeFichas.tieneFichasDisponibles(letraStr)) {
				letrasDisponibles.add(letra);
			}
		}
		
		// Si no hay letras disponibles del conjunto, intentar con cualquier letra
		if(letrasDisponibles.isEmpty()) {
			return obtenerLetraAleatoriaDisponible(bolsaDeFichas, random);
		}
		
		// Seleccionar una letra aleatoria del conjunto disponible
		int indiceAleatorio = random.nextInt(letrasDisponibles.size());
		char letraSeleccionada = letrasDisponibles.get(indiceAleatorio);
		
		return new Letra(String.valueOf(letraSeleccionada));
	}
	
	/**
	 * Obtiene cualquier letra disponible de la bolsa de fichas
	 */
	private Letra obtenerLetraAleatoriaDisponible(BolsaFichas bolsaDeFichas, Random random) {
		// Letras válidas del alfabeto español para Scrabble
		String[] letrasValidas = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
								  "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", 
								  "U", "V", "X", "Y", "Z"};
		
		// Crear lista de letras que tienen fichas disponibles
		ArrayList<String> letrasDisponibles = new ArrayList<>();
		
		for(String letra : letrasValidas) {
			if(bolsaDeFichas.tieneFichasDisponibles(letra)) {
				letrasDisponibles.add(letra);
			}
		}
		
		// Si no hay letras disponibles, retornar null
		if(letrasDisponibles.isEmpty()) {
			return null;
		}
		
		// Seleccionar una letra aleatoria de las disponibles
		int indiceAleatorio = random.nextInt(letrasDisponibles.size());
		String letraSeleccionada = letrasDisponibles.get(indiceAleatorio);
		
		return new Letra(letraSeleccionada);
	}
	
	/**
	 * Reparte fichas a un jugador hasta completar 7 fichas en su atril
	 * (usado cuando un jugador juega una palabra)
	 */
	public void completarAtrilJugador(BolsaFichas bolsaDeFichas, Jugador jugador) {
		Random random = new Random();
		
		while(jugador.getAtril().size() < ConfiguracionJuego.FICHAS_INICIALES_POR_JUGADOR && 
			  !bolsaDeFichas.estaVacia()) {
			
			Letra nuevaLetra = obtenerLetraAleatoriaDisponible(bolsaDeFichas, random);
			if(nuevaLetra != null) {
				repartirFichas(bolsaDeFichas, jugador, nuevaLetra);
			} else {
				break; // No hay más fichas disponibles
			}
		}
	}
	
	
	
	//Devolver fichas
	
	public boolean cambiarFichas(BolsaFichas bolsaDeFichas, Jugador jugadorActual, char[] fichasACambiar) throws RemoteException {

		// Validar que el jugador tiene las fichas que quiere cambiar
		for(Character c: fichasACambiar) {
			boolean tieneLetra = false;
			for(Letra letra : jugadorActual.getAtril()) {
				if(letra.getDescripcion().equals(c + "")) {
					tieneLetra = true;
					break;
				}
			}
			if(!tieneLetra) {
				modelo.notificarObservadores(Evento.ERROR_ATRIL);
				return false;
			}
		}
		
		// Validar que hay suficientes fichas en la bolsa para el intercambio
		if(bolsaDeFichas.getCantidadFichas() < fichasACambiar.length) {
			modelo.notificarObservadores(Evento.ERROR_ATRIL);
			return false;
		}
		
        // Devolver las fichas del jugador a la bolsa
        for (Character f : fichasACambiar) {
            // Buscar y eliminar la primera letra que coincida por contenido
        	Letra letraAEliminar = null;
        	for(Letra letra : jugadorActual.getAtril()) {
        		if(letra.getDescripcion().equals(f + "")) {
        			letraAEliminar = letra;
        			break;
        		}
        	}
        	
        	if(letraAEliminar != null) {
        		jugadorActual.getAtril().remove(letraAEliminar);
        		
        		// Devuelvo la ficha del atril a la bolsa
                int cantidadActual = bolsaDeFichas.get(f + "");
                bolsaDeFichas.put(f + "", cantidadActual + 1);
        	}
        }

        // Repartir nuevas fichas al jugador usando el método mejorado
        Random random = new Random();
        int cantidadARepartir = fichasACambiar.length;
        
        for (int c = 0; c < cantidadARepartir; c++) {
            Letra nuevaLetra = obtenerLetraAleatoriaDisponible(bolsaDeFichas, random);
            if(nuevaLetra != null) {
                repartirFichas(bolsaDeFichas, jugadorActual, nuevaLetra);
            } else {
                // Si no hay más fichas disponibles, terminar el intercambio
                break;
            }
        }
        
        modelo.siguienteTurno();
        modelo.notificarObservadores(Evento.CAMBIO_FICHAS);
		modelo.notificarObservadores(Evento.CAMBIO_ESTADO_PARTIDA);
        return true;
    }
	
	
	
	//Agregar palabra
	
	public void addPalabra(BolsaFichas bolsaDeFichas, Jugador jugadorActual, int x, int y, Palabra palabraActual, boolean horizontal) throws RemoteException {

		// Hago un alias del conjunto de letras de la palabra
        List<Letra> letrasPalabra = palabraActual.getLetras();
        
        // Hago un alias del atril del jugador
        List<Letra> atril = jugadorActual.getAtril();

        // Las coordenadas del usuario (1-15) se mapean directamente al array 16x16
        // donde [0] son etiquetas y [1-15] son las posiciones del juego
        // NO necesitamos restar 1 porque el array ya tiene el tamaño correcto

        // SOLUCION: Crear lista de letras reales del atril que coinciden con la palabra
        List<Letra> letrasRealesDelAtril = new ArrayList<>();
        for (Letra letraPalabra : letrasPalabra) {
            // Buscar una letra en el atril que tenga la misma descripción
            for (Letra letraAtril : atril) {
                if (letraAtril.getDescripcion().equals(letraPalabra.getDescripcion())) {
                    letrasRealesDelAtril.add(letraAtril);
                    break; // Solo tomar una instancia por letra
                }
            }
        }

        // Calculo el puntaje inicial de la palabra (usando las letras originales para el cálculo)
        int puntajePalabra = calcularPuntajePalabra(x, y, letrasPalabra, horizontal);

        // Seteo el puntaje al jugador
        jugadorActual.setPuntaje(jugadorActual.getPuntaje() + puntajePalabra);

        // Coloco las letras REALES DEL ATRIL en el tablero y las elimino del atril
        int i = x, d = y;
        for (Letra letraReal : letrasRealesDelAtril) {
            tablero[i][d] = letraReal;
            atril.remove(letraReal); // Ahora sí funciona porque son las instancias reales
            if (horizontal) {
                d++;
            } else {
                i++;
            }
        }
        
        System.out.println("Fichas eliminadas del atril. Atril actual de " + jugadorActual.getNombre() + ":");
        for (Letra l : atril) {
            System.out.println("  - " + l.getDescripcion() + " (ID: " + l.getId() + ")");
        }

        // Completar el atril del jugador con nuevas fichas
        completarAtrilJugador(bolsaDeFichas, jugadorActual);
        
        System.out.println("Atril completado de " + jugadorActual.getNombre() + ":");
        for (Letra l : atril) {
            System.out.println("  - " + l.getDescripcion() + " (ID: " + l.getId() + ")");
        }
    }
	
	public boolean validarPalabra(int x, int y, Palabra palabraActual, boolean horizontal, boolean esPrimerMovimiento) {
		
		// Validaciones de entrada
		if (palabraActual == null) {
			return false;
		}
		
		List<Letra> letrasPalabra = palabraActual.getLetras();
		if (letrasPalabra == null || letrasPalabra.isEmpty()) {
			return false;
		}
		
		// Las coordenadas del usuario (1-15) se mapean directamente al array 16x16
		// donde [0] son etiquetas y [1-15] son las posiciones del juego
		// NO necesitamos restar 1 porque el array ya tiene el tamaño correcto
		
		// Validar que las coordenadas estén dentro del rango válido (1-15)
		if (x < 1 || x > 15 || y < 1 || y > 15) {
			return false;
		}
		
		// Validar que la palabra no se salga del tablero (rango 1-15)
		int longitudPalabra = letrasPalabra.size();
		if (horizontal) {
			if (y + longitudPalabra - 1 > 15) {
				return false;
			}
		} else {
			if (x + longitudPalabra - 1 > 15) {
				return false;
			}
		}
		
		// Si es el primer movimiento, debe pasar por el centro
		if (esPrimerMovimiento) {
			int centroX = 8, centroY = 8; // Centro del tablero 15x15
			boolean pasaPorCentro = false;
			
			for (int i = 0; i < longitudPalabra; i++) {
				int posX = horizontal ? x : x + i;
				int posY = horizontal ? y + i : y;
				
				if (posX == centroX && posY == centroY) {
					pasaPorCentro = true;
					break;
				}
			}
			
			if (!pasaPorCentro) {
				return false;
			}
		}
		
		//Valido que la palabra se coloque al lado de una palabra ya existente
		int p = x, q = y;
		boolean valor = false;
		for(Letra ltr: letrasPalabra) { 
					
			if(!valor) {
				if(!tablero[p][q].getDescripcion().equals(casilleroVacio)) {
					valor = true;
				}
				else {
					if(horizontal) {
						// ARREGLO BUG #4: Validar límites del tablero antes de acceder a posiciones adyacentes
						boolean hayAdyacenteArriba = (p > 1) && !tablero[p - 1][q].getDescripcion().equals(casilleroVacio);
						boolean hayAdyacenteAbajo = (p < 15) && !tablero[p + 1][q].getDescripcion().equals(casilleroVacio);
						
						if(hayAdyacenteArriba || hayAdyacenteAbajo) {
							valor = true;
						}				
					}
					else {
						// ARREGLO BUG #4: Validar límites del tablero antes de acceder a posiciones adyacentes
						boolean hayAdyacenteIzquierda = (q > 1) && !tablero[p][q - 1].getDescripcion().equals(casilleroVacio);
						boolean hayAdyacenteDerecha = (q < 15) && !tablero[p][q + 1].getDescripcion().equals(casilleroVacio);
						
						if(hayAdyacenteIzquierda || hayAdyacenteDerecha) {
							valor = true;
						}
					}
				}
			}
					
			if(valor && tablero[p][q] instanceof Letra && !(tablero[p][q].getDescripcion().equals(casilleroVacio)) && !(tablero[p][q].getDescripcion().equals(ltr + ""))) {
				return false;
			}
			
			if(horizontal) {
				q++;				
			}
			else{
				p++;
			}
			
		}
		return valor;
	}
	
	
	private int calcularPuntajePalabra(int x, int y, List<Letra> letrasPalabra, boolean horizontal) {
		
		//Calculo el puntaje inicial
		int puntajePalabra = 0;
		int cantVecesMultiplicar = 1;
		boolean multiplicarPalabra = false;
		
		int i = x, d = y;
	
		for(Letra letra: letrasPalabra) {
			
			if(tablero[i][d].getClass() == PremioLetra.class) {
				new PuntajeFichas();
				puntajePalabra += PuntajeFichas.getPuntaje(letra + "") * tablero[i][d].getPuntos();					
			}
			else if(tablero[i][d].getClass() == PremioPalabra.class) {
				new PuntajeFichas();
				puntajePalabra += PuntajeFichas.getPuntaje(letra + "");
				cantVecesMultiplicar *= tablero[i][d].getPuntos();
				multiplicarPalabra = true;
			}
			else {
				new PuntajeFichas();
				puntajePalabra += PuntajeFichas.getPuntaje(letra + "");
			}
			
			if(horizontal) {
				d++;				
			}
			else{
				i++;
			}
		}
		
		if(multiplicarPalabra) {
			puntajePalabra *= cantVecesMultiplicar;
		}
		return puntajePalabra;
	}
	
	//Setters y Getters
	
	/**
	 * Obtiene una copia defensiva del tablero para evitar modificaciones externas
	 * @return Copia del tablero actual
	 */
	public Casillero[][] getTablero() {
		Casillero[][] copia = new Casillero[tablero.length][tablero[0].length];
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero[i].length; j++) {
				copia[i][j] = tablero[i][j];
			}
		}
		return copia;
	}
	
	//Carga de tablero
	
	private void cargarTablero() {
		Casillero casilleroVacio = new CasilleroVacio(this.casilleroVacio);
		tablero[0][0] = new CasilleroVacio("  ");
		for(int f = 1; f < tablero.length; f++) {
			for(int c = 1; c < tablero[f].length; c++) {
				tablero[f][c] = casilleroVacio;	 		
				}
			}
		//Cargar etiquetas numéricas (solo para referencia visual interna) - Para coordenadas numéricas 1-15
		for(int f = 1; f <= 15; f++) {
			tablero[f][0] = new CasilleroVacio(String.valueOf(f));  // Números de fila 1-15
			tablero[0][f] = new CasilleroVacio(String.valueOf(f));  // Números de columna 1-15
		}  
	}
	
	private void cargarPremios() {
		
	//Premio PALABRA DOBLE
	for(int pd = 1; pd <= tablero.length - 1; pd ++) {
		tablero[pd][pd] = new PremioPalabra(TipoPuntaje.DOBLE);					
		}
	for(int pd = 1; pd <= tablero.length - 1; pd ++) {
		tablero[pd][(tablero.length - 1) - (pd - 1)] = new PremioPalabra(TipoPuntaje.DOBLE);
		}
	
	//Premio LETRA TRIPLE
	for(int x = 2; x <= tablero.length - 1; x += 12) {
		for(int y = 6; y <= 10; y += 4) {
			tablero[x][y] = new PremioLetra(TipoPuntaje.TRIPLE);										
			}
		}
	for(int x = 6; x <= 10; x += 4) {
		for(int y = 2; y <= tablero.length - 1; y += 4) {
			tablero[x][y] = new PremioLetra(TipoPuntaje.TRIPLE);										
			}
		}
	
	//Premio PALABRA TRIPLE
	for(int x = 1; x <= tablero.length - 1; x += 7) {
		for(int y = 1; y <= tablero.length - 1; y += 7) {
			tablero[x][y] = new PremioPalabra(TipoPuntaje.TRIPLE);					
			}
		}	
	
	//Premio LETRA DOBLE (Alrededores)
	for(int x = 4; x <= tablero.length - 1; x += 8) {
		for(int y = 1; y <= tablero.length - 1; y += 14) {
			tablero[x][y] = new PremioLetra(TipoPuntaje.DOBLE);					
			}
		}
	for(int x = 1; x <= tablero.length - 1; x += 14) {
		for(int y = 4; y <= tablero.length - 1; y += 8) {
			tablero[x][y] = new PremioLetra(TipoPuntaje.DOBLE);					
			}
		}
	
	//Premio LETRA DOBLE (Centrales) 
	for(int x = 3; x <= tablero.length - 1; x += 10) {
		for(int y = 7; y <= 9; y += 2) {
			tablero[x][y] = new PremioLetra(TipoPuntaje.DOBLE);					
			}
		}
	for(int x = 7; x <= 9; x += 2) {
		for(int y = 7; y <= 9; y += 2) {
			tablero[x][y] = new PremioLetra(TipoPuntaje.DOBLE);					
			}
		}
	for(int x = 7; x <= 9; x += 2) {
		for(int y = 3; y <= tablero.length - 1; y += 10) {
			tablero[x][y] = new PremioLetra(TipoPuntaje.DOBLE);					
			}
		}
	for(int y = 8; y <= 8; y++) {
		for(int x = 4; x <= tablero.length - 1; x += 8) {
			tablero[x][y] = new PremioLetra(TipoPuntaje.DOBLE);
			tablero[y][x] = new PremioLetra(TipoPuntaje.DOBLE);
			}
		}
	
	//Premio PALABRA DOBLE (Estrella)
	tablero[8][8] = new PremioPalabra(TipoPuntaje.DOBLE);
	
	}
	
	/**
	 * Método de utilidad para debugging - muestra la distribución de fichas
	 */
	public void mostrarEstadoBolsa(BolsaFichas bolsaDeFichas) {
		System.out.println("ESTADO DE LA BOLSA");
		System.out.println("Fichas totales: " + bolsaDeFichas.getCantidadFichas());
		
		String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
						   "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", 
						   "U", "V", "X", "Y", "Z"};
		
		for(String letra : letras) {
			int cantidad = bolsaDeFichas.get(letra);
			if(cantidad > 0) {
				System.out.println(letra + ": " + cantidad);
			}
		}
		System.out.println("--------------------");
	}
	
}
