package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.Medicion;
import una.utiles.Utiles;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

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
	private JTable medicionesList;
	private JScrollPane medicionesListContainer;

	private Controller controller;
	private Model model;
	private Instrumento instrumentoSeleccionado;

	public View() {
		initializeUI();
		setupEventHandlers();
		initializeButtonStates();
	}

	private void initializeUI() {
		medicionesListContainer.setVisible(false);
		list.getTableHeader().setReorderingAllowed(false);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		medicionesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	private void setupEventHandlers() {
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
				if (save.isEnabled()) {
					saveAction();
				}
			}
		});
		delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.handleDeleteAction(list.getSelectedRow());
			}
		});
		edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				editAction();
			}
		});
		report.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				generateReport();
			}
		});
		list.getSelectionModel().addListSelectionListener(e -> {
			updateDeleteButtonState();
			updateEditButtonState();
			updateSaveState();
		});
	}

	private void initializeButtonStates() {
		updateDeleteButtonState();
		updateEditButtonState();
		updateSaveState();
	}

	private void generateReport() {
		controller.generateReport();
	}

	private void editAction() {
		try {
			Calibracion calibracion = model.getCurrent();
			// recuperar la medicion seleccionada
			int row = medicionesList.getSelectedRow();
			if (row == -1) {
				showError("Debe seleccionar una medición");
				return;
			}
			Medicion medicion = calibracion.getMediciones().get(row);
			System.out.println(medicion.getReferencia());
			controller.edit(calibracion, medicion);
			clearAction();
		} catch (Exception ex) {
			showError(ex.getMessage());
		}
	}

	private void saveAction() {
		try {
			Calibracion calibracion = new Calibracion();
			String numeroCalibracion = this.generateNumero();
			this.numero.setText(numeroCalibracion);
			calibracion.setNumero(numeroCalibracion);
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
		numero.setEnabled(false);
		medicionesListContainer.setVisible(false);
	}

	private void searchAction() {
		try {
			Calibracion filter = new Calibracion();
			String searchTerm = searchNumero.getText();

			if (instrumentoSeleccionado == null) {
				// Si el instrumento seleccionado es NULL, muestra la tabla vacía
				model.setList(new ArrayList<>());
			} else if (!searchTerm.isEmpty()) {
				// Si se ingresó un término de búsqueda, filtra las calibraciones por instrumento y término
				filter.setInstrumento(instrumentoSeleccionado);
				filter.setNumero(searchTerm);
				controller.search(filter);
			} else {
				// Si no se ingresó un término de búsqueda, muestra todas las calibraciones del instrumento
				filter.setInstrumento(instrumentoSeleccionado);
				controller.search(filter);
			}
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

	public void update(Observable updatedModel, Object properties) {
		int changedProps = (int) properties;

		if ((changedProps & Model.LIST) == Model.LIST) {
			updateCalibracionList();
		}

		if ((changedProps & Model.CURRENT) == Model.CURRENT) {
			updateCurrentCalibracion();
		}

		panel.revalidate();
	}

	private void updateCalibracionList() {
		int[] cols = {TableModel.NUMERO, TableModel.FECHA, TableModel.MEDICIONES};
		list.setModel(new TableModel(cols, model.getList()));
		list.setRowHeight(30);
		TableColumnModel columnModel = list.getColumnModel();
		columnModel.getColumn(2).setPreferredWidth(200);
	}

	private void updateCurrentCalibracion() {
		Calibracion currentCalibracion = model.getCurrent();
		numero.setText(String.valueOf(currentCalibracion.getNumero()));
		fecha.setText(currentCalibracion.getFecha().toString());
		mediciones.setText(String.valueOf(currentCalibracion.getNumeroDeMediciones()));

		boolean enableEdit = currentCalibracion.getNumero().isEmpty() || model.getList().isEmpty();
		numero.setEnabled(false);

		int[] cols = {MedicionesTableModel.NUMERO, MedicionesTableModel.REFERENCIA, MedicionesTableModel.MEDICION};
		List<Boolean> editables = Arrays.asList(false, true, true);
		medicionesList.setModel(new MedicionesTableModel(cols, currentCalibracion.getMediciones(), editables));
		medicionesList.setRowHeight(30);
		TableColumnModel columnModel = medicionesList.getColumnModel();
		columnModel.getColumn(2).setPreferredWidth(200);
		medicionesListContainer.setVisible(!enableEdit);
	}

	public void setInstrumentoSeleccionado(Instrumento instrumento) {
		try {
			instrumentoSeleccionado = instrumento;
			Calibracion filter = new Calibracion();
			String searchTerm = searchNumero.getText();

			if (instrumentoSeleccionado == null) {
				// Si el instrumento seleccionado es NULL, muestra la tabla vacía
				controller.setList(new ArrayList<>());	// este metodo llama al setList del model
				System.out.println("instrumento seleccionado es null");
			} else if (!searchTerm.isEmpty()) {
				// Si se ingresó un término de búsqueda, filtra las calibraciones por instrumento y término
				filter.setInstrumento(instrumentoSeleccionado);
				filter.setNumero(searchTerm);
				controller.search(filter);
				System.out.println("instrumento seleccionado no es null, pero se ingreso un termino de busqueda");
			} else {
				// Si no se ingresó un término de búsqueda, muestra todas las calibraciones del instrumento
				filter.setInstrumento(instrumentoSeleccionado);
				controller.search(filter);
				System.out.println("instrumento seleccionado no es null, y no se ingreso un termino de busqueda");
			}
		} catch (Exception ex) {
			showError(ex.getMessage());
		}
	}

	public void mostrarInformacionInstrumento(Instrumento instrumento) {
		if (instrumento == null) {
			instrumentoLbl.setText("No hay ningún instrumento seleccionado");
		} else {
			String labelText = String.format("%s - %s (%s - %s)",
					instrumento.getSerie(), instrumento.getDescripcion(), instrumento.getMinimo(), instrumento.getMaximo());
			instrumentoLbl.setText(labelText);
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


	private void updateDeleteButtonState() {
		int selectedRowCount = list.getSelectedRowCount();
		delete.setEnabled(selectedRowCount > 0);
	}

	private void updateEditButtonState() {
		int selectedRowCount = list.getSelectedRowCount();
		edit.setEnabled(selectedRowCount > 0);
	}

	private void updateSaveState() {
		int selectedRowCount = list.getSelectedRowCount();
		save.setEnabled(selectedRowCount == 0);
	}

	// Generar número de calibración aleatorio
	private String generateNumero() {
		return String.valueOf((int) (Math.random() * 1000000));
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
