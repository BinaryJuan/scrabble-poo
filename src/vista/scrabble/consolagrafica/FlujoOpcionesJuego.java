package vista.scrabble.consolagrafica;

import java.io.IOException;
import java.rmi.RemoteException;
import controlador.scrabble.Controlador;

public class FlujoOpcionesJuego extends Flujo{
	
	public FlujoOpcionesJuego(ConsolaGrafica vista, Controlador controlador) {
		super(vista, controlador);
	}
	
    public void mostarMenuTextual() {
        vista.mostrarMensaje("");
        vista.mostrarMensaje("=== OPCIONES DE TURNO ===");
        vista.mostrarMensaje("Que deseas hacer?:");
        vista.mostrarMensaje("1. Agregar palabra (seguir pasos guiados)");
        vista.mostrarMensaje("2. Cambiar fichas del atril");
        vista.mostrarMensaje("3. Pasar el turno");
        vista.mostrarMensaje("4. Volver al menu principal");
        vista.mostrarMensaje("Seleccione una opcion (1-4): ");
    }
    
   
	public Flujo elegirOpcion(String opcion) {
		if(vista.esTurnoActual()) {
			switch (opcion.trim()) {
            case "1":
            	vista.mostrarMensaje("Iniciando proceso para agregar palabra...");
            	return ingresarPalabra();
            case "2":
            	vista.mostrarMensaje("Iniciando cambio de fichas...");
            	return cambiarFichas();
            case "3":
            	vista.mostrarMensaje("Pasando turno...");
            	return avanzarFlujo();
            case "4":
            	vista.mostrarMensaje("Volviendo al menu principal...");
            	return volverMenuPrincipal();
            default:
            	vista.mostrarMensaje("Opcion invalida. Use 1, 2, 3 o 4.");
            	break;
            }
		} 
		else {
			vista.mostrarMensaje("Espere a que los demas terminen su turno");
		}
        return this;
    }

	//INTERFAZ
	
    public Flujo ingresarPalabra() {
    	return new FlujoIngresarPalabra(vista,controlador);
    }
    
    public Flujo cambiarFichas() {
    	return new FlujoCambiarFichas(vista,controlador);
    }
    
    public Flujo avanzarFlujo() {
    	controlador.pasarTurno();
    	return new FlujoOpcionesJuego(vista,controlador);
    }
    
    public Flujo volverMenuPrincipal() {
    	try {
    		controlador.guardarPartida();
    		return new FlujoMenuPrincipal(vista,controlador);
		} catch (RemoteException e) {
			return null;
		} catch (IOException e) {
			return this;
		}
    }
	
}
