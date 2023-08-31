//import una.instrumentos.presentation.tipos.Controller;
//import una.instrumentos.presentation.tipos.Model;
//import una.instrumentos.presentation.tipos.View;


import una.instrumentos.logic.Instrumento;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Application {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ex) {};

		window = new JFrame();
		JTabbedPane tabbedPane = new JTabbedPane();
		window.setContentPane(tabbedPane);

		// Se usa el nombre completo de las clases para evitar conflictos con otras clases que se llamen igual
		// VIEWS
		una.instrumentos.presentation.tipos.Model tiposModel= new una.instrumentos.presentation.tipos.Model();
		una.instrumentos.presentation.tipos.View tiposView = new una.instrumentos.presentation.tipos.View();

		una.instrumentos.presentation.instrumentos.Model instrumentosModel= new una.instrumentos.presentation.instrumentos.Model();
		una.instrumentos.presentation.instrumentos.View instrumentosView = new una.instrumentos.presentation.instrumentos.View();

		una.instrumentos.presentation.calibraciones.Model calibracionesModel= new una.instrumentos.presentation.calibraciones.Model();
		una.instrumentos.presentation.calibraciones.View calibracionesView = new una.instrumentos.presentation.calibraciones.View();

		una.instrumentos.presentation.acercaDe.View acercaDeView = new una.instrumentos.presentation.acercaDe.View();

		// CONTROLLERS
		tiposController = new una.instrumentos.presentation.tipos.Controller(tiposView,tiposModel);
		instrumentosController = new una.instrumentos.presentation.instrumentos.Controller(instrumentosView,instrumentosModel);
		calibracionesController = new una.instrumentos.presentation.calibraciones.Controller(calibracionesView,calibracionesModel);

		// Se agregan los paneles al tabbed pane (la pestanita de arriba y su contenido)
		window.getContentPane().add("Tipos de Instrumento",tiposView.getPanel());
		window.getContentPane().add("Instrumentos",instrumentosView.getPanel());
		window.getContentPane().add("Calibraciones",calibracionesView.getPanel());
		window.getContentPane().add("Acerca de",acercaDeView.getPanel());

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = tabbedPane.getSelectedIndex();
				if (selectedIndex == 2) { // Índice 2 corresponde a la pestaña de Calibraciones
					// Si se cambió a la pestaña de Calibraciones, se mantiene activa
					tabbedPane.setSelectedIndex(selectedIndex);
					// debug
					System.out.println("Calibraciones");
					// obtener el Instrumento seleccionado en la pestaña de Instrumentos
					instrumentoSeleccionado = instrumentosController.getSelected();
					// debug
					if (instrumentoSeleccionado == null) {
						System.out.println("instrumento seleccionado es null");
						calibracionesView.setInstrumentoSeleccionado(null);
					} else {
						System.out.println(instrumentoSeleccionado.getDescripcion());
						calibracionesView.setInstrumentoSeleccionado(instrumentoSeleccionado);
					}
				}
			}
		});

		// Configuracion de la ventana
		window.setSize(900, 400);
		window.setResizable(true);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setIconImage((new ImageIcon(Application.class.getResource("icon.png"))).getImage());
		window.setTitle("SILAB: Sistema de Laboratorio Industrial");
		window.setVisible(true);
	}
	public static una.instrumentos.presentation.tipos.Controller tiposController;
	public static una.instrumentos.presentation.instrumentos.Controller instrumentosController;
	public static una.instrumentos.presentation.calibraciones.Controller calibracionesController;
	public static Instrumento instrumentoSeleccionado;	// Se usa en la ventana de calibraciones
	public static JFrame window;
}
