package una.instrumentos.presentation.acercaDe;

import javax.swing.*;
import java.awt.*;

public class View {
	public View() {
		agregarElTextoAcercaDe();
	}
	private JPanel panel;
	private JLabel acercaDeLabel;

	// Metodo para agregar la informacion necesaria en la pestana de acerca de, se utiliza HTML para darle formato (MUY BASICO)
	private void agregarElTextoAcercaDe() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<h1>Sylab: Sistema de Laboratorios</h1>");
		sb.append("<p>Version 1.0</p>");
		sb.append("<p>Copyright (C) 2023</p>");
		sb.append("<p>Desarrollado por: Carlos Gonzalez Garita</p>");
		sb.append("<h1>Contacto</h1>");
		sb.append("<p>Correo: carlosgarita3110@gmail.com</p>");
		sb.append("<p>LinkedIn: <a href=\"https://www.linkedin.com/in/carlos-gonzalez-a69839200/\">Carlos Gonzalez Garita</a></p>");
		sb.append("</html>");
		acercaDeLabel.setText(sb.toString());
	}

	public Component getPanel() {
		return panel;
	}
}
