package una.instrumentos.presentation.tipos;

import una.instrumentos.logic.TipoInstrumento;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
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
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deleteAction();
            }
        });
    }
    private void deleteAction() {
        try {
            int row = list.getSelectedRow();
            TipoInstrumento tipoInstrumento = model.getList().get(row);
            controller.delete(tipoInstrumento);
            clearAction();
        } catch (Exception ex) {
            showErrorMessageBox(ex.getMessage());
        }
    }
    private void saveAction() {
        try {
            TipoInstrumento tipoInstrumento = new TipoInstrumento();
            tipoInstrumento.setCodigo(codigo.getText());
            tipoInstrumento.setNombre(nombre.getText());
            tipoInstrumento.setUnidad(unidad.getText());
            controller.save(tipoInstrumento);
            clearAction();
        } catch (Exception ex) {
            showErrorMessageBox(ex.getMessage());
        }
    }
    private void clearAction() {
        // Se limpian los campos (o sea, se ponen en blanco)
        codigo.setText("");
        nombre.setText("");
        unidad.setText("");
        // Ademas de limpiar los campos, se deselecciona la lista
        list.clearSelection();
    }
    private void searchAction() {
        try {
            TipoInstrumento filter = new TipoInstrumento();
            filter.setNombre(searchNombre.getText());
            controller.search(filter);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel, ex.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void handleListClick() {
        int row = list.getSelectedRow();
        controller.edit(row);
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
}
