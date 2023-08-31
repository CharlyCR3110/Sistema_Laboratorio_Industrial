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
	private final String[] colNames = {"Numero", "Referencia", "Lectura"};

	public MedicionesTableModel(int[] cols, List<Medicion> rows) {
		this.cols = cols;
		this.rows = rows;
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
