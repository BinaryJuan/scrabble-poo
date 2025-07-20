package vista.scrabble;

import javax.swing.JFrame;
import modelo.scrabble.Casillero;
import modelo.scrabble.IJugador;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.Cursor;
import utilidades.scrabble.*;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.Timer;

public class VentanaTablero implements Ventana{

	private JFrame frmScrabble;
	private JLabel panelNotificaciones; 
	private JLabel jugador;
	private JLabel turnoDe;
	private JLabel puntaje;
	private JLabel cntFichas;
	private JButton enviarPalabra;
	private JButton cambioFichas;
	private JButton pasarTurno;
	private JButton desconectar; 
	private JRadioButton horizontal;
	private JRadioButton vertical;
	private JTable tablero;
	private JTableAtril tablaAtril;
	private JTextField coorY;
	private JTextField coorX;
	private JTextField palabra;
	
	// Paneles para cambiar colores según el turno
	private JPanel panelNorte;
	private JPanel panelJugadorInfo;
	private JPanel panelDerecho;
	
	// Estado del juego
	private boolean juegoTerminado = false;
	private boolean mensajeFinMostrado = false; // Evitar múltiples diálogos
	
	
	//CONSTRUCTOR
	public VentanaTablero() {
		inicializarVentana();
	}
	
	public void setCliente(IJugador cliente) {
		// Variable cliente ya no es necesaria como campo
		jugador.setText(cliente.getNombre());
	}
	
	

	public void setVisible(boolean b) {
		frmScrabble.setVisible(b);
	}
	

