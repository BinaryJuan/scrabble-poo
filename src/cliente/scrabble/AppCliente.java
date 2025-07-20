package cliente.scrabble;

import java.rmi.RemoteException;
import java.util.ArrayList;
import vista.scrabble.*;
import vista.scrabble.consolagrafica.ConsolaGrafica;
import javax.swing.JOptionPane;
import controlador.scrabble.*;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

public class AppCliente {

	public static void main(String[] args) {
		try {
			// Obtener IP del cliente
			ArrayList<String> ips = Util.getIpDisponibles();
			String ip = (String) JOptionPane.showInputDialog(
					null, 
					"Seleccione la IP en la que escuchara peticiones el cliente", "IP del cliente - Vista Consola", 
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
						"Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente - Vista Consola", 
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
						"Puerto inválido. Debe ser un número entre 1024 y 65535.", 
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
					System.out.println("Operación cancelada por el usuario.");
					return;
				}
				
				if (!esIpValida(ipServidor)) {
					JOptionPane.showMessageDialog(null, 
						"IP inválida. Use formato como 'localhost', '127.0.0.1' o '192.168.1.100'", 
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
					System.out.println("Operación cancelada por el usuario.");
					return;
				}
				
				if (!esPortoValido(portServidor)) {
					JOptionPane.showMessageDialog(null, 
						"Puerto inválido. Debe ser un número entre 1024 y 65535.", 
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
						"¿Cómo se llamará el jugador?", "Nombre del jugador - Vista Consola", 
						JOptionPane.QUESTION_MESSAGE,
						null,
						null,
						"JugadorConsola"
				);
				
				if (nombreJugador == null) {
					System.out.println("Operación cancelada por el usuario.");
					return;
				}
				
				if (!esNombreValido(nombreJugador)) {
					JOptionPane.showMessageDialog(null, 
						"Nombre inválido. Use solo letras, números, espacios, guiones y guiones bajos (mínimo 2 caracteres).", 
						"Error", 
						JOptionPane.ERROR_MESSAGE);
					nombreJugador = null;
				}
			}
			
			// Crear e iniciar el cliente con vista de consola
			Controlador controlador = new Controlador();
			Vista vista = new ConsolaGrafica(controlador, nombreJugador);
			Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
			
			System.out.println("=== CLIENTE SCRABBLE - VISTA CONSOLA ===");
			System.out.println("Conectando cliente...");
			System.out.println("IP Cliente: " + ip + ":" + port);
			System.out.println("IP Servidor: " + ipServidor + ":" + portServidor);
			System.out.println("Jugador: " + nombreJugador);
			System.out.println("Modo: Vista de Consola");
			System.out.println("======================================");
			
			c.iniciar(controlador);
			vista.iniciar();
			
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, 
				"Error de conexión remota: " + e.getMessage(), 
				"Error RMI - Vista Consola", 
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (RMIMVCException e) {
			JOptionPane.showMessageDialog(null, 
				"Error en el framework RMI-MVC: " + e.getMessage(), 
				"Error Framework - Vista Consola", 
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, 
				"Error en formato de puerto: " + e.getMessage(), 
				"Error - Vista Consola", 
				JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, 
				"Error inesperado: " + e.getMessage(), 
				"Error - Vista Consola", 
				JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	/**
	 * Valida si una IP es válida (localhost o formato IPv4)
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
	 * Valida si un puerto es válido (entre 1024 y 65535)
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
	 * Valida si un nombre de jugador es válido
	 */
	private static boolean esNombreValido(String nombre) {
		if (nombre == null || nombre.trim().isEmpty()) {
			return false;
		}
		
		nombre = nombre.trim();
		
		// Mínimo 2 caracteres, máximo 20
		if (nombre.length() < 2 || nombre.length() > 20) {
			return false;
		}
		
		// Solo letras, números, espacios y algunos caracteres especiales
		return nombre.matches("[a-zA-Z0-9\\s_-]+");
	}

}
