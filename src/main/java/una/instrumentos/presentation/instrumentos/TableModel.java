package una.instrumentos.presentation.instrumentos;

import una.instrumentos.logic.Instrumento;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel implements javax.swing.table.TableModel {
	// Constructor
	public TableModel(int[] cols, List<Instrumento> rows){
		this.cols=cols;
		this.rows=rows;
		initColNames();
	}
	// Metodos
	@Override
	public int getColumnCount() {
		return cols.length;
	}
	@Override
	public String getColumnName(int col){
		return colNames[cols[col]];
	}
	@Override
	public Class<?> getColumnClass(int col){
		switch (cols[col]){
			default: return super.getColumnClass(col);
		}
	}
	@Override
	public int getRowCount() {
		return rows.size();
	}
	@Override
	public Object getValueAt(int row, int col) {
		Instrumento sucursal = rows.get(row);
		switch (cols[col]){
			case SERIE: return sucursal.getSerie();
			case DESCRIPCION: return sucursal.getDescripcion();
			case MINIMO: return sucursal.getMinimo();
			case MAXIMO: return sucursal.getMaximo();
			case TOLERANCIA: return sucursal.getTolerancia();
			case TIPO: return sucursal.getTipo();
			default: return "";
		}
	}
	public Instrumento getRowAt(int row) {
		return rows.get(row);
	}
	public static final int SERIE = 0;
	public static final int DESCRIPCION = 1;
	public static final int MINIMO = 2;
	public static final int MAXIMO = 3;
	public static final int TOLERANCIA = 4;
	public static final int TIPO = 5;
	String[] colNames = new String[6];
	private void initColNames(){
		colNames[SERIE] = "No. Serie";
		colNames[DESCRIPCION] = "Descripcion";
		colNames[MINIMO] = "Minimo";
		colNames[MAXIMO] = "Maximo";
		colNames[TOLERANCIA] = "Tolerancia";
		colNames[TIPO] = "Tipo";
	}
	List<Instrumento> rows;
	int[] cols;
}