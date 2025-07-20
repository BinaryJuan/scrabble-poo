package utilidades.scrabble;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.scrabble.Casillero;
import modelo.scrabble.Letra;

public class ModeloAtril extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private Object[][] datos; 
    private String[] columnas;

    public ModeloAtril(List<Letra> datos) {
    	int c = 0;
    	this.datos = new Object[1][datos.size()];
    	this.columnas = new String[datos.size()];
    	for(Letra l: datos) {
    		this.datos[0][c] = l;
    		c++;
    	}
    }

    public int getRowCount() {
        return datos.length;
    }

    public int getColumnCount() {
        return columnas.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
    	
        Casillero objetoLetra = (Casillero) datos[0][columnIndex];

        // Dependiendo de la columna, devuelve el valor del atributo correspondiente
        if(columnIndex >= 0 && columnIndex <= this.datos[0].length) {
        	return objetoLetra.getDescripcion();
        }
        else {
        	return new Object();
        }
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	super.setValueAt(columnas, rowIndex, columnIndex);
        Casillero objetoLetra = (Casillero) datos[0][columnIndex];
        if(aValue.getClass() == String.class) {
        	objetoLetra.setDescripcion((String)aValue);        	
        }
    }
    
    

	
    
}
