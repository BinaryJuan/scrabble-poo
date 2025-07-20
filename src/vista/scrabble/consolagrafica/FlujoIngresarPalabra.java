package vista.scrabble.consolagrafica;

import java.io.IOException;
import java.rmi.RemoteException;

import controlador.scrabble.Controlador;
import modelo.scrabble.Diccionario;
import modelo.scrabble.IJugador;
import modelo.scrabble.Casillero;
import modelo.scrabble.PremioLetra;

public class FlujoIngresarPalabra extends Flujo{
	
	public FlujoIngresarPalabra(ConsolaGrafica vista, Controlador controlador) {
		super(vista, controlador);
	}
	
	private String cadenaString = "";
	private String x = "";
	private String y = "";
	private EstadosPosibles estadoActual = EstadosPosibles.INGRESANDO_PALABRA;
	
	public enum EstadosPosibles{
		INGRESANDO_PALABRA,
		INGRESANDO_COORDENADA_X,
		INGRESANDO_COORDENADA_Y,
		INGRESANDO_DISPOSICION
	}

	public void mostarMenuTextual() {
		switch(estadoActual) {
		case INGRESANDO_PALABRA:
			vista.mostrarMensaje("=== AGREGAR PALABRA ===");
			vista.mostrarMensaje("Paso 1/4: Ingrese la palabra que desea colocar:");
			vista.mostrarMensaje("(Solo letras, sin espacios ni numeros)");
			break;
		case INGRESANDO_COORDENADA_X:
			vista.mostrarMensaje("Paso 2/4: Ingrese la coordenada HORIZONTAL (X):");
			vista.mostrarMensaje("(Numeros del 1 al 15, donde 1=izquierda, 15=derecha)");
			vista.mostrarMensaje("Palabra: " + cadenaString);
			break;
		case INGRESANDO_COORDENADA_Y:
			vista.mostrarMensaje("Paso 3/4: Ingrese la coordenada VERTICAL (Y):");
			vista.mostrarMensaje("(Numeros del 1 al 15, donde 1=arriba, 15=abajo)");
			vista.mostrarMensaje("Palabra: " + cadenaString + " | X: " + x);
			break;
		case INGRESANDO_DISPOSICION:
			vista.mostrarMensaje("Paso 4/4: Como quiere colocar la palabra?:");
			vista.mostrarMensaje("1. Horizontal (de izquierda a derecha)");
			vista.mostrarMensaje("2. Vertical (de arriba hacia abajo)");
			vista.mostrarMensaje("Palabra: " + cadenaString + " | Posicion: (" + x + "," + y + ")");
			break;
		}
	}

	public Flujo elegirOpcion(String opcion) {

		switch(estadoActual) {
			case INGRESANDO_PALABRA:
				try {
					return ingresarPalabra(opcion);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case INGRESANDO_COORDENADA_X:
				return ingresarX(opcion);
			case INGRESANDO_COORDENADA_Y:
				return ingresarY(opcion);
			case INGRESANDO_DISPOSICION:
				return ingresarDisposicion(opcion);
		}
			return this;
	}
	
	//INTERFAZ
	
	public Flujo ingresarPalabra(String cadenaString) throws IOException {
		cadenaString = cadenaString.trim().toUpperCase();
		
		// Validar que solo contenga letras
		if (!cadenaString.matches("[A-Z]+")) {
			vista.mostrarMensaje("ERROR: Solo se permiten letras (sin numeros, espacios o simbolos)");
			vista.mostrarMensaje("Intenta nuevamente:");
			return this;
		}
		
		// Validar longitud minima
		if (cadenaString.length() < 2) {
			vista.mostrarMensaje("ERROR: La palabra debe tener al menos 2 letras");
			vista.mostrarMensaje("Intenta nuevamente:");
			return this;
		}
		
		this.cadenaString = cadenaString;
		vista.mostrarMensaje("Palabra aceptada: " + cadenaString);
		
		// SIEMPRE pedir coordenadas - no saltarse pasos
		estadoActual = EstadosPosibles.INGRESANDO_COORDENADA_X;
		return this;
	}
	
	public Flujo ingresarX(String x) {
		x = x.trim();
		
		// Validar que sea un numero
		try {
			int coordX = Integer.parseInt(x);
			if (coordX < 1 || coordX > 15) {
				vista.mostrarMensaje("ERROR: La coordenada X debe estar entre 1 y 15");
				vista.mostrarMensaje("Intenta nuevamente:");
				return this;
			}
		} catch (NumberFormatException e) {
			vista.mostrarMensaje("ERROR: Debes ingresar un numero valido");
			vista.mostrarMensaje("Intenta nuevamente:");
			return this;
		}
		
		this.x = x;
		vista.mostrarMensaje("Coordenada X aceptada: " + x);
		estadoActual = EstadosPosibles.INGRESANDO_COORDENADA_Y;
		return this;
	}
	
	public Flujo ingresarY(String y) {
		y = y.trim();
		
		// Validar que sea un numero
		try {
			int coordY = Integer.parseInt(y);
			if (coordY < 1 || coordY > 15) {
				vista.mostrarMensaje("ERROR: La coordenada Y debe estar entre 1 y 15");
				vista.mostrarMensaje("Intenta nuevamente:");
				return this;
			}
		} catch (NumberFormatException e) {
			vista.mostrarMensaje("ERROR: Debes ingresar un numero valido");
			vista.mostrarMensaje("Intenta nuevamente:");
			return this;
		}
		
		this.y = y;
		vista.mostrarMensaje("Coordenada Y aceptada: " + y);
		estadoActual = EstadosPosibles.INGRESANDO_DISPOSICION;
		return this;
	}
	
	public Flujo ingresarDisposicion(String disposicion) {
		disposicion = disposicion.trim();
		
		if (!disposicion.equals("1") && !disposicion.equals("2")) {
			vista.mostrarMensaje("ERROR: Debes elegir 1 (Horizontal) o 2 (Vertical)");
			vista.mostrarMensaje("Intenta nuevamente:");
			return this;
		}
		
		String direccion = disposicion.equals("1") ? "Horizontal" : "Vertical";
		vista.mostrarMensaje("Direccion aceptada: " + direccion);
		vista.mostrarMensaje("");
		vista.mostrarMensaje("Intentando colocar: " + cadenaString + " en (" + x + "," + y + ") " + direccion);
		
		if(!controlador.agregarPalabra(x,y,cadenaString,disposicion)) {
			vista.mostrarMensaje("No se pudo agregar la palabra. Intenta con otra palabra o posicion.");
			vista.mostrarMensaje("");
			return new FlujoIngresarPalabra(vista,controlador);
		}
		
		vista.mostrarMensaje("Palabra agregada exitosamente!");
		return new FlujoOpcionesJuego(vista,controlador);
	}
	
	
	

}
