package controlador.scrabble;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import modelo.scrabble.*;
import vista.scrabble.Vista;
import vista.scrabble.VistaGrafica;

public class Controlador implements IControladorRemoto{
	
	private Vista vista;
	private IModeloRemoto modelo;
	private IJugador jugadorCliente; // Referencia al jugador de este cliente espec�fico
	private boolean finPartidaProcesado = false; // Evitar procesar FIN_PARTIDA m�ltiples veces
	
	//CONSTRUCTOR
		public Controlador(IModeloRemoto modelo) {
			try {
				setModeloRemoto(modelo);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		public Controlador() {}
		
		public void setVista(Vista vista) {
			this.vista = vista;
		}
		
		public <T extends IObservableRemoto> void setModeloRemoto(T modelo) throws RemoteException {
			this.modelo = (IModeloRemoto) modelo;
		}
		
		
	public IJugador agregarJugador(String nombreJugador) {
		
		// Validaciones de entrada
		if (nombreJugador == null || nombreJugador.trim().isEmpty()) {
			vista.mostrarMensaje("Error: El nombre del jugador no puede estar vac�o");
			return null;
		}
		
		if (modelo == null) {
			vista.mostrarMensaje("Error: No hay conexi�n con el servidor");
			return null;
		}
		
		try {
			System.out.println("Agregando jugador: " + nombreJugador.trim());
			IJugador jugador = new Jugador(nombreJugador.trim());
			this.jugadorCliente = jugador; // Guardar referencia al jugador de este cliente
			modelo.addJugador((Jugador) jugador);
			System.out.println("Jugador agregado exitosamente al modelo");
			
			// Verificar que el jugador se agreg� correctamente
			try {
				ArrayList<Jugador> jugadores = modelo.getJugadores();
				System.out.println("Total de jugadores en el modelo: " + jugadores.size());
				for (Jugador j : jugadores) {
					System.out.println("- " + j.getNombre());
				}
			} catch (IOException e) {
				System.out.println("Error al verificar jugadores: " + e.getMessage());
			}
			
			return jugador;
		} catch (IllegalArgumentException e) {
			System.out.println("Error de argumento al agregar jugador: " + e.getMessage());
			vista.mostrarMensaje("Error: " + e.getMessage());
			return null;
		} catch (IllegalStateException e) {
			System.out.println("Error de estado al agregar jugador: " + e.getMessage());
			vista.mostrarMensaje("Error: " + e.getMessage());
			return null;
		} catch (RemoteException e) {
			System.out.println("Error de conexi�n al agregar jugador: " + e.getMessage());
			vista.mostrarMensaje("Error de conexi�n: " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("Error inesperado al agregar jugador: " + e.getMessage());
			vista.mostrarMensaje("Error inesperado: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
		
		
		public IJugador desconectarJugador(IJugador cliente) {
			try {
				return modelo.desconectarJugador((Jugador) cliente);
			} catch (RemoteException e) {
// Manejo de excepcion
				e.printStackTrace();
				return null;
			}
		}
		
		
	public void comenzarPartida() {
		if (modelo == null) {
			vista.mostrarMensaje("Error: No hay conexi�n con el servidor");
			System.out.println("Error: Modelo es null en comenzarPartida()");
			return;
		}
		
		try {
			// Verificar cu�ntos jugadores hay antes de comenzar
			ArrayList<Jugador> jugadores = modelo.getJugadores();
			System.out.println("Verificando jugadores antes de comenzar partida...");
			System.out.println("Total de jugadores: " + jugadores.size());
			for (Jugador j : jugadores) {
				System.out.println("- " + j.getNombre() + " (conectado: " + j.isConectado() + ")");
			}
			
			if (jugadores.size() < 2) {
				String mensaje = "Se necesitan al menos 2 jugadores para comenzar (hay " + jugadores.size() + ")";
				System.out.println(mensaje);
				vista.mostrarMensaje(mensaje);
				return;
			}
			
			System.out.println("Iniciando partida en el servidor...");
			modelo.comenzarPartida();
			System.out.println("Partida iniciada exitosamente en el servidor");
		} catch (IllegalStateException e) {
			String mensaje = "Error de estado: " + e.getMessage();
			System.out.println(mensaje);
			vista.mostrarMensaje(mensaje);
		} catch (Exception e) {
			String mensaje = "Error al comenzar partida: " + e.getMessage();
			System.out.println(mensaje);
			vista.mostrarMensaje(mensaje);
			e.printStackTrace();
		}
	}
		
		
	public boolean agregarPalabra(String x, String y, String cadenaString, String disposicion) {
		
		// Validaciones de entrada en el controlador
		if (cadenaString == null || cadenaString.trim().isEmpty()) {
			vista.mostrarMensaje("Error: La palabra no puede estar vac�a");
			return false;
		}
		
		if (x == null || x.trim().isEmpty() || y == null || y.trim().isEmpty()) {
			vista.mostrarMensaje("Error: Las coordenadas no pueden estar vac�as");
			return false;
		}
		
		if (disposicion == null || (!disposicion.equals(ConfiguracionJuego.DISPOSICION_HORIZONTAL) && 
									!disposicion.equals(ConfiguracionJuego.DISPOSICION_VERTICAL))) {
			vista.mostrarMensaje("Error: Disposici�n inv�lida");
			return false;
		}
		
		//Creo la nueva palabra dentro del Controlador
		Palabra nuevaPalabra = new Palabra(cadenaString.trim().toUpperCase());
		
		//Casteo las coordenadas antes de agregarlas al Modelo
		x = x.toUpperCase().trim();
			y = y.toUpperCase();
			
			try {
				return modelo.agregarPalabra(x, y, nuevaPalabra, disposicion);
			} catch (IOException e) {
// Manejo de excepcion
				e.printStackTrace();
			}
			return false;
			
		}
		
		
	public boolean cambiarFichas(String cadena) {
		// Validaciones de entrada
		if (cadena == null || cadena.trim().isEmpty()) {
			vista.mostrarMensaje("Error: Debe especificar las fichas a cambiar");
			return false;
		}
		
		if (modelo == null) {
			vista.mostrarMensaje("Error: No hay conexi�n con el servidor");
			return false;
		}
		
		String cadenaLimpia = cadena.trim().toUpperCase();
		
		// Validar que no exceda el l�mite de fichas
		if (cadenaLimpia.length() > ConfiguracionJuego.FICHAS_INICIALES_POR_JUGADOR) {
			vista.mostrarMensaje("Error: No puede cambiar m�s de " + 
								ConfiguracionJuego.FICHAS_INICIALES_POR_JUGADOR + " fichas");
			return false;
		}
		
		// Validar que solo contenga letras v�lidas
		if (!cadenaLimpia.matches("^[A-Z������]+$")) {
			vista.mostrarMensaje("Error: Solo puede cambiar letras v�lidas del alfabeto espa�ol");
			return false;
		}
		
		try {
			char[] cadenaCaracteres = cadenaLimpia.toCharArray();
			return modelo.cambiarFichas(cadenaCaracteres);
		} catch (RemoteException e) {
			vista.mostrarMensaje("Error de conexi�n: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
		
		
		public boolean validarPalabra(int x, int y, String cadenaString, boolean horizontal) {
			
			//Creo la nueva palabra dentro del Controlador
			Palabra nuevaPalabra = new Palabra(cadenaString);
					
			//La envio al modelo
			try {
				return modelo.validarPalabra(x, y, nuevaPalabra, horizontal);
			} catch (RemoteException e) {
				e.printStackTrace();
				return false;
			}
			
		}
		
		
		public String validarDisposicion(boolean horizontal) {
			String disp = "";
			if(horizontal) {
				disp = "1";
			}
			else {
				disp = "2";
			}
			return disp;
		}
		
		
		public boolean validarFlujo() {
			boolean avanzar = true;
			try {
				if(modelo.isPrimerMovimiento()) {
					avanzar = false;
				}
			} catch (RemoteException e) {
// Manejo de excepcion
				e.printStackTrace();
			}
			return avanzar;
		}
		
		
		public void cargarPartida(int idPartida) throws IOException{
			try {
				modelo.cargarPartida(idPartida);			
			}
			catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		
		
		public void guardarPartida() throws IOException{
			try {
				modelo.guardarPartida();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		
		
		public ArrayList<IJugador> obtenerTop5Jugadores() throws IOException{
			try {
				// CUMPLIMIENTO MVC: Solo llamar al modelo
				return modelo.getTop5JugadoresInterfaz();
			} catch (ClassNotFoundException | RemoteException e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
		
		/**
		 * Obtiene el ranking en formato texto optimizado.
		 */
		public String obtenerRankingTexto() {
			try {
				// CUMPLIMIENTO MVC: Solo llamar al modelo
				return modelo.getRankingTexto();
			} catch (RemoteException e) {
				e.printStackTrace();
				return "Error al cargar el ranking.\nIntenta nuevamente m�s tarde.";
			}
		}
		
		
		public ArrayList<IPartida> obtenerPartidas() throws IOException{
			try {
				// CUMPLIMIENTO MVC: Solo llamar al modelo
				return modelo.getListaPartidasInterfaz();
			} catch (ClassNotFoundException | RemoteException e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
		
		
		public Casillero[][] obtenerTablero() {
			try {
				return modelo.getTablero();
			} catch (RemoteException e) {
				return null;
			}
		}
		
		
		public int obtenerGanador() throws RemoteException{
			return modelo.obtenerGanador();
		}
		
		
		public ArrayList<IJugador> obtenerJugadores() throws RemoteException{
			try {
				ArrayList<Jugador> jugadores = modelo.getJugadores();
				ArrayList<IJugador> listaJugadores = new ArrayList<>();		
				for(IJugador j: jugadores) {
					listaJugadores.add(j);
				}
				return listaJugadores;
			} catch (IOException e) {
				System.out.println("Error de IO al obtener jugadores: " + e.getMessage());
				e.printStackTrace();
				throw new RemoteException("Error al obtener jugadores del servidor", e);
			} catch (Exception e) {
				System.out.println("Error inesperado al obtener jugadores: " + e.getMessage());
				e.printStackTrace();
				throw new RemoteException("Error inesperado al obtener jugadores", e);
			}
		}
		
		
		public IJugador obtenerJugadores(int idJugador){
			try {
				return modelo.getJugadores().get(idJugador);
			} catch (RemoteException e) {
				return null;
			}
		}
		
		
		public boolean bolsaEstaVacia() throws RemoteException {
			return modelo.isVacia();
		}
		
		
		public int obtenerCantidadFichas() {
			try {
				return modelo.getCantidadFichas();
			} catch (RemoteException e) {
// Manejo de excepcion
				e.printStackTrace();
				return -1;		}
	}
	
	/**
	 * Busca un jugador por nombre en la lista del servidor
	 */
	private IJugador buscarJugadorPorNombre(String nombre) {
		try {
			ArrayList<Jugador> jugadores = modelo.getJugadores();
			for (Jugador jugador : jugadores) {
				if (jugador.getNombre().equals(nombre)) {
					return jugador;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al buscar jugador por nombre: " + e.getMessage());
		}
		return null;
	}
	
		public int obtenerTurnoActual() {
			try {
				return modelo.getTurnoActual();
			} catch (RemoteException e) {
				return -1;
			}
		}
		
		
		public void pasarTurno() {
			try {
				modelo.pasarTurno();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		
		public void actualizar(IObservableRemoto arg0, Object arg1) throws RemoteException {
			if(arg1 instanceof Evento) {
				switch ((Evento) arg1) {
				case NUEVO_JUGADOR:
					vista.mostrarMensaje("El usuario se ha conectado exitosamente.");
					break;
				case JUGADOR_DESCONECTADO:
					vista.mostrarMensaje("El usuario se ha desconectado.");
					break;
				case NUEVA_PARTIDA:
					System.out.println("Evento NUEVA_PARTIDA recibido");
					try {
						ArrayList<IJugador> jugadores = obtenerJugadores();
						System.out.println("Jugadores obtenidos: " + (jugadores != null ? jugadores.size() : "null"));
						if (jugadores != null && !jugadores.isEmpty()) {
							vista.mostrarComenzarPartida(jugadores);
						} else {
							System.out.println("No hay jugadores para mostrar");
							vista.mostrarMensaje("Error: No hay jugadores conectados para iniciar la partida");
						}
					} catch (RemoteException e) {
						System.out.println("Error remoto al obtener jugadores: " + e.getMessage());
						e.printStackTrace();
						vista.mostrarMensaje("Error de conexi�n al obtener jugadores: " + e.getMessage());
					} catch (Exception e) {
						System.out.println("Error inesperado al manejar evento NUEVA_PARTIDA: " + e.getMessage());
						e.printStackTrace();
						vista.mostrarMensaje("Error al iniciar la partida: " + e.getMessage());
					}
					break;
				case PARTIDA_CARGADA:
					vista.mostrarMensaje("Se ha cargado la partida exitosamente.");
					break;
				case PARTIDA_GUARDADA:
					vista.mostrarMensajePartidaGuardada();
					try {
						vista.mostrarPartidasGuardadas(obtenerPartidas());
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case NUEVA_PALABRA:
					// Los mensajes de confirmaci�n se muestran localmente en VistaGrafica
					break;
				case CAMBIO_FICHAS:
					// Los mensajes de confirmaci�n se muestran localmente en VistaGrafica
					break;
				case CAMBIO_ESTADO_PARTIDA:
					Casillero[][] tablero = modelo.getTablero();
					int turnoActual = modelo.getTurnoActual();
					if(modelo.getCantidadJugadores() != 0) {
						Jugador jugadorActual = modelo.getJugadores().get(turnoActual);
						vista.mostrarTablero(tablero);	
						
						// SOLUCION SEPARADA: 
						// 1. Mostrar el turno actual (siempre del jugador que tiene el turno)
						vista.mostrarTurnoActual(jugadorActual);
						
						// 2. Mostrar MI atril espec�fico (del cliente)
						System.out.println("Jugador del turno: " + jugadorActual.getNombre());
						System.out.println("Mi cliente es: " + (jugadorCliente != null ? jugadorCliente.getNombre() : "no identificado"));
						
						if (jugadorCliente != null) {
							// Buscar MI jugador actualizado en la lista del servidor
							IJugador miJugadorActualizado = buscarJugadorPorNombre(jugadorCliente.getNombre());
							if (miJugadorActualizado != null) {
								vista.mostrarAtrilCliente(miJugadorActualizado);
							} else {
								vista.mostrarAtrilCliente(jugadorCliente);
							}
						} else {
							// Fallback: mostrar estado completo si no se puede identificar el cliente
							vista.mostrarEstadoJugador(jugadorActual);
						}
					}
					break;
				case FIN_PARTIDA:
					// Evitar procesar m�ltiples eventos FIN_PARTIDA
					if (finPartidaProcesado) {
						System.out.println("Evento FIN_PARTIDA ya procesado, ignorando duplicado");
						break;
					}
					finPartidaProcesado = true;
					System.out.println("Procesando evento FIN_PARTIDA");
					
					try {
						// CUMPLIMIENTO MVC: Solo llamar al modelo, sin l�gica de negocio
						String mensajeFinPartida = modelo.obtenerMensajeFinPartida();
						Jugador ganador = modelo.obtenerJugadorGanador();
						String nombreGanador = (ganador != null) ? ganador.getNombre() : null;
						
						// Solo delegar a la vista
						if (vista instanceof VistaGrafica) {
							((VistaGrafica) vista).mostrarMensajeFinPartida(mensajeFinPartida, nombreGanador);
						} else {
							vista.mostrarMensaje(mensajeFinPartida);
						}
						
						vista.bloquearJuego();
						
					} catch (RemoteException e) {
						System.out.println("Error al obtener informaci�n del fin de partida: " + e.getMessage());
						vista.mostrarMensaje("?? �PARTIDA TERMINADA!\nError al mostrar el ranking final.");
						vista.bloquearJuego();
					}
					break;
				case ERROR_ATRIL:
					vista.mostrarMensaje("<Ingrese una palabra que contenga las letras de su atril.>");
					break;
				case ERROR_COORDENADAS:
					vista.mostrarMensaje("<Ingrese coordenadas num�ricas entre 1 y 15.>");
					break;
				case ERROR_DICCIONARIO:
					vista.mostrarMensaje("<La palabra ingresada no es valida, intente con otra.>");
					break;
				case ERROR_VALIDACION_PALABRA:
					vista.mostrarMensaje("<La palabra debe al menos estar en contacto con una ficha ya existente.>");
					break;
				case ERROR_DISPOSICION:
					vista.mostrarMensaje("<Ingrese un n�mero valido entre 1 y 2.>");
					break;
				}
			}
		}

		

	
	

}
