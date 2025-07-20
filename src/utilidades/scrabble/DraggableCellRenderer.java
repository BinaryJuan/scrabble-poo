package utilidades.scrabble;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DraggableCellRenderer extends DefaultTableCellRenderer{
	
	private static final long serialVersionUID = 1L;
	
	public DraggableCellRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
    }

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(JLabel.CENTER);
        return component;
    }

}
