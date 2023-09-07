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

	Controller controller;
	Model model;

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
		search.addActionListener(e -> controller.handleSearchAction(searchDescripcion.getText()) );
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.edit(list.getSelectedRow());
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
				controller.handleDeleteAction(list.getSelectedRow());
			}
		});
		edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.handleEditAction(list.getSelectedRow());
			}
		});
		report.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.generateReport();
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

	public void clearAction() {
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
			tipo.setSelectedItem(model.getCurrent().getTipo().getNombre());
		}
		this.panel.revalidate();
	}

	public void showError(String message) {
		JOptionPane.showMessageDialog(panel, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(panel, message, "Informacion", JOptionPane.INFORMATION_MESSAGE);
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

	public int getSelectedRow() {
		return list.getSelectedRow();
	}
}