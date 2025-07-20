package servidor.scrabble;

import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.scrabble.*;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

public class AppServidor {
	
	// Constantes de configuracion
	private static final int PUERTO_DEFAULT = 8888;
	private static final String TITULO_IP = "IP del servidor";
	private static final String TITULO_PUERTO = "Puerto del servidor";
	private static final String MENSAJE_IP = "Seleccione la IP en la que escuchara peticiones el servidor";
	private static final String MENSAJE_PUERTO = "Seleccione el puerto en el que escuchara peticiones el servidor";

	public static void main(String[] args) {
		ArrayList<String> ips = Util.getIpDisponibles();
		String ip = (String) JOptionPane.showInputDialog(
				null, 
				MENSAJE_IP, TITULO_IP, 
				JOptionPane.QUESTION_MESSAGE, 
				null,
				ips.toArray(),
				null
		);
		String port = (String) JOptionPane.showInputDialog(
				null, 
				MENSAJE_PUERTO, TITULO_PUERTO, 
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				PUERTO_DEFAULT
		);
		
		// Validaciones de entrada
		if (ip == null || ip.trim().isEmpty()) {
			System.err.println("Error: Debe seleccionar una IP valida");
			System.exit(1);
		}
		
		if (port == null || port.trim().isEmpty()) {
			port = String.valueOf(PUERTO_DEFAULT);
		}
		
		int puertoNumerico;
		try {
			puertoNumerico = Integer.parseInt(port);
			if (puertoNumerico < 1024 || puertoNumerico > 65535) {
				throw new NumberFormatException("Puerto fuera de rango valido");
			}
		} catch (NumberFormatException e) {
			System.err.println("Error: Puerto invalido. Usando puerto por defecto: " + PUERTO_DEFAULT);
			puertoNumerico = PUERTO_DEFAULT;
		}
		
		try {
			ModeloJuego modelo = new ModeloJuego();
			Servidor servidor = new Servidor(ip, puertoNumerico);
			servidor.iniciar(modelo);
			System.out.println("Servidor iniciado correctamente en " + ip + ":" + port);
			System.out.println("Esperando conexiones de clientes...");
		} catch (RemoteException e) {
			System.err.println("Error de RMI al iniciar el servidor: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (RMIMVCException e) {
			System.err.println("Error del framework RMI-MVC: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			System.err.println("Error inesperado: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

}
