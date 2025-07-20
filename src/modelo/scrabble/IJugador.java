package modelo.scrabble;

import java.io.Serializable;
import java.util.List;

public interface IJugador extends Serializable{

	int getId();

	String getNombre();

	void setNombre(String nombre);

	int getPuntaje();

	void setPuntaje(int puntaje);

	List<Letra> getAtril();

	void setAtril(List<Letra> atril);
	
	void setConectado(boolean conectado);
		
	boolean isConectado();
		

}