package una.instrumentos.presentation.tipos;

import una.instrumentos.logic.TipoInstrumento;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    private JPanel panel;
    private JTextField searchNombre;
    private JButton search;
    private JButton save;
    private JTable list;
    private JButton delete;
    private JLabel searchNombreLbl;
    private JButton report;
    private JTextField codigo;
    private JTextField nombre;
    private JTextField unidad;
    private JLabel codigoLbl;
    private JLabel nombreLbl;
    private JLabel unidadLbl;
    private JButton clear;
    private JButton edit;

    public View() {
        initializeUI();
        setupEventHandlers();
        initializeButtonStates();
    }

    private void initializeUI() {
        if (list == null) {
            list = new JTable();
        }
        list.getTableHeader().setReorderingAllowed(false);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupEventHandlers() {
        search.addActionListener(e -> controller.handleSearchAction(searchNombre.getText()));
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
                controller.handleSaveAction(codigo.getText(), nombre.getText(), unidad.getText());
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

    private void clearAction() {
        codigo.setText("");
        nombre.setText("");
        unidad.setText("");
        list.clearSelection();
        save.setEnabled(true);
        codigo.setEnabled(true);
    }

    private void handleListClick() {
        int row = list.getSelectedRow();
        controller.edit(row);
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
            int[] cols = {TableModel.CODIGO, TableModel.NOMBRE, TableModel.UNIDAD};
            list.setModel(new TableModel(cols, model.getList()));
            list.setRowHeight(30);
            TableColumnModel columnModel = list.getColumnModel();
            columnModel.getColumn(2).setPreferredWidth(200);
        }
        if ((changedProps & Model.CURRENT) == Model.CURRENT) {
            codigo.setText(model.getCurrent().getCodigo());
            nombre.setText(model.getCurrent().getNombre());
            unidad.setText(model.getCurrent().getUnidad());
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
            case "codigo":
                codigo.requestFocus();
                break;
            case "nombre":
                nombre.requestFocus();
                break;
            case "unidad":
                unidad.requestFocus();
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
        codigo.setEnabled(selectedRowCount == 0);
    }

    public String getCodigo() {
        return codigo.getText();
    }

    public String getNombre() {
        return nombre.getText();
    }

    public String getUnidad() {
        return unidad.getText();
    }
}
