package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Calibracion;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel implements javax.swing.table.TableModel {
	// Constructor
	public TableModel(int[] cols, List<Calibracion> rows){
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
		Calibracion sucursal = rows.get(row);
		switch (cols[col]){
			case NUMERO: return sucursal.getNumero();
			case FECHA: return sucursal.getFecha();
			case MEDICIONES: return sucursal.getNumeroDeMediciones();
			default: return "";
		}
	}
	public Calibracion getRowAt(int row) {
		return rows.get(row);
	}
	public static final int NUMERO = 0;
	public static final int FECHA = 1;
	public static final int MEDICIONES = 2;
	String[] colNames = new String[6];
	private void initColNames(){
		colNames[NUMERO] = "Numero";
		colNames[FECHA] = "Fecha";
		colNames[MEDICIONES] = "Mediciones";
	}
	List<Calibracion> rows;
	int[] cols;
}