package modelo.scrabble;

import java.io.Serializable;

public class PremioLetra implements Casillero, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String letra = "L";
	private TipoPuntaje tipoPuntaje;
	
	//CONSTRUCTOR
	public PremioLetra(TipoPuntaje tipoPuntaje) {
		this.tipoPuntaje = tipoPuntaje;
		if(tipoPuntaje == TipoPuntaje.DOBLE) {
			letra += "D";
			}
		else {
			letra += "T";
		}
			
		
	}
	
	//INTERFAZ
	
	public String getDescripcion() {
		return letra;
	}
	
	public int getPuntos() {
		int premio;
		if(tipoPuntaje == TipoPuntaje.DOBLE) {
			premio = 2;
		}
		else {
			premio = 3;
		}
		return premio;
	}

	public void setDescripcion(String descripcion) {
		this.letra = descripcion;
	}
	
}