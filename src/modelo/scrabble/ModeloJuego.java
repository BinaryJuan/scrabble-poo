package modelo.scrabble;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;
import java.io.*;

public class ModeloJuego extends ObservableRemoto implements IModeloRemoto {
	
	private static final long serialVersionUID = 7507099928370303646L;
	private Tablero tablero;
	private BolsaFichas bolsaDeFichas;
	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private int turnoActual;
	
	// Variables para el control de fin de juego
	private int turnosConsecutivosPasados = 0;
	private static final int MAX_TURNOS_PASADOS = 5; // 5 turnos consecutivos pasados para terminar
	private boolean juegoTerminado = false;
	
	// Sistema de ranking persistente
	private Ranking ranking;
	
	// Constructor - inicializar el ranking
	public ModeloJuego() throws RemoteException {
		super();
		try {
			this.ranking = Ranking.cargar();
		} catch (Exception e) {
			System.err.println("Error al cargar ranking, creando uno nuevo: " + e.getMessage());
			this.ranking = new Ranking();
		}
	}
	
	//INTERFAZ
	
	public synchronized void addJugador(Jugador jugador) throws RemoteException {
		// Validaciones de entrada
		if (jugador == null) {
			throw new IllegalArgumentException("El jugador no puede ser null");
		}
		
		if (jugador.getNombre() == null || jugador.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del jugador no puede estar vacío");
		}
		
		// Validar que no exceda el máximo de jugadores
		if (jugadores.size() >= 4) { // Máximo típico del Scrabble
			throw new IllegalStateException("No se pueden agregar más de 4 jugadores");
		}
		
		// Validar que no exista ya un jugador con el mismo nombre
		for (Jugador j : jugadores) {
			if (j.getNombre().equalsIgnoreCase(jugador.getNombre().trim())) {
				throw new IllegalArgumentException("Ya existe un jugador con el nombre: " + jugador.getNombre());
			}
		}
		
		conectarJugador(jugador);
	}
	
