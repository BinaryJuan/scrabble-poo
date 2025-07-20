package vista.scrabble;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;

public class VentanaPrincipal implements Ventana {

	private JFrame frmScrabble;
	private JMenuItem iNuevaPartida;
	private JMenuItem iRanking; 
	
	//CONSTRUCTOR
	public VentanaPrincipal() {
		inicializarVentana();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private void inicializarVentana() {

		frmScrabble = new JFrame();
		frmScrabble.setResizable(false);
		frmScrabble.setTitle("Scrabble");
		frmScrabble.setBounds(100, 100, 517, 346);
		frmScrabble.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmScrabble.getContentPane().setLayout(new GridLayout(4, 1, 0, 0));
		
		JPanel panel = new JPanel();
		frmScrabble.getContentPane().add(panel);
		
		JPanel panel_1 = new JPanel();
		frmScrabble.getContentPane().add(panel_1);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel = new JLabel("SCRABBLE");
		panel_1.add(lblNewLabel);
		
		JPanel panel_2 = new JPanel();
		frmScrabble.getContentPane().add(panel_2);
		
		JPanel panel_3 = new JPanel();
		frmScrabble.getContentPane().add(panel_3);
		
		JMenuBar menuBar = new JMenuBar();
		frmScrabble.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Jugar a Scrabble");
		menuBar.add(mnNewMenu);
		
		iNuevaPartida = new JMenuItem("Nueva partida");
		mnNewMenu.add(iNuevaPartida);
		
		iRanking = new JMenuItem("Ver ranking");
		mnNewMenu.add(iRanking);
	}

	
	public void setVisible(boolean b) {
		frmScrabble.setVisible(b);
	}
	
	
	public void nuevaPartida(ActionListener accion) {
		iNuevaPartida.addActionListener(accion);
	}
	
	public JFrame parentComponent() {
		return frmScrabble;
	}

	public void verRanking(ActionListener accion) {
		iRanking.addActionListener(accion);
	}

	



	
}
