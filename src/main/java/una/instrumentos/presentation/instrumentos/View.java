package una.instrumentos.presentation.instrumentos;

import una.instrumentos.logic.Instrumento;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
//		save.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				saveAction();
//			}
//		});
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
	private void clearAction() {
		// Se limpian los campos (o sea, se ponen en blanco)
		serie.setText("");
		descripcion.setText("");
		minimo.setText("0");
		maximo.setText("0");
		tolerancia.setText("0");
		tipo.setSelectedIndex(0);
		// Ademas de limpiar los campos, se deselecciona la lista
		list.clearSelection();
		// Reactivar el boton de guardar
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
			int[] cols = {TableModel.SERIE, TableModel.DESCRIPCION, TableModel.MINIMO};
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
			// Se deshabilitan el boton Guardar y el campo de codigo
			if (model.getCurrent().getSerie().equals("")) {
				save.setEnabled(true);
				serie.setEnabled(true);
			} else {
				save.setEnabled(false);
				serie.setEnabled(false);
			}
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
		}
	}

	// metodos para obtener los valores de los campos de texto
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
	public String getTipo() {
		return tipo.getSelectedItem().toString();
	}

}
