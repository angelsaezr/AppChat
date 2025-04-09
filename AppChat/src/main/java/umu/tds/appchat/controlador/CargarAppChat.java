package umu.tds.appchat.controlador;

import java.awt.EventQueue;
import umu.tds.appchat.vista.VentanaLogin;

/**
 * Clase para lanzar la aplicacion.
 * 
 * @author Ángel
 * @author Francisco Javier
 */
public class CargarAppChat {
	
	public static void main(String[] args) {
		
	    System.out.println("Fin de la carga de datos");
	    
	    /**
	     * Inicia la aplicación en el hilo de despacho de eventos de AWT.
	     * Se encarga de crear y mostrar la ventana de login de forma segura en el hilo principal de la interfaz gráfica.
	     */
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                VentanaLogin frame = new VentanaLogin();
	                frame.setVisible(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    });

	}

}