	public synchronized void comenzarPartida() throws RemoteException {
		
		// Validaciones antes de comenzar
		if (jugadores.isEmpty()) {
			throw new IllegalStateException("No hay jugadores para iniciar la partida");
		}
		
		if (jugadores.size() < 2) {
			throw new IllegalStateException("Se necesitan al menos 2 jugadores para iniciar la partida");
		}
		
		if (tablero != null) {
			throw new IllegalStateException("Ya hay una partida en curso");
		}
		
		//Comenzamos la primer partida
		tablero = new Tablero(this);
		bolsaDeFichas = new BolsaFichas();
		turnoActual = -1;

		tablero.comenzarPartida(jugadores, bolsaDeFichas);
		siguienteTurno();

		notificarObservadores(Evento.NUEVA_PARTIDA);
		notificarObservadores(Evento.CAMBIO_ESTADO_PARTIDA);
		
	}
	
	
	public boolean agregarPalabra(String x, String y, Palabra palabraActual, String disposicion) throws IOException {
		
		// Validaciones de entrada
		if (palabraActual == null || palabraActual.getLetras() == null || palabraActual.getLetras().isEmpty()) {
			notificarObservadores(Evento.ERROR_VALIDACION_PALABRA);
			return false;
		}
		
		if (jugadores.isEmpty() || turnoActual < 0 || turnoActual >= jugadores.size()) {
			notificarObservadores(Evento.ERROR_VALIDACION_PALABRA);
			return false;
		}
		
		// Referencio al jugador actual
		Jugador jugadorActual = jugadores.get(getTurnoActual());
		
		// Hago un alias del conjunto de letras de la palabra
        List<Letra> letrasPalabra = palabraActual.getLetras();
		
		//Primero valido que la palabra contenga letras del atril
		for(Letra l: letrasPalabra) {
			boolean tieneLetra = false;
			for(Letra letraAtril : jugadorActual.getAtril()) {
				if(letraAtril.getDescripcion().equals(l.getDescripcion())) {
					tieneLetra = true;
					break;
				}
			}
			if(!tieneLetra) {
				notificarObservadores(Evento.ERROR_ATRIL);
				return false;
			}
		}
		
		//Segundo, valido la palabra en el diccionario
		if(!modelo.scrabble.servicios.DiccionarioFactory.obtenerInstancia().contieneA(palabraActual.getPalabra().toLowerCase())) {
			notificarObservadores(Evento.ERROR_DICCIONARIO);
			return false;
		}
	
		//Valido las coordenadas X e Y
		int X = ConfiguracionJuego.COORDENADA_CENTRO, Y = ConfiguracionJuego.COORDENADA_CENTRO;
		if(!isPrimerMovimiento()) {
			if (x == null || x.isEmpty() || y == null || y.isEmpty()) {
				notificarObservadores(Evento.ERROR_COORDENADAS);
				return false;
			}
			
			// Convertir coordenadas numéricas (String) a enteros
			try {
				X = Integer.parseInt(x.trim());
				Y = Integer.parseInt(y.trim());
			} catch (NumberFormatException e) {
				notificarObservadores(Evento.ERROR_COORDENADAS);
				return false;
			}
		
		if((X < ConfiguracionJuego.MIN_COORDENADA || X > ConfiguracionJuego.MAX_COORDENADA) || 
		   (Y < ConfiguracionJuego.MIN_COORDENADA || Y > ConfiguracionJuego.MAX_COORDENADA)) {
			notificarObservadores(Evento.ERROR_COORDENADAS);
			return false;
			}
		}
		
		//Valido las disposición
		boolean horizontal = false;
		switch(disposicion) {
		case ConfiguracionJuego.DISPOSICION_HORIZONTAL:
			horizontal = true;
			break;
		case ConfiguracionJuego.DISPOSICION_VERTICAL:
			horizontal = false;
			break;
		default:
			notificarObservadores(Evento.ERROR_DISPOSICION);
			return false;
		}
		
		if(!isPrimerMovimiento() && !validarPalabra(X, Y, palabraActual, horizontal)) {
			notificarObservadores(Evento.ERROR_VALIDACION_PALABRA);
			return false;
			}
		
		
		//Agregamos la palabra
		tablero.addPalabra(bolsaDeFichas, jugadorActual, X, Y, palabraActual, horizontal);
		
		// Resetear contador de turnos pasados consecutivos al agregar una palabra
		turnosConsecutivosPasados = 0;
		
		// Verificar si el juego debe terminar después de agregar la palabra
		if (verificarFinDeJuego()) {
			finalizarPartida();
			return true;
		}
		
		siguienteTurno();
		try {
			notificarObservadores(Evento.NUEVA_PALABRA);
			notificarObservadores(Evento.CAMBIO_ESTADO_PARTIDA);
		} catch (RemoteException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		}
		return true;
	}
	
	
	public boolean validarPalabra(int x, int y, Palabra palabraActual, boolean horizontal) throws RemoteException{
		return tablero.validarPalabra(x, y, palabraActual, horizontal, isPrimerMovimiento());
	}
	
	
	public synchronized boolean cambiarFichas(char[] fichasACambiar) throws RemoteException {
		
		// Validación de entrada
		if (fichasACambiar == null || fichasACambiar.length == 0) {
			notificarObservadores(Evento.ERROR_VALIDACION_PALABRA);
			return false;
		}
		
		// Validar que hay jugadores
		if (jugadores.isEmpty() || turnoActual < 0 || turnoActual >= jugadores.size()) {
			notificarObservadores(Evento.ERROR_VALIDACION_PALABRA);
			return false;
		}
		
		//Referencio al jugador actual
		Jugador jugadorActual = jugadores.get(turnoActual);
		
		//Devolvemos las fichas
		boolean resultado = tablero.cambiarFichas(bolsaDeFichas, jugadorActual, fichasACambiar);
		
		// Cambiar fichas cuenta como un turno pasado según reglas Scrabble
		if (resultado) {
			// Verificar si el juego debe terminar después de cambiar fichas
			if (verificarFinDeJuego()) {
				finalizarPartida();
				return true;
			}
		}
		
		return resultado;

	} 
	
	
	public synchronized void siguienteTurno() throws RemoteException{
		// Si el juego ya terminó, no hacer nada
		if (juegoTerminado) {
			System.out.println("siguienteTurno(): Juego ya terminado, no cambiando turno");
			return;
		}
		
		// Continuar con el siguiente turno sin verificar fin de juego aquí
		// Las verificaciones se hacen en los métodos que llaman a este
		if(++this.turnoActual < jugadores.size()) {
			return;
		}
		else {
			this.turnoActual = 0;
		}
	}
	
	
	public synchronized void pasarTurno() throws RemoteException{
		// Incrementar contador de turnos consecutivos pasados
		turnosConsecutivosPasados++;
		System.out.println("Turno pasado. Total consecutivos: " + turnosConsecutivosPasados);
		
		// Verificar si debe terminar el juego ANTES de cambiar turno
		if (verificarFinDeJuego()) {
			finalizarPartida();
			return;
		}
		
		siguienteTurno();
		notificarObservadores(Evento.CAMBIO_ESTADO_PARTIDA);
	}
	
	
	public int obtenerGanador() throws RemoteException{
		int mayor = jugadores.get(0).getPuntaje();
		int idGanador = 0;
		for(int j = 1; j < jugadores.size(); j++) {
			if(jugadores.get(j).getPuntaje() > mayor) {
				mayor = jugadores.get(j).getPuntaje();
				idGanador = j;
			}
		}
		return idGanador;
	}
	
