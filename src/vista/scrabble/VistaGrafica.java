package vista.scrabble;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import controlador.scrabble.Controlador;
import modelo.scrabble.IJugador;
import modelo.scrabble.Casillero;
import modelo.scrabble.Letra;

public class VistaGrafica implements Vista{

	//Como siempre inicio un tipo de Vista en el Cliente (Vista Gráfica, Consola, Mobile, etc.),
	//debo tener referencia del <JUGADOR> asociado a dicho cliente.
	private String nombreJugador = "";
	private IJugador cliente;
	private Controlador controlador;
	private VentanaPrincipal ventanaPrincipal;
	private VentanaTablero ventanaTablero;
	private VentanaRanking ventanaRanking;
	
	// Estado del juego
	private boolean juegoTerminado = false;
	private boolean mensajeFinMostrado = false; // Evitar múltiples diálogos

	
	//CONSTRUCTOR
	public VistaGrafica(Controlador controlador, String nombreJugador) {
		
		this.controlador = controlador;
		this.nombreJugador = nombreJugador.toUpperCase();
		ventanaPrincipal = new VentanaPrincipal();
		ventanaTablero = new VentanaTablero();
		ventanaRanking = new VentanaRanking();
		controlador.setVista(this);
		
		//FLUJO
		
		// * Nueva Partida
		//Acción de Nueva Partida
		ventanaPrincipal.nuevaPartida(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				controlador.comenzarPartida();				
			}
		});
		
		
		//Acción de Ingresar palabra
		ventanaTablero.ingresarPalabra(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if (juegoTerminado) {
					mostrarMensaje("Error: La partida ya ha terminado");
					return;
				}
				
				if(!estaConectado()) {
					mostrarMensaje("Error: Ya no puedes participar de esta partida. Inicia otra");
				}
				else if(!esTurnoActual()) {
					mostrarMensaje("Error: Espere a que los demas terminen su turno");
				}
				else {
					// Validar campos antes de enviar
					if (!ventanaTablero.validarCamposCompletos()) {
						return; // La validación ya mostró el mensaje de error
					}
					
					String cadenaString = ventanaTablero.recibirPalabra().toUpperCase().trim();
					String x = ventanaTablero.recibirCoordenadaX().trim();
					String y = ventanaTablero.recibirCoordenadaY().trim();
					String disposicion = controlador.validarDisposicion(ventanaTablero.isSelected());
					
					// Validaciones adicionales
					if (cadenaString.length() < 2) {
						mostrarMensaje("Error: La palabra debe tener al menos 2 letras");
						return;
					}
					
					if(!controlador.agregarPalabra(x, y, cadenaString, disposicion)) {
						// ARREGLO BUG #2: Restaurar fichas en el atril cuando hay error
						ventanaTablero.restaurarFichasEnError();
						mostrarMensaje("Error: La palabra '" + cadenaString + "' no es válida o no se puede colocar en esa posición");
					} else {
						ventanaTablero.limpiar();
						mostrarMensaje("¡Palabra '" + cadenaString + "' agregada correctamente!");
					}
				}
			}
		});
		
		
		//Acción de cambiar fichas
		ventanaTablero.cambiarFichas(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if (juegoTerminado) {
					mostrarMensaje("Error: La partida ya ha terminado");
					return;
				}
				
				if(!estaConectado()) {
					mostrarMensaje("Error: Ya no puedes participar de esta partida. Inicia otra");
				}
				else if(!esTurnoActual()) {
					mostrarMensaje("Error: Espere a que los demas terminen su turno");
				}
				else {
					String cadenaString = ventanaTablero.recibirCadenaString().trim();
					
					// Validaciones para cambio de fichas
					if (cadenaString.isEmpty()) {
						mostrarMensaje("Error: Debe seleccionar al menos una ficha para cambiar");
						return;
					}
					
					if (cadenaString.length() > 7) {
						mostrarMensaje("Error: No puede cambiar mas de 7 fichas a la vez");
						return;
					}
					
					if(controlador.cambiarFichas(cadenaString)) {
						ventanaTablero.limpiar();
						mostrarMensaje("Fichas cambiadas correctamente. Turno pasado.");
					} else {
						mostrarMensaje("Error: No se pudieron cambiar las fichas seleccionadas");
					}
				}
			}
		});
		
		
		//Acción de pasar turno
		ventanaTablero.pasarTurno(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if (juegoTerminado) {
					mostrarMensaje("Error: La partida ya ha terminado");
					return;
				}
				
				if(!estaConectado()) {
					mostrarMensaje("Error: Ya no puedes participar de esta partida. Inicia otra");
				}
				else if(!esTurnoActual()) {
					mostrarMensaje("Error: Espere a que los demas terminen su turno");
				}
				else {
					// Confirmar si realmente quiere pasar turno
					int confirmacion = JOptionPane.showConfirmDialog(
						ventanaTablero.parentComponent(),
						"¿Está seguro de que desea pasar su turno sin hacer ninguna jugada?",
						"Confirmar pasar turno",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
					);
					
					if (confirmacion == JOptionPane.YES_OPTION) {
						controlador.pasarTurno();
						mostrarMensaje("Turno pasado correctamente");
					}
				}
			}
		});
		
		
		//Acción de desconectar jugador
		ventanaTablero.desconectar(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				cliente = controlador.desconectarJugador(cliente);
			}
		});
		
		
		// * Ver Ranking
		//Acción de Ver Ranking
		ventanaPrincipal.verRanking(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				mostrarRanking();		
			}
		});
		
		
		//Cerrar Ventana
		ventanaTablero.cerrar(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				// Lógica de cierre si es necesaria en el futuro
			}
		});
	}	

	//Método para controlar los eventos del jugador del turno actual
	public boolean esTurnoActual() {
		IJugador jugadorTurnoActual = controlador.obtenerJugadores(controlador.obtenerTurnoActual());
		return cliente.equals(jugadorTurnoActual);
	}
	
	public boolean estaConectado() {
		return cliente.isConectado();
	}
	
	/**
	 * Bloquea toda la funcionalidad del juego cuando termina la partida
	 */
	public void bloquearJuego() {
		juegoTerminado = true;
		System.out.println("Bloqueando funcionalidad del juego - Partida terminada");
		if (ventanaTablero != null) {
			ventanaTablero.bloquearBotones();
		}
	}
	
	/**
	 * Muestra mensaje de fin de partida con diálogo modal
	 */
	public void mostrarMensajeFinPartida(String mensaje, String ganador) {
		// Evitar múltiples diálogos si ya se mostró uno
		if (mensajeFinMostrado) {
			System.out.println("Mensaje de fin ya mostrado, ignorando llamada duplicada");
			return;
		}
		
		mensajeFinMostrado = true;
		System.out.println("Mostrando mensaje de fin de partida para: " + nombreJugador);
		
		if (ventanaTablero != null) {
			// Usar SwingUtilities para asegurar que se ejecute en el hilo de eventos de Swing
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						ventanaTablero.mostrarMensajeFinPartida(mensaje, ganador);
					} catch (Exception e) {
						System.err.println("Error al mostrar mensaje de fin de partida: " + e.getMessage());
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	//Métodos de VistaGrafica
	public void iniciar() {
		ventanaPrincipal.setVisible(true);
		this.cliente = controlador.agregarJugador(nombreJugador);
	}

	public void mostrarComenzarPartida(ArrayList<IJugador> jugadores) {
		System.out.println("Iniciando vista de partida...");
		
		// Validar que hay jugadores
		if (jugadores == null || jugadores.isEmpty()) {
			System.out.println("Error: No hay jugadores para mostrar");
			mostrarMensaje("Error: No hay jugadores conectados");
			return;
		}
		
		// Validar el turno actual
		int turnoActual = controlador.obtenerTurnoActual();
		if (turnoActual < 0 || turnoActual >= jugadores.size()) {
			System.out.println("Error: Turno actual inválido: " + turnoActual);
			mostrarMensaje("Error: Problema con el turno de juego");
			return;
		}
		
		try {
			mostrarComenzarPartida();
			String nombreJugadorActual = jugadores.get(turnoActual).getNombre();
			System.out.println("Turno actual: " + turnoActual + " - Jugador: " + nombreJugadorActual);
			ventanaTablero.mostrarMensaje("Comienza la partida. Empieza el jugador " + nombreJugadorActual + ".");
		} catch (Exception e) {
			System.out.println("Error al mostrar partida: " + e.getMessage());
			e.printStackTrace();
			mostrarMensaje("Error al iniciar la interfaz de juego: " + e.getMessage());
		}
	}
	
	public void mostrarComenzarPartida() {
		try {
			System.out.println("Configurando ventana del tablero...");
			
			if (ventanaTablero == null) {
				System.out.println("Error: ventanaTablero es null");
				mostrarMensaje("Error: La ventana de juego no está inicializada");
				return;
			}
			
			if (cliente == null) {
				System.out.println("Advertencia: cliente es null");
			} else {
				ventanaTablero.setCliente(cliente);
				System.out.println("Cliente configurado en ventanaTablero");
			}
			
			ventanaTablero.setVisible(true);
			System.out.println("Ventana del tablero mostrada exitosamente");
			
		} catch (Exception e) {
			System.out.println("Error en mostrarComenzarPartida(): " + e.getMessage());
			e.printStackTrace();
			mostrarMensaje("Error al mostrar la ventana de juego: " + e.getMessage());
		}
	}

	
	public void mostrarTablero(Casillero[][] tablero) {
		ventanaTablero.mostrarTablero(tablero);
	}

	
	public void mostrarEstadoJugador(IJugador jugador) {
		int cantidadFichas = 0;
		cantidadFichas = controlador.obtenerCantidadFichas();
		
		System.out.println("Vista mostrando estado del jugador: " + jugador.getNombre() + " (Mi cliente: " + nombreJugador + ")");
		System.out.println("Atril del jugador mostrado:");
		for(Letra letra : jugador.getAtril()) {
			System.out.println("  - " + letra.getDescripcion() + " (ID: " + letra.getId() + ")");
		}
		
		ventanaTablero.mostrarEstadoJugador(jugador, cantidadFichas);
	}
	
	/**
	 * Muestra el turno actual (sin cambiar el atril del cliente)
	 */
	public void mostrarTurnoActual(IJugador jugadorTurno) {
		int cantidadFichas = controlador.obtenerCantidadFichas();
		ventanaTablero.mostrarTurnoActual(jugadorTurno, cantidadFichas);
	}
	
	/**
	 * Actualiza solo el atril del cliente específico
	 */
	public void mostrarAtrilCliente(IJugador miJugador) {
		System.out.println("Vista actualizando atril del cliente: " + miJugador.getNombre());
		System.out.println("Atril actualizado:");
		for(Letra letra : miJugador.getAtril()) {
			System.out.println("  - " + letra.getDescripcion() + " (ID: " + letra.getId() + ")");
		}
		ventanaTablero.mostrarAtrilCliente(miJugador);
	}
	
	public void mostrarRanking() {
		try {
			ventanaRanking.setVisible(true);
			// Usar el nuevo método optimizado de ranking texto
			String rankingTexto = controlador.obtenerRankingTexto();
			ventanaRanking.mostrarRankingTexto(rankingTexto);
		} catch (Exception e) {
			System.err.println("Error al mostrar ranking: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarMensaje(String mensaje) {
		ventanaTablero.mostrarMensaje(mensaje);
	}

	public void mostrarFinalPartida(IJugador jugador) {
		ventanaTablero.mostrarMensaje("El juego ha terminado. ¡Felicidades " + jugador.getNombre() + ", sos el ganador!");
	}

	
	
	
	


	
	
	
}
	

