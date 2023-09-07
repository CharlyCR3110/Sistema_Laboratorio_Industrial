package una.instrumentos.presentation.instrumentos;

import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.TipoInstrumento;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
	private JPanel panel;
	private JTextField searchDescripcion;
	private JButton search;
	private JButton save;
	private JTable list;
	private JButton delete;
	private JLabel searchDescripcionLbl;
	private JButton report;
	private JTextField serie;
	private JTextField nombre;
	private JTextField descripcion;
	private JLabel serieLbl;
	private JLabel minimoLbl;
	private JLabel descripcionLbl;
	private JButton clear;
	private JButton edit;
	private JComboBox tipo;
	private JLabel maximoLbl;
	private JTextField maximo;
	private JLabel toleranciaLbl;
	private JTextField tolerancia;
	private JLabel tipoLbl;
	private JTextField minimo;

	public View() {
		initializeUI();
		setupEventHandlers();
		initializeButtonStates();
	}

	private void initializeUI() {
		list.getTableHeader().setReorderingAllowed(false);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		initializeButtonStates();
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
				controller.handleSaveAction(serie.getText(), descripcion.getText(), Integer.parseInt(minimo.getText()), Integer.parseInt(maximo.getText()), Integer.parseInt(maximo.getText()), tipo.getSelectedItem().toString());
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
		int row = list.getSelectedRow();
		try {
			Instrumento instrumento = model.getList().get(row);
			controller.edit(instrumento);
		} catch (IndexOutOfBoundsException e) {
			showErrorMessageBox("Debe seleccionar un elemento de la lista");
		}
		clearAction();
	}

	private void deleteAction() {
		try {
			int row = list.getSelectedRow();
			Instrumento instrumento = model.getList().get(row);
			controller.delete(instrumento);
			clearAction();
		} catch (IndexOutOfBoundsException ex) {
			showErrorMessageBox("Debe seleccionar un elemento de la lista");
		} catch (Exception ex) {
			showErrorMessageBox(ex.getMessage());
		}
	}

	private void clearAction() {
		serie.setText("");
		descripcion.setText("");
		minimo.setText("0");
		maximo.setText("0");
		tolerancia.setText("0");
		tipo.setSelectedIndex(0);
		list.clearSelection();
		save.setEnabled(true);
		serie.setEnabled(true);
		System.out.println("clearAction");
	}

	private void searchAction() {
		try {
			Instrumento filter = new Instrumento();
			filter.setDescripcion(searchDescripcion.getText());
			controller.search(filter);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(panel, ex.getMessage(), "Informacion", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void handleListClick() {
		int row = list.getSelectedRow();
		controller.edit(row);
	}

	private void showErrorMessageBox(String message) {
		JOptionPane.showMessageDialog(panel, message, "Informacion", JOptionPane.INFORMATION_MESSAGE);
	}

	public JPanel getPanel() {
		return panel;
	}

	Controller controller;
	Model model;

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
			int[] cols = {TableModel.SERIE, TableModel.DESCRIPCION, TableModel.MINIMO, TableModel.MAXIMO, TableModel.TOLERANCIA, TableModel.TIPO};
			list.setModel(new TableModel(cols, model.getList()));
			list.setRowHeight(30);
			TableColumnModel columnModel = list.getColumnModel();
			columnModel.getColumn(2).setPreferredWidth(200);
		}
		if ((changedProps & Model.CURRENT) == Model.CURRENT) {
			serie.setText(model.getCurrent().getSerie());
			descripcion.setText(model.getCurrent().getDescripcion());
			minimo.setText(String.valueOf(model.getCurrent().getMinimo()));
			maximo.setText(String.valueOf(model.getCurrent().getMaximo()));
			tolerancia.setText(String.valueOf(model.getCurrent().getTolerancia()));
			tipo.setSelectedItem(model.getCurrent().getTipo());
		}
		this.panel.revalidate();
	}

	public void showError(String message) {
		JOptionPane.showMessageDialog(panel, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void highlightEmptyField(String fieldName) {
		switch (fieldName) {
			case "serie":
				serie.requestFocus();
				break;
			case "descripcion":
				descripcion.requestFocus();
				break;
			case "minimo":
				minimo.requestFocus();
				break;
			case "maximo":
				maximo.requestFocus();
				break;
			case "tolerancia":
				tolerancia.requestFocus();
				break;
			case "tipo":
				tipo.requestFocus();
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
		serie.setEnabled(selectedRowCount == 0);
	}

	private List<TipoInstrumento> getTipos() {
		return controller.getTipos();
	}

	public void setTipos() {
		List<TipoInstrumento> tipos = getTipos();
		tipo.removeAllItems();
		tipo.addItem("");
		for (TipoInstrumento tipo : tipos) {
			this.tipo.addItem(tipo.getNombre());
		}
	}

	public String getSerie() {
		return serie.getText();
	}

	public String getDescripcion() {
		return descripcion.getText();
	}

	public String getMinimo() {
		return minimo.getText();
	}

	public String getMaximo() {
		return maximo.getText();
	}

	public String getTolerancia() {
		return tolerancia.getText();
	}

	public TipoInstrumento getTipoSeleccionado() {
		return controller.getTipoSeleccionado(tipo.getSelectedItem().toString());
	}

	private Instrumento createInstrumentoFromView() {
		Instrumento instrumento = new Instrumento();
		instrumento.setSerie(serie.getText());
		instrumento.setDescripcion(descripcion.getText());
		instrumento.setMinimo(Integer.parseInt(minimo.getText()));
		instrumento.setMaximo(Integer.parseInt(maximo.getText()));
		instrumento.setTolerancia(Integer.parseInt(tolerancia.getText()));
		instrumento.setTipo(getTipoSeleccionado());
		return instrumento;
	}

	public int getSelectedRow() {
		return list.getSelectedRow();
	}
}