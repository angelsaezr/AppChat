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
	
	/**
	 * Método principal de la aplicación.
	 * 
	 * Inicia la aplicación en el hilo de despacho de eventos de AWT,
	 * encargándose de mostrar de forma segura la ventana de login {@link VentanaLogin}
	 * en el hilo principal de la interfaz gráfica.
	 * 
	 * @param args argumentos de línea de comandos (no se utilizan)
	 */
	public static void main(String[] args) {
		
	    System.out.println("Fin de la carga de datos");
	    
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
