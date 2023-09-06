package una.instrumentos.presentation.tipos;

import una.instrumentos.logic.TipoInstrumento;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
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
        list.getTableHeader().setReorderingAllowed(false);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
            TipoInstrumento tipoInstrumento = model.getList().get(row);
            controller.edit(tipoInstrumento);
        } catch (IndexOutOfBoundsException e) {
            showErrorMessageBox("Debe seleccionar un elemento de la lista");
        }
        clearAction();
    }

    private void deleteAction() {
        try {
            int row = list.getSelectedRow();
            TipoInstrumento tipoInstrumento = model.getList().get(row);
            controller.delete(tipoInstrumento);
            clearAction();
        } catch (IndexOutOfBoundsException e) {
            showErrorMessageBox("Debe seleccionar un elemento de la lista");
        } catch (Exception e) {
            showErrorMessageBox(e.getMessage());
        }
    }

    private void saveAction() {
        try {
            TipoInstrumento tipoInstrumento = new TipoInstrumento();
            tipoInstrumento.setCodigo(codigo.getText());
            tipoInstrumento.setNombre(nombre.getText());
            tipoInstrumento.setUnidad(unidad.getText());
            if (controller.save(tipoInstrumento) == 0) {
                return;
            }
            clearAction();
        } catch (Exception ex) {
            showErrorMessageBox(ex.getMessage());
        }
    }

    private void clearAction() {
        codigo.setText("");
        nombre.setText("");
        unidad.setText("");
        list.clearSelection();
        save.setEnabled(true);
        codigo.setEnabled(true);
    }

    private void searchAction() {
        try {
            TipoInstrumento filter = new TipoInstrumento();
            filter.setNombre(searchNombre.getText());
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
