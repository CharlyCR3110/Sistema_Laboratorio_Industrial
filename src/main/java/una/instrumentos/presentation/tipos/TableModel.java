package una.instrumentos.presentation.tipos;

import una.instrumentos.logic.TipoInstrumento;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel {
	public static final int CODIGO = 0;
	public static final int NOMBRE = 1;
	public static final int UNIDAD = 2;

	private final int[] cols;
	private final List<TipoInstrumento> rows;
	private final String[] colNames = {"Codigo", "Nombre", "Unidad"};

	public TableModel(int[] cols, List<TipoInstrumento> rows) {
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
		TipoInstrumento tipoInstrumento = rows.get(row);
		switch (cols[col]) {
			case CODIGO:
				return tipoInstrumento.getCodigo();
			case NOMBRE:
				return tipoInstrumento.getNombre();
			case UNIDAD:
				return tipoInstrumento.getUnidad();
			default:
				return "";
		}
	}

	public TipoInstrumento getRowAt(int row) {
		return rows.get(row);
	}
}