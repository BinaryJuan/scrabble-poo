package cliente.scrabble;

import java.rmi.RemoteException;
import java.util.ArrayList;
import vista.scrabble.*;
import javax.swing.JOptionPane;
import controlador.scrabble.*;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

public class AppClienteVistaGrafica {

	public static void main(String[] ar1gs) {
		try {
			// Obtener IP del cliente
			ArrayList<String> ips = Util.getIpDisponibles();
			String ip = (String) JOptionPane.showInputDialog(
					null, 
					"Seleccione la IP en la que escuchara peticiones el cliente", "IP del cliente", 
					JOptionPane.QUESTION_MESSAGE, 
					null,
					ips.toArray(),
					null
			);
			
			if (ip == null) {
				System.out.println("Operacion cancelada por el usuario.");
				return;
			}
			
			// Obtener puerto del cliente
			String port = null;
			while (port == null) {
				port = (String) JOptionPane.showInputDialog(
						null, 
						"Seleccione el puerto en el que escuchara peticiones el cliente", "Puerto del cliente", 
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						9999
				);
				
				if (port == null) {
					System.out.println("Operacion cancelada por el usuario.");
					return;
				}
				
				if (!esPortoValido(port)) {
					JOptionPane.showMessageDialog(null, 
						"Puerto invalido. Debe ser un numero entre 1024 y 65535.", 
						"Error", 
						JOptionPane.ERROR_MESSAGE);
					port = null;
				}
			}
			
			// Obtener IP del servidor
			String ipServidor = null;
			while (ipServidor == null) {
				ipServidor = (String) JOptionPane.showInputDialog(
						null, 
						"Ingrese la IP donde corre el servidor (ej: localhost, 127.0.0.1, 192.168.1.100)", "IP del servidor", 
						JOptionPane.QUESTION_MESSAGE, 
						null,
						null,
						"127.0.0.1"
				);
				
				if (ipServidor == null) {
					System.out.println("Operacion cancelada por el usuario.");
					return;
				}
				
				if (!esIpValida(ipServidor)) {
					JOptionPane.showMessageDialog(null, 
						"IP invalida. Use formato como 'localhost', '127.0.0.1' o '192.168.1.100'", 
						"Error", 
						JOptionPane.ERROR_MESSAGE);
					ipServidor = null;
				}
			}
			
			// Obtener puerto del servidor
			String portServidor = null;
			while (portServidor == null) {
				portServidor = (String) JOptionPane.showInputDialog(
						null, 
						"Seleccione el puerto en el que corre el servidor", "Puerto del servidor", 
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						8888
				);
				
				if (portServidor == null) {
					System.out.println("Operacion cancelada por el usuario.");
					return;
				}
				
				if (!esPortoValido(portServidor)) {
					JOptionPane.showMessageDialog(null, 
						"Puerto invalido. Debe ser un numero entre 1024 y 65535.", 
						"Error", 
						JOptionPane.ERROR_MESSAGE);
					portServidor = null;
				}
			}
			
			// Obtener nombre del jugador
			String nombreJugador = null;
			while (nombreJugador == null) {
				nombreJugador = (String) JOptionPane.showInputDialog(
						null, 
						"Como se llamara el jugador?", "Nombre del jugador", 
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						"Jugador1"
				);
				
				if (nombreJugador == null) {
					System.out.println("Operacion cancelada por el usuario.");
					return;
				}
				
				if (!esNombreValido(nombreJugador)) {
					JOptionPane.showMessageDialog(null, 
						"Nombre invalido. Use solo letras, numeros, espacios, guiones y guiones bajos (minimo 2 caracteres).", 
						"Error", 
						JOptionPane.ERROR_MESSAGE);
					nombreJugador = null;
				}
			}
			
			// Crear e iniciar el cliente
			Controlador controlador = new Controlador();
			Vista vista = new VistaGrafica(controlador, nombreJugador);
			Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
			
			System.out.println("Conectando cliente...");
			System.out.println("IP Cliente: " + ip + ":" + port);
			System.out.println("IP Servidor: " + ipServidor + ":" + portServidor);
			System.out.println("Jugador: " + nombreJugador);
			
			c.iniciar(controlador);
			vista.iniciar();
			
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, 
				"Error de conexion remota: " + e.getMessage(), 
				"Error RMI", 
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (RMIMVCException e) {
			JOptionPane.showMessageDialog(null, 
				"Error en el framework RMI-MVC: " + e.getMessage(), 
				"Error Framework", 
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, 
				"Error en formato de puerto: " + e.getMessage(), 
				"Error", 
				JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, 
				"Error inesperado: " + e.getMessage(), 
				"Error", 
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	/**
	 * Valida si una IP es valida (localhost o formato IPv4)
	 */
	private static boolean esIpValida(String ip) {
		if (ip == null || ip.trim().isEmpty()) {
			return false;
		}
		
		ip = ip.trim();
		
		// Permitir localhost
		if ("localhost".equalsIgnoreCase(ip)) {
			return true;
		}
		
		// Validar formato IPv4
		String[] partes = ip.split("\\.");
		if (partes.length != 4) {
			return false;
		}
		
		try {
			for (String parte : partes) {
				int num = Integer.parseInt(parte);
				if (num < 0 || num > 255) {
					return false;
				}
			}
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Valida si un puerto es valido (entre 1024 y 65535)
	 */
	private static boolean esPortoValido(String port) {
		if (port == null || port.trim().isEmpty()) {
			return false;
		}
		
		try {
			int puerto = Integer.parseInt(port.trim());
			return puerto >= 1024 && puerto <= 65535;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Valida si un nombre de jugador es valido
	 */
	private static boolean esNombreValido(String nombre) {
		if (nombre == null || nombre.trim().isEmpty()) {
			return false;
		}
		
		nombre = nombre.trim();
		
		// Minimo 2 caracteres, maximo 20
		if (nombre.length() < 2 || nombre.length() > 20) {
			return false;
		}
		
		// Solo letras, numeros, espacios y algunos caracteres especiales
		return nombre.matches("[a-zA-Z0-9\\s_-]+");
	}

}
