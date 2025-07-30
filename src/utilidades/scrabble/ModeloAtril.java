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
    	// Asegurar que siempre haya al menos 7 columnas para evitar errores
    	int numColumnas = Math.max(7, datos.size());
    	this.datos = new Object[1][numColumnas];
    	this.columnas = new String[numColumnas];
    	
    	// Inicializar nombres de columnas
    	for(int i = 0; i < numColumnas; i++) {
    		this.columnas[i] = "Ficha " + (i + 1);
    	}
    	
    	// Llenar con las letras del atril
    	for(Letra l: datos) {
    		if(c < numColumnas) {
    			this.datos[0][c] = l;
    			c++;
    		}
    	}
    	
    	// Llenar espacios vacíos con null (se manejará en getValueAt)
    	for(int i = c; i < numColumnas; i++) {
    		this.datos[0][i] = null;
    	}
    }

    public int getRowCount() {
        return datos.length;
    }

    public int getColumnCount() {
        return columnas.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
    	// Validar índices
    	if(rowIndex < 0 || rowIndex >= datos.length || 
    	   columnIndex < 0 || columnIndex >= datos[0].length) {
    		return ""; // Retornar cadena vacía para índices inválidos
    	}
    	
    	Object objeto = datos[0][columnIndex];
    	
    	// Si no hay letra en esta posición, mostrar vacío
    	if(objeto == null) {
    		return "";
    	}

        Casillero objetoLetra = (Casillero) objeto;
        return objetoLetra.getDescripcion();
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	// Validar índices antes de proceder
    	if(rowIndex < 0 || rowIndex >= datos.length || 
    	   columnIndex < 0 || columnIndex >= datos[0].length) {
    		return; // No hacer nada si los índices son inválidos
    	}
    	
    	Object objeto = datos[0][columnIndex];
    	if(objeto != null && aValue instanceof String) {
    		Casillero objetoLetra = (Casillero) objeto;
    		objetoLetra.setDescripcion((String)aValue);
    		fireTableCellUpdated(rowIndex, columnIndex);
    	}
    }
    
    

	
    
}
