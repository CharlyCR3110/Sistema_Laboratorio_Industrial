package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Medicion;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MedicionesTableModel extends AbstractTableModel {
	public static final int NUMERO = 0;
	public static final int REFERENCIA = 1;
	public static final int MEDICION = 2;
	private final int[] cols;
	private final List<Medicion> rows;
	private List<Boolean> editableCols;
	private final String[] colNames = {"Numero", "Referencia", "Lectura"};

	public MedicionesTableModel(int[] cols, List<Medicion> rows, List<Boolean> editableCols) {
		this.cols = cols;
		this.rows = rows;
		this.editableCols = editableCols;
	}
	@Override
	public boolean isCellEditable(int row, int column) {
		return editableCols.get(column);
	}
	@Override
	public void setValueAt(Object value, int row, int col) {
		Medicion medicion = rows.get(row);
		switch (cols[col]) {
			case REFERENCIA:
				medicion.setReferencia((int) value);
				break;
			case MEDICION:
				medicion.setMedicion((int) value);
				break;
		}
		fireTableCellUpdated(row, col);
	}
	@Override
	public int getColumnCount() {
		return cols.length;
	}

	@Override
	public String getColumnName(int col) {
		return colNames[cols[col]];
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Medicion medicion = rows.get(row);
		switch (cols[col]) {
			case NUMERO:
				return medicion.getNumero();
			case REFERENCIA:
				return medicion.getReferencia();
			case MEDICION:
				return medicion.getMedicion();
			default:
				return "";
		}
	}

	public Medicion getRowAt(int row) {
		return rows.get(row);
	}
}
