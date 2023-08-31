package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Calibracion;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel {
	public static final int NUMERO = 0;
	public static final int FECHA = 1;
	public static final int MEDICIONES = 2;

	private final int[] cols;
	private final List<Calibracion> rows;
	private final String[] colNames = {"Numero", "Fecha", "Mediciones"};

	public TableModel(int[] cols, List<Calibracion> rows) {
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
		Calibracion calibracion = rows.get(row);
		switch (cols[col]) {
			case NUMERO:
				return calibracion.getNumero();
			case FECHA:
				return calibracion.getFecha();
			case MEDICIONES:
				return calibracion.getNumeroDeMediciones();
			default:
				return "";
		}
	}

	public Calibracion getRowAt(int row) {
		return rows.get(row);
	}
}