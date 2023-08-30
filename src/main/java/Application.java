import una.instrumentos.presentation.tipos.Controller;
import una.instrumentos.presentation.tipos.Model;
import una.instrumentos.presentation.tipos.View;

import javax.swing.*;

public class Application {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ex) {};

		window = new JFrame();
		window.setContentPane(new JTabbedPane());

		Model tiposModel= new Model();
		View tiposView = new View();
		tiposController = new Controller(tiposView,tiposModel);

		// Se agrega el panel de tipos de instrumento al tabbed pane (la pestanita de arriba y su contenido)
		window.getContentPane().add("Tipos de Instrumento",tiposView.getPanel());
		window.setSize(900, 400);
		window.setResizable(true);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setIconImage((new ImageIcon(Application.class.getResource("icon.png"))).getImage());
		window.setTitle("SILAB: Sistema de Laboratorio Industrial");
		window.setVisible(true);
	}
	public static Controller tiposController;
	public static JFrame window;
}
