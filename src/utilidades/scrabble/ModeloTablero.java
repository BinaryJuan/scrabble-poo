package utilidades.scrabble;

import javax.swing.table.DefaultTableModel;
import modelo.scrabble.Casillero;
import modelo.scrabble.Letra;

public class ModeloTablero extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;

	// Constructor que toma una matriz de instancias de Casillero - COMO ANTES
    public ModeloTablero(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    public Object getValueAt(int row, int column) {
    	
    	Object rawValue = super.getValueAt(row, column);
    	
    	// Si no hay casillero o es null, mostrar casillero vacío
    	if (rawValue == null || !(rawValue instanceof Casillero)) {
    		return "__"; // Casillero vacío estándar
    	}
    	
    	Casillero objetoCasillero = (Casillero) rawValue;

        // Mostrar la descripción del casillero - COMO ANTES
        return objetoCasillero.getDescripcion();
    }
    
   
    
}
