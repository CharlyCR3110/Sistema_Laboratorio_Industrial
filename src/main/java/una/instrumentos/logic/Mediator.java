package una.instrumentos.logic;

public class Mediator {
	private una.instrumentos.presentation.instrumentos.Controller instrumentosController;
	private una.instrumentos.presentation.calibraciones.Controller calibracionesController;
	public Mediator(una.instrumentos.presentation.instrumentos.Controller instrumentosController, una.instrumentos.presentation.calibraciones.Controller calibracionesController) {
		this.instrumentosController = instrumentosController;
		this.calibracionesController = calibracionesController;
	}

	public void setInstrumentoSeleccionado() {
		Instrumento instrumentoSeleccionado = instrumentosController.getSelected();
		calibracionesController.instrumentoSeleccionadoCambiado(instrumentoSeleccionado);
	}

}
