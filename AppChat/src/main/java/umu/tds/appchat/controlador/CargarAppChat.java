package umu.tds.appchat.controlador;

import java.awt.EventQueue;
import java.time.LocalDate;

import umu.tds.appchat.dominio.TipoDescuento;
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
		/*AppChat.getInstance().registrarUsuario("javi", "1", "1", LocalDate.now(), "src/main/resources/profile1.jpg", "hola", "javi@a.com");
		AppChat.getInstance().login("1", "1");
		AppChat.getInstance().activarPremium(TipoDescuento.FECHA);
		AppChat.getInstance().anularPremium();*/
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