	/**
	 * Verifica si el juego debe terminar según las reglas del Scrabble
	 * @return true si el juego debe terminar
	 */
	private synchronized boolean verificarFinDeJuego() throws RemoteException {
		// Si ya terminó, no verificar de nuevo
		if (juegoTerminado) {
			return true;
		}
		
		// Condición 1: No quedan más fichas en el montón
		if (bolsaDeFichas.getCantidadFichas() == 0) {
			System.out.println("Fin de juego: No quedan más fichas en el montón");
			return true;
		}
		
		// Condición 2: Un jugador ha colocado todas sus fichas (solo si no hay fichas en la bolsa)
		for (Jugador jugador : jugadores) {
			if (jugador.getAtril().isEmpty() && bolsaDeFichas.getCantidadFichas() == 0) {
				System.out.println("Fin de juego: Un jugador agotó sus fichas y no quedan más en la bolsa");
				return true;
			}
		}
		
		// Condición 3: Se han pasado demasiados turnos consecutivos
		if (turnosConsecutivosPasados >= MAX_TURNOS_PASADOS) {
			System.out.println("Fin de juego: Se pasaron " + MAX_TURNOS_PASADOS + " turnos consecutivos");
			return true;
		}
		
		// Condición 4: Todos los jugadores tienen el atril vacío (caso extremo)
		boolean todosVacios = true;
		for (Jugador jugador : jugadores) {
			if (!jugador.getAtril().isEmpty()) {
				todosVacios = false;
				break;
			}
		}
		if (todosVacios) {
			System.out.println("Fin de juego: Todos los jugadores tienen el atril vacío");
			return true;
		}
		
		return false;
	}
	
	/**
	 * Finaliza la partida y actualiza el ranking con los jugadores
	 */
	private synchronized void finalizarPartida() throws RemoteException {
		if (juegoTerminado) {
			System.out.println("finalizarPartida(): Juego ya terminado, ignorando llamada duplicada");
			return;
		}
		
		juegoTerminado = true;
		System.out.println("finalizarPartida(): Iniciando finalización de partida");
		
		// Actualizar el ranking con el puntaje antes de descontar fichas restantes
		ranking.actualizarConPartida(jugadores);
		
		// Procesar puntaje final: restar puntos de fichas sobrantes en atriles
		for (Jugador jugador : jugadores) {
			int puntajeOriginal = jugador.getPuntaje();
			int puntosARestar = calcularPuntosFichasAtril(jugador);
			int puntajeFinal = Math.max(0, puntajeOriginal - puntosARestar);
			
			jugador.setPuntaje(puntajeFinal);
		}
		
		// Mostrar información de fin de partida
		System.out.println("=== PARTIDA TERMINADA ===");
		for (int i = 0; i < jugadores.size(); i++) {
			Jugador jugador = jugadores.get(i);
			System.out.println((i + 1) + ". " + jugador.getNombre() + " - " + jugador.getPuntaje() + " puntos");
		}
		
		// Notificar a los observadores
		notificarObservadores(Evento.FIN_PARTIDA);
	}
	
