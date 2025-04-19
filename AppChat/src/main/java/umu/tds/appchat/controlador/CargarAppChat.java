package umu.tds.appchat.controlador;

import java.awt.EventQueue;
import java.time.LocalDate;

import umu.tds.appchat.dominio.ContactoIndividual;
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
		AppChat.getInstance().registrarUsuario("javi", "1", "1", LocalDate.now(), "src/main/resources/profile1.jpg", "hola", "javi@a.com");
		AppChat.getInstance().login("1", "1");
		AppChat.getInstance().activarPremium(TipoDescuento.FECHA);
		AppChat.getInstance().anularPremium();
		
		AppChat.getInstance().registrarUsuario("angel", "2", "2", LocalDate.now(), "src/main/resources/profile1.jpg", "hola", "angel@a.com");
		AppChat.getInstance().login("2", "2");
		
		AppChat.getInstance().registrarUsuario("jesus", "3", "3", LocalDate.now(), "src/main/resources/profile1.jpg", "hola", "jesus@a.com");
		AppChat.getInstance().login("3", "3");
		
		ContactoIndividual c1 = AppChat.getInstance().agregarContactoIndividual("javi", "1");
		ContactoIndividual c2 = AppChat.getInstance().agregarContactoIndividual("angel", "2");
		AppChat.getInstance().enviarMensajeContacto(c1, "hola", -1);
		AppChat.getInstance().enviarMensajeContacto(c1, "que tal", -1);
		AppChat.getInstance().enviarMensajeContacto(c1, "", 2);
		
		AppChat.getInstance().enviarMensajeContacto(c2, "hola", -1);
		AppChat.getInstance().enviarMensajeContacto(c2, "que tal", -1);
		AppChat.getInstance().enviarMensajeContacto(c2, "", 2);
		
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
