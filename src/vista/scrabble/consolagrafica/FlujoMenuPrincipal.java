package vista.scrabble.consolagrafica;

import java.rmi.RemoteException;

import controlador.scrabble.*;

public class FlujoMenuPrincipal extends Flujo{

	
	public FlujoMenuPrincipal(ConsolaGrafica vista, Controlador controlador) {
		super(vista, controlador);
	}
	
	//Cascara del menu
    public void mostarMenuTextual() {
		vista.mostrarMensaje("SCRABBLE - VISTA CONSOLA");
        vista.mostrarMensaje("Menu Principal:");
        vista.mostrarMensaje("1. Comenzar nueva partida.");
        vista.mostrarMensaje("2. Ver ranking de mejores jugadores.");
        vista.mostrarMensaje("3. Salir.");
        vista.mostrarMensaje("Seleccione una opcion: ");
    }

	//Menu
    public Flujo elegirOpcion(String opcion) {
        switch (opcion) {
            case "1":
            	controlador.comenzarPartida();
            	return new FlujoOpcionesJuego(vista, controlador);
            case "2":
            	return new FlujoVerRanking(vista, controlador);
            case "3":
            	System.exit(0);
            	break;
            default:
            	vista.mostrarMensaje("Opcion invalida. Use 1, 2 o 3.");
            	break;
        }
        return this;
    }
    
    

	
	
	

}