	/**
	 * Calcula los puntos totales de las fichas restantes en el atril de un jugador
	 */
	private int calcularPuntosFichasAtril(Jugador jugador) {
		int puntosTotales = 0;
		for (Letra letra : jugador.getAtril()) {
			puntosTotales += letra.getPuntos();
		}
		return puntosTotales;
	}
	
	
	public boolean isPrimerMovimiento() throws RemoteException{
		int i = 0;
		boolean esPrimer = true;
		while(esPrimer && i < jugadores.size()) {
			Jugador j = jugadores.get(i);
			if (j != null && j.getPuntaje() > 0) {
				esPrimer = false;
			}
			i++;
		}
		return esPrimer;
	}
	
	
	
	//Lectura de archivos
	

	/**
	 * Obtiene el top 5 de jugadores desde el ranking persistente.
	 * Ya no lee todas las partidas, sino el ranking optimizado.
	 */
	public ArrayList<Jugador> getTop5Jugadores() throws ClassNotFoundException, IOException{
		try {
			// Cargar ranking actualizado desde archivo
			Ranking rankingActual = Ranking.cargar();
			
			// Convertir EntradaRanking a Jugador para compatibilidad
			ArrayList<Jugador> top5 = new ArrayList<>();
			for (Ranking.EntradaRanking entrada : rankingActual.getTop5()) {
				Jugador jugador = new Jugador(entrada.getNombre());
				jugador.setPuntaje(entrada.getPuntaje());
				top5.add(jugador);
			}
			
			return top5;
		} catch (Exception e) {
			System.err.println("Error al cargar ranking: " + e.getMessage());
			// En caso de error, devolver lista vacía
			return new ArrayList<>();
		}
	}
	
	
	//Metodos de observer
	
	public void notificarObservadores(Object obj) throws RemoteException {
		super.notificarObservadores(obj);
	}
	
	
	//Metodos del modelo
	
	public Jugador obtenerJugadorGanador() throws RemoteException{
		// FIX: Usar la lista real de jugadores, no crear una vacía
		if (jugadores == null || jugadores.isEmpty()) {
			return null; // No hay jugadores
		}
		
		// Crear copia para no modificar la lista original
		ArrayList<Jugador> jugadoresCopia = new ArrayList<>(this.jugadores);
		Comparator<Jugador> comp = Comparator.comparing(Jugador::getPuntaje).reversed();
		jugadoresCopia.sort(comp);
		return jugadoresCopia.get(0); 
	}
	
	private void conectarJugador(Jugador nuevoJugador) throws RemoteException {
		jugadores.add(nuevoJugador);
		notificarObservadores(Evento.NUEVO_JUGADOR);
	}
	
	
	public Jugador desconectarJugador(Jugador jugador) throws RemoteException {
		if(this.jugadores.size() > 1) {
			jugador.setConectado(false);
			this.jugadores.remove(jugador);
			notificarObservadores(Evento.JUGADOR_DESCONECTADO);
			notificarObservadores(Evento.CAMBIO_ESTADO_PARTIDA);
		}
		return jugador;
	}
	
	
	//Setters y Getters
	
	public Casillero[][] getTablero() throws RemoteException{
		return tablero.getTablero();
	}

	public int getTurnoActual() throws RemoteException{
		return turnoActual;
	}
	
	public BolsaFichas getBolsaDeFichas() throws RemoteException{
		return bolsaDeFichas;
	}
	
	/**
	 * Verifica si hay fichas disponibles de una letra específica
	 * Método más específico que exponer toda la bolsa
	 */
	public boolean hayFichasDisponibles(String letra) throws RemoteException {
		return bolsaDeFichas.tieneFichasDisponibles(letra);
	}
	
	/**
	 * Obtiene la cantidad de fichas de una letra específica
	 * Método más específico que exponer toda la bolsa
	 */
	public int getCantidadFichasLetra(String letra) throws RemoteException {
		return bolsaDeFichas.get(letra);
	}
	
