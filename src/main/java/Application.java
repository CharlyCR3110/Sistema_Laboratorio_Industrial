import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.Mediator;
import una.instrumentos.logic.Service;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Application {
	private static JFrame window;
	private static JTabbedPane tabbedPane;
	private static Instrumento instrumentoSeleccionado; // Se usa en la ventana de calibraciones
	private static una.instrumentos.presentation.tipos.Controller tiposController;
	private static una.instrumentos.presentation.instrumentos.Controller instrumentosController;
	private static una.instrumentos.presentation.calibraciones.Controller calibracionesController;

	private static Mediator mediator;

	public static void main(String[] args) {
		Service service = Service.instance();
		setLookAndFeel();

		initializeComponents();
		setupControllers();
		setupTabs();
		setupWindow();
		addTabChangeListeners();
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void initializeComponents() {
		window = new JFrame();
		tabbedPane = new JTabbedPane();
		window.setContentPane(tabbedPane);
	}

	private static void setupControllers() {
		tiposController = new una.instrumentos.presentation.tipos.Controller(
				new una.instrumentos.presentation.tipos.View(),
				new una.instrumentos.presentation.tipos.Model()
		);

		instrumentosController = new una.instrumentos.presentation.instrumentos.Controller(
				new una.instrumentos.presentation.instrumentos.View(),
				new una.instrumentos.presentation.instrumentos.Model()
		);

		calibracionesController = new una.instrumentos.presentation.calibraciones.Controller(
				new una.instrumentos.presentation.calibraciones.View(),
				new una.instrumentos.presentation.calibraciones.Model()
		);

		mediator = new Mediator(instrumentosController, calibracionesController);
	}

	private static void setupTabs() {
		tabbedPane.addTab("Tipos de Instrumento", tiposController.getView().getPanel());
		tabbedPane.addTab("Instrumentos", instrumentosController.getView().getPanel());
		tabbedPane.addTab("Calibraciones", calibracionesController.getView().getPanel());
		tabbedPane.addTab("Acerca de", new una.instrumentos.presentation.acercaDe.View().getPanel());
	}

	private static void addTabChangeListeners () {
		tabbedPane.addChangeListener(createTabChangeListener());
		tabbedPane.addChangeListener(createTipoInstrumentoChangeListener());
	}

	private static ChangeListener createTabChangeListener() {
		return new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = tabbedPane.getSelectedIndex();
				if (selectedIndex == 2) { // Índice 2 corresponde a la pestaña de Calibraciones
					tabbedPane.setSelectedIndex(selectedIndex);
					mediator.setInstrumentoSeleccionado();
				}
			}
		};
	}

	// change listener para cargar los tipos de instrumento en el combo box de la ventana de instrumentos
	public static ChangeListener createTipoInstrumentoChangeListener() {
		return new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = tabbedPane.getSelectedIndex();
				if (selectedIndex == 1) { // Índice 1 corresponde a la pestaña de Instrumentos
					tabbedPane.setSelectedIndex(selectedIndex);
					System.out.println("Cargando tipos de instrumento");
					instrumentosController.getView().setTipos();
				}
			}
		};
	}

	private static void setupWindow() {
		window.setSize(900, 400);
		window.setResizable(true);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setIconImage(new ImageIcon(Application.class.getResource("icon.png")).getImage());
		window.setTitle("SILAB: Sistema de Laboratorio Industrial");
		window.setVisible(true);
	}

}
