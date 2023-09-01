import una.instrumentos.logic.Instrumento;
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

	public static void main(String[] args) {
		Service service = Service.instance();
		setLookAndFeel();

		initializeComponents();
		setupControllers();
		loadData();
		setupTabs();
		setupWindow();
		tabbedPane.addChangeListener(createTabChangeListener());


		// Guarda los datos en archivos XML
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				saveData();
			}
		}));
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
	}

	private static void setupTabs() {
		tabbedPane.addTab("Tipos de Instrumento", tiposController.getView().getPanel());
		tabbedPane.addTab("Instrumentos", instrumentosController.getView().getPanel());
		tabbedPane.addTab("Calibraciones", calibracionesController.getView().getPanel());
		tabbedPane.addTab("Acerca de", new una.instrumentos.presentation.acercaDe.View().getPanel());
	}

	private static ChangeListener createTabChangeListener() {
		return new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = tabbedPane.getSelectedIndex();
				if (selectedIndex == 2) { // Índice 2 corresponde a la pestaña de Calibraciones
					tabbedPane.setSelectedIndex(selectedIndex);
					instrumentoSeleccionado = instrumentosController.getSelected();
					calibracionesController.getView().setInstrumentoSeleccionado(instrumentoSeleccionado);
				}
			}
		};
	}

	// Guarda los datos en archivos XML
	private static void saveData() {
		try {
			una.utiles.XMLDataManager.saveToXML(tiposController.getModel().getList(), "src/main/java/una/xmlFiles/tipos.xml");
			una.utiles.XMLDataManager.saveToXML(instrumentosController.getModel().getList(), "src/main/java/una/xmlFiles/instrumentos.xml");
			una.utiles.XMLDataManager.saveToXML(calibracionesController.getModel().getList(), "src/main/java/una/xmlFiles/calibraciones.xml");
			System.out.println("Datos guardados");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void loadData() {
		try {
			tiposController.getModel().loadList(una.utiles.XMLDataManager.loadFromXML("src/main/java/una/xmlFiles/tipos.xml"));
			instrumentosController.getModel().loadList(una.utiles.XMLDataManager.loadFromXML("src/main/java/una/xmlFiles/instrumentos.xml"));
			calibracionesController.getModel().loadList(una.utiles.XMLDataManager.loadFromXML("src/main/java/una/xmlFiles/calibraciones.xml"));
			System.out.println("Datos cargados");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
