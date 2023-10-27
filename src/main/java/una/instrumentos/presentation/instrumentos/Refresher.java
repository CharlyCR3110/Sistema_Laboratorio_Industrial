package una.instrumentos.presentation.instrumentos;

import javax.swing.SwingUtilities;

public class Refresher {

	una.instrumentos.presentation.instrumentos.Controller controller;

	public Refresher(Controller controller) {
		this.controller = controller;
	}

	private Thread hilo;
	private boolean condition=false;
	public void start(){
		Runnable task = new Runnable(){
			public void run(){
				while(condition){
					try { Thread.sleep(5000);}
					catch (InterruptedException ex) {}
					refresh();
				}
			}
		};
		hilo = new Thread(task);
		condition=true;
		hilo.start();
	}
	public void stop(){
		condition=false;
	}
	private void refresh(){
		SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						controller.refresh();
					}
				}
		);
	}
}