	public void mostrarMensaje(String mensaje) {
		panelNotificaciones.setText(mensaje);
		panelNotificaciones.setVisible(true);
		
		// Si el mensaje contiene palabras de error, ponerlo en rojo
		if (mensaje.toLowerCase().contains("error") || 
			mensaje.toLowerCase().contains("inválid") || 
			mensaje.toLowerCase().contains("no válid") ||
			mensaje.toLowerCase().contains("espere") ||
			mensaje.toLowerCase().contains("turno")) {
			panelNotificaciones.setForeground(new Color(220, 53, 69)); // Rojo
		} else {
			panelNotificaciones.setForeground(Color.WHITE); // Blanco normal
		}
		
		// Auto-ocultar después de 5 segundos si es un mensaje de error
		if (mensaje.toLowerCase().contains("error")) {
			Timer timer = new Timer(5000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					panelNotificaciones.setText("");
					panelNotificaciones.setVisible(false);
				}
			});
			timer.setRepeats(false);
			timer.start();
		}
	}


	public void mostrarTablero(Casillero[][] tableroCompleto) {
		// El tablero interno es 16x16 donde [0] son etiquetas y [1-15] es el juego
		// Extraer solo la parte del juego (15x15) saltando las etiquetas
		Object[][] datosJuego = new Object[15][15];
		
		for (int i = 1; i <= 15; i++) {          // Saltar fila 0 (etiquetas)
			for (int j = 1; j <= 15; j++) {      // Saltar columna 0 (etiquetas)
				datosJuego[i-1][j-1] = tableroCompleto[i][j];  // Mapear a 0-14
			}
		}
		
		// Usar la metodología anterior que funcionaba
		this.tablero.setModel(new ModeloTablero(datosJuego,
				new String[] {
					"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"
				}
			));
		
		// Aplicar renderer de colores después de cargar el modelo
		for (int i = 0; i < tablero.getColumnCount(); i++) {
			tablero.getColumnModel().getColumn(i).setCellRenderer(new ColoredTableCellRenderer());
		}
	}


	public void ingresarPalabra(ActionListener accion) {
		enviarPalabra.addActionListener(accion);
	}
	
	
	public void cambiarFichas(ActionListener accion) {
		cambioFichas.addActionListener(accion);
	}
	
	
	public void pasarTurno(ActionListener accion) {
		this.pasarTurno.addActionListener(accion);
	}
	
	
	public String recibirPalabra() {
		return palabra.getText();
	}
	
	
	public String recibirCoordenadaX() {
		return coorX.getText();
	}
	
	
	public String recibirCoordenadaY() {
		return coorY.getText();
	}
	

	public String recibirCadenaString() {
		return palabra.getText(); // Ahora usa el mismo campo que la palabra
	}
	
	public boolean isSelected() {
		return horizontal.isSelected();
	}
	
	public void mostrarEstadoJugador(IJugador jugador, int cantidadFichas) {
		this.turnoDe.setText(jugador.getNombre());
		this.puntaje.setText(jugador.getPuntaje() + "");
		this.tablaAtril.setModel(new ModeloAtril(jugador.getAtril()));
		this.cntFichas.setText(cantidadFichas + "");
	}
	
	/**
	 * Actualiza solo el turno e información general, sin cambiar el atril
	 * También actualiza el color de la franja superior según quien tiene el turno
	 */
	public void mostrarTurnoActual(IJugador jugadorTurno, int cantidadFichas) {
		this.turnoDe.setText(jugadorTurno.getNombre());
		this.cntFichas.setText(cantidadFichas + "");
		
		// Cambiar color de la franja superior según el turno
		String miNombre = this.jugador.getText();
		boolean esMiTurno = miNombre.equals(jugadorTurno.getNombre());
		
		Color colorFondo = esMiTurno ? new Color(40, 167, 69) : new Color(220, 53, 69); // Verde si es mi turno, rojo si no
		
		// Actualizar colores de todos los paneles superiores
		if (panelNorte != null) {
			panelNorte.setBackground(colorFondo);
		}
		if (panelJugadorInfo != null) {
			panelJugadorInfo.setBackground(colorFondo);
		}
		if (panelDerecho != null) {
			panelDerecho.setBackground(colorFondo);
		}
		
		// Habilitar/deshabilitar botones según el turno
		if (esMiTurno) {
			// ES MI TURNO - Habilitar todos los botones (solo si el juego no terminó)
			if (!juegoTerminado) {
				if (enviarPalabra != null) {
					enviarPalabra.setEnabled(true);
					enviarPalabra.setText("Formar palabra");
				}
				if (cambioFichas != null) {
					cambioFichas.setEnabled(true);
					cambioFichas.setText("Cambiar y pasar");
				}
				if (pasarTurno != null) {
					pasarTurno.setEnabled(true);
					pasarTurno.setText("Pasar turno");
				}
				if (palabra != null) palabra.setEnabled(true);
				if (coorX != null) coorX.setEnabled(true);
				if (coorY != null) coorY.setEnabled(true);
				if (horizontal != null) horizontal.setEnabled(true);
				if (vertical != null) vertical.setEnabled(true);
				if (tablaAtril != null) tablaAtril.setEnabled(true);
			}
		} else {
			// NO ES MI TURNO - Deshabilitar todos los botones
			if (enviarPalabra != null) {
				enviarPalabra.setEnabled(false);
				enviarPalabra.setText("Espere su turno");
			}
			if (cambioFichas != null) {
				cambioFichas.setEnabled(false);
				cambioFichas.setText("Espere su turno");
			}
			if (pasarTurno != null) {
				pasarTurno.setEnabled(false);
				pasarTurno.setText("Espere su turno");
			}
			if (palabra != null) palabra.setEnabled(false);
			if (coorX != null) coorX.setEnabled(false);
			if (coorY != null) coorY.setEnabled(false);
			if (horizontal != null) horizontal.setEnabled(false);
			if (vertical != null) vertical.setEnabled(false);
			if (tablaAtril != null) tablaAtril.setEnabled(false);
		}
		
		// Forzar repintado
		if (frmScrabble != null) {
			frmScrabble.repaint();
		}
	}
	
	/**
	 * Actualiza solo el atril y puntaje del cliente específico
	 */
	public void mostrarAtrilCliente(IJugador miJugador) {
		this.puntaje.setText(miJugador.getPuntaje() + "");
		this.tablaAtril.setModel(new ModeloAtril(miJugador.getAtril()));
	}

	public JFrame parentComponent() {
		return frmScrabble;
	}

	public void limpiar() {
		palabra.setText("");
		coorX.setText("");
		coorY.setText("");
		// cadenaFichas eliminado - ahora todo usa el campo palabra
	}
	
	/**
	 * Bloquea todos los botones y campos cuando termina la partida
	 */
	public void bloquearBotones() {
		juegoTerminado = true;
		
		// Bloquear botones de acción
		if (enviarPalabra != null) enviarPalabra.setEnabled(false);
		if (cambioFichas != null) cambioFichas.setEnabled(false);
		if (pasarTurno != null) pasarTurno.setEnabled(false);
		
		// Bloquear campos de entrada
		if (palabra != null) palabra.setEnabled(false);
		if (coorX != null) coorX.setEnabled(false);
		if (coorY != null) coorY.setEnabled(false);
		if (horizontal != null) horizontal.setEnabled(false);
		if (vertical != null) vertical.setEnabled(false);
		
		// Bloquear interacción con el atril
		if (tablaAtril != null) tablaAtril.setEnabled(false);
		
		// Cambiar colores para indicar que está bloqueado
		if (enviarPalabra != null) {
			enviarPalabra.setBackground(Color.GRAY);
			enviarPalabra.setText("Partida terminada");
		}
		if (cambioFichas != null) {
			cambioFichas.setBackground(Color.GRAY);
		}
		if (pasarTurno != null) {
			pasarTurno.setBackground(Color.GRAY);
		}
		
		System.out.println("Interfaz bloqueada - Partida terminada");
	}
	
	/**
	 * Muestra un diálogo modal con el mensaje de fin de partida
	 */
	public void mostrarMensajeFinPartida(String mensaje, String ganador) {
		// Evitar múltiples diálogos si ya se mostró uno
		if (mensajeFinMostrado) {
			System.out.println("VentanaTablero: Mensaje de fin ya mostrado, ignorando llamada duplicada");
			return;
		}
		
		mensajeFinMostrado = true;
		
		try {
			// Cambiar franja a amarillo si soy el ganador
			String miNombre = this.jugador.getText();
			if (ganador != null && miNombre.equals(ganador)) {
				Color amarillo = new Color(255, 193, 7); // Amarillo bootstrap
				if (panelNorte != null) panelNorte.setBackground(amarillo);
				if (panelJugadorInfo != null) panelJugadorInfo.setBackground(amarillo);
				if (panelDerecho != null) panelDerecho.setBackground(amarillo);
				frmScrabble.repaint();
				System.out.println("Franja cambiada a amarillo para el ganador: " + miNombre);
			}
			
			// Mostrar diálogo modal con el mensaje completo
			System.out.println("Mostrando diálogo de fin de partida");
			JOptionPane.showMessageDialog(
				frmScrabble, 
				mensaje, 
				"🏆 PARTIDA TERMINADA", 
				JOptionPane.INFORMATION_MESSAGE
			);
			System.out.println("Diálogo cerrado correctamente");
			
		} catch (Exception e) {
			System.err.println("Error al mostrar diálogo de fin de partida: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void cerrar(WindowAdapter accion) {
		frmScrabble.addWindowListener(accion);
	}


	public void desconectar(ActionListener accion) {
		desconectar.addActionListener(accion);
	}
	

	public void configurarTablero() {
		tablero = new JTable();
		tablero.setDropMode(DropMode.INSERT);
		tablero.setDragEnabled(true);
		tablero.setShowGrid(true);
		tablero.setGridColor(Color.BLACK);
		tablero.setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 13));
		tablero.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablero.setRowSelectionAllowed(false);
		// Tamaño adecuado para el tablero 15x15: 15 columnas * 30px = 450px
		tablero.setPreferredSize(new Dimension(500, 500)); // Tablero más grande
		tablero.setRowHeight(30);
		
		// Modelo inicial vacío 15x15 para el juego
		tablero.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"
			}
		));
		
		// Deshabilito la EDICIÓN DIRECTA de las celdas
		tablero.setDefaultEditor(Object.class, null);
        
        // Aplicar el renderer personalizado con colores a todas las columnas de la tabla
        for (int i = 0; i < tablero.getColumnCount(); i++) {
        	tablero.getColumnModel().getColumn(i).setCellRenderer(new ColoredTableCellRenderer());
        	tablero.getColumnModel().getColumn(i).setPreferredWidth(30);
        	tablero.getColumnModel().getColumn(i).setResizable(false);
        } 
        
        // Configurar encabezados
        tablero.getTableHeader().setReorderingAllowed(false);
        tablero.getTableHeader().setResizingAllowed(false);
        
	}

	
	public void configurarAtril() {
		tablaAtril = new JTableAtril();
		tablaAtril.setDragEnabled(true);
		tablaAtril.setDropMode(DropMode.INSERT);
		tablaAtril.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column", "New column", "New column", "New column"
			}
		));
		tablaAtril.setCellSelectionEnabled(true);
		tablaAtril.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaAtril.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		tablaAtril.setRowHeight(32); // Reducido de 52 a 32 para menos espacio en blanco
		tablaAtril.setSize(new Dimension(10, 10));
		tablaAtril.setRowSelectionAllowed(false);
		tablaAtril.setPreferredSize(new Dimension(10, 10));
		
		// Deshabilito la EDICIÓN DIRECTA de las celdas
        tablaAtril.setDefaultEditor(Object.class, null);
        
        // Aplica el renderizador a todas las columnas de la tabla
        for (int i = 0; i < tablaAtril.getColumnCount(); i++) {
        	tablaAtril.getColumnModel().getColumn(i).setCellRenderer(new DraggableCellRenderer());
        }

        // Establecer el renderizador personalizado para las celdas
        tablaAtril.setDefaultRenderer(Object.class, new DraggableCellRenderer());
        
        // Agrego el MouseListener a la tabla - SIMPLIFICADO
        tablaAtril.addMouseListener(new MouseAdapter() {
         
            public void mouseClicked(MouseEvent e) {
            	
            	// VALIDACIÓN: Solo procesar si el atril está habilitado (es mi turno)
            	if (!tablaAtril.isEnabled()) {
            		return; // No hacer nada si no es mi turno
            	}
            	
            	//Obtengo las coordenadas del casillero seleccionado
                int fila = tablaAtril.rowAtPoint(e.getPoint());
                int columna = tablaAtril.columnAtPoint(e.getPoint());
                
                String dato = (String) tablaAtril.getValueAt(fila, columna);
                
                // VALIDACIÓN: Solo procesar si la casilla NO está vacía
                if (dato != null && !dato.trim().isEmpty()) {
                	tablaAtril.addCasillero(dato, fila, columna);
                	
                	// SIMPLIFICADO: Siempre agregar al campo palabra
                	// Sirve tanto para formar palabras como para cambiar fichas
                	palabra.setText(palabra.getText() + dato);
                	
                	tablaAtril.repaint();
                }
                // Si la casilla está vacía, no hacer nada (evita el bug del deshacer)
            }
        });

	}

	//Inicializa la ventana principal.
	private void inicializarVentana() {
		frmScrabble = new JFrame();
		frmScrabble.setResizable(false);
		frmScrabble.setTitle("Scrabble");
		frmScrabble.setBounds(100, 100, 1050, 800); // Ventana más grande para más espacio
		frmScrabble.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmScrabble.getContentPane().setBackground(new Color(240, 240, 240));
		
		JPanel panelNorte = new JPanel();
		panelNorte.setBorder(null);
		panelNorte.setBackground(new Color(220, 53, 69)); // Rojo del diseño
		panelNorte.setPreferredSize(new Dimension(100, 40));
		frmScrabble.getContentPane().add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new BorderLayout());
		
		// Asignar a variable de instancia para cambiar color después
		this.panelNorte = panelNorte;
		
		// Panel izquierdo con información del jugador
		JPanel panelJugadorInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelJugadorInfo.setBackground(new Color(220, 53, 69));
		
		// Asignar a variable de instancia para cambiar color después
		this.panelJugadorInfo = panelJugadorInfo;
		
		JLabel lblJugador = new JLabel("Jugador: ");
		lblJugador.setForeground(Color.WHITE);
		lblJugador.setFont(new Font("Arial", Font.BOLD, 12));
		panelJugadorInfo.add(lblJugador);
		
		jugador = new JLabel("[JUGADOR]");
		jugador.setForeground(Color.WHITE);
		jugador.setFont(new Font("Arial", Font.BOLD, 12));
		panelJugadorInfo.add(jugador);
		
		JLabel lblTurno = new JLabel("   Turno de: ");
		lblTurno.setForeground(Color.WHITE);
		lblTurno.setFont(new Font("Arial", Font.BOLD, 12));
		panelJugadorInfo.add(lblTurno);
		
		turnoDe = new JLabel("[JUGADOR]");
		turnoDe.setForeground(Color.WHITE);
		turnoDe.setFont(new Font("Arial", Font.BOLD, 12));
		panelJugadorInfo.add(turnoDe);
		
		panelNorte.add(panelJugadorInfo, BorderLayout.WEST);
		
		// Panel derecho con botón desconectar
		JPanel panelDesconectar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelDesconectar.setBackground(new Color(220, 53, 69));
		
		// Asignar a variable de instancia para cambiar color después
		this.panelDerecho = panelDesconectar;
		
		desconectar = new JButton("Desconectar");
		desconectar.setFont(new Font("Arial", Font.PLAIN, 11));
		desconectar.setBackground(Color.WHITE);
		desconectar.setForeground(Color.BLACK);
		panelDesconectar.add(desconectar);
		
		panelNorte.add(panelDesconectar, BorderLayout.EAST);
		
		// Panel central para notificaciones (opcional, oculto por defecto)
		panelNotificaciones = new JLabel("");
		panelNotificaciones.setForeground(Color.WHITE);
		panelNotificaciones.setHorizontalAlignment(SwingConstants.CENTER);
		panelNotificaciones.setVisible(false);
		panelNorte.add(panelNotificaciones, BorderLayout.CENTER);
		
		// Eliminar paneles laterales - solo conservar el tablero central
		
		JPanel panelSur = new JPanel();
		panelSur.setBackground(new Color(240, 240, 240)); // Fondo gris claro
		panelSur.setPreferredSize(new Dimension(100, 250)); // Más altura para dar espacio
		frmScrabble.getContentPane().add(panelSur, BorderLayout.SOUTH);
		panelSur.setLayout(new BorderLayout());
		
		// Panel superior con información de puntaje y fichas
		JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelInfo.setBackground(new Color(240, 240, 240));
		panelInfo.setBorder(BorderFactory.createTitledBorder("Informacion de Partida"));
		
		JLabel lblPuntaje = new JLabel("Puntaje: ");
		lblPuntaje.setFont(new Font("Arial", Font.BOLD, 12));
		panelInfo.add(lblPuntaje);
		
		puntaje = new JLabel("[PUNTAJE]");
		puntaje.setFont(new Font("Arial", Font.BOLD, 12));
		puntaje.setForeground(new Color(0, 123, 255)); // Azul
		panelInfo.add(puntaje);
		
		JLabel lblPuntos = new JLabel(" puntos   ");
		lblPuntos.setFont(new Font("Arial", Font.PLAIN, 12));
		panelInfo.add(lblPuntos);
		
		JLabel lblMonton = new JLabel("Monton: ");
		lblMonton.setFont(new Font("Arial", Font.BOLD, 12));
		panelInfo.add(lblMonton);
		
		cntFichas = new JLabel("[CANT_FICHAS]");
		cntFichas.setFont(new Font("Arial", Font.BOLD, 12));
		cntFichas.setForeground(new Color(40, 167, 69)); // Verde
		panelInfo.add(cntFichas);
		
		JLabel lblFichas = new JLabel(" fichas");
		lblFichas.setFont(new Font("Arial", Font.PLAIN, 12));
		panelInfo.add(lblFichas);
		
		panelSur.add(panelInfo, BorderLayout.NORTH);
		
		// Panel central con el atril
		JPanel panelAtrilContainer = new JPanel(new BorderLayout());
		panelAtrilContainer.setBackground(new Color(240, 240, 240));
		panelAtrilContainer.setBorder(BorderFactory.createTitledBorder("Atril"));
		
		configurarAtril();
		panelAtrilContainer.add(tablaAtril, BorderLayout.CENTER);
		panelSur.add(panelAtrilContainer, BorderLayout.CENTER);
		
		// Panel inferior con controles
		JPanel panelControles = new JPanel(new BorderLayout());
		panelControles.setBackground(new Color(240, 240, 240));
		
		// Panel izquierdo - Campo palabra y coordenadas (más organizado)
		JPanel panelIzquierdo = new JPanel(new BorderLayout());
		panelIzquierdo.setBackground(new Color(240, 240, 240));
		
		// Fila superior: Palabra
		JPanel filaPalabra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8)); // Más espacio
		filaPalabra.setBackground(new Color(240, 240, 240));
		
		JLabel lblPalabra = new JLabel("Palabra:");
		filaPalabra.add(lblPalabra);
		
		palabra = new JTextField(12);
		palabra.setEditable(false);
		filaPalabra.add(palabra);
		
		panelIzquierdo.add(filaPalabra, BorderLayout.NORTH);
		
		// Fila inferior: Coordenadas y disposición
		JPanel filaCoordenadas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8)); // Más espacio
		filaCoordenadas.setBackground(new Color(240, 240, 240));
		
		JLabel lblCoorX = new JLabel("Coordenadas X, Y (1-15):");
		filaCoordenadas.add(lblCoorX);
		
		coorX = new JTextField(3);
		coorX.setToolTipText("Solo números del 1 al 15");
		// Agregar validación en tiempo real para coordenada X
		coorX.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				validarCoordenada(coorX);
			}
		});
		filaCoordenadas.add(coorX);
		
		coorY = new JTextField(3);
		coorY.setToolTipText("Solo números del 1 al 15");
		// Agregar validación en tiempo real para coordenada Y
		coorY.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				validarCoordenada(coorY);
			}
		});
		filaCoordenadas.add(coorY);
		
		JLabel lblDisposicion = new JLabel("   Disposición:");
		filaCoordenadas.add(lblDisposicion);
		
		horizontal = new JRadioButton("Horizontal", true); // Seleccionado por defecto
		horizontal.setBackground(new Color(240, 240, 240));
		horizontal.setToolTipText("La palabra se colocará de izquierda a derecha");
		
		vertical = new JRadioButton("Vertical");
		vertical.setBackground(new Color(240, 240, 240));
		vertical.setToolTipText("La palabra se colocará de arriba hacia abajo");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(horizontal);
		buttonGroup.add(vertical);
		
		filaCoordenadas.add(horizontal);
		filaCoordenadas.add(vertical);
		
		panelIzquierdo.add(filaCoordenadas, BorderLayout.SOUTH);
		
		panelControles.add(panelIzquierdo, BorderLayout.WEST);
		
		// Panel derecho - Botones (con más espacio)
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8)); // Más espacio horizontal y vertical
		panelBotones.setBackground(new Color(240, 240, 240));
		
		enviarPalabra = new JButton("Formar palabra");
		enviarPalabra.setBackground(new Color(40, 167, 69)); // Verde
		enviarPalabra.setForeground(Color.WHITE);
		enviarPalabra.setFont(new Font("Arial", Font.BOLD, 11));
		enviarPalabra.setPreferredSize(new Dimension(140, 35)); // Más ancho y alto para que no se corte
		panelBotones.add(enviarPalabra);
		
		// Botón cambiar fichas - usa el mismo campo "Palabra"
		cambioFichas = new JButton("Cambiar y pasar");
		cambioFichas.setBackground(new Color(248, 149, 167)); // Rosa
		cambioFichas.setForeground(Color.WHITE);
		cambioFichas.setFont(new Font("Arial", Font.BOLD, 11));
		cambioFichas.setPreferredSize(new Dimension(130, 35)); // Más alto
		cambioFichas.setToolTipText("Usa las letras del campo 'Palabra' para cambiar fichas");
		panelBotones.add(cambioFichas);
		
		pasarTurno = new JButton("Pasar turno");
		pasarTurno.setBackground(new Color(248, 149, 167)); // Rosa
		pasarTurno.setForeground(Color.WHITE);
		pasarTurno.setFont(new Font("Arial", Font.BOLD, 11));
		pasarTurno.setPreferredSize(new Dimension(110, 35)); // Más alto
		panelBotones.add(pasarTurno);
		
		JButton deshacer = new JButton("Deshacer");
		deshacer.setBackground(new Color(255, 193, 7)); // Amarillo
		deshacer.setForeground(Color.BLACK);
		deshacer.setFont(new Font("Arial", Font.BOLD, 11));
		deshacer.setPreferredSize(new Dimension(90, 35)); // Más alto
		deshacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Restaurar fichas seleccionadas al atril (cada jugador tiene su propio estado)
				tablaAtril.deshacer();
				// Limpiar campos de entrada
				limpiar(); // Limpia palabra, coordenadas Y cadena de fichas
			}
		});
		panelBotones.add(deshacer);
		
		panelControles.add(panelBotones, BorderLayout.EAST);
		
		panelSur.add(panelControles, BorderLayout.SOUTH);
	
		JPanel panelCentro = new JPanel();
		panelCentro.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		panelCentro.setFont(new Font("Courier Prime", Font.BOLD, 12));
		panelCentro.setBackground(new Color(240, 240, 240)); // Fondo gris claro
		frmScrabble.getContentPane().add(panelCentro, BorderLayout.CENTER);
		panelCentro.setLayout(new BorderLayout(0, 0));
		
		configurarTablero();
		
		// Crear JScrollPane para el tablero con etiquetas de fila
		JScrollPane scrollTablero = new JScrollPane(tablero);
		scrollTablero.setBackground(new Color(240, 240, 240));
		
		// Crear etiquetas de fila con números del 1 al 15
		JList<String> etiquetasFilas = new JList<String>(new String[] {
			"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"
		});
		etiquetasFilas.setFixedCellHeight(30); // Mismo alto que las filas del tablero
		etiquetasFilas.setFixedCellWidth(30);
		etiquetasFilas.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setHorizontalAlignment(SwingConstants.CENTER);
				setFont(new Font("JetBrains Mono Medium", Font.PLAIN, 13));
				setBackground(UIManager.getColor("TableHeader.background"));
				setBorder(UIManager.getBorder("TableHeader.cellBorder"));
				return this;
			}
		});
		etiquetasFilas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollTablero.setRowHeaderView(etiquetasFilas);
		
		panelCentro.add(scrollTablero, BorderLayout.CENTER);
	}

	/**
	 * Valida que una coordenada esté en el rango 1-15
	 */
	private void validarCoordenada(JTextField campo) {
		String texto = campo.getText().trim();
		if (!texto.isEmpty()) {
			try {
				int valor = Integer.parseInt(texto);
				if (valor < 1 || valor > 15) {
					campo.setBackground(new Color(255, 235, 235)); // Fondo rojizo
					mostrarMensaje("Error: Las coordenadas deben estar entre 1 y 15");
				} else {
					campo.setBackground(Color.WHITE); // Fondo normal
				}
			} catch (NumberFormatException e) {
				campo.setBackground(new Color(255, 235, 235)); // Fondo rojizo
				mostrarMensaje("Error: Solo se permiten números en las coordenadas");
			}
		} else {
			campo.setBackground(Color.WHITE); // Fondo normal cuando está vacío
		}
	}

	/**
	 * Valida que los campos obligatorios estén completos antes de enviar
	 */
	public boolean validarCamposCompletos() {
		boolean esValido = true;
		StringBuilder errores = new StringBuilder();
		
		if (palabra.getText().trim().isEmpty()) {
			errores.append("- Debe seleccionar fichas para formar una palabra\n");
			esValido = false;
		}
		
		if (coorX.getText().trim().isEmpty() || coorY.getText().trim().isEmpty()) {
			errores.append("- Debe ingresar las coordenadas X e Y\n");
			esValido = false;
		}
		
		if (!horizontal.isSelected() && !vertical.isSelected()) {
			errores.append("- Debe seleccionar una disposición (Horizontal o Vertical)\n");
			esValido = false;
		}
		
		if (!esValido) {
			mostrarMensaje("Error: Faltan datos obligatorios:\n" + errores.toString());
		}
		
		return esValido;
	}

	/**
	 * Renderer personalizado para colorear el FONDO de las celdas del tablero según su tipo
	 */
	private class ColoredTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {
			
			Component component = super.getTableCellRendererComponent(table, value, 
					isSelected, hasFocus, row, column);
			
			// Obtener el contenido de la celda
			String contenido = (value != null) ? value.toString().trim() : "";
			
			// Aplicar colores de FONDO según el tipo de casilla
			if ("PT".equals(contenido)) {
				// Palabra Triple - Fondo rojo suave
				component.setBackground(new Color(255, 200, 200)); // Rojo muy claro
				component.setForeground(Color.BLACK);
			} else if ("PD".equals(contenido)) {
				// 🔵 Palabra Doble - Fondo azul suave
				component.setBackground(new Color(200, 220, 255)); // Azul muy claro
				component.setForeground(Color.BLACK);
			} else if ("LT".equals(contenido)) {
				// 🌸 Letra Triple - Fondo rosa suave
				component.setBackground(new Color(255, 230, 255)); // Rosa muy claro
				component.setForeground(Color.BLACK);
			} else if ("LD".equals(contenido)) {
				// 🟦 Letra Doble - Fondo celeste suave
				component.setBackground(new Color(220, 255, 255)); // Celeste muy claro
				component.setForeground(Color.BLACK);
			} else if (row == 7 && column == 7) {
				// 🟡 Centro del tablero (8,8) - Fondo amarillo suave
				component.setBackground(new Color(255, 255, 200)); // Amarillo muy claro
				component.setForeground(Color.BLACK);
			} else if (contenido.length() == 1 && contenido.matches("[A-Z]")) {
				// Fichas colocadas (letras A-Z) - Fondo verde mas intenso
				component.setBackground(new Color(150, 255, 150)); // Verde más intenso pero suave
				component.setForeground(Color.BLACK);
			} else {
				// ⬜ Casillas normales - Fondo blanco
				component.setBackground(Color.WHITE);
				component.setForeground(Color.BLACK);
			}
			
			// Mantener bordes
			setBorder(BorderFactory.createLineBorder(Color.GRAY));
			
			return component;
		}
	}


	

}
