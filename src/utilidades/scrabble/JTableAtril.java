package utilidades.scrabble;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import modelo.scrabble.Casillero;
import javax.swing.*;

public class JTableAtril extends JTable{

	private static final long serialVersionUID = 1L;
    private HashMap<Integer,String> casilleros = new HashMap<>(); // YA NO ES STATIC
	
	public void addCasillero(Object dato, int fila, int columna) {
		
		//Seteo en vacio al casillero actual
        this.setValueAt("", fila, columna); 
        
        //Guardo el dato anterior en el HashMap
        casilleros.put(columna, (String) dato);
        
    }
	
	public void deshacer() {
		int columnas = this.getColumnCount();
		for(int i = 0; i < columnas; i++) {
			String dato = casilleros.get(i);
			if(dato != null) {
				casilleros.remove(i);				
				this.setValueAt(dato, 0, i);
			}
		}
		this.repaint();
    }
	
	
	
	
	

}