	/**
	 * Obtiene una copia defensiva de la lista de jugadores
	 * @return Lista inmutable de jugadores
	 */
	public ArrayList<Jugador> getJugadores() {
		return new ArrayList<>(jugadores);
	}
	
	public boolean isVacia() throws RemoteException{
		return bolsaDeFichas.getCantidadFichas() == 0;
	}
	
	public int getCantidadFichas() throws RemoteException{
		return bolsaDeFichas.getCantidadFichas();
	}
	
	public int getCantidadJugadores() throws RemoteException{
		return jugadores.size();
	}
	
	/**
	 * Obtiene el mensaje completo de fin de partida con el ranking de jugadores.
	 * Toda la lógica de negocio se encuentra aquí para cumplir con MVC.
	 */
	public String obtenerMensajeFinPartida() throws RemoteException {
		StringBuilder mensaje = new StringBuilder();
		mensaje.append("PARTIDA FINALIZADA!\n\n");
		
		// Validar que hay jugadores
		if (jugadores == null || jugadores.isEmpty()) {
			mensaje.append("No hay jugadores en la partida.\n");
			return mensaje.toString();
		}
		
		// Crear copia de jugadores y ordenar por puntaje
		ArrayList<Jugador> ranking = new ArrayList<>(jugadores);
		ranking.sort(new Comparator<Jugador>() {
			@Override
			public int compare(Jugador j1, Jugador j2) {
				return Integer.compare(j2.getPuntaje(), j1.getPuntaje());
			}
		});
		
		// Encontrar el puntaje más alto
		int puntajeGanador = ranking.get(0).getPuntaje();
		
		// Contar cuántos jugadores tienen el puntaje más alto (empate)
		int cantidadGanadores = 0;
		for (Jugador j : ranking) {
			if (j.getPuntaje() == puntajeGanador) {
				cantidadGanadores++;
			} else {
				break; // Ya no hay más ganadores
			}
		}
		
		// Mensaje de ganador(es)
		if (cantidadGanadores > 1) {
			mensaje.append("EMPATE! Los siguientes jugadores comparten el primer lugar:\n\n");
		} else {
			mensaje.append("GANADOR: ").append(ranking.get(0).getNombre()).append("!\n\n");
		}
		
		// Ranking completo
		mensaje.append("📊 RANKING FINAL:\n");
		mensaje.append("═══════════════════════\n");
		
		for (int i = 0; i < ranking.size(); i++) {
			Jugador jugador = ranking.get(i);
			String emoji;
			
			if (i == 0) {
				emoji = "🥇"; // Oro
			} else if (i == 1) {
				emoji = "🥈"; // Plata
			} else if (i == 2) {
				emoji = "🥉"; // Bronce
			} else {
				emoji = "🏅"; // Medalla general
			}
			
			mensaje.append(String.format("%s %d. %s - %d puntos\n", 
				emoji, (i + 1), jugador.getNombre(), jugador.getPuntaje()));
		}
		
		mensaje.append("\nGracias por jugar!");
		
		return mensaje.toString();
	}
	
	/**
	 * Devuelve el top 5 de jugadores como interfaz IJugador.
	 * Cumple con MVC: la conversión se hace en el modelo.
	 * Ahora usa el ranking persistente optimizado.
	 */
	public ArrayList<IJugador> getTop5JugadoresInterfaz() throws RemoteException, ClassNotFoundException, IOException {
		try {
			// Usar directamente el método del ranking para evitar conversiones innecesarias
			return ranking.getTop5ComoIJugador();
		} catch (Exception e) {
			System.err.println("Error al obtener ranking como IJugador: " + e.getMessage());
			// En caso de error, devolver lista vacía
			return new ArrayList<>();
		}
	}
	
	/**
	 * Obtiene el ranking en formato texto para mostrar en la interfaz.
	 * Método nuevo para el sistema de ranking mejorado.
	 */
	public String getRankingTexto() throws RemoteException {
		try {
			return ranking.getRankingTexto();
		} catch (Exception e) {
			System.err.println("Error al obtener ranking texto: " + e.getMessage());
			return "Error al cargar el ranking.\nIntenta nuevamente más tarde.";
		}
	}
	


		
	

}
