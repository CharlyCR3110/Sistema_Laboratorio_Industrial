package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.utiles.Utiles;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
	private JPanel panel;
	private JTextField searchNumero;
	private JButton search;
	private JButton save;
	private JTable list;
	private JButton delete;
	private JLabel searchNumeroLbl;
	private JTextField numero;
	private JTextField mediciones;
	private JTextField fecha;
	private JLabel numeroLbl;
	private JLabel medicionesLbl;
	private JLabel fechaLlb;
	private JButton clear;
	private JButton edit;
	private JButton report;
	private JLabel instrumentoLbl;

	private Controller controller;
	private Model model;
	private Instrumento instrumentoSeleccionado;

	public View() {
		list.getTableHeader().setReorderingAllowed(false);

		search.addActionListener(e -> searchAction());
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleListClick();
			}
		});
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clearAction();
			}
		});
		save.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				saveAction();
			}
		});
		delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				deleteAction();
			}
		});
		edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				editAction();
			}
		});
	}

	private void editAction() {
		try {
			Calibracion calibracion = model.getCurrent();
			controller.edit(calibracion);
			clearAction();
		} catch (Exception ex) {
			showError(ex.getMessage());
		}
	}

	private void deleteAction() {
		try {
			Calibracion calibracion = model.getCurrent();
			controller.delete(calibracion);
			clearAction();
		} catch (Exception ex) {
			showError(ex.getMessage());
		}
	}

	private void saveAction() {
		try {
			Calibracion calibracion = new Calibracion();
			calibracion.setNumero(numero.getText());
			calibracion.setNumeroDeMediciones(Integer.valueOf(mediciones.getText()));
			calibracion.setFecha(Utiles.parseDate(fecha.getText()));

			if (instrumentoSeleccionado == null) {
				showError("Debe seleccionar un instrumento");
				return;
			}

			controller.save(calibracion, instrumentoSeleccionado);
			clearAction();
		} catch (Exception ex) {
			showError("El formato de la fecha no es válido");
		}
	}

	private void clearAction() {
		numero.setText("");
		mediciones.setText("0");
		fecha.setText("0");
		list.clearSelection();
		save.setEnabled(true);
		numero.setEnabled(true);
	}

	private void searchAction() {
		try {
			Calibracion filter = new Calibracion();
			filter.setNumero(searchNumero.getText());
			controller.search(filter);
		} catch (Exception ex) {
			showError(ex.getMessage());
		}
	}

	private void handleListClick() {
		int row = list.getSelectedRow();
		controller.edit(row);
	}

	public void showError(String message) {
		JOptionPane.showMessageDialog(panel, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void setModel(Model model) {
		this.model = model;
		model.addObserver(this);
	}

	@Override
	public void update(Observable updatedModel, Object properties) {
		int changedProps = (int) properties;
		if ((changedProps & Model.LIST) == Model.LIST) {
			int[] cols = {TableModel.NUMERO, TableModel.FECHA, TableModel.MEDICIONES};
			list.setModel(new TableModel(cols, model.getList()));
			list.setRowHeight(30);
			TableColumnModel columnModel = list.getColumnModel();
			columnModel.getColumn(2).setPreferredWidth(200);
		}
		if ((changedProps & Model.CURRENT) == Model.CURRENT) {
			Calibracion currentCalibracion = model.getCurrent();
			numero.setText(String.valueOf(currentCalibracion.getNumero()));
			fecha.setText(currentCalibracion.getFecha().toString());
			mediciones.setText(String.valueOf(currentCalibracion.getNumeroDeMediciones()));

			boolean enableEdit = currentCalibracion.getNumero().isEmpty() || model.getList().isEmpty();
			save.setEnabled(enableEdit);
			numero.setEnabled(enableEdit);
		}
		panel.revalidate();
	}

	public void setInstrumentoSeleccionado(Instrumento instrumento) {
		if (instrumento == null) {
			instrumentoLbl.setText("No hay ningún instrumento seleccionado");
			instrumentoSeleccionado = null;
		} else {
			String labelText = String.format("%s - %s (%s - %s)",
					instrumento.getSerie(), instrumento.getDescripcion(), instrumento.getMinimo(), instrumento.getMaximo());
			instrumentoLbl.setText(labelText);
			instrumentoSeleccionado = instrumento;
		}
	}

	public void highlightEmptyField(String fieldName) {
		switch (fieldName) {
			case "numero":
				numero.requestFocus();
				break;
			case "mediciones":
				mediciones.requestFocus();
				break;
			case "fecha":
				fecha.requestFocus();
				break;
		}
	}

	public String getNumero() {
		return numero.getText();
	}

	public String getMediciones() {
		return mediciones.getText();
	}

	public String getFecha() {
		return fecha.getText();
	}
}
