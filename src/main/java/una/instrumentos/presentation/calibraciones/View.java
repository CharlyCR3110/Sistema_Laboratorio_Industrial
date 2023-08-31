package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.utiles.Utiles;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
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

	public View() {
		// Para que no se pueda editar la tabla
		list.getTableHeader().setReorderingAllowed(false);
		// Eventos
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
//		delete.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				deleteAction();
//			}
//		});
//		edit.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				editAction();
//			}
//		});
	}
//	private void editAction() {
//		// Se obtiene el elemento seleccionado de la lista
//		int row = list.getSelectedRow();
//		try {
//			Calibracion calibracion = model.getList().get(row);
//			controller.edit(tipoInstrumento);
//		} catch (IndexOutOfBoundsException e) {
//			showErrorMessageBox("Debe seleccionar un elemento de la lista");
//		}
//		// Se llama al controlador para editar el elemento
//		clearAction();
//	}
//
//	private void deleteAction() {
//		try {
//			int row = list.getSelectedRow();
//			TipoInstrumento tipoInstrumento = model.getList().get(row);
//			controller.delete(tipoInstrumento);
//			clearAction();
//		} catch (IndexOutOfBoundsException e) {
//			showErrorMessageBox("Debe seleccionar un elemento de la lista");
//		}
//	}
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
			JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	private void clearAction() {
		// Se limpian los campos (o sea, se ponen en blanco)
		numero.setText("");
		mediciones.setText("0");
		fecha.setText("0");
		// Ademas de limpiar los campos, se deselecciona la lista
		list.clearSelection();
		// Reactivar el boton de guardar
		save.setEnabled(true);
		numero.setEnabled(true);
	}
	private void searchAction() {
		try {
			Calibracion filter = new Calibracion();
			filter.setNumero(searchNumero.getText());
			controller.search(filter);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	private void handleListClick() {
		int row = list.getSelectedRow();
		controller.edit(row);   // Se envia la fila seleccionada al controlador
	}
	private void showErrorMessageBox(String message) {
		JOptionPane.showMessageDialog(panel, message, "Información", JOptionPane.INFORMATION_MESSAGE);
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
			System.out.println("LIST: " + model.getList().size());
			list.setModel(new TableModel(cols, model.getList()));
			System.out.println("DEBU1G");
			for (Calibracion el : model.getList()) {
				System.out.println("DEBUG");
				System.out.println(el.getNumero());
			}

			list.setRowHeight(30);
			TableColumnModel columnModel = list.getColumnModel();
			columnModel.getColumn(2).setPreferredWidth(200);
		}
		if ((changedProps & Model.CURRENT) == Model.CURRENT) {
			numero.setText(String.valueOf(model.getCurrent().getNumero()));
			fecha.setText(model.getCurrent().getFecha().toString());
			mediciones.setText(model.getCurrent().getNumeroDeMediciones().toString());
			// Se deshabilitan el boton Guardar y el campo de codigo
			if (model.getCurrent().getNumero().equals("") || model.getList().isEmpty()) {
				save.setEnabled(true);
				numero.setEnabled(true);
			} else {
				save.setEnabled(false);
				numero.setEnabled(false);
			}
		}
		this.panel.revalidate();
	}
	public void showError(String message) {
		JOptionPane.showMessageDialog(panel, message, "Error", JOptionPane.ERROR_MESSAGE);
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

	// metodos para obtener los valores de los campos de texto
	public String getNumero() {
		return numero.getText();
	}
	public String getMediciones() {
		return mediciones.getText();
	}
	public String getFecha() {
		return fecha.getText();
	}
	public void setInstrumentoSeleccionado(Instrumento instrumento) {
		if (instrumento == null) {
			this.instrumentoLbl.setText("No hay ningun instrumento seleccionado");
			this.instrumentoSeleccionado = null;	// para evitar que se modifique otro instrumento
			return;
		}
		this.instrumentoLbl.setText(String.format("%s - %s (%s - %s)", instrumento.getSerie(), instrumento.getDescripcion(), instrumento.getMinimo(), instrumento.getMaximo()));
		instrumentoSeleccionado = instrumento;
	}

	Controller controller;
	Model model;
	Instrumento instrumentoSeleccionado;
}
