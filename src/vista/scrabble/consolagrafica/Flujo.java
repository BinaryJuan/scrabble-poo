package vista.scrabble.consolagrafica;

import controlador.scrabble.Controlador;

public abstract class Flujo {

	protected final ConsolaGrafica vista;
    protected final Controlador controlador;
    
    public Flujo(ConsolaGrafica vista, Controlador controlador) {
        this.vista = vista;
        this.controlador = controlador;
    }

    public abstract void mostarMenuTextual();
	public abstract Flujo elegirOpcion(String opcion);

    
	
}
