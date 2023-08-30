//import una.instrumentos.presentation.tipos.Controller;
//import una.instrumentos.presentation.tipos.Model;
//import una.instrumentos.presentation.tipos.View;


import javax.swing.*;

public class Application {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ex) {};

		window = new JFrame();
		window.setContentPane(new JTabbedPane());

		// Se usa el nombre completo de las clases para evitar conflictos con otras clases que se llamen igual
		// VIEWS
		una.instrumentos.presentation.tipos.Model tiposModel= new una.instrumentos.presentation.tipos.Model();
		una.instrumentos.presentation.tipos.View tiposView = new una.instrumentos.presentation.tipos.View();

		una.instrumentos.presentation.instrumentos.Model instrumentosModel= new una.instrumentos.presentation.instrumentos.Model();
		una.instrumentos.presentation.instrumentos.View instrumentosView = new una.instrumentos.presentation.instrumentos.View();

		una.instrumentos.presentation.acercaDe.View acercaDeView = new una.instrumentos.presentation.acercaDe.View();

		// CONTROLLERS
		tiposController = new una.instrumentos.presentation.tipos.Controller(tiposView,tiposModel);
		instrumentosController = new una.instrumentos.presentation.instrumentos.Controller(instrumentosView,instrumentosModel);


		// Se agrega el panel de tipos de instrumento al tabbed pane (la pestanita de arriba y su contenido)
		window.getContentPane().add("Tipos de Instrumento",tiposView.getPanel());
		// Se agrega el panel de instrumentos al tabbed pane (la pestanita de arriba y su contenido)
		window.getContentPane().add("Instrumentos",instrumentosView.getPanel());
		// Se agrega el panel de acerca de al tabbed pane (la pestanita de arriba y su contenido)
		window.getContentPane().add("Acerca de",acercaDeView.getPanel());
		window.setSize(900, 400);
		window.setResizable(true);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setIconImage((new ImageIcon(Application.class.getResource("icon.png"))).getImage());
		window.setTitle("SILAB: Sistema de Laboratorio Industrial");
		window.setVisible(true);
	}
	public static una.instrumentos.presentation.tipos.Controller tiposController;
	public static una.instrumentos.presentation.instrumentos.Controller instrumentosController;
	public static JFrame window;
}
