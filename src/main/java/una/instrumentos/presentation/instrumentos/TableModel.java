package una.instrumentos.presentation.instrumentos;

import una.instrumentos.logic.Instrumento;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel {
	public static final int SERIE = 0;
	public static final int DESCRIPCION = 1;
	public static final int MINIMO = 2;
	public static final int MAXIMO = 3;
	public static final int TOLERANCIA = 4;
	public static final int TIPO = 5;

	private final int[] cols;
	private final List<Instrumento> rows;
	private final String[] colNames = {"No. Serie", "Descripcion", "Minimo", "Maximo", "Tolerancia", "Tipo"};

	public TableModel(int[] cols, List<Instrumento> rows) {
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
		Instrumento instrumento = rows.get(row);
		switch (cols[col]) {
			case SERIE:
				return instrumento.getSerie();
			case DESCRIPCION:
				return instrumento.getDescripcion();
			case MINIMO:
				return instrumento.getMinimo();
			case MAXIMO:
				return instrumento.getMaximo();
			case TOLERANCIA:
				return instrumento.getTolerancia();
			case TIPO:
				return instrumento.getTipo().getNombre();
			default:
				return "";
		}
	}

	public Instrumento getRowAt(int row) {
		return rows.get(row);
	}
}
