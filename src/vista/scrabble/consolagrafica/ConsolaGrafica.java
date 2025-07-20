package vista.scrabble.consolagrafica;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import modelo.scrabble.Casillero;
import modelo.scrabble.Evento;
import modelo.scrabble.IJugador;
import modelo.scrabble.IPartida;
import vista.scrabble.Vista;
import javax.swing.JTextArea;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import controlador.scrabble.*;
import java.awt.Rectangle;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class ConsolaGrafica implements Vista{

	//Vista debe conocer a su Controlador
	private Controlador controlador;
	private String nombreJugador = "";
	private IJugador cliente;
	private JFrame frmScrabble;
	private JTextField entrada;
	private JButton intro;
	private Flujo flujoActual;
	private JPanel panelNorte;
	private JPanel panelOeste;
	private JPanel panelEste;
	private JScrollPane panelTerminal;
	private JTextArea terminal;
	private JMenuBar menuBar;
	private JMenu iOpciones;
	private JMenuItem desconectar;
	private JPanel panelSobreNorte;
	private JLabel jugador;
	private JLabel jugadorNombre;
	
	
	//CONSTRUCTOR
		public ConsolaGrafica(Controlador controlador, String nombreJugador) {
			this.nombreJugador = nombreJugador.toUpperCase();
			this.controlador = controlador;
			controlador.setVista(this);
		}
	
	//Metodo general para procesar la entrada del usuario
	private void procesarEntrada(String entrada) {
		entrada = entrada.trim();
        if (entrada.isEmpty())
            return;
        mostrarMensaje("");
        flujoActual = flujoActual.elegirOpcion(entrada);
        if(flujoActual.getClass() != FlujoOpcionesJuego.class) {
        	flujoActual.mostarMenuTextual();        	
        }
    }
	
	//Metodo para controlar los eventos del jugador del turno actual
	public boolean esTurnoActual() {
		IJugador jugadorTurnoActual = controlador.obtenerJugadores(controlador.obtenerTurnoActual());
		return cliente.equals(jugadorTurnoActual);
	}
	
	public boolean estaConectado() {
		return cliente.isConectado();
	}


	//Inicializa la ventana principal.
	/**
	 * @wbp.parser.entryPoint
	 */
	
	private void inicializarVentana() {
		
		frmScrabble = new JFrame();
		frmScrabble.setBounds(new Rectangle(250, 0, 0, 0));
		frmScrabble.getContentPane().setForeground(new Color(255, 255, 255));
		frmScrabble.getContentPane().setBackground(new Color(0, 0, 0));
		frmScrabble.setSize(700, 728);
		frmScrabble.setResizable(true);
		frmScrabble.setTitle("Scrabble");
		
		JPanel panelPrincipal = (JPanel)frmScrabble.getContentPane();
		panelPrincipal.setLayout(new BorderLayout());
		JPanel panelSur = new JPanel();
		panelSur.setBackground(new Color(255, 255, 255));
		panelPrincipal.add(panelSur,BorderLayout.SOUTH);
		panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.X_AXIS));
		
		entrada = new JTextField();
		entrada.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!estaConectado()) {
						mostrarMensaje("<Ya no puedes participar de esta partida. Inicia otra>");
					}
					else {
						procesarEntrada(entrada.getText());
						entrada.setText("");						
					}
				}	
			}
		});
		entrada.setForeground(new Color(255, 255, 255));
		entrada.setBackground(new Color(0, 0, 0));
		entrada.setFont(new Font("Segoe UI Variable Static Text", Font.PLAIN, 13));
		intro = new JButton();
		intro.setForeground(new Color(255, 255, 255));
		intro.setBackground(new Color(0, 0, 0));
		intro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!estaConectado()) {
					mostrarMensaje("<Ya no puedes participar de esta partida. Inicia otra>");
				}
				else {
					procesarEntrada(entrada.getText());
					entrada.setText("");					
					}
				}
		});
		intro.setFont(new Font("Segoe UI Variable Static Text", Font.PLAIN, 13));
		intro.setText("Intro");
		
		panelSur.add(entrada); panelSur.add(intro);
 
		panelNorte = new JPanel();
		panelNorte.setBackground(new Color(0, 0, 0));
		panelNorte.setPreferredSize(new Dimension(100,50));
		frmScrabble.getContentPane().add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BorderLayout(0, 0));
 
		panelOeste = new JPanel();
		panelOeste.setBackground(new Color(0, 0, 0));
		panelOeste.setPreferredSize(new Dimension(80,70));
		frmScrabble.getContentPane().add(panelOeste, BorderLayout.WEST);
 
		panelEste = new JPanel();
		panelEste.setBackground(new Color(0, 0, 0));
		panelEste.setPreferredSize(new Dimension(80,70));
		frmScrabble.getContentPane().add(panelEste, BorderLayout.EAST);
		
		panelTerminal = new JScrollPane();
		panelTerminal.getVerticalScrollBar().setValue(panelTerminal.getVerticalScrollBar().getMaximum());
		panelTerminal.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelTerminal.setBackground(new Color(0, 0, 0));
		frmScrabble.getContentPane().add(panelTerminal, BorderLayout.CENTER);
		
		terminal = new JTextArea();
		terminal.setBorder(new EmptyBorder(0, 0, 0, 0));
		terminal.setFont(new Font("JetBrains Mono", Font.PLAIN, 17));
		terminal.setEditable(false);
		terminal.setForeground(new Color(255, 255, 255));
		terminal.setBackground(new Color(0, 0, 0));
		panelTerminal.setViewportView(terminal);
		
		menuBar = new JMenuBar();
		frmScrabble.setJMenuBar(menuBar);
		
		iOpciones = new JMenu("Opciones");
		menuBar.add(iOpciones);
		
		desconectar = new JMenuItem("Desconectar");
		desconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cliente = controlador.desconectarJugador(cliente);
			}
		});
		iOpciones.add(desconectar);
		
		panelSobreNorte = new JPanel();
		menuBar.add(panelSobreNorte);
		panelSobreNorte.setLayout(new GridLayout(0, 6, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panelSobreNorte.add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		panelSobreNorte.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 255, 255));
		panelSobreNorte.add(panel_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(255, 255, 255));
		panelSobreNorte.add(panel_3);
		
		jugador = new JLabel("Jugador:");
		panel_3.add(jugador);
		
		jugadorNombre = new JLabel("");
		panel_3.add(jugadorNombre);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(255, 255, 255));
		panelSobreNorte.add(panel_4);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(255, 255, 255));
		panelSobreNorte.add(panel_5);
		
	}
	
	
	public void iniciar() {
		inicializarVentana();
		frmScrabble.setVisible(true);
		this.cliente = controlador.agregarJugador(nombreJugador);
		jugadorNombre.setText(cliente.getNombre());
		flujoActual = new FlujoMenuPrincipal(this, controlador);
        flujoActual.mostarMenuTextual();
	}
	
	//Metodos para imprimir por pantalla
	public void mostrarMensaje(String mensaje) {
	    terminal.append(mensaje + "\n");
	}
	
	public void mostrarComenzarPartida(ArrayList<IJugador> jugadores) {
		int turnoActual = 0;
		turnoActual = controlador.obtenerTurnoActual();
		mostrarMensaje("Comienza la partida. Empieza el jugador " + jugadores.get(turnoActual).getNombre() + ".");
	}

	public void mostrarTablero(Casillero[][] tablero) {
		String obtenerTablero = "";
		for(int f = 0; f < tablero.length; f++) {
			for(int c = 0; c < tablero[f].length; c++) {
				obtenerTablero += tablero[f][c].getDescripcion() + " ";
			}
			obtenerTablero += "\n"; 
		}
		mostrarMensaje(obtenerTablero);
	}

	public void mostrarEstadoJugador(IJugador jugador) {
		String obtenerEstadoJugador = null;
		obtenerEstadoJugador = "CANT. FICHAS: " + controlador.obtenerCantidadFichas() + "\n"
				+ "Jugador: " + jugador.getNombre() + "\n"
				+ "ATRIL: " + jugador.getAtril() + "\n"
				+ "PUNTAJE: " + jugador.getPuntaje() + "\n";
		mostrarMensaje(obtenerEstadoJugador);
		mostrarFlujoOpcionesJuego();
	}

	public void mostrarPartidasGuardadas(Object arg0) {
		ArrayList<IPartida> listaPartidas = new ArrayList<>();
		try {
			listaPartidas = controlador.obtenerPartidas();
			for(IPartida p: listaPartidas) {
				mostrarMensaje(p.toString());					
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarPartidasGuardadas() {}

	public void mostrarRanking() {
		try {
			// Usar el nuevo metodo optimizado de ranking texto
			String rankingTexto = controlador.obtenerRankingTexto();
			mostrarMensaje("==================================================");
			mostrarMensaje(rankingTexto);
			mostrarMensaje("==================================================");
		} catch (Exception e) {
			System.err.println("Error al mostrar ranking: " + e.getMessage());
			mostrarMensaje("Error al cargar el ranking.");
			mostrarMensaje("Intenta nuevamente mas tarde.");
			e.printStackTrace();
		}
	}
	
	public void mostrarFlujoOpcionesJuego() {
		flujoActual = new FlujoOpcionesJuego(this,controlador);
		flujoActual.mostarMenuTextual();
	}
	
	public void mostrarMensajePartidaGuardada() {
		mostrarMensaje("Se ha guardado la partida.");
		flujoActual = new FlujoMenuPrincipal(this,controlador);
		flujoActual.mostarMenuTextual();
	}

	@Override
	public void mostrarPartidasGuardadas(ArrayList<IPartida> partidas) {
		// Mostrar partidas guardadas en consola
		
	}

	public void mostrarFinalPartida(IJugador jugador) {
		flujoActual = new FlujoFinalPartida(this, controlador, jugador);
		flujoActual.mostarMenuTextual();
	}

	@Override
	public void mostrarTurnoActual(IJugador jugadorTurno) {
		// En consola grafica, simplemente muestra un mensaje
		mostrarMensaje("Turno de: " + jugadorTurno.getNombre());
	}

	@Override
	public void mostrarAtrilCliente(IJugador miJugador) {
		// En consola grafica, muestra el atril del jugador
		mostrarEstadoJugador(miJugador);
	}
	
	@Override
	public void bloquearJuego() {
		// En consola grafica, simplemente deshabilitar la entrada
		if (entrada != null) {
			entrada.setEnabled(false);
			entrada.setText("PARTIDA TERMINADA");
		}
		if (intro != null) {
			intro.setEnabled(false);
			intro.setText("Fin");
		}
		mostrarMensaje("🏆 PARTIDA TERMINADA - Toda funcionalidad bloqueada");
	}
	
	}
	
	

